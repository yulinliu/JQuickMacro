package org.quickmacro;

import java.util.List;

public class EventThreads {

	private boolean singleWindow = false;
	private int eventDelay = 0;
	private int startWait = 0;
	private List singleCheck;
	private List thread;
	private int exitTime = -1;
	
	public boolean isSingleWindow() {
		return singleWindow;
	}
	
	public void setSingleWindow(boolean singleWindow) {
		this.singleWindow = singleWindow;
	}
	
	public int getEventDelay() {
		return eventDelay;
	}
	
	public void setEventDelay(int eventDelay) {
		this.eventDelay = eventDelay;
	}
	
	public int getStartWait() {
		return startWait;
	}
	
	public void setStartWait(int startWait) {
		this.startWait = startWait;
	}
	
	public List getSingleCheck() {
		return singleCheck;
	}
	
	public void setSingleCheck(List singleCheck) {
		this.singleCheck = singleCheck;
	}
	
	public List getThread() {
		return thread;
	}
	
	public void setThread(List thread) {
		this.thread = thread;
	}
	
	public int getExitTime() {
		return exitTime;
	}
	
	public void setExitTime(int exitTime) {
		this.exitTime = exitTime;
	}
}