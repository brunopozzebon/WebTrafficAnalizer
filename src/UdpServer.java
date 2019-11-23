import java.io.*;
import java.net.*;

class UdpServer {
	private int door;
	private int sequence;
	private DatagramSocket serverSocket;

	public UdpServer(int door) {

		this.door = door;
		this.sequence = 0;
	}

	public void start() throws IOException {
		
		//This will close the socket when process ends
		Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
				serverSocket.close();
            }
        });
		
		serverSocket = new DatagramSocket(door);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];

		System.out.println("Listening at door "+door);
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			serverSocket.receive(receivePacket);

			 /* There is a small aplication protocol that control the order or losses of the packages,
             *  Actually, the server will receive a sequence number from the client, if it�s what server is 
             *  waiting for, the server sends a OK;With the received sentence with upper case letters, 
             *  otherwise, the server sends a message with ER; with the sequence that the server need.
             *  All packages out of order will be discarted, so this is like a go-back-n algorithm
             */
			String sentence = new String(receivePacket.getData()).trim();
			int indexOfFirstSemiColon = sentence.indexOf(";");
			int seqNumber = Integer.parseInt(sentence.substring(0,indexOfFirstSemiColon).split("_")[1]);

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();

          
			if(seqNumber==sequence){
			    sequence++;
                capitalizedSentence = "OK;"+capitalizedSentence;
                sendData = capitalizedSentence.getBytes();
				
                DatagramPacket sendPacket = new DatagramPacket(sendData,capitalizedSentence.length(), IPAddress, port);
                serverSocket.send(sendPacket);
            }else{
            	 System.out.println("Wrong datagram, send me "+sequence);
			    String problem = "ER;"+sequence+";";
                sendData = problem.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,problem.length(), IPAddress, port);
                serverSocket.send(sendPacket);
            }

			receiveData = new byte[1024];
		}
	}
}