package com.ls.http.base;

import java.lang.reflect.Type;

import org.eclipse.jdt.annotation.NonNull;

import com.google.gson.Gson;
import com.ls.util.ObjectsFactory;

class JSONResponceHandler extends ResponceHandler
{

	public Object itemFromResponce(@NonNull String json,@NonNull Class<?> theClass)
	{
		Object result = createInstanceByInterface(json, theClass);
		if (result == null)
		{
			Gson gson = SharedGson.getGson();
			result = gson.fromJson(json, theClass);			
		}
		return result;
	}

	public Object itemFromResponce(@NonNull String json,@NonNull Type theType)
	{		
		Class<?> theClass = theType.getClass();

		Object result = createInstanceByInterface(json, theClass);
		if (result == null)
		{
			Gson gson = SharedGson.getGson();
			result = gson.fromJson(json, theType);			
		}
		return result;
	}

	private Object createInstanceByInterface(String json, Class<?> theClass)
	{
		Object result = null;

		if (IResponceItem.class.isAssignableFrom(theClass))
		{
			IResponceItem item;
			item = (IResponceItem) ObjectsFactory.newInstance(theClass);
			item.initWithJSON(json);
			result = item;
		}
		return result;
	}

}
