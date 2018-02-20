package com.hodo.talkking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.talkking.Data.ChatData;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.CommonFunc;
import com.hodo.talkking.Util.LocationFunc;
import com.hodo.talkking.Util.NotiFunc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class UserPageActivity extends AppCompatActivity {
    private UserData stTargetData;
    private ImageView bg_fan;
    private ImageView ic_fan;
    private MyData mMyData = MyData.getInstance();
    private NotiFunc mNotiFunc = NotiFunc.getInstance();
    private FirebaseData mFireBase = FirebaseData.getInstance();
    private MyJewelAdapter myjewelAdapter;
    private LocationFunc mLocFunc = LocationFunc.getInstance();
    private UIData mUIdata = UIData.getInstance();

    private TextView txtProfile;
    private TextView txtMemo;

    private TextView txtDistance;

    private TextView tv_liked;

    //private TextView txtHeart;
    //private TextView txtProfile;

    //private TextView tv_like;
    private TextView[] txt_Fan = new TextView[5];

    private TextView txt_Fan_0;
    private TextView txt_Fan_1;
    private TextView txt_Fan_2;
    private TextView txt_Fan_3;
    private TextView txt_Fan_4;


    private ImageButton btnRegister;
    private ImageButton btnGiftHoney;
    //private ImageButton btnGiftJewel;

    private ImageButton btnShare;
    private ImageButton btnMessage;
   // private Button btnPublicChat;

    private ImageView imgProfile;
    private ImageView imgBestItem;
    private ImageView imgGrade;

    RecyclerView listView_like, listView_liked;
    final Context context = this;
    LinearLayout stickers_holder;
    Activity mActivity;
    private UserData TempSendUserData = new UserData();

    private SwipeRefreshLayout refreshlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        /*refreshlayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                RefreshData(refreshlayout);

//                refreshlayout.setRefreshing(false);

            }
        });*/

        mMyData.SetCurFrag(0);
        ic_fan =findViewById(R.id.ic_fan);
        bg_fan= findViewById(R.id.bg_fan);
        btnShare = (ImageButton)findViewById(R.id.UserPage_btnShared);

        myjewelAdapter = new MyJewelAdapter(getApplicationContext(),mUIdata.getJewels());
        mActivity = this;



        Intent intent = getIntent();

        final Bundle bundle = getIntent().getExtras();
        stTargetData = (UserData) bundle.getSerializable("Target");

        TempSendUserData.arrStarList = stTargetData.arrStarList;
        TempSendUserData.arrFanList = stTargetData.arrFanList;

        //getTargetfanData();

        txtProfile = (TextView) findViewById(R.id.UserPage_txtProfile);
        txtProfile.setText(stTargetData.NickName + ",  " + stTargetData.Age+"세");

        //View Divide_Memo = (View)findViewById(R.id.Divide_Memo);
        txtMemo = (TextView) findViewById(R.id.UserPage_txtMemo);
        if(stTargetData.Memo == null || stTargetData.Memo.equals(""))
        {
            //Divide_Memo.setVisibility(View.GONE);
            txtMemo.setVisibility(View.GONE);
        }
        else
            txtMemo.setText(stTargetData.Memo);

        txtDistance = (TextView) findViewById(R.id.UserPage_txtDistance);

        double Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), stTargetData.Lat, stTargetData.Lon,"kilometer");
        Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);


        if(Dist < 1.0)
            txtDistance.setText("1km");
        else
            txtDistance.setText((int)Dist + "km");

        //private TextView txtProfile;

        //tv_like = (TextView) findViewById(R.id.tv_like);
        //tv_like.setText(stTargetData.NickName+"님을 좋아하는 사람들");

        imgProfile = (ImageView)findViewById(R.id.UserPage_ImgProfile);
        //imgProfile.setLayoutParams(mUIdata.getRLP(1,0.6f));
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ImageViewPager.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Target", stTargetData);
                bundle.putSerializable("Index", 0);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        Glide.with(getApplicationContext())
                .load(stTargetData.ImgGroup0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        imgBestItem = (ImageView)findViewById(R.id.iv_item);
        imgBestItem.setVisibility(View.VISIBLE);

        if(stTargetData.BestItem == 0)
        {
            imgBestItem.setImageResource(R.mipmap.randombox);
        }
        else
        {
            imgBestItem.setImageResource(mUIdata.getJewels()[stTargetData.BestItem]);
        }

        imgGrade = (ImageView)findViewById(R.id.iv_rank);
        imgGrade.setImageResource(mUIdata.getGrades()[stTargetData.Grade]);
/*        Glide.with(getApplicationContext())
                .load(stTargetData.BestItem)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .into(imgBestItem);*/

        btnRegister = findViewById(R.id.UserPage_btnRegister);
        btnRegister.setVisibility(stTargetData.Idx.equals(mMyData.getUserIdx()) ? View.GONE : View.VISIBLE);
        btnGiftHoney =  findViewById(R.id.UserPage_btnGiftHoney);
        btnGiftHoney.setVisibility(stTargetData.Idx.equals(mMyData.getUserIdx()) ? View.GONE : View.VISIBLE);
        btnMessage =  findViewById(R.id.UserPage_btnMessage);
        btnMessage.setVisibility(stTargetData.Idx.equals(mMyData.getUserIdx()) ? View.GONE : View.VISIBLE);



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
                        buildAlertDialog(builder,"즐겨찾기에 등록할까요?", "즐겨찾기에 등록하시면"+ "\n" + "언제든 찾을 수 있죠!", "등록한다");

                        //ClickBtnSendHeart();
                        break;
          /*          case R.id.UserPage_btnGiftJewel:

                        View v = inflater.inflate(R.layout.dialog_give_jewel,null);
                       AlertDialog dialog1 = builder.setView(v).create();
                       dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                       dialog1.show();
                       Spinner sp_jewel = v.findViewById(R.id.sp_jewel);
                       sp_jewel.setAdapter(myjewelAdapter);
                        break;*/

                    case R.id.UserPage_btnShared:

                        // 공유하기 버튼
                        int aaa= 0;
                       // String subject = "회원님을 위한 특별한 이성을 발견했습니다.";
                        String text = "회원님을 위한 특별한 이성을 발견했습니다.\n톡킹에 로그인해 맘에 드는지 확인해보세요 \n" + mMyData.strDownUri;

                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                       // intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        intent.putExtra(Intent.EXTRA_TEXT, text);
                        Intent chooser = Intent.createChooser(intent, "타이틀");
                        startActivity(chooser);
                        break;

                    case R.id.UserPage_btnGiftHoney:


                        int nSize = mMyData.arrBlockedDataList.size();

                        int nBlockSize = mMyData.arrBlockDataList.size();

                        boolean bBlocked = false;
                        boolean bBlock = false;

                        for (int i = 0; i < nSize; i++) {
                            if (mMyData.arrBlockedDataList.get(i).Idx.equals(stTargetData.Idx)) {
                                bBlocked = true;
                                break;
                            }
                        }

                        for (int i = 0; i < nBlockSize; i++) {
                            if (mMyData.arrBlockDataList.get(i).Idx.equals(stTargetData.Idx)) {
                                bBlock = true;
                                break;
                            }
                        }

                        if(bBlocked == true)
                        {
                            // 블락됬습니다 표시
                            final int[] nSendHoneyCnt = new int[1];
                            nSendHoneyCnt[0] = 0;
                            View giftView = inflater.inflate(R.layout.alert_send_msg, null);
                            builder.setView(giftView);
                            final AlertDialog dialog = builder.create();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.show();

                            final TextView Msg = giftView.findViewById(R.id.textView);
                            Msg.setText("날리기 실패");

                            final EditText Edit = giftView.findViewById(R.id.et_msg);
                            Edit.setVisibility(View.GONE);

                            final TextView Body = giftView.findViewById(R.id.textView4);
                            Body.setText("당신은 차단 되었습니다");

                            final Button OK = giftView.findViewById(R.id.btn_send);
                            OK.setText("확인");
                            OK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            final Button No = giftView.findViewById(R.id.btn_cancel);
                            No.setVisibility(View.GONE);
                        }

                        else if(bBlock == true)
                        {
                            // 블락됬습니다 표시
                            final int[] nSendHoneyCnt = new int[1];
                            nSendHoneyCnt[0] = 0;
                            View giftView = inflater.inflate(R.layout.alert_send_msg, null);
                            builder.setView(giftView);
                            final AlertDialog dialog = builder.create();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.show();

                            final TextView Msg = giftView.findViewById(R.id.textView);
                            Msg.setText("날리기 실패");

                            final EditText Edit = giftView.findViewById(R.id.et_msg);
                            Edit.setVisibility(View.GONE);

                            final TextView Body = giftView.findViewById(R.id.textView4);
                            Body.setText("당신이 차단한 상대입니다");

                            final Button OK = giftView.findViewById(R.id.btn_send);
                            OK.setText("확인");
                            OK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            final Button No = giftView.findViewById(R.id.btn_cancel);
                            No.setVisibility(View.GONE);
                        }

                        else
                        {
                            final int[] nSendHoneyCnt = new int[1];
                            nSendHoneyCnt[0] = 10;
                            final View giftView = inflater.inflate(R.layout.alert_send_gift, null);
                            builder.setView(giftView);
                            final AlertDialog gold_Dialog = builder.create();
                            gold_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            gold_Dialog.show();

                            //TextView tvHeartCnt = giftView.findViewById(R.id.HeartPop_MyHeart);
                            Button btnHeartCharge = giftView.findViewById(R.id.HeartPop_Charge);
                            Button btnHeart100 = giftView.findViewById(R.id.HeartPop_10);
                            Button btnHeart200 = giftView.findViewById(R.id.HeartPop_30);
                            Button btnHeart300 = giftView.findViewById(R.id.HeartPop_50);
                            Button btnHeart500 = giftView.findViewById(R.id.HeartPop_100);
                            Button btnHeart1000 = giftView.findViewById(R.id.HeartPop_300);
                            Button btnHeart5000 = giftView.findViewById(R.id.HeartPop_500);
                            final TextView Msg = giftView.findViewById(R.id.HeartPop_text);
                            Msg.setText("현재 보유 골드는 "+String.valueOf(mMyData.getUserHoney())+"골드 입니다." );

                            //tvHeartCnt.setText("보유 골드: " + Integer.toString(mMyData.getUserHoney()));
                            //Msg.setText("100개의 꿀을 보내시겠습니까?");

                            final Button btn_gift_send = giftView.findViewById(R.id.btn_gift_send);

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
                                    if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                        int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                        btn_gift_send.setEnabled(false);
                                        Msg.setText("골드가 부족합니다. ("+String.valueOf(nPrice)+"골드 필요)" );
                                    }
                                    else {
                                        btn_gift_send.setEnabled(true);
                                        Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+ nSendHoneyCnt[0]+"골드 필요)");
                                    }

                                }
                            });

                            btnHeart200.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nSendHoneyCnt[0] = 30;
                                    if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                        int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                        btn_gift_send.setEnabled(false);
                                        Msg.setText("골드가 부족합니다. ("+String.valueOf(nPrice)+"골드 필요)" );
                                    }
                                    else {
                                        btn_gift_send.setEnabled(true);
                                        Msg.setText(nSendHoneyCnt[0] + "하트를 날리겠습니까?("+nSendHoneyCnt[0]+"골드 필요)");
                                    }

                                }
                            });

                            btnHeart300.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nSendHoneyCnt[0] = 50;
                                    if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                        int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                        btn_gift_send.setEnabled(false);
                                        Msg.setText("골드가 부족합니다. ("+String.valueOf(nPrice)+"골드 필요)" );
                                    }
                                    else {
                                        btn_gift_send.setEnabled(true);
                                        Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+nSendHoneyCnt[0]+"골드 필요)");
                                    }

                                }
                            });

                            btnHeart500.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nSendHoneyCnt[0] = 100;
                                    if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                        int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                        btn_gift_send.setEnabled(false);
                                        Msg.setText("골드가 부족합니다. ("+String.valueOf(nPrice)+"골드 필요)" );
                                    }
                                    else {
                                        btn_gift_send.setEnabled(true);
                                        Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+nSendHoneyCnt[0]+"골드 필요)");
                                    }

                                }
                            });

                            btnHeart1000.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nSendHoneyCnt[0] = 300;
                                    if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                        int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                        btn_gift_send.setEnabled(false);
                                        Msg.setText("골드가 부족합니다. ("+String.valueOf(nPrice)+"골드 필요)" );
                                    }
                                    else {
                                        btn_gift_send.setEnabled(true);
                                        Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+nSendHoneyCnt[0]+"골드 필요)");
                                    }
                                }
                            });

                            btnHeart5000.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nSendHoneyCnt[0] = 500;
                                    if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                        int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                        btn_gift_send.setEnabled(false);
                                        Msg.setText("골드가 부족합니다. ("+String.valueOf(nPrice)+"골드 필요)" );
                                    }
                                    else {
                                        btn_gift_send.setEnabled(true);
                                        Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+nSendHoneyCnt[0]+"골드 필요)");
                                    }

                                }
                            });


                            final EditText SendMsg = giftView.findViewById(R.id.HeartPop_Msg);


                            btn_gift_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(CommonFunc.getInstance().CheckTextMaxLength(SendMsg.getText().toString(), CoomonValueData.TEXT_MAX_LENGTH_SEND_HONEY, UserPageActivity.this ,"하트 날리기", true) == false)
                                        return;


                                    if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                       // Toast.makeText(getApplicationContext(), "골드가 없습니다. 표시 기능 추가 예정", Toast.LENGTH_SHORT).show();

                                    } else {
                                        String strSendMsg = SendMsg.getText().toString();

                                        boolean rtValuew = mMyData.makeSendList(stTargetData, strSendMsg.toString(),nSendHoneyCnt[0]);
                                        rtValuew = mMyData.makeCardList(stTargetData);
                                        rtValuew = mMyData.makeSendHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);
                                        rtValuew = mMyData.makeRecvHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);



                                        mMyData.setUserHoney(mMyData.getUserHoney() - nSendHoneyCnt[0]);
                                        mNotiFunc.SendHoneyToFCM(stTargetData, nSendHoneyCnt[0], strSendMsg);
                                        mMyData.setSendHoneyCnt(nSendHoneyCnt[0]);
                                        mMyData.makeFanList(stTargetData, nSendHoneyCnt[0]);
                                        // mMyData.makeStarList(stTargetData, nSendHoneyCnt[0]);
                                      //  Toast.makeText(getApplicationContext(), rtValuew + "", Toast.LENGTH_SHORT).show();

                                        Calendar cal = Calendar.getInstance();
                                        Date date = cal.getTime();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                                        String formatStr = sdf.format(date);


                                        ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(),  stTargetData.NickName, strSendMsg, formatStr, "", 0, nSendHoneyCnt[0]);


                                        final String ChatName = mMyData.getUserIdx()+"_"+stTargetData.Idx;
                                        String ChatName1 = stTargetData.Idx + "_"+ mMyData.getUserIdx();
                                        DatabaseReference mRef;

                                        if(mMyData.arrChatNameList.contains(ChatName) ) {
                                            mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(ChatName);
                                        }
                                        else     if(mMyData.arrChatNameList.contains(ChatName1) ) {
                                         mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(ChatName1);
                                        }
                                        else
                                            mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(ChatName);


                                        mRef.push().setValue(chat_Data);


                                    }


                                    gold_Dialog.dismiss();
                                    CommonFunc.getInstance().ShowDefaultPopup(UserPageActivity.this, "하트 날리기", nSendHoneyCnt[0]  + "하트를 보냈습니다.");

                                }
                            });
                            Button btn_gift_cancel = giftView.findViewById(R.id.btn_gift_cancel);
                            btn_gift_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    gold_Dialog.dismiss();

                                }
                            });

                        }

                        //ClickBtnSendHeart();
                        break;

                    case R.id.UserPage_btnMessage:

                        nSize = mMyData.arrBlockedDataList.size();
                        nBlockSize= mMyData.arrBlockDataList.size();
                        boolean bMsgBlock = false;
                        boolean bMsgBlocked = false;

                        if (stTargetData.RecvMsgReject >= 1) {
                            // 블락됬습니다 표시
                            final int[] nSendHoneyCnt = new int[1];
                            nSendHoneyCnt[0] = 0;
                            View giftView = inflater.inflate(R.layout.alert_send_msg, null);
                            builder.setView(giftView);
                            final AlertDialog dialog = builder.create();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.show();

                            final TextView Msg = giftView.findViewById(R.id.textView);
                            Msg.setText("수신 거부 알림");

                            final EditText Edit = giftView.findViewById(R.id.et_msg);
                            Edit.setVisibility(View.GONE);

                            final TextView Body = giftView.findViewById(R.id.textView4);
                            Body.setText("상대방이 쪽지 수신을" + "\n" +"거부 하였습니다");

                            final Button OK = giftView.findViewById(R.id.btn_send);
                            OK.setText("확인");
                            OK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            final Button No = giftView.findViewById(R.id.btn_cancel);
                            No.setVisibility(View.GONE);

                            break;
                        }

                        else
                        {
                            for (int i = 0; i < nSize; i++) {
                                if (mMyData.arrBlockedDataList.get(i).Idx.equals(stTargetData.Idx)) {

                                    bMsgBlocked = true;
                                    break;
                                }
                            }

                            for (int i = 0; i < nBlockSize; i++) {
                                if (mMyData.arrBlockDataList.get(i).Idx.equals(stTargetData.Idx)) {

                                    bMsgBlock = true;
                                    break;
                                }
                            }

                            if(bMsgBlocked == true)
                            {
                                // 블락됬습니다 표시
                                final int[] nSendHoneyCnt = new int[1];
                                nSendHoneyCnt[0] = 0;
                                View giftView = inflater.inflate(R.layout.alert_send_msg, null);
                                builder.setView(giftView);
                                final AlertDialog dialog = builder.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();

                                final TextView Msg = giftView.findViewById(R.id.textView);
                                Msg.setText("날리기 실패");

                                final EditText Edit = giftView.findViewById(R.id.et_msg);
                                Edit.setVisibility(View.GONE);

                                final TextView Body = giftView.findViewById(R.id.textView4);
                                Body.setText("당신은 차단 되었습니다");

                                final Button OK = giftView.findViewById(R.id.btn_send);
                                OK.setText("확인");
                                OK.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                final Button No = giftView.findViewById(R.id.btn_cancel);
                                No.setVisibility(View.GONE);
                            }

                            else   if(bMsgBlock == true)
                            {
                                // 블락됬습니다 표시
                                final int[] nSendHoneyCnt = new int[1];
                                nSendHoneyCnt[0] = 0;
                                View giftView = inflater.inflate(R.layout.alert_send_msg, null);
                                builder.setView(giftView);
                                final AlertDialog dialog = builder.create();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();

                                final TextView Msg = giftView.findViewById(R.id.textView);
                                Msg.setText("날리기 실패");

                                final EditText Edit = giftView.findViewById(R.id.et_msg);
                                Edit.setVisibility(View.GONE);

                                final TextView Body = giftView.findViewById(R.id.textView4);
                                Body.setText("당신이 차단한 상대입니다");

                                final Button OK = giftView.findViewById(R.id.btn_send);
                                OK.setText("확인");
                                OK.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                final Button No = giftView.findViewById(R.id.btn_cancel);
                                No.setVisibility(View.GONE);
                            }

                            else
                            {
                                final String ChatName = mMyData.getUserIdx()+"_"+stTargetData.Idx;
                                String ChatName1 = stTargetData.Idx + "_"+ mMyData.getUserIdx();

                                if(mMyData.arrChatNameList.contains(ChatName) )
                                {
                                    intent = new Intent(getApplicationContext(),ChatRoomActivity.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putSerializable("Target", stTargetData);
                                    bundle.putSerializable("Position", -1);
                                    bundle.putSerializable("RoomName", ChatName);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(mMyData.arrChatNameList.contains(ChatName1))
                                {
                                    intent = new Intent(getApplicationContext(),ChatRoomActivity.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putSerializable("Target", stTargetData);
                                    bundle.putSerializable("Position", -1);
                                    bundle.putSerializable("RoomName", ChatName1);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }

                                else {
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

                                            String strMemo = et_msg.getText().toString();
                                            strMemo = CommonFunc.getInstance().RemoveEmptyString(strMemo);

                                            if(CommonFunc.getInstance().CheckTextMaxLength(strMemo, CoomonValueData.TEXT_MAX_LENGTH_MAIL, UserPageActivity.this ,"쪽지 쓰기", true) == false)
                                                return;

                                            if(strMemo == null || strMemo.equals(""))
                                            {
                                                return;
                                            }

                                            mNotiFunc.SendMSGToFCM(stTargetData, strMemo);
                                            boolean rtValuew = mMyData.makeSendList(stTargetData, et_msg.getText().toString(), 0);

                                            Calendar cal = Calendar.getInstance();
                                            Date date = cal.getTime();
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                                            String formatStr = sdf.format(date);

                                            ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(),  stTargetData.NickName, strMemo, formatStr, "", 0, 0);
                                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(ChatName);
                                            mRef.push().setValue(chat_Data);


                                            msgDialog.dismiss();

                                            CommonFunc.getInstance().ShowDefaultPopup(UserPageActivity.this, "쪽지", "쪽지를 보냈습니다.");
                                        }
                                    });
                                }


                            }
                        }
                        break;
                }
            }
        };


        final GestureDetector gestureDetector = new GestureDetector(UserPageActivity.this,new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });

        btnRegister.setOnClickListener(listener);
        btnGiftHoney.setOnClickListener(listener);
        //btnGiftJewel.setOnClickListener(listener);

        btnShare.setOnClickListener(listener);

        btnMessage.setOnClickListener(listener);

        //LinearLayout layout = (LinearLayout) findViewById(R.id.ll_fan);
        //View Divide_Fan = (View)findViewById(R.id.divide_fan);

        if(stTargetData.arrFanList.size() == 0 && stTargetData.arrStarList.size() == 0 ) {
            //layout.setVisibility(View.GONE);
            //Divide_Fan.setVisibility(View.GONE);
        }

        //LinearLayout layoutFanLike = (LinearLayout) findViewById(R.id.ll_fan_like);
        //LinearLayout layoutFanLiked = (LinearLayout) findViewById(R.id.ll_fan_liked);
        listView_like = (RecyclerView) findViewById(R.id.lv_like);

        if(stTargetData.arrFanList.size() != 0)
        {
            //tv_like = findViewById(R.id.tv_like);
            //tv_like.setText(stTargetData.NickName+"님을 좋아하는 사람들");

            TargetLikeAdapter likeAdapter = new TargetLikeAdapter(getApplicationContext(), stTargetData.arrFanList);
            listView_like.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            bg_fan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserFanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Target", stTargetData);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            listView_like.setAdapter(likeAdapter);
            listView_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //  startActivity(new Intent(getApplicationContext(),FanClubActivity.class));
                    //Intent intent = new Intent(getApplicationContext(), FanClubActivity.class);
                    Intent intent = new Intent(getApplicationContext(), UserFanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Target", stTargetData);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });

            /*listView_like.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
            {

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    if(gestureDetector.onTouchEvent(e))
                    {
                        //
                        Intent intent = new Intent(getApplicationContext(), UserFanActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Target", TempSendUserData);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });
*/
        }
        else {
            listView_like.setVisibility(View.GONE);

            ic_fan.setVisibility(View.GONE);
        }


    }
/*
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
         Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F1.jpeg?alt=media&token=9f02c84b-c268-428a-bfb7-ba9c4efdbd1f")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker1);
        }
        if(stTargetData.Item_2 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.gold1);
            Img_Sticker2.setImageDrawable(myDrawable);

            Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F2.jpeg?alt=media&token=97e20f9a-671c-4800-b6a3-fcec805fdb54")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker2);
        }

        if(stTargetData.Item_3 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.pearl);
            Img_Sticker3.setImageDrawable(myDrawable);
            /*
            Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F3.jpg?alt=media&token=89c1d595-a17f-47a1-bdde-01cd0dd18089")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker3);
        }

        if(stTargetData.Item_4 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.opal);
            Img_Sticker4.setImageDrawable(myDrawable);
            /*Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F4.jpg?alt=media&token=44edade3-8d83-4726-ace2-0c001a3a1b58")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker4);
        }
        if(stTargetData.Item_5 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.emerald);
            Img_Sticker5.setImageDrawable(myDrawable);
          /*  Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F5.jpeg?alt=media&token=1d08a448-1f0a-4198-80c1-5f4ef5c226a8")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker5);
        }
        if(stTargetData.Item_6 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.sapphire);
            Img_Sticker6.setImageDrawable(myDrawable);
            /*
            Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F6.jpg?alt=media&token=41421db2-9356-4a98-8fd5-7039c45dbf68")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker6);
        }
        if(stTargetData.Item_7 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.ruby);
            Img_Sticker7.setImageDrawable(myDrawable);

           /* Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F7.jpg?alt=media&token=241f8b68-0bf4-4a5d-8bf6-cad2bf8e37da")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker7);
        }
        if(stTargetData.Item_8 != 0)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.diamond);
            Img_Sticker8.setImageDrawable(myDrawable);
            /*
            Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/jamtalk-cf526.appspot.com/o/Data%2F8.jpg?alt=media&token=76ed6a50-9ca5-4a50-a10f-14a506b063df")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Img_Sticker8);
        }

    }*/

    private void buildAlertDialog(AlertDialog.Builder builder1, String s, String s1, String s2) {


        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_exit_app,null,false);

        final AlertDialog dialog = builder1.setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.getWindow().setLayout(160, 150);
        dialog.show();
        //dialog.getWindow().setLayout(50, 50);

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
              // Toast.makeText(getApplicationContext(),rtValuew + "",Toast.LENGTH_SHORT).show();
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
                        //TempSendUserData.arrFanData.add(tempUserData);
                        TempSendUserData.mapFanData.put(tempUserData.Idx, tempUserData);

                      /*  for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.FanList.entrySet())
                            TempSendUserData.arrFanData.get(finalI).arrFanList.add(entry.getValue());*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
            }
        }
    }
/*
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

                        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.FanList.entrySet())
                            TempSendUserData.arrStarData.get(finalI).arrFanList.add(entry.getValue());

                        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet())
                            TempSendUserData.arrStarData.get(finalI).arrStarList.add(entry.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
            }
        }
    }*/

    private void RefreshData(final SwipeRefreshLayout refreshlayout) {
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
                        refreshlayout.setRefreshing(false);

                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Target", stTargetData);
                        intent.putExtras(bundle);
                        finish();
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });
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
