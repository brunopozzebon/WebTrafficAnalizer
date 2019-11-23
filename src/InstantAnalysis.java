
public class InstantAnalysis {
	private long timeout;
	private int windowSize;
	private int losses;
	private int atualSequence;
	
	public InstantAnalysis(long timeout,int windowSize, int losses, int sequence) {
		this.timeout = timeout;
		this.windowSize = windowSize;
		this.losses = losses;
		this.atualSequence = sequence;
	}
	
	@Override
	public String toString() {
		return timeout+"_"+windowSize+"_"+losses+"_"+atualSequence;
	}
}
