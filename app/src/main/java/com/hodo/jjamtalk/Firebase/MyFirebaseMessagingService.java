package com.hodo.jjamtalk.Firebase;


import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hodo.jjamtalk.LoginActivity;
import com.hodo.jjamtalk.R;

import java.util.Map;


/**
 * Created by boram on 2017-07-19.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final static String TAG = "FCM_MESSAGE";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification Body: " + body);

            Map<String, String> data = remoteMessage.getData();

            String ImgUrl = data.get("Img");
            String NickName = data.get("NickName");

            if (remoteMessage.getData().size() > 0) {
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


}