package com.ls.util;

import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ObjectsFactory
{
	public static Object newInstance(Class<?> theClass)
	{
		Method newInstance;
		try
		{
			newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
			newInstance.setAccessible(true);
			Object result =  newInstance.invoke(null, theClass, Object.class);
			return result;
		} catch (NoSuchMethodException e)
		{			
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{			
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{			
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
//	public static Object newInstance(Class<?> theClass)
//	{
//		Objenesis objenesis = new ObjenesisStd();
//		ObjectInstantiator instantiator = objenesis.getInstantiatorOf(theClass);
//		Object result = instantiator.newInstance();
//		Log.d("ObjectsFactory","Created instance:"+result.toString());
//		return result;
//	}
}
