package com.ls.drupal.login;

import com.android.volley.RequestQueue;
import com.ls.http.base.BaseRequest;

public interface ILoginManager {
	void login(String userName,String password,RequestQueue queue);
	boolean isLogged();
	void applyLoginDataToRequest(BaseRequest request);
	void restoreLoginData();
	void logout(RequestQueue queue);
}
