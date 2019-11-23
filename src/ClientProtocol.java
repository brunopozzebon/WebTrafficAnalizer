import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientProtocol {
	
	protected String FILE_NAME = "../list.txt";
	
	protected String server;
	protected int door;
	protected WindowIncrease windowIncrease;

	protected InetAddress IPAddres;
	protected Timer timer;
	protected Auxiliar auxiliar;
	protected int sequence;
	protected int totalLineNumber;

	protected int requestedSequence;
	protected int losses;

	protected int inicialTimer;
	
	public ClientProtocol(String hostName, int door, boolean withIperf, String type) throws UnknownHostException {
		this.server = hostName;
		this.door=door;
		this.IPAddres = InetAddress.getByName(server);
		this.windowIncrease = new SlowStart(1,Integer.MAX_VALUE);
		this.auxiliar = new Auxiliar(type,withIperf);
		this.timer = new Timer();
		this.losses = 0;
		this.sequence = 0;
		this.inicialTimer = System.currentTimeMillis();
		this.requestedSequence=-1;
		this.totalLineNumber = auxiliar.getNumberOfLines(FILE_NAME);
	}
	
	protected void decrease() {
		System.out.println("Some package loss happen");
		losses++;
		timer.incrementTimer();
		windowIncrease = windowIncrease.decrease();
	}
	
	protected int getThatShoudGo(int atual) {
		int linesThatINeedToGoBack = requestedSequence-sequence;    	
    	linesThatINeedToGoBack= linesThatINeedToGoBack +1%totalLineNumber ;
    	if(linesThatINeedToGoBack>atual) {
    		return  totalLineNumber+(atual-linesThatINeedToGoBack);
    	}else {
    		return atual-linesThatINeedToGoBack;	
    	}	
	}
	
	protected void verifyServerLog(String responseSentence) {
		 responseSentence.trim();
		if(responseSentence.startsWith("ER")){
             requestedSequence = Integer.parseInt(responseSentence.split(";")[1]);
             System.out.println("ERRADO");
         }
	}
}
