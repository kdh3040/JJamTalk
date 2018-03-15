package com.hodo.talkking.Util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.FanData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SimpleChatData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.MainActivity;
import com.hodo.talkking.R;
import com.hodo.talkking.UserPageActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    private CommonFunc() {

    }

    private MyData mMyData = MyData.getInstance();
    private InterstitialAd mInterstitialAd;
    private SoundPool mSoundPool = null;
    private int mPlaySoundIndex = 0;
    public String LastBoardWrite;

    public boolean Honey_Box_Enable = false, Honey_Box_Indicator_Enable = false;
    public ImageView Card_Alarm, Chat_Alarm, Mail_Alarm, Fan_Alarm;
    public ImageView Item_Box, Honey_Box, Board_Write, Filter;
    public Button MyBoard_Write;
    public static AppStatus mAppStatus = AppStatus.FOREGROUND;

    private boolean bClickSync = false;

    private Button btnHeart10;
    private Button btnHeart30;
    private Button btnHeart50;
    private Button btnHeart100;
    private Button btnHeart300;
    private Button btnHeart500;

    private AppCompatDialog mProgressDialog = null;


    public static class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        // running activity count
        private int running = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (++running == 1) {
                // running activity is 1,
                // app must be returned from background just now (or first launch)
                mAppStatus = AppStatus.RETURNED_TO_FOREGROUND;
            } else if (running > 1) {
                // 2 or more running activities,
                // should be foreground already.
                mAppStatus = AppStatus.FOREGROUND;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (--running == 0) {
                // no active activity
                // app goes to background
                mAppStatus = AppStatus.BACKGROUND;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }

    public AppStatus getAppStatus() {
        return mAppStatus;
    }

    // check if app is foreground
    public boolean isForeground() {
        return mAppStatus.ordinal() > AppStatus.BACKGROUND.ordinal();
    }

    public enum AppStatus {
        BACKGROUND,                // app is background
        RETURNED_TO_FOREGROUND,    // app returned to foreground(or first launch)
        FOREGROUND;            // app is foreground
    }

    public void refreshMainActivity(Activity mActivity, int StartFragMent) {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra("StartFragment", StartFragMent);
        intent.putExtra("Noti", 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
        mActivity.startActivity(intent);
        mActivity.finish();

    }

    public void MoveUserPage(final Activity mActivity, final UserData tempUserData) {

        CommonFunc.getInstance().setClickStatus(false);
/*        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
            tempUserData.arrStarList.add(entry.getValue());
        }*/

        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
            tempUserData.arrFanList.add(entry.getValue());
            //tempUserData.FanList.put(entry.getValue().Idx, entry.getValue());
        }

        if(tempUserData.arrFanList.size() == 0)
        {
            Intent intent = new Intent(mActivity, UserPageActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", tempUserData);
            intent.putExtra("FanList", tempUserData.arrFanList);
            intent.putExtra("FanCount", tempUserData.FanCount);

            intent.putExtra("StarList", tempUserData.arrStarList);
            intent.putExtras(bundle);

            mActivity.startActivity(intent);
        }

        for(int i = 0 ;i < tempUserData.arrFanList.size(); i++)
        {
            Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(tempUserData.arrFanList.get(i).Idx);
            final FanData finalTempFanData = tempUserData.arrFanList.get(i);
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                    tempUserData.arrFanData.put(finalTempFanData.Idx, DBData);

                    if( finalI == tempUserData.arrFanList.size() -1)
                    {
                        Intent intent = new Intent(mActivity, UserPageActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("Target", tempUserData);
                        intent.putExtra("FanList", tempUserData.arrFanList);
                        intent.putExtra("FanCount", tempUserData.FanCount);

                        intent.putExtra("StarList", tempUserData.arrStarList);
                        intent.putExtras(bundle);

                        mActivity.startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        //mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
    }

    public void getUserData(final Activity mActivity, final SimpleUserData Target) {
        CommonFunc.getInstance().setClickStatus(true);

        final String strTargetIdx = Target.Idx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int saa = 0;
                UserData tempUserData = dataSnapshot.getValue(UserData.class);
                if (tempUserData != null) {
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
                if (mInterstitialAd.isLoaded()) {
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

    public interface ShowDefaultPopup_YesListener {
        void YesListener();
    }

    public void ShowDefaultPopup(Context context, String title, String centerDesc) {
        ShowDefaultPopup(context, null, null, title, centerDesc, null, null, false);
    }

    public void ShowDefaultPopup(Context context, final ShowDefaultPopup_YesListener listenerYes, final ShowDefaultPopup_YesListener listenerNo, String title, String centerDesc, String yesDesc, String noDesc) {
        ShowDefaultPopup(context, listenerYes, listenerNo, title, centerDesc, yesDesc, noDesc, true);
    }

    public void ShowDefaultPopup(Context context, final ShowDefaultPopup_YesListener listenerYes, final ShowDefaultPopup_YesListener listenerNo, String title, String centerDesc, String yesDesc, String noDesc, Boolean btnView) {
        TextView Title, CenterDesc;
        Button YesButton, NoButton;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_exit_app, null, false);

        Title = (TextView) v.findViewById(R.id.title);
        CenterDesc = (TextView) v.findViewById(R.id.msg);
        YesButton = (Button) v.findViewById(R.id.btn_yes);
        NoButton = (Button) v.findViewById(R.id.btn_no);

        Title.setText(title);
        CenterDesc.setText(centerDesc);


        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        if (btnView) {
            YesButton.setVisibility(View.VISIBLE);
            NoButton.setVisibility(View.VISIBLE);
            YesButton.setText(yesDesc);
            NoButton.setText(noDesc);

            YesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listenerYes != null)
                        listenerYes.YesListener();
                    dialog.dismiss();
                }
            });
            NoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listenerNo != null)
                        listenerNo.YesListener();
                    dialog.dismiss();
                }
            });
        } else {
            YesButton.setVisibility(View.GONE);
            NoButton.setVisibility(View.GONE);

            YesButton.setOnClickListener(null);
            NoButton.setOnClickListener(null);
        }
    }

    public void ShowToast(Context context, String msg, boolean shortView)
    {
        if(shortView)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }



    public void ShowGiftPopup(Context context, final SimpleChatData SendList) {
        TextView from, tv_count, msg;
        ImageView profile, heart;
        Button confirm, block, report;

        Activity mActivity = mMyData.mActivity;

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        View v = LayoutInflater.from(mActivity).inflate(R.layout.alert_mail, null, false);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();


        from = (TextView) v.findViewById(R.id.from);
        tv_count = (TextView) v.findViewById(R.id.tv_count);
        msg = (TextView) v.findViewById(R.id.msg);

        profile = (ImageView) v.findViewById(R.id.profile);
        heart = (ImageView) v.findViewById(R.id.heart);

        confirm = (Button) v.findViewById(R.id.confirm);
        block = (Button) v.findViewById(R.id.block);
        report = (Button) v.findViewById(R.id.report);

        from.setVisibility(TextView.VISIBLE);
        tv_count.setText(Integer.toString(SendList.SendHeart));
        msg.setText(SendList.Msg);

        heart.setVisibility(ImageView.VISIBLE);
        Glide.with(context)
                .load(SendList.Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(profile);

        confirm.setVisibility(View.VISIBLE);
        block.setVisibility(View.VISIBLE);
        report.setVisibility(View.VISIBLE);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String RoomName1 = mMyData.getUserIdx() + "_" + SendList.Idx;
                String RoomName2 = SendList.Idx + "_" + mMyData.getUserIdx();
                String RoomName = null;
                int RoomPos = 0;
                for (int i = 0; i < mMyData.arrChatNameList.size(); i++) {
                    if (mMyData.arrChatNameList.get(i).contains(RoomName1)) {
                        RoomName = RoomName1;
                        RoomPos = i;
                        break;
                    }
                    if (mMyData.arrChatNameList.get(i).contains(RoomName2)) {
                        RoomName = RoomName2;
                        RoomPos = i;
                        break;
                    }
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference table;
                table = database.getReference("User/" + mMyData.getUserIdx() + "/SendList/").child(RoomName);
                table.removeValue();

                table = database.getReference("User/" + SendList.Idx + "/SendList/").child(RoomName);
                table.removeValue();

                mMyData.makeBlockList(SendList);
                FirebaseData.getInstance().DelChatData(RoomName);

                mMyData.arrChatDataList.remove(mMyData.arrChatNameList.get(RoomPos));
                mMyData.arrChatNameList.remove(RoomPos);

                dialog.dismiss();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String RoomName1 = mMyData.getUserIdx() + "_" + SendList.Idx;
                String RoomName2 = SendList.Idx + "_" + mMyData.getUserIdx();
                String RoomName = null;
                int RoomPos = 0;
                for (int i = 0; i < mMyData.arrChatNameList.size(); i++) {
                    if (mMyData.arrChatNameList.get(i).contains(RoomName1)) {
                        RoomName = RoomName1;
                        RoomPos = i;
                        break;
                    }
                    if (mMyData.arrChatNameList.get(i).contains(RoomName2)) {
                        RoomName = RoomName2;
                        RoomPos = i;
                        break;
                    }
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference table;
                table = database.getReference("User/" + mMyData.getUserIdx() + "/SendList/").child(RoomName);
                table.removeValue();

                table = database.getReference("User/" + SendList.Idx + "/SendList/").child(RoomName);
                table.removeValue();

                mMyData.makeBlockList(SendList);
                FirebaseData.getInstance().DelChatData(RoomName);

                mMyData.arrChatDataList.remove(mMyData.arrChatNameList.get(RoomPos));
                mMyData.arrChatNameList.remove(RoomPos);


                table = database.getReference("Reported").child(SendList.Idx);
                final DatabaseReference user = table;

                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("ReportType", 1);
                user.push().setValue(updateMap);

                dialog.dismiss();
            }
        });
    }

    public void ShowMsgPopup(Context context, final SimpleChatData SendList) {
        Activity mActivity = mMyData.mActivity;
        TextView from, tv_count, msg;
        ImageView profile, heart;
        Button confirm, block, report;

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        View v = LayoutInflater.from(mActivity).inflate(R.layout.alert_msg, null, false);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        from = (TextView) v.findViewById(R.id.from);
        msg = (TextView) v.findViewById(R.id.msg);

        profile = (ImageView) v.findViewById(R.id.image);

        confirm = (Button) v.findViewById(R.id.confirm);
        block = (Button) v.findViewById(R.id.block);
        report = (Button) v.findViewById(R.id.report);

        from.setVisibility(TextView.VISIBLE);
        msg.setText(SendList.Msg);

        Glide.with(mActivity)
                .load(SendList.Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(profile);

        confirm.setVisibility(View.VISIBLE);
        block.setVisibility(View.VISIBLE);
        report.setVisibility(View.VISIBLE);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String RoomName1 = mMyData.getUserIdx() + "_" + SendList.Idx;
                String RoomName2 = SendList.Idx + "_" + mMyData.getUserIdx();
                String RoomName = null;
                int RoomPos = 0;
                for (int i = 0; i < mMyData.arrChatNameList.size(); i++) {
                    if (mMyData.arrChatNameList.get(i).contains(RoomName1)) {
                        RoomName = RoomName1;
                        RoomPos = i;
                        break;
                    }
                    if (mMyData.arrChatNameList.get(i).contains(RoomName2)) {
                        RoomName = RoomName2;
                        RoomPos = i;
                        break;
                    }
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference table;
                table = database.getReference("User/" + mMyData.getUserIdx() + "/SendList/").child(RoomName);
                table.removeValue();

                table = database.getReference("User/" + SendList.Idx + "/SendList/").child(RoomName);
                table.removeValue();

                mMyData.makeBlockList(SendList);
                FirebaseData.getInstance().DelChatData(RoomName);

                mMyData.arrChatDataList.remove(mMyData.arrChatNameList.get(RoomPos));
                mMyData.arrChatNameList.remove(RoomPos);

                dialog.dismiss();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String RoomName1 = mMyData.getUserIdx() + "_" + SendList.Idx;
                String RoomName2 = SendList.Idx + "_" + mMyData.getUserIdx();
                String RoomName = null;
                int RoomPos = 0;
                for (int i = 0; i < mMyData.arrChatNameList.size(); i++) {
                    if (mMyData.arrChatNameList.get(i).contains(RoomName1)) {
                        RoomName = RoomName1;
                        RoomPos = i;
                        break;
                    }
                    if (mMyData.arrChatNameList.get(i).contains(RoomName2)) {
                        RoomName = RoomName2;
                        RoomPos = i;
                        break;
                    }
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference table;
                table = database.getReference("User/" + mMyData.getUserIdx() + "/SendList/").child(RoomName);
                table.removeValue();

                table = database.getReference("User/" + SendList.Idx + "/SendList/").child(RoomName);
                table.removeValue();

                mMyData.makeBlockList(SendList);
                FirebaseData.getInstance().DelChatData(RoomName);

                mMyData.arrChatDataList.remove(mMyData.arrChatNameList.get(RoomPos));
                mMyData.arrChatNameList.remove(RoomPos);


                table = database.getReference("Reported").child(SendList.Idx);
                final DatabaseReference user = table;

                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("ReportType", 1);
                user.push().setValue(updateMap);

                dialog.dismiss();
            }
        });
    }

    public Date GetStringToDate(String date, String format) {
        SimpleDateFormat tt = new SimpleDateFormat(format);

        try {
            return tt.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public boolean IsTodayDate(Date date_1, Date date_2) {
        if (date_1.getYear() == date_2.getYear() &&
                date_1.getMonth() == date_2.getMonth() &&
                date_1.getDay() == date_2.getDay())
            return true;

        return false;
    }

    public long GetCurrentTime() {
        // 우리나라는 UTC + 9:00 이기 때문에 더해줘야 한다.
        return System.currentTimeMillis();
    }

    public Date GetCurrentDate() {
        return new Date(GetCurrentTime());
    }

    // 현재 시간이 지정한 시간보다 지났나?
    public boolean IsCurrentDateCompare(Date pastDate, int offsetMin) {
        if (pastDate.equals(new Date(0)))
            return true;

        Date currentDate = new Date(GetCurrentTime());
        if (offsetMin <= 0) {
            if (currentDate.compareTo(pastDate) > 0)
                return true;
            else
                return false;
        } else {
            Date compareDate = new Date(pastDate.getTime() + offsetMin * CoomonValueData.MIN_MILLI_SECONDS);

            if (currentDate.compareTo(compareDate) > 0)
                return true;
            else
                return false;
        }
    }

    // 미래의 시간이 얼마나 남았는지 보여주는 함수
    public String GetRemainTimeByFuture(Date futureTime, boolean secView)
    {
        String Desc = "";
        long diffTime = futureTime.getTime() - GetCurrentTime();
        if (futureTime.equals(new Date(0)) || diffTime <= 0)
            return Desc;

        int hour = (int)(diffTime / CoomonValueData.HOUR_MILLI_SECONDS);
        int min = (int)(diffTime / CoomonValueData.MIN_MILLI_SECONDS) % 60;
        int sec = (int)(diffTime / CoomonValueData.SEC_MILLI_SECONDS) % 60;

        if(hour > 0)
        {
            if(secView)
                Desc = Desc + " " + String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
            else
                Desc = Desc + " " + String.format("%02d", hour) + ":" + String.format("%02d", min);
        }
        else
        {
            if(secView)
                Desc = Desc + " " + String.format("%02d", min) + ":" + String.format("%02d", sec);
            else
                Desc = Desc + " " + String.format("%02d", min);
        }

        return Desc;
    }

    public void PlayVibration(Context context) {
        if (mMyData.nAlarmSetting_Vibration) {
            final Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
        }
    }

    public void PlayAlramSound(Context context, int resId) {
        if (mMyData.nAlarmSetting_Sound) {
            if (mSoundPool == null)
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

    public enum ACTIVITY_TYPE
    {
        NONE,
        BOARD_ACTIVITY,
        HOME_ACTIVITY,
    }

    public void SetActivityTopRightBtn(ACTIVITY_TYPE type)
    {
        switch (type)
        {
            case BOARD_ACTIVITY:
                SetActivityTopRightBtn(false, false, true, true, false);
                break;
            case HOME_ACTIVITY:
                SetActivityTopRightBtn(true, true, false, false, true);
                break;
            default:
                SetActivityTopRightBtn(false, false, false, false, false);
                break;
        }
    }

    private void SetActivityTopRightBtn(boolean itemBox, boolean honeyBox, boolean boardWrite, boolean myBoardList, boolean filter)
    {
        Honey_Box_Enable = honeyBox;
        if (Item_Box != null)
            Item_Box.setVisibility(itemBox ? View.VISIBLE : View.GONE);
        if (Honey_Box != null)
            Honey_Box.setVisibility(honeyBox ? View.VISIBLE : View.GONE);
        if (Board_Write != null)
            Board_Write.setVisibility(boardWrite ? View.VISIBLE : View.GONE);
        if (MyBoard_Write != null)
            MyBoard_Write.setVisibility(myBoardList ? View.VISIBLE : View.GONE);
        if (Filter != null)
            Filter.setVisibility(filter ? View.VISIBLE : View.GONE);

        if (Mail_Alarm != null)
            Mail_Alarm.setVisibility(Honey_Box_Enable && Honey_Box_Indicator_Enable ? View.VISIBLE : View.GONE);

    }

    public void SetCardAlarmVisible(boolean enable) {
        if (Card_Alarm != null)
            Card_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void SetChatAlarmVisible(boolean enable) {
        if (Chat_Alarm != null)
        {
            if(mMyData.GetCurFrag() != 2)
                Chat_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
        }

    }

    public void SetFanAlarmVisible(boolean enable) {
        if (Fan_Alarm != null)
            Fan_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void SetMailAlarmVisible(boolean enable) {
        Honey_Box_Indicator_Enable = enable;

        if (Mail_Alarm != null)
            Mail_Alarm.setVisibility(Honey_Box_Enable && Honey_Box_Indicator_Enable ? View.VISIBLE : View.GONE);

        if(Honey_Box != null)
        {
            if(Honey_Box_Enable && Honey_Box_Indicator_Enable)
            {
                AnimationDrawable frameAnimation = (AnimationDrawable) Honey_Box.getBackground();
                if(frameAnimation.isRunning()) frameAnimation.stop();

                frameAnimation = (AnimationDrawable) Honey_Box.getBackground();
                frameAnimation.start();
            }
        }
    }


    public int Select_OpenedItem() {
        int rtValue = 0;
        int nGrade = 0;
        nGrade = (int) (Math.random() * 2000) + 1;

        if (1001 <= nGrade) rtValue =  1;
        else if (501 <= nGrade && nGrade <= 1000) rtValue = 2;
        else if (231 <= nGrade && nGrade <= 500) rtValue = 3;
        else if (91 <= nGrade && nGrade <= 230) rtValue = 4;
        else if (21 <= nGrade && nGrade <= 90) rtValue = 5;
        else if (2 <= nGrade && nGrade <= 20) rtValue = 6;
        else if (1 == nGrade ) rtValue = 7;

        return rtValue;
    }

    public void View_OpenedItem(Context context, View v, int result, ImageView img_Opened, TextView text_Opened) {
        img_Opened.setImageResource(UIData.getInstance().getJewels()[result]);

        switch (result) {
            case 1: {
                text_Opened.setText(R.string.Item_0_text);
                break;
            }
            case 2: {
                text_Opened.setText(R.string.Item_1_text);
                break;
            }
            case 3: {
                text_Opened.setText(R.string.Item_2_text);
                break;
            }
            case 4: {
                text_Opened.setText(R.string.Item_3_text);
                break;
            }
            case 5: {
                text_Opened.setText(R.string.Item_4_text);
                break;
            }
            case 6: {
                text_Opened.setText(R.string.Item_5_text);
                break;
            }
            case 7: {
                text_Opened.setText(R.string.Item_6_text);
                break;
            }
        }

    }

    public interface ShowBoxOpen_End {
        void EndListener();
    }

    public interface ViewBox_End {
        void EndListener();
    }


    public void BuyItemPopup(final Context context, final ShowBoxOpen_End endListener)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.popup_item_get, null);
        ImageView Img_Opened = (ImageView) v.findViewById(R.id.iv_item);
        TextView Text_Opened = (TextView) v.findViewById(R.id.tv_content);
        //Button Btn_Opened = (Button)v.findViewById(R.id.opened_btn);
        TextView Text_Prob = (TextView)v.findViewById(R.id.tv_prob);
        TextView Text_blar = (TextView)v.findViewById(R.id.tv_blar);
        //Button Btn_Opened = (Button)v.findViewById(R.id.opened_btn);


        final int result = CommonFunc.getInstance().Select_OpenedItem();
        CommonFunc.getInstance().View_OpenedItem(context, v, result, Img_Opened, Text_Opened);
        mMyData.setMyItem(result);
        Text_Prob.setText("확률 " + UIData.getInstance().getProb()[result]);
        Text_blar.setText(UIData.getInstance().getItemBlar()[result]);
        Button btn_confirm = v.findViewById(R.id.button3);
        Button btn_sell = v.findViewById(R.id.button2);
        builder.setView(v);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                View v = LayoutInflater.from(context).inflate(R.layout.dialog_exit_app,null,false);

                final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                final TextView txt_Title;
                txt_Title = (TextView)v.findViewById(R.id.title);
                txt_Title.setText("아이템 판매");
                final TextView txt_Body;
                txt_Body = (TextView)v.findViewById(R.id.msg);
                txt_Body.setText(UIData.getInstance().getItems()[result] + " 아이템을 4골드에 판매합니다");

                final Button btn_exit;
                final Button btn_no;

                btn_exit = (Button) v.findViewById(R.id.btn_yes);
                btn_exit.setText("판매");
                btn_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonFunc.getInstance().ShowToast(context, "아이템이 4골드에 판매되었습니다", false);
                        int nCount = mMyData.itemList.get(result);
                        nCount--;
                        mMyData.itemList.put(result, nCount);
                        mMyData.setUserHoney(mMyData.getUserHoney() + 4);
                        mMyData.SetBestItem();
                        mMyData.SaveMyItem(result, nCount);
                        mMyData.refreshItemIdex();
                        endListener.EndListener();
                        dialog.dismiss();


                    }
                });

                btn_no = (Button) v.findViewById(R.id.btn_no);
                btn_no.setText("닫기");
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });

            }
        });
    }

    public void ViewBox(final Context context, final int Index, final ShowBoxOpen_End endListener)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.popup_item_get, null);
        ImageView Img_Opened = (ImageView) v.findViewById(R.id.iv_item);
        TextView Text_Opened = (TextView) v.findViewById(R.id.tv_content);
        TextView Text_Prob = (TextView)v.findViewById(R.id.tv_prob);
        //Button Btn_Opened = (Button)v.findViewById(R.id.opened_btn);
        TextView Text_blar = (TextView)v.findViewById(R.id.tv_blar);

        Text_Prob.setText("확률 " + UIData.getInstance().getProb()[Index]);
        Text_blar.setText(UIData.getInstance().getItemBlar()[Index]);
        CommonFunc.getInstance().View_OpenedItem(context, v, Index, Img_Opened, Text_Opened);

        Button btn_confirm = v.findViewById(R.id.button3);
        Button btn_sell = v.findViewById(R.id.button2);
        builder.setView(v);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();  

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                View v = LayoutInflater.from(context).inflate(R.layout.dialog_exit_app,null,false);

                final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                final TextView txt_Title;
                txt_Title = (TextView)v.findViewById(R.id.title);
                txt_Title.setText("아이템 판매");
                final TextView txt_Body;
                txt_Body = (TextView)v.findViewById(R.id.msg);
                txt_Body.setText(UIData.getInstance().getItems()[Index] + " 아이템을 4코인에 판매합니다");

                final Button btn_exit;
                final Button btn_no;

                btn_exit = (Button) v.findViewById(R.id.btn_yes);
                btn_exit.setText("판매");
                btn_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonFunc.getInstance().ShowToast(context, "아이템 판매 완료!", false);
                        int nCount = mMyData.itemList.get(Index);
                        nCount--;
                        mMyData.itemList.put(Index, nCount);
                        mMyData.setUserHoney(mMyData.getUserHoney() + 4);
                        mMyData.SetBestItem();
                        mMyData.SaveMyItem(Index, nCount);
                        mMyData.refreshItemIdex();
                        endListener.EndListener();
                        dialog.dismiss();
                    }
                });

                btn_no = (Button) v.findViewById(R.id.btn_no);
                btn_no.setText("닫기");
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });

            }
        });
    }

    public void ShowBoxOpen(final Context context, final int count, final int bonus, final ShowBoxOpen_End endListener, final ShowBoxOpen_End buyGoldListener) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_exit_app, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView tv_title = v.findViewById(R.id.title);
        if (bonus > 0)
            tv_title.setText("보석 " + count + "개 + 보너스 " + bonus + "개를 뽑을까요?");
        else
            tv_title.setText("보석 " + count + "개를 뽑을까요?");

        TextView tv_msg = v.findViewById(R.id.msg);


      //  if (mMyData.getUserHoney() > CoomonValueData.OPEN_BOX_COST * count)
        {
            tv_msg.setText((CoomonValueData.OPEN_BOX_COST * count) + "코인이 필요합니다");
            Button btn_yes = v.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    if (mMyData.getUserHoney() >= (CoomonValueData.OPEN_BOX_COST * count)) {
                        mMyData.setUserHoney(mMyData.getUserHoney() - (CoomonValueData.OPEN_BOX_COST * count));
                        for (int i = 0; i < count + bonus; i++) {
                            BuyItemPopup(context, endListener);
                        }

                        endListener.EndListener();
                    } else {
                        CommonFunc.getInstance().ShowToast(context, "코인이 부족합니다.", false);
                    }
                }
            });

            btn_yes.setText("네");
            Button btn_no = v.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {
                    dialog.dismiss();
                }

            });

            btn_no.setText("취소");
        }
 /*       else
            {
            int nGold = (CoomonValueData.OPEN_BOX_COST * count) - mMyData.getUserHoney();
            tv_msg.setText(nGold + "골드가 부족합니다");
            Button btn_yes = v.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new_img View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buyGoldListener.EndListener();
                    dialog.cancel();

                }
            });

            btn_yes.setText("골드 충전하기");
            Button btn_no = v.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new_img View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btn_no.setText("닫기");
        }*/
    }

    public boolean CheckTextMaxLength(String text, int maxLength, Context context, String Title, boolean emptyCheck)
    {
        String tempStr = text;
        boolean stringEmpty = IsStringEmptyCheck(tempStr);

        if(tempStr.length() > maxLength)
            ShowDefaultPopup(context, Title, maxLength+"자 이하로 작성 하셔야 합니다.");
        else
        {
            if(emptyCheck && stringEmpty)
                ShowDefaultPopup(context, Title, "내용이 없습니다.");
        }

        if(tempStr.length() > 0 && tempStr.length() <= maxLength)
            return true;

        return false;
    }

    public boolean IsStringEmptyCheck(String text)
    {
        String tempStr = text;
        tempStr = tempStr.replace("\n",""); // 개행 문자 제거
        tempStr = tempStr.replace(" ",""); // 공란제거

        return tempStr.length() <= 0;
    }

    public String RemoveEmptyString(String text)
    {
        String returnString = "";

        boolean frontEmpty = true;
        for(int index = 0 ; index < text.length() ; ++index)
        {
            if(frontEmpty && (text.charAt(index) == ' ' || text.charAt(index) == '\n'))
                continue;

            frontEmpty = false;
            returnString += text.charAt(index);
        }

        return returnString;
    }

    public void setClickStatus(boolean Status)
    {
        bClickSync = Status;
    }

    public boolean getClickStatus()
    {
        return bClickSync;
    }

    public interface HeartGiftPopup_Send_End {
        void EndListener(int heartCount, String msg);
    }

    public interface HeartGiftPopup_Change_End {
        void EndListener();
    }

    public void HeartGiftPopup(final Context context, String targetIdx, final HeartGiftPopup_Change_End heartChagneFunc, final HeartGiftPopup_Send_End sendFunc) {
        boolean bBlocked = mMyData.arrBlockedDataList.indexOf(targetIdx) > 0; // 내가 차단 당함
        boolean bBlock = mMyData.arrBlockDataList.indexOf(targetIdx) > 0; // 내가 차단함

        if(ShowBlockUser(context, targetIdx) == false)
        {
            final int[] nSendHoneyCnt = new int[1];
            final View v = LayoutInflater.from(context).inflate(R.layout.alert_send_gift, null, false);

            final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

            final TextView Msg = v.findViewById(R.id.HeartPop_text);
            final TextView coin_size = v.findViewById(R.id.tv_coin);
            coin_size.setText(String.valueOf(mMyData.getUserHoney()));

            final Button btn_gift_send = v.findViewById(R.id.btn_gift_send);
            final EditText SendMsg = v.findViewById(R.id.HeartPop_Msg);

            Button btnHeartCharge = v.findViewById(R.id.HeartPop_Charge);
            btnHeartCharge.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    heartChagneFunc.EndListener();

                    dialog.dismiss();
                }
            });

            btnHeart10 = v.findViewById(R.id.HeartPop_10);
            btnHeart30 = v.findViewById(R.id.HeartPop_30);
            btnHeart50 = v.findViewById(R.id.HeartPop_50);
            btnHeart100 = v.findViewById(R.id.HeartPop_100);
            btnHeart300 = v.findViewById(R.id.HeartPop_300);
            btnHeart500 = v.findViewById(R.id.HeartPop_500);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.HeartPop_10:
                            SetHeartBtnColor(0);
                            SetHeartFontColor(0);

                            nSendHoneyCnt[0] = 10;
                            break;
                        case R.id.HeartPop_30:
                            SetHeartBtnColor(1);
                            SetHeartFontColor(1);

                            nSendHoneyCnt[0] = 30;
                            break;
                        case R.id.HeartPop_50:
                            SetHeartBtnColor(2);
                            SetHeartFontColor(2);

                            nSendHoneyCnt[0] = 50;
                            break;
                        case R.id.HeartPop_100:
                            SetHeartBtnColor(3);
                            SetHeartFontColor(3);

                            nSendHoneyCnt[0] = 100;
                            break;
                        case R.id.HeartPop_300:
                            SetHeartBtnColor(4);
                            SetHeartFontColor(4);

                            nSendHoneyCnt[0] = 300;
                            break;
                        case R.id.HeartPop_500:
                            SetHeartBtnColor(5);
                            SetHeartFontColor(5);

                            nSendHoneyCnt[0] = 500;
                            break;
                    }

                    if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                        int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                        btn_gift_send.setEnabled(false);
                        Msg.setText("코인이 부족합니다. (" + nSendHoneyCnt[0] + " 코인 필요)");
                    } else {
                        btn_gift_send.setEnabled(true);
                        Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?(" + nSendHoneyCnt[0] + "코인 소모)");
                    }
                }
            };

            btnHeart10.setOnClickListener(listener);
            btnHeart10.callOnClick();
            btnHeart30.setOnClickListener(listener);
            btnHeart50.setOnClickListener(listener);
            btnHeart100.setOnClickListener(listener);
            btnHeart300.setOnClickListener(listener);
            btnHeart500.setOnClickListener(listener);


            btn_gift_send.setOnClickListener(new View.OnClickListener()
            {
                 @Override
                 public void onClick(View view) {
                     String strSendMsg = SendMsg.getText().toString();

                     sendFunc.EndListener(nSendHoneyCnt[0], strSendMsg);

                     dialog.dismiss();
                     CommonFunc.getInstance().ShowToast(context, nSendHoneyCnt[0] + " 하트를 보냈습니다.", true);

                 }
             });

            Button btn_gift_cancel = v.findViewById(R.id.btn_gift_cancel);
            btn_gift_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            ConstraintLayout ll = (ConstraintLayout)v.findViewById(R.id.constraintLayout);
            ll.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    imm.hideSoftInputFromWindow(SendMsg.getWindowToken(), 0);
                }
            });
        }
    }

    private void SetHeartBtnColor(int idx) {
        btnHeart10.setBackgroundResource(R.drawable.heart_empty);
        btnHeart30.setBackgroundResource(R.drawable.heart_empty);
        btnHeart50.setBackgroundResource(R.drawable.heart_empty);
        btnHeart100.setBackgroundResource(R.drawable.heart_empty);
        btnHeart300.setBackgroundResource(R.drawable.heart_empty);
        btnHeart500.setBackgroundResource(R.drawable.heart_empty);

        switch (idx)
        {
            case 0:
                btnHeart10.setBackgroundResource(R.drawable.heart_red);
                break;
            case 1:
                btnHeart30.setBackgroundResource(R.drawable.heart_red);
                break;
            case 2:
                btnHeart50.setBackgroundResource(R.drawable.heart_red);
                break;
            case 3:
                btnHeart100.setBackgroundResource(R.drawable.heart_red);
                break;
            case 4:
                btnHeart300.setBackgroundResource(R.drawable.heart_red);
                break;
            case 5:
                btnHeart500.setBackgroundResource(R.drawable.heart_red);
                break;
        }
    }

    private void SetHeartFontColor(int idx) {
        btnHeart10.setTextColor(Color.BLACK);
        btnHeart30.setTextColor(Color.BLACK);
        btnHeart50.setTextColor(Color.BLACK);
        btnHeart100.setTextColor(Color.BLACK);
        btnHeart300.setTextColor(Color.BLACK);
        btnHeart500.setTextColor(Color.BLACK);

        switch (idx)
        {
            case 0:
                btnHeart10.setTextColor(Color.WHITE);
                break;
            case 1:
                btnHeart30.setTextColor(Color.WHITE);
                break;
            case 2:
                btnHeart50.setTextColor(Color.WHITE);
                break;
            case 3:
                btnHeart100.setTextColor(Color.WHITE);
                break;
            case 4:
                btnHeart300.setTextColor(Color.WHITE);
                break;
            case 5:
                btnHeart500.setTextColor(Color.WHITE);
                break;
        }
    }


    public boolean ShowBlockUser(Context context, String targetIdx)
    {
        boolean bBlocked = false;// 내가 차단 당함
        for(int index = 0 ; index < mMyData.arrBlockedDataList.size() ; ++index)
        {
            if(mMyData.arrBlockedDataList.get(index).Idx.equals(targetIdx))
                bBlocked = true;
        }

        boolean bBlock = false;// 내가 차단함
        for(int index = 0 ; index < mMyData.arrBlockDataList.size() ; ++index)
        {
            if(mMyData.arrBlockDataList.get(index).Idx.equals(targetIdx))
                bBlock = true;
        }

        if (bBlocked == true)
        {
            ShowDefaultPopup(context, "차단",  "당신은 차단 되었습니다");
            return true;
        }
        else if (bBlock == true)
        {
            ShowDefaultPopup(context, "차단",  "당신이 차단한 상대입니다");
            return true;
        }

        return false;
    }

    public void ShowLoadingPage(Context context, String message) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            SetShowLoadingPageMsg(message);
        } else {

            mProgressDialog = new AppCompatDialog(context);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.loading_dialog);
            mProgressDialog.show();

        }

        TextView tv_progress_message = (TextView) mProgressDialog.findViewById(R.id.tv_progress_message);
        if(message.isEmpty())
            message = "로딩중";
        //tv_progress_message.setText(message);
    }

    public void SetShowLoadingPageMsg(String message) {

        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        TextView tv_progress_message = (TextView) mProgressDialog.findViewById(R.id.tv_progress_message);
        if(message.isEmpty())
            message = "로딩중";
        //tv_progress_message.setText(message);
    }

    public void DismissLoadingPage() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
