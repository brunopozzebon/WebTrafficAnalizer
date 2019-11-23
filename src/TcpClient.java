import java.io.FileReader;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TcpClient extends ClientProtocol{

	public TcpClient(String hostName, int door, boolean withIperf, String type) throws UnknownHostException {
		super(hostName,door,withIperf,type);
	}


	public void start() throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException {

		Socket client = new Socket(this.server, this.door);

		LineNumberReader file = new LineNumberReader(new FileReader(FILE_NAME));
		String sentence = file.readLine();

		PrintStream output = new PrintStream(client.getOutputStream());
		Scanner input = new Scanner(client.getInputStream());

		while(sequence<30000){

		}

		while (sentence != null) {

			//This work like the timeout and the SocketException, a timer to TCP
			Timer taskTimeOut = new Timer();
			taskTimeOut.schedule(new TimerTask() {
				public void run() {					
					decrease();       
				}
			}, timer.getTimeOut());

			for(int i=0;i<windowIncrease.getWindowSize();i++) {

				if(requestedSequence!=-1) {
					int atualLine = file.getLineNumber();
					file.close();
					int lineThatINeedToGo = getThatShoudGo(atualLine);
					file = auxiliar.readerInLine(lineThatINeedToGo, FILE_NAME);
					sentence = file.readLine();
					sequence=requestedSequence;
					requestedSequence=-1;
				}	

				if(i==0) {
					timer.begin();
				}

				sentence="SEQ_"+(sequence++)+";"+sentence;	
				Thread.sleep(1);
				output.println(sentence);

				sentence = file.readLine();
				if(sentence==null) {
					file.close();
					file = new LineNumberReader(new FileReader(FILE_NAME));
					sentence = file.readLine();
				}
			}				

			auxiliar.log(windowIncrease.getWindowSize(), losses, timer.getTimeOut());

			for (int i = 0; i < windowIncrease.getWindowSize(); i++) {
				String responseSentence = input.nextLine();
				verifyServerLog(responseSentence); 	
				if(i==0) {
					timer.finish();
				}
			}	
			taskTimeOut.cancel();	
			windowIncrease = windowIncrease.increase();

		}
		auxiliar.saveStructure();
		input.close();
		file.close();
		output.close();
		client.close();		
	}
}
