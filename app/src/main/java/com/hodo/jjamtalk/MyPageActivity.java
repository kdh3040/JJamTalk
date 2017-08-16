package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class MyPageActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();

    ImageButton btn_Setting,btn_my_profile;
    Button btn_heart;
    ImageView img_Mypic;

    TextView txt_MyProfile;
    TextView txt_MyHeartCnt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_MyProfile = (TextView)findViewById(R.id.MyPage_txtProfile);
        txt_MyProfile.setText(mMyData.getUserNick() + "," + mMyData.getUserAge());

        txt_MyHeartCnt = (TextView)findViewById(R.id.MyPage_txtHeart);
        txt_MyHeartCnt.setText("소지하고 있는 하트 개수 : " + mMyData.getUserHeart() + " 개");

        btn_Setting = (ImageButton)findViewById(R.id.btn_setting);
        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));

            }
        });
        btn_heart = (Button)findViewById(R.id.btn_heart);
        btn_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HeartActivity.class));
            }
        });

        img_Mypic = (ImageView)findViewById(R.id.img_mypic);

        Glide.with(getApplicationContext())
                .load(mMyData.getUserImg())
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_Mypic);

        img_Mypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ClickedMyPicActivity.class));

            }
        });
        btn_my_profile = (ImageButton)findViewById(R.id.ib_my_profile);
        btn_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        txt_MyHeartCnt = (TextView)findViewById(R.id.MyPage_txtHeart);
        txt_MyHeartCnt.setText("소지하고 있는 하트 개수 : " + mMyData.getUserHeart() + " 개");
    }
}
