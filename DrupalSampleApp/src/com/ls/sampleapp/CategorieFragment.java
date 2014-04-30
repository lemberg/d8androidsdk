package com.ls.sampleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ls.drupal.DrupalClient;
import com.ls.sampleapp.adapters.CategorieArticlesListAdapter;

public class CategorieFragment extends Fragment
{
	private final static String CATEGORIE_ID_KEY = "categorie_id";
	

	public static CategorieFragment newInstance(String categorieId)
	{
		CategorieFragment fragment = new CategorieFragment();
		Bundle args = new Bundle();
		args.putString(CATEGORIE_ID_KEY, categorieId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		String categorieId = this.getArguments().getString(CATEGORIE_ID_KEY);
		ViewGroup result = (ViewGroup) inflater.inflate(R.layout.fragment_categorie, container, false);
		ListView list = (ListView) result.findViewById(R.id.listView);	
		
		DrupalClient client = new DrupalClient(AppConstants.SERVER_BASE_URL, this.getActivity());
		list.setAdapter(new CategorieArticlesListAdapter(categorieId,client,this.getActivity()));
		return result;
	}
}
