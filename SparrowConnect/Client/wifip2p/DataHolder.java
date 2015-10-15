package wifip2p;

import java.net.InetAddress;

/*
* Data holder class.
* Queueing object to be used between wifip2pHandler and the AsyncSocketTask
*
* Author: Lara
*
*/

public class DataHolder {
    InetAddress inetAddress;
    int port;
    String data;

    public DataHolder(InetAddress inetAddress, int port, String data) {
        this.inetAddress = inetAddress;
        this.port = port;
        this.data = data;
    }
}
