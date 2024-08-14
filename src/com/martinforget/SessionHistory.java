package com.martinforget;

public class SessionHistory {

	private String startTime;
	private String endTime;

	private long duration;

	private int cycles;

	private int ratio;

	private int heartBegin;

	private int heartEnd;

	public int getHeartEnd() {
		return heartEnd;
	}

	public void setHeartEnd(int heartEnd) {
		this.heartEnd = heartEnd;
	}



	public int getHeartBegin() {
		return heartBegin;
	}

	public void setHeartBegin(int heartBegin) {
		this.heartBegin = heartBegin;
	}


	public int getRatio() {

		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getCycles() {
		return cycles;
	}

	public void setCycles(int cycles) {
		this.cycles = cycles;
	}

public String toString(){
	
	
	return String.valueOf(startTime)+String.valueOf(endTime)+" "+String.valueOf(duration)+" "+String.valueOf(cycles)+" "+String.valueOf(ratio)+" "+String.valueOf(heartEnd);
}

}
