package com.ls.http.base;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

abstract class ResponceHandler
{
	abstract Object itemFromResponce(@NotNull String responce,@NotNull Class<?> theClass);
	abstract Object itemFromResponce(@NotNull String responce,@NotNull Type theType);

	Object itemFromResponceWithSpecifier(String responce, Object theSpecifier)
	{
		Object result = null;
		if(responce != null && theSpecifier != null)
		{
			if(theSpecifier instanceof Class<?>)
			{
				return itemFromResponce(responce, (Class<?>)theSpecifier);
			}else if(theSpecifier instanceof Type){
				return itemFromResponce(responce, (Type)theSpecifier);
			}else{
				throw new IllegalArgumentException("You have to specify Class<?> or Type instance");
			}
		}
		return result;
	}
}
