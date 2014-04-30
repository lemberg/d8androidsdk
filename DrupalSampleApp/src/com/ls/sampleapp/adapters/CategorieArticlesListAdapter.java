package com.ls.sampleapp.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ls.sampleapp.article.ArticlePreview;

public class CategorieArticlesListAdapter extends BaseAdapter
{

	int pagesLoaded = 0;
	private List<ArticlePreview> data;
	
	public CategorieArticlesListAdapter(String categoryId)
	{
		data = new ArrayList<ArticlePreview>();
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
		// TODO Auto-generated method stub
		return null;
	}

	
}
