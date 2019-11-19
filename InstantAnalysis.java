
public class InstantAnalysis {
	private long timeout;
	private int windowSize;
	private int losses;
	
	public InstantAnalysis(long timeout,int windowSize, int losses) {
		this.timeout = timeout;
		this.windowSize = windowSize;
		this.losses = losses;
	}
	
	@Override
	public String toString() {
		return timeout+"_"+windowSize+"_"+losses;
	}
}
