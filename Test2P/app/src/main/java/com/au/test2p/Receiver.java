package com.au.test2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lara on 13/10/2015.
 */
public class Receiver extends BroadcastReceiver {
    final static String TAG = "rec101";

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private MainActivity activity;

    public Receiver(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
        Log.e(TAG," receiver started");
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(activity.getApplicationContext(), "Receiving something",
                Toast.LENGTH_SHORT).show();
        Log.e(TAG,"received something");
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            Log.e(TAG,"WIFI_P2P_STATE_CHANGED_ACTION");
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Log.e(MainActivity.TAG, "Wifi P2P is enabled");
            } else {
                // Wi-Fi P2P is not enabled
                Log.e(MainActivity.TAG, "Wi-Fi P2P is not enabled");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.e(TAG,"WIFI_P2P_PEERS_CHANGED_ACTION");
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.e(TAG,"WIFI_P2P_CONNECTION_CHANGED_ACTION");

            //here
            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                // we are connected with the other device, request connection
                // info to find group owner IP
                manager.requestConnectionInfo(channel, new ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        Log.e(TAG, "blah blah");
                        Log.e(TAG, ""+info);
                    }
                });
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.e(TAG,"WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }
    }
}
