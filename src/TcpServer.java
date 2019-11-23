
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpServer {
	private int door;
	private int sequence;
	private ServerSocket entranceSocket;

	public TcpServer(int door) {
		this.door = door;
		this.sequence = 0;
	}

	public void start() throws IOException, NumberFormatException, ClassNotFoundException {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){				
				try {
					entranceSocket.close();
				} catch (IOException e) {						
					e.printStackTrace();
				}				
			}
		});

		entranceSocket = new ServerSocket(this.door);		
		System.out.println("Server running on port "+this.door);
		Socket cliente = entranceSocket.accept();

		Scanner input = new Scanner(cliente.getInputStream());
		PrintStream output = new PrintStream(cliente.getOutputStream());

		while (input.hasNextLine()) {
			String sentence = input.nextLine();

			int indexOfFirstSemiColon = sentence.indexOf(";");
			int seqNumber = Integer.parseInt(sentence.substring(0,indexOfFirstSemiColon).split("_")[1]);

			if(seqNumber==sequence){
				sequence++;
				sentence = "OK;"+sentence;
			}else{
				sentence = "ER;"+sequence+";";
			}

			output.println(sentence);
		}

		input.close();
		entranceSocket.close();
		cliente.close();
	}	
}
