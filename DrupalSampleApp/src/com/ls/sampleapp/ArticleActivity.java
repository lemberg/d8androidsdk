package com.ls.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;

public class ArticleActivity extends ActionBarActivity
{
	private final static String NID_KEY = "NID";
	private final static String TITLE_KEY = "TITLE";

	public static Intent getExecutionIntent(Context theContext, String theNid, String theTitle)
	{
		Intent intent = new Intent(theContext, ArticleActivity.class);
		intent.putExtra(NID_KEY, theNid);
		intent.putExtra(TITLE_KEY, theTitle);
		return intent;
	}

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		final int contentId = 1001;
		final ActionBar bar = this.getSupportActionBar();
		
		String title = this.getIntent().getStringExtra(TITLE_KEY);
		if(title != null)
		{
			title = Html.fromHtml(title).toString();
		}
		bar.setTitle(title);

		View content = new FrameLayout(this);
		content.setId(contentId);
		this.setContentView(content);

		ArticleFragment articleFragment = ArticleFragment.newInstance(this.getIntent().getStringExtra(NID_KEY));
		FragmentTransaction trans = this.getSupportFragmentManager().beginTransaction();
		trans.add(contentId, articleFragment);
		trans.commit();
	}
}
