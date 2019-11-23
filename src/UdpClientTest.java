
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.UnknownHostException;

public class UdpClientTest {
	public static void main(String[] args) throws IOException {
		boolean withIperf=false;
		
		LineNumberReader arquivo = new LineNumberReader(new FileReader("../ip.txt"));
		String IPServer= arquivo.readLine().trim();
		arquivo.close();
		
		System.out.println(IPServer);
		
		if(args.length>0) {
			withIperf=true;
		}
		UdpClient udpClient;
		try {
			udpClient = new UdpClient(IPServer,9000,withIperf,"udp");
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
