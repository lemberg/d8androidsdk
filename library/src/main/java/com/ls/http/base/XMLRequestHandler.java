package com.ls.http.base;


import org.jetbrains.annotations.NotNull;

class XMLRequestHandler extends RequestHandler
{

	public XMLRequestHandler(@NotNull Object theObject)
	{
		super(theObject);
	}

	@Override
	public String stringBodyFromItem()
	{
		return null;//TODO implementt later
	}
}
