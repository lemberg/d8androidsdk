package com.ls.drupal;

import org.eclipse.jdt.annotation.NonNull;


public abstract class DrupalEntity extends AbstractDrupalEntity
{
	public DrupalEntity(DrupalClient client)
	{
		super(client);		
	}
	
	@Override
	public @NonNull	Object getManagedData()
	{		
		return this;
	}
}
