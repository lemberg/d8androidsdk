package com.ls.drupal.login.session;

import com.android.volley.RequestQueue;
import com.ls.drupal.login.ILoginManager;
import com.ls.http.base.BaseRequest;
import com.ls.http.base.BaseRequest.RequestFormat;
import com.ls.http.base.BaseRequest.RequestMethod;
import com.ls.http.base.ResponseData;

public class LoginSessionManager implements ILoginManager
{	
	private LoginSession session;
	private final String baseURL;
	private final RequestFormat format;
	
	private final String GET_SESSION = "/user/login";
	private final String GET_TOKEN = "/services/session/token";
	
	public LoginSessionManager(String theBaseURL,RequestFormat theFormat)
	{
		this.baseURL = theBaseURL;
		this.format = theFormat;
	}
	
	@Override
	public void login(String userName, String password, RequestQueue queue)
	{		
		LoginSession session = new LoginSession();
		
		BaseRequest sesionRequest = BaseRequest.newBaseRequest(RequestMethod.POST, this.baseURL + GET_SESSION, this.format, Object.class);
		sesionRequest.addPostParameter("username", userName);
		sesionRequest.addPostParameter("password", password);
		
		ResponseData response = sesionRequest.performRequest(true, queue);		
		session.setSessionId(response.getResponceString());
		
		BaseRequest tokenRequest = BaseRequest.newBaseRequest(RequestMethod.POST, this.baseURL + GET_TOKEN, this.format,Object.class);
		response = tokenRequest.performRequest(true, queue);
		session.setToken(response.getResponceString());
		
		if(session.isValid())
		{
			this.session = session;
		}
	}

	@Override
	public boolean isLogged()
	{		
		return session != null;
	}

	@Override
	public void applyLoginDataToRequest(BaseRequest request)
	{
		request.addPostParameter("Cookie", session.getSessionId());
		request.addPostParameter("X-CSRF-Token", session.getToken());
	}

	@Override
	public void restoreLoginData()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout(RequestQueue queue)
	{
		// TODO Auto-generated method stub
		
	}

}
