package com.au.test2p;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "whatevs";
    IntentFilter mIntentFilter;
    private WifiP2pManager manager;
    private Channel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //forced to add
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // I code here
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        //receiver = new Receiver(manager, channel, this);
        manager.initialize(getApplicationContext(), getMainLooper(), null);
        /*//discover peers
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Discovery Initiated",
                        Toast.LENGTH_SHORT).show();
                if (manager != null) {
                    blah();
                }
            }
            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getApplicationContext(), "Discovery Failed : " + reasonCode,
                        Toast.LENGTH_SHORT).show();
            }
        });*/
        new Receiver(manager, channel, this);
    }
    public void blah(){
        new Receiver(manager, channel, this);
    }
}
