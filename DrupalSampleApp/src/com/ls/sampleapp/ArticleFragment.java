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
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.AbstractBaseDrupalEntity.OnEntityRequestListener;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.ResponseData;
import com.ls.sampleapp.article.Article;

public class ArticleFragment extends Fragment implements OnEntityRequestListener
{
	final static String PROGRESS_DIALOG_TAG = "progress_dialog";
	private final static String PAGE_ID_KEY = "page_id";
	private Article page;
	private DrupalClient client;

	private TextView title;
	private WebView content;
	
	private View progressView;

	public static ArticleFragment newInstance(String pageId)
	{
		ArticleFragment fragment = new ArticleFragment();
		Bundle args = new Bundle();
		args.putString(PAGE_ID_KEY, pageId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		String pageId = this.getArguments().getString(PAGE_ID_KEY);		
		this.client = new DrupalClient(AppConstants.SERVER_BASE_URL, this.getActivity());
		this.page = new Article(client, pageId);		
//		this.page.setProgressListener(this);
		this.page.pullFromServer(false,null,this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup result = (ViewGroup) inflater.inflate(R.layout.fragment_article, container, false);
		this.title = (TextView) result.findViewById(R.id.pageTitle);
		this.content = (WebView) result.findViewById(R.id.contentView);
		this.progressView = result.findViewById(R.id.progressView);
		this.resetPageContent();
		return result;
	}

	private void resetPageContent()
	{
		if(this.page.getDrupalClient().getActiveRequestsCount() > 0)
		{
			this.showLoadingDialog();
		}else{
			this.dismissLoadingDialog();
		}
		
		Log.d("ArticleFragment", "Page title:" + this.page.getTitle());
		if (title != null)
		{
			this.title.setText(this.page.getTitle());
		}

		if (content != null && this.page.getBody() != null)
		{
			this.content.loadData(this.page.getBody(), "text/html", this.page.getCharset());
		}
	}

	// Request listener
	
	public void showLoadingDialog()
	{
		if(this.progressView != null)
		{
			this.progressView.setVisibility(View.VISIBLE);
		}
	}

	public void dismissLoadingDialog()
	{
		if(this.progressView != null)
		{
			this.progressView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onRequestComplete(AbstractBaseDrupalEntity entity, Object tag, ResponseData data)
	{
		this.resetPageContent();
	}

	@Override
	public void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, VolleyError error)
	{
		Toast.makeText(this.getActivity(), "Page fetch failed", Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag)
	{}

	@Override
	public void onDestroy()
	{
		this.client.cancelAll();
		super.onDestroy();
	}
}
