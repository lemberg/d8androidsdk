package com.ls.http.base;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;


abstract class ResponseHandler
{
	abstract Object itemFromResponse(@NonNull String response,@NonNull Class<?> theClass);
	abstract Object itemFromResponse(@NonNull String response,@NonNull Type theType);
	
	Object itemFromResponseWithSpecifier(String response, Object theSpecifier)
	{
		Object result = null;
		if(response != null && theSpecifier != null)
		{
			if(theSpecifier instanceof Class<?>)
			{
                result = itemFromResponse(response, (Class<?>)theSpecifier);
			}else if(theSpecifier instanceof Type){
                result = itemFromResponse(response, (Type)theSpecifier);
			}else{
				throw new IllegalArgumentException("You have to specify Class<?> or Type instance");
			}
		}
		return result;
	}
}
