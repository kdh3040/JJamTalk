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

        initValue();

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

    }

    private void initValue() {

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
}
