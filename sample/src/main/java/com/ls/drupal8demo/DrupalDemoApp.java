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

package com.ls.drupal8demo;

import com.android.volley.RequestQueue;
import com.ls.drupal.DrupalClient;
import com.ls.http.base.BaseRequest;
import com.ls.util.image.DrupalImageView;
import com.ls.util.internal.VolleyResponseUtils;

import android.app.Application;

/**
 * Created on 27.04.2015.
 */
public class DrupalDemoApp extends Application {

    private static int IMAGE_CACHE_SIZE = 1024*1024*20;//5MB

    @Override
    public void onCreate() {
        super.onCreate();
//        RequestQueue imageQueue = VolleyResponseUtils.newRequestQueue(this,null,IMAGE_CACHE_SIZE);
//        DrupalClient imageClient = new DrupalClient(null,imageQueue, BaseRequest.RequestFormat.JSON,null);
//        DrupalImageView.setupSharedClient(imageClient);
    }
}
