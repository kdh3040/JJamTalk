package com.dohosoft.talkking.Util;

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
/*
    public String CreateUserIdx(String Email)
    {
        String rtValue = null;

        try{
            String email = URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(Email,"UTF-8");
            String addr ="http://13.113.143.45/firebaseact.php";
            URL url = new_img URL(addr);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if(conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(10000);
                Log.d("hngpic","settimeout");
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new_img BufferedWriter(new_img OutputStreamWriter(outputStream));
                writer.write(email);
                writer.flush();
                outputStream.close();
                writer.close();
                InputStream inputStream = conn.getInputStream();
                BufferedReader br  = new_img BufferedReader(new_img InputStreamReader(inputStream));
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
            URL url = new_img URL(addr);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if(conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setConnectTimeout(10000);
                Log.d("hngpic","settimeout");
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new_img BufferedWriter(new_img OutputStreamWriter(outputStream));
                writer.write(email);
                writer.flush();
                outputStream.close();
                writer.close();
                InputStream inputStream = conn.getInputStream();
                BufferedReader br  = new_img BufferedReader(new_img InputStreamReader(inputStream));
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
    }*/

}
