package com.dohosoft.talkking;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.dohosoft.talkking.Data.ChatData;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.LocationFunc;
import com.dohosoft.talkking.Util.NotiFunc;
import com.kakao.usermgmt.response.model.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.dohosoft.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;
import static com.dohosoft.talkking.Data.CoomonValueData.MOD_AddHotMember;
import static com.dohosoft.talkking.Data.CoomonValueData.SEND_MSG_COST;
import static com.dohosoft.talkking.Data.CoomonValueData.TEXTCOLOR_MAN;
import static com.dohosoft.talkking.Data.CoomonValueData.TEXTCOLOR_WOMAN;
import static com.dohosoft.talkking.MainActivity.mFragmentMng;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class UserPageActivity extends AppCompatActivity {

    private UserData stTargetData;
    private ImageView bg_fan;
    private ImageView ic_fan;
    private ImageView Divider_memo, Divider_fan;

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

    private ImageView imgAds;
    private TextView txt_Ads1;
    private TextView txt_Ads2;
    private ImageView img_sub;

    RecyclerView listView_like, listView_liked;
    final Context context = this;
    LinearLayout stickers_holder;
    Activity mActivity;
    private UserData TempSendUserData = new UserData();

    private SwipeRefreshLayout refreshlayout;
    private TargetLikeAdapter likeAdapter;

    private ImageButton btnFAB;

    private TextView txtWhen;

    public IInAppBillingService mService;
    public ServiceConnection mServiceConn;


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
            mServiceConn = null;
            mService = null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        imgAds = findViewById(R.id.UserPage_Ads);
        imgAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewSubPopup();
            }
        });

        txt_Ads1 = findViewById(R.id.tv_ad);
        txt_Ads2 = findViewById(R.id.tv_ad1);
        img_sub = findViewById(R.id.iv_sub);




        if(MyData.getInstance().IsViewAds() == true)
        {
            mMyData.mInterstitialAd = new InterstitialAd(UserPageActivity.this);
            mMyData.mInterstitialAd.setAdUnitId("ca-app-pub-4020702622451243/1718076510");
            mMyData.mInterstitialAd.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (mMyData.mInterstitialAd.isLoaded()) {
                        mMyData.mInterstitialAd.show();
                    }
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }
            });

            mMyData.mInterstitialAd.loadAd(CoomonValueData.adRequest);

        }

        /*

        if (mMyData.SubStatus == 0) {
            if (mServiceConn == null) {
                mServiceConn = new ServiceConnection() {
                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        mService = null;
                    }

                    @Override
                    public void onServiceConnected(ComponentName name,
                                                   IBinder service) {
                        mService = IInAppBillingService.Stub.asInterface(service);

                        //loadRewardedVideoAd(BuyGoldActivity.this);

                        try {
                            mMyData.skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", mMyData.querySkus);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        int response = mMyData.skuDetails.getInt("RESPONSE_CODE");
                        if (response == 0) {
                            ArrayList<String> responseList
                                    = mMyData.skuDetails.getStringArrayList("DETAILS_LIST");

                            for (String thisResponse : responseList) {
                                JSONObject object = null;
                                try {
                                    object = new JSONObject(thisResponse);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    mMyData.sku = object.getString("productId");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    mMyData.price = object.getString("price");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (mMyData.sku.equals("gold_10"))
                                    mMyData.strGold[0] = mMyData.price;
                                else if (mMyData.sku.equals("gold_20"))
                                    mMyData.strGold[1] = mMyData.price;
                                else if (mMyData.sku.equals("gold_50"))
                                    mMyData.strGold[2] = mMyData.price;
                                else if (mMyData.sku.equals("gold_100"))
                                    mMyData.strGold[3] = mMyData.price;
                                else if (mMyData.sku.equals("gold_200"))
                                    mMyData.strGold[4] = mMyData.price;
                                else if (mMyData.sku.equals("gold_500"))
                                    mMyData.strGold[5] = mMyData.price;
                                else if (mMyData.sku.equals("gold_1000"))
                                    mMyData.strGold[6] = mMyData.price;
                                else if (mMyData.sku.equals("sub_week"))
                                    mMyData.strGold[7] = mMyData.price;
                                else if (mMyData.sku.equals("sub_month"))
                                    mMyData.strGold[8] = mMyData.price;
                                else if (mMyData.sku.equals("sub_year"))
                                    mMyData.strGold[9] = mMyData.price;
                            }
                        }

                    }
                };

                Intent serviceIntent =
                        new Intent("com.android.vending.billing.InAppBillingService.BIND");
                serviceIntent.setPackage("com.android.vending");
                bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
            }

            mMyData.mInterstitialAd.loadAd(CoomonValueData.adRequest);
        }
        else
        {
            imgAds.setVisibility(View.GONE);
        }

*/

        txtWhen = findViewById(R.id.tv_when);

        mMyData.SetCurFrag(0);
        ic_fan = findViewById(R.id.ic_fan);
        bg_fan = findViewById(R.id.bg_fan);
        btnShare = (ImageButton) findViewById(R.id.UserPage_btnShared);

        myjewelAdapter = new MyJewelAdapter(getApplicationContext(), mUIdata.getJewels());
        mActivity = this;

        Divider_memo = (ImageView) findViewById(R.id.divider_memo);
        Divider_fan = (ImageView) findViewById(R.id.divider_fan);


        Intent intent = getIntent();

        final Bundle bundle = getIntent().getExtras();
        stTargetData = (UserData) bundle.getSerializable("Target");

        TempSendUserData.arrStarList = stTargetData.arrStarList;
        TempSendUserData.arrFanList = stTargetData.arrFanList;
        //getTargetfanData();

        txtWhen.setText(CommonFunc.getInstance().GetUserDate(stTargetData.ConnectDate));

        txtProfile = (TextView) findViewById(R.id.UserPage_txtProfile);
        txtProfile.setText(stTargetData.NickName + "(" + stTargetData.Age + "세)");

        if (stTargetData.Gender.equals("여자"))
            txtProfile.setTextColor(TEXTCOLOR_WOMAN);
        else
            txtProfile.setTextColor(TEXTCOLOR_MAN);

        //View Divide_Memo = (View)findViewById(R.id.Divide_Memo);
        txtMemo = (TextView) findViewById(R.id.UserPage_txtMemo);
        if (stTargetData.Memo == null || stTargetData.Memo.equals("")) {
            //Divide_Memo.setVisibility(View.GONE);
            txtMemo.setVisibility(View.GONE);
        } else
            txtMemo.setText(stTargetData.Memo);

        txtDistance = (TextView) findViewById(R.id.UserPage_txtDistance);

        double Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), stTargetData.Lat, stTargetData.Lon, "kilometer");
        Log.d("Guide !!!! ", "Case 1 : " + (int) Dist);


        if (Dist < 1.0)
            txtDistance.setText("1km");
        else
            txtDistance.setText((int) Dist + "km");

        //private TextView txtProfile;

        //tv_like = (TextView) findViewById(R.id.tv_like);
        //tv_like.setText(stTargetData.NickName+"님을 좋아하는 사람들");

        imgProfile = (ImageView) findViewById(R.id.UserPage_ImgProfile);
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

        CommonFunc.getInstance().ShowLoadingPage(UserPageActivity.this, "로딩중");
        Glide.with(getApplicationContext())
                .load(stTargetData.ImgGroup0)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                        CommonFunc.getInstance().DismissLoadingPage();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                        CommonFunc.getInstance().DismissLoadingPage();
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        imgBestItem = (ImageView) findViewById(R.id.iv_item);
        imgBestItem.setVisibility(View.VISIBLE);

        if (stTargetData.BestItem == 0) {
            imgBestItem.setVisibility(View.GONE);
        } else
            imgBestItem.setImageResource(mUIdata.getJewels()[stTargetData.BestItem]);


       /* imgBestItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunc.getInstance().ViewUserBox(mActivity, stTargetData.BestItem);
            }
        });*/


        imgGrade = (ImageView) findViewById(R.id.iv_fan);
        imgGrade.setImageResource(mUIdata.getGrades()[stTargetData.Grade]);
/*        Glide.with(getApplicationContext())
                .load(stTargetData.BestItem)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new_img CropCircleTransformation(getApplicationContext()))
                .into(imgBestItem);*/

/*
if(mMyData.itemList.get(i) != 0)
            {
                Img_item[i].setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.MULTIPLY);
                txt_item[i].setText(mMyData.itemList.get(i).toString() + "개");
            }
            else
            {
                Img_item[i].setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.MULTIPLY);
                txt_item[i].setText("미 보유");

            }
 */
        btnRegister = findViewById(R.id.UserPage_btnRegister);
        btnRegister.setImageResource(mMyData.IsCardList(stTargetData.Idx) ? R.drawable.favor_pressed : R.drawable.favor);
        //btnRegister.setVisibility(stTargetData.Idx.equals(mMyData.getUserIdx()) ? View.GONE : View.VISIBLE);
        btnGiftHoney = findViewById(R.id.UserPage_btnGiftHoney);
        //btnGiftHoney.setVisibility(stTargetData.Idx.equals(mMyData.getUserIdx()) ? View.GONE : View.VISIBLE);
        btnMessage = findViewById(R.id.UserPage_btnMessage);


        //btnMessage.setVisibility(stTargetData.Idx.equals(mMyData.getUserIdx()) ? View.GONE : View.VISIBLE);



        /*btnPublicChat = (Button) findViewById(R.id.UserPage_btnPublicChat);
        btnPublicChat.setOnClickListener(new_img View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(stTargetData.PublicRoomStatus == 0)
                {
                    final AlertDialog.Builder alertDialogBuilder = new_img AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("공개채팅방이 입장 불가");
                    alertDialogBuilder.setMessage("공개채팅방이 개설되지 않았습니다")
                            .setCancelable(true)
                            .setPositiveButton("확인", new_img DialogInterface.OnClickListener() {
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
                    Intent intent = new_img Intent(getApplicationContext(), PublicChatRoomActivity.class);
                    Bundle bundle = new_img Bundle();
                    bundle.putSerializable("Target", stTargetData);
                    intent.putExtras(bundle);
                    startActivity(intent);


                  //  startActivity(new_img Intent(getApplicationContext(), PublicChatRoomActivity.class));
                }
            }
        });*/


        View.OnClickListener listener = new View.OnClickListener() {
            LayoutInflater inflater = LayoutInflater.from(mActivity);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    /*case R.id.UserPage_btnPublicChat:

                        break;*/

                    case R.id.UserPage_btnRegister:

                        if (CoomonValueData.MOD_AddHotMember == true) {
                            CommonFunc.getInstance().AddHotMember(stTargetData);
                            CommonFunc.getInstance().ShowToast(UserPageActivity.this, "핫멤버로 등록 하였습니다.", true);
                        } else {
                            if (mMyData.IsCardList(stTargetData.Idx) == false) {
                                CommonFunc.getInstance().ShowToast(UserPageActivity.this, "즐겨찾기에 등록 하였습니다.", true);
                                mMyData.makeCardList(stTargetData);
                            } else {
                                CommonFunc.getInstance().ShowToast(UserPageActivity.this, "즐겨찾기를 취소 하였습니다.", true);
                                mMyData.removeCardList(stTargetData);
                            }


                            Fragment frg = null;
                            frg = mFragmentMng.findFragmentByTag("CardListFragment");
                            if (frg != null) {
                                final FragmentTransaction ft = mFragmentMng.beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commitAllowingStateLoss();
                            }


                            btnRegister.setImageResource(mMyData.IsCardList(stTargetData.Idx) ? R.drawable.favor_pressed : R.drawable.favor);

                        }

                        //ClickBtnSendHeart();
                        break;
          /*          case R.id.UserPage_btnGiftJewel:

                        View v = inflater.inflate(R.layout.dialog_give_jewel,null);
                       AlertDialog dialog1 = builder.setView(v).create();
                       dialog1.getWindow().setBackgroundDrawable(new_img ColorDrawable(android.graphics.Color.TRANSPARENT));
                       dialog1.show();
                       Spinner sp_jewel = v.findViewById(R.id.sp_jewel);
                       sp_jewel.setAdapter(myjewelAdapter);
                        break;*/

                    case R.id.UserPage_btnShared:

                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                        if (MOD_AddHotMember == true) {
                            CommonFunc.getInstance().RemoveHotMember(stTargetData);
                            CommonFunc.getInstance().ShowToast(UserPageActivity.this, "핫멤버에서 삭제 하였습니다.", true);
                        } else {
                            int aaa = 0;
                            // String subject = "회원님을 위한 특별한 이성을 발견했습니다.";
                            String text = "회원님을 위한 특별한 이성을 발견했습니다.\n톡킹에 로그인해 맘에 드는지 확인해보세요 \n" + CoomonValueData.getInstance().DownUrl;


                            intent.setType("text/plain");
                            // intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                            intent.putExtra(Intent.EXTRA_TEXT, text);
                            Intent chooser = Intent.createChooser(intent, "타이틀");
                            startActivity(chooser);
                        }
                        // 공유하기 버튼

                        break;

                    case R.id.UserPage_btnGiftHoney:

                        CommonFunc.getInstance().ShowLoadingPage(UserPageActivity.this, "로딩중");

                        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

                        Query data;
                        if (mMyData.getUserGender().equals("여자")) {
                            data = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman").child(mMyData.getUserIdx()).child("Honey");
                        } else {
                            data = FirebaseDatabase.getInstance().getReference().child("Users").child("Man").child(mMyData.getUserIdx()).child("Honey");
                        }

                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                CommonFunc.getInstance().DismissLoadingPage();
                                int tempValue = dataSnapshot.getValue(int.class);
                                mMyData.setUserHoney(tempValue);

                                CommonFunc.HeartGiftPopup_Change_End changeListener = new CommonFunc.HeartGiftPopup_Change_End() {
                                    @Override
                                    public void EndListener() {
                                        startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
                                    }
                                };
                                CommonFunc.HeartGiftPopup_Send_End sendListener = new CommonFunc.HeartGiftPopup_Send_End() {
                                    @Override
                                    public void EndListener(int heartCount, String msg) {
                                        //mMyData.makeSendList(stTargetData, msg, heartCount);
                                        mMyData.makeCardList(stTargetData);
                                        mMyData.makeSendHoneyList(stTargetData, heartCount, msg);
                                        mMyData.makeRecvHoneyList(stTargetData, heartCount, msg);
                                        mMyData.setUserHoney(mMyData.getUserHoney() - heartCount);
                                        mMyData.setSendHoneyCnt(heartCount);
                                        mMyData.makeFanList(mActivity, stTargetData, heartCount, msg);

                                        RefreshFanData();
                                        likeAdapter.notifyDataSetChanged();

                                    }
                                };
                                CommonFunc.getInstance().HeartGiftPopup(UserPageActivity.this, stTargetData.Idx, changeListener, sendListener);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        //ClickBtnSendHeart();
                        break;

                    case R.id.UserPage_btnMessage:

                        int nSize = mMyData.arrBlockedDataList.size();
                        int nBlockSize = mMyData.arrBlockDataList.size();
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

                            final EditText Edit = giftView.findViewById(R.id.et_nick);
                            Edit.setVisibility(View.GONE);

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

                            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                            ConstraintLayout ll = (ConstraintLayout) giftView.findViewById(R.id.constraintLayout);
                            ll.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    imm.hideSoftInputFromWindow(Edit.getWindowToken(), 0);
                                }
                            });

                            break;
                        } else {


                            if (CommonFunc.getInstance().ShowBlockUser(UserPageActivity.this, stTargetData.Idx) == false) {
                                final String ChatName = mMyData.getUserIdx() + "_" + stTargetData.Idx;
                                String ChatName1 = stTargetData.Idx + "_" + mMyData.getUserIdx();

                                if (mMyData.arrChatNameList.contains(ChatName)) {
                                    intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putSerializable("Target", stTargetData);
                                    bundle.putSerializable("Position", -1);
                                    bundle.putSerializable("RoomName", ChatName);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    //finish();
                                } else if (mMyData.arrChatNameList.contains(ChatName1)) {
                                    intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putSerializable("Target", stTargetData);
                                    bundle.putSerializable("Position", -1);
                                    bundle.putSerializable("RoomName", ChatName1);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    //finish();
                                } else {


                                    CommonFunc.getInstance().ShowLoadingPage(UserPageActivity.this, "로딩중");

                                    fierBaseDataInstance = FirebaseDatabase.getInstance();

                                    if (mMyData.getUserGender().equals("여자")) {
                                        data = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman").child(mMyData.getUserIdx()).child("Honey");
                                    } else {
                                        data = FirebaseDatabase.getInstance().getReference().child("Users").child("Man").child(mMyData.getUserIdx()).child("Honey");
                                    }


                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            CommonFunc.getInstance().DismissLoadingPage();
                                            int tempValue = dataSnapshot.getValue(int.class);
                                            mMyData.setUserHoney(tempValue);

                                            View view1;

                                            view1 = inflater.inflate(R.layout.alert_send_msg_male, null);

                                            TextView CoinText = (TextView) view1.findViewById(R.id.coinText);
                                            ImageView CoinImg = (ImageView) view1.findViewById(R.id.iv_Coin);
                                            TextView CoinMine = (TextView) view1.findViewById(R.id.tv_myCoin);
                                            TextView Coin = (TextView) view1.findViewById(R.id.ex);

                                            Button CoinCharge = (Button) view1.findViewById(R.id.HeartPop_Charge);



                                            if (mMyData.getUserGender().equals("여자") && stTargetData.Gender.equals("남자")) {
                                                CoinText.setVisibility(View.GONE);
                                                CoinImg.setVisibility(View.GONE);
                                                CoinMine.setVisibility(View.GONE);
                                                Coin.setVisibility(View.GONE);
                                                CoinCharge.setVisibility(View.GONE);
                                            } else {
                                                CoinText.setVisibility(View.VISIBLE);
                                                CoinImg.setVisibility(View.VISIBLE);
                                                CoinMine.setVisibility(View.VISIBLE);
                                                Coin.setVisibility(View.VISIBLE);
                                                CoinCharge.setVisibility(View.VISIBLE);

                                                CoinMine.setText(Integer.toString(mMyData.getUserHoney()));
                                            }


                                            if(mMyData.IsViewAds() == false )
                                            {
                                                Coin.setVisibility(View.GONE);
                                                CoinMine.setVisibility(View.GONE);
                                            }
                                            else
                                            {
                                                Coin.setVisibility(View.VISIBLE);
                                                CoinMine.setVisibility(View.VISIBLE);
                                            }

                                            Button btn_cancel = view1.findViewById(R.id.btn_cancel);
                                            final EditText et_msg = view1.findViewById(R.id.et_nick);
                                            Button btn_send = view1.findViewById(R.id.btn_send);
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

                                            CoinCharge.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
                                                    msgDialog.dismiss();
                                                }
                                            });


                                            btn_send.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    if(mMyData.IsViewAds() == false )
                                                    {
                                                        String strMemo = et_msg.getText().toString();
                                                        strMemo = CommonFunc.getInstance().RemoveEmptyString(strMemo);

                                                        if (CommonFunc.getInstance().CheckTextMaxLength(strMemo, CoomonValueData.TEXT_MAX_LENGTH_MAIL, UserPageActivity.this, "쪽지 쓰기", true) == false)
                                                            return;

                                                        if (strMemo == null || strMemo.equals("")) {
                                                            return;
                                                        }

                                                        mNotiFunc.SendMSGToFCM(stTargetData, strMemo);
                                                        boolean rtValuew = mMyData.makeSendList(stTargetData, et_msg.getText().toString(), 0);
                                                        long nowTime = CommonFunc.getInstance().GetCurrentTime();
                                                        ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), stTargetData.NickName, strMemo, nowTime, "", 0, 0);
                                                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(ChatName);
                                                        mRef.push().setValue(chat_Data);

                                                        CommonFunc.getInstance().ShowToast(UserPageActivity.this, "쪽지를 보냈습니다.", true);
                                                    }
                                                    else
                                                    {
                                                        if (mMyData.getUserHoney() < SEND_MSG_COST) {
                                                            CommonFunc.getInstance().ShowToast(UserPageActivity.this, "코인이 부족합니다", true);
                                                        } else {
                                                            String strMemo = et_msg.getText().toString();
                                                            strMemo = CommonFunc.getInstance().RemoveEmptyString(strMemo);

                                                            if (CommonFunc.getInstance().CheckTextMaxLength(strMemo, CoomonValueData.TEXT_MAX_LENGTH_MAIL, UserPageActivity.this, "쪽지 쓰기", true) == false)
                                                                return;

                                                            if (strMemo == null || strMemo.equals("")) {
                                                                return;
                                                            }

                                                            mNotiFunc.SendMSGToFCM(stTargetData, strMemo);
                                                            mMyData.setUserHoney(mMyData.getUserHoney() - SEND_MSG_COST);

                                                            boolean rtValuew = mMyData.makeSendList(stTargetData, et_msg.getText().toString(), 0);

                                                            long nowTime = CommonFunc.getInstance().GetCurrentTime();
                                                            ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), stTargetData.NickName, strMemo, nowTime, "", 0, 0);
                                                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(ChatName);
                                                            mRef.push().setValue(chat_Data);

                                                            CommonFunc.getInstance().ShowToast(UserPageActivity.this, "쪽지를 보냈습니다.", true);
                                                        }
                                                    }


                                                    msgDialog.dismiss();
                                                }
                                            });

                                            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                                            ConstraintLayout ll = (ConstraintLayout) view1.findViewById(R.id.constraintLayout);
                                            ll.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    imm.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                        }
                        break;
                }
            }
        };


        final GestureDetector gestureDetector = new GestureDetector(UserPageActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
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

        if (stTargetData.arrFanList.size() == 0 && stTargetData.arrStarList.size() == 0) {
            //layout.setVisibility(View.GONE);
            //Divide_Fan.setVisibility(View.GONE);
        }

        //LinearLayout layoutFanLike = (LinearLayout) findViewById(R.id.ll_fan_like);
        //LinearLayout layoutFanLiked = (LinearLayout) findViewById(R.id.ll_fan_liked);
        listView_like = (RecyclerView) findViewById(R.id.lv_like);


        if (stTargetData.arrFanList.size() != 0) {
            //tv_like = findViewById(R.id.tv_like);
            //tv_like.setText(stTargetData.NickName+"님을 좋아하는 사람들");

            likeAdapter = new TargetLikeAdapter(getApplicationContext(), stTargetData);
            listView_like.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
                    //  startActivity(new_img Intent(getApplicationContext(),FanClubActivity.class));
                    //Intent intent = new_img Intent(getApplicationContext(), FanClubActivity.class);
                    Intent intent = new Intent(getApplicationContext(), UserFanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Target", stTargetData);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });

            /*listView_like.addOnItemTouchListener(new_img RecyclerView.OnItemTouchListener()
            {

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    if(gestureDetector.onTouchEvent(e))
                    {
                        //
                        Intent intent = new_img Intent(getApplicationContext(), UserFanActivity.class);
                        Bundle bundle = new_img Bundle();
                        bundle.putSerializable("Target", TempSendUserData);
                        intent.putExtras(bundle);ㅎ
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
        } else {
            Divider_memo.setVisibility(View.GONE);
            Divider_fan.setVisibility(View.GONE);

            listView_like.setVisibility(View.GONE);
            bg_fan.setVisibility(View.GONE);
            ic_fan.setVisibility(View.GONE);
        }


        btnFAB = (ImageButton) findViewById(R.id.btnFAB);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFunc.getInstance().GoMainActivity(mActivity, MAIN_ACTIVITY_HOME, 0, 0);
            }
        });
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


    public void getTargetfanData() {

        // stTargetData.arrFanData.clear();

        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        //user.addChildEventListener(new_img ChildEventListener() {
        for (int i = 0; i < stTargetData.arrFanList.size(); i++) {
            strTargetIdx = stTargetData.arrFanList.get(i).Idx;

            if (strTargetIdx != null) {

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

        //user.addChildEventListener(new_img ChildEventListener() {
        for (int i = 0; i < stTargetData.arrStarList.size(); i++) {
            strTargetIdx = stTargetData.arrStarList.get(i).Idx;

            if (strTargetIdx != null)
            {

                final int finalI = i;
                table.child(strTargetIdx).addListenerForSingleValueEvent(new_img ValueEventListener() {
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

    private void RefreshFanData() {
        ic_fan.setVisibility(View.VISIBLE);
        listView_like.setVisibility(View.VISIBLE);
        bg_fan.setVisibility(View.VISIBLE);

        likeAdapter = new TargetLikeAdapter(getApplicationContext(), stTargetData);
        listView_like.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
                //  startActivity(new_img Intent(getApplicationContext(),FanClubActivity.class));
                //Intent intent = new_img Intent(getApplicationContext(), FanClubActivity.class);
                Intent intent = new Intent(getApplicationContext(), UserFanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Target", stTargetData);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

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

        if (mMyData.SubStatus == 0) {
            if (mServiceConn == null) {
                mServiceConn = new ServiceConnection() {
                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        mService = null;
                    }

                    @Override
                    public void onServiceConnected(ComponentName name,
                                                   IBinder service) {
                        mService = IInAppBillingService.Stub.asInterface(service);

                        //loadRewardedVideoAd(BuyGoldActivity.this);

                        try {
                            mMyData.skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", mMyData.querySkus);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        int response = mMyData.skuDetails.getInt("RESPONSE_CODE");
                        if (response == 0) {
                            ArrayList<String> responseList
                                    = mMyData.skuDetails.getStringArrayList("DETAILS_LIST");

                            for (String thisResponse : responseList) {
                                JSONObject object = null;
                                try {
                                    object = new JSONObject(thisResponse);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    mMyData.sku = object.getString("productId");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    mMyData.price = object.getString("price");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (mMyData.sku.equals("gold_10"))
                                    mMyData.strGold[0] = mMyData.price;
                                else if (mMyData.sku.equals("gold_20"))
                                    mMyData.strGold[1] = mMyData.price;
                                else if (mMyData.sku.equals("gold_50"))
                                    mMyData.strGold[2] = mMyData.price;
                                else if (mMyData.sku.equals("gold_100"))
                                    mMyData.strGold[3] = mMyData.price;
                                else if (mMyData.sku.equals("gold_200"))
                                    mMyData.strGold[4] = mMyData.price;
                                else if (mMyData.sku.equals("gold_500"))
                                    mMyData.strGold[5] = mMyData.price;
                                else if (mMyData.sku.equals("gold_1000"))
                                    mMyData.strGold[6] = mMyData.price;
                                else if (mMyData.sku.equals("sub_week"))
                                    mMyData.strGold[7] = mMyData.price;
                                else if (mMyData.sku.equals("sub_month"))
                                    mMyData.strGold[8] = mMyData.price;
                                else if (mMyData.sku.equals("sub_year"))
                                    mMyData.strGold[9] = mMyData.price;
                            }
                        }

                    }
                };

                Intent serviceIntent =
                        new Intent("com.android.vending.billing.InAppBillingService.BIND");
                serviceIntent.setPackage("com.android.vending");
                bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
            }
            imgAds.setVisibility(View.VISIBLE);
            img_sub.setVisibility(View.VISIBLE);
            txt_Ads2.setVisibility(View.VISIBLE);
            txt_Ads1.setVisibility(View.VISIBLE);
        }
        else
        {
            imgAds.setVisibility(View.GONE);
            img_sub.setVisibility(View.GONE);
            txt_Ads2.setVisibility(View.GONE);
            txt_Ads1.setVisibility(View.GONE);
        }

        android.app.AlertDialog.Builder mDialog = null;
        mDialog = new android.app.AlertDialog.Builder(this);

        {
            if (CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
                mMyData.badgecount = pref.getInt("Badge", 1);

                if (mMyData.badgecount >= 1) {
                    mMyData.badgecount = 0;
                    Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                    intent.putExtra("badge_count_package_name", "com.dohosoft.talkking");
                    intent.putExtra("badge_count_class_name", "com.dohosoft.talkking.LoginActivity");
                    intent.putExtra("badge_count", mMyData.badgecount);
                    sendBroadcast(intent);
                }
            }
        }


        mMyData.SetCurFrag(0);
    }

    void ViewSubPopup() {
        View v = LayoutInflater.from(UserPageActivity.this).inflate(R.layout.popup_sub, null, false);

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        final Button btn_week;
        final Button btn_month;
        final Button btn_year;

        btn_week = (Button) v.findViewById(R.id.btn_1900);
        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(UserPageActivity.this, mMyData.skuGold[7]);
                dialog.dismiss();
            }
        });

        btn_month = (Button) v.findViewById(R.id.btn_4900);
        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(UserPageActivity.this, mMyData.skuGold[8]);
                dialog.dismiss();
            }
        });

        btn_year = (Button) v.findViewById(R.id.btn_49000);
        btn_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(UserPageActivity.this, mMyData.skuGold[9]);
                dialog.dismiss();
            }
        });

    }


    private void BuyGoldByGoogle(Context context, String tempStrGold) {
        try {
            if (mService == null) {
                CommonFunc.getInstance().ShowToast(context, "결제에 실패하였습니다. 다시 시도해주세요.", true);
                return;
            }
            mMyData.buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                    tempStrGold, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (mMyData.buyIntentBundle == null) {
            CommonFunc.getInstance().ShowToast(context, "결제에 실패하였습니다. 다시 시도해주세요.", true);
            return;
        }

        mMyData.pendingIntent = mMyData.buyIntentBundle.getParcelable("BUY_INTENT");
        if (mMyData.pendingIntent != null) {
            try {
                startIntentSenderForResult(mMyData.pendingIntent.getIntentSender(),
                        1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0));
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    final String strToken = jo.getString("purchaseToken");
                    FirebaseData.getInstance().SetSubStatus(UserPageActivity.this, sku);
                    setBuyGold(sku);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int response = mService.consumePurchase(3, getPackageName(), strToken);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void setBuyGold(String ID) {
        int nPrice = 0;

        if (ID.equals(mMyData.skuGold[7])) {
            nPrice = CoomonValueData.SUBSTATUS_WEEK_COIN;
        }
        else if (ID.equals(mMyData.skuGold[8])) {
            nPrice = CoomonValueData.SUBSTATUS_MONTH_COIN;
        }
        else if (ID.equals(mMyData.skuGold[9])) {
            nPrice = CoomonValueData.SUBSTATUS_YEAR_COIN;
        }

        CommonFunc.getInstance().ShowLoadingPage(UserPageActivity.this, "로딩중");

        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

        Query data;
        if (mMyData.getUserGender().equals("여자")) {
            data = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman").child(mMyData.getUserIdx()).child("Honey");
        } else {
            data = FirebaseDatabase.getInstance().getReference().child("Users").child("Man").child(mMyData.getUserIdx()).child("Honey");
        }

        final int finalNPrice = nPrice;
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CommonFunc.getInstance().DismissLoadingPage();
                int tempValue = dataSnapshot.getValue(int.class);
                mMyData.setUserHoney(tempValue);
                CommonFunc.getInstance().ShowDefaultPopup(UserPageActivity.this, "보상", finalNPrice + "코인을 획득 하였습니다.");
                mMyData.setUserHoney(mMyData.getUserHoney() + finalNPrice);
                mMyData.setPoint(finalNPrice);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
