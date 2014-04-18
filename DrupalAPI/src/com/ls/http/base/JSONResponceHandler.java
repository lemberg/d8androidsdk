package com.ls.http.base;

import com.google.gson.Gson;

class JSONResponceHandler extends ResponceHandler
{
	
	protected Object itemFromResponce(String json, Class theClass)
	{				
		Object result = null;		
		if(theClass != null)
		{
			Gson gson = new Gson();
			result = gson.fromJson(json, theClass);
		}
		return result;
	}	
}
