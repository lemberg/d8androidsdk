package com.ls.drupal.node;

import java.util.Map;

import junit.framework.Assert;

import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalEntity;
import com.ls.http.base.BaseRequest.RequestMethod;

public class DrupalNode extends DrupalEntity
{	
	public DrupalNode(DrupalClient client)
	{
		super(client);		
	}

	private String nodeId;
		
	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	@Override
	protected String getPath()
	{
		Assert.assertNotNull("Node id can't be null while requesting item", nodeId);
		return "entity/node"+"/"+nodeId;
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
