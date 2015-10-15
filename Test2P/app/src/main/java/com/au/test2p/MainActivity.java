package com.au.test2p;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    //final static String TAG = "Server";
    private static int port = 8988;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringBuilder out = new StringBuilder();
        new ServerRecursiveTask(this, out,port);

    }
}
