package com.example.john8324.cartravelclient;

import java.io.*;
import java.util.*;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import org.json.JSONException;
import org.json.JSONObject;

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

class CarDataTask extends AsyncTask<Void, JSONObject, Void> {

    private AssetManager assetManager;
    private TextView textView;

    CarDataTask(AssetManager assetManager, TextView textView) {
        this.assetManager = assetManager;
        this.textView = textView;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Scanner input = openCsv();
        if (input == null) {
            return null;
        }
        // ------
        // TODO Login the server
        new Thread(new httpposter()).start();

        // ------
        final String[] match = {"Latitude", "Longitude", "Vehicle speed", "Instant fuel economy"};
        String[] key = null;
        boolean[] pass = null;
        while (!isCancelled() && input.hasNextLine()) {
            String[] row = input.nextLine().split(",");
            // Log.d("", "Len = " + cell.length);
            if (row.length == 1 && row[0].startsWith("#")) {
                continue;
            }
            if (key == null) {
                key = row;
                pass = new boolean[key.length];
                for (int i = 0; i < key.length; ++i) {
                    for (String str : match) {
                        pass[i] = pass[i] || key[i].contains(str);
                        if (pass[i]) {
                            break;
                        }
                    }
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < key.length; ++i) {
                    try {
                        if (pass[i]) {
                            jsonObject.put(key[i], row[i]);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                publishProgress(jsonObject);

                // ------
                // TODO POST jsonObject to server
                // ------

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        input.close();
        return null;
    }

    @Override
    protected void onProgressUpdate(JSONObject... values) {
        super.onProgressUpdate(values);
        textView.setText(values[0].toString());
    }

    private Scanner openCsv() {
        try {
            return new Scanner(assetManager.open("test.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}


