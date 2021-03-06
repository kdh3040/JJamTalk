package com.dohosoft.talkking;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Switch;

import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SettingData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.CommonFunc;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class SettingActivity extends AppCompatActivity {

    private SettingData mSetting = SettingData.getInstance();
    private MyData mMyData = MyData.getInstance();
    private FirebaseData mFireBaseData = FirebaseData.getInstance();


    private Button  btn_Law;
    private Button  btn_Help;
    private Button  btn_LogOut;







    private Button alarm;
    private Button recvMsg;
    private Button blockList;
    private Button share;
    private Button buyGold;
    private Button delete;


    private RadioButton btn_ViewMode_2;
    private RadioButton btn_ViewMode_3;
    private RadioButton btn_ViewMode_4;

    //private Switch sw_SearchMan;
    //private Switch sw_SearchWoman;

    private Switch sw_AlarmNoti;
    private Switch sw_AlarmFollow;
    private Switch sw_AlarmLike;



    private CheckBox cbRecvMsg;

    private CheckBox SoundCheckBox;
    private CheckBox VibrationCheckBox;
    private CheckBox PopCheckBox;

    private CheckBox RecvRejectCheckBox;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting,menu);


        return true;
    }

    private void SaveSettingData()
    {
        mMyData.setSettingData(getApplicationContext(),mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting());
        mFireBaseData.SaveSettingData(mMyData.getUserIdx(), mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration(), mSetting.IsAlarmSettingPop());
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            SaveSettingData();
            onBackPressed();
        }
        if(keyCode == KeyEvent.KEYCODE_HOME){
            SaveSettingData();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            SaveSettingData();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //imageview 및 textview 초기화

        mMyData.SetCurFrag(0);

        alarm = findViewById(R.id.btn_alarm);
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_setting_alarm,null,false);

                SoundCheckBox = (CheckBox) v.findViewById(R.id.sound);
                SoundCheckBox.setChecked(mMyData.nAlarmSetting_Sound);
                VibrationCheckBox = (CheckBox) v.findViewById(R.id.vibration);
                VibrationCheckBox.setChecked(mMyData.nAlarmSetting_Vibration);

                PopCheckBox = (CheckBox) v.findViewById(R.id.popup);
                PopCheckBox.setChecked(mMyData.nAlarmSetting_Pop);

                final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).setView(v).create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss (DialogInterface var1){

                        mMyData.setAlarmSettingData(SoundCheckBox.isChecked(), VibrationCheckBox.isChecked(), PopCheckBox.isChecked());

                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PrefSetting", getApplicationContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("Sound",SoundCheckBox.isChecked() );
                        editor.putBoolean("Vibe",VibrationCheckBox.isChecked() );
                        editor.commit();


                    }

                });

                dialog.show();
            }
        });

        recvMsg = findViewById(R.id.recvMessage);
        recvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_setting_recv,null,false);

                RecvRejectCheckBox = (CheckBox) v.findViewById(R.id.reject);
                RecvRejectCheckBox.setChecked(mMyData.nRecvMsgReject);

                final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).setView(v).create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss (DialogInterface var1){
                        mSetting.setRecvMsgRejectSetting(RecvRejectCheckBox.isChecked());
                    }

                });

                dialog.show();
            }
        });

        blockList = (Button) findViewById(R.id.blocklist);
        blockList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BlockListActivity.class));
            }
        });

        share = (Button) findViewById(R.id.btn_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "회원님을 위한 특별한 어플을 발견했습니다.\n톡킹에 로그인해보세요 \n" + CoomonValueData.getInstance().DownUrl;
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                // intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                Intent chooser = Intent.createChooser(intent, "타이틀");
                startActivity(chooser);
            }
        });


        //1 이 차단 0이 디폴트
        //cbRecvMsg = (CheckBox)findViewById(R.id.checkBox2);
        /*if(mSetting.getnRecvMsg() == 0)
            cbRecvMsg.setChecked(false);
        else
            cbRecvMsg.setChecked(true);

        /*cbRecvMsg.setOnClickListener(new_img CheckBox.OnClickListener() {
            @Override public void onClick(View v) {
                if(cbRecvMsg.isChecked() == true)
                    mSetting.setnRecvMsg(1);
                else
                    mSetting.setnRecvMsg(0);
                 }
        }) ;*/

        buyGold = findViewById(R.id.buyCoin);
        buyGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BuyGoldActivity.class));


            }
        });


        btn_Help = (Button) findViewById(R.id.btn_help);
        btn_Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HelpActivity.class));


            }
        });
        delete = (Button) findViewById(R.id.btn_del);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                    public void YesListener() {

                        FirebaseData.getInstance().DelUser(mMyData.ANDROID_ID, mMyData.getUserIdx());

                        mMyData.Clear();
                        CommonFunc.getInstance().restartApp(getApplicationContext());
                        finish();

                /*        FirebaseUser currentUser =  FirebaseAuth.getInstance().getCurrentUser();
                        currentUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseData.getInstance().DelUser(mMyData.ANDROID_ID, mMyData.getUserIdx());

                                            mMyData.Clear();

                                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                        }
                                    }
                                });*/


                    }
                };

                CommonFunc.getInstance().ShowDefaultPopup(SettingActivity.this, listener, null, getResources().getString(R.string.setting_delete_title), getResources().getString(R.string.setting_delete_body), getResources().getString(R.string.text_yes), getResources().getString(R.string.text_no));
            }
        });



        btn_Law = (Button) findViewById(R.id.doc_law);
        btn_Law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AccessActivity.class);
                startActivity(intent);
            }
        });
    }

        /*btn_ViewMode_2 = (RadioButton) findViewById(R.id.rb_2);

        btn_ViewMode_3 = (RadioButton) findViewById(R.id.rb_3);
        btn_ViewMode_4 = (RadioButton) findViewById(R.id.rb_4);


        RadioButton.OnClickListener optionOnClickListener = new_img RadioButton.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(btn_ViewMode_2.isChecked())
                    mSetting.setnViewSetting(0);
                else if(btn_ViewMode_3.isChecked())
                    mSetting.setnViewSetting(1);
                else if(btn_ViewMode_4.isChecked())
                    mSetting.setnViewSetting(2);
            }
        };

        btn_ViewMode_2.setOnClickListener(optionOnClickListener);
        btn_ViewMode_3.setOnClickListener(optionOnClickListener);
        btn_ViewMode_4.setOnClickListener(optionOnClickListener);


        sw_SearchMan = (Switch)findViewById(R.id.Setting_swMan);
        sw_SearchWoman = (Switch)findViewById(R.id.Setting_swWoman);
        */

        //sw_AlarmNoti = (Switch)findViewById(R.id.Setting_swNoti);
        //sw_AlarmFollow = (Switch)findViewById(R.id.Setting_swFollow);
        //sw_AlarmLike = (Switch)findViewById(R.id.Setting_swLike);

        //initSearchValue();
        //initNotiValue();
        //initViewValue();

        /*sw_SearchMan.setOnCheckedChangeListener(new_img CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if(bChecked == true) {
                    if(sw_SearchWoman.isChecked() == true)
                        mSetting.setnSearchSetting(3);
                    else
                        mSetting.setnSearchSetting(1);
                }
                else
                    mSetting.setnSearchSetting(2);
            }
        });

        sw_SearchWoman.setOnCheckedChangeListener(new_img CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if(bChecked == true)
                {
                    if(sw_SearchMan.isChecked() == true)
                        mSetting.setnSearchSetting(3);
                    else
                        mSetting.setnSearchSetting(2);
                }
                else
                    mSetting.setnSearchSetting(1);

            }
        });*/

        /*sw_AlarmNoti.setOnCheckedChangeListener(new_img CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if(bChecked == true)
                {
                    if(sw_AlarmFollow.isChecked() == true && sw_AlarmLike.isChecked() == true)  // 1 1 1
                        mSetting.setnAlarmSetting(7);
                    else if(sw_AlarmFollow.isChecked() == true && sw_AlarmLike.isChecked() == false) // 1 1 0
                        mSetting.setnAlarmSetting(6);
                    else if(sw_AlarmFollow.isChecked() == false && sw_AlarmLike.isChecked() == true) // 1 0 1
                        mSetting.setnAlarmSetting(5);
                    else if(sw_AlarmFollow.isChecked() == false && sw_AlarmLike.isChecked() == false) // 1 0 0
                        mSetting.setnAlarmSetting(4);
                }
                else {
                    if(sw_AlarmFollow.isChecked() == true && sw_AlarmLike.isChecked() == true) // 0 1 1
                        mSetting.setnAlarmSetting(3);
                    else if(sw_AlarmFollow.isChecked() == true && sw_AlarmLike.isChecked() == false) // 0 1 0
                        mSetting.setnAlarmSetting(2);
                    else if(sw_AlarmFollow.isChecked() == false && sw_AlarmLike.isChecked() == true) // 0 0 1
                        mSetting.setnAlarmSetting(1);
                    else if(sw_AlarmFollow.isChecked() == false && sw_AlarmLike.isChecked() == false) // 0 0 0
                        mSetting.setnAlarmSetting(0);
                }
            }
        });

        sw_AlarmFollow.setOnCheckedChangeListener(new_img CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if(bChecked == true)
                {
                    if(sw_AlarmNoti.isChecked() == true && sw_AlarmLike.isChecked() == true)  // 1 1 1
                        mSetting.setnAlarmSetting(7);
                    else if(sw_AlarmNoti.isChecked() == true && sw_AlarmLike.isChecked() == false) // 1 1 0
                        mSetting.setnAlarmSetting(6);
                    else if(sw_AlarmNoti.isChecked() == false && sw_AlarmLike.isChecked() == true) // 0 1 1
                        mSetting.setnAlarmSetting(3);
                    else if(sw_AlarmNoti.isChecked() == false && sw_AlarmLike.isChecked() == false) // 0 1 0
                        mSetting.setnAlarmSetting(2);
                }
                else {

                     if(sw_AlarmNoti.isChecked() == true && sw_AlarmLike.isChecked() == true) // 1 0 1
                        mSetting.setnAlarmSetting(5);
                    else if(sw_AlarmNoti.isChecked() == true && sw_AlarmLike.isChecked() == false) // 1 0 0
                        mSetting.setnAlarmSetting(4);
                    else if(sw_AlarmNoti.isChecked() == false && sw_AlarmLike.isChecked() == true) // 0 0 1
                        mSetting.setnAlarmSetting(1);
                    else if(sw_AlarmNoti.isChecked() == false && sw_AlarmLike.isChecked() == false) // 0 0 0
                        mSetting.setnAlarmSetting(0);
                }

            }
        });
        sw_AlarmLike.setOnCheckedChangeListener(new_img CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if(bChecked == true)
                {
                    if(sw_AlarmNoti.isChecked() == true && sw_AlarmFollow.isChecked() == true)  // 1 1 1
                        mSetting.setnAlarmSetting(7);
                    else if(sw_AlarmNoti.isChecked() == false && sw_AlarmFollow.isChecked() == true) // 0 1 1
                        mSetting.setnAlarmSetting(3);
                    else if(sw_AlarmNoti.isChecked() == true && sw_AlarmFollow.isChecked() == false) // 1 0 1
                        mSetting.setnAlarmSetting(5);
                    else if(sw_AlarmNoti.isChecked() == false && sw_AlarmFollow.isChecked() == false) // 0 0 1
                        mSetting.setnAlarmSetting(1);
                }
                else {

                    if(sw_AlarmNoti.isChecked() == true && sw_AlarmFollow.isChecked() == false) // 1 0 0
                        mSetting.setnAlarmSetting(4);
                      else if(sw_AlarmNoti.isChecked() == true && sw_AlarmFollow.isChecked() == true) // 1 1 0
                        mSetting.setnAlarmSetting(6);
                    else if(sw_AlarmNoti.isChecked() == false && sw_AlarmFollow.isChecked() == true) // 0 1 0
                        mSetting.setnAlarmSetting(2);
                    else if(sw_AlarmNoti.isChecked() == false && sw_AlarmFollow.isChecked() == false) // 0 0 0
                        mSetting.setnAlarmSetting(0);
                }
            }
        });

    }



    /*private void initSearchValue() {

        if(mSetting.getnSearchSetting() == 1) {
            sw_SearchMan.setChecked(true);
            sw_SearchWoman.setChecked(false);
        }
        else if(mSetting.getnSearchSetting() == 2) {
            sw_SearchMan.setChecked(false);
            sw_SearchWoman.setChecked(true);
        }
        else{
            sw_SearchMan.setChecked(true);
            sw_SearchWoman.setChecked(true);
        }
    }*/


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        android.app.AlertDialog.Builder mDialog = null;
        mDialog = new android.app.AlertDialog.Builder(this);

  /*      if (!MyData.getInstance().verSion.equals(MyData.getInstance().marketVersion)) {
            mDialog.setMessage("업데이트 후 사용해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("업데이트 바로가기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent marketLaunch = new Intent(
                                            Intent.ACTION_VIEW);
                                    marketLaunch.setData(Uri
                                            //.parse("https://play.google.com/store/apps/details?id=패키지명 적으세요"));
                                            .parse("https://play.google.com/store/apps/details?id=com.dohosoft.talkking"));

                                    startActivity(marketLaunch);
                                    System.exit(0);
                                }
                            });
            android.app.AlertDialog alert = mDialog.create();
            alert.setTitle("안 내");
            alert.show();
        }

        else*/
        {
            if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
                mMyData.badgecount = pref.getInt("Badge", 1 );


                if (mMyData.badgecount >= 1) {
                    mMyData.badgecount = 0;
                    Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                    intent.putExtra("badge_count_package_name", "com.dohosoft.talkking");
                    intent.putExtra("badge_count_class_name", "com.dohosoft.talkking.LoginActivity");
                    intent.putExtra("badge_count", mMyData.badgecount);
                    sendBroadcast(intent);
                }
            }
        }

        mMyData.SetCurFrag(0);
    }


    private void initNotiValue() {

        /*switch (mSetting.getnAlarmSetting())
        {
            case 0:
                sw_AlarmNoti.setChecked(false);
                sw_AlarmFollow.setChecked(false);
                sw_AlarmLike.setChecked(false);
                break;
            case 1:
                sw_AlarmNoti.setChecked(false);
                sw_AlarmFollow.setChecked(false);
                sw_AlarmLike.setChecked(true);
                break;
            case 2:
                sw_AlarmNoti.setChecked(false);
                sw_AlarmFollow.setChecked(true);
                sw_AlarmLike.setChecked(false);
                break;
            case 3:
                sw_AlarmNoti.setChecked(false);
                sw_AlarmFollow.setChecked(true);
                sw_AlarmLike.setChecked(true);
                break;
            case 4:
                sw_AlarmNoti.setChecked(true);
                sw_AlarmFollow.setChecked(false);
                sw_AlarmLike.setChecked(false);
                break;
            case 5:
                sw_AlarmNoti.setChecked(true);
                sw_AlarmFollow.setChecked(false);
                sw_AlarmLike.setChecked(true);
                break;
            case 6:
                sw_AlarmNoti.setChecked(true);
                sw_AlarmFollow.setChecked(true);
                sw_AlarmLike.setChecked(false);
                break;
            case 7:
                sw_AlarmNoti.setChecked(true);
                sw_AlarmFollow.setChecked(true);
                sw_AlarmLike.setChecked(true);
                break;
        }*/
    }

    /*private void initViewValue() {

        if(mSetting.getnViewSetting() == 0) {
            btn_ViewMode_2.setChecked(true);
        }
        else if(mSetting.getnViewSetting() == 1) {
            btn_ViewMode_3.setChecked(true);
        }
        else{
            btn_ViewMode_4.setChecked(true);
        }
    }*/


}
