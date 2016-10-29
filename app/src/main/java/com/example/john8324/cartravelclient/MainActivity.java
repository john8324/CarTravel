package com.example.john8324.cartravelclient;

import java.io.*;
import java.util.*;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

public class MainActivity extends Activity {

    AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();
        ArrayList<String[]> csv_data = null;

        try {
            csv_data = readCsv("test.csv");
            Log.d("", "" + csv_data.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("", "IO Fail");
        }

        TextView textView = (TextView)findViewById(R.id.textView);
        if (csv_data == null) {
            textView.setText("FAILLLLLLLL");
            return;
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        String[] key = csv_data.get(0), value = csv_data.get(3);
        for (int i = 0; i < value.length; i++) {
            arrayAdapter.add(key[i] + ": " + value[i]);
        }
        spinner.setAdapter(arrayAdapter);

    }

    public ArrayList<String[]> readCsv(String filename) throws IOException {
        ArrayList<String[]> allData = new ArrayList<>();
        Scanner input = new Scanner(assetManager.open(filename));
        while (input.hasNextLine() && allData.size() < 20) {
            String[] row = input.nextLine().split(",");
            // Log.d("", "Len = " + cell.length);
            if (row.length == 1 && row[0].startsWith("#")) {
                continue;
            }

            allData.add(row);
        }
        input.close();
        return allData;
    }
}
