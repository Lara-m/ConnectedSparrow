package com.au.test2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by lara on 14/10/2015.
 */
public class AsyncSocketTask extends AsyncTask<DataHolder, Void, String> {
    DataHolder dh;

    public AsyncSocketTask(DataHolder dh) {
        this.dh = dh;
    }
    @Override
    protected String doInBackground(DataHolder... params) {
        try {
            Log.d(MainActivity.TAG, "Opening client socket... ");
            Socket socket;
            socket = new Socket(dh.inetAddress, dh.port);
            Log.d(MainActivity.TAG, "Client socket is connected : " + socket.isConnected());
            OutputStream stream = socket.getOutputStream();
            stream.write(dh.data.getBytes());
            socket.close();
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }
}
