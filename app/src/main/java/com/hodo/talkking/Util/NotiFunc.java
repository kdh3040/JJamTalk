package com.hodo.talkking.Util;

import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UserData;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by boram on 2017-08-16.
 */

public class NotiFunc {

    private String SERVER_KEY = "AAAA6jLM7_4:APA91bE4W2A2LGIkPmn975GLKL7pFzg9yqfczKrY41GKzCfIFEH97H2AGbAzNsaK-LgzkhuNVa6PqWfnrR9yf87fjIJpcZ6i9wr7jLIXPjjpHOX-98y_nzB41ZGR9FLW-spfyMM9zW3B";
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


    public void SendMSGToFCM(final UserData stTargetData, String strMsg) {
        try {

            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();

            notification.put("body", strMsg);

            notification.put("title", mMyData.getUserNick() + "님이 쪽지를 보냈습니다");
            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("NickName", mMyData.getUserNick());
            data.put("Type", "Msg");

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
            data.put("Type", "Heart");

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
            data.put("Type", "Msg");

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

    public void SendHoneyToFCM(final UserData stTargetData, int nHoneyCnt, String strMsg) {
        try {

            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();

            notification.put("body", strMsg);

            notification.put("title", mMyData.getUserNick() + "님이 하트를" + nHoneyCnt+ "개 보냈습니다");

            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("Gender", stTargetData.Gender);
            data.put("NickName", mMyData.getUserNick());
            data.put("Type", "Honey");
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

    public void SendChatToFCM(final UserData stTargetData, String Msg) {
        try {

            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();

            notification.put("body", Msg);

            notification.put("title", mMyData.getUserNick() + "님이 메세지를 보냈습니다");
            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("NickName", mMyData.getUserNick());
            data.put("Type", "Msg");

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
