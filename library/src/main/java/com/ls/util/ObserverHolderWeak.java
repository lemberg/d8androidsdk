package com.ls.util;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lemberg-i5 on 07.10.2014.
 */
public class ObserverHolderWeak<ObserverClass> {
    protected List<WeakReference<ObserverClass>> observers;

    public ObserverHolderWeak()
    {
        observers = new LinkedList<WeakReference<ObserverClass>>();
    }

    /**
     *
     * @param theObserver
     * @return false if observer was already registered
     */
    public synchronized boolean registerObserver(ObserverClass theObserver)
    {
        List<WeakReference<ObserverClass>> toRemove = new LinkedList<WeakReference<ObserverClass>>();

        boolean isAlreadyRegistered = false;
        for(WeakReference<ObserverClass> observerRef:observers)
        {
            ObserverClass observer = observerRef.get();
            if(observer != null)
            {
                if(observer==theObserver)
                {
                    isAlreadyRegistered = true;
                }
            }else{
                toRemove.add(observerRef);
            }
        }
        this.observers.removeAll(toRemove);

        if(!isAlreadyRegistered)
        {
            this.observers.add(new WeakReference<ObserverClass>(theObserver));
        }
        return !isAlreadyRegistered;
    }

    public synchronized void unregisterObserver(ObserverClass theObserver)
    {
        List<WeakReference<ObserverClass>> toRemove = new LinkedList<WeakReference<ObserverClass>>();

        for(WeakReference<ObserverClass> observerRef:observers)
        {
            ObserverClass observer = observerRef.get();
            if(observer == null || observer == theObserver)
            {
                toRemove.add(observerRef);
            }
        }
        this.observers.removeAll(toRemove);
    }

    public synchronized void clearAll()
    {
        this.observers.clear();
    }

    public synchronized void notifyAllObservers(ObserverNotifier<ObserverClass> notifier)
    {
        for(WeakReference<ObserverClass> observerRef:observers)
        {
            ObserverClass observer = observerRef.get();
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
