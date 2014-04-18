package com.ls.drupal.login.session;


public class LoginSession
{		
	private String sessionId;
	private String token; 
	
	public String getSessionId()
	{
		return sessionId;
	}
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	
	public boolean isValid()
	{
		return sessionId != null && token != null;
	}
}
