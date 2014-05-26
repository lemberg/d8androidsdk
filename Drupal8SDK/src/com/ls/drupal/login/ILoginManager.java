package com.ls.drupal.login;

import com.android.volley.RequestQueue;
import com.ls.http.base.BaseRequest;

public interface ILoginManager {
	/**
	 * Login request, responsible for login data fetch
	 * @param userName
	 * @param password
	 * @param queue operation queue to perform login within
	 * @return login result object
	 */
	Object login(String userName,String password,RequestQueue queue);
	/**	
	 * @return true if all necessary user id data is fetched and there is no need in performing login
	 */
	boolean isLogged();
	/**
	 * Add necessary authentication data to request headers or post/get parameters
	 * @param request
	 */
	void applyLoginDataToRequest(BaseRequest request);
	/**
	 * Restore login data, if possible.
	 */
	void restoreLoginData();
	/**
	 * Perform logout operation
	 * @param queue
	 * @return logout request result
	 */
	Object logout(RequestQueue queue);
}
