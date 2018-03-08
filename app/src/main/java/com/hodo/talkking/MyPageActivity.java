package com.hodo.talkking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Util.CommonFunc;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class MyPageActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    UIData mUIdata = UIData.getInstance();

    ImageView btn_Setting,btn_my_profile,btn_history;
    TextView txt_Setting,txt_profile;

    Button btn_Heart;
    //Button btn_buyjewel;
    ImageView img_Mypic;
    ImageView img_MyGrade;
    ImageButton iv_gold;
    ImageView iv_MyGift;
    Button profileButton1;
    Button settingButton;

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
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity = this;

        mMyData.SetCurFrag(0);
        profileButton1 = findViewById(R.id.cover_profile);
        profileButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
            }
        });
        settingButton = findViewById(R.id.cover_setting);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));
            }
        });
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


        iv_MyGift=findViewById(R.id.jewel);
        iv_MyGift.setVisibility(View.VISIBLE);

       /* if(mMyData.bestItem == 0)
        {
            iv_MyGift.setImageResource(R.mipmap.randombox);
        }
        else
        {
            iv_MyGift.setImageResource(mUIdata.getJewels()[mMyData.bestItem]);
        }*/



        iv_MyGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyJewelBoxActivity.class));
            }
        });


        img_MyGrade = findViewById(R.id.rank);
        img_MyGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
                View view = LayoutInflater.from(mActivity).inflate(R.layout.rank_doc,null,false);

                TextView mycoin = (TextView) view.findViewById(R.id.mycoin);
                mycoin.setText("나의 누적 코인 : " + mMyData.getPoint());
                AlertDialog dialog = builder1.setView(view).create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }
        });




        txt_MyGoldCnt = findViewById(R.id.coin);
        txt_MyProfile = (TextView)findViewById(R.id.nickname);

        int nGold = mMyData.getUserHoney();
        //txt_MyHeartCnt = (TextView)findViewById(R.id.tv_gold);
        //txt_MyHeartCnt.setText("보유 골드: " + nGold);



        btn_Heart = (Button)findViewById(R.id.coin_charge);
        btn_Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BuyGoldActivity.class));
            }
        });

        img_Mypic = (ImageView)findViewById(R.id.profile);


        img_Mypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ClickedMyPicActivity.class));


            }
        });


        /*txt_profile = findViewById(R.id.textView6);
        txt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
                finish();
            }
        });
*/
        adapter = new MyPageJewelAdapter(getApplicationContext());

        RefreshMyData();
    }

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

        RefreshMyData();
    }

    private void DrawMyGrade() {
        mMyData.SetMyGrade();

        if(mMyData.getGrade() <= 0)
            img_MyGrade.setImageResource(R.drawable.rank_bronze);
        else if(mMyData.getGrade() <= 1)
            img_MyGrade.setImageResource(R.drawable.rank_silver);
        else if(mMyData.getGrade() <= 2)
            img_MyGrade.setImageResource(R.drawable.rank_gold);
        else if(mMyData.getGrade() <= 3)
            img_MyGrade.setImageResource(R.drawable.rank_diamond);
        else if(mMyData.getGrade() <= 4)
            img_MyGrade.setImageResource(R.drawable.rank_vip);
        else if(mMyData.getGrade() <= 5)
            img_MyGrade.setImageResource(R.drawable.rank_vvip);
    }

    public void RefreshMyData()
    {
        txt_MyProfile.setText( mMyData.getUserNick());

        Glide.with(getApplicationContext())
                .load(mMyData.getUserProfileImg(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .into(img_Mypic);

        DrawMyGrade();

        iv_MyGift.setImageResource(mUIdata.getJewels()[mMyData.bestItem]);
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
        txt_MyGoldCnt.setText(nGold+"");
  /*      if(mMyData.bestItem == 0)
        {
            iv_MyGift.setImageResource(R.mipmap.randombox);
        }
        else
        {
            iv_MyGift.setImageResource(mUIdata.getJewels()[mMyData.bestItem]);
        }*/

        iv_MyGift.setImageResource(mUIdata.getJewels()[mMyData.bestItem]);

        DrawMyGrade();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //
      //  mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME);

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
