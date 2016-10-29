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

        // Login the server
        httpposter login = new httpposter();
        login.login();

        final String[] match = {"Latitude", "Longitude", "Vehicle speed", "Instant fuel economy"};
        final String[] actualKey = {"start_time", "latitude", "longitude", "vehicle_speed"};
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
                    for (int j = 0; j < match.length; ++j) {
                        pass[i] = pass[i] || key[i].contains(match[j]);
                        if (pass[i]) {
                            key[i] = actualKey[j];
                            break;
                        }
                    }
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("vehicle_id", 7122);
                    for (int i = 0; i < key.length; ++i) {
                        if (pass[i]) {
                            jsonObject.put(key[i], row[i]);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                publishProgress(jsonObject);
                String postString = "apiFun=infoAdd&json=" + jsonObject.toString();
                Log.d("postString", postString);
                login.doPost("http://140.113.216.201/carInfoApi.php", postString, login.cookie, "utf-8");

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


