package com.ls.drupal.login;

import com.android.volley.RequestQueue;
import com.ls.http.base.BaseRequest;

public class AnonymousLoginManager implements ILoginManager
{

	@Override
	public void login(String userName, String password, RequestQueue queue)
	{}

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
	public void logout(RequestQueue queue)
	{}

}
