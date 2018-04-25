package com.dohosoft.talkking.Firebase;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.LoginActivity;
import com.dohosoft.talkking.MainActivity;
import com.dohosoft.talkking.R;
import com.dohosoft.talkking.Util.CommonFunc;

import java.util.List;

import static com.dohosoft.talkking.Data.CoomonValueData.OFFAPP;


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

        if (remoteMessage.getData() != null) {
            Resources res = getResources();
            String body = remoteMessage.getData().get("body");
            String title = remoteMessage.getData().get("title");

            String index = remoteMessage.getData().get("TargetIdx");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);


            Intent LoginNotifyIntent =
                    new Intent(this, MainActivity.class);
           // LoginNotifyIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            LoginNotifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            LoginNotifyIntent.putExtra("StartFragment", 2);
            LoginNotifyIntent.putExtra("Noti", 1);
            LoginNotifyIntent.putExtra("New", 0);


            PendingIntent LoginNotifyPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            LoginNotifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            Intent BackGroundNotifyIntent =
                    new Intent(this, MainActivity.class);
            BackGroundNotifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            BackGroundNotifyIntent.putExtra("StartFragment", 0);
            BackGroundNotifyIntent.putExtra("Noti", 0);
            BackGroundNotifyIntent.putExtra("New", 0);


            PendingIntent BackgroundNotifyPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            BackGroundNotifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );



            Intent notifyIntent =
                    new Intent(getApplicationContext(), LoginActivity.class);
            notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent notifyPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_NO_CREATE);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("PrefSetting", getApplicationContext().MODE_PRIVATE);
            boolean bSound =  true;
            boolean bVibe =  true;

            if(OFFAPP == true) {

                builder.setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.logo300)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logo300))
                        .setAutoCancel(true)
                        .setContentIntent(LoginNotifyPendingIntent)
                        .setWhen(System.currentTimeMillis());


                if (pref.getBoolean("Sound", bSound)  ) {
                    Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(uri);
                }

                if (pref.getBoolean("Vibe", bVibe)  ) {
                    builder.setVibrate(new long[] {0, 1000});
                }


                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(1234, builder.build());

                mMyData.badgecount++ ;
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count_package_name", "com.dohosoft.talkking");
                intent.putExtra("badge_count_class_name", "com.dohosoft.talkking.LoginActivity");
                intent.putExtra("badge_count", mMyData.badgecount);
                sendBroadcast(intent);

                SharedPreferences prefs = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("Badge",  mMyData.badgecount);
                editor.commit();
            }

           else if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND  || CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

                if(mMyData.GetCurFrag() == 5)
                {
                    if(mMyData.CurChatTartgetIdx.equals(index))
                    {
                        return;
                    }
                }

                //if(mMyData.GetCurFrag() != 5)
                {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("ExecByNoti", getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("StartFrag",  2);
                    editor.putInt("ExecByNoti", 1);
                    editor.commit();
                    {
                        builder.setContentTitle(title)
                                .setContentText(body)
                                .setSmallIcon(R.drawable.logo300)
                                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logo300))
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(LoginNotifyPendingIntent)
                                .setDefaults(Notification.DEFAULT_LIGHTS);

                        if(mMyData.nAlarmSetting_Pop == true)
                        {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                builder.setCategory(Notification.CATEGORY_MESSAGE)
                                        .setPriority(Notification.PRIORITY_HIGH)
                                        .setVisibility(Notification.VISIBILITY_PUBLIC);
                            }
                        }


                        if (pref.getBoolean("Sound", bSound)  ) {
                            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            builder.setSound(uri);
                        }

                        if (pref.getBoolean("Vibe", bVibe)  ) {
                            builder.setVibrate(new long[] {0, 1000});
                        }


                        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        nm.notify(1234, builder.build());
                    }
                }
            }

            else  if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.BACKGROUND) {

                SharedPreferences ExecByNoti = getApplicationContext().getSharedPreferences("ExecByNoti", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor ExecByEditor = ExecByNoti.edit();
                ExecByEditor.putInt("StartFrag",  0);
                ExecByEditor.putInt("ExecByNoti", 0);
                ExecByEditor.commit();


                builder.setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.drawable.logo300)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logo300))
                        .setAutoCancel(true)
                        .setContentIntent(BackgroundNotifyPendingIntent)
                        .setWhen(System.currentTimeMillis());

                if (pref.getBoolean("Sound", bSound)  ) {
                    Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(uri);
                }

                if (pref.getBoolean("Vibe", bVibe)  ) {
                    builder.setVibrate(new long[] {0, 1000});
                }

                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(1234, builder.build());

                mMyData.badgecount++ ;
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count_package_name", "com.dohosoft.talkking");
                intent.putExtra("badge_count_class_name", "com.dohosoft.talkking.LoginActivity");
                intent.putExtra("badge_count", mMyData.badgecount);
                sendBroadcast(intent);

                SharedPreferences prefs = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("Badge",  mMyData.badgecount);
                editor.commit();

            }
        }
    }

}