package com.example.john8324.cartravelclient;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    AssetManager assetManager;
    CarDataTask carDataTask;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialization
        assetManager = getAssets();
        textView = (TextView) findViewById(R.id.textView);
        carDataTask = new CarDataTask(assetManager, textView);
        carDataTask.execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
        carDataTask.cancel(true);
    }
}


