package com.ls.sampleapp.adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ls.sampleapp.vo.Category;

public class ArticlesCategoryAdapter extends FragmentPagerAdapter
{
	private ArrayList<Category> categories;
	
	private void initcategories()
	{
		categories = new ArrayList<Category>(5);
		categories.add(new Category("All Posts", "1"));
		categories.add(new Category("Featured Articles", "2"));
		categories.add(new Category("Industry News", "3"));
		categories.add(new Category("Our Posts", "4"));
		categories.add(new Category("tech Notes", "5"));		
	}

	public ArticlesCategoryAdapter(FragmentManager fm)
	{
		super(fm);
		this.initcategories();
	}

	@Override
	public Fragment getItem(int arg0)
	{
		
		return null;
	}

	@Override
	public int getCount()
	{		
		return categories.size();
	}
	
}
