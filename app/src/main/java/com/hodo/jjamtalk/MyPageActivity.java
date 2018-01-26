package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Util.CommonFunc;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.hodo.jjamtalk.Data.CoomonValueData.MAIN_ACTIVITY_HOME;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class MyPageActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    UIData mUIdata = UIData.getInstance();

    ImageButton btn_Setting,btn_my_profile,btn_history;
    Button btn_Heart;
    //Button btn_buyjewel;
    ImageView img_Mypic;
    ImageButton iv_gold;
    ImageView iv_MyGift;


    TextView txt_MyProfile;
    TextView txt_MyHeartCnt;
    TextView txt_MyGoldCnt;

    TextView txt_MySendHoney;
    TextView txt_MyRecvHoney;

    LinearLayout ll_gift_get,ll_gift_sent,ll_jewel_box;
    RecyclerView rv_myjewels;
    MyPageJewelAdapter adapter;

    private CommonFunc mCommon = CommonFunc.getInstance();

    private Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity = this;

        /*ll_jewel_box= (LinearLayout) findViewById(R.id.ll_jewel_box);
        ll_jewel_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyJewelBoxActivity.class));
            }
<<<<<<< HEAD
        });*/

        //iv_gold = findViewById(R.id.iv_gold);
        //iv_gold.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.boardBgColor), PorterDuff.Mode.MULTIPLY);

        /*iv_jewely=findViewById(R.id.iv_jewely);
        iv_jewely.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.textColorDark), PorterDuff.Mode.MULTIPLY);
*/





        iv_MyGift=findViewById(R.id.iv_myGift);

        if(mMyData.bestItem == 0)
            iv_MyGift.setImageResource(R.drawable.gold);
        else
            iv_MyGift.setImageResource(mUIdata.getJewels()[mMyData.bestItem  - 1]);

        iv_MyGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyJewelBoxActivity.class));
            }
        });



        txt_MyGoldCnt = findViewById(R.id.tv_goldsize);
        txt_MyProfile = (TextView)findViewById(R.id.MyPage_txtProfile);

        txt_MyProfile.setText( mMyData.getUserNick() + ",  " + mMyData.getUserAge()+"세");

        int nGold = mMyData.getUserHoney();
        //txt_MyHeartCnt = (TextView)findViewById(R.id.tv_gold);
        //txt_MyHeartCnt.setText("보유 골드: " + nGold);

        btn_Setting = findViewById(R.id.btn_setting);
        //btn_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.textColorDark), PorterDuff.Mode.MULTIPLY);

        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));

            }
        });
        btn_Heart = (Button)findViewById(R.id.buyHeart);
        btn_Heart.setOnClickListener(new View.OnClickListener() {
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
        btn_my_profile = findViewById(R.id.ib_my_profile);
        //btn_my_profile.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.textColorDark), PorterDuff.Mode.MULTIPLY);
        btn_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
                finish();
            }
        });

        adapter = new MyPageJewelAdapter(getApplicationContext());
        /*rv_myjewels = (RecyclerView) findViewById(R.id.rv_myjewels);
=======
        /*adapter = new MyPageJewelAdapter(getApplicationContext());
        rv_myjewels = (RecyclerView) findViewById(R.id.rv_myjewels);
>>>>>>> 1265d8791052af2aff29453ee3b9765c0694b7e2

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
        if(mMyData.bestItem == 0)
            iv_MyGift.setImageResource(R.drawable.gold);
        else
            iv_MyGift.setImageResource(mUIdata.getJewels()[mMyData.bestItem  - 1]);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //
        mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME);

 /*       Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("StartFragment", 0);
        startActivity(intent);
        overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
        finish();*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
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
}
