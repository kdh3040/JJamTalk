package com.hodo.talkking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.talkking.Data.BlockData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Util.CommonFunc;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mjk on 2017-10-01.
 */

public class MyJewelBoxActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private UIData mUIData = UIData.getInstance();

    TextView txt_myGold;

    RecyclerView rv_sell_item;
    TextView txt_price;

    Button btn_openBox;
    Button btn_sellJewely;
    Activity mActivity;

    ImageView btn_item[] = new ImageView[7];

    ImageView img_item[] = new ImageView[7];
    TextView txt_item[] = new TextView[7];
    TextView txt_itemCnt[] = new TextView[7];


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jewel_box);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity = this;

        btn_item[0] = (ImageView)findViewById(R.id.btn_item0);
        btn_item[1] = (ImageView)findViewById(R.id.btn_item1);
        btn_item[2] = (ImageView)findViewById(R.id.btn_item2);
        btn_item[3] = (ImageView)findViewById(R.id.btn_item3);
        btn_item[4] = (ImageView)findViewById(R.id.btn_item4);
        btn_item[5] = (ImageView)findViewById(R.id.btn_item5);
        btn_item[6] = (ImageView)findViewById(R.id.btn_item6);


        img_item[0] = (ImageView)findViewById(R.id.iv_coin0);
        img_item[1] = (ImageView)findViewById(R.id.iv_coin1);
        img_item[2] = (ImageView)findViewById(R.id.iv_coin2);
        img_item[3] = (ImageView)findViewById(R.id.iv_coin3);
        img_item[4] = (ImageView)findViewById(R.id.iv_coin4);
        img_item[5] = (ImageView)findViewById(R.id.iv_coin5);
        img_item[6] = (ImageView)findViewById(R.id.iv_coin6);

        txt_item[0] = (TextView) findViewById(R.id.tv_coin0);
        txt_item[1] = (TextView)findViewById(R.id.tv_coin1);
        txt_item[2] = (TextView)findViewById(R.id.tv_coin2);
        txt_item[3] = (TextView)findViewById(R.id.tv_coin3);
        txt_item[4] = (TextView)findViewById(R.id.tv_coin4);
        txt_item[5] = (TextView)findViewById(R.id.tv_coin5);
        txt_item[6] = (TextView)findViewById(R.id.tv_coin6);

        txt_itemCnt[0] = (TextView) findViewById(R.id.tv_cnt0);
        txt_itemCnt[1] = (TextView)findViewById(R.id.tv_cnt1);
        txt_itemCnt[2] = (TextView)findViewById(R.id.tv_cnt2);
        txt_itemCnt[3] = (TextView)findViewById(R.id.tv_cnt3);
        txt_itemCnt[4] = (TextView)findViewById(R.id.tv_cnt4);
        txt_itemCnt[5] = (TextView)findViewById(R.id.tv_cnt5);
        txt_itemCnt[6] = (TextView)findViewById(R.id.tv_cnt6);

        SetMyItemStatus();
/*        recyclerView = (RecyclerView)findViewById(R.id.rv_myjewel);
        Myjeweladapter = new MyPageMyJewelAdapter(getApplicationContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(Myjeweladapter);*/

/*        rv_sell_item = findViewById(R.id.rv_sell_item);
        sellAdapter = new SellItemAdapter(getApplicationContext(),this);
        rv_sell_item.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        rv_sell_item.setAdapter(sellAdapter);
        rv_sell_item.setVisibility(View.GONE);*/

        //txt_price = (TextView)findViewById(R.id.txt_sell_price);
        //txt_price.setVisibility(View.GONE);

        txt_myGold = (TextView)findViewById(R.id.tv_coin);
        txt_myGold.setText(mMyData.getUserHoney() + " 골드");

        btn_openBox = findViewById(R.id.btn_openBox);
        btn_sellJewely = findViewById(R.id.btn_sellJewely);

        mMyData.SetCurFrag(0);

        btn_openBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowOpenBox(1,0);
            }
        });
        btn_sellJewely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowOpenBox(10,1);
            }
        });

        for(int i = 0 ;i < 7; i++)
        {
            final int finalI = i;
            btn_item[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mMyData.itemList.get(finalI) == 0)
                        ShowOpenBox(1,0);
                }
            });
        }


   /*
        btn_item[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMyData.itemList.get(2) == 0)
                    ShowOpenBox(1,0);
            }
        });
        btn_item[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowOpenBox(1,0);
            }
        });
        btn_item[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowOpenBox(1,0);
            }
        });
        btn_item[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowOpenBox(1,0);
            }
        });
        btn_item[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowOpenBox(1,0);
            }
        });
        btn_item[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowOpenBox(1,0);
            }
        });*/

     /*   btn_sellJewely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUIData.bSellItemStatus == false)
                {
                   // rv_sell_item.setVisibility(View.VISIBLE);
                    txt_price.setVisibility(View.VISIBLE);
                }
                else
                {
                   // rv_sell_item.setVisibility(View.GONE);
                    txt_price.setVisibility(View.GONE);
                }

                mUIData.bSellItemStatus = !mUIData.bSellItemStatus;
            }
        });*/
    }

    public void SetMyItemStatus()
    {
        for(int i = 0; i<7; i++)
        {
            if( mMyData.itemList.get(i) != 0) {
                img_item[i].setImageResource(UIData.getInstance().getJewels()[i]);
                txt_item[i].setText(UIData.getInstance().getItems()[i]);
                txt_itemCnt[i].setVisibility(TextView.VISIBLE);
                txt_itemCnt[i].setText(mMyData.itemList.get(i).toString() + "개");
            }
            else{


                img_item[i].setImageResource(UIData.getInstance().getJewels()[i]);
                img_item[i].setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.MULTIPLY);

                txt_item[i].setText(UIData.getInstance().getItems()[i]);
                txt_itemCnt[i].setText("미 보유");
            }


        }
    }

    public void refreshHearCnt()
    {
        txt_myGold.setText("보유 골드 : " + mMyData.getUserHoney());
    }

    public void refreshItemStatus(int itemIdx, int itemCnt)
    {
        txt_myGold.setText("보유 골드 : " + mMyData.getUserHoney());
    }

    public void ShowOpenBox(int count, int bonus)
    {
        CommonFunc.ShowBoxOpen_End endlistener = new CommonFunc.ShowBoxOpen_End() {
            public void EndListener() {
                refreshHearCnt();
                SetMyItemStatus();
            }
        };

        CommonFunc.ShowBoxOpen_End buyGoldlistener = new CommonFunc.ShowBoxOpen_End() {
            public void EndListener() {
                startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
                finish();
            }
        };

        CommonFunc.getInstance().ShowBoxOpen(mActivity, count, bonus, endlistener, buyGoldlistener);
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
    }

}
