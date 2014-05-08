package com.ls.sampleapp.article;

import java.util.HashMap;
import java.util.Map;

import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalItemsArray;
import com.ls.http.base.BaseRequest.RequestMethod;

public class Page extends DrupalItemsArray<ArticlePreview>
{

	transient private int pageNumber;
	transient private String categoryId;
	
	public Page(DrupalClient client, int thePageNumber,String theCategoryId)
	{
		super(client, 5);	
		this.pageNumber = thePageNumber;
		this.categoryId = theCategoryId;
	}

	@Override
	protected String getPath()
	{		
		if(categoryId == null)
		{
			return "blog-rest";
		}else{
			return "category/"+categoryId;
		}
	}

	@Override
	protected Map<String, String> getItemRequestPostParameters()
	{		
		return null;
	}

	@Override
	protected Map<String, String> getItemRequestGetParameters(RequestMethod method)
	{
		switch (method) {
		case GET:
			Map<String, String> result = new HashMap<String, String>();
			result.put("page", Integer.toString(pageNumber));
			return result;			
		default:
			return null;			
		}
	}

	public int getPageNumber()
	{
		return pageNumber;
	}
	
}
