package com.ls.http.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SharedGson
{
	static{
		init();
	}
	private static Gson gson ;	
	
	private static void init()
	{
		GsonBuilder builder = new GsonBuilder();
//		builder.registerTypeAdapter(DrupalGetItemsArray.class, new JsonSerializer(){
//
//			@Override
//			public JsonElement serialize(Object arg0, Type arg1, JsonSerializationContext arg2)
//			{
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//		});
		gson = builder.create();
	}

	public static Gson getGson()
	{
		return gson;
	}	
}
