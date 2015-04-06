package com.ls.drupal;

import android.support.annotation.NonNull;

/**
 * 
 * @author lemberg
 *
 * @param <T> class of container content
 */
public abstract class AbstractDrupalEntityContainer<T> extends AbstractBaseDrupalEntity
{		
	transient private T data;
	public AbstractDrupalEntityContainer(DrupalClient client,T theData)
	{
		super(client);
		if(theData == null)
		{
			throw new IllegalArgumentException("Data object can't be null");
		}
		this.data = theData;
	}	

	@SuppressWarnings("null")
	public @NonNull T getManagedData()
	{
		return data;
	}	
	
	@Override
	protected final void consumeObject(Object entity)
	{
         AbstractBaseDrupalEntity.consumeObject(this.data, entity);
	}
}
