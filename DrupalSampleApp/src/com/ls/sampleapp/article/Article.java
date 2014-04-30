package com.ls.sampleapp.article;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalEntity;
import com.ls.http.base.BaseRequest.RequestMethod;
import com.ls.sampleapp.drupal.DrupalValueArrayWrapper;
import com.ls.sampleapp.drupal.DrupalValueContainer;

public class Article extends DrupalEntity
{

	private List<DrupalValueContainer<String>> nid;
	private List<DrupalValueContainer<String>> body;
	private List<DrupalValueContainer<String>> title;
	
	public Article(DrupalClient client, int nodeNumber)
	{
		super(client);	
		this.nid = new DrupalValueArrayWrapper<String>(Integer.toString(nodeNumber));
	}	
	
	@Override
	protected String getPath()
	{
		Assert.assertNotNull("Node id can't be null while requesting item", this.nid);
		return "node"+"/"+this.getNid();
	}

	@Override
	protected Map<String, String> getItemRequestPostParameters()
	{		
		return null;
	}

	@Override
	protected Map<String, String> getItemRequestGetParameters(RequestMethod method)
	{		
		return null;
	}

	public String getBody()
	{
		return getValue(this.body);
	}
	
	public String getTitle()
	{
		return getValue(this.title);
	}
	
	public String getNid()
	{
		return getValue(this.nid);
	}

	public void setBody(String body)
	{
		this.body = new DrupalValueArrayWrapper<String>(body);
	}
	
	private <T> T getValue(List<DrupalValueContainer<T>> list)
	{
		if(list!=null && !list.isEmpty())
		{
			return list.get(0).value;
		}else{
			return null;
		}
	}
}
