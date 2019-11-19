
public class SlowStart implements WindowIncrease{
	private int windowSize;
	private int ssthreshhold;
	
	public SlowStart(int windowSize, int sstreshhold) {
		this.windowSize = windowSize;
		this.ssthreshhold = sstreshhold;
	}

	@Override
	public WindowIncrease increase() {
		if(this.windowSize*2<this.ssthreshhold) {
			this.windowSize*=2;		
			return this;
		}else {
			return new CongestionAvoidance(this.windowSize, this.ssthreshhold);
		}
	}

	@Override
	public WindowIncrease decrease() {
		this.ssthreshhold=this.windowSize/2;
		this.windowSize=1;
		return this;
	}

	@Override
	public int getWindowSize() {
		return this.windowSize;
	}

	

}
