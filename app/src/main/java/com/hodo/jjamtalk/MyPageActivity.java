package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class MyPageActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();

    ImageButton btn_Setting,btn_my_profile,btn_history;
    Button btn_heart;
    ImageView img_Mypic;

    TextView txt_MyProfile;
    TextView txt_MyHeartCnt;

    TextView txt_MySendHoney;
    TextView txt_MyRecvHoney;

    LinearLayout ll_gift_get,ll_gift_sent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_MyProfile = (TextView)findViewById(R.id.MyPage_txtProfile);
        txt_MyProfile.setText(mMyData.getUserNick() + "," + mMyData.getUserAge());

        int nGold = mMyData.getUserHoney();
        txt_MyHeartCnt = (TextView)findViewById(R.id.MyPage_txtHeart);
        txt_MyHeartCnt.setText("현재 보유 꿀: " + nGold);

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
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
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

                //finish();
            }
        });
/*
        txt_MyRecvHoney = (TextView)findViewById(R.id.MyPage_RecvHoney);
        int nRecvCount = mMyData.getRecvHoney() * -1;
        txt_MyRecvHoney.setText("받은 꿀 : " + nRecvCount);

        ll_gift_get = (LinearLayout)findViewById(R.id.layout_gift_get_history);
        ll_gift_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HoneyGetHistoryActivity.class));


            }
        });

        txt_MySendHoney = (TextView)findViewById(R.id.MyPage_SendHoney);
        int nSendCount = mMyData.getSendHoney() * -1;
        txt_MySendHoney.setText("선물한 꿀 : " + nSendCount);

        ll_gift_sent = (LinearLayout)findViewById(R.id.layout_gift_sent_history);
        ll_gift_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HoneySentHistoryActivity.class));

            }
        });*/
    }

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
               break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onStart()
    {
        super.onStart();
        int nGold = mMyData.getUserHoney();
        txt_MyHeartCnt = (TextView)findViewById(R.id.MyPage_txtHeart);
        txt_MyHeartCnt.setText("현재 보유 꿀 : " + nGold + " 꿀");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

    }


}
