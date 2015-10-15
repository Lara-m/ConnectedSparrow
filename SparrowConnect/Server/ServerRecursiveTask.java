package com.au.test2p;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerRecursiveTask {
    final static String TAG = "P2PServer";

    private WifiP2pManager manager;
    private Channel channel;
    private int port;
    private IntentFilter mIntentFilter;
    private Activity activity;
    private StringBuilder out;

    ServerRecursiveTask(Activity activity, StringBuilder out, int port) {
        this.activity = activity;
        //Assigned port
        this.port = port;
        //Assigned for output
        this.out = out;
        //Thread that runs over and over and keeps the socket open.
        t.start();
    }

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {

            //forced to add
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

            // I code from here
            manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
            channel = manager.initialize(activity, activity.getMainLooper(), null);
            //discover peers
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    if (manager != null) {
                        new Async().execute();
                    }
                }
                @Override
                public void onFailure(int reasonCode) {
                    Log.e(TAG, "Discovery Failed : " + reasonCode);
                }
            });
        }
    });

    class Async extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            while (true) {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    Log.e(TAG, "Server: Socket opened");
                    Socket client = serverSocket.accept();
                    InputStream inputstream = client.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }
                    Log.e(TAG, "inputstream : " + out);
                    serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
