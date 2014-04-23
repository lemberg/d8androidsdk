package com.ls.drupal;

import org.eclipse.jdt.annotation.NonNull;

import com.google.gson.annotations.Expose;

public abstract class DrupalEntityContainer extends AbstractDrupalEntity
{	
	@Expose
	private final Object data;
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
