package com.ls.drupal.login;

import com.android.volley.RequestQueue;
import com.ls.http.base.BaseRequest;

public class AnonymousLoginManager implements ILoginManager
{

	@Override
	public Object login(String userName, String password, RequestQueue queue)
	{
		return null;
	}

	@Override
	public boolean isLogged()
	{		
		return true;
	}

	@Override
	public void applyLoginDataToRequest(BaseRequest request)
	{}

	@Override
	public void restoreLoginData()
	{}

	@Override
	public Object logout(RequestQueue queue)
	{
		return null;
	}

}
