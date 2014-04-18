package com.ls.http.base;

import com.google.gson.Gson;


public class PostableItem implements IPostableItem{
		
	/**
	 * Method can be overridden by subclasses in order to define correct charset, used in order to post data to server.
	 * @return
	 */
	public String getCharset()
	{
		return URF_8;
	}
	
	public String toJsonString()
	{
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public String toXMLString()
	{
		//TODO implement in future
		return null;
	}	
}
