package com.hodo.talkking.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.FanData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.MainActivity;
import com.hodo.talkking.R;
import com.hodo.talkking.UserPageActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by woong on 2018-01-05.
 */

public class CommonFunc {

    private static CommonFunc _Instance;

    public static CommonFunc getInstance() {
        if (_Instance == null)
            _Instance = new CommonFunc();

        return _Instance;
    }

    private CommonFunc()
    {

    }

    private MyData mMyData = MyData.getInstance();
    private InterstitialAd mInterstitialAd;
    private SoundPool mSoundPool = null;
    private int mPlaySoundIndex = 0;
    public String LastBoardWrite;

    public ImageView Card_Alarm, Chat_Alarm, Mail_Alarm, Fan_Alarm;

    public void refreshMainActivity(Activity mActivity, int StartFragMent)
    {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra("StartFragment", StartFragMent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
    }

    public void MoveUserPage(Activity mActivity, UserData tempUserData)
    {

/*        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
            tempUserData.arrStarList.add(entry.getValue());
        }*/

        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
            tempUserData.arrFanList.add(entry.getValue());
            //tempUserData.FanList.put(entry.getValue().Idx, entry.getValue());
        }

        Intent intent = new Intent(mActivity, UserPageActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("Target", tempUserData);
        intent.putExtra("FanList", tempUserData.arrFanList);
        intent.putExtra("FanCount", tempUserData.FanCount);

        intent.putExtra("StarList", tempUserData.arrStarList);
        intent.putExtras(bundle);

        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
    }

    public void getUserData(final Activity mActivity, final SimpleUserData Target) {
        final String strTargetIdx = Target.Idx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int saa = 0;
                UserData tempUserData = dataSnapshot.getValue(UserData.class);
                if(tempUserData != null)
                {
                    MoveUserPage(mActivity, tempUserData);
                    //moveCardPage(position);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void loadInterstitialAd(Context mContext) {
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId("ca-app-pub-7666588215496282/6908851457");
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    public interface ShowDefaultPopup_YesListener{
        void YesListener();
    }

    public void ShowDefaultPopup(Context context, String title, String centerDesc)
    {
        ShowDefaultPopup(context, null, title, centerDesc, null, null, false);
    }

    public void ShowDefaultPopup(Context context, final ShowDefaultPopup_YesListener listenerYes, String title, String centerDesc, String yesDesc, String noDesc)
    {
        ShowDefaultPopup(context, listenerYes, title, centerDesc, yesDesc, noDesc, true);
    }

    public void ShowDefaultPopup(Context context, final ShowDefaultPopup_YesListener listenerYes, String title, String centerDesc, String yesDesc, String noDesc, Boolean btnView)
    {
        TextView Title, CenterDesc;
        Button YesButton, NoButton;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_exit_app,null,false);

        Title = (TextView) v.findViewById(R.id.title);
        CenterDesc = (TextView) v.findViewById(R.id.msg);
        YesButton = (Button) v.findViewById(R.id.btn_yes);
        NoButton = (Button) v.findViewById(R.id.btn_no);

        Title.setText(title);
        CenterDesc.setText(centerDesc);


        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        if(btnView)
        {
            YesButton.setVisibility(View.VISIBLE);
            NoButton.setVisibility(View.VISIBLE);
            YesButton.setText(yesDesc);
            NoButton.setText(noDesc);

            YesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listenerYes != null)
                        listenerYes.YesListener();
                    dialog.dismiss();
                }
            });
            NoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        else
        {
            YesButton.setVisibility(View.GONE);
            NoButton.setVisibility(View.GONE);

            YesButton.setOnClickListener(null);
            NoButton.setOnClickListener(null);
        }
    }

    public Date GetStringToDate(String date, String format)
    {
        SimpleDateFormat tt = new SimpleDateFormat(format);

        try
        {
            return  tt.parse(date);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    public boolean IsTodayDate(Date date_1, Date date_2)
    {
        if(date_1.getYear() == date_2.getYear() &&
                date_1.getMonth() == date_2.getMonth() &&
                date_1.getDay() == date_2.getDay())
            return true;

        return false;
    }

    public long GetCurrentTime()
    {
        // 우리나라는 UTC + 9:00 이기 때문에 더해줘야 한다.
        return System.currentTimeMillis();
    }

    public Date GetCurrentDate()
    {
        return new Date(GetCurrentTime());
    }

    public boolean IsFutureDateCompare(Date pastDate, int min)
    {
        if(min <= 0)
            return false;

        if(pastDate.equals(new Date(0)))
            return true;

        Date futureDate = new Date(pastDate.getTime() + min * CoomonValueData.MIN_MILLI_SECONDS);

        if(futureDate.compareTo(pastDate) > 0)
            return false;
        else
            return true;
    }

    public void PlayVibration(Context context)
    {
        if(mMyData.nAlarmSetting_Vibration)
        {
            final Vibrator vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
        }
    }

    public void PlayAlramSound(Context context, int resId)
    {
        if(mMyData.nAlarmSetting_Sound)
        {
            if(mSoundPool == null)
                mSoundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);

            mPlaySoundIndex = mSoundPool.load(context, resId, 1);
            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    soundPool.play(mPlaySoundIndex, 1f, 1f, 0, 0, 1f);
                }
            });
        }
    }

    public void SetCardAlarmVisible(boolean enable)
    {
        if(Card_Alarm != null)
            Card_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
    public void SetChatAlarmVisible(boolean enable)
    {
        if(Chat_Alarm != null)
            Chat_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
    public void SetFanAlarmVisible(boolean enable)
    {
        if(Fan_Alarm != null)
            Fan_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
    public void SetMailAlarmVisible(boolean enable)
    {
        if(Mail_Alarm != null)
            Mail_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
}
