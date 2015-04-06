package com.ls.http.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SharedGson
{
	static{
		init();
	}
	private static GsonBuilder builder;
	private static Gson gson ;
	
	private static void init()
	{
		builder = new GsonBuilder();
		
	}
	
	public static synchronized Gson getGson()
	{
		if(gson == null)
		{
			gson = builder.create();			
		}
		return gson;		
	}
	
	/**
	 * all following gson calls will use updated gson object.
	 */
	public static synchronized void performUpdates()
	{
		gson = null;
	}
	
	public static GsonBuilder getbuilder()
	{
		return builder;
	}
}
