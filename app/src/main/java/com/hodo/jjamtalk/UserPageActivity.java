package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.LocationFunc;
import com.hodo.jjamtalk.Util.NotiFunc;

import java.util.LinkedHashMap;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class UserPageActivity extends AppCompatActivity {
    private UserData stTargetData;

    private MyData mMyData = MyData.getInstance();
    private NotiFunc mNotiFunc = NotiFunc.getInstance();
    private FirebaseData mFireBase = FirebaseData.getInstance();
    private MyJewelAdapter myjewelAdapter;
    private LocationFunc mLocFunc = LocationFunc.getInstance();

    private TextView txtProfile;
    private TextView txtMemo;

    private TextView txtDistance;

    private TextView tv_liked;

    //private TextView txtHeart;
    //private TextView txtProfile;

    private TextView tv_like;
    private TextView[] txt_Fan = new TextView[5];

    private TextView txt_Fan_0;
    private TextView txt_Fan_1;
    private TextView txt_Fan_2;
    private TextView txt_Fan_3;
    private TextView txt_Fan_4;


    private Button btnRegister;
    private Button btnGiftHoney;
    private Button btnGiftJewel;

    private Button btnMessage;
   // private Button btnPublicChat;

    private ImageView imgProfile;
    RecyclerView listView_like, listView_liked;
    final Context context = this;
    LinearLayout stickers_holder;
    UIData mUIData = UIData.getInstance();
    Activity mActivity;
    private UserData TempSendUserData = new UserData();

    private SwipeRefreshLayout refreshlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        refreshlayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                RefreshData();
                refreshlayout.setRefreshing(false);

            }
        });

        myjewelAdapter = new MyJewelAdapter(getApplicationContext(),mUIData.getJewels());
        mActivity = this;

        Intent intent = getIntent();

        Bundle bundle = getIntent().getExtras();
        stTargetData = (UserData) bundle.getSerializable("Target");

        TempSendUserData.arrStarList = stTargetData.arrStarList;
        TempSendUserData.arrFanList = stTargetData.arrFanList;
        //getTargetfanData();
        //getTargetstarData();

        txtProfile = (TextView) findViewById(R.id.UserPage_txtProfile);
        txtProfile.setText(stTargetData.NickName + ",  " + stTargetData.Age);
        txtMemo = (TextView) findViewById(R.id.UserPage_txtMemo);
        txtMemo.setText(stTargetData.Memo);

        txtDistance = (TextView) findViewById(R.id.UserPage_txtDistance);

        double Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), stTargetData.Lat, stTargetData.Lon,"kilometer");
        Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);


        if(Dist < 1.0)
            txtDistance.setText("1km 이내에 있음");
        else
            txtDistance.setText((int)Dist + "km 거리에 있음");

        //private TextView txtProfile;

        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_like.setText(stTargetData.NickName+"님을 좋아하는 사람들");

        imgProfile = (ImageView)findViewById(R.id.UserPage_ImgProfile);
        imgProfile.setLayoutParams(mUIData.getRLP(1,0.6f));
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ImageViewPager.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Target", stTargetData);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        /*stickers_holder = (LinearLayout)findViewById(R.id.stickers_holder);
        stickers_holder.setLayoutParams(mUIData.getFLP(1,0.1f));

        SetStickerImg();*/

        Glide.with(getApplicationContext())
                .load(stTargetData.Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        //txtHeart = (TextView)findViewById(R.id.UserPage_txtHeart);
        //txtHeart.setText(Integer.toString(stTargetData.Honey));


        btnRegister = (Button) findViewById(R.id.UserPage_btnRegister);
        btnGiftHoney = (Button) findViewById(R.id.UserPage_btnGiftHoney);
        btnGiftJewel = (Button)findViewById(R.id.UserPage_btnGiftJewel);


        btnMessage = (Button) findViewById(R.id.UserPage_btnMessage);
        /*btnPublicChat = (Button) findViewById(R.id.UserPage_btnPublicChat);
        btnPublicChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(stTargetData.PublicRoomStatus == 0)
                {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("공개채팅방이 입장 불가");
                    alertDialogBuilder.setMessage("공개채팅방이 개설되지 않았습니다")
                            .setCancelable(true)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {

                    mMyData.setAnotherPublicRoomList(stTargetData);
                    Intent intent = new Intent(getApplicationContext(), PublicChatRoomActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Target", stTargetData);
                    intent.putExtras(bundle);
                    startActivity(intent);


                  //  startActivity(new Intent(getApplicationContext(), PublicChatRoomActivity.class));
                }
            }
        });*/


        View.OnClickListener listener = new View.OnClickListener() {
            LayoutInflater inflater = LayoutInflater.from(mActivity);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    /*case R.id.UserPage_btnPublicChat:

                        break;*/

                    case R.id.UserPage_btnRegister:
                        buildAlertDialog(builder,"내카드에 등록", "내 카드에 등록하시겠습니까?", "등록한다!");

                        //ClickBtnSendHeart();
                        break;
                    case R.id.UserPage_btnGiftJewel:

                        View v = inflater.inflate(R.layout.dialog_give_jewel,null);


                       AlertDialog dialog1 = builder.setView(v).create();
                       dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                       dialog1.show();


                        Spinner sp_jewel = v.findViewById(R.id.sp_jewel);

                       sp_jewel.setAdapter(myjewelAdapter);







                        break;
                    case R.id.UserPage_btnGiftHoney:


                        int nSize = mMyData.arrBlockedDataList.size();

                        for (int i = 0; i < nSize; i++) {
                            if (mMyData.arrBlockedDataList.get(i).strTargetName.equals(stTargetData.Idx)) {
                                // 블락됬습니다 표시
                                final int[] nSendHoneyCnt = new int[1];
                                nSendHoneyCnt[0] = 0;
                                View giftView = inflater.inflate(R.layout.alert_send_gift, null);
                                builder.setView(giftView);
                                final AlertDialog dialog = builder.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();

                                TextView tvHeartCnt = giftView.findViewById(R.id.HeartPop_MyHeart);
                                tvHeartCnt.setText("선물 실패");
                                final TextView Msg = giftView.findViewById(R.id.HeartPop_Msg);
                                Msg.setText("당신은 차단되었습니다");

                                break;
                            }
                        }


                        final int[] nSendHoneyCnt = new int[1];
                        nSendHoneyCnt[0] = 100;
                        final View giftView = inflater.inflate(R.layout.alert_send_gift, null);
                        builder.setView(giftView);
                        final AlertDialog gold_Dialog = builder.create();
                        gold_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        gold_Dialog.show();

                        TextView tvHeartCnt = giftView.findViewById(R.id.HeartPop_MyHeart);
                        Button btnHeartCharge = giftView.findViewById(R.id.HeartPop_Charge);
                        Button btnHeart100 = giftView.findViewById(R.id.HeartPop_100);
                        Button btnHeart200 = giftView.findViewById(R.id.HeartPop_200);
                        Button btnHeart300 = giftView.findViewById(R.id.HeartPop_300);
                        Button btnHeart500 = giftView.findViewById(R.id.HeartPop_500);
                        Button btnHeart1000 = giftView.findViewById(R.id.HeartPop_1000);
                        Button btnHeart5000 = giftView.findViewById(R.id.HeartPop_5000);
                        final TextView Msg = giftView.findViewById(R.id.HeartPop_text);

                        tvHeartCnt.setText("꿀 : " + Integer.toString(mMyData.getUserHoney()) + " 개");
                        //Msg.setText("100개의 꿀을 보내시겠습니까?");


                        btnHeartCharge.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
                            }
                        });

                        btnHeart100.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 10;
                                Msg.setText(nSendHoneyCnt[0] + "골드를 보내시겠습니까?");
                            }
                        });

                        btnHeart200.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 20;
                                Msg.setText(nSendHoneyCnt[0] + "골드를 보시겠습니까?");
                            }
                        });

                        btnHeart300.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 30;
                                Msg.setText(nSendHoneyCnt[0] + "골드를 보내시겠습니까?");
                            }
                        });

                        btnHeart500.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 50;
                                Msg.setText(nSendHoneyCnt[0] + "골드를 보내시겠습니까?");
                            }
                        });

                        btnHeart1000.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 100;
                                Msg.setText(nSendHoneyCnt[0] + "골드를 보내시겠습니까?");
                            }
                        });

                        btnHeart5000.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 500;
                                Msg.setText(nSendHoneyCnt[0] + "골드를 보내시겠습니까?");
                            }
                        });


                        final EditText SendMsg = giftView.findViewById(R.id.HeartPop_Msg);

                        Button btn_gift_send = giftView.findViewById(R.id.btn_gift_send);
                        btn_gift_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                    Toast.makeText(getApplicationContext(), "골드가 없습니다. 표시 기능 추가 예정", Toast.LENGTH_SHORT).show();
                                } else {
                                    String strSendMsg = SendMsg.getText().toString();
                                    if (strSendMsg.equals(""))
                                        strSendMsg = "안녕하세요";

                                    boolean rtValuew = mMyData.makeSendList(stTargetData, strSendMsg.toString());
                                    rtValuew = mMyData.makeChatTargetList(stTargetData);
                                    rtValuew = mMyData.makeCardList(stTargetData);
                                     rtValuew = mMyData.makeSendHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);
                                    rtValuew = mMyData.makeRecvHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);



                                    if (rtValuew == true) {
                                        mNotiFunc.SendHoneyToFCM(stTargetData, nSendHoneyCnt[0]);
                                        mMyData.setSendHoneyCnt(nSendHoneyCnt[0]);
                                        mMyData.makeFanList(stTargetData, nSendHoneyCnt[0]);
                                        mMyData.makeStarList(stTargetData, nSendHoneyCnt[0]);
                                        Toast.makeText(getApplicationContext(), rtValuew + "", Toast.LENGTH_SHORT).show();
                                    }
                                }


                                gold_Dialog.dismiss();

                            }
                        });
                        Button btn_gift_cancel = giftView.findViewById(R.id.btn_gift_cancel);
                        btn_gift_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                gold_Dialog.dismiss();

                            }
                        });

                        //ClickBtnSendHeart();
                        break;

                    case R.id.UserPage_btnMessage:

                        nSize = mMyData.arrBlockedDataList.size();

                        for (int i = 0; i < nSize; i++) {
                            if (mMyData.arrBlockedDataList.get(i).strTargetName.equals(stTargetData.Idx)) {
                                // 블락됬습니다 표시
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("쪽지 전송 실패");
                                alertDialogBuilder.setMessage("차단 되었습니다")
                                        .setCancelable(true)
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

                                break;
                            }
                        }


                        if (stTargetData.RecvMsg == 1) {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("쪽지 전송 실패");
                            alertDialogBuilder.setMessage("상대방이 수신을 거부 하였습니다")
                                    .setCancelable(true)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                            break;
                        }
//쪽지 공짜
                        View view1 = inflater.inflate(R.layout.alert_send_msg, null);
                        Button btn_cancel = view1.findViewById(R.id.btn_cancel);
                        final EditText et_msg = view1.findViewById(R.id.et_msg);

                        builder.setView(view1);

                        final AlertDialog msgDialog = builder.create();
                        msgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        msgDialog.show();
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                msgDialog.dismiss();
                            }
                        });
                        Button btn_send = view1.findViewById(R.id.btn_send);
                        btn_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean rtValuew = mMyData.makeSendList(stTargetData, et_msg.getText().toString());
                                rtValuew = mMyData.makeChatTargetList(stTargetData);
                                if (rtValuew == true) {
                                    mNotiFunc.SendMSGToFCM(stTargetData);
                                    Toast.makeText(getApplicationContext(), rtValuew + "", Toast.LENGTH_SHORT).show();
                                }
                                msgDialog.dismiss();
                            }
                        });


                        /*
                        else
                        {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("하트가 부족 합니다");
                            alertDialogBuilder.setMessage("하트를 구매하시겠습니까?")
                                    .setCancelable(true)
                                    .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    })
                                    .setNegativeButton("구매한다!",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(getApplicationContext(),BuyGoldActivity.class));

                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }

                        //ClickBtnSendHeart();
                        break;*/
                }
            }
        };

        btnRegister.setOnClickListener(listener);
        btnGiftHoney.setOnClickListener(listener);
        btnGiftJewel.setOnClickListener(listener);

        btnMessage.setOnClickListener(listener);

        tv_like = findViewById(R.id.tv_like);
        tv_like.setText(stTargetData.NickName+"님을 좋아하는 사람들");
        listView_like = (RecyclerView) findViewById(R.id.lv_like);
        LikeAdapter likeAdapter = new LikeAdapter(this, stTargetData.arrFanList);
        listView_like.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        listView_like.setAdapter(likeAdapter);

        tv_liked = findViewById(R.id.tv_liked);
        tv_liked.setText(stTargetData.NickName+"님이 좋아하는 사람들");

        listView_liked = (RecyclerView) findViewById(R.id.lv_liked);
        LikedAdapter LikedAdapter = new LikedAdapter(this, stTargetData.arrStarList);
        listView_liked.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        listView_liked.setAdapter(LikedAdapter);

        LinearLayout layout = (LinearLayout) findViewById(R.id.ll_fan);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //  startActivity(new Intent(getApplicationContext(),FanClubActivity.class));

                Intent intent = new Intent(getApplicationContext(), FanClubActivity.class);
                Bundle bundle = new Bundle();


                bundle.putSerializable("Target", TempSendUserData);




             //   intent.putExtra("StarData", StarData);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        int nLayoutSize = 0;
        if (stTargetData.arrFanList.size() > stTargetData.arrStarList.size())
            nLayoutSize = stTargetData.arrFanList.size();
        else if (stTargetData.arrFanList.size() < stTargetData.arrStarList.size())
            nLayoutSize = stTargetData.arrStarList.size();
        else if (stTargetData.arrFanList.size() == stTargetData.arrStarList.size())
            nLayoutSize = stTargetData.arrStarList.size();

        if (nLayoutSize == 0) {
            layout.setVisibility(View.GONE);
        } else {
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

            //layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nLayoutSize * nLayoutSize * LinearLayout.LayoutParams.MATCH_PARENT));
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nLayoutSize * height + 80));
        }
    }

    private void SetStickerImg() {
        ImageView Img_Sticker1 = (ImageView) stickers_holder.findViewById(R.id.jw1);
        ImageView Img_Sticker2 = (ImageView) stickers_holder.findViewById(R.id.jw2);
        ImageView Img_Sticker3 = (ImageView) stickers_holder.findViewById(R.id.jw3);
        ImageView Img_Sticker4 = (ImageView) stickers_holder.findViewById(R.id.jw4);
        ImageView Img_Sticker5 = (ImageView) stickers_holder.findViewById(R.id.jw5);
        ImageView Img_Sticker6 = (ImageView) stickers_holder.findViewById(R.id.jw6);
        ImageView Img_Sticker7 = (ImageView) stickers_holder.findViewById(R.id.jw7);
        ImageView Img_Sticker8 = (ImageView) stickers_holder.findViewById(R.id.jw8);

        if(stTargetData.Item_1 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.silver);
            Img_Sticker1.setImageDrawable(myDrawable);
   /*         Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F1.jpeg?alt=media&token=9f02c84b-c268-428a-bfb7-ba9c4efdbd1f")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker1);*/
        }
        if(stTargetData.Item_2 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.gold1);
            Img_Sticker2.setImageDrawable(myDrawable);
            /*
            Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F2.jpeg?alt=media&token=97e20f9a-671c-4800-b6a3-fcec805fdb54")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker2);*/
        }

        if(stTargetData.Item_3 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.pearl);
            Img_Sticker3.setImageDrawable(myDrawable);
            /*
            Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F3.jpg?alt=media&token=89c1d595-a17f-47a1-bdde-01cd0dd18089")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker3);*/
        }

        if(stTargetData.Item_4 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.opal);
            Img_Sticker4.setImageDrawable(myDrawable);
            /*Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F4.jpg?alt=media&token=44edade3-8d83-4726-ace2-0c001a3a1b58")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker4);*/
        }
        if(stTargetData.Item_5 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.emerald);
            Img_Sticker5.setImageDrawable(myDrawable);
          /*  Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F5.jpeg?alt=media&token=1d08a448-1f0a-4198-80c1-5f4ef5c226a8")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker5);*/
        }
        if(stTargetData.Item_6 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.sapphire);
            Img_Sticker6.setImageDrawable(myDrawable);
            /*
            Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F6.jpg?alt=media&token=41421db2-9356-4a98-8fd5-7039c45dbf68")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker6);*/
        }
        if(stTargetData.Item_7 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.ruby);
            Img_Sticker7.setImageDrawable(myDrawable);

           /* Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F7.jpg?alt=media&token=241f8b68-0bf4-4a5d-8bf6-cad2bf8e37da")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker7);*/
        }
        if(stTargetData.Item_8 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.diamond);
            Img_Sticker8.setImageDrawable(myDrawable);
            /*
            Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F8.jpg?alt=media&token=76ed6a50-9ca5-4a50-a10f-14a506b063df")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker8);*/
        }

    }

    private void buildAlertDialog(AlertDialog.Builder builder1, String s, String s1, String s2) {


        View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app,null,false);

        final AlertDialog dialog = builder1.setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        final Button btn_exit;
        final Button btn_no;
        final TextView tv_title;
        final TextView tv_msg;

        tv_title = v.findViewById(R.id.title);
        tv_msg = v.findViewById(R.id.msg);

        tv_title.setText(s);
        tv_msg.setText(s1);

        btn_exit = (Button) v.findViewById(R.id.btn_yes);
        btn_exit.setText(s2);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean rtValuew = mMyData.makeCardList(stTargetData);
                Toast.makeText(getApplicationContext(),rtValuew + "",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btn_no = (Button) v.findViewById(R.id.btn_no);
        btn_no.setText("취소");
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
/*
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.MyAlertDialog);
        alertDialogBuilder.setTitle(s);
        alertDialogBuilder.setMessage(s1)
                .setCancelable(true)
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(s2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean rtValuew = mMyData.makeCardList(stTargetData);
                                Toast.makeText(getApplicationContext(),rtValuew + "",Toast.LENGTH_SHORT).show();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();*/
    }

    public void getTargetfanData() {

       // stTargetData.arrFanData.clear();

        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        //user.addChildEventListener(new ChildEventListener() {
        for (int i = 0; i < stTargetData.arrFanList.size(); i++) {
            strTargetIdx = stTargetData.arrFanList.get(i).Idx;

            if (strTargetIdx != null)
            {

                final int finalI = i;
                table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int saa = 0;
                        UserData tempUserData = dataSnapshot.getValue(UserData.class);
                        TempSendUserData.arrFanData.add(tempUserData);

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet())
                            TempSendUserData.arrFanData.get(finalI).arrFanList.add(entry.getValue());

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.StarList.entrySet())
                            TempSendUserData.arrFanData.get(finalI).arrStarList.add(entry.getValue());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
            }
        }
    }

    public void getTargetstarData() {
        stTargetData.arrStarData.clear();

        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        //user.addChildEventListener(new ChildEventListener() {
        for (int i = 0; i < stTargetData.arrStarList.size(); i++) {
            strTargetIdx = stTargetData.arrStarList.get(i).Idx;

            if (strTargetIdx != null)
            {

                final int finalI = i;
                table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int saa = 0;
                        UserData tempUserData = dataSnapshot.getValue(UserData.class);
                        TempSendUserData.arrStarData.add(tempUserData);

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet())
                            TempSendUserData.arrStarData.get(finalI).arrFanList.add(entry.getValue());

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.StarList.entrySet())
                            TempSendUserData.arrStarData.get(finalI).arrStarList.add(entry.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
            }
        }
    }

    private void RefreshData() {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("User").child(stTargetData.Idx);

       // Query query= ref.orderByChild(stTargetData.Idx);//키가 id와 같은걸 쿼리로 가져옴
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserData tempUserData = dataSnapshot.getValue(UserData.class);
                        //stTargetData.PublicRoomStatus = tempUserData.PublicRoomStatus;

                        stTargetData = tempUserData;

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });
    }

}
