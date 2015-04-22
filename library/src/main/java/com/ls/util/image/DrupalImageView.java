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

import com.android.volley.VolleyError;
import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalImageEntity;
import com.ls.http.base.ResponseData;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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

        if(this.imageContainer != null && this.imageContainer.url.equals(imagePath))
        {
            return;
        }

        this.cancelLoading();

        if(TextUtils.isEmpty(imagePath))
        {
            this.setImageDrawable(null);
            this.imageContainer = null;
            return;
        }

        this.imageContainer = new ImageContainer(imagePath,client);
        this.startLoading();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        cancelLoading();
        this.imageContainer = null;
        super.setImageDrawable(drawable);
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

    public void cancelLoading()
    {
        if(this.imageContainer != null)
        {
            this.imageContainer.cancelLoad();
        }
    }

    public void startLoading()
    {
        if(this.imageContainer != null)
        {
            this.imageContainer.loadImage();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.cancelLoading();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.startLoading();
    }

    private class ImageContainer
    {
        DrupalImageEntity image;
        String url;
        DrupalClient client;

        ImageContainer(String url,DrupalClient client)
        {
            this.url = url;
            this.client = client;
            DrupalImageEntity entity = new DrupalImageEntity(client);
            entity.setImagePath(url);
        }

        void cancelLoad()
        {
            image.cancellAllRequests();
        }

        void loadImage()
        {
            ImageLoadingListener listener = new ImageLoadingListener(url);
            image.pullFromServer(false,url,listener);
        }
    }

    private class ImageLoadingListener implements AbstractBaseDrupalEntity.OnEntityRequestListener
    {
        private String acceptableURL;
        ImageLoadingListener(String url)
        {
            this.acceptableURL = url;
        }

        @Override
        public void onRequestCompleted(AbstractBaseDrupalEntity entity, Object tag, ResponseData data) {
            if(checkCurrentURL())
            {
                DrupalImageView.super.setImageDrawable(((DrupalImageEntity) entity).getManagedData());
            }
        }

        @Override
        public void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, VolleyError error) {

        }

        @Override
        public void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag) {

        }

        private boolean checkCurrentURL()
        {
            return imageContainer != null && imageContainer.url != null && imageContainer.url.equals(acceptableURL);
        }
    }

}
