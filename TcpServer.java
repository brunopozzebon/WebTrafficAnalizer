
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpServer {
	private int door;

	public TcpServer(int door) {
		this.door = door;
	}

	public void start() throws IOException {
		ServerSocket servidor = new ServerSocket(this.door);
        System.out.println("Porta 12345 aberta!");

        Socket cliente = servidor.accept();
      
        Scanner s = new Scanner(cliente.getInputStream());
        PrintStream saida = new PrintStream(cliente.getOutputStream());
        
        while (s.hasNextLine()) {
           String sentence = s.nextLine();
           System.out.println(sentence);
           saida.println(sentence);
        }

        s.close();
        servidor.close();
        cliente.close();
	}
	
}
