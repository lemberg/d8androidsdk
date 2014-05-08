package com.ls.sampleapp;

public class AppConstants
{
	public final static String SERVER_BASE_URL = "http://vh015.uk.dev-ls.co.uk/";
	
	public static enum CATEGORIE {ALL_POSTS("All Posts",null),INDUSTRY_NEWS("Industry News","1"),OUR_POSTS("Our Posts","2"),TECH_NOTES("Tech notes","3");
		
		public final String name;
		public final String id;
		CATEGORIE(String theName,String theId)
		{
			name = theName;
			this.id = theId;
		}
	}
}
