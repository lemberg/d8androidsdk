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
