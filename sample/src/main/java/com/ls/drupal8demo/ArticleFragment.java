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

import com.android.volley.VolleyError;
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.AbstractBaseDrupalEntity.OnEntityRequestListener;
import com.ls.drupal.DrupalClient;
import com.ls.drupal8demo.article.ArticleWrapper;
import com.ls.http.base.ResponseData;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ArticleFragment extends Fragment implements OnEntityRequestListener {

	private final static String ARTICLE_CONTENT_STUB = "%ARTICLE_BODY%";
	private final static String ARTICLE_TITLE_STUB = "%ARTICLE_TITLE%";
	private final static String ARTICLE_IMAGE_STUB = "%ARTICLE_IMAGE_URL%";

	private final static String PAGE_ID_KEY = "page_id";
	private final static String BLOG_IMAGE_KEY = "blog_image";
	private ArticleWrapper mPage;
	private DrupalClient mClient;

	private WebView mContent;
	private View mProgressView;

	public static ArticleFragment newInstance(String pageId, String blogImage) {
		ArticleFragment fragment = new ArticleFragment();
		Bundle args = new Bundle();
		args.putString(PAGE_ID_KEY, pageId);
		args.putString(BLOG_IMAGE_KEY, blogImage);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String pageId = getArguments().getString(PAGE_ID_KEY);
		mClient = new DrupalClient(AppConstants.SERVER_BASE_URL, getActivity());
		mPage = new ArticleWrapper(mClient, pageId);
		mPage.pullFromServer(false, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup result = (ViewGroup) inflater.inflate(R.layout.fragment_article, container, false);
		mContent = (WebView) result.findViewById(R.id.contentView);
		mProgressView = result.findViewById(R.id.progressView);
		resetPageContent();
		return result;
	}

	private void resetPageContent() {
		if (mPage.getDrupalClient().getActiveRequestsCount() > 0) {
			showLoadingDialog();
		} else {
			dismissLoadingDialog();
		}

		if (mContent != null && mPage.getBody() != null) {
			String template = loadPageTemplate();
			String body = template.replaceFirst(ARTICLE_CONTENT_STUB, mPage.getBody());
			body = body.replaceAll(ARTICLE_TITLE_STUB, mPage.getTitle());

			String imageURL = getArguments().getString(BLOG_IMAGE_KEY);
			body = body.replaceAll(ARTICLE_IMAGE_STUB, imageURL);

			mContent.loadDataWithBaseURL("file:///android_asset/fonts", body, "text/html", mPage.getCharset(), null);
		}
	}

	private String loadPageTemplate() {
		InputStream json = null;
		try {
			StringBuilder buf = new StringBuilder();
			json = getActivity().getAssets().open("blog.html");
			BufferedReader in = new BufferedReader(new InputStreamReader(json));
			String str;

			while ((str = in.readLine()) != null) {
				buf.append(str);
			}
			return buf.toString();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (json != null) {
				try {
					json.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	// Request listener

	public void showLoadingDialog() {
		if (mProgressView != null) {
			mProgressView.setVisibility(View.VISIBLE);
		}
	}

	public void dismissLoadingDialog() {
		if (mProgressView != null) {
			mProgressView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onRequestCompleted(AbstractBaseDrupalEntity entity, Object tag, ResponseData data) {
		this.resetPageContent();
	}

	@Override
	public void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, VolleyError error) {
		Toast.makeText(getActivity(), "Page fetch failed", Toast.LENGTH_SHORT).show();
		resetPageContent();
	}

	@Override
	public void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag) {
	}

	@Override
	public void onDestroy() {
		mClient.cancelAll();
		super.onDestroy();
	}
}
