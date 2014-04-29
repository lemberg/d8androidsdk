package com.ls.sampleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ls.drupal.AbstractDrupalEntity;
import com.ls.drupal.AbstractDrupalEntity.OnEntityRequestListener;
import com.ls.drupal.DrupalClient;
import com.ls.sampleapp.article.Page;

public class ArticleFragment extends Fragment implements OnEntityRequestListener
{
	private final static String PAGE_ID_KEY = "page_id";
	private Page page;
		
	private TextView title;
	private WebView content;
	
	public static ArticleFragment newInstance(int pageId)
	{
		ArticleFragment fragment = new ArticleFragment();
	    Bundle args = new Bundle();
	    args.putInt(PAGE_ID_KEY, pageId);
	    fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);		
		int pageId = this.getArguments().getInt(PAGE_ID_KEY);
		this.page = new Page(new DrupalClient(AppConstants.SERVER_BASE_URL,this.getActivity()), pageId);
		this.page.setListener(this);
		this.page.getDataFromServer(false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup result = (ViewGroup)inflater.inflate(R.layout.fragment_article, container, false);
		this.title = (TextView) result.findViewById(R.id.title);				
		this.content = (WebView) result.findViewById(R.id.contentView);
		this.resetPageContent();
		return result;
	}

	private void resetPageContent()
	{
		if(title != null)
		{
			this.title.setText(this.page.getTitle());
			Log.e("Setting","title:"+this.page.getTitle());
			
		}
		
		if(content != null)
		{
			this.content.loadData(this.page.getBody(), null, this.page.getCharset());
		}
	}
	
	@Override
	public void onEntityFetched(AbstractDrupalEntity entity)
	{
		this.resetPageContent();
	}

	@Override
	public void onEntityPosted(AbstractDrupalEntity entity)
	{}

	@Override
	public void onEntityPatched(AbstractDrupalEntity entity)
	{}

	@Override
	public void onEntityRemoved(AbstractDrupalEntity entity)
	{}

	@Override
	public void onRequestFailed(VolleyError error, AbstractDrupalEntity entity)
	{
		Toast.makeText(this.getActivity(), "Page fetch failed", Toast.LENGTH_SHORT);		
	}
}
