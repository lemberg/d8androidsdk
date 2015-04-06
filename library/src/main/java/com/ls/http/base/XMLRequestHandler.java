package com.ls.http.base;


import android.support.annotation.NonNull;

class XMLRequestHandler extends RequestHandler
{
	
	public XMLRequestHandler(@NonNull Object theObject)
	{
		super(theObject);		
	}

	@Override
	public String stringBodyFromItem()
	{
		return null;//TODO implementt later
	}
}
