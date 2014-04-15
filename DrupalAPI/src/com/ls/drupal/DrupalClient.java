package com.ls.drupal;

public class DrupalClient {

	private String baseURL;
	
	public DrupalClient(String theBaseURL) {
		this.baseURL = theBaseURL;
	}
	
	public DrupalClient(String theBaseURL,String theUserName) {
		this(theBaseURL);
		
	}
	
	public boolean restoreSession()
	{
		return true;
	}
	
	public final void login(final String userName, final String password)
	{
		
	}
	
	public final void logout()
	{
		
	}
}
