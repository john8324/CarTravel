package com.example.john8324.cartravelclient;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

class httpposter{

    //Map<String, List<String>> mapProperties;
    String cookie;
    private String _cookie;

    void login() {
        // CRITICAL WARNING: Must upload FAKE account and password !!!!!!!!!!!!!!!!!!!!!!!!!!!!
        doPost("http://140.113.216.201/logincheck.php", "account=jerrychen&password=abc123", null, "utf-8");
        cookie = _cookie;
    }

    boolean doPost(String sURL, String data, String cookie, String charset) {

        boolean doSuccess = false;
        try {

            URL url = new URL(sURL);
            HttpURLConnection URLConn = (HttpURLConnection) url.openConnection();

            URLConn.setDoOutput(true);
            URLConn.setDoInput(true);
            URLConn.setRequestMethod("POST");
            URLConn.setUseCaches(false);
            URLConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            URLConn.setInstanceFollowRedirects(true);

            URLConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
            URLConn.setRequestProperty("Accept-Language", "zh-tw,en-us");
            URLConn.setRequestProperty("Accept-Charset", "utf-8");
            if (cookie != null)
                URLConn.setRequestProperty("Cookie", cookie);

            URLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            URLConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));

            Log.d("Cookie: ", cookie == null ? "NULL" : cookie);

            java.io.DataOutputStream dos = new java.io.DataOutputStream(URLConn.getOutputStream());
            dos.writeBytes(data);

            java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(URLConn.getInputStream(), charset));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

            rd.close();
            URLConn.disconnect();
            _cookie = URLConn.getHeaderField("set-cookie");

            doSuccess = true;
        } catch (java.io.IOException e) {
            doSuccess = false;
            System.out.println(e);
        }


        return doSuccess;
    }
}






