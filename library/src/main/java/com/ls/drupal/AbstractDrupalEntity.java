package com.ls.drupal;

import org.jetbrains.annotations.NotNull;


public abstract class AbstractDrupalEntity extends AbstractBaseDrupalEntity
{
	public AbstractDrupalEntity(DrupalClient client)
	{
		super(client);
	}

	@Override
	public @NotNull
    Object getManagedData()
	{
		return this;
	}
}
