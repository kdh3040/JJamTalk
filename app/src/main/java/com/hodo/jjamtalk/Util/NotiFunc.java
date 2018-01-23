package com.hodo.jjamtalk.Util;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by boram on 2017-08-16.
 */

public class NotiFunc {

    private String SERVER_KEY = "AAAAccOh5zg:APA91bGpV8OODt9IWTdDRKDK-TWkfdIMymbUgAhw0v0Yp3QHkPSArm4Ir8m5gzK5hxjrTrJ7iobjxAbylw7SR1cncxtaU3ctKj2qOAqNgIMUIMdzpSQ3Or-xTn64xiZEN5n06iNk-qeJ";
    private String MSG_URL = "https://fcm.googleapis.com/fcm/send";

    private static NotiFunc _Instance;

    public static NotiFunc getInstance() {
        if (_Instance == null)
            _Instance = new NotiFunc();

        return _Instance;
    }

    private NotiFunc()
    {

    }

    private MyData mMyData = MyData.getInstance();


    public void SendMSGToFCM(final UserData stTargetData) {
        try {

            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();

            notification.put("body", mMyData.getUserNick() + "님이 쪽지를 보냈습니다");

            notification.put("title", "꿀톡");
            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("NickName", mMyData.getUserNick());
            root.put("notification", notification);
            root.put("to", stTargetData.Token);
            root.put("data", data);
            // FMC 메시지 생성 end

            URL Url = new URL(MSG_URL);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(root.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void SendHeartToFCM(final UserData stTargetData, int nHeartCnt) {
        try {

            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();

            notification.put("body", mMyData.getUserNick() + "님이 좋아요를 보냈습니다");

            notification.put("title", "꿀톡");

            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("Gender", stTargetData.Gender);
            data.put("NickName", mMyData.getUserNick());
            data.put("Heart", nHeartCnt);

            root.put("notification", notification);
            root.put("to", stTargetData.Token);
            root.put("data", data);
            // FMC 메시지 생성 end

            URL Url = new URL(MSG_URL);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(root.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendMsgToFCM(final UserData stTargetData) {
        try {

            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();

            notification.put("body", mMyData.getUserNick() + "님이 메세지를 보냈습니다");

            notification.put("title", "굿톡");

            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("Gender", stTargetData.Gender);
            data.put("NickName", mMyData.getUserNick());

            root.put("notification", notification);
            root.put("to", stTargetData.Token);
            root.put("data", data);
            // FMC 메시지 생성 end

            URL Url = new URL(MSG_URL);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(root.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendHoneyToFCM(final UserData stTargetData, int nHoneyCnt) {
        try {

            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();

            notification.put("body", mMyData.getUserNick() + "님이 골드를 보냈습니다");

            notification.put("title", "굿톡");

            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("Gender", stTargetData.Gender);
            data.put("NickName", mMyData.getUserNick());
           // data.put("Honey", nHoneyCnt);


            root.put("notification", notification);
            root.put("to", stTargetData.Token);
            root.put("data", data);
            // FMC 메시지 생성 end

            URL Url = new URL(MSG_URL);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(root.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
