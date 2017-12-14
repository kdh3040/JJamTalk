package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mjk on 2017. 9. 18..
 */

public class BuyJewelActivity extends AppCompatActivity {
    private MyData mMyData = MyData.getInstance();
    Button btn_openbox;
    Activity mActivity;

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_jewel);
        mActivity = this;
        mContext = getApplicationContext();
        btn_openbox = (Button)findViewById(R.id.btn_openbox);


        //잔액부족일때

        btn_openbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    String alertTitle = "상자 열기";
                    new AlertDialog.Builder(mActivity)
                            .setTitle(alertTitle)
                            .setMessage("3꿀을 소비하여 상자를 여시겠습니까")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    if(mMyData.getUserHoney() > 3){
                                        mMyData.setUserHoney(mMyData.getUserHoney() - 3);
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

                                }else {

                                    Toast.makeText(getApplicationContext(), "골드가 부족합니다", Toast.LENGTH_LONG).show();

                                }

                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).show();

            }
        });



    }

    private void View_OpenedItem(View v, int result, ImageView img_Opened, TextView text_Opened) {
        URL url = null;
        Bitmap bitmap = null;


        switch (result)
        {
            case 1:
            {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F1.jpeg?alt=media&token=9f02c84b-c268-428a-bfb7-ba9c4efdbd1f");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("실버 획득!!");
                break;
            }
            case 2:
            {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F2.jpeg?alt=media&token=97e20f9a-671c-4800-b6a3-fcec805fdb54");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("골드 획득!!");
                break;
            }
            case 3:
            {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F1.jpeg?alt=media&token=9f02c84b-c268-428a-bfb7-ba9c4efdbd1f");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("진주 획득!!");
                break;
            }
            case 4:
            {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F4.jpg?alt=media&token=44edade3-8d83-4726-ace2-0c001a3a1b58");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("오펄 획득!!");
                break;
            }
            case 5:
            {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F5.jpeg?alt=media&token=1d08a448-1f0a-4198-80c1-5f4ef5c226a8");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("에메랄드 획득!!");
                break;
            }
            case 6:
            {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F6.jpg?alt=media&token=41421db2-9356-4a98-8fd5-7039c45dbf68");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("사파이어 획득!!");
                break;
            }
            case 7:
            {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F7.jpg?alt=media&token=241f8b68-0bf4-4a5d-8bf6-cad2bf8e37da");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("루비 획득!!");
                break;
            }
            case 8:
            {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F8.jpg?alt=media&token=76ed6a50-9ca5-4a50-a10f-14a506b063df");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("다이아 획득!!");
                break;
            }
        }

        img_Opened.setImageBitmap(bitmap);
    }

    private int Select_OpenedItem() {
        int rtValue = 0;
        int nGrade = 0;
        nGrade = (int) (Math.random()*500)+1;

        if(151 <= nGrade) rtValue = 1;

        else if(42 <= nGrade && nGrade <= 150)  rtValue = 2;

        else if(22 <= nGrade && nGrade <= 41)  rtValue = 3;
        else if(17 <= nGrade && nGrade <= 21)  rtValue = 4;
        else if(12 <= nGrade && nGrade <= 16)  rtValue = 5;
        else if(7 <= nGrade && nGrade <= 11) rtValue = 6;
        else if(2 <= nGrade && nGrade <= 6) rtValue = 7;
        else if(1 == nGrade) rtValue = 8;

        return rtValue;
    }
}
