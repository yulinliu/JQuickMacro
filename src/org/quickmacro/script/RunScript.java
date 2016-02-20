package org.quickmacro.script;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.quickmacro.EventThread;
import org.quickmacro.EventThreads;
import org.quickmacro.Robot;
import org.xml.sax.SAXException;

public class RunScript {

	private static Logger logger = Logger.getLogger(EventThread.class);
	
	/**
	 * @param args
	 * @throws AWTException 
	 */
	public static void main(String[] args) throws AWTException {
		
		try {
			String scriptfile = "./script/default.xml";
			if(args.length > 0){
				scriptfile = args[0];
			}else{
				System.out.println("Please Set Script File Path...  default '"+scriptfile+"'");
				InputStreamReader converter = new InputStreamReader(System.in);
				BufferedReader in = new BufferedReader(converter);
				String line = in.readLine();
				if(!line.equals("")){
					scriptfile = line;
				}
			}
			
			EventThreads threads = XmlParse.parse(scriptfile);
			
			Robot bot = new Robot();
			bot.setAutoDelay(threads.getEventDelay());
			bot.setSingleWindow(threads.isSingleWindow());
			bot.setScreen(threads.getSingleCheck());

			logger.info((((double) threads.getStartWait()) / 1000)+" 秒後執行.... "+scriptfile);
			bot.delay(threads.getStartWait());
			
			List threadList = threads.getThread();
			for(int i=0;i<threadList.size();i++){
				EventThread thread = (EventThread) threadList.get(i);
				thread.setBot(bot);
				thread.start();
				
				try {
					Thread.sleep(1500);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(threads.getExitTime() > 0){
				try {
					logger.info((((double) threads.getExitTime()) / 1000)+" 秒後關閉程式....");
					Thread.sleep(threads.getExitTime());
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					System.exit(0);
				}
			}
			
		} catch (IllegalArgumentException e) {
			logger.error(e);
			
		} catch (SecurityException e) {
			logger.error(e);
			
		} catch (IOException e) {
			logger.error(e);
			
		} catch (ClassNotFoundException e) {
			logger.error(e);
			
		} catch (IllegalAccessException e) {
			logger.error(e);
			
		} catch (NoSuchFieldException e) {
			logger.error(e);
			
		} catch (ParserConfigurationException e) {
			logger.error(e);
			
		} catch (SAXException e) {
			logger.error(e);
		}
		
	}

}
