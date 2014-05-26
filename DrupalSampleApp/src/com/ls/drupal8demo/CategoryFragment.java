package com.ls.drupal8demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ls.drupal.DrupalClient;
import com.ls.drupal8demo.R;
import com.ls.drupal8demo.adapters.CategoryArticlesListAdapter;
import com.ls.drupal8demo.article.ArticlePreview;

public class CategoryFragment extends Fragment implements OnItemClickListener
{
	private final static String CATEGORY_ID_KEY = "category_id";
	
	private DrupalClient client;

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
		list.setOnItemClickListener(this);
		
		this.client = new DrupalClient(AppConstants.SERVER_BASE_URL, this.getActivity());
		View noArticlesView = result.findViewById(R.id.emptyView);
		list.setAdapter(new CategoryArticlesListAdapter(categoryId,client,this.getActivity(),noArticlesView));
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{		
		ArticlePreview article = (ArticlePreview)parent.getItemAtPosition(position);
		
		Intent intent = ArticleActivity.getExecutionIntent(getActivity(), article);
		this.getActivity().startActivity(intent);
	}
	
	@Override
	public void onDestroyView()
	{
		if(this.client != null)
		{
			this.client.cancelAll();
		}
		super.onDestroyView();
	}
}
