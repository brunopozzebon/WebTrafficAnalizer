import java.io.IOException;

public class TcpServerTest {

	public static void main(String[] args) throws NumberFormatException, ClassNotFoundException {
		TcpServer server = new TcpServer(9001);
		try {
			server.start();
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("Invalid door");
		}

	}

}
