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

package com.ls.drupal8demo.adapters;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.AbstractBaseDrupalEntity.OnEntityRequestListener;
import com.ls.drupal.DrupalClient;
import com.ls.drupal8demo.R;
import com.ls.drupal8demo.article.ArticlePreview;
import com.ls.drupal8demo.article.Page;
import com.ls.http.base.ResponseData;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CategoryArticlesListAdapter extends BaseAdapter implements OnEntityRequestListener
{
	private final static int PRELOADING_PAGE_OFFSET = 4;

	private int pagesLoaded = 0;
	private boolean canLoadMore;

	private Page currentLoadingPage;
	private final DrupalClient client;
	private final LayoutInflater inflater;
	private ImageLoader loader;
	private String categoryId;
	private View emptyView;

	private final List<ArticlePreview> data;

	public CategoryArticlesListAdapter(String theCategoryId, DrupalClient theClient, Context theContext, View emptyView)
	{
		this.emptyView = emptyView;
		this.data = new ArrayList<ArticlePreview>();
		this.inflater = LayoutInflater.from(theContext);
		this.client = theClient;
		this.setCanLoadMore(true);
		this.categoryId = theCategoryId;
		this.initImageLoader(theContext);
		this.loadNextPage();
	}

	private void initImageLoader(Context theContext)
	{
		RequestQueue queue = Volley.newRequestQueue(theContext);
		this.loader = new ImageLoader(queue, new ImageCache()
		{
			@Override
			public void putBitmap(String url, Bitmap bitmap)
			{
			}

			@Override
			public Bitmap getBitmap(String url)
			{
				return null;
			}
		});
	}

	@Override
	public int getCount()
	{
		return data.size();
	}

	@Override
	public ArticlePreview getItem(int position)
	{
		return data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public void setCanLoadMore(boolean canLoadMore)
	{
		this.canLoadMore = canLoadMore;
		if(!this.canLoadMore && this.data.isEmpty())
		{
			this.emptyView.setVisibility(View.VISIBLE);
		}else{
			this.emptyView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (position > this.data.size() - PRELOADING_PAGE_OFFSET)
		{
			this.loadNextPage();
		}

		if (convertView == null)
		{
			convertView = this.inflater.inflate(R.layout.list_item_article, null);
		}

		ArticlePreview item = this.data.get(position);
		if (item != null)
		{
			TextView title = (TextView) convertView.findViewById(R.id.title);
			title.setText(Html.fromHtml(item.getTitle()).toString());

			TextView author = (TextView) convertView.findViewById(R.id.author);
			View byView = convertView.findViewById(R.id.by);
			String authorName = item.getAuthor();
			if (authorName != null && !authorName.isEmpty())
			{
				author.setText(authorName);
				byView.setVisibility(View.VISIBLE);
				author.setVisibility(View.VISIBLE);
			} else
			{
				byView.setVisibility(View.INVISIBLE);
				author.setVisibility(View.INVISIBLE);
			}

			TextView date = (TextView) convertView.findViewById(R.id.date);
			date.setText(item.getDate());

			TextView description = (TextView) convertView.findViewById(R.id.description);
			description.setText(Html.fromHtml(item.getBody()));

			NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.image);
			imageView.setImageUrl(item.getImage(), this.loader);
		}
		return convertView;
	}

	private void loadNextPage()
	{
		if (this.canLoadMore && this.currentLoadingPage == null)
		{
			this.currentLoadingPage = new Page(this.client, this.pagesLoaded, this.categoryId);
			this.currentLoadingPage.pullFromServer(false, null, this);
		}
	}

	@Override
	public void onRequestCompleted(AbstractBaseDrupalEntity entity, Object tag, ResponseData data)
	{
		if (!this.currentLoadingPage.isEmpty())
		{
			this.data.addAll(this.currentLoadingPage);
			this.currentLoadingPage = null;
			this.pagesLoaded++;
			this.notifyDataSetChanged();
		} else
		{
			this.setCanLoadMore(false);
		}
	}

	@Override
	public void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, VolleyError error)
	{
		this.setCanLoadMore(false);
	}

	@Override
	public void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag)
	{
	}

}
