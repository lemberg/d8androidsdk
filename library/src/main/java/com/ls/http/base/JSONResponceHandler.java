package com.ls.http.base;

import com.google.gson.Gson;

import com.ls.util.ObjectsFactory;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

class JSONResponceHandler extends ResponceHandler
{

	public Object itemFromResponce(@NotNull String json,@NotNull Class<?> theClass)
	{
		Object result = createInstanceByInterface(json, theClass);
		if (result == null)
		{
			Gson gson = SharedGson.getGson();
			result = gson.fromJson(json, theClass);
		}
		return result;
	}

	public Object itemFromResponce(@NotNull String json,@NotNull Type theType)
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
