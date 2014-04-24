package com.ls.drupal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class DrupalItemsArray<T> extends DrupalEntity implements Collection<T>
{		
	transient private ArrayList<T>items;
	
	public DrupalItemsArray(DrupalClient client, int itemCount)
	{
		super(client);	
		items = new ArrayList<T>(itemCount);
	}

	@Override
	public boolean add(T object)
	{
		return items.add(object);
	}

	@Override
	public boolean addAll(Collection<? extends T> collection)
	{
		return this.items.addAll(collection);
	}

	@Override
	public void clear()
	{
		this.items.clear();
	}

	@Override
	public boolean contains(Object object)
	{
		return items.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection)
	{
		return items.containsAll(collection);
	}

	@Override
	public boolean isEmpty()
	{		
		return items.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{		
		return items.iterator();
	}

	@Override
	public boolean remove(Object object)
	{		
		return items.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> collection)
	{		
		return items.removeAll(collection);
	}

	@Override
	public boolean retainAll(Collection<?> collection)
	{		
		return items.retainAll(collection);
	}

	@Override
	public int size()
	{		
		return items.size();
	}

	@Override
	public Object[] toArray()
	{		
		return items.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array)
	{		
		return items.toArray(array);
	}	
	
	/**
	 * Returns the element at the specified location in this list.
	 * @param index the index of the element to return.
	 * @return the element at the specified index.
	 */
	public T get(int index)
	{
		return items.get(index);
	}
	
	/**
	 * Replaces the element at the specified location in this ArrayList with the specified object.
	 * @param index
	 * @param object
	 * @return the previous element at the index.
	 * @throws IndexOutOfBoundsException - when location < 0 || location >= size()
	 */
	public T set(int index,T object)
	{
		return items.set(index, object);
	}
	
	/**
	 * Removes the object at the specified location from this list.
	 * @param index the index of the object to remove.
	 * @return the removed object.
	 * @throws IndexOutOfBoundsException - when location < 0 || location >= size()
	 */
	public T remove(int index)
	{
		return items.remove(index);
	}
}
