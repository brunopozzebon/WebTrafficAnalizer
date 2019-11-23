import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.UnknownHostException;

public class TcpClientTest {
	public static void main(String[] args) throws IOException {
		boolean withIperf=false;
		if(args.length>0) {
			withIperf=true;
		}
		
		//Read IP of the server from ip.txt
		LineNumberReader arquivo = new LineNumberReader(new FileReader(";./ip.txt"));
		String IPServer= arquivo.readLine().trim();
		arquivo.close();
		
		System.out.println(IPServer);
		
		TcpClient client;
		try {
			client = new TcpClient(IPServer,9001,withIperf,"tcp");
			client.start();
		} catch (UnknownHostException e) {
			System.out.println("Invalid host");
		} catch (IOException e) {
			System.out.println("Probem opening file: "+ e.toString());
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
}
