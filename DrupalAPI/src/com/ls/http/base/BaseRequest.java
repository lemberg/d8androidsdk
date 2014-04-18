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

	private static final String PROTOCOL_CONTENT_TYPE_JSON = "application/json; charset=";
	private static final String PROTOCOL_CONTENT_TYPE_XML = "application/xml; charset=";

	public static enum RequestMethod
	{
		GET(Method.GET), POST(Method.POST), PATCH(Method.PATCH), DELETE(Method.DELETE);
		final private int methodCode;

		RequestMethod(int theCode)
		{
			this.methodCode = theCode;
		}
	}

	public static enum RequestFormat
	{
		JSON, XML
	};

	private final RequestFormat format;	
	private final RequestFuture<String> syncLock;
	private final Class responceClass;
	
	private OnResponseListener responceListener;

	private Map<String, String> requestHeaders;
	private Map<String, String> postParameters;
	private Map<String, String> getParameters;
	private IPostableItem objectToPost;
	
	private ResponseData result;
	
/**
 * 
 * @param requestMethod
 * @param requestUrl
 * @param requestFormat
 * @param responseClass Class, returned as data field of ResultData object, can be null if you djn't need one.
 * @return
 */
	public static BaseRequest newBaseRequest(RequestMethod requestMethod, String requestUrl, RequestFormat requestFormat, Class responseClass)
	{
		RequestFuture<String> lock = RequestFuture.newFuture();
		BaseRequest result = new BaseRequest(requestMethod, requestUrl, requestFormat, responseClass, lock);
		return result;
	};

	private BaseRequest(RequestMethod requestMethod, String requestUrl, RequestFormat requestFormat,Class theResponseClass,RequestFuture<String> lock)
	{
		super(requestMethod.methodCode, requestUrl, lock, lock);
		this.format = requestFormat;
		this.syncLock = lock;		
		this.initRequestHeaders();	
		this.responceClass = theResponseClass;
	}

	public Class getResponceClass()
	{
		return responceClass;
	}

	private void initRequestHeaders()
	{
		this.requestHeaders = new HashMap<String, String>();
		switch (this.format) {
		case XML:
			this.addRequestHeader("Accept", "application/xml");
			break;
		case JSON:
		default:
			this.addRequestHeader("Accept", "application/json");
		}
	}

	public ResponseData performRequest(boolean synchronous, RequestQueue theQueque)
	{
		theQueque.add(this);

		if (synchronous)
		{
			try
			{
				String response = this.syncLock.get(); // this call will block
														// thread
			} catch (InterruptedException e)
			{
			} catch (ExecutionException e)
			{
			}
		}

		return this.result;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response)
	{
		Response<String> requestResult = super.parseNetworkResponse(response);
		this.result = new ResponseData();
		this.result.error = requestResult.error;
		this.result.statusCode = response.statusCode;
		this.result.headers = new HashMap<String, String>(response.headers);
		this.result.responceString = requestResult.result;
		if (requestResult.result != null)
		{

			switch (this.format) {
			case XML:
				this.result.data = new XMLResponceHandler().itemFromResponce(requestResult.result,responceClass);
				break;
			case JSON:
			default:
				this.result.data = new JSONResponceHandler().itemFromResponce(requestResult.result,responceClass);
			}

		}
		return requestResult;
	}

	@Override
	protected void deliverResponse(String response)
	{
		if (this.responceListener != null)
		{
			this.responceListener.onResponceReceived(result, this);
		}
	}

	@Override
	public void deliverError(VolleyError error)
	{
		if (this.responceListener != null)
		{
			this.responceListener.onError(error, this);
		}
	}
	
	public static interface OnResponseListener
	{
		void onResponceReceived(ResponseData data, BaseRequest request);

		void onError(VolleyError error, BaseRequest request);
	}

	public OnResponseListener getResponceListener()
	{
		return responceListener;
	}

	public void setResponceListener(OnResponseListener responceListener)
	{
		this.responceListener = responceListener;
	}

	public RequestFormat getFormat()
	{
		return format;
	}
	
	//Header parameters handling
	
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
	
	//Post parameters handling
	
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
	
	public void addPostParameter(String key,String value)
	{
		if (this.postParameters == null)
		{
			this.postParameters = new HashMap<String, String>();
		}
		if(value == null)
		{
			this.postParameters.remove(key);
		}else{
			this.postParameters.put(key, value);
		}	
	}
	
	//Post Body handling
	
	@Override
	public byte[] getBody() throws AuthFailureError
	{
		if (this.objectToPost != null && this.postParameters == null)
		{
			String content;
			switch (this.format) {
			case XML:
				content = this.objectToPost.toXMLString();
				break;
			case JSON:
			default:
				content = this.objectToPost.toJsonString();
				break;
			}
			try
			{
				return content.getBytes(this.objectToPost.getCharset());
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

	@Override
	public String getBodyContentType()
	{
		if (this.objectToPost != null)
		{
			switch (this.format) {
			case XML:
				return PROTOCOL_CONTENT_TYPE_XML + this.objectToPost.getCharset();
			case JSON:
				return PROTOCOL_CONTENT_TYPE_JSON + this.objectToPost.getCharset();
			}
		}

		return super.getBodyContentType();
	}
	
	public IPostableItem getObjectToPost()
	{
		return objectToPost;
	}

	public void setObjectToPost(IPostableItem objectToPost)
	{
		this.objectToPost = objectToPost;
	}
	
	//Get parameters handling
	
	@Override
	public String getUrl()
	{
		if(this.getParameters != null && !this.getParameters.isEmpty())
		{			
			Uri.Builder builder = Uri.parse(super.getUrl()).buildUpon();
			for(Map.Entry<String, String> entry:this.getParameters.entrySet())
			{
				builder.appendQueryParameter(entry.getKey(), entry.getValue());				
			}
			
			return builder.build().toString();
		}else{		
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
	
	public void addGetParameter(String key,String value)
	{
		if (this.getParameters == null)
		{
			this.getParameters = new HashMap<String, String>();
		}
		if(value == null)
		{
			this.getParameters.remove(key);
		}else{
			this.getParameters.put(key, value);
		}	
	}
}
