package com.example.john8324.cartravelclient;

import java.io.*;
import java.util.*;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = getAssets();

        try {
            ArrayList<HashMap<String, String>> csv_data = readCsv("test.csv");
            Log.d("", "" + csv_data.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("", "IO Fail");
        }



    }

    public ArrayList<HashMap<String, String>> readCsv(String filename) throws FileNotFoundException, IOException {
        ArrayList<HashMap<String, String>> allData = new ArrayList<>();
        Scanner input = new Scanner(assetManager.open(filename));
        String[] title = input.nextLine().split(",");
        for (int i = 0; i < title.length; i++) {
            title[i] = title[i].trim();
        }
        while (input.hasNext()) {
            String[] line = input.nextLine().split(",");
            HashMap<String, String> aRow = new HashMap<>();
            for (int i = 0; i < title.length; i++) {
                aRow.put(title[i], line[i].trim());
            }
            allData.add(aRow);
        }
        input.close();
        return allData;
    }
}
