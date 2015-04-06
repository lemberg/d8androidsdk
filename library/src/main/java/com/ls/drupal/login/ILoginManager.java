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
	Object login(String userName, String password, RequestQueue queue);

    /**
     * @return true if manager has to perform login restore attempt in case of 401 error
     */
    boolean shouldRestoreLogin();

    /**
     * @return true if login can be restored (there are credentials or access token cached)
     */
    boolean canRestoreLogin();

    /**
     * Return true in case if base URL depends on login. In that case background relogin will be triggered in case of 404 exception
     * @return
     */
    boolean domainDependsOnLogin();

	/**
	 * Add necessary authentication data to request headers or post/get parameters
	 * @param request
	 */
	void applyLoginDataToRequest(BaseRequest request);
	/**
	 * Restore login data, if possible.
     * Note: this call should be performed synchronously
     * @param queue operation queue you can to perform login within (but it isn't necessary)
     * @return true if restore succeeded (or you can't define a result) false in case of failure
	 */
	boolean restoreLoginData(RequestQueue queue);

    /**
     * Method will be called in case if {@link #restoreLoginData restoreLoginData} returned false or we get 401 exception after login was restored
     */
    void onLoginRestoreFailed();

	/**
	 * Perform logout operation
	 * @param queue
	 * @return logout request result
	 */
	Object logout(RequestQueue queue);

}
