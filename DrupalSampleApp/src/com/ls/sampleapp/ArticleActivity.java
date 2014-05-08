package com.ls.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

public class ArticleActivity extends FragmentActivity
{
	private final static String NID_KEY = "NID";
	
	public static Intent getExecutionIntent(Context theContext, String theNid)
	{
		Intent intent = new Intent(theContext,ArticleActivity.class);
		intent.putExtra(NID_KEY, theNid);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle arg0)
	{		
		super.onCreate(arg0);
		final int contentId = 1001;
		View content = new FrameLayout(this);
		content.setId(contentId);
		this.setContentView(content);

		ArticleFragment articleFragment = ArticleFragment.newInstance(this.getIntent().getStringExtra(NID_KEY));
		FragmentTransaction trans = this.getSupportFragmentManager().beginTransaction();
		trans.add(contentId, articleFragment);
		trans.commit();
	}
}
