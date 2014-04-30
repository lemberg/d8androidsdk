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
import com.ls.sampleapp.article.Article;

public class ArticleFragment extends Fragment implements OnEntityRequestListener
{
	final static String PROGRESS_DIALOG_TAG = "progress_dialog";
	private final static String PAGE_ID_KEY = "page_id";
	private Article page;

	private TextView title;
	private WebView content;
	
	private View progressView;

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
		this.page = new Article(new DrupalClient(AppConstants.SERVER_BASE_URL, this.getActivity()), pageId);
		this.page.setRequestListener(this);
//		this.page.setProgressListener(this);
		this.page.getDataFromServer(false);
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
		if(this.page.getActiveRequestCount() > 0)
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

	@Override
	public void onEntityFetched(AbstractDrupalEntity entity)
	{
		Log.d(this.getClass().getName(), "Page fetched");
		this.resetPageContent();
	}

	@Override
	public void onEntityPosted(AbstractDrupalEntity entity)	{}

	@Override
	public void onEntityPatched(AbstractDrupalEntity entity){}

	@Override
	public void onEntityRemoved(AbstractDrupalEntity entity){}

	@Override
	public void onRequestFailed(VolleyError error, AbstractDrupalEntity entity)
	{
		Toast.makeText(this.getActivity(), "Page fetch failed", Toast.LENGTH_SHORT).show();
	}
	
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
	
	// Request progress listener

//		@Override
//		public void onRequestStarted(AbstractDrupalEntity theEntity, int activeRequests)
//		{		
//			if (activeRequests == 1)
//			{
//				showLoadingDialog();
//			}
//		}
//
//		@Override
//		public void onRequestFinished(AbstractDrupalEntity theEntity, int activeRequests)
//		{		
//			if (activeRequests == 0)
//			{
//				dismissLoadingDialog();
//			}
//		}
}
