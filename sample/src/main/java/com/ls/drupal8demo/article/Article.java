/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Lemberg Solutions Limited
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
