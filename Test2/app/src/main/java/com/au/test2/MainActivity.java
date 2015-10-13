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
import java.net.InetSocketAddress;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {
    final static String TAG = "whatevs";
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    WifiP2pDeviceList peerList = new WifiP2pDeviceList();
    IntentFilter mIntentFilter;
    private static String address = "66:b3:10:d1:75:d7";
    private static int port = 8080;
    //configure the address
    WifiP2pConfig config = new WifiP2pConfig();
    static WifiP2pInfo winfo;


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
        Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_SHORT).show();
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        manager.initialize(getApplicationContext(), getMainLooper(), null);

        //discover peers
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Discovery Initiated",
                        Toast.LENGTH_SHORT).show();
                if (manager != null) {
                    try {
                        Thread.sleep(3000);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            //on peers available, update peer list and log them
                            //Log.d(MainActivity.TAG, String.format("PeerListListener: %d peers available, updating device list", peers.getDeviceList().size()));
                            peerList = peers;
                            Log.e(TAG, "DeviceList : " + peers.getDeviceList());
                            connect();
                        }
                    });
                }
            }
            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getApplicationContext(), "Discovery Failed : " + reasonCode,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void connect(){
        config.deviceAddress = address;
        // connect to the address
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //success logic
                Toast.makeText(getApplicationContext(), "Connected",
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Connected");
                try {
                    Thread.sleep(5000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        if (info.groupOwnerAddress != null){
                            winfo=info;
                            Log.e(TAG, "info : \n" + info);
                            //send IP Address from here
                            //Or do whatever
                            //transfer(info.groupOwnerAddress);
                            new Async(info.groupOwnerAddress).execute();
                        }
                        else{
                            Log.e(TAG,"requestConnectionInfo null");
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
/*
    public void transfer(InetAddress IPAdress) {
        final InetAddress adrs = IPAdress;
        Log.d(TAG, "Opening client socket... ");
        Socket socket;
        String data = "hi.txt";
        try{
            socket = new Socket(adrs, 8898);
            //socket.connect((new InetSocketAddress(IPAdress, 8898)), 5000);
            Log.d(TAG, "Client socket - " + socket.isConnected());
            OutputStream stream = socket.getOutputStream();
            stream.write(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    */
}
class Async extends AsyncTask<InetAddress, Void, String> {
    InetAddress adrs;
    public Async(InetAddress adrs) {this.adrs = adrs;}
    @Override
    protected String doInBackground(InetAddress... params) {
        try {
            Log.d(MainActivity.TAG, "Opening client socket... ");
            Socket socket;// =new Socket();
            String data = "hi.txt";
            Log.e(MainActivity.TAG, "getAddress : \n" + adrs.getHostAddress());
            Log.e(MainActivity.TAG, "adrs : \n" + adrs);
            socket = new Socket(adrs, 8898);
            Log.e(MainActivity.TAG, ""+ socket.getInetAddress());
            //socket.connect((new InetSocketAddress(IPAdress, 8898)), 5000);
            Log.d(MainActivity.TAG, "Client socket - " + socket.isConnected());
            OutputStream stream = socket.getOutputStream();
            stream.write(data.getBytes());
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
