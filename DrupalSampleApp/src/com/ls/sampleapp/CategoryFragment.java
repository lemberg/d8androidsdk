package com.ls.sampleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ls.sampleapp.vo.Category;

public class CategoryFragment extends Fragment
{
	private final String  CATEGORY_ID_KEY = "CATEGORY_ID";
	
	public CategoryFragment instance(Category category)
	{
		CategoryFragment result = new CategoryFragment();
		
		Bundle args = new Bundle();
		args.putString(CATEGORY_ID_KEY, category.getId());		
		result.setArguments(args);
		
		return result;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ListView view = (ListView)inflater.inflate(R.layout.fragment_catregory, null);
		return view;
	}
		
}
