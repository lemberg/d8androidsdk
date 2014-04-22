package com.ls.drupal;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ls.drupal.login.AnonymousLoginManager;
import com.ls.drupal.login.ILoginManager;
import com.ls.http.base.BaseRequest;
import com.ls.http.base.BaseRequest.OnResponseListener;
import com.ls.http.base.BaseRequest.RequestFormat;
import com.ls.http.base.BaseRequest.RequestMethod;
import com.ls.http.base.ResponseData;

public class DrupalClient implements OnResponseListener{

	private final String baseURL;
	private final RequestFormat requestFormat;
	private RequestQueue queue;
	private Map<BaseRequest,OnResponseListener> listeners;
	
	private ILoginManager loginManager;
		
	public DrupalClient(String theBaseURL,Context theContext) {
		this(theBaseURL,theContext,RequestFormat.JSON);
	}
	
	public DrupalClient(String theBaseURL,Context theContext, RequestFormat theFormat) {
		this(theBaseURL,theContext,theFormat,null);
	}
	
	public DrupalClient(String theBaseURL,Context theContext,RequestFormat theFormat ,ILoginManager theLoginManager) {
		this.listeners = new HashMap<BaseRequest, DrupalClient.OnResponseListener>();
		this.queue = Volley.newRequestQueue(theContext.getApplicationContext());		
		this.baseURL = theBaseURL;
		this.requestFormat = theFormat;
		if(theLoginManager != null)
		{
			this.setLoginManager(theLoginManager);
		}else{
			this.setLoginManager(new AnonymousLoginManager());
		}
	}
	
	public ResponseData performRequest(BaseRequest request,boolean synchronous)
	{		
		return performRequest(request, null, null, synchronous);		
	}
	
	public ResponseData performRequest(BaseRequest request,Object tag, OnResponseListener listener,boolean synchronous)
	{
		if(!this.loginManager.isLogged())
		{
			throw new IllegalStateException("User isnt logged in");
		}
		request.setTag(tag);
		this.loginManager.applyLoginDataToRequest(request);
		this.listeners.put(request, listener);		
		Log.d("DrupalClient","Performing request:"+request.getUrl());
		return request.performRequest(synchronous, queue);		
	}
	
	public ResponseData getObject(DrupalEntity entity,Class resultClass,Object tag, OnResponseListener listener,boolean synchronous)
	{		
		BaseRequest request = BaseRequest.newBaseRequest(RequestMethod.GET,getURLForEntity(entity), this.requestFormat, resultClass);		
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.GET));
		
		return this.performRequest(request, tag, listener, synchronous);
	}
	
	public ResponseData postObject(DrupalEntity entity, Class resultClass, Object tag, OnResponseListener listener,boolean synchronous)
	{		
		BaseRequest request = BaseRequest.newBaseRequest(RequestMethod.POST,getURLForEntity(entity), this.requestFormat, resultClass);
		request.setObjectToPost(entity);
		request.setPostParameters(entity.getItemRequestPostParameters());
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.POST));
		
		return this.performRequest(request, tag, listener, synchronous);
	}
	
	public ResponseData patchObject(DrupalEntity entity, Class resultClass, Object tag, OnResponseListener listener,boolean synchronous)
	{		
		BaseRequest request = BaseRequest.newBaseRequest(RequestMethod.PATCH,getURLForEntity(entity), this.requestFormat, resultClass);		
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.PATCH));
		request.setObjectToPost(entity);
		return this.performRequest(request, tag, listener, synchronous);
	}
	
	public ResponseData deleteObject(DrupalEntity entity,Class resultClass,Object tag, OnResponseListener listener,boolean synchronous)
	{		
		BaseRequest request = BaseRequest.newBaseRequest(RequestMethod.DELETE,getURLForEntity(entity), this.requestFormat, resultClass);		
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.DELETE));		
		return this.performRequest(request, tag, listener, synchronous);
	}
	
	private String getURLForEntity(DrupalEntity entity)
	{
		return this.baseURL+entity.getPath();
	}

	/**
	 * This request is always synchronous
	 * @param userName
	 * @param password
	 */
	public final void login(final String userName, final String password)
	{
		if(!this.loginManager.isLogged())
		{
			this.loginManager.login(userName, password, queue);
		}
	}
	
	/**
	 * This request is always synchronous	
	 */
	public final void logout()
	{
		if(this.loginManager.isLogged())
		{
			this.loginManager.logout(queue);
		}
	}
	
	public boolean isLogged()
	{
		return this.loginManager.isLogged();
	}

	public ILoginManager getLoginManager()
	{
		return loginManager;
	}

	public void setLoginManager(ILoginManager loginManager)
	{
		this.loginManager = loginManager;
		if(!this.loginManager.isLogged())
		{
			this.loginManager.restoreLoginData();
		}
	}

	@Override
	public void onResponceReceived(ResponseData data, BaseRequest request)
	{
		OnResponseListener listener = this.listeners.get(request);
		if(listener!=null)
		{
			listener.onResponceReceived(data, request.getTag());
		}
		this.listeners.remove(request);
	}

	@Override
	public void onError(VolleyError error, BaseRequest request)
	{
		OnResponseListener listener = this.listeners.get(request);
		if(listener!=null)
		{
			listener.onError(error, request.getTag());
		}
		this.listeners.remove(request);
	}	
	

	public static interface OnResponseListener
	{
		void onResponceReceived(ResponseData data, Object tag);

		void onError(VolleyError error, Object tag);
	}
}
