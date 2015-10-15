package com.au.test2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import wifip2p.Wifip2pHandler;

public class MainActivity extends AppCompatActivity {
    private static String address = "66:b3:10:d1:75:d7";
    private static int port = 8988;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Wifip2pHandler(this,address,port, "The quick brown fox jumps over the lazy dog!");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Wifip2pHandler(this,address,port, "blah blah!");
    }
}
