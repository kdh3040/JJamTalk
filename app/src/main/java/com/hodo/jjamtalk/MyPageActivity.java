package com.hodo.jjamtalk;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    Button btn_Gold;
    //Button btn_buyjewel;
    ImageView img_Mypic;
    ImageView iv_gold;
    ImageView iv_jewely;


    TextView txt_MyProfile;
    TextView txt_MyHeartCnt;
    TextView txt_MyGoldCnt;

    TextView txt_MySendHoney;
    TextView txt_MyRecvHoney;

    LinearLayout ll_gift_get,ll_gift_sent,ll_jewel_box;
    RecyclerView rv_myjewels;
    MyPageJewelAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*ll_jewel_box= (LinearLayout) findViewById(R.id.ll_jewel_box);
        ll_jewel_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyJewelBoxActivity.class));
            }
        });
*/
        iv_gold = findViewById(R.id.iv_gold);
        iv_gold.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.textColorDark), PorterDuff.Mode.MULTIPLY);

        //iv_jewely=findViewById(R.id.iv_jewely);
        //iv_jewely.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.textColorDark), PorterDuff.Mode.MULTIPLY);

        txt_MyGoldCnt = findViewById(R.id.tv_goldsize);
        txt_MyProfile = (TextView)findViewById(R.id.MyPage_txtProfile);

        txt_MyProfile.setText(mMyData.getUserNick() + "," + mMyData.getUserAge());

        int nGold = mMyData.getUserHoney();
        //txt_MyHeartCnt = (TextView)findViewById(R.id.tv_gold);
        //txt_MyHeartCnt.setText("보유 골드: " + nGold);

        btn_Setting = (ImageButton)findViewById(R.id.btn_setting);
        btn_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.textColorDark), PorterDuff.Mode.MULTIPLY);

        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));

            }
        });
        btn_Gold = (Button)findViewById(R.id.btn_BuyGold);
        btn_Gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BuyGoldActivity.class));
            }
        });
        /*btn_buyjewel = (Button)findViewById(R.id.btn_buyjewel);
        btn_buyjewel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BuyJewelActivity.class));
            }
        });
*/

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
        btn_my_profile.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.textColorDark), PorterDuff.Mode.MULTIPLY);
        btn_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));

                //finish();
            }
        });
        /*adapter = new MyPageJewelAdapter(getApplicationContext());
        rv_myjewels = (RecyclerView) findViewById(R.id.rv_myjewels);

        rv_myjewels.setLayoutManager(new LinearLayoutManager(getApplicationContext(),0,false));
        rv_myjewels.setAdapter(adapter);*/

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
        //txt_MyHeartCnt = (TextView)findViewById(R.id.tv_gold);
        //txt_MyHeartCnt.setText(" 보유 골드 : " + nGold + " 골드");
        txt_MyGoldCnt.setText(nGold+" 골드");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

    }


}
