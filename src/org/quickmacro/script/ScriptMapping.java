package org.quickmacro.script;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.quickmacro.Event;
import org.quickmacro.Robot;


public class ScriptMapping {
	
	public static void eventMapping(Robot bot,List event) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		int size = event.size();
		
		for(int i=0;i<size;i++){
			Event run = (Event) event.get(i);
			
			runMethod(bot,run);
		}
	}
	
	public static Object runMethod(Object obj,Event event) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Method runMethod = obj.getClass().getMethod(event.getName(), event.getParamClass());
		return runMethod.invoke(obj, event.getParam());
	}
	
	public static Object getField(Class cla,String name,Object obj) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException{
		return cla.getField(name).get(obj);
	}
}
