package org.quickmacro;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
import org.quickmacro.script.ScriptMapping;

public class EventThread extends java.lang.Thread {

	private Logger logger = Logger.getLogger(EventThread.class);
	
	private Robot bot;
	private List event;
	private boolean repeatedly = false;
	private int autodelay = 0;
	
	public void run() {
		
		try {
			while(true){
				
				runEvent();
				
				if(!repeatedly){
					break;
				}
				
				try {
					sleep(autodelay);
				} catch (InterruptedException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
					
		} catch (SecurityException e) {
			logger.error(this.getName() + " Error",e);
			
		} catch (IllegalArgumentException e) {
			logger.error(this.getName() + " Error",e);
			
		} catch (NoSuchMethodException e) {
			logger.error(this.getName() + " Error",e);
			
		} catch (IllegalAccessException e) {
			logger.error(this.getName() + " Error",e);
			
		} catch (InvocationTargetException e) {
			logger.error(this.getName() + " Error",e);
		}
		
	}

	public synchronized void runEvent() throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		if(bot.checkWindow()){
			logger.info(this.getName()+" Operation");
			ScriptMapping.eventMapping(bot, event);
			
		}else{
			logger.info(this.getName()+" Check Window Failure");
		}
	}
	
	public Robot getBot() {
		return bot;
	}


	public void setBot(Robot bot) {
		this.bot = bot;
	}


	public List getEvent() {
		return event;
	}


	public void setEvent(List event) {
		this.event = event;
	}


	public boolean isRepeatedly() {
		return repeatedly;
	}


	public void setRepeatedly(boolean repeatedly) {
		this.repeatedly = repeatedly;
	}


	public int getAutodelay() {
		return autodelay;
	}


	public void setAutodelay(int autodelay) {
		this.autodelay = autodelay;
	}	
}
