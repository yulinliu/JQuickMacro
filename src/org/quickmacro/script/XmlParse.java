package org.quickmacro.script;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.quickmacro.Event;
import org.quickmacro.EventThread;
import org.quickmacro.EventThreads;
import org.quickmacro.Robot;
import org.quickmacro.ScreenCapture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParse {

	private static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static EventThreads parse(String scriptfile) throws IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ParserConfigurationException, SAXException{
		EventThreads threads = new EventThreads();
		
		DocumentBuilder parser = SAXBuilder(true);
        Document doc = parser.parse(scriptfile);

        Element doc_threads = (Element) doc.getElementsByTagName("Threads").item(0);
        String singleWindow = doc_threads.getAttribute("singleWindow");
        String eventDelay = doc_threads.getAttribute("eventDelay");
        String startWait = doc_threads.getAttribute("startWait");
        
        if(singleWindow != null && !singleWindow.equals("")){
        	threads.setSingleWindow(Boolean.parseBoolean(singleWindow));
        }
        if(eventDelay != null && !eventDelay.equals("")){
        	threads.setEventDelay((int) calcultionTime(eventDelay));
        }
        if(startWait != null && !startWait.equals("")){
        	threads.setStartWait((int) calcultionTime(startWait));
        }
        
        if(threads.isSingleWindow()){
        	NodeList screen_list = doc.getElementsByTagName("ScreenCapture");
            List singleCheck = new ArrayList();
    		for(int i=0;i<screen_list.getLength();i++){
    			Element screen_element = (Element) screen_list.item(i);
    			
    			ScreenCapture screen = new ScreenCapture();
    			String x = screen_element.getAttribute("x");
    			String y = screen_element.getAttribute("y");
    			String width = screen_element.getAttribute("width");
    			String height = screen_element.getAttribute("height");
    			String fileName = screen_element.getTextContent();

    			if(x != null && !x.equals("")){
    				screen.setX(calculationPositionX(x));
    	        }
    			if(y != null && !y.equals("")){
    				screen.setY(calculationPositionY(y));
    	        }
    			if(width != null && !width.equals("")){
    				screen.setWidth(calculationPositionX(width));
    	        }
    			if(height != null && !height.equals("")){
    				screen.setHeight(calculationPositionY(height));
    	        }
    			if(fileName != null && !fileName.equals("")){
    				screen.setImage(ImageIO.read(new File(fileName)));
    			}
    			
    			singleCheck.add(screen);
    		}
    		threads.setSingleCheck(singleCheck);
        }
		
		List thread = new ArrayList();
        NodeList thread_list = doc.getElementsByTagName("Thread");
        for(int i=0;i<thread_list.getLength();i++){
        	Element thread_element = (Element) thread_list.item(i);
			
        	EventThread j_thread = new EventThread();
        	String repeatedly = thread_element.getAttribute("repeatedly");
        	String threadName = thread_element.getAttribute("name");
        	String autodelay = thread_element.getAttribute("autodelay");
				
        	if(repeatedly != null && !repeatedly.equals("")){
        		j_thread.setRepeatedly(Boolean.parseBoolean(repeatedly));
        	}
        	if(threadName != null){
        		j_thread.setName(threadName);
        	}
        	if(autodelay != null && !autodelay.equals("")){
        		j_thread.setAutodelay((int) calcultionTime(autodelay));
        	}
			 
        	List eventList = new ArrayList();
        	NodeList eventChildren = thread_element.getElementsByTagName("event");
        	for(int j=0;j<eventChildren.getLength();j++){
        		Element event_element = (Element) eventChildren.item(j);
        		
        		Event event = new Event();
    			String eventName = event_element.getAttribute("name");
    			String eventValue = event_element.getTextContent();
    			
    			if(eventName != null && !eventName.equals("")){
    				event.setName(eventName);
    	        }
    			
    			if(eventName != null && eventName.equals("mouseMove")){
    				String[] pos = eventValue.split(",");
    				Object[] param = new Object[2];
    				param[0] = Integer.valueOf(calculationPositionX(pos[0]));
    				param[1] = Integer.valueOf(calculationPositionY(pos[1]));
    				
    				event.setParam(param);
    				event.setParamClass(new Class[]{int.class,int.class});
    				
    			}else if(eventName != null && eventName.equals("delay")){
    				event.setParam(new Object[]{Integer.valueOf((int) calcultionTime(eventValue))});
    				event.setParamClass(new Class[]{int.class});
    				
    			}else if(eventName != null && !eventName.equals("")){
    				Class cla;
    				if(eventName.startsWith("key")){
    					cla = Class.forName("java.awt.event.KeyEvent");
    					
    				}else if(eventName.equals("mouseWheel")){
    					cla = Class.forName("java.awt.event.MouseWheelEvent");
    					
    				}else{
    					cla = Class.forName("java.awt.event.InputEvent");
    				}

    				event.setParam(new Object[]{ScriptMapping.getField(cla, eventValue, int.class)});
    				event.setParamClass(new Class[]{int.class});
    			}
    			eventList.add(event);
        	}
        	j_thread.setEvent(eventList);
        	thread.add(j_thread);
        }
        threads.setThread(thread);
	
        Element exitTime = (Element) doc.getElementsByTagName("ExitTime").item(0);
		if(exitTime != null && !exitTime.getTextContent().trim().equals("")){
			long exit = calcultionTime(exitTime.getTextContent());
			if(exit > 0){
				threads.setExitTime((int) exit);
			}
        }
        
        return threads;
	}
	
	public static void initSingleCheck(String scriptfile,Robot bot) throws Exception{
		DocumentBuilder parser = SAXBuilder(true);
        Document doc = parser.parse(scriptfile);

        NodeList sing = doc.getElementsByTagName("ScreenCapture");
        for(int i=0;i<sing.getLength();i++){
        	Element screen_element = (Element) sing.item(i);

        	String x = screen_element.getAttribute("x");
			String y = screen_element.getAttribute("y");
			String width = screen_element.getAttribute("width");
			String height = screen_element.getAttribute("height");
			String fileName = screen_element.getTextContent();

			if(x == null || x.equals("")){
				throw new Exception("Attribute Value 'x' Not Set");
	        }
			if(y == null || y.equals("")){
				throw new Exception("Attribute Value 'y' Not Set");
	        }
			if(width == null || width.equals("")){
				throw new Exception("Attribute Value 'width' Not Set");
	        }
			if(height == null || height.equals("")){
				throw new Exception("Attribute Value 'height' Not Set");
	        }
			if(fileName == null || fileName.equals("")){
				fileName = "./screen/"+System.currentTimeMillis()+".png";
			}
			
			BufferedImage image = bot.createScreenCapture(new Rectangle(calculationPositionX(x) ,calculationPositionY(y) ,calculationPositionX(width) ,calculationPositionY(height)));
			File file =  new File(fileName);
			file.mkdirs();
						
			ImageIO.write(image, "png", file);
			
			screen_element.setTextContent(file.getAbsolutePath());
        }
        
        Transformer transformer =  newTransformer();
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doc.getDoctype().getSystemId());
         
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(scriptfile));
        transformer.transform(source, result);
	}
	
	private static DocumentBuilder SAXBuilder(boolean validation) throws ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(validation);
		DocumentBuilder parser = factory.newDocumentBuilder();
		
		ErrorHandler myErrorHandler = new ErrorHandler();
		parser.setErrorHandler(myErrorHandler);
		
		return parser;
	}
	
	private static Transformer newTransformer() throws TransformerConfigurationException{
		TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        return transformer;
	}
	
	private static String reChar(String temp){
		char[] ch = temp.toCharArray();
		StringBuffer sb = new StringBuffer();
		char c = '*';
		char a = ' ';
		
		for(int i=0;i<ch.length;i++){
			if(ch[i] == c){
				sb.append(",");
				
			}else if(ch[i] == a){
				
			}else{
				sb.append(ch[i]);
			}
		}
		
		return sb.toString();
	}
	
	public static long calcultionTime(String time){
		long c_time = 0;
		
		if(time.indexOf("*") != -1){
			String[] temp = reChar(time).split(",");
			for(int i=0;i<temp.length;i++){
				if(i == 0){
					c_time =  Long.parseLong(temp[i]);
					
				}else{
					c_time *=  Long.parseLong(temp[i]);
				}				
			}
			
		}else{
			c_time = Long.parseLong(time.trim());
		}
		
		return c_time;
	}
	
	public static int calculationPositionX(String x){
		if(x.indexOf("%") != -1){
			double pos = Double.parseDouble(reChar(x).replace("%", "")) / 100;
			return (int) (screen.width * pos);
			
		}else{
			return Integer.parseInt(reChar(x));
		}
	}
	
	public static int calculationPositionY(String y){
		if(y.indexOf("%") != -1){
			double pos = Double.parseDouble(reChar(y).replace("%", "")) / 100;
			return (int) (screen.height * pos);
			
		}else{
			return Integer.parseInt(reChar(y));
		}
	}
}
