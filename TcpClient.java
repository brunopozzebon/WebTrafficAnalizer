import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TcpClient {
	private static  String FILE_NAME = "./list.txt";
	private static int losses =0;

	private int door;
	private String hostName;
	private WindowIncrease windowIncrease;
	private Auxiliar auxiliar;

	private Timer timer = new Timer();

	public TcpClient(String hostName, int door, boolean withIperf) {
		this.hostName = hostName;
		this.door=door;
		this.windowIncrease = new SlowStart(1,Integer.MAX_VALUE);
		this.auxiliar = new Auxiliar("tcp",withIperf);
	}


	public void start() throws UnknownHostException, IOException, InterruptedException {
		Socket client = new Socket(this.hostName, this.door);

		BufferedReader file = new BufferedReader(new FileReader(FILE_NAME));
		String sentence = file.readLine();

		PrintStream output = new PrintStream(client.getOutputStream());
		Scanner input = new Scanner(client.getInputStream());

		while (sentence != null) {
			try {
				for(int i=0;i<windowIncrease.getWindowSize();i++) {
					if(i==0) {
						timer.begin();
					}
					output.println(sentence);
					sentence = file.readLine();
					if(sentence==null) {
						file.close();
						file = new BufferedReader(new FileReader(FILE_NAME));
						sentence = file.readLine();
					}
				}
				
				client.setSoTimeout((int) timer.getTimeOut());
				
				auxiliar.log(windowIncrease.getWindowSize(), losses, timer.getTimeOut());
				
				for (int i = 0; i < windowIncrease.getWindowSize(); i++) {
					String response = input.nextLine();
					if(i==0) {
						timer.finish();
					}
				}	
				//Thread.sleep(1000);

				windowIncrease = windowIncrease.increase();
			}catch(SocketTimeoutException e) {
				System.out.println("Some package loss happen");
				losses++;
				windowIncrease = windowIncrease.increase();	
			}
		}
		input.close();
		file.close();
		output.close();
		client.close();
	}
}
