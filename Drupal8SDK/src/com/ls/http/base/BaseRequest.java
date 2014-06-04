package com.ls.http.base;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

public class BaseRequest extends StringRequest
{
	private static String ACCEPT_HEADER_KEY = "Accept";
	
	private static final String PROTOCOL_REQUEST_APP_TYPE_JSON = "application/json";
	private static final String PROTOCOL_REQUEST_APP_TYPE_XML = "application/xml";
	private static final String PROTOCOL_REQUEST_APP_TYPE_JSON_HAL = "application/hal+json";

	private static final String CONTENT_TYPE_CHARSET_PREFIX ="; charset=";

	public static enum RequestMethod
	{
		GET(Method.GET), POST(Method.POST), PATCH(Method.PATCH), DELETE(Method.DELETE), PUT(Method.PUT),HEAD(Method.HEAD),OPTIONS(Method.OPTIONS),TRACE(Method.TRACE);
		final private int methodCode;

		RequestMethod(int theCode)
		{
			this.methodCode = theCode;
		}
	}

	public static enum RequestFormat
	{
		JSON, XML, JSON_HAL
	};

	private final RequestFormat format;
	private final RequestFuture<String> syncLock;
	private final Object responseClasSpecifier;
	private String defaultCharset;

	private OnResponseListener responseListener;

	private Map<String, String> requestHeaders;
	private Map<String, String> postParameters;
	private Map<String, String> getParameters;
	private Object objectToPost;

	private ResponseData result;

	/**
	 * 
	 * @param requestMethod
	 * @param requestUrl
	 * @param requestFormat
	 * @param responseClassSpecifier
	 *            Class or Type, returned as data field of ResultData object, can be
	 *            null if you don't need one.
	 * @return
	 */
	public static BaseRequest newBaseRequest(RequestMethod requestMethod, String requestUrl, RequestFormat requestFormat, Object responseClassSpecifier)
	{
		RequestFuture<String> lock = RequestFuture.newFuture();
		BaseRequest result = new BaseRequest(requestMethod, requestUrl, requestFormat, responseClassSpecifier, lock);
		return result;
	};

	private BaseRequest(RequestMethod requestMethod, String requestUrl, RequestFormat requestFormat,Object theResponseClass, RequestFuture<String> lock)
	{
		super(requestMethod.methodCode, requestUrl, lock, lock);
		this.format = requestFormat;
		this.syncLock = lock;
		this.initRequestHeaders();
		this.responseClasSpecifier = theResponseClass;
		this.result = new ResponseData();
	}

	public Object getResponseClasSpecifier()
	{
		return responseClasSpecifier;
	}

	private void initRequestHeaders()
	{
		this.requestHeaders = new HashMap<String, String>();
		switch (this.format) {
		case XML:
			this.addRequestHeader(ACCEPT_HEADER_KEY, PROTOCOL_REQUEST_APP_TYPE_XML);
			break;
		case JSON_HAL:
			this.addRequestHeader(ACCEPT_HEADER_KEY, PROTOCOL_REQUEST_APP_TYPE_JSON_HAL);
			break;
		case JSON:
		default:
			this.addRequestHeader(ACCEPT_HEADER_KEY, PROTOCOL_REQUEST_APP_TYPE_JSON);
		}
	}

	public ResponseData performRequest(boolean synchronous, RequestQueue theQueque)
	{
		theQueque.add(this);

		if (synchronous)
		{
			try
			{
				@SuppressWarnings("unused")
				String response = this.syncLock.get(); // this call will block
														// thread
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (ExecutionException e)
			{
				e.printStackTrace();
			}
		}

		return this.result;
	}
		
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response)
	{
		Response<String> requestResult = super.parseNetworkResponse(response);	
		if (!this.isCanceled())
		{			
			this.result.error = requestResult.error;
			this.result.statusCode = response.statusCode;
			this.result.headers = new HashMap<String, String>(response.headers);
			this.result.responseString = requestResult.result;
			if (requestResult.result != null)
			{

				switch (this.format) {
				case XML:
					this.result.data = new XMLResponseHandler().itemFromResponseWithSpecifier(requestResult.result, responseClasSpecifier);
					break;
				case JSON:
				case JSON_HAL:
				default:
					this.result.data = new JSONResponseHandler().itemFromResponseWithSpecifier(requestResult.result, responseClasSpecifier);
				}

			}
		}
		return requestResult;
	}
	
	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError)
	{
		VolleyError error = super.parseNetworkError(volleyError);
		if(volleyError.networkResponse != null)
		{
			this.result.statusCode = volleyError.networkResponse.statusCode;
		}
		this.result.error = error;
		return error;
	}

	@Override
	protected void deliverResponse(String response)
	{		
		if (this.responseListener != null)
		{
			this.responseListener.onResponseReceived(result, this);
		}
		this.syncLock.onResponse(response);
	}

	@Override
	public void deliverError(VolleyError error)
	{
		if (this.responseListener != null)
		{
			this.responseListener.onError(error, this);
		}
		this.syncLock.onErrorResponse(error);
	}

	public static interface OnResponseListener
	{
		void onResponseReceived(ResponseData data, BaseRequest request);

		void onError(VolleyError error, BaseRequest request);
	}

	public OnResponseListener getResponseListener()
	{
		return responseListener;
	}

	public void setResponseListener(OnResponseListener responseListener)
	{
		this.responseListener = responseListener;
	}

	public RequestFormat getFormat()
	{
		return format;
	}

	// Header parameters handling

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError
	{
		if (this.requestHeaders != null)
		{
			return this.requestHeaders;
		} else
		{
			return super.getHeaders();
		}
	}

	public Map<String, String> getRequestHeaders()
	{
		return requestHeaders;
	}

	public void setRequestHeaders(Map<String, String> requestHeaders)
	{
		this.requestHeaders = requestHeaders;
	}

	public void addRequestHeaders(Map<String, String> theRequestHeaders)
	{
		this.requestHeaders.putAll(theRequestHeaders);
	}

	public void addRequestHeader(String key, String value)
	{
		this.requestHeaders.put(key, value);
	}

	// Post parameters handling

	@Override
	protected Map<String, String> getParams() throws AuthFailureError
	{
		if (this.postParameters != null)
		{
			return this.postParameters;
		} else
		{
			return super.getParams();
		}
	}

	public Map<String, String> getPostParameters()
	{
		return postParameters;
	}

	public void setPostParameters(Map<String, String> postParameters)
	{
		this.postParameters = postParameters;
	}

	public void addPostParameters(Map<String, String> postParameters)
	{
		if (this.postParameters == null)
		{
			this.postParameters = postParameters;
		} else
		{
			this.postParameters.putAll(postParameters);
		}
	}

	public void addPostParameter(String key, String value)
	{
		if (this.postParameters == null)
		{
			this.postParameters = new HashMap<String, String>();
		}
		if (value == null)
		{
			this.postParameters.remove(key);
		} else
		{
			this.postParameters.put(key, value);
		}
	}

	// Post Body handling

	@SuppressWarnings("null")
	@Override
	public byte[] getBody() throws AuthFailureError
	{
		if (this.objectToPost != null && this.postParameters == null)
		{
			RequestHandler handler;
			switch (this.format) {
			case XML:
				handler = new XMLRequestHandler(this.objectToPost);
				break;
			case JSON:
			case JSON_HAL:
				handler = new JSONRequestHandler(this.objectToPost);
				break;
			default:
				throw new IllegalArgumentException("Unrecognised request format");
			}
			try
			{
				String content = handler.stringBodyFromItem();
				return content.getBytes(handler.getCharset(this.defaultCharset));
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
				return new byte[0];
			}
		} else
		{
			return super.getBody();
		}
	}

	@SuppressWarnings("null")
	@Override
	public String getBodyContentType()
	{
		if (this.objectToPost != null)
		{
			RequestHandler handler;
			switch (this.format) {
			case XML:
				handler = new XMLRequestHandler(objectToPost);
				return PROTOCOL_REQUEST_APP_TYPE_XML + CONTENT_TYPE_CHARSET_PREFIX + handler.getCharset(this.defaultCharset);
			case JSON_HAL:
				handler = new JSONRequestHandler(objectToPost);
				return PROTOCOL_REQUEST_APP_TYPE_JSON_HAL + CONTENT_TYPE_CHARSET_PREFIX + handler.getCharset(this.defaultCharset);
			case JSON:			
				handler = new JSONRequestHandler(objectToPost);
				return PROTOCOL_REQUEST_APP_TYPE_JSON + CONTENT_TYPE_CHARSET_PREFIX + handler.getCharset(this.defaultCharset);
			default:
				throw new IllegalArgumentException("Unrecognised request format");
			}
		}

		return super.getBodyContentType();

	}

	public Object getObjectToPost()
	{
		return objectToPost;
	}

	public void setObjectToPost(Object objectToPost)
	{
		this.objectToPost = objectToPost;
	}

	// Get parameters handling

	@Override
	public String getUrl()
	{
		if (this.getParameters != null && !this.getParameters.isEmpty())
		{			
			Uri.Builder builder = Uri.parse(super.getUrl()).buildUpon();
			for (Map.Entry<String, String> entry : this.getParameters.entrySet())
			{
				builder.appendQueryParameter(entry.getKey(), entry.getValue());
			}					
			return builder.build().toString();
		} else
		{
			return super.getUrl();
		}
	}

	public Map<String, String> getGetParameters()
	{
		return getParameters;
	}

	public void setGetParameters(Map<String, String> getParameters)
	{
		this.getParameters = getParameters;
	}

	public void addGetParameters(Map<String, String> getParameters)
	{
		if (this.getParameters == null)
		{
			this.getParameters = getParameters;
		} else
		{
			this.getParameters.putAll(getParameters);
		}
	}

	public void addGetParameter(String key, String value)
	{
		if (this.getParameters == null)
		{
			this.getParameters = new HashMap<String, String>();
		}
		if (value == null)
		{
			this.getParameters.remove(key);
		} else
		{
			this.getParameters.put(key, value);
		}
	}

	public String getDefaultCharset()
	{
		return defaultCharset;
	}

	/**
	 * 
	 * @param defaultCharset charset, used to encode post body.
	 */
	public void setDefaultCharset(String defaultCharset)
	{
		this.defaultCharset = defaultCharset;
	}

	@Override
	public void cancel()
	{
		this.syncLock.onResponse(null);
		super.cancel();
	}
}
