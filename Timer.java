
public class Timer {

	private double ALPHA = 0.125;
	private double BETA = 0.25;
	private int DEVIATION_SCALAR = 4;
	
	private long timerBegin;
	private long timerEnd;
	
	private long estimatedTime = 1000;
	private long deviation = 1;
	
	private long timeOutInterval;
	
	public void begin() {
		this.timerBegin=System.currentTimeMillis();
	}
	
	public void finish() {
		this.timerEnd=System.currentTimeMillis();
		calculateTimerValue();
	}
	
	public long getTimeOut() {
		return this.timeOutInterval;
	}
	
	private void calculateTimerValue() {
		long difference = this.timerEnd-this.timerBegin;
		this.estimatedTime = (long) ((1-ALPHA)*this.estimatedTime + ALPHA*difference);
		this.deviation = (long) ((1-BETA)*this.deviation + BETA*Math.abs(difference-estimatedTime));
		this.timeOutInterval =  this.estimatedTime + DEVIATION_SCALAR*this.deviation;		
	}
	
	
}
