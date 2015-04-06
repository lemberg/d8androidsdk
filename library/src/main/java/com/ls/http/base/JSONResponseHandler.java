package com.ls.http.base;

import com.google.gson.Gson;

import com.ls.util.ObjectsFactory;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;

class JSONResponseHandler extends ResponseHandler
{

	public Object itemFromResponse(@NonNull String json,@NonNull Class<?> theClass)
	{
		Object result = createInstanceByInterface(json, theClass);
		if (result == null)
		{
			Gson gson = SharedGson.getGson();
			result = gson.fromJson(json, theClass);			
		}
		return result;
	}

	public Object itemFromResponse(@NonNull String json,@NonNull Type theType)
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

		if (IResponseItem.class.isAssignableFrom(theClass))
		{
			IResponseItem item;
			item = (IResponseItem) ObjectsFactory.newInstance(theClass);
			item.initWithJSON(json);
			result = item;
		}
		return result;
	}

}
