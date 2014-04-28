package com.ls.sampleapp.article;

import java.util.HashMap;
import java.util.Map;

import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalItemsArray;
import com.ls.http.base.BaseRequest.RequestMethod;

public class Page extends DrupalItemsArray<Article>
{	
	private int pageId;
				
	public int getPageId()
	{
		return pageId;
	}

	public void setPageId(int pageId)
	{
		this.pageId = pageId;
	}

	public Page(DrupalClient client, int pageId)
	{
		super(client,5);		
		this.pageId = pageId;
	}

	@Override
	protected String getPath()
	{		
		return "/entity/views/articles";
	}

	@Override
	protected Map<String, String> getItemRequestPostParameters()
	{		
		return null;
	}

	@Override
	protected Map<String, String> getItemRequestGetParameters(RequestMethod method)
	{
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", Integer.toString(this.pageId));
		return parameters;
	}	
}
