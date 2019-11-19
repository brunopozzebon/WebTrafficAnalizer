
import java.io.IOException;
import java.net.UnknownHostException;

public class UdpClientTest {
	public static void main(String[] args) {
		boolean withIperf=false;
		if(args.length>0) {
			withIperf=true;
		}
		UdpClient udpClient;
		try {
			udpClient = new UdpClient("localhost",9000,withIperf);
			udpClient.start();
		} catch (UnknownHostException e) {
			System.out.println("Invalid host");
		} catch (IOException e) {
			System.out.println("Probem opening file");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
