package com.hodo.talkking;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Util.CommonFunc;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by User0 on 2018-02-20.
 */

public class MyJewelBoxActivity extends Activity {

    private MyData mMyData = MyData.getInstance();
    private UIData mUIData = UIData.getInstance();

    private ImageView Img_Back;

    private ImageView Btn_Back;
    private ImageView Img_Coin;
    private TextView txt_Coin;

    private ImageView Btn_item[] = new ImageView[7];
    private ImageView Img_item[] = new ImageView[7];
    private TextView txt_item[] = new TextView[7];

    private ImageButton open_1, open_10;
    private Button buy_Coin;

    Context mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jewel_box);
        mActivity = this;
        buy_Coin = findViewById(R.id.buy_coin);
        buy_Coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
            }
        });
        Img_Back = (ImageView)findViewById(R.id.bg_topbar);

        Btn_item[0] = (ImageView)findViewById(R.id.btn_item0);
        Btn_item[1] = (ImageView)findViewById(R.id.btn_item1);
        Btn_item[2] = (ImageView)findViewById(R.id.btn_item2);
        Btn_item[3] = (ImageView)findViewById(R.id.btn_item3);
        Btn_item[4] = (ImageView)findViewById(R.id.btn_item4);
        Btn_item[5] = (ImageView)findViewById(R.id.btn_item5);
        Btn_item[6] = (ImageView)findViewById(R.id.btn_item6);


        Img_item[0] = (ImageView)findViewById(R.id.iv_coin0);
        Img_item[1] = (ImageView)findViewById(R.id.iv_coin1);
        Img_item[2] = (ImageView)findViewById(R.id.iv_coin2);
        Img_item[3] = (ImageView)findViewById(R.id.iv_coin3);
        Img_item[4] = (ImageView)findViewById(R.id.iv_coin4);
        Img_item[5] = (ImageView)findViewById(R.id.iv_coin5);
        Img_item[6] = (ImageView)findViewById(R.id.iv_coin6);


        txt_item[0] = (TextView)findViewById(R.id.tv_cnt0);
        txt_item[1] = (TextView)findViewById(R.id.tv_cnt1);
        txt_item[2] = (TextView)findViewById(R.id.tv_cnt2);
        txt_item[3] = (TextView)findViewById(R.id.tv_cnt3);
        txt_item[4] = (TextView)findViewById(R.id.tv_cnt4);
        txt_item[5] = (TextView)findViewById(R.id.tv_cnt5);
        txt_item[6] = (TextView)findViewById(R.id.tv_cnt6);

        Img_Coin = (ImageView)findViewById(R.id.iv_coin);
        txt_Coin = (TextView)findViewById(R.id.tv_coin);

        Btn_Back = (ImageView)findViewById(R.id.back);
        Btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지 확대
                onBackPressed();
            }
        });

        open_1 = (ImageButton) findViewById(R.id.btn_openBox);
        open_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지 확대
                ShowOpenBox(1,0);
            }
        });

        open_10 = (ImageButton)findViewById(R.id.btn_sellJewely);
        open_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지 확대
                ShowOpenBox(10,1);
            }
        });

        for(int i = 0 ;i < 7; i++)
        {
            final int finalI = i;
            Btn_item[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mMyData.itemList.get(finalI) == 0){
                        //ShowOpenBox(1,0);
                         }
                    else{
                        ViewItem(finalI);}
                }
            });
        }


        refreshHearCnt();
        SetMyItemStatus();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
    }

    private void refreshHearCnt() {
        Img_Back.setVisibility(View.VISIBLE);
        Btn_Back.setVisibility(View.VISIBLE);
        Img_Coin.setVisibility(View.VISIBLE);
        txt_Coin.setText(Integer.toString(mMyData.getUserHoney()));
    }

    private void SetMyItemStatus() {
        for(int i = 0; i< 7; i++)
        {
            Img_item[i].setImageResource(mUIData.getJewels()[i + 1]);
            if(mMyData.itemList.get(i + 1) != 0)
            {
                Img_item[i].setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.MULTIPLY);
                txt_item[i].setText(mMyData.itemList.get(i + 1).toString() + "개");
            }
            else
            {
                Img_item[i].setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.MULTIPLY);
                txt_item[i].setText("미 보유");

            }
        }
    }

    public void ViewItem(int Index)
    {
        CommonFunc.ShowBoxOpen_End endlistener = new CommonFunc.ShowBoxOpen_End() {
            public void EndListener() {
                refreshHearCnt();
                SetMyItemStatus();
            }
        };

        CommonFunc.getInstance().ViewBox(mActivity, Index, endlistener);
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


}

