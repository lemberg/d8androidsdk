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

package com.ls.util.image;

import com.ls.drupal.DrupalClient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created on 22.04.2015.
 */
public class DrupalImageView extends ImageView {

    private static DrupalClient sharedClient;

    /**
     * Use this method to provide default drupal client, used by all image views.
     * @param client to be used in order to load images.
     */
    public static void setupSharedClient(DrupalClient client)
    {
        DrupalImageView.sharedClient = client;
    }

    private DrupalClient localClient;

    private ImageContainer imageContainer;

    public DrupalImageView(Context context) {
        super(context);
    }

    public DrupalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrupalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageWithPath(String imagePath)
    {
        if (this.isInEditMode()) {
            return;
        }

        DrupalClient client = this.getClient();
        if(client == null)
        {
            throw new IllegalStateException("No DrupalClient set. Please provide local or shared DrupalClient to perform loading");
        }
    }

    private DrupalClient getClient()
    {
        if(this.localClient != null)
        {
            return this.localClient;
        }

        return DrupalImageView.sharedClient;
    }


    public DrupalClient getLocalClient() {
        return localClient;
    }

    public void setLocalClient(DrupalClient localClient) {
        this.localClient = localClient;
    }

    private class ImageContainer
    {
        Drawable image;
        String url;

        ImageContainer(String url)
        {
            this.url = url;
        }
    }
}
