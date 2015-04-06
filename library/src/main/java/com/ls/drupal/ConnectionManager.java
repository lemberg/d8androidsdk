package com.ls.drupal;


import com.ls.util.ObserverHolder;

/**
 * Created by Lemberg-i5 on 07.10.2014.
 * Note: this isn't the best implementation of singleton but it's enough for our application
 */
public class ConnectionManager {
    private static ConnectionManager instance = new ConnectionManager();

    private ObserverHolder<OnConnectionStateChangedObserver> connectionObservers;

    public boolean connected;

    public static ConnectionManager instance()
    {
        return instance;
    }

    private ConnectionManager()
    {
        this.connected = true;
        connectionObservers = new ObserverHolder<OnConnectionStateChangedObserver>();
    }

    public void registerObserver(OnConnectionStateChangedObserver observer)
    {
        if(connectionObservers.registerObserver(observer)){
            observer.onConnectionStateChanged(connected);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(final boolean connected) {
        if(this.connected != connected) {
            this.connected = connected;
            this.connectionObservers.notifyAllObservers(new ObserverHolder.ObserverNotifier<OnConnectionStateChangedObserver>() {
                @Override
                public void onNotify(OnConnectionStateChangedObserver observer) {
                    observer.onConnectionStateChanged(connected);
                }
            });
        }
    }

    public void unregisterObserver(OnConnectionStateChangedObserver observer)
   {
        connectionObservers.unregisterObserver(observer);
   }

    public static interface OnConnectionStateChangedObserver
    {
        public void onConnectionStateChanged(boolean connectionPresent);
    }

}
