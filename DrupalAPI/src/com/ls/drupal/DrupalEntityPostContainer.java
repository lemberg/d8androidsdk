package com.ls.drupal;

import java.util.Map;

import com.google.gson.Gson;
import com.ls.http.base.BaseRequest.RequestMethod;
import com.ls.http.base.SharedGson;
/**
 * Class, used in order to post data to server only. Fetch/remove operations aren't supported.
 * @author lemberg
 *
 */
public class  DrupalEntityPostContainer extends DrupalEntity
{		
	private final Object data;
	private final String path;
	private Map<String, String> getParameters;
	private final String charset;

	public DrupalEntityPostContainer(DrupalClient client,Object theData, String thePath, String theCharset)
	{
		super(client);		
		this.path = thePath;
		this.data = theData;
		this.charset = theCharset;
	}

	public void setGetParameters(Map<String, String> getParameters)
	{
		this.getParameters = getParameters;
	}

	@Override
	public String toJsonString()
	{
		Gson gson = SharedGson.getGson();;
		return gson.toJson(this.data);
	}

	@Override
	public String toXMLString()
	{		
		return null;
	}

	@Override
	protected String getPath()
	{		
		return path;
	}

	@Override
	protected Map<String, String> getItemRequestPostParameters()
	{		
		return null;
	}

	@Override
	protected Map<String, String> getItemRequestGetParameters(RequestMethod method)
	{		
		if(method == RequestMethod.POST||method == RequestMethod.PATCH)
		{
			return getParameters;
		}else{
			return null;
		}
	}
	
	@Override
	public String getCharset()
	{		
		return this.charset;
	}
	
}
