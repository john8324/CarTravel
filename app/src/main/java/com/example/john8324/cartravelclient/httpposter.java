package com.example.john8324.cartravelclient;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jerry on 2016/10/29.
 */

public class httpposter implements Runnable{

    public String MyCookie ="";
    CookieManager CM = new CookieManager();

    @Override
    public void run() {
        JSONObject obj = new JSONObject();
        try {
  //          obj.put("apiFun", "infoAdd");
            obj.put("vehicle_id", 1);
            obj.put("start_time", 1111);
            obj.put("latitude", 123.123);
            obj.put("longitude", 321.321);
            obj.put("vehicle_speed",9999);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String test = null;
//        try {
//            test = "apiFun=infoAdd&json="+ URLEncoder.encode(obj.toString(), "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        test = "apiFun=infoAdd&json="+ obj.toString();
        Log.d("jerry:",test);
        String go = "vehicle_id=1&apiFun=infoAdd&start_time=1111&latitude=123.123";
        //System.out.println("jerry:");
        doPost("http://140.113.216.201/logincheck.php", "account=7122&password=7122", null, null, "utf-8");
      //  doPost("http://140.113.216.201/test.php", "account=7122&password=7122", null, null, "utf-8");
        doJSON("http://140.113.216.201/carInfoApi.php", test, MyCookie, null, "utf-8");
        //doJSON("http://140.113.216.201/carInfoApi.php", obj.toString(), MyCookie, null, "utf-8");
    }
    public  boolean doJSON(String sURL, String data, String cookie, String referer, String charset) {
        boolean doSuccess = false;
        java.io.BufferedWriter wr = null;
        try {

            URL url = new URL(sURL);
            HttpURLConnection URLConn = (HttpURLConnection) url
                    .openConnection();

            URLConn.setDoOutput(true);
            URLConn.setDoInput(true);
            ((HttpURLConnection) URLConn).setRequestMethod("POST");
            URLConn.setUseCaches(false);
            URLConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            URLConn.setInstanceFollowRedirects(true);

           // URLConn.setRequestProperty("Cookie", cookie);

            URLConn.setRequestProperty(
                    "User-agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) "
                            + "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)");
            URLConn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
            URLConn.setRequestProperty("Accept-Language",
                    "zh-tw,en-us;q=0.7,en;q=0.3");
            URLConn.setRequestProperty("Accept-Charse",
                    "Big5,utf-8;q=0.7,*;q=0.7");
            if (cookie != null)
                URLConn.setRequestProperty("Cookie", cookie);
            if (referer != null)
                URLConn.setRequestProperty("Referer", referer);

            URLConn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            URLConn.setRequestProperty("Content-Length", String.valueOf(data
                    .getBytes().length));

            java.io.DataOutputStream dos = new java.io.DataOutputStream(URLConn.getOutputStream());
            dos.writeBytes(data);

            java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(URLConn.getInputStream(), charset));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

            rd.close();
            String cookieee = URLConn.getHeaderField("set-cookie");
            if(cookieee!=null && cookieee.length()>0){
                Log.d("cook",cookieee);

            }

        } catch (java.io.IOException e) {
            doSuccess = false;
            System.out.println(e);

        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (java.io.IOException ex) {
                    System.out.println(ex);
                }
                wr = null;
            }
        }

        return doSuccess;

    }
    public boolean doPost(String sURL, String data, String cookie, String referer, String charset) {

        boolean doSuccess = false;
        java.io.BufferedWriter wr = null;
        try {

            URL url = new URL(sURL);
            HttpURLConnection URLConn = (HttpURLConnection) url
                    .openConnection();

            CM.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            URLConn.setDoOutput(true);
            URLConn.setDoInput(true);
            ((HttpURLConnection) URLConn).setRequestMethod("POST");
            URLConn.setUseCaches(false);
            URLConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            URLConn.setInstanceFollowRedirects(true);

            URLConn.setRequestProperty(
                    "User-agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) "
                            + "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)");
            URLConn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            URLConn.setRequestProperty("Accept-Language",
                    "zh-tw,en-us;q=0.7,en;q=0.3");
            URLConn.setRequestProperty("Accept-Charse",
                    "Big5,utf-8;q=0.7,*;q=0.7");
            if (cookie != null)
                URLConn.setRequestProperty("Cookie", cookie);
            if (referer != null)
                URLConn.setRequestProperty("Referer", referer);

            URLConn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            URLConn.setRequestProperty("Content-Length", String.valueOf(data
                    .getBytes().length));

            java.io.DataOutputStream dos = new java.io.DataOutputStream(URLConn.getOutputStream());
            dos.writeBytes(data);

            java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(URLConn.getInputStream(), charset));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

            rd.close();
            String cookieee = URLConn.getHeaderField("set-cookie");
            if(cookieee!=null && cookieee.length()>0){
                this.MyCookie = cookieee;
                Log.d("cook",cookieee);

            }

        } catch (java.io.IOException e) {
            doSuccess = false;
            System.out.println(e);

        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (java.io.IOException ex) {
                    System.out.println(ex);
                }
                wr = null;
            }
        }

        return doSuccess;
    }
}

