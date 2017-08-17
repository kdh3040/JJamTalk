package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.ServiceWorkerClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class SettingActivity extends AppCompatActivity {

    private SettingData mSetting = SettingData.getInstance();
    private MyData mMyData = MyData.getInstance();

    private Button btn_PurchaseHeart;
    private Button btn_Help;
    private Button btn_LogOut;
    private Button btn_Delete;

    private Switch sw_SearchMan;
    private Switch sw_SearchWoman;

    private Switch sw_AlarmNoti;
    private Switch sw_AlarmFollow;
    private Switch sw_AlarmLike;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_PurchaseHeart = (Button)findViewById(R.id.Setting_btnHeart);
        btn_Help = (Button)findViewById(R.id.Setting_btnHelp);
        btn_LogOut = (Button)findViewById(R.id.Setting_btnLogout);
        btn_Delete = (Button)findViewById(R.id.Setting_btnDel);

        sw_SearchMan = (Switch)findViewById(R.id.Setting_swMan);
        sw_SearchWoman = (Switch)findViewById(R.id.Setting_swWoman);

        sw_AlarmNoti = (Switch)findViewById(R.id.Setting_swNoti);
        sw_AlarmFollow = (Switch)findViewById(R.id.Setting_swFollow);
        sw_AlarmLike = (Switch)findViewById(R.id.Setting_swLike);

        initSearchValue();
        initNotiValue();

        sw_SearchMan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        });

        sw_AlarmNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    private void initSearchValue() {

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
    }

    private void initNotiValue() {

        switch (mSetting.getnAlarmSetting())
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
        }
    }
}
