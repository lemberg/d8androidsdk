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

package com.ls.drupal8demo.article;

import com.ls.drupal.AbstractDrupalArrayEntity;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.BaseRequest.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public class Page extends AbstractDrupalArrayEntity<ArticlePreview> {

	transient private int mPageNumber;
	transient private String mCategoryId;

	public Page(DrupalClient client, int thePageNumber, String theCategoryId) {
		super(client, 5);
		mPageNumber = thePageNumber;
		mCategoryId = theCategoryId;
	}

	@Override
	protected String getPath() {
		if (mCategoryId == null) {
			return "blog-rest";
		} else {
			return "category/" + mCategoryId;
		}
	}

	@Override
	protected Map<String, String> getItemRequestPostParameters() {
		return null;
	}

	@Override
	protected Map<String, String> getItemRequestGetParameters(RequestMethod method) {
		switch (method) {
			case GET:
				Map<String, String> result = new HashMap<String, String>();
				result.put("page", Integer.toString(mPageNumber));
				return result;
			default:
				return null;
		}
	}

	public int getPageNumber() {
		return mPageNumber;
	}

}
