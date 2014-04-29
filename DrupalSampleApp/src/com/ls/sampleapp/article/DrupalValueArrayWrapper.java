package com.ls.sampleapp.article;

import java.util.ArrayList;

public class DrupalValueArrayWrapper<T> extends ArrayList<DrupalValueContainer<T>>
{		
	private static final long serialVersionUID = 9137776045434777936L;

	public DrupalValueArrayWrapper(T value)
	{
		super(1);
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
		this.clear();
		this.add(new DrupalValueContainer<T>(value));
	}
	
	
}
