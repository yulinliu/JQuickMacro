package org.quickmacro.script;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.quickmacro.Robot;

public class InitSingleCheck {

	private static Logger logger = Logger.getLogger(InitSingleCheck.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
			
			Robot bot = new Robot();
			logger.info("5 秒後執行.... "+scriptfile);
			bot.delay(5000);
			
			//XmlParse.initSingleCheck(scriptfile, bot);
			
		} catch (AWTException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		
	}

}
