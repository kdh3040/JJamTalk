package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Firebase.FirebaseData;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class SettingActivity extends AppCompatActivity {

    private SettingData mSetting = SettingData.getInstance();
    private MyData mMyData = MyData.getInstance();
    private FirebaseData mFireBaseData = FirebaseData.getInstance();

    private Button btn_PurchaseHeart;
    private Button btn_Help;
    private Button btn_LogOut;
    private Button btn_Delete;

    private RadioButton btn_ViewMode_2;
    private RadioButton btn_ViewMode_3;
    private RadioButton btn_ViewMode_4;

    private Switch sw_SearchMan;
    private Switch sw_SearchWoman;

    private Switch sw_AlarmNoti;
    private Switch sw_AlarmFollow;
    private Switch sw_AlarmLike;

    private TextView tv_blocklist;

    private CheckBox cbRecvMsg;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            //프로필 저장 구현
            Toast.makeText(this,"프로필이 저장되었습니다",Toast.LENGTH_LONG).show();
            mMyData.setSettingData(mSetting.getnSearchSetting(), mSetting.getnAlarmSetting(), mSetting.getnViewSetting(), mSetting.getnRecvMsg());
            mFireBaseData.SaveSettingData(mMyData.getUserIdx(), mSetting.getnSearchSetting(), mSetting.getnAlarmSetting(), mSetting.getnViewSetting(), mSetting.getnRecvMsg());
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

        //1 이 차단 0이 디폴트
        cbRecvMsg = (CheckBox)findViewById(R.id.checkBox2);
        if(mSetting.getnRecvMsg() == 0)
            cbRecvMsg.setChecked(false);
        else
            cbRecvMsg.setChecked(true);

        cbRecvMsg.setOnClickListener(new CheckBox.OnClickListener() {
            @Override public void onClick(View v) {
                if(cbRecvMsg.isChecked() == true)
                    mSetting.setnRecvMsg(1);
                else
                    mSetting.setnRecvMsg(0);
                 }
        }) ;



        btn_PurchaseHeart = (Button)findViewById(R.id.Setting_btnHeart);
        btn_Help = (Button)findViewById(R.id.Setting_btnHelp);
        btn_LogOut = (Button)findViewById(R.id.Setting_btnLogout);
        btn_Delete = (Button)findViewById(R.id.Setting_btnDel);

        btn_ViewMode_2 = (RadioButton) findViewById(R.id.rb_2);

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

        sw_AlarmNoti = (Switch)findViewById(R.id.Setting_swNoti);
        sw_AlarmFollow = (Switch)findViewById(R.id.Setting_swFollow);
        sw_AlarmLike = (Switch)findViewById(R.id.Setting_swLike);

        initSearchValue();
        initNotiValue();
        initViewValue();

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
        tv_blocklist = (TextView)findViewById(R.id.tv_blocklist);
        tv_blocklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BlockListActivity.class));
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

    private void initViewValue() {

        if(mSetting.getnViewSetting() == 0) {
            btn_ViewMode_2.setChecked(true);
        }
        else if(mSetting.getnViewSetting() == 1) {
            btn_ViewMode_3.setChecked(true);
        }
        else{
            btn_ViewMode_4.setChecked(true);
        }
    }

}
