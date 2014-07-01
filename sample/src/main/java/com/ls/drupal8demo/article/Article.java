package com.ls.drupal8demo.article;

import com.ls.drupal.AbstractDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupal8demo.drupal.DrupalValueArrayWrapper;
import com.ls.drupal8demo.drupal.DrupalValueContainer;
import com.ls.http.base.BaseRequest.RequestMethod;

import junit.framework.Assert;

import java.util.List;
import java.util.Map;

public class Article extends AbstractDrupalEntity
{
	/**
	 * Such complicated structures are just a workaround to map all server data to objects.
	 * Server can be configured to get rid of them.
	 */
	private List<DrupalValueContainer<String>> nid;
	private List<DrupalValueContainer<String>> body;
	private List<DrupalValueContainer<String>> title;
	private List<DrupalValueContainer<String>> field_blog_date;
	private List<DrupalValueContainer<String>> field_blog_author;

	public Article(DrupalClient client, String nodeId)
	{
		super(client);
		this.nid = new DrupalValueArrayWrapper<String>(nodeId);
	}

	@Override
	protected String getPath()
	{
		Assert.assertNotNull("Node id can't be null while requesting item", this.nid);
		return "node"+"/"+this.getNid();
	}

	@Override
	protected Map<String, String> getItemRequestPostParameters()
	{
		return null;
	}

	@Override
	protected Map<String, String> getItemRequestGetParameters(RequestMethod method)
	{
		return null;
	}

	public String getBody()
	{
		return getValue(this.body);
	}

	public String getTitle()
	{
		return getValue(this.title);
	}

	public String getAuthor()
	{
		return getTargetId(this.field_blog_author);
	}

	public String getDate()
	{
		return getTargetId(this.field_blog_date);
	}

	public String getNid()
	{
		return getValue(this.nid);
	}

	public void setBody(String body)
	{
		this.body = new DrupalValueArrayWrapper<String>(body);
	}

	private <T> T getValue(List<DrupalValueContainer<T>> list)
	{
		DrupalValueContainer<T> item = getItem(list);
		if(item!= null)
		{
			return item.value;
		}else{
			return null;
		}
	}

	private <T> T getTargetId(List<DrupalValueContainer<T>> list)
	{
		DrupalValueContainer<T> item = getItem(list);
		if(item!= null)
		{
			return item.target_id;
		}else{
			return null;
		}
	}

	private <T> DrupalValueContainer<T> getItem(List<DrupalValueContainer<T>> list)
	{
		if(list!=null && !list.isEmpty())
		{
			return list.get(0);
		}else{
			return null;
		}
	}
}
