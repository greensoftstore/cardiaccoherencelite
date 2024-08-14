package com.martinforget;

public class Chrono {
	private static Chrono   chrono;
	private boolean running = false;
	private boolean initial = true;
	private long basetime = 0;
	private long lastpause = 0;
	
	private Chrono(){

	        //ToDo here

	}
	public static Chrono getInstance()
	{
	    if (chrono == null)
	   {
	    	chrono = new Chrono();
	   }
	   return chrono;
	   }
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean started) {
		this.running = started;
	}
	public long getBasetime() {
		return basetime;
	}
	public void setBasetime(long basetime) {
		this.basetime = basetime;
	}
	public long getLastpause() {
		return lastpause;
	}
	public void setLastpause(long lastpause) {
		this.lastpause = lastpause;
	}
	public boolean isInitial() {
		return initial;
	}
	public void setInitial(boolean initial) {
		this.initial = initial;
		if (initial)
		{
			this.lastpause = 0;
			this.running = false;
		}
	}
}
