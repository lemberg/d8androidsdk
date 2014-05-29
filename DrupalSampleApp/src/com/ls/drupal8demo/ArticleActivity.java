package com.ls.drupal8demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.ls.drupal8demo.article.ArticlePreview;
import com.ls.http.base.SharedGson;

public class ArticleActivity extends ActionBarActivity
{	
	private final static String ARTICLE_PREVIEW_KEY = "ARTIVLE_PREVIEW";
	
	private String articleURL;
	
	private ShareActionProvider mShareActionProvider;

	public static Intent getExecutionIntent(Context theContext, ArticlePreview article)
	{
		Intent intent = new Intent(theContext, ArticleActivity.class);
		intent.putExtra(ARTICLE_PREVIEW_KEY, SharedGson.getGson().toJson(article));		
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		final int contentId = 1001;
		final ActionBar bar = this.getSupportActionBar();
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);		
		String previewString = this.getIntent().getStringExtra(ARTICLE_PREVIEW_KEY);
		
		ArticlePreview preview = SharedGson.getGson().fromJson(previewString, ArticlePreview.class);
		this.articleURL = preview.getArticleURL();
		this.InitHeaderWithPreview(preview);

		View content = new FrameLayout(this);
		content.setId(contentId);
		this.setContentView(content);

		ArticleFragment articleFragment = ArticleFragment.newInstance(preview.getNid(),preview.getImage());
		FragmentTransaction trans = this.getSupportFragmentManager().beginTransaction();
		trans.add(contentId, articleFragment);
		trans.commit();
	}
	
	private void InitHeaderWithPreview(ArticlePreview preview)
	{
		final ActionBar bar = this.getSupportActionBar();
		String author = preview.getAuthor();
		
		StringBuilder subtitleText  = new StringBuilder();
		if(author != null && !author.isEmpty())
		{
			subtitleText.append("By " + author);
		}		
		subtitleText.append(" // "+preview.getDate());

		bar.setSubtitle(subtitleText);
		
		String title = preview.getCategory();
		if (title != null)
		{
			title = Html.fromHtml(title).toString();
		}
		bar.setTitle(title);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	    
	    getMenuInflater().inflate(R.menu.share_menu, menu);
	    MenuItem item = menu.findItem(R.id.menu_item_share);
	    
	    mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
	    if(mShareActionProvider != null)
	    {
	    	mShareActionProvider.setShareIntent(getShareIntent());
	    }
	    return true;
	}
	
	private Intent getShareIntent() {	   
	    	String message = "Check this article:"+this.articleURL;
	    	Intent share = new Intent(Intent.ACTION_SEND);
	    	share.setType("text/plain");
	    	share.putExtra(Intent.EXTRA_TEXT, message);	   
	    	return share;
	}
}
