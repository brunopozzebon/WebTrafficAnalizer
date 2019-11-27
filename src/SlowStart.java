
public class SlowStart implements WindowIncrease{
	private int windowSize;
	private int ssthreshhold;
	
	public SlowStart(int windowSize, int sstreshhold) {
		this.windowSize = windowSize;
		this.ssthreshhold = sstreshhold;
	}

	@Override
	public WindowIncrease increase() {
		if(windowSize*2<ssthreshhold) {
			return new SlowStart(windowSize*=2,ssthreshhold);
		}else {
			return new CongestionAvoidance(windowSize, ssthreshhold);
		}
	}

	@Override
	public WindowIncrease decrease() {
		return new SlowStart(1,windowSize);
	}

	@Override
	public int getWindowSize() {
		return this.windowSize;
	}

	

}
