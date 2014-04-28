package com.ls.http.base;

import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

import com.google.gson.Gson;

class JSONResponceHandler implements IResponceHandler
{

	public Object itemFromResponce(String json, Class<?> theClass)
	{
//		Log.e("Response:", json);
		Object result = null;
		if (theClass != null)
		{
			if (IResponceItem.class.isAssignableFrom(theClass))
			{
				try
				{
					IResponceItem item;
					item = (IResponceItem) this.newInstance(theClass);
					item.initWithJSON(json);
					result = item;
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (InvocationTargetException e)
				{
					e.printStackTrace();
				} catch (NoSuchMethodException e)
				{
					e.printStackTrace();
				}

			} else
			{
				Gson gson = SharedGson.getGson();
				result = gson.fromJson(json, theClass);
			}
		}
		return result;
	}

	// private Object newInstance(Class<?> theClass)
	// {
	// final ReflectionFactory reflection =
	// ReflectionFactory.getReflectionFactory();
	// final Constructor<Object> constructor =
	// reflection.newConstructorForSerialization(
	// theClass, Object.class.getDeclaredConstructor(new Class[0]));
	// final Object o = constructor.newInstance(new Object[0]);
	//
	// return o;
	// }

	private Object newInstance(Class<?> theClass) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException
	{
		Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
		newInstance.setAccessible(true);
		return newInstance.invoke(null, theClass, Object.class);
	}
}
