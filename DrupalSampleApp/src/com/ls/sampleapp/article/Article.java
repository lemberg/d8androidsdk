package com.ls.sampleapp.article;

import java.util.HashMap;
import java.util.Map;

import com.ls.drupal.DrupalClient;
import com.ls.drupal.view.DrupalView;
import com.ls.http.base.BaseRequest.RequestMethod;

public class Article extends DrupalView
{
	private int pageId;
	private  String title;
	private  String body;
	private Coment coment;
		
	public int getPageId()
	{
		return pageId;
	}

	public void setPageId(int pageId)
	{
		this.pageId = pageId;
	}

	public Article(DrupalClient client, int pageId)
	{
		super(client);		
		this.pageId = pageId;
	}

	@Override
	protected String getPath()
	{		
		return super.getPath()+"articles";
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

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public Coment getComent()
	{
		return coment;
	}

	public void setComent(Coment coment)
	{
		this.coment = coment;
	}		
}
