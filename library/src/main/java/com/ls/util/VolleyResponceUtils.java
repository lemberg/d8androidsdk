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

package com.ls.util;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.net.HttpURLConnection;

/**
 * Created by Lemberg-i5 on 07.10.2014.
 */
public class VolleyResponceUtils {
    public static boolean isNetworkingError(VolleyError volleyError)
    {
        if (volleyError.networkResponse == null) {
            if (volleyError instanceof TimeoutError) {
               return true;
            }

            if (volleyError instanceof NoConnectionError) {
                return true;
            }

            if (volleyError instanceof NetworkError) {
                return true;
            }

        }
        return false;
    }

    public static boolean isAuthError(VolleyError volleyError){
        return volleyError != null && volleyError.networkResponse != null
                && volleyError.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED;
    }

    public static boolean isNotFoundError(VolleyError volleyError){

        return false;
//        if (volleyError.getCause() instanceof UnknownHostException) {
//             return true;
//        }
//
//        return volleyError != null && volleyError.networkResponse != null
//                && volleyError.networkResponse.statusCode == HttpURLConnection.HTTP_NOT_FOUND;
    }
}
