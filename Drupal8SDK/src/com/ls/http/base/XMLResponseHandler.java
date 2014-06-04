package com.ls.http.base;

import java.lang.reflect.Type;

import org.eclipse.jdt.annotation.NonNull;


	class XMLResponseHandler extends ResponseHandler{
	
	@Override
	public Object itemFromResponse(@NonNull String response, @NonNull Class<?> theClass)
	{
		Object result = null;
		//TODO implement parsing
		return result;
	}

	@Override
	public Object itemFromResponse(@NonNull String json, @NonNull Type theType)
	{		
		Object result = null;
		//TODO implement parsing
		return result;
	}
}
