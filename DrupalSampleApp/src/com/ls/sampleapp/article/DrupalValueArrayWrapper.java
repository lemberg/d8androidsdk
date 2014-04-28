package com.ls.sampleapp.article;

import java.util.LinkedList;

public class DrupalValueArrayWrapper<T> extends LinkedList<DrupalValueContainer<T>>
{		
	private static final long serialVersionUID = 9137776045434777936L;

	public DrupalValueArrayWrapper(T value)
	{
		this.setValue(value);
	}
	
	public T getValue()
	{
		if(!this.isEmpty())
		{
			return this.get(0).value;
		}else{
			return null;
		}
	}
	
	public void setValue(T value)
	{
		this.set(0, new DrupalValueContainer<T>(value));
	}
	
	
}
