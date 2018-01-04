package com.hodo.jjamtalk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hodo.jjamtalk.Data.MyData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mjk on 2017-10-01.
 */

public class MyJewelBoxActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    RecyclerView recyclerView;
    MyPageMyJewelAdapter adapter;
    Button btn_openBox;
    Button btn_sellJewely;
    Activity mActivity;

    private int Select_OpenedItem() {
        int rtValue = 0;
        int nGrade = 0;
        nGrade = (int) (Math.random()*500)+1;

        if(321 <= nGrade) rtValue = 1;

        else if(160 <= nGrade && nGrade <= 320)  rtValue = 2;
        else if(79 <= nGrade && nGrade <= 159)  rtValue = 3;
        else if(38 <= nGrade && nGrade <= 78)  rtValue = 4;
        else if(17 <= nGrade && nGrade <= 37)  rtValue = 5;
        else if(7 <= nGrade && nGrade <= 16) rtValue = 6;
        else if(2 <= nGrade && nGrade <= 6) rtValue = 7;
        else if(1 == nGrade) rtValue = 8;

        return rtValue;
    }
    private void View_OpenedItem(View v, int result, ImageView img_Opened, TextView text_Opened) {
        URL url = null;
        Bitmap bitmap = null;


        switch (result) {
            case 1: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fheel_hng.png?alt=media&token=b63df8ec-7946-455f-a7db-f6555c13b8a3");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("명품 구두 획득!!");
                break;
            }
            case 2: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fdress_hng.png?alt=media&token=3e195e09-0fcb-4cf9-b154-9c871dac8dc5");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("명품 드레스 획득!!");
                break;
            }
            case 3: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fbag_hng.png?alt=media&token=14ce0c10-fcaa-4d7b-b196-dc56a1f86233");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("명품 가방 획득!!");
                break;
            }
            case 4: {
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
            case 5: {
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
            case 6: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fcar_hng.png?alt=media&token=8ecd4bfc-3911-4c87-86d5-9ed055c3a864");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("자동차 획득!!");
                break;
            }
            case 7: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fboat_hng.png?alt=media&token=5c42d065-c643-4517-8751-88a71b45d14d");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("요트 획득!!");
                break;
            }
            case 8: {
                try {
                    url = new URL("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/test%2Fjet_hng.png?alt=media&token=0db8857d-9481-43e8-af74-45f7337deaf5");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                text_Opened.setText("제트기 획득!!");
                break;
            }
        }

        img_Opened.setImageBitmap(bitmap);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jewel_box);
        mActivity = this;
        recyclerView = (RecyclerView)findViewById(R.id.rv_myjewel);
        adapter = new MyPageMyJewelAdapter(getApplicationContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setAdapter(adapter);
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
                tv_title.setText("상자 열기");
                TextView tv_msg = v.findViewById(R.id.msg);
                tv_msg.setText("상자를 여시겠습니까?(3골드 필요)");

                Button btn_yes = v.findViewById(R.id.btn_yes);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
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
        });

        btn_sellJewely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
