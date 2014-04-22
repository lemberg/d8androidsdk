package com.ls.drupal.view;

import java.util.Map;

import junit.framework.Assert;

import com.android.volley.VolleyError;
import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalEntity;
import com.ls.http.base.BaseRequest.RequestMethod;
import com.ls.http.base.ResponseData;

public abstract class DrupalView extends DrupalEntity
{	
	public DrupalView(DrupalClient client)
	{
		super(client);		
	}

	@Override
	protected String getPath()
	{		
		return "entity/views/";
	}

//	@Override
//	protected Map<String, String> getItemRequestPostParameters()
//	{		
//		return null;
//	}
//	
//	@Override
//	protected Map<String, String> getItemRequestGetParameters(RequestMethod method)
//	{		
//		return null;
//	}	
	
}
