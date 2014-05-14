package com.ls.sampleapp.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.AbstractBaseDrupalEntity.OnEntityRequestListener;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.ResponseData;
import com.ls.sampleapp.R;
import com.ls.sampleapp.article.ArticlePreview;
import com.ls.sampleapp.article.Page;

public class CategoryArticlesListAdapter extends BaseAdapter implements OnEntityRequestListener
{
	private final static int PRELOADING_PAGE_OFFSET = 4;

	private int pagesLoaded = 0;
	private boolean canLoadMore;
	
	private Page currentLoadingPage;
	private final DrupalClient client;
	private final LayoutInflater inflater;
	private String categoryId;
	
	private final List<ArticlePreview> data;
	
	public CategoryArticlesListAdapter(String theCategoryId, DrupalClient theClient, Context theContext)
	{
		this.data = new ArrayList<ArticlePreview>();
		this.inflater = LayoutInflater.from(theContext);
		this.client = theClient;
		this.canLoadMore = true;
		this.categoryId = theCategoryId;
		this.loadNextPage();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{		
		if(position > this.data.size() - PRELOADING_PAGE_OFFSET)
		{
			this.loadNextPage();
		}
		
		if(convertView == null)
		{
			convertView = this.inflater.inflate(R.layout.list_item_article, null);
		}
		
		ArticlePreview item = this.data.get(position);
		if(item != null)
		{
			TextView title = (TextView)convertView.findViewById(R.id.title);
			title.setText(Html.fromHtml(item.getTitle()));
			
			TextView author = (TextView)convertView.findViewById(R.id.author);
			author.setText(item.getAuthor());
			
			TextView date = (TextView)convertView.findViewById(R.id.date);
			date.setText(item.getDate());
			
			TextView description = (TextView)convertView.findViewById(R.id.description);
			description.setText(Html.fromHtml(item.getBody()));
		}
		return convertView;
	}

	private void loadNextPage()
	{
		if(this.canLoadMore && this.currentLoadingPage == null)
		{
			this.currentLoadingPage = new Page(this.client, this.pagesLoaded,this.categoryId);
			this.currentLoadingPage.setRequestListener(this);
			this.currentLoadingPage.pullFromServer(false);			
		}
	}

	@Override
	public void onEntityPulled(AbstractBaseDrupalEntity entity, ResponseData data)
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
	public void onEntityPushed(AbstractBaseDrupalEntity entity, ResponseData data)
	{}

	@Override
	public void onEntityPatched(AbstractBaseDrupalEntity entity, ResponseData data)
	{}

	@Override
	public void onEntityRemoved(AbstractBaseDrupalEntity entity, ResponseData data)
	{}

	@Override
	public void onRequestFailed(AbstractBaseDrupalEntity entity, VolleyError error)
	{
		this.canLoadMore = false;
	}
	
}
