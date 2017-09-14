package com.hodo.jjamtalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.hodo.jjamtalk.Util.NotiFunc;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class UserPageActivity extends AppCompatActivity {
    private UserData stTargetData;
    public ArrayList<FanData> FanList = new ArrayList<>();
    public ArrayList<UserData> FanData = new ArrayList<>();

    public ArrayList<FanData> StarList = new ArrayList<>();
    public ArrayList<UserData> StarData = new ArrayList<>();

    private MyData mMyData = MyData.getInstance();
    private NotiFunc mNotiFunc = NotiFunc.getInstance();
    private FirebaseData mFireBase = FirebaseData.getInstance();

    private TextView txtProfile;
    private TextView txtMemo;
    //private TextView txtHeart;
    //private TextView txtProfile;

    private TextView txt_FanTitle;
    private TextView[] txt_Fan = new TextView[5];

    private TextView txt_Fan_0;
    private TextView txt_Fan_1;
    private TextView txt_Fan_2;
    private TextView txt_Fan_3;
    private TextView txt_Fan_4;


    private Button btnRegister;
    private Button btnGift;

    private Button btnMessage;
    private Button btnPublicChat;

    private ImageView imgProfile;
    ListView listView, listView_Star;
    final Context context = this;
    LinearLayout stickers_holder;
    UIData mUIData = UIData.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        Intent intent = getIntent();
        //stTargetData = (UserData)intent.getExtras().getSerializable("Target");
      //  stTargetData = (UserData)intent.getParcelableExtra("Target");

        Bundle bundle = getIntent().getExtras();
        stTargetData = (UserData)bundle.getSerializable("Target");
        FanList = (ArrayList<FanData>)getIntent().getSerializableExtra("FanList");
        StarList = (ArrayList<FanData>)getIntent().getSerializableExtra("StarList");
        getTargetfanData();

       // ArrayList<Parcelable> temp= bundle.getParcelableArrayList("FanList");
      //  FanList = (ArrayList<FanData>) temp.clone();


        txtProfile = (TextView)findViewById(R.id.UserPage_txtProfile);
        txtProfile.setText(stTargetData.NickName + ",  " + stTargetData.Age);
        txtMemo = (TextView)findViewById(R.id.UserPage_txtMemo);
        txtMemo.setText(stTargetData.Memo);
        //private TextView txtProfile;

        txt_FanTitle = (TextView)findViewById(R.id.UserPage_FanTitle);


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

        stickers_holder = (LinearLayout)findViewById(R.id.stickers_holder);
        stickers_holder.setLayoutParams(mUIData.getFLP(1,0.1f));

        Glide.with(getApplicationContext())
                .load(stTargetData.Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        //txtHeart = (TextView)findViewById(R.id.UserPage_txtHeart);
        //txtHeart.setText(Integer.toString(stTargetData.Honey));


        btnRegister = (Button) findViewById(R.id.UserPage_btnRegister);
        btnGift = (Button) findViewById(R.id.UserPage_btnGift);

        btnMessage = (Button) findViewById(R.id.UserPage_btnMessage);
        btnPublicChat = (Button)findViewById(R.id.UserPage_btnPublicChat);
        btnPublicChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PublicChatRoomActivity.class));
            }
        });



        View.OnClickListener listener = new View.OnClickListener()
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.UserPage_btnPublicChat:

                        break;

                    case R.id.UserPage_btnRegister:
                        buildalertDialog("내카드에 등록", "내 카드에 등록하시겠습니까?","등록한다!");

                        //ClickBtnSendHeart();
                        break;
                    case R.id.UserPage_btnGift:

                        int nSize =mMyData.arrBlockedDataList.size();

                        for(int i = 0; i<nSize; i++) {
                            if (mMyData.arrBlockedDataList.get(i).strTargetName.equals(stTargetData.Idx)) {
                                // 블락됬습니다 표시
                                final int[] nSendHoneyCnt = new int[1];
                                nSendHoneyCnt[0] = 0;
                                View giftView = inflater.inflate(R.layout.alert_send_gift,null);
                                builder.setView(giftView);
                                final AlertDialog dialog = builder.create();
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
                                final View giftView = inflater.inflate(R.layout.alert_send_gift,null);
                                builder.setView(giftView);
                                final AlertDialog dialog = builder.create();
                                dialog.show();

                                TextView tvHeartCnt = giftView.findViewById(R.id.HeartPop_MyHeart);
                                Button btnHeartCharge =  giftView.findViewById(R.id.HeartPop_Charge);
                                Button btnHeart100 = giftView.findViewById(R.id.HeartPop_100);
                                Button btnHeart200 = giftView.findViewById(R.id.HeartPop_200);
                                Button btnHeart300 = giftView.findViewById(R.id.HeartPop_300);
                                Button btnHeart500 = giftView.findViewById(R.id.HeartPop_500);
                                Button btnHeart1000 = giftView.findViewById(R.id.HeartPop_1000);
                                Button btnHeart5000 = giftView.findViewById(R.id.HeartPop_5000);
                                final TextView Msg = giftView.findViewById(R.id.HeartPop_text);

                                tvHeartCnt.setText("꿀 : " +  Integer.toString(mMyData.getUserHoney()) + " 개");
                                Msg.setText("100개의 꿀을 보내시겠습니까?");


                                btnHeartCharge.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(getApplicationContext(),HeartActivity.class));
                                    }
                                });

                                btnHeart100.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        nSendHoneyCnt[0] = 100;
                                        Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                                    }
                                });

                                btnHeart200.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        nSendHoneyCnt[0] = 200;
                                        Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                                    }
                                });

                                btnHeart300.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        nSendHoneyCnt[0] = 300;
                                        Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                                    }
                                });

                                btnHeart500.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        nSendHoneyCnt[0] = 500;
                                        Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                                    }
                                });

                                btnHeart1000.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        nSendHoneyCnt[0] = 1000;
                                        Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                                    }
                                });

                                btnHeart5000.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        nSendHoneyCnt[0] = 5000;
                                        Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                                    }
                                });


                        final EditText SendMsg = giftView.findViewById(R.id.HeartPop_Msg);

                                Button btn_gift_send= giftView.findViewById(R.id.btn_gift_send);
                                btn_gift_send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String strSendMsg = SendMsg.getText().toString();
                                        if(strSendMsg.equals(""))
                                            strSendMsg = "꿀 보내드려요";

                                        boolean rtValuew = mMyData.makeSendHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);
                                        rtValuew = mMyData.makeRecvHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);

                                        if(rtValuew == true) {
                                            mNotiFunc.SendHoneyToFCM(stTargetData, nSendHoneyCnt[0]);
                                            mMyData.setSendHoneyCnt(nSendHoneyCnt[0]);
                                            mMyData.makeFanList(stTargetData, nSendHoneyCnt[0]);
                                            mMyData.makeStarList(stTargetData, nSendHoneyCnt[0]);
                                            Toast.makeText(getApplicationContext(), rtValuew + "", Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();

                                    }
                                });
                                Button btn_gift_cancel= giftView.findViewById(R.id.btn_gift_cancel);
                                btn_gift_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog.dismiss();

                                    }
                                });

                        //ClickBtnSendHeart();
                        break;

                    case R.id.UserPage_btnMessage:

                        nSize =mMyData.arrBlockedDataList.size();

                        for(int i = 0; i<nSize; i++) {
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


                        if(stTargetData.RecvMsg == 1) {
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
                            View view1= inflater.inflate(R.layout.alert_send_msg,null);
                            Button btn_cancel = view1.findViewById(R.id.btn_cancel);
                            final EditText et_msg = view1.findViewById(R.id.et_msg);

                            builder.setView(view1);

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });
                            Button btn_send = view1.findViewById(R.id.btn_send);
                            btn_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    boolean rtValuew = mMyData.makeSendList(stTargetData, et_msg.getText().toString());
                                    if(rtValuew == true) {
                                        mNotiFunc.SendMSGToFCM(stTargetData);
                                        Toast.makeText(getApplicationContext(), rtValuew + "", Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
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
                                                    startActivity(new Intent(getApplicationContext(),HeartActivity.class));

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
        btnGift.setOnClickListener(listener);

        btnMessage.setOnClickListener(listener);


        listView = (ListView)findViewById(R.id.lv_fan);
        UserPageFanAdapter fanAdapter = new UserPageFanAdapter(this, FanList);
        listView.setAdapter(fanAdapter);

        listView_Star = (ListView)findViewById(R.id.lv_star);
        UserPageStarAdapter StarAdapter = new UserPageStarAdapter(this, StarList);
        listView_Star.setAdapter(StarAdapter);

        LinearLayout layout = (LinearLayout) findViewById(R.id.ll_fan);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(getApplicationContext(),FanClubActivity.class));
                /*
                Intent intent = new Intent(getApplicationContext(), FanFragment.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Target", stTargetData);
                intent.putExtra("FanList", FanList);
                intent.putExtra("FanData", FanData);

                intent.putExtra("StarList", StarList);
                intent.putExtra("StarData", StarData);

                intent.putExtra("ViewMode", 1);
                intent.putExtras(bundle);
                startActivity(intent);*/

            }
        });

        int nLayoutSize = 0;
        if(FanList.size() > StarList.size())
            nLayoutSize = FanList.size();
        else if(FanList.size() < StarList.size())
            nLayoutSize = StarList.size();

        if(nLayoutSize == 0)
        {
            layout.setVisibility(View.GONE);
        }
        else
        {
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50 , getResources().getDisplayMetrics());

            //layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nLayoutSize * nLayoutSize * LinearLayout.LayoutParams.MATCH_PARENT));
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nLayoutSize * height + 50));
        }
    }

    private void buildalertDialog(String s, String s1, String s2) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
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
        alertDialog.show();
    }

    public void getTargetfanData() {
        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        //user.addChildEventListener(new ChildEventListener() {
        for (int i = 0; i < FanList.size(); i++) {
            strTargetIdx = FanList.get(i).Idx;

            if (strTargetIdx != null)
            {

                final int finalI = i;
                table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int saa = 0;
                        UserData tempUserData = dataSnapshot.getValue(UserData.class);
                        FanData.add(tempUserData);

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet())
                            FanData.get(finalI).arrFanList.add(entry.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
            }
        }
    }

}
