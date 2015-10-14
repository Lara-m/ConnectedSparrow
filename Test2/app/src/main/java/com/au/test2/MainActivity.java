package com.au.test2;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {
    final static String TAG = "P2Pconn";
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    //WifiP2pDeviceList peerList = new WifiP2pDeviceList();
    IntentFilter mIntentFilter;
    private static String address = "66:b3:10:d1:75:d7";
    private static int port = 8988;
    //configure the address
    WifiP2pConfig config;
    //static WifiP2pInfo winfo;
    String data = "blah blah, I got this thing here and it drags text from here to there! :D " +
            "Hihihihi! :D ";


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
        //Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_SHORT).show();
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        //manager.initialize(getApplicationContext(), getMainLooper(), null);
        config = new WifiP2pConfig();
        config.deviceAddress = address;
        //discover peers
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //Toast.makeText(getApplicationContext(), "Discovery Initiated",
                //        Toast.LENGTH_SHORT).show();
                if (manager != null) {
                    manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            //on peers available, update peer list and log them
                            //Log.d(MainActivity.TAG, String.format("PeerListListener: %d peers available, updating device list", peers.getDeviceList().size()));
                            //peerList = peers;
                            //Log.e(TAG, "DeviceList : " + peers.getDeviceList());
                            connect();
                        }
                    });
                }
            }
            @Override
            public void onFailure(int reasonCode) {
                Log.e(TAG, "Discovery Failed : " + reasonCode);
            }
        });
    }

    public void connect(){

        // connect to the address
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //success logic
                //Toast.makeText(getApplicationContext(), "Connected",
                //        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Connected");
                manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        if (info.groupOwnerAddress != null) {
                            //winfo = info;
                            Log.d(TAG, "InetAddress : " + info.groupOwnerAddress.getHostAddress() + "\nPort:"+port);
                            //send IP Address from here
                            //Or do whatever
                            new AsyncSocketTask(new DataHolder(info.groupOwnerAddress,port,data)).execute();
                        } else {
                            Log.e(TAG, "requestConnectionInfo null");
                        }
                    }
                });

            }
            @Override
            public void onFailure(int reason) {
                //failure logic
                Toast.makeText(getApplicationContext(), "Connect Failed : " + reason,
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Connect Failed : " + reason);
            }
        });

    }
}
