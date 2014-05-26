package com.ls.drupal;

import org.eclipse.jdt.annotation.NonNull;


public abstract class AbstractDrupalEntity extends AbstractBaseDrupalEntity
{
	public AbstractDrupalEntity(DrupalClient client)
	{
		super(client);		
	}
	
	@Override
	public @NonNull	Object getManagedData()
	{		
		return this;
	}
}
