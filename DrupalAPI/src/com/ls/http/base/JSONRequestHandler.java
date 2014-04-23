package com.ls.http.base;

import org.eclipse.jdt.annotation.NonNull;

import com.google.gson.Gson;


class JSONRequestHandler extends RequestHandler
{
	
	public JSONRequestHandler(@NonNull Object theObject)
	{
		super(theObject);		
	}

	@Override
	public String stringBodyFromItem()
	{
		if(implementsPostableInterface())
		{
			IPostableItem item = (IPostableItem)this.object;
			return item.toJsonString();
		}else{
			Gson gson = SharedGson.getGson();
			return gson.toJson(this.object);
		}
	}
}
