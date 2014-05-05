package com.ls.sampleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ls.drupal.DrupalClient;
import com.ls.sampleapp.adapters.CategoryArticlesListAdapter;

public class CategoryFragment extends Fragment
{
	private final static String CATEGORY_ID_KEY = "category_id";
	

	public static CategoryFragment newInstance(String categoryId)
	{
		CategoryFragment fragment = new CategoryFragment();
		Bundle args = new Bundle();
		args.putString(CATEGORY_ID_KEY, categoryId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		String categoryId = this.getArguments().getString(CATEGORY_ID_KEY);
		ViewGroup result = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);
		ListView list = (ListView) result.findViewById(R.id.listView);	
		
		DrupalClient client = new DrupalClient(AppConstants.SERVER_BASE_URL, this.getActivity());
		list.setAdapter(new CategoryArticlesListAdapter(categoryId,client,this.getActivity()));
		return result;
	}
}
