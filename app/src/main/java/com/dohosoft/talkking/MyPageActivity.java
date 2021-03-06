package com.dohosoft.talkking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
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
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Util.CommonFunc;

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
    ImageButton profileButton1;
    ImageButton settingButton;

    TextView txt_MyProfile;
    TextView txt_MyHeartCnt;
    TextView txt_MyGoldCnt;


    TextView txt_MySendHoney;
    TextView txt_MyRecvHoney;

    LinearLayout ll_gift_get,ll_gift_sent,ll_jewel_box;
    RecyclerView rv_myjewels;
    MyPageJewelAdapter adapter;

    ImageView img_Noti;
    TextView txt_noti;

    TextView img_sub;
    TextView txt_sub, txt_sub_date;

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

        img_sub = findViewById(R.id.iv_sub);
        txt_sub = findViewById(R.id.tv_sub);
        txt_sub_date = findViewById(R.id.tv_sub_date);

        if(mMyData.IsViewAds())
        {
            img_sub.setVisibility(View.GONE);
            txt_sub.setVisibility(View.GONE);
            txt_sub_date.setVisibility(View.GONE);
        }
        else
        {
            img_sub.setVisibility(View.VISIBLE);
            txt_sub.setVisibility(View.VISIBLE);
            txt_sub_date.setVisibility(View.VISIBLE);
            txt_sub_date.setText(" (" + CommonFunc.getInstance().GetRemainSubDate() + ")");
        }


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

        img_Noti = (ImageView)findViewById(R.id.iv_notice);
        txt_noti = (TextView) findViewById(R.id.tv_notice);
        img_Noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NotiListActivity.class));
            }
        });
        txt_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NotiListActivity.class));
            }
        });

        iv_MyGift=findViewById(R.id.jewel);
        iv_MyGift.setVisibility(View.VISIBLE);

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
                mycoin.setText(" "+mMyData.getPoint());
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
        txt_profile.setOnClickListener(new_img View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new_img Intent(getApplicationContext(),MyProfileActivity.class));
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

        mMyData.SetCurFrag(0);

        android.app.AlertDialog.Builder mDialog = null;
        mDialog = new android.app.AlertDialog.Builder(this);

   /*     if (!MyData.getInstance().verSion.equals(MyData.getInstance().marketVersion)) {
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

            RefreshMyData();
        }


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
                .placeholder(R.drawable.image)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .into(img_Mypic);

        DrawMyGrade();

        if(mMyData.bestItem == 0)
        {
            iv_MyGift.setVisibility(View.GONE);
        }
        else
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

       if(mMyData.bestItem == 0)
        {
            iv_MyGift.setVisibility(View.GONE);
        }
        else
            iv_MyGift.setImageResource(mUIdata.getJewels()[mMyData.bestItem]);

        DrawMyGrade();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //
      //  mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME);

 /*       Intent intent = new_img Intent(MyPageActivity.this, MainActivity.class);
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
