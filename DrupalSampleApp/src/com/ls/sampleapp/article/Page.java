package com.ls.sampleapp.article;

import java.util.Map;

import junit.framework.Assert;

import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalEntity;
import com.ls.http.base.BaseRequest.RequestMethod;

public class Page extends DrupalEntity
{

	private DrupalValueArrayWrapper<Integer> nid;
	
	public Page(DrupalClient client, int nodeNumber)
	{
		super(client);	
		this.nid = new DrupalValueArrayWrapper<Integer>(nodeNumber);
	}	
	
	@Override
	protected String getPath()
	{
		Assert.assertNotNull("Node id can't be null while requesting item", this.nid);
		return "/node"+"/"+this.nid;
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
}
