package com.hodo.talkking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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
    RecyclerView recyclerView;
    TextView txt_myGold;


    RecyclerView rv_sell_item;
    TextView txt_price;


    public  MyPageMyJewelAdapter Myjeweladapter;
 //   public  SellItemAdapter sellAdapter;
    Button btn_openBox;
    Button btn_sellJewely;
    Activity mActivity;


    private BlockData blockList;
    private int fanCount;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jewel_box);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity = this;

        recyclerView = (RecyclerView)findViewById(R.id.rv_myjewel);
        Myjeweladapter = new MyPageMyJewelAdapter(getApplicationContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        recyclerView.setAdapter(Myjeweladapter);

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

    public void refreshHearCnt()
    {
        txt_myGold.setText("보유 골드 : " + mMyData.getUserHoney());
    }

    public void ShowOpenBox(int count, int bonus)
    {
        CommonFunc.ShowBoxOpen_End endlistener = new CommonFunc.ShowBoxOpen_End() {
            public void EndListener() {
                refreshHearCnt();
                Myjeweladapter.notifyDataSetChanged();
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
