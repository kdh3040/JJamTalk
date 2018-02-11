package com.hodo.talkking.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.talkking.BuyGoldActivity;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.FanData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SendData;
import com.hodo.talkking.Data.SimpleChatData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.MainActivity;
import com.hodo.talkking.R;
import com.hodo.talkking.UserPageActivity;

import java.net.MalformedURLException;
import java.net.URL;
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

    public ImageView Card_Alarm, Chat_Alarm, Mail_Alarm, Fan_Alarm;
    public ImageView Item_Box, Honey_Box, Board_Write;
    public Button MyBoard_Write;

    public void refreshMainActivity(Activity mActivity, int StartFragMent) {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra("StartFragment", StartFragMent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
        mActivity.finish();
        //mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
    }

    public void MoveUserPage(Activity mActivity, UserData tempUserData) {

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
        //mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
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
        ShowDefaultPopup(context, null, title, centerDesc, null, null, false);
    }

    public void ShowDefaultPopup(Context context, final ShowDefaultPopup_YesListener listenerYes, String title, String centerDesc, String yesDesc, String noDesc) {
        ShowDefaultPopup(context, listenerYes, title, centerDesc, yesDesc, noDesc, true);
    }

    public void ShowDefaultPopup(Context context, final ShowDefaultPopup_YesListener listenerYes, String title, String centerDesc, String yesDesc, String noDesc, Boolean btnView) {
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

    public void SetCardAlarmVisible(boolean enable) {
        if (Card_Alarm != null)
            Card_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void SetChatAlarmVisible(boolean enable) {
        if (Chat_Alarm != null)
            Chat_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void SetFanAlarmVisible(boolean enable) {
        if (Fan_Alarm != null)
            Fan_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void SetMailAlarmVisible(boolean enable) {
        if (Mail_Alarm != null)
            Mail_Alarm.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void SetMainActivityTopRightBtn(boolean boardMode) {
        if (Item_Box != null)
            Item_Box.setVisibility(boardMode ? View.GONE : View.VISIBLE);
        if (Honey_Box != null)
            Honey_Box.setVisibility(boardMode ? View.GONE : View.VISIBLE);
        if (Board_Write != null)
            Board_Write.setVisibility(boardMode ? View.VISIBLE : View.GONE);
        if (MyBoard_Write != null)
            MyBoard_Write.setVisibility(boardMode ? View.VISIBLE : View.GONE);
    }


    public int Select_OpenedItem() {
        int rtValue = 0;
        int nGrade = 0;
        nGrade = (int) (Math.random() * 2000) + 1;

        if (604 <= nGrade) rtValue = 1;

        else if (160 <= nGrade && nGrade <= 603) rtValue = 2;
        else if (80 <= nGrade && nGrade <= 160) rtValue = 3;
        else if (58 <= nGrade && nGrade <= 80) rtValue = 4;
        else if (28 <= nGrade && nGrade <= 58) rtValue = 5;
        else if (15 <= nGrade && nGrade <= 27) rtValue = 6;
        else if (2 <= nGrade && nGrade <= 14) rtValue = 7;
        else if (1 == nGrade) rtValue = 8;

        return rtValue;
    }

    public void View_OpenedItem(Context context, View v, int result, ImageView img_Opened, TextView text_Opened) {
        URL url = null;
        Bitmap bitmap = null;


        switch (result) {
            case 0: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fheel_hng.png?alt=media&token=b63df8ec-7946-455f-a7db-f6555c13b8a3");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                text_Opened.setText("명품 구두 획득!!");
                break;
            }
            case 1: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fdress_hng.png?alt=media&token=3e195e09-0fcb-4cf9-b154-9c871dac8dc5");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("명품 드레스 획득!!");
                break;
            }
            case 2: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fbag_hng.png?alt=media&token=14ce0c10-fcaa-4d7b-b196-dc56a1f86233");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("명품 가방 획득!!");
                break;
            }
            case 3: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fwatch_hng.png?alt=media&token=dbefc601-6770-48f3-a6e7-227a15ae5d36");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("명품 시계 획득!!");
                break;
            }
            case 4: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fring_hng.png?alt=media&token=2d624e62-9b42-4b44-b268-81ddc4c98ccf");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                text_Opened.setText("보석 획득!!");
                break;
            }
            case 5: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fcar_hng.png?alt=media&token=8ecd4bfc-3911-4c87-86d5-9ed055c3a864");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                text_Opened.setText("자동차 획득!!");
                break;
            }
            case 6: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fboat_hng.png?alt=media&token=5c42d065-c643-4517-8751-88a71b45d14d");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("요트 획득!!");
                break;
            }
            case 7: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fjet_hng.png?alt=media&token=0db8857d-9481-43e8-af74-45f7337deaf5");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                text_Opened.setText("제트기 획득!!");
                break;
            }
        }

        Glide.with(context)
                .load(url.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(img_Opened);
    }

    public interface ShowBoxOpen_End {
        void EndListener();
    }

    public void ShowBoxOpen(final Context context, final int count, final int bonus, final ShowBoxOpen_End endListener, final ShowBoxOpen_End buyGoldListener) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_exit_app, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView tv_title = v.findViewById(R.id.title);
        if (bonus > 0)
            tv_title.setText("상자 " + count + "개 + 보너스 " + bonus + "개 열기");
        else
            tv_title.setText("상자 " + count + "개를 열까요?");

        TextView tv_msg = v.findViewById(R.id.msg);


        if (mMyData.getUserHoney() > CoomonValueData.OPEN_BOX_COST * count) {
            tv_msg.setText(CoomonValueData.OPEN_BOX_COST + "골드가 필요합니다");
            Button btn_yes = v.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    if (mMyData.getUserHoney() > (CoomonValueData.OPEN_BOX_COST * count)) {
                        mMyData.setUserHoney(mMyData.getUserHoney() - (CoomonValueData.OPEN_BOX_COST * count));
                        for (int i = 0; i < count + bonus; i++) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View v = LayoutInflater.from(context).inflate(R.layout.dialog_jewelbox_opened, null);
                            ImageView Img_Opened = (ImageView) v.findViewById(R.id.opened_img);
                            TextView Text_Opened = (TextView) v.findViewById(R.id.opened_text);
                            //Button Btn_Opened = (Button)v.findViewById(R.id.opened_btn);

                            int result = CommonFunc.getInstance().Select_OpenedItem();
                            CommonFunc.getInstance().View_OpenedItem(context, v, result, Img_Opened, Text_Opened);
                            mMyData.setMyItem(result);

                            Button btn_confirm = v.findViewById(R.id.opened_btn);
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
                        }

                        endListener.EndListener();
                    } else {
                        Toast.makeText(context, "골드가 부족합니다", Toast.LENGTH_LONG).show();
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

            btn_no.setText("아니오");
        } else {
            int nGold = (CoomonValueData.OPEN_BOX_COST * count) - mMyData.getUserHoney();
            tv_msg.setText(nGold + "골드가 부족합니다");
            Button btn_yes = v.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buyGoldListener.EndListener();
                    dialog.cancel();

                }
            });

            btn_yes.setText("골드 충전하기");
            Button btn_no = v.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btn_no.setText("닫기");
        }
    }

    public boolean CheckTextMaxLength(String text, int maxLength, Context context, String Title, boolean emptyCheck)
    {
        boolean emptyString = false;
        if(text.equals("") || text.equals("\n") || text.equals("\n\n") || text.equals("\n\n\n") || text.equals("\n\n\n\n") || text.replace(" ", "").equals(""))
            emptyString = true;

        if(text.length() > maxLength && emptyString == false)
            ShowDefaultPopup(context, Title, maxLength+"자 이하로 작성 하셔야 합니다.");
        else
        {
            if(emptyCheck && emptyString)
                ShowDefaultPopup(context, Title, "채팅 내용이 없습니다.");
        }

        if(text.length() <= maxLength)
            return true;

        return false;
    }
}
