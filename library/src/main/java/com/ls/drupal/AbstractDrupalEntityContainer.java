package com.ls.drupal;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 * @author lemberg
 *
 * @param <T> class of container content
 */
public abstract class AbstractDrupalEntityContainer<T> extends AbstractBaseDrupalEntity
{
	transient private final T data;
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
	public @NotNull
    T getManagedData()
	{
		return data;
	}

	@Override
	protected final void consumeObject(Object entity)
	{
		AbstractBaseDrupalEntity.consumeObject(this.data, entity);
	}

	@Override
	protected final Object getManagedDataClassSpecifyer()
	{
		Class<?> itemsArrayClass = this.getClass();
		Type classType = null;

		while (classType == null)
		{
			if (itemsArrayClass.getSuperclass().equals(AbstractDrupalArrayEntity.class))
			{
				classType = itemsArrayClass.getGenericSuperclass();
			}
			itemsArrayClass = itemsArrayClass.getSuperclass();
		}

		Type genericArgType = ((ParameterizedType) classType).getActualTypeArguments()[0];

		return genericArgType;

	}
}
