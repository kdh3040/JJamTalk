package com.hodo.jjamtalk.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by boram on 2017-07-31.
 */

public class AwsFunc {
    private static AwsFunc _Instance;

    public static AwsFunc getInstance() {
        if (_Instance == null)
            _Instance = new AwsFunc();

        return _Instance;
    }

    private AwsFunc()
    {

    }

    public String CreateUserIdx(String Email)
    {
        String rtValue = null;

        try{
            String email = URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(Email,"UTF-8");
            String addr ="http://13.113.143.45/firebaseact.php";
            URL url = new URL(addr);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if(conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(10000);
                Log.d("hngpic","settimeout");
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(email);
                writer.flush();
                outputStream.close();
                writer.close();
                InputStream inputStream = conn.getInputStream();
                BufferedReader br  = new BufferedReader(new InputStreamReader(inputStream));
                rtValue = br.readLine();
                if(rtValue ==null) {
                    Log.d("hngpic","readline is null");
                }else {
                    Log.d("hngpic", rtValue);
                }
                br.close();
                inputStream.close();
                conn.disconnect();
            }
        }catch(Exception e) {
            e.printStackTrace();

        }
        return rtValue;
    }

    public String GetUserIdx(String Email)
    {
        String rtValue = null;

        try{
            String email = URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(Email,"UTF-8");
            String addr ="http://13.113.143.45/firebase_login.php";
            URL url = new URL(addr);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if(conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(10000);
                Log.d("hngpic","settimeout");
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(email);
                writer.flush();
                outputStream.close();
                writer.close();
                InputStream inputStream = conn.getInputStream();
                BufferedReader br  = new BufferedReader(new InputStreamReader(inputStream));
                rtValue = br.readLine();
                if(rtValue ==null) {
                    Log.d("hngpic","readline is null");
                }else {
                    Log.d("hngpic", rtValue);
                }
                br.close();
                inputStream.close();
                conn.disconnect();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return rtValue;
    }

    public String CreateBoardIdx(String date)
    {
        String rtValue = null;

        try{
            String addr ="http://13.113.143.45/firebaseact.php";
            URL url = new URL(addr);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if(conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(10000);
                Log.d("hngpic","settimeout");
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(date);
                writer.flush();
                outputStream.close();
                writer.close();
                InputStream inputStream = conn.getInputStream();
                BufferedReader br  = new BufferedReader(new InputStreamReader(inputStream));
                rtValue = br.readLine();
                if(rtValue ==null) {
                    Log.d("hngpic","readline is null");
                }else {
                    Log.d("hngpic", rtValue);
                }
                br.close();
                inputStream.close();
                conn.disconnect();
            }
        }catch(Exception e) {
            e.printStackTrace();

        }
        return rtValue;
    }

}
