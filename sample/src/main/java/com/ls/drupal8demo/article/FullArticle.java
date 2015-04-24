/*
 * The MIT License (MIT)
 *  Copyright (c) 2014 Lemberg Solutions Limited
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package com.ls.drupal8demo.article;

import com.ls.drupal.AbstractDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupal8demo.util.ModelUtils;
import com.ls.http.base.BaseRequest.RequestMethod;

import junit.framework.Assert;

import java.util.Map;

public class FullArticle {

	/**
	 * Such complicated structures are just a workaround to map all server data to objects. Server can be configured to
	 * get rid of them.
	 */
	private String nid;
	private String body;
	private String title;
	private String field_blog_date;
	private String field_blog_author;
    private String field_image;

	public FullArticle(String nodeId) {
		this.nid = nodeId;
	}

	public String getBody() {
		return this.body;
	}

	public String getTitle() {
		return this.title;
	}

	public String getAuthor() {
		return this.field_blog_author;
	}

	public String getDate() {
		return this.field_blog_date;
	}

	public String getNid() {
		return this.nid;
	}

    public String getImage() {
        return field_image;
    }

}
