package com.ls.http.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class RequestHandler
{
	protected final String DEFAULT_CHARSET = "utf-8";

	protected Object object;

	public abstract String stringBodyFromItem();

	public RequestHandler(@NotNull Object theObject)
	{
		this.object = theObject;
	}

	protected boolean implementsPostableInterface()
	{
		return object instanceof IPostableItem;
	}

	String getCharset(@Nullable String defaultCharset)
	{
		String charset = null;
		if(object instanceof ICharsetItem)
		{
			charset =  ((ICharsetItem)object).getCharset();
		}

		if(charset == null)
		{
			charset = defaultCharset;;
		}

		if(charset == null)
		{
			charset = DEFAULT_CHARSET;
		}
		return charset;
	}
}
