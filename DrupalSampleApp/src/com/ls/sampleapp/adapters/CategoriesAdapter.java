package com.ls.sampleapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ls.sampleapp.AppConstants;
import com.ls.sampleapp.CategoryFragment;

public class CategoriesAdapter extends FragmentPagerAdapter
{
	public CategoriesAdapter(FragmentManager fm)
	{
		super(fm);		
	}

	@Override
	public Fragment getItem(int arg0)
	{
		
		return CategoryFragment.newInstance(AppConstants.CATEGORIES_LIST[arg0]);
	}

	@Override
	public int getCount()
	{		
		return AppConstants.CATEGORIES_LIST.length;
	}
	
}
