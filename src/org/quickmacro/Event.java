package org.quickmacro;

public class Event {

	private String name;
	private Object[] param;
	private Class[] paramClass;
	
	public Class[] getParamClass() {
		return paramClass;
	}

	public void setParamClass(Class[] paramClass) {
		this.paramClass = paramClass;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Object[] getParam() {
		return param;
	}
	
	public void setParam(Object[] param) {
		this.param = param;
	}
	
}
