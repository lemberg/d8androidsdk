package com.ls.http.base;

import android.util.Log;

import com.google.gson.Gson;

class JSONResponceHandler extends ResponceHandler
{
	
	protected Object itemFromResponce(String json, Class theClass)
	{				
		Log.e("Response:",json);
		Object result = null;		
		if(theClass != null)
		{
			if(IResponceItem.class.isAssignableFrom(theClass))
			{				
				try
				{
					IResponceItem item;
					item = (IResponceItem)theClass.newInstance();
					item.initWithJSON(json);
					result = item;
				} catch (InstantiationException e)
				{					
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{					
					e.printStackTrace();
				}
				
			}else{
				Gson gson = SharedGson.getGson();				
				result = gson.fromJson(json, theClass);
			}
		}
		return result;
	}	
}
