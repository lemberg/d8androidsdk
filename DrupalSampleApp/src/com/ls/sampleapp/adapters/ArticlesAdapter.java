package com.ls.sampleapp.adapters;

import com.ls.sampleapp.AppConstants;
import com.ls.sampleapp.ArticleFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ArticlesAdapter extends FragmentPagerAdapter
{
	public ArticlesAdapter(FragmentManager fm)
	{
		super(fm);		
	}

	@Override
	public Fragment getItem(int arg0)
	{
		
		return ArticleFragment.newInstance(AppConstants.NODE_IDS[arg0]);
	}

	@Override
	public int getCount()
	{		
		return AppConstants.NODE_IDS.length;
	}
	
}
