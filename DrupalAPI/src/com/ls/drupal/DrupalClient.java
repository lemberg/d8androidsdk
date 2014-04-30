package com.ls.drupal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import android.content.Context;

import com.android.volley.Request;
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

public class DrupalClient implements OnResponseListener
{

	private final String baseURL;
	private final RequestFormat requestFormat;
	private RequestQueue queue;
	private Map<BaseRequest, OnResponseListener> listeners;
	private String defaultCharset;

	private ILoginManager loginManager;

	public static interface OnResponseListener
	{
		void onResponceReceived(ResponseData data, Object tag);

		void onError(VolleyError error, Object tag);
		
		void onCancel(Object tag);
	}
	
	public DrupalClient(@NonNull String theBaseURL,@NonNull Context theContext)
	{
		this(theBaseURL, theContext,null);
	}

	public DrupalClient(@NonNull String theBaseURL,@NonNull Context theContext,@Nullable RequestFormat theFormat)
	{
		this(theBaseURL, theContext, theFormat, null);
	}

	public DrupalClient(@NonNull String theBaseURL,@NonNull Context theContext,@Nullable RequestFormat theFormat,@Nullable ILoginManager theLoginManager)
	{
		this(theBaseURL,getDefaultQueue(theContext),theFormat,theLoginManager);		
	}
	
	@SuppressWarnings("null")
	private static @NonNull RequestQueue getDefaultQueue(@NonNull Context theContext)
	{
		return Volley.newRequestQueue(theContext.getApplicationContext());
	}
	
	public DrupalClient(@NonNull String theBaseURL,@NonNull RequestQueue theQueue,@Nullable RequestFormat theFormat,@Nullable ILoginManager theLoginManager)
	{
		this.listeners = new HashMap<BaseRequest, DrupalClient.OnResponseListener>();
		this.queue = theQueue;
		this.baseURL = theBaseURL;
		
		if(theFormat != null)
		{
			this.requestFormat = theFormat;
		}else{
			this.requestFormat =  RequestFormat.JSON;
		}
		
		if (theLoginManager != null)
		{
			this.setLoginManager(theLoginManager);
		} else
		{
			this.setLoginManager(new AnonymousLoginManager());
		}
	}

	public ResponseData performRequest(BaseRequest request, boolean synchronous)
	{
		return performRequest(request, null, null, synchronous);
	}

	public ResponseData performRequest(BaseRequest request, Object tag, OnResponseListener listener, boolean synchronous)
	{
		if (!this.loginManager.isLogged())
		{
			throw new IllegalStateException("User isnt logged in");
		}
		request.setTag(tag);
		request.setResponceListener(this);
		this.loginManager.applyLoginDataToRequest(request);
		this.listeners.put(request, listener);
//		Log.d("DrupalClient", "Performing request:" + request.getUrl());
		return request.performRequest(synchronous, queue);
	}

	public ResponseData getObject(AbstractDrupalEntity entity, Class<?> resultClass, Object tag, OnResponseListener listener,
			boolean synchronous)
	{
		BaseRequest request = BaseRequest.newBaseRequest(RequestMethod.GET, getURLForEntity(entity), this.requestFormat, resultClass);
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.GET));

		return this.performRequest(request, tag, listener, synchronous);
	}

	public ResponseData postObject(AbstractDrupalEntity entity, Class<?> resultClass, Object tag, OnResponseListener listener,
			boolean synchronous)
	{
		BaseRequest request = BaseRequest.newBaseRequest(RequestMethod.POST, getURLForEntity(entity), this.requestFormat, resultClass);
		request.setObjectToPost(entity.getManagedData());
		request.setPostParameters(entity.getItemRequestPostParameters());
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.POST));

		return this.performRequest(request, tag, listener, synchronous);
	}

	public ResponseData patchObject(AbstractDrupalEntity entity, Class<?> resultClass, Object tag, OnResponseListener listener,
			boolean synchronous)
	{
		BaseRequest request = BaseRequest.newBaseRequest(RequestMethod.PATCH, getURLForEntity(entity), this.requestFormat, resultClass);
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.PATCH));
		request.setObjectToPost(entity.getPatchObject());
		return this.performRequest(request, tag, listener, synchronous);
	}

	public ResponseData deleteObject(AbstractDrupalEntity entity, Class<?> resultClass, Object tag, OnResponseListener listener,
			boolean synchronous)
	{
		BaseRequest request = BaseRequest.newBaseRequest(RequestMethod.DELETE, getURLForEntity(entity), this.requestFormat, resultClass);
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.DELETE));
		return this.performRequest(request, tag, listener, synchronous);
	}

	private String getURLForEntity(AbstractDrupalEntity entity)
	{
		return this.baseURL + entity.getPath();
	}

	/**
	 * This request is always synchronous
	 * 
	 * @param userName
	 * @param password
	 */
	public final void login(final String userName, final String password)
	{
		if (!this.loginManager.isLogged())
		{
			this.loginManager.login(userName, password, queue);
		}
	}

	/**
	 * This request is always synchronous
	 */
	public final void logout()
	{
		if (this.loginManager.isLogged())
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
		if (!this.loginManager.isLogged())
		{
			this.loginManager.restoreLoginData();
		}
	}

	@Override
	public void onResponceReceived(ResponseData data, BaseRequest request)
	{			
		OnResponseListener listener = this.listeners.get(request);
		if (listener != null)
		{			
			listener.onResponceReceived(data, request.getTag());
		}
		this.listeners.remove(request);
	}

	@Override
	public void onError(VolleyError error, BaseRequest request)
	{
		OnResponseListener listener = this.listeners.get(request);
		if (listener != null)
		{
			listener.onError(error, request.getTag());
		}
		this.listeners.remove(request);
	}
	
	public String getDefaultCharset()
	{
		return defaultCharset;
	}

	public void setDefaultCharset(String defaultCharset)
	{
		this.defaultCharset = defaultCharset;
	}

	public void cancelByTag(final @NonNull Object tag)
	{
		this.cancelAllRequestsForListener(null, tag);
	}
		
	public void cancelAll()
	{
		this.cancelAllRequestsForListener(null, null);
	}
	
	/**
	 * Cancel all requests for given listener with tag
	 * @param listener listener to cancel requests for in case if null passed- all requests for given tag will be canceled
	 * @param tag to cancel requests for, in case if null passed- all requests for given listener will be canceled
	 */
	public void cancelAllRequestsForListener(final @Nullable OnResponseListener theListener, final @Nullable Object theTag)
	{
		this.queue.cancelAll(new RequestQueue.RequestFilter()
		{			
			@Override
			public boolean apply(Request<?> request)
			{
				if(theTag == null||theTag.equals(request.getTag()))
				{
					OnResponseListener listener = listeners.get(request);
					if(theListener == null||listener.equals(theListener))
					{				
						if(listener != null)
						{							
							listeners.remove(request);		
							listener.onCancel(request.getTag());
						}
						return true;
					}
				}
				
				return false;
			}
		});		
	}
}