package com.ls.drupal.base;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

public class BaseRequest extends StringRequest {

	private static final String PROTOCOL_CONTENT_TYPE_JSON ="application/json; charset=";
	private static final String PROTOCOL_CONTENT_TYPE_XML ="application/xml; charset=";
	
	public static enum RequestMethod {
		GET(Method.GET), POST(Method.POST), PATCH(Method.PATCH), DELETE(
				Method.DELETE);
		final private int methodCode;

		RequestMethod(int theCode) {
			this.methodCode = theCode;
		}
	}

	public static enum RequestFormat {
		JSON, XML
	};

	private final RequestFormat format;
	private final String requestId;
	private final RequestFuture<String> syncLock;
	private OnResponseListener responceListener;

	private Map<String, String> requestHeaders;
	private Map<String, String> postParameters;
	private BaseItem objectToPost;
	
	private ResponseData result;

	public static  BaseRequest newBaseRequest(
			RequestMethod requestMethod, String requestUrl,
			RequestFormat requestFormat, String requestID) {
		RequestFuture<String> lock = RequestFuture.newFuture();
		BaseRequest result = new BaseRequest(requestMethod, requestUrl,
				requestFormat, requestID, lock);
		return result;
	};

	private BaseRequest(RequestMethod requestMethod, String requestUrl,
			RequestFormat requestFormat, String requestID,
			RequestFuture<String> lock) {
		super(requestMethod.methodCode, requestUrl, lock, lock);
		this.format = requestFormat;
		this.syncLock = lock;
		this.requestId = requestID;
	}

	public ResponseData performRequest(boolean synchronous,
			RequestQueue theQueque) {
		theQueque.add(this);

		if (synchronous) {
			try {
				String response = this.syncLock.get(); // this call will block
														// thread
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			}
		}

		return this.result;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		Response<String> requestResult = super.parseNetworkResponse(response);
		this.result = new ResponseData();
		this.result.error = requestResult.error;
		this.result.statusCode = response.statusCode;
		this.result.headers = new HashMap<String, String>(response.headers);		
		if(requestResult.result != null)
		{
			this.result.data = new BaseItem();
			switch(this.format)
			{
			case XML:
				this.result.data.initWithXMLString(requestResult.result);
				break;
			case JSON:
			default:
				this.result.data.initWithJsonString(requestResult.result);
			}
		}
		return requestResult;
	}

	
	@Override
	protected void deliverResponse(String response) {
		if (this.responceListener != null) {
			this.responceListener.onResponceReceived(result, requestId);
		}
	}

	@Override
	public void deliverError(VolleyError error) {
		if (this.responceListener != null) {
			this.responceListener.onError(error, requestId);
		}
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		if (this.objectToPost == null && this.postParameters != null) {
			return this.postParameters;
		} else {
			return super.getParams();
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		if(this.requestHeaders != null)
		{
			return this.requestHeaders;
		}else{
			return super.getHeaders();
		}
	}
	
	@Override
	public byte[] getBody() throws AuthFailureError {
		if(this.objectToPost != null)
		{
			String content;
			switch(this.format)
			{
			case XML:
				content = this.objectToPost.toXMLString();
				break;
			case JSON:
			default:
				content = this.objectToPost.toJsonString();
				break;
			}
			try {
				return content.getBytes(this.objectToPost.getCharset());
			} catch (UnsupportedEncodingException e) {				
				e.printStackTrace();
				return new byte[0];
			}
		}else{		
			return super.getBody();
		}
	}
	
	@Override
	public String getBodyContentType() {
		if(this.objectToPost != null)
		{
			switch(this.format)
			{
			case XML:
				return PROTOCOL_CONTENT_TYPE_XML+this.objectToPost.getCharset();
			case JSON:
				return PROTOCOL_CONTENT_TYPE_JSON+this.objectToPost.getCharset();
			}
		}		
		
		return super.getBodyContentType();				
	}
	
	public static interface OnResponseListener {
		void onResponceReceived(ResponseData data, String requestId);

		void onError(VolleyError error, String requestId);
	}

	public static class ResponseData {
		protected BaseItem data;
		protected Map<String, String> headers;
		protected int statusCode;
		protected VolleyError error;

		public BaseItem getData() {
			return data;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public VolleyError getError() {
			return error;
		}
	}

	public OnResponseListener getResponceListener() {
		return responceListener;
	}

	public void setResponceListener(OnResponseListener responceListener) {
		this.responceListener = responceListener;
	}

	public RequestFormat getFormat() {
		return format;
	}

	public String getRequestId() {
		return requestId;
	}

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Map<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public Map<String, String> getPostParameters() {
		return postParameters;
	}

	public void setPostParameters(Map<String, String> postParameters) {
		this.postParameters = postParameters;
	}
	
	public void addRequestHeaders(Map<String, String> requestHeaders) {
		if(this.requestHeaders == null)
		{
			this.requestHeaders = requestHeaders;
		}else{
			this.requestHeaders.putAll(requestHeaders);
		}
	}
	
	public void addPostParameters(Map<String, String> postParameters) {
		if(this.postParameters == null)
		{
			this.postParameters = postParameters;
		}else{
			this.postParameters.putAll(postParameters);
		}
	}
}
