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
import com.ls.drupal.AbstractDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupal8demo.util.ModelUtils;
import com.ls.http.base.BaseRequest.RequestMethod;

import junit.framework.Assert;

import java.util.Map;

public class ArticleWrapper extends AbstractDrupalArrayEntity<FullArticle> {

    private static FullArticle STUB_ARTICLE = new FullArticle("");

	private String nid;

	public ArticleWrapper(DrupalClient client, String nodeId) {
		super(client,1);
		this.nid = nodeId;
	}

	@Override
	protected String getPath() {
		Assert.assertNotNull("Node id can't be null while requesting item", this.nid);
		return "node" + "/" + this.getNid();
	}

	@Override
	protected Map<String, String> getItemRequestPostParameters() {
		return null;
	}

	@Override
	protected Map<String, String> getItemRequestGetParameters(RequestMethod method) {
		return null;
	}

	public String getBody() {
		return getItem().getBody();
	}

	public String getTitle() {
		return getItem().getTitle();
	}

	public String getAuthor() {
		return getItem().getAuthor();
	}

	public String getDate() {
		return getItem().getDate();
	}

	public String getNid() {
		return this.nid;
	}

    public String getImage() {
        return getItem().getImage();
    }

    private FullArticle getItem()
    {
        if(!this.isEmpty())
        {
            return this.get(0);
        }

        return STUB_ARTICLE;
    }
}
