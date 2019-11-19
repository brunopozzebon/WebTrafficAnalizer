import java.io.*;
import java.net.*;

class UdpServer {
	private int door;

	public UdpServer(int door) {
		this.door = door;
	}

	public void start() throws IOException {
		DatagramSocket serverSocket = new DatagramSocket(door);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];

		System.out.println("Listening at door "+door);
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			serverSocket.receive(receivePacket);

			String sentence = new String(receivePacket.getData()).trim();

			InetAddress IPAddress = receivePacket.getAddress();

			int port = receivePacket.getPort();

			String capitalizedSentence = sentence.toUpperCase();
			sendData = capitalizedSentence.getBytes();
			System.out.println(capitalizedSentence);
			DatagramPacket sendPacket = new DatagramPacket(sendData,capitalizedSentence.length(), IPAddress, port);

			System.out.println(capitalizedSentence);

			serverSocket.send(sendPacket);
			receiveData = new byte[1024];
		}
	}
}