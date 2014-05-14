package com.ls.http.base;

import java.lang.reflect.Type;

import org.eclipse.jdt.annotation.NonNull;


	class XMLResponceHandler extends ResponceHandler{
	
	@Override
	public Object itemFromResponce(@NonNull String responce, @NonNull Class<?> theClass)
	{
		Object result = null;
		//TODO implement parsing
		return result;
	}

	@Override
	public Object itemFromResponce(@NonNull String json, @NonNull Type theType)
	{		
		Object result = null;
		//TODO implement parsing
		return result;
	}
}
