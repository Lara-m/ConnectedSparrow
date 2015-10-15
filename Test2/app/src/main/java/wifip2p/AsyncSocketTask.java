package wifip2p;

import android.os.AsyncTask;
import android.util.Log;
import java.io.OutputStream;
import java.net.Socket;

/*
* AynchTask class to open a socket asynchronously and write data to the open socket.
* Autonomous operation.
* Input:DataHolder
* Output: Void
* Contains: doInBackground method
*
* Author: Lara
*
*/


public class AsyncSocketTask extends AsyncTask<DataHolder, Void, String> {
    DataHolder dh;

    public AsyncSocketTask(DataHolder dh) {
        Log.d(Wifip2pHandler.TAG,"AsynchTask Started");
        this.dh = dh;
    }
    @Override
    protected String doInBackground(DataHolder... params) {
        try {
            Log.d(Wifip2pHandler.TAG, "Opening client socket... ");
            Socket socket;
            socket = new Socket(dh.inetAddress, dh.port);
            Log.d(Wifip2pHandler.TAG, "Client socket is connected : " + socket.isConnected());
            OutputStream stream = socket.getOutputStream();
            stream.write(dh.data.getBytes());
            socket.close();
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }
}
