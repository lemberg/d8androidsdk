package com.ls.util;

import java.lang.reflect.Type;

public class ClassManagementUtiles
{
	private static final String TYPE_NAME_PREFIX = "class ";
	 
	public static String getClassName(Type type) {
	    if (type==null) {
	        return "";
	    }
	    String className = type.toString();
	    if (className.startsWith(TYPE_NAME_PREFIX)) {
	        className = className.substring(TYPE_NAME_PREFIX.length());
	    }
	    return className;
	}
	 
	public static Class<?> getClass(Type type) 
	            throws ClassNotFoundException {
	    String className = getClassName(type);
	    if (className==null || className.length()==0) {
	        return null;
	    }
	    return Class.forName(className);
	}
}
