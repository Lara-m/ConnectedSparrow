package wifip2p;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.app.Activity;

/*
* wifip2pHandler
*
* Handles p2p connections.
* Autonomous operation via constructor.
* Input: Activity, Address, Port, String of data
* Output: Void
*
* Author: Lara
*
*/
public class Wifip2pHandler {
    final static String TAG = "P2Pconn";
    private static String address; //= "66:b3:10:d1:75:d7";
    private static int port;// = 8988;
    private String data = null;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private IntentFilter mIntentFilter;
    private WifiP2pConfig config = new WifiP2pConfig();
    private WifiP2pInfo winfo = null;
    private Activity activity;

    private Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            //forced to add
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

            //I code form here
            manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
            channel = manager.initialize(activity, activity.getMainLooper(), null);
            config.deviceAddress = address;
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    if (manager != null) {
                        Log.d(TAG, "peers discovered.");
                        manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                            @Override
                            public void onPeersAvailable(WifiP2pDeviceList peers) {
                                Log.d(TAG, "Peers Available");
                                manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(TAG, "Connected");
                                        manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                                            @Override
                                            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                                                if (info.groupOwnerAddress != null) {
                                                    winfo = info;
                                                    Log.d(TAG, "InetAddress : " + info.groupOwnerAddress.getHostAddress()
                                                            + "\nPort:" + port);

                                                    new AsyncSocketTask(new DataHolder(info.groupOwnerAddress, port, data)).execute();
                                                } else {
                                                    Log.e(TAG, "requestConnectionInfo null");
                                                }
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure(int reason) {
                                        Log.e(TAG, "Connect Failed : " + reason);
                                    }
                                });
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
    });

    public Wifip2pHandler(Activity a, String address,int port, String data){
        this.activity = a;
        this.data = data;
        this.address = address;
        this.port = port;
        if (this.data!=null)
            t.start();
    }

}
