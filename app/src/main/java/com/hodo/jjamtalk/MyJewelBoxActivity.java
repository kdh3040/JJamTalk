package com.hodo.jjamtalk;

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
import com.hodo.jjamtalk.Data.BlockData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;

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



    private int Select_OpenedItem() {
        int rtValue = 0;
        int nGrade = 0;
        nGrade = (int) (Math.random()*500)+1;

        if(321 <= nGrade) rtValue = 0;

        else if(160 <= nGrade && nGrade <= 320)  rtValue = 1;
        else if(79 <= nGrade && nGrade <= 159)  rtValue = 2;
        else if(38 <= nGrade && nGrade <= 78)  rtValue = 3;
        else if(17 <= nGrade && nGrade <= 37)  rtValue = 4;
        else if(7 <= nGrade && nGrade <= 16) rtValue = 5;
        else if(2 <= nGrade && nGrade <= 6) rtValue = 6;
        else if(1 == nGrade) rtValue = 7;

        return rtValue;
    }
    private void View_OpenedItem(View v, int result, ImageView img_Opened, TextView text_Opened) {
        URL url = null;
        Bitmap bitmap = null;


        switch (result) {
            case 0: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fheel_hng.png?alt=media&token=b63df8ec-7946-455f-a7db-f6555c13b8a3");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                text_Opened.setText("명품 구두 획득!!");
                break;
            }
            case 1: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fdress_hng.png?alt=media&token=3e195e09-0fcb-4cf9-b154-9c871dac8dc5");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("명품 드레스 획득!!");
                break;
            }
            case 2: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fbag_hng.png?alt=media&token=14ce0c10-fcaa-4d7b-b196-dc56a1f86233");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("명품 가방 획득!!");
                break;
            }
            case 3: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fwatch_hng.png?alt=media&token=dbefc601-6770-48f3-a6e7-227a15ae5d36");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("명품 시계 획득!!");
                break;
            }
            case 4: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fring_hng.png?alt=media&token=2d624e62-9b42-4b44-b268-81ddc4c98ccf");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                text_Opened.setText("보석 획득!!");
                break;
            }
            case 5: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fcar_hng.png?alt=media&token=8ecd4bfc-3911-4c87-86d5-9ed055c3a864");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                text_Opened.setText("자동차 획득!!");
                break;
            }
            case 6: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fboat_hng.png?alt=media&token=5c42d065-c643-4517-8751-88a71b45d14d");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("요트 획득!!");
                break;
            }
            case 7: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fjet_hng.png?alt=media&token=0db8857d-9481-43e8-af74-45f7337deaf5");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                text_Opened.setText("제트기 획득!!");
                break;
            }
        }

        Glide.with(getApplicationContext())
                .load(url.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(img_Opened);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jewel_box);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity = this;

        recyclerView = (RecyclerView)findViewById(R.id.rv_myjewel);
        Myjeweladapter = new MyPageMyJewelAdapter(getApplicationContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setAdapter(Myjeweladapter);

/*        rv_sell_item = findViewById(R.id.rv_sell_item);
        sellAdapter = new SellItemAdapter(getApplicationContext(),this);
        rv_sell_item.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        rv_sell_item.setAdapter(sellAdapter);
        rv_sell_item.setVisibility(View.GONE);*/

        txt_price = (TextView)findViewById(R.id.txt_sell_price);
        txt_price.setVisibility(View.GONE);

        txt_myGold = (TextView)findViewById(R.id.txt_myGold);
        txt_myGold.setText(mMyData.getUserHoney() + " 골드");

        btn_openBox = findViewById(R.id.btn_openBox);
        btn_sellJewely = findViewById(R.id.btn_sellJewely);

        btn_openBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = getLayoutInflater().inflate(R.layout.dialog_exit_app,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                final AlertDialog dialog = builder.setView(v).create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                TextView tv_title = v.findViewById(R.id.title);
                tv_title.setText("상자 1개를 열까요?");
                TextView tv_msg = v.findViewById(R.id.msg);

                if(mMyData.getUserHoney() > 7){
                    tv_msg.setText("7골드가 필요합니다");
                    Button btn_yes = v.findViewById(R.id.btn_yes);
                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            if(mMyData.getUserHoney() > 7){
                                mMyData.setUserHoney(mMyData.getUserHoney() - 7);
                               // for(int i = 0 ; i< 3 ; i++)
                                {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_jewelbox_opened, null);
                                    ImageView Img_Opened = (ImageView)v.findViewById(R.id.opened_img);
                                    TextView Text_Opened = (TextView) v.findViewById(R.id.opened_text);
                                    //Button Btn_Opened = (Button)v.findViewById(R.id.opened_btn);

                                    int result = Select_OpenedItem();
                                    View_OpenedItem(v, result, Img_Opened, Text_Opened);
                                    mMyData.setMyItem(result);

                                    Button btn_confirm = v.findViewById(R.id.opened_btn);
                                    builder.setView(v);

                                    final AlertDialog dialog = builder.create();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    dialog.show();

                                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                }


                                txt_myGold.setText(mMyData.getUserHoney() + " 골드");
                                Myjeweladapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(getApplicationContext(), "골드가 부족합니다", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    btn_yes.setText("네");
                    Button btn_no = v.findViewById(R.id.btn_no);
                    btn_no.setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View view) {
                            dialog.dismiss();
                        }

                    });

                    btn_no.setText("아니오");
                }
                else
                {
                    int nGold = 7 - mMyData.getUserHoney();
                    tv_msg.setText(nGold + "골드가 부족합니다");
                    Button btn_yes = v.findViewById(R.id.btn_yes);
                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
                            finish();
                            dialog.cancel();

                        }
                    });

                    btn_yes.setText("골드 충전하기");
                    Button btn_no = v.findViewById(R.id.btn_no);
                    btn_no.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btn_no.setText("닫기");
                }
            }
        });

        btn_sellJewely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = getLayoutInflater().inflate(R.layout.dialog_exit_app,null,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                final AlertDialog dialog = builder.setView(v).create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                TextView tv_title = v.findViewById(R.id.title);
                tv_title.setText("상자 10개 + 보너스 1개 열기");
                TextView tv_msg = v.findViewById(R.id.msg);

                if(mMyData.getUserHoney() > 70){
                    tv_msg.setText("70골드가 필요합니다");
                    Button btn_yes = v.findViewById(R.id.btn_yes);
                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                                mMyData.setUserHoney(mMyData.getUserHoney() - 70);

                            for(int i = 0 ; i< 11 ; i++)
                            {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_jewelbox_opened, null);
                                    ImageView Img_Opened = (ImageView)v.findViewById(R.id.opened_img);
                                    TextView Text_Opened = (TextView) v.findViewById(R.id.opened_text);
                                    //Button Btn_Opened = (Button)v.findViewById(R.id.opened_btn);

                                    int result = Select_OpenedItem();
                                    View_OpenedItem(v, result, Img_Opened, Text_Opened);
                                    mMyData.setMyItem(result);

                                    Button btn_confirm = v.findViewById(R.id.opened_btn);
                                    builder.setView(v);

                                    final AlertDialog dialog = builder.create();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    dialog.show();

                                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                                txt_myGold.setText(mMyData.getUserHoney() + " 골드");
                                Myjeweladapter.notifyDataSetChanged();
                        }
                    });

                    btn_yes.setText("네");
                    Button btn_no = v.findViewById(R.id.btn_no);
                    btn_no.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btn_no.setText("아니오");
                }
                else
                {
                    int nGold = 70 - mMyData.getUserHoney();
                    tv_msg.setText(nGold + "골드가 부족합니다");
                    Button btn_yes = v.findViewById(R.id.btn_yes);
                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
                            finish();
                            dialog.cancel();

                        }
                    });

                    btn_yes.setText("골드 사러가기");
                    Button btn_no = v.findViewById(R.id.btn_no);
                    btn_no.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btn_no.setText("닫기");
                }
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
}
