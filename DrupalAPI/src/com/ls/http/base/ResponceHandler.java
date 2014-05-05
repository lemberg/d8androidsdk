package com.ls.http.base;

import java.lang.reflect.Type;

import org.eclipse.jdt.annotation.NonNull;

abstract class ResponceHandler
{
	abstract Object itemFromResponce(@NonNull String responce,@NonNull Class<?> theClass);
	abstract Object itemFromResponce(@NonNull String responce,@NonNull Type theType);
	
	Object itemFromResponceWithSpecifier(String responce, Object theSpecifier)
	{
		Object result = null;
		if(responce != null && theSpecifier != null)
		{
			if(theSpecifier instanceof Type)
			{
				return itemFromResponce(responce, (Type)theSpecifier);
			}else if(theSpecifier instanceof Class<?>){
				return itemFromResponce(responce, (Class<?>)theSpecifier);
			}else{
				throw new IllegalArgumentException("You have to specify Class<?> or Type instance");
			}
		}
		return result;
	}
}
