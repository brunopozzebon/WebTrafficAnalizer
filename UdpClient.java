import java.io.*;
import java.net.*;

class UdpClient{
	private static String FILE_NAME = "./list.txt";
	private static int losses =0;
	
	private String server;
	private int door;
	private WindowIncrease windowIncrease;

	private InetAddress IPAddres;
	private Timer timer = new Timer();
	private Auxiliar auxiliar;

	private byte[] sendData = new byte[1024];
	private byte[] receiveData = new byte[1024];
	
	public UdpClient(String hostName, int door, boolean withIperf) throws UnknownHostException {
		this.server = hostName;
		this.door=door;
		this.IPAddres = InetAddress.getByName(server);
		this.windowIncrease = new SlowStart(1,Integer.MAX_VALUE);
		this.auxiliar = new Auxiliar("udp",withIperf);
	}
	
	public void start() throws IOException, InterruptedException {

		while(true) {
			BufferedReader arquivo = new BufferedReader(new FileReader(FILE_NAME));
			String sentence=arquivo.readLine();
			while (sentence != null) {
				try {
					System.out.println("\nWindow size at : "+windowIncrease.getWindowSize()+"\n");
					DatagramSocket clientSocket = new DatagramSocket();
					
					for(int i=0;i<windowIncrease.getWindowSize();i++) {
						if(i==0) {
							timer.begin();
						}
						sendData = sentence.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(sendData,sentence.length(), this.IPAddres, this.door);
						
						clientSocket.send(sendPacket);
						sentence="";
						sentence = arquivo.readLine();
						if(sentence==null) {
							arquivo.close();
							arquivo = new BufferedReader(new FileReader(FILE_NAME));
							sentence = arquivo.readLine();
						}
					}
					auxiliar.log(windowIncrease.getWindowSize(), losses, timer.getTimeOut());
					
					clientSocket.setSoTimeout((int) timer.getTimeOut());
					
					for (int i = 0; i < windowIncrease.getWindowSize(); i++) {
						DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
						receiveData = new byte[1024];
						clientSocket.receive(receivePacket);
						String modifiedSentence = new String(receivePacket.getData());
						
						if(i==0) {
							timer.finish();
						}
					}			
					//Thread.sleep(500);
				
					windowIncrease = windowIncrease.increase();
					
					clientSocket.close();
				}catch(SocketTimeoutException exception) {
					System.out.println("Some package loss happen");
					losses++;
					windowIncrease = windowIncrease.decrease();
				}
			}
			arquivo.close();
		}
	}
}