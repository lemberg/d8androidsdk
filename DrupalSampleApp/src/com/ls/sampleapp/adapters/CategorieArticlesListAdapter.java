package com.ls.sampleapp.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.VolleyError;
import com.ls.drupal.AbstractDrupalEntity;
import com.ls.drupal.AbstractDrupalEntity.OnEntityRequestListener;
import com.ls.drupal.DrupalClient;
import com.ls.sampleapp.article.ArticlePreview;
import com.ls.sampleapp.article.Page;

public class CategorieArticlesListAdapter extends BaseAdapter implements OnEntityRequestListener
{
	private final static int PRELOADING_PAGE_OFFSET = 4;

	private int pagesLoaded = 0;
	private boolean canLoadMore;
	
	private Page currentLoadingPage;
	private final DrupalClient client;
	private final LayoutInflater inflater;
	
	private final List<ArticlePreview> data;
	
	public CategorieArticlesListAdapter(String categoryId, DrupalClient theClient, Context theContext)
	{
		this.data = new ArrayList<ArticlePreview>();
		this.inflater = LayoutInflater.from(theContext);
		this.client = theClient;
		this.canLoadMore = true;
		this.loadNextPage();
	}
	
	@Override
	public int getCount()
	{		
		return data.size();
	}

	@Override
	public Object getItem(int position)
	{		
		return data.get(position);
	}

	@Override
	public long getItemId(int position)
	{		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{		
		if(position > this.data.size() - PRELOADING_PAGE_OFFSET)
		{
			this.loadNextPage();
		}
		
		//TODO implement view creation;
		return null;
	}

	private void loadNextPage()
	{
		if(this.canLoadMore && this.currentLoadingPage == null)
		{
			this.currentLoadingPage = new Page(this.client, this.pagesLoaded);
			this.currentLoadingPage.setRequestListener(this);
			this.currentLoadingPage.getDataFromServer(false);			
		}
	}

	@Override
	public void onEntityFetched(AbstractDrupalEntity entity)
	{		
		if(!this.currentLoadingPage.isEmpty())
		{
			this.data.addAll(this.currentLoadingPage);
			this.currentLoadingPage = null;
			this.pagesLoaded++;
			this.notifyDataSetChanged();
		}else{
			this.canLoadMore = false;
		}
	}

	@Override
	public void onEntityPosted(AbstractDrupalEntity entity)
	{}

	@Override
	public void onEntityPatched(AbstractDrupalEntity entity)
	{}

	@Override
	public void onEntityRemoved(AbstractDrupalEntity entity)
	{}

	@Override
	public void onRequestFailed(VolleyError error, AbstractDrupalEntity entity)
	{
		this.canLoadMore = false;
	}
	
}
