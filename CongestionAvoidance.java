
public class CongestionAvoidance implements WindowIncrease {
	private int windowSize;
	private int ssthreshhold;
	
	public CongestionAvoidance(int windowSize, int sstreshhold) {
		this.windowSize = windowSize;
		this.ssthreshhold = sstreshhold;
	}
	
	@Override
	public WindowIncrease increase() {
		this.windowSize*=1.01;
		return this;
	}

	@Override
	public WindowIncrease decrease() {
		this.ssthreshhold=this.windowSize/2;
		this.windowSize/=2;
		return new SlowStart(this.windowSize,this.ssthreshhold);
	}

	@Override
	public int getWindowSize() {
		return this.windowSize;
	}
}
