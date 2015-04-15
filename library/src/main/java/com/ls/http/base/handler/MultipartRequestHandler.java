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

package com.ls.http.base.handler;

import com.ls.http.base.IPostableItem;
import com.ls.http.base.RequestHandler;
import com.ls.util.L;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

class MultipartRequestHandler extends RequestHandler
{

    private MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    private HttpEntity httpentity;


    public MultipartRequestHandler() {
        this.entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    }

    @Override
    public void setObject(Object object) {
        super.setObject(object);
//        entity.addTextBody();
//
//        //After setting
//        httpentity = entity.build();
    }


    @Override
	public String stringBodyFromItem()
	{
		if(implementsPostableInterface())
		{
			IPostableItem item = (IPostableItem)this.object;
			return item.toPlainText();
		}else{
			return this.object.toString();
		}
	}

    @Override
    public String getBodyContentType(String defaultCharset) {
        return httpentity.getContentType().getValue();
    }

    @Override
    public byte[] getBody(String defaultCharset) throws UnsupportedEncodingException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpentity.writeTo(bos);
        } catch (IOException e) {
            L.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }
}
