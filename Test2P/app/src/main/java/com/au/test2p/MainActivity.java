package com.au.test2p;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "whatevs";
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    //WifiP2pDeviceList peerList = new WifiP2pDeviceList();
    IntentFilter mIntentFilter;
    //private static String address = "66:b3:10:d1:75:d7";
    private static int port = 8988;
    //configure the address
    //WifiP2pConfig config = new WifiP2pConfig();
    //static WifiP2pInfo winfo;

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
        manager.initialize(getApplicationContext(), getMainLooper(), null);

        //discover peers
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (manager != null) {
                    //new Async(getApplicationContext()).execute();
                    new Async().execute();
/*
                    manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peers) {
                            new Async(getApplicationContext()).execute();
                        }
                    });
*/
                }
            }
            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getApplicationContext(), "Discovery Failed : " + reasonCode,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    class Async extends AsyncTask<Void, Void, String> {
        //private Context context;

        //public Async(Context context) {
        //    this.context = context;
        //}

        @Override
        protected String doInBackground(Void... params) {
            while (true) {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    Log.e(MainActivity.TAG, "Server: Socket opened");
                    Socket client = serverSocket.accept();

                /*
                final File f = new File(Environment.getExternalStorageDirectory() + "/"
                        + "/wifip2pshared/shared-" + System.currentTimeMillis()
                        + ".txt");

                File dirs = new File(f.getParent());
                if (!dirs.exists())
                    dirs.mkdirs();
                f.createNewFile();

                Log.e(MainActivity.TAG, "server: copying files " + f.toString());
                */
                    InputStream inputstream = client.getInputStream();
                    //copyFile(inputstream, new FileOutputStream(f));

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
                    StringBuilder out = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }
                    //
                    //write out to whatever you want.
                    //
                    Log.e(MainActivity.TAG, "inputstream : " + out);
                    Toast.makeText(getApplicationContext(),out,Toast.LENGTH_SHORT).show();
                    serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        /*
        public boolean copyFile(InputStream inputStream, OutputStream out) {
            byte buf[] = new byte[1024];
            int len;
            long startTime=System.currentTimeMillis();

            try {
                while ((len = inputStream.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.close();
                inputStream.close();
                long endTime=System.currentTimeMillis()-startTime;
                Log.e("", "Time taken to transfer all bytes is : " + endTime);

            } catch (IOException e) {
                Log.e(MainActivity.TAG, e.toString());
                return false;
            }
            return true;
        }
        */
    }
}
