package com.ls.drupal;

import org.eclipse.jdt.annotation.NonNull;

public abstract class DrupalEntityContainer extends AbstractDrupalEntity
{		
	transient private final Object data;
	public DrupalEntityContainer(DrupalClient client,Object theData)
	{
		super(client);
		this.data = theData;
	}	

	public @NonNull Object getManagedData()
	{
		return data;
	}	
}
