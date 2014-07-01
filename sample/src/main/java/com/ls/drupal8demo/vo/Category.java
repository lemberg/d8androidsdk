package com.ls.drupal8demo.vo;

public class Category
{
	private String name;
	private String id;
	
	public Category(String theName, String theId)
	{
		this.name = theName;
		this.id = theId;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}	
}
