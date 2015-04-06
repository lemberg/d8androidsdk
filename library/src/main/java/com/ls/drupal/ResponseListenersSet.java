package com.ls.drupal;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created on 27.03.2015.
 */
public class ResponseListenersSet {
    private Map<Request, List<DrupalClient.OnResponseListener>> listeners;
    public ResponseListenersSet()
    {
        listeners = new HashMap<Request, List<DrupalClient.OnResponseListener>>();
    }

    /**
     *
     * @param request
     * @param listener listener to register for request
     * @return true if new request was registered, false otherwise
     */
    public boolean registerListenerForRequest(Request request,DrupalClient.OnResponseListener listener)
    {
        boolean result = false;
        List<DrupalClient.OnResponseListener> listenersList = listeners.get(request);
        if(listenersList == null)
        {
            listenersList = new LinkedList<DrupalClient.OnResponseListener>();
            listeners.put(request,listenersList);
            result = true;
        }

        listenersList.add(listener);
        return result;
    }

    /**
     *
     * @param request
     * @return Listeners, registered for this request
     */
    public List<DrupalClient.OnResponseListener> getListenersForRequest(Request request)
    {
        return listeners.get(request);
    }

    /**
     * Remove all listeners for request
     * @param request
     */
    public void removeListenersForRequest(Request request)
    {
        listeners.remove(request);
    }

    public void removeAllListeners()
    {
        listeners.clear();
    }

    public int registeredRequestCount()
    {
        return listeners.size();
    }
}
