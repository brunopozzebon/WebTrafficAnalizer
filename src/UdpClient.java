import java.io.*;
import java.net.*;

class UdpClient extends ClientProtocol{

	private byte[] sendData = new byte[1024];
	private byte[] receiveData = new byte[1024];

	public UdpClient(String hostName, int door, boolean withIperf,String type) throws UnknownHostException {
		super(hostName,door,withIperf,type);
	}

	public void start() throws IOException, InterruptedException {
		
			LineNumberReader file = new LineNumberReader(new FileReader(FILE_NAME));
			String sentence=file.readLine();
			while (sentence != null && sequence<100000 ) {
				try {
					DatagramSocket clientSocket = new DatagramSocket();

					/*
					 * This loop sends datagrams according with the windows size.
					 * The first if statment control if my server is receiving the sequence in the right order,
					 * if it�s, the requestedSequence will be -1 and the client continuos sending normally, but if it�s not,
					 * the requestedSequence�ll receive the sequence that my server is waiting for, therefore 
					 * i need to return the lines, i send the information again
					 * 
					 * After that, I start to count the time and send the sentence to the server with the sequence;
					 */
					
					for(int i=0;i<windowIncrease.getWindowSize();i++) {
						if(requestedSequence!=-1) {	
							int atualLine = file.getLineNumber();
							file.close();
							int lineThatINeedToGo = getThatShoudGo(atualLine);

							file = auxiliar.readerInLine(lineThatINeedToGo, FILE_NAME);
							sentence = file.readLine();

							System.out.println("Sending "+requestedSequence+" again");
							sequence=requestedSequence;
							requestedSequence=-1;
						}

						if(i==0) 
							timer.begin();

						sentence="SEQ_"+(sequence++)+";"+sentence;
						sendData = sentence.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(sendData,sentence.length(), this.IPAddres, this.door);


						clientSocket.send(sendPacket);
						sentence="";
						sentence = file.readLine();
						
						//Reread the file, if it�ll end
						if(sentence==null) {
							file.close();
							file = new LineNumberReader(new FileReader(FILE_NAME));
							sentence = file.readLine();
						}
					}

					//This, print the log and save the log in a internal structure
					auxiliar.log(windowIncrease.getWindowSize(), losses, timer.getTimeOut(),sequence);

					
					clientSocket.setSoTimeout((int) timer.getTimeOut());

					/*
					 * This will receive the datagram from server, waiting just timer.getTimeOut() milliseconds, 
					 * If I�ll receive all datagrams, the window will increase, if I won�t, the window will decrease
					 */
					for (int i = 0; i < windowIncrease.getWindowSize(); i++) {
						DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
						receiveData = new byte[1024];
						clientSocket.receive(receivePacket);

						String responseSentence = new String(receivePacket.getData());

						//This set to the variable requestedSequence a number bigger than -1 
						// if the server dosn�t receive a package in order
						
						verifyServerLog(responseSentence); 

				
						if(i==0) 
							timer.finish(); //Update estimated time for time out
					}			

					windowIncrease = windowIncrease.increase();
					clientSocket.close();
				}catch(SocketTimeoutException exception) {
					decrease();
				}
			}
			long endTime = System.currentTimeMillis();
			long time = endTime - inicialTimer;
			auxiliar.saveStructure( time/ (long)1000);
			file.close();
		}
		
}