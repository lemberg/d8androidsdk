package com.ls.drupal8demo.drupal;

public class DrupalValueContainer<T>
{
	public T value;
	public T target_id;
	
	public DrupalValueContainer(T theValue)
	{
		value = theValue;
	}
}
