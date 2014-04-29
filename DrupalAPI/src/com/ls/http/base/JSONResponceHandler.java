package com.ls.http.base;

import com.google.gson.Gson;
import com.ls.util.ObjectsFactory;

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
					IResponceItem item;
					item = (IResponceItem) ObjectsFactory.newInstance(theClass);
					item.initWithJSON(json);
					result = item;
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

	
}
