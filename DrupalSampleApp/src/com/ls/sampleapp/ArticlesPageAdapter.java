package com.ls.sampleapp;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ls.sampleapp.article.Page;

public class ArticlesPageAdapter extends FragmentPagerAdapter
{
	private List<Page> pages;

	public ArticlesPageAdapter(FragmentManager fm)
	{
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
}
