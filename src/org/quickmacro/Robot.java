package org.quickmacro;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.log4j.Logger;

public class Robot extends java.awt.Robot {
	
	private Logger logger = Logger.getLogger(EventThread.class);
	
	private boolean isSingleWindow = false;
	private List screen;

	public List getScreen() {
		return screen;
	}

	public void setScreen(List screen) {
		this.screen = screen;
	}

	public boolean isSingleWindow() {
		return isSingleWindow;
	}
	
	public void setSingleWindow(boolean isSingleWindow) {
		this.isSingleWindow = isSingleWindow;
	}
	
	public synchronized void delay(int ms) {
		try {
			EventThread.sleep(ms);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	public Robot() throws AWTException {
		super();
	}

	public synchronized void keyClick(int keycode){
		this.keyPress(keycode);
		this.keyRelease(keycode);
	}
	
	public synchronized void mouseClick(int buttons){
		this.mousePress(buttons);
		this.mouseRelease(buttons);
	}
	
	public boolean checkWindow(){
		if(!isSingleWindow){
			return true;
		}
		
		boolean isSingle = false;		
		int size = screen.size();
		
		for(int i=0;i<size;i++){
			ScreenCapture check = (ScreenCapture) screen.get(i);
			
			BufferedImage image = check.getImage();
			BufferedImage capture = this.createScreenCapture(new Rectangle(check.getX(),check.getY(),check.getWidth(),check.getHeight()));

			
			for(int x=0;x<check.getWidth();x++){
				for(int y=0;y<check.getHeight();y++){
					if(image.getRGB(x, y) == capture.getRGB(x, y)){
						isSingle = true;
						
					}else{
						logger.debug("ScreenCapture Pixel x="+x + ",y=" + y + " Failure");
						return false;
					}
				}
			}
		}
		
		return isSingle;
	}
}
