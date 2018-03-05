package com.hodo.talkking;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SettingData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.CommonFunc;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class SettingActivity extends AppCompatActivity {

    private SettingData mSetting = SettingData.getInstance();
    private MyData mMyData = MyData.getInstance();
    private FirebaseData mFireBaseData = FirebaseData.getInstance();


    private TextView  btn_Help;
    private TextView  btn_LogOut;
    private TextView  btn_Delete;

    private TextView  btn_Share;


    private LinearLayout lo_recvMsg;
    private LinearLayout lo_buyGold ;
    private LinearLayout lo_alarm;


    private RadioButton btn_ViewMode_2;
    private RadioButton btn_ViewMode_3;
    private RadioButton btn_ViewMode_4;

    //private Switch sw_SearchMan;
    //private Switch sw_SearchWoman;

    private Switch sw_AlarmNoti;
    private Switch sw_AlarmFollow;
    private Switch sw_AlarmLike;

    private TextView tv_blocklist;

    private CheckBox cbRecvMsg;

    private CheckBox SoundCheckBox;
    private CheckBox VibrationCheckBox;
    private CheckBox RecvRejectCheckBox;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile,menu);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            //프로필 저장 구현
            CommonFunc.getInstance().ShowToast(this, "프로필이 저장되었습니다", false);
            mMyData.setSettingData(mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration());
            mFireBaseData.SaveSettingData(mMyData.getUserIdx(), mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration());

            onBackPressed();
           // finish();
       //     overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

        }
        if(item.getItemId() == android.R.id.home)
        {
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

        lo_alarm = findViewById(R.id.ll_alarm);
        lo_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_setting_alarm,null,false);

                SoundCheckBox = (CheckBox) v.findViewById(R.id.sound);
                SoundCheckBox.setChecked(mMyData.nAlarmSetting_Sound);
                VibrationCheckBox = (CheckBox) v.findViewById(R.id.vibration);
                VibrationCheckBox.setChecked(mMyData.nAlarmSetting_Vibration);

                final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).setView(v).create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss (DialogInterface var1){
                        mSetting.setAlarmSetting(SoundCheckBox.isChecked(), VibrationCheckBox.isChecked());
                    }

                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }
        });

        lo_recvMsg = findViewById(R.id.lo_recvMsg);
        lo_recvMsg.setOnClickListener(new View.OnClickListener() {
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
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }
        });

        tv_blocklist = (TextView)findViewById(R.id.tv_blocklist);
        tv_blocklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BlockListActivity.class));
            }
        });

        btn_Share = (TextView)findViewById(R.id.btn_share);
        btn_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "회원님을 위한 특별한 어플을 발견했습니다.\n톡킹에 로그인해보세요 \n" + mMyData.strDownUri;
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

        /*cbRecvMsg.setOnClickListener(new CheckBox.OnClickListener() {
            @Override public void onClick(View v) {
                if(cbRecvMsg.isChecked() == true)
                    mSetting.setnRecvMsg(1);
                else
                    mSetting.setnRecvMsg(0);
                 }
        }) ;*/

        lo_buyGold = findViewById(R.id.lo_buyGold);
        lo_buyGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BuyGoldActivity.class));


            }
        });


        btn_Help = (TextView )findViewById(R.id.Setting_btnHelp);
        btn_LogOut = (TextView )findViewById(R.id.Setting_logout);
        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                    public void YesListener() {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }
                };

                CommonFunc.getInstance().ShowDefaultPopup(SettingActivity.this, listener, "로그아웃", "로그아웃을 하시겠습니까?", "네", "아니요");
            }
        });

        btn_Delete = (TextView )findViewById(R.id.Setting_btnDel);
        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                    public void YesListener() {

                        FirebaseUser currentUser =  FirebaseAuth.getInstance().getCurrentUser();
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
                                });


                    }
                };

                CommonFunc.getInstance().ShowDefaultPopup(SettingActivity.this, listener, "계정삭제", "계정삭제를 하시겠습니까?", "네", "아니요");
            }
        });



    }

        /*btn_ViewMode_2 = (RadioButton) findViewById(R.id.rb_2);

        btn_ViewMode_3 = (RadioButton) findViewById(R.id.rb_3);
        btn_ViewMode_4 = (RadioButton) findViewById(R.id.rb_4);


        RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener(){

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

        /*sw_SearchMan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        sw_SearchWoman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        /*sw_AlarmNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        sw_AlarmFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        sw_AlarmLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

            if (mMyData.badgecount >= 1) {
                mMyData.badgecount = 0;
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count_package_name", "com.hodo.talkking");
                intent.putExtra("badge_count_class_name", "com.hodo.talkking.LoginActivity");
                intent.putExtra("badge_count", mMyData.badgecount);
                sendBroadcast(intent);
            }
        }
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
