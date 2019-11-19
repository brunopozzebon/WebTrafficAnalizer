import java.io.IOException;
import java.net.UnknownHostException;

public class TcpClientTest {
	public static void main(String[] args) {
		boolean withIperf=false;
		if(args.length==0) {
			withIperf=true;
		}
		
		TcpClient client;
		try {
			client = new TcpClient("localhost",9000,withIperf);
			client.start();
		} catch (UnknownHostException e) {
			System.out.println("Invalid host");
		} catch (IOException e) {
			System.out.println("Probem opening file");
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
}
