
import java.io.IOException;

public class UdpServerTest {
	public static void main(String[] args) {
		
		UdpServer server = new UdpServer(9000);
		try {
			server.start();
		} catch (IOException e) {
			System.out.println("Invalid door");
		}
	}
}
