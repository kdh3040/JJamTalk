package com.dohosoft.talkking.Util;

import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.UserData;

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
            JSONObject data = new JSONObject();

            data.put("body", strMsg);

            data.put("title", mMyData.getUserNick() + " 님이 쪽지를 보냈습니다");
            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("NickName", mMyData.getUserNick());
            data.put("Type", "Msg");

            root.put("data", data);
            root.put("to", stTargetData.Token);
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

    public void SendHoneyToFCM(final UserData stTargetData, int nHoneyCnt, String strMsg, int Rank) {

        try {

            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("title", mMyData.getUserNick() + "님이 하트를 " + nHoneyCnt+ "개 보냈습니다");

            switch (Rank)
            {
                case 0:

                    data.put("body", mMyData.getUserNick() + "님이 팬클럽에 가입하였습니다!");
                    break;
                case 1:
                    data.put("body", mMyData.getUserNick() + "님이 1등 팬이 되셨군요!");
                    break;
                case 2:
                    data.put("body", strMsg);
                    break;
            }

            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("Gender", stTargetData.Gender);
            data.put("NickName", mMyData.getUserNick());
            data.put("Type", "Honey");
           // data.put("Honey", nHoneyCnt);

            root.put("data", data);
            root.put("to", stTargetData.Token);
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
            JSONObject data = new JSONObject();

            data.put("body", Msg);

            data.put("title", mMyData.getUserNick() + "님이 메세지를 보냈습니다");
            data.put("Img", stTargetData.Img);
            data.put("Idx", stTargetData.Idx);
            data.put("NickName", mMyData.getUserNick());
            data.put("Type", "Msg");

            root.put("data", data);
            root.put("to", stTargetData.Token);
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
