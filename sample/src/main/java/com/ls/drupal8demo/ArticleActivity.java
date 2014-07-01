/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Lemberg Solutions Limited
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ls.drupal8demo;

import com.ls.drupal8demo.article.ArticlePreview;
import com.ls.http.base.SharedGson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class ArticleActivity extends ActionBarActivity
{
	private final static String ARTICLE_PREVIEW_KEY = "ARTIVLE_PREVIEW";

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
		this.InitHeaderWithPreview(preview);

		View content = new FrameLayout(this);
		content.setId(contentId);
		this.setContentView(content);

		ArticleFragment articleFragment = ArticleFragment.newInstance(preview.getNid(), preview.getImage());
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
}
