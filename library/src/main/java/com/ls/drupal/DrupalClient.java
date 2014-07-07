/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Lemberg Solutions Limited
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ls.drupal;

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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Class is used to generate requests based on DrupalEntities and attach them to request queue
 *
 * @author lemberg
 */
public class DrupalClient implements OnResponseListener {

	private final String baseURL;
	private final RequestFormat requestFormat;
	private RequestQueue queue;
	private Map<BaseRequest, OnResponseListener> listeners;
	private String defaultCharset;

	private ILoginManager loginManager;
	private RequestProgressListener progressListener;

	public static interface OnResponseListener {

		void onResponseReceived(ResponseData data, Object tag);

		void onError(VolleyError error, Object tag);

		void onCancel(Object tag);
	}

	/**
	 * Can be used in order to react on request count changes (start/success/failure or canceling).
	 *
	 * @author lemberg
	 */
	public interface RequestProgressListener {

		/**
		 * Called after new request was added to queue
		 *
		 * @param activeRequests number of requests pending
		 */
		void onRequestStarted(DrupalClient theClient, int activeRequests);

		/**
		 * Called after current request was complete
		 *
		 * @param activeRequests number of requests pending
		 */
		void onRequestFinished(DrupalClient theClient, int activeRequests);
	}

	/**
	 * @param theBaseURL this URL will be appended with {@link com.ls.drupal.AbstractBaseDrupalEntity#getPath()}
	 * @param theContext application context, used to create request queue
	 */
	public DrupalClient(@NotNull String theBaseURL, @NotNull Context theContext) {
		this(theBaseURL, theContext, null);
	}

	/**
	 * @param theBaseURL this URL will be appended with {@link com.ls.drupal.AbstractBaseDrupalEntity#getPath()}
	 * @param theContext application context, used to create request queue
	 * @param theFormat  server request/response format. Defines format of serialized objects and server response
	 *                   format, see {@link com.ls.http.base.BaseRequest.RequestFormat}
	 */
	public DrupalClient(@NotNull String theBaseURL, @NotNull Context theContext, @Nullable RequestFormat theFormat) {
		this(theBaseURL, theContext, theFormat, null);
	}

	/**
	 * @param theBaseURL      this URL will be appended with {@link com.ls.drupal.AbstractBaseDrupalEntity#getPath()}
	 * @param theContext      application context, used to create request queue
	 * @param theFormat       server request/response format. Defines format of serialized objects and server response
	 *                        format, see {@link com.ls.http.base.BaseRequest.RequestFormat}
	 * @param theLoginManager contains user profile data and can update request parameters and headers in order to apply
	 *                        it.
	 */
	public DrupalClient(@NotNull String theBaseURL, @NotNull Context theContext, @Nullable RequestFormat theFormat,
			@Nullable ILoginManager theLoginManager) {
		this(theBaseURL, getDefaultQueue(theContext), theFormat, theLoginManager);
	}

	@SuppressWarnings("null")
	private static
	@NotNull
	RequestQueue getDefaultQueue(@NotNull Context theContext) {
		return Volley.newRequestQueue(theContext.getApplicationContext());
	}

	/**
	 * @param theBaseURL      this URL will be appended with {@link com.ls.drupal.AbstractBaseDrupalEntity#getPath()}
	 * @param theQueue        queue to execute requests. You can customize cache management, by setting custom queue
	 * @param theFormat       server request/response format. Defines format of serialized objects and server response
	 *                        format, see {@link com.ls.http.base.BaseRequest.RequestFormat}
	 * @param theLoginManager contains user profile data and can update request parameters and headers in order to apply
	 *                        it.
	 */
	public DrupalClient(@NotNull String theBaseURL, @NotNull RequestQueue theQueue, @Nullable RequestFormat theFormat,
			@Nullable ILoginManager theLoginManager) {
		this.listeners = new HashMap<BaseRequest, OnResponseListener>();
		this.queue = theQueue;
		this.baseURL = theBaseURL;

		if (theFormat != null) {
			this.requestFormat = theFormat;
		} else {
			this.requestFormat = RequestFormat.JSON;
		}

		if (theLoginManager != null) {
			this.setLoginManager(theLoginManager);
		} else {
			this.setLoginManager(new AnonymousLoginManager());
		}
	}

	/**
	 * @param request     Request object to be performed
	 * @param synchronous if true request result will be returned synchronously
	 * @return {@link com.ls.http.base.ResponseData} object, containing request result code and string or error and
	 * deserialized object, specified in request.
	 */
	public ResponseData performRequest(BaseRequest request, boolean synchronous) {
		return performRequest(request, null, null, synchronous);
	}

	/**
	 * @param request     Request object to be performed
	 * @param tag         will be applied to the request and returned in listener
	 * @param synchronous if true request result will be returned synchronously
	 * @return {@link com.ls.http.base.ResponseData} object, containing request result code and string or error and
	 * deserialized object, specified in request.
	 */
	public ResponseData performRequest(BaseRequest request, Object tag, OnResponseListener listener,
			boolean synchronous) {
		if (!this.loginManager.isLogged()) {
			throw new IllegalStateException("User isn't logged in");
		}
		request.setTag(tag);
		request.setResponseListener(this);
		this.loginManager.applyLoginDataToRequest(request);
		this.listeners.put(request, listener);
		this.onNewRequestStarted();
		return request.performRequest(synchronous, queue);
	}

	/**
	 * @param entity                 Object, specifying request parameters, retrieved data will be merged to this
	 *                               object.
	 * @param responseClassSpecifier Class<?> or Type of the object, returned as data field of ResultData object, can be
	 *                               null if you don't need one.
	 * @param tag                    will be attached to request and returned in listener callback, can be used in order
	 *                               to cancel request
	 * @param synchronous            if true - result will be returned synchronously.
	 * @return ResponseData object or null if request was asynchronous.
	 */
	public ResponseData getObject(AbstractBaseDrupalEntity entity, Object responseClassSpecifier, Object tag,
			OnResponseListener listener, boolean synchronous) {
		BaseRequest request = BaseRequest
				.newBaseRequest(RequestMethod.GET, getURLForEntity(entity), this.requestFormat, responseClassSpecifier);
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.GET));

		return this.performRequest(request, tag, listener, synchronous);
	}

	/**
	 * @param entity                 Object, specifying request parameters
	 * @param responseClassSpecifier Class<?> or Type of the object, returned as data field of ResultData object, can be
	 *                               null if you don't need one.
	 * @param tag                    will be attached to request and returned in listener callback, can be used in order
	 *                               to cancel request
	 * @param synchronous            if true - result will be returned synchronously.
	 * @return ResponseData object or null if request was asynchronous.
	 */
	public ResponseData postObject(AbstractBaseDrupalEntity entity, Object responseClassSpecifier, Object tag,
			OnResponseListener listener, boolean synchronous) {
		BaseRequest request = BaseRequest
				.newBaseRequest(RequestMethod.POST, getURLForEntity(entity), this.requestFormat,
						responseClassSpecifier);
		request.setObjectToPost(entity.getManagedData());
		request.setPostParameters(entity.getItemRequestPostParameters());
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.POST));

		return this.performRequest(request, tag, listener, synchronous);
	}

	/**
	 * @param entity                 Object, specifying request parameters, must have "createFootPrint" called before.
	 * @param responseClassSpecifier Class<?> or Type of the object, returned as data field of ResultData object, can be
	 *                               null if you don't need one.
	 * @param tag                    will be attached to request and returned in listener callback, can be used in order
	 *                               to cancel request
	 * @param synchronous            if true - result will be returned synchronously.
	 * @return ResponseData object or null if request was asynchronous.
	 */
	public ResponseData patchObject(AbstractBaseDrupalEntity entity, Object responseClassSpecifier, Object tag,
			OnResponseListener listener, boolean synchronous) {
		BaseRequest request = BaseRequest
				.newBaseRequest(RequestMethod.PATCH, getURLForEntity(entity), this.requestFormat,
						responseClassSpecifier);
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.PATCH));
		request.setObjectToPost(entity.getPatchObject());
		return this.performRequest(request, tag, listener, synchronous);
	}

	/**
	 * @param entity                 Object, specifying request parameters
	 * @param responseClassSpecifier Class<?> or Type of the object, returned as data field of ResultData object, can be
	 *                               null if you don't need one.
	 * @param tag                    will be attached to request and returned in listener callback, can be used in order
	 *                               to cancel request
	 * @param synchronous            if true - result will be returned synchronously.
	 * @return ResponceData object or null if request was asynchronous.
	 */
	public ResponseData deleteObject(AbstractBaseDrupalEntity entity, Object responseClassSpecifier, Object tag,
			OnResponseListener listener,
			boolean synchronous) {
		BaseRequest request = BaseRequest
				.newBaseRequest(RequestMethod.DELETE, getURLForEntity(entity), this.requestFormat,
						responseClassSpecifier);
		request.setGetParameters(entity.getItemRequestGetParameters(RequestMethod.DELETE));
		return this.performRequest(request, tag, listener, synchronous);
	}

	private String getURLForEntity(AbstractBaseDrupalEntity entity) {
		return this.baseURL + entity.getPath();
	}

	/**
	 * This request is always synchronous and has no callback
	 */
	public final Object login(final String userName, final String password) {
		return this.loginManager.login(userName, password, queue);
	}

	/**
	 * This request is always synchronous
	 */
	public final void logout() {
		this.loginManager.logout(queue);
	}

	/**
	 * @return true if all necessary user id data is fetched and there is no need in performing login
	 */
	public boolean isLogged() {
		return this.loginManager.isLogged();
	}

	public ILoginManager getLoginManager() {
		return loginManager;
	}

	public void setLoginManager(ILoginManager loginManager) {
		this.loginManager = loginManager;
		if (!this.loginManager.isLogged()) {
			this.loginManager.restoreLoginData();
		}
	}

	@Override
	public void onResponseReceived(ResponseData data, BaseRequest request) {
		OnResponseListener listener = this.listeners.get(request);
		this.listeners.remove(request);
		this.onRequestComplete();
		if (listener != null) {
			listener.onResponseReceived(data, request.getTag());
		}
	}

	@Override
	public void onError(VolleyError error, BaseRequest request) {
		OnResponseListener listener = this.listeners.get(request);
		this.listeners.remove(request);
		this.onRequestComplete();
		if (listener != null) {
			listener.onError(error, request.getTag());
		}
	}

	/**
	 * @return Charset, used to encode/decode server request post body and response.
	 */
	public String getDefaultCharset() {
		return defaultCharset;
	}

	/**
	 * @param defaultCharset Charset, used to encode/decode server request post body and response.
	 */
	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	/**
	 * @param tag Cancel all requests, containing given tag. If no tag is specified - all requests are canceled.
	 */
	public void cancelByTag(final @NotNull Object tag) {
		this.cancelAllRequestsForListener(null, tag);
	}

	/**
	 * @param tag Cancel all requests
	 */
	public void cancelAll() {
		this.cancelAllRequestsForListener(null, null);
	}

	/**
	 * Cancel all requests for given listener with tag
	 *
	 * @param listener listener to cancel requests for in case if null passed- all requests for given tag will be
	 *                 canceled
	 * @param tag      to cancel requests for, in case if null passed- all requests for given listener will be canceled
	 */
	public void cancelAllRequestsForListener(final @Nullable OnResponseListener theListener,
			final @Nullable Object theTag) {
		this.queue.cancelAll(new RequestQueue.RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				if (theTag == null || theTag.equals(request.getTag())) {
					OnResponseListener listener = listeners.get(request);
					if (theListener == null || listener.equals(theListener)) {
						if (listener != null) {
							listeners.remove(request);
							listener.onCancel(request.getTag());
							DrupalClient.this.onRequestComplete();
						}
						return true;
					}
				}

				return false;
			}
		});
	}

	// Manage request progress

	/**
	 * @return number of requests pending
	 */
	public int getActiveRequestsCount() {
		return this.listeners.size();
	}

	public void setProgressListener(RequestProgressListener progressListener) {
		this.progressListener = progressListener;
	}

	private void onNewRequestStarted() {
		if (this.progressListener != null) {

			int requestCount = this.getActiveRequestsCount();
			this.progressListener.onRequestStarted(this, requestCount);

		}
	}

	private void onRequestComplete() {
		if (this.progressListener != null) {
			int requestCount = this.getActiveRequestsCount();
			this.progressListener.onRequestFinished(this, requestCount);
		}
	}

}