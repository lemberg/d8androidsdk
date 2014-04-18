package com.ls.http.base;



public interface IPostableItem {
	
	public final static String URF_8 = "utf-8"; 
			
	/**
	 * Method can be overridden by subclasses in order to define correct charsret, used in order to post data to server.
	 * @return
	 */
	public String getCharset();
	
	public String toJsonString();
	
	public String toXMLString();
}
