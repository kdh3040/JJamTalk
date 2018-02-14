package com.hodo.talkking.Firebase;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.hodo.talkking.ChatRoomActivity;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.LoginActivity;
import com.hodo.talkking.R;
import com.hodo.talkking.Util.CommonFunc;

import java.util.HashMap;
import java.util.List;
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
    private String strSenderType;

    private String strSenderHoney;
    private String strSenderHeart;

    private int nHeartCnt;
    private int nHoneyCnt;
    private int nRecvHoneyCnt;

    private int nAlarmSoundIndex = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            Resources res = getResources();
            String body = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);

            if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND) {

                if(mMyData.GetCurFrag() == 2 || mMyData.GetCurFrag() == 5)
                {
                    builder.setContentTitle(title)
                            .setContentText(body)
                            .setSmallIcon(R.drawable.picture)
                            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.picture))
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis());

                    if (mMyData.nAlarmSetting_Vibration) {
                        builder.setVibrate(new long[] {1000});
                    }
                    if (mMyData.nAlarmSetting_Sound)
                    {
                        builder.setSound(Uri.parse("android.resource://com.hodo.talkking/" + com.hodo.talkking.R.raw.katalk));
                    }
                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(1234, builder.build());
                }

                else
                {
                    builder.setContentTitle(title)
                            .setContentText(body)
                            .setSmallIcon(R.drawable.picture)
                            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.picture))
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setDefaults(Notification.DEFAULT_LIGHTS);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        builder.setCategory(Notification.CATEGORY_MESSAGE)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setVisibility(Notification.VISIBILITY_PUBLIC);
                    }

                    if (mMyData.nAlarmSetting_Vibration) {
                        builder.setVibrate(new long[] {1000});
                    }
                    if (mMyData.nAlarmSetting_Sound)
                    {
                        builder.setSound(Uri.parse("android.resource://com.hodo.talkking/" + com.hodo.talkking.R.raw.katalk));
                    }
                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(1234, builder.build());
                }

            }
/*
            if(info.get(0).topActivity.getClassName().equals(ChatRoomActivity.class.getName()) == false)
            {
            */
/*    builder.setCategory(Notification.CATEGORY_MESSAGE)
                        .setVibrate(new long[] {1000})
                        .setSound(Uri.parse("android.resource://com.hodo.talkking/" + com.hodo.talkking.R.raw.katalk));*//*


                CommonFunc.getInstance().PlayVibration(getApplicationContext());
                CommonFunc.getInstance().PlayAlramSound(getApplicationContext(), R.raw.katalk);
            }
*/


/*

            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification Body: " + body);


            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();
                strSenderImg= data.get("Img");
                strSenderName= data.get("NickName");

                strSenderGender= data.get("Gender");
                strSenderIdx= data.get("Idx");

                strSenderType = data.get("Type");

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
                        .setSmallIcon(R.drawable.picture) // 알림 영역에 노출 될 아이콘.
                        .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                       .setPriority(2)
                        .setContentText(body); // Firebase Console 에서 사용자가 전달한 메시지내용

            }

            else
            {
              notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.picture) // 알림 영역에 노출 될 아이콘.
                        .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                        .setContentText(body) // Firebase Console 에서 사용자가 전달한 메시지내용
                        .setPriority(2)
                        .setContentIntent(pending);
            }

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(0x1001, notificationBuilder.build());
*/


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