package com.ls.sampleapp.article;

import java.util.HashMap;
import java.util.Map;

import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalItemsArray;
import com.ls.http.base.BaseRequest.RequestMethod;

public class Page extends DrupalItemsArray<Article>
{

	transient private int pageNumber;
	
	public Page(DrupalClient client, int thePageNumber)
	{
		super(client, 5);	
		this.pageNumber = thePageNumber;
	}

	@Override
	protected String getPath()
	{		
		return "blog-rest";
	}

	@Override
	protected Map<String, String> getItemRequestPostParameters()
	{		
		return null;
	}

	@Override
	protected Map<String, String> getItemRequestGetParameters(RequestMethod method)
	{
		switch (method) {
		case GET:
			Map result = new HashMap<String, String>();
			result.put("page", Integer.toString(pageNumber));
			return result;			
		default:
			return null;			
		}
	}

	public int getPageNumber()
	{
		return pageNumber;
	}
	
}
