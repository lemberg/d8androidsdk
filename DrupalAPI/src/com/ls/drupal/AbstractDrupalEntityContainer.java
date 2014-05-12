package com.ls.drupal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.eclipse.jdt.annotation.NonNull;

public abstract class AbstractDrupalEntityContainer<T> extends AbstractDrupalEntity
{		
	transient private final Object data;
	public AbstractDrupalEntityContainer(DrupalClient client,T theData)
	{
		super(client);
		this.data = theData;
	}	

	public @NonNull Object getManagedData()
	{
		return data;
	}	
	
	@Override
	protected void consumeObject(Object entity)
	{
		AbstractDrupalEntity.consumeObject(this.data, entity);
	}
	
	@Override
	protected Object getManagedDataClassSpecifyer()
	{
		Class<?> itemsArrayClass = this.getClass();
		Type classType = null;

		while (classType == null)
		{
			if (itemsArrayClass.getSuperclass().equals(AbstractDrupalItemsArray.class))
			{
				classType = itemsArrayClass.getGenericSuperclass();
			}
			itemsArrayClass = itemsArrayClass.getSuperclass();
		}

		Type genericArgType = ((ParameterizedType) classType).getActualTypeArguments()[0];
	
		return genericArgType;

	}
}
