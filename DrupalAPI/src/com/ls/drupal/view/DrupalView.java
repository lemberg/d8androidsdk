package com.ls.drupal.view;

import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalEntity;

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
