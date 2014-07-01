package com.ls.http.base;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;


class XMLResponceHandler extends ResponceHandler{

	@Override
	public Object itemFromResponce(@NotNull String responce, @NotNull Class<?> theClass)
	{
		Object result = null;
		//TODO implement parsing
		return result;
	}

	@Override
	public Object itemFromResponce(@NotNull String json, @NotNull Type theType)
	{
		Object result = null;
		//TODO implement parsing
		return result;
	}
}
