package com.ls.http.base;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;


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
