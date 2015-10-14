package com.au.test2;

import java.net.InetAddress;

/**
 * Created by lara on 14/10/2015.
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
