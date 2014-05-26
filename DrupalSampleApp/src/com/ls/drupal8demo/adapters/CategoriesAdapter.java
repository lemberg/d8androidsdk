package com.ls.drupal8demo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ls.drupal8demo.AppConstants;
import com.ls.drupal8demo.CategoryFragment;

public class CategoriesAdapter extends FragmentPagerAdapter
{
	public CategoriesAdapter(FragmentManager fm)
	{
		super(fm);		
	}

	@Override
	public Fragment getItem(int arg0)
	{
		
		return CategoryFragment.newInstance(AppConstants.CATEGORIE.values()[arg0].id);
	}

	@Override
	public int getCount()
	{		
		return AppConstants.CATEGORIE.values().length;
	}
	
}
