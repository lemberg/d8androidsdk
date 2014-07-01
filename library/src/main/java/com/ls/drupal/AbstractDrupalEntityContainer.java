/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Lemberg Solutions Limited
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
