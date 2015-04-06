package com.ls.http.base;

import com.google.gson.Gson;

import android.support.annotation.NonNull;


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
