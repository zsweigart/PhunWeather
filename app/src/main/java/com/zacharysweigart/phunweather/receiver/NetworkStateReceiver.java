package com.zacharysweigart.phunweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines a broadcast receiver that will monitor the network status
 *
 * @author Zachary Sweigart
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    protected List<NetworkStateReceiverListener> listeners;
    protected Boolean connected;

    public NetworkStateReceiver() {
        listeners = new ArrayList<NetworkStateReceiverListener>();
        connected = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();

        if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
            connected = false;
        }

        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for(NetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(NetworkStateReceiverListener listener) {
        if(connected == null || listener == null)
            return;

        if(connected)
            listener.networkAvailable();
        else
            listener.networkUnavailable();
    }

    /**
     * Adds a listener to the list so that it will receive connection state change updates
     *
     * @param l a class which implements the NetworkStateReceiverListener interface
     */
    public void addListener(NetworkStateReceiverListener l) {
        listeners.add(l);
        notifyState(l);
    }

    /**
     * Removes a listener from the list so that it will no longer receive connection state change updates
     *
     * @param l a class which implements the NetworkStateReceiverListener interface
     */
    public void removeListener(NetworkStateReceiverListener l) {
        listeners.remove(l);
    }

    /**
     * This interface defines objects that will handle connection state changes
     */
    public interface NetworkStateReceiverListener {
        /**
         * When the connection state is changed and there is a connection, this method is called
         * for registered classes
         */
        public void networkAvailable();
        /**
         * When the connection state is changed and there is not a connection, this method is called
         * for registered classes
         */
        public void networkUnavailable();
    }
}
