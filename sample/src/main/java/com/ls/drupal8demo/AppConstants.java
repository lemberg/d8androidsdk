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

public class AppConstants {

	public final static String SERVER_BASE_URL = "http://d8.uat.link/";//"http://vh015.uk.dev-ls.co.uk/";

	public static enum CATEGORY {
		ALL_POSTS("All Posts", null),
		INDUSTRY_NEWS("Industry News", "1"),
		OUR_POSTS("Our Posts", "2"),
		TECH_NOTES("Tech notes", "3");

		public final String name;
		public final String id;

		CATEGORY(String theName, String theId) {
			name = theName;
			this.id = theId;
		}
	}
}
