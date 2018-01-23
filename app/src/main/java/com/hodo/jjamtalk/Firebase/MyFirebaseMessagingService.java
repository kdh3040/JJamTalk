package com.hodo.jjamtalk.Firebase;


import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.LoginActivity;
import com.hodo.jjamtalk.R;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by boram on 2017-07-19.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final static String TAG = "FCM_MESSAGE";
    private MyData mMyData = MyData.getInstance();


    private String strSenderImg;
    private String strSenderName;
    private String strSenderGender;
    private String strSenderIdx;

    private String strSenderHoney;
    private String strSenderHeart;

    private int nHeartCnt;
    private int nHoneyCnt;
    private int nRecvHoneyCnt;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification Body: " + body);


            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();
                strSenderImg= data.get("Img");
                strSenderName= data.get("NickName");

                strSenderGender= data.get("Gender");
                strSenderIdx= data.get("Idx");

             //   strSenderHoney = data.get("Honey");
               // strSenderHeart = data.get("Heart");

                //getTargetData(strSenderGender, strSenderIdx);


                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder;

            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
               notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher) // 알림 영역에 노출 될 아이콘.
                        .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                        .setContentText(body); // Firebase Console 에서 사용자가 전달한 메시지내용
            }

            else
            {
              notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher) // 알림 영역에 노출 될 아이콘.
                        .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                        .setContentText(body) // Firebase Console 에서 사용자가 전달한 메시지내용
                        .setContentIntent(pending);
            }

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(0x1001, notificationBuilder.build());


        }
    }

    void getTargetData(String Gender, String idx)
    {
        final DatabaseReference ref;

            ref = FirebaseDatabase.getInstance().getReference().child("User").child(idx);
            ref.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            UserData stRecvData = new UserData ();
                            stRecvData = dataSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {

                                Map<String, Object> updateMap = new HashMap<>();


                                if(strSenderHoney != null) {
                                    nRecvHoneyCnt = stRecvData.RecvCount;
                                    nRecvHoneyCnt -= Integer.valueOf(strSenderHoney);
                                    updateMap.put("RecvCount", nRecvHoneyCnt);
                                    mMyData.setRecvHoneyCnt(Integer.valueOf(strSenderHoney));
                                    mMyData.setUserHoney(mMyData.getUserHoney() + Integer.valueOf(strSenderHoney));

                                  /*  FanData tempData = new FanData();
                                    tempData.Img = strSenderImg;
                                    tempData.Idx = strSenderIdx;
                                    tempData.Nick = strSenderName;
                                    mMyData.makeFanList(tempData, Integer.parseInt(strSenderHoney));*/

                                }

                                ref.updateChildren(updateMap);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                        }
                    });
    }

}