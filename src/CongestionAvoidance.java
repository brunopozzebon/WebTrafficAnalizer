
public class CongestionAvoidance implements WindowIncrease {
	private int windowSize;
	private int ssthreshhold;

	public CongestionAvoidance(int windowSize, int sstreshhold) {
		this.windowSize = windowSize;
		this.ssthreshhold = sstreshhold;
	}
	
	@Override
	public WindowIncrease increase() {
		if(windowSize==1){
			this.windowSize++;
		}else{
			this.windowSize+=10;
		}
		
		return this;
	}

	@Override
	public WindowIncrease decrease() {
		
		return new SlowStart(1,this.windowSize/2);
	
	}

	@Override
	public int getWindowSize() {
		return this.windowSize;
	}
}
