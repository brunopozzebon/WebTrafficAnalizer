import java.io.IOException;

public class TcpServerTest {

	public static void main(String[] args) {
		TcpServer server = new TcpServer(9000);
		try {
			server.start();
		} catch (IOException e) {
			System.out.println("Invalid door");
		}

	}

}
