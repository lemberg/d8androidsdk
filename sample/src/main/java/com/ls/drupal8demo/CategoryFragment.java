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

import com.ls.drupal.DrupalClient;
import com.ls.drupal8demo.adapters.CategoryArticlesListAdapter;
import com.ls.drupal8demo.article.ArticlePreview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CategoryFragment extends Fragment implements OnItemClickListener {

	private final static String CATEGORY_ID_KEY = "category_id";

	private DrupalClient mClient;

	public static CategoryFragment newInstance(String categoryId) {
		CategoryFragment fragment = new CategoryFragment();
		Bundle args = new Bundle();
		args.putString(CATEGORY_ID_KEY, categoryId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		String categoryId = getArguments().getString(CATEGORY_ID_KEY);
		ViewGroup result = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);
		ListView list = (ListView) result.findViewById(R.id.listView);
		list.setOnItemClickListener(this);

		mClient = new DrupalClient(AppConstants.SERVER_BASE_URL, getActivity());
		View noArticlesView = result.findViewById(R.id.emptyView);
		list.setAdapter(new CategoryArticlesListAdapter(categoryId, mClient, getActivity(), noArticlesView));
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ArticlePreview article = (ArticlePreview) parent.getItemAtPosition(position);

		Intent intent = ArticleActivity.getExecutionIntent(getActivity(), article);
		getActivity().startActivity(intent);
	}

	@Override
	public void onDestroyView() {
		if (mClient != null) {
			mClient.cancelAll();
		}
		super.onDestroyView();
	}
}
