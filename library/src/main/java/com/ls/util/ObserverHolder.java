package com.ls.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lemberg-i5 on 07.10.2014.
 */
public class ObserverHolder<ObserverClass> {
    protected List<ObserverClass> observers;

    public ObserverHolder()
    {
        observers = new LinkedList<ObserverClass>();
    }

    /**
     *
     * @param theObserver
     * @return false if observer was already registered
     */
    public synchronized boolean registerObserver(ObserverClass theObserver)
    {
        boolean isAlreadyRegistered = false;
        for(ObserverClass observer:observers)
        {
            if(observer==theObserver)
            {
                isAlreadyRegistered = true;
                break;
            }
        }

        if(!isAlreadyRegistered)
        {
            this.observers.add(theObserver);
        }
        return !isAlreadyRegistered;
    }

    public synchronized void unregisterObserver(ObserverClass theObserver)
    {
        this.observers.remove(theObserver);
    }

    public synchronized void clearAll()
    {
        this.observers.clear();
    }

    public synchronized void notifyAllObservers(ObserverNotifier<ObserverClass> notifier)
    {
        List<ObserverClass> observersCopy = new LinkedList<ObserverClass>(observers);
        for(ObserverClass observer:observersCopy)
        {
            if(observer != null)
            {
                notifier.onNotify(observer);
            }
        }
    }

   public static interface ObserverNotifier<ObserverClass>
   {
       public void onNotify(ObserverClass observer);
   }
}
