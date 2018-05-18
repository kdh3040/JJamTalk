package com.dohosoft.talkking;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.dohosoft.talkking.Data.UserData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.dohosoft.talkking.Data.BoardData;
import com.dohosoft.talkking.Data.BoardMsgDBData;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.FanData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SettingData;
import com.dohosoft.talkking.Data.SimpleChatData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.AppStatus;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.LocationFunc;
import com.unity3d.ads.UnityAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.dohosoft.talkking.Data.CoomonValueData.FIRST_LOAD_MAIN_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;
import static com.dohosoft.talkking.Data.CoomonValueData.OFFAPP;
import static com.dohosoft.talkking.Data.CoomonValueData.REF_LAT;
import static com.dohosoft.talkking.Data.CoomonValueData.REF_LON;
import static com.dohosoft.talkking.Data.CoomonValueData.bMyLoc;
import static com.dohosoft.talkking.Data.CoomonValueData.bMySet;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetNear;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetNew;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetRecv;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetRich;

public class MainActivity extends AppCompatActivity {


    boolean RefreshTest = false;



    ImageView ib_home;
    ImageView ib_cardList;
    ImageView ib_chatList;
    ImageView ib_fan;
    ImageView ib_board;

    ImageView iv_myPage;
    TextView txt_title;

    TextView txt_home;
    TextView txt_cardList;
    TextView txt_chatList;
    TextView txt_fan;
    TextView txt_board;

    ImageView iv_heartCnt;
    TextView txt_heartCnt;

    ImageView iv_fanCnt;
    TextView txt_fanCnt;

    //ImageButton ib_pcr_open;
    ImageButton ib_buy_jewel;

    ImageView iv_refresh;

    ImageView logo;

    TextView tv_MainTitle;
    LinearLayout layout_lowbar, layout_topbar;
    BoardFragment boardFragment;

    ImageButton RefreshBtn;

    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private UIData mUIData = UIData.getInstance();
    private SettingData mSetting = SettingData.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    ArrayList<Class> arrFragment = new ArrayList<>();
    private CardListFragment cardListFragment;
    private ChatListFragment chatListFragment;
    private FanListFragment fanFragment;
    private HomeFragment homeFragment;// = HomeFragment.getInstance();

    public static Context mContext;
    public static Activity mActivity;
    public static android.support.v4.app.FragmentManager mFragmentMng;

    public int nStartFragment = 0;
    public int nStartByNoti = 0;
    public int nStartByNew = 0;

    // 눌렀을때의 폰트 색상
    private int nEnableFontColor = Color.BLACK;
    // 안눌러졌을때의 폰트 색상
    private int nDisableFontColor = Color.GRAY;

    private FirebaseAnalytics mFirebaseAnalytics;

    boolean bAlarmChat = false;

    public interface  CallBack{
        void callback();
    }

    CallBack mCallback = new CallBack() {
        @Override
        public void callback() {
            mFragmentMng.beginTransaction().replace(R.id.frag_container, chatListFragment, "ChatListFragment").commit();
        }
    };

    public class Prepare extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mMyData.mServiceConn == null) {
                mMyData.mServiceConn = new ServiceConnection() {
                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        mMyData.mService = null;
                    }

                    @Override
                    public void onServiceConnected(ComponentName name,
                                                   IBinder service) {
                        mMyData.mService = IInAppBillingService.Stub.asInterface(service);

                        try {
                            mMyData.skuDetails = mMyData.mService.getSkuDetails(3, getPackageName(), "inapp", mMyData.querySkus);
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
                            }
                        }

                    }
                };

                Intent serviceIntent =
                        new Intent("com.android.vending.billing.InAppBillingService.BIND");
                serviceIntent.setPackage("com.android.vending");
                bindService(serviceIntent, mMyData.mServiceConn, Context.BIND_AUTO_CREATE);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    public class SortDataAge extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            mMyData.arrUserAll_Near_Age = mMyData.SortData_Age(mMyData.arrUserAll_Near, mMyData.nStartAge, mMyData.nEndAge);
            mMyData.arrUserAll_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Recv, mMyData.nStartAge, mMyData.nEndAge);
            mMyData.arrUserAll_New_Age = mMyData.SortData_Age(mMyData.arrUserAll_New, mMyData.nStartAge, mMyData.nEndAge);
            mMyData.arrUserAll_Send_Age = mMyData.SortData_Age(mMyData.arrUserAll_Send, mMyData.nStartAge, mMyData.nEndAge);

            return 0;
        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mCommon.GoMainActivity(mActivity, MAIN_ACTIVITY_HOME, 0, 0);
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }

    }

 /*   @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMyData.mService != null) {
            unbindService(mMyData.mServiceConn);
        }
    }*/


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        android.app.AlertDialog.Builder mDialog = null;
        mDialog = new android.app.AlertDialog.Builder(this);

  /*      if (!mMyData.verSion.equals(mMyData.marketVersion)) {
            mDialog.setMessage("업데이트 후 사용해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("업데이트 바로가기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent marketLaunch = new Intent(
                                            Intent.ACTION_VIEW);
                                    marketLaunch.setData(Uri
                                            //.parse("https://play.google.com/store/apps/details?id=패키지명 적으세요"));
                                            .parse("https://play.google.com/store/apps/details?id=com.dohosoft.talkking"));

                                    startActivity(marketLaunch);
                                    System.exit(0);
                                }
                            });
            android.app.AlertDialog alert = mDialog.create();
            alert.setTitle("안 내");
            alert.show();
        }

        else*/
        {

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancelAll();

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

            int nTempNoti = 0;
            int nTempStartFrag = 0;
            SharedPreferences NotiPref = getApplicationContext().getSharedPreferences("ExecByNoti", getApplicationContext().MODE_PRIVATE);
            nTempStartFrag = NotiPref.getInt("StartFrag", 0);
            nTempNoti = NotiPref.getInt("ExecByNoti", 0);

            if (mMyData.getUserIdx() == null) {
                CommonFunc.getInstance().restartApp(getApplicationContext());
            }

            if (CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND) {
                if (nTempNoti == 1) {
                    SetButtonColor(nTempStartFrag);
                    SetFontColor(nTempStartFrag);

                    logo.setVisibility(View.GONE);
                    mMyData.SetCurFrag(2);

                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, chatListFragment, "ChatListFragment").commit();

                    CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
                    setImageAlpha(100, 100, 255, 100, 100);
                    iv_myPage.setVisibility(View.GONE);
                    txt_title.setVisibility(TextView.VISIBLE);
                    txt_title.setText("채팅 목록");
                }
            } else if (CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

       /*     SetButtonColor(nTempStartFrag);
            SetFontColor(nTempStartFrag);

            logo.setVisibility(View.GONE);
            mMyData.SetCurFrag(2);


            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, chatListFragment, "ChatListFragment").commit();

            CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
            setImageAlpha(100, 100, 255, 100, 100);
            iv_myPage.setVisibility(View.GONE);
            txt_title.setVisibility(TextView.VISIBLE);
            txt_title.setText("채팅 목록");*/


                if (mMyData.GetCurFrag() == 2) {
                    Fragment frg = null;
                    frg = mFragmentMng.findFragmentByTag("ChatListFragment");
                    FragmentTransaction ft = mFragmentMng.beginTransaction();
                    ft.detach(frg);
                    ft.attach(frg);
                    ft.commit();
                }


            }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        mActivity = this;
        mContext = getApplicationContext();


        SharedPreferences MyDataPref = mContext.getSharedPreferences("MyData", mContext.MODE_PRIVATE);
        SharedPreferences.Editor MyDataEditor = MyDataPref.edit();
        MyDataEditor.putString("Gender",  mMyData.getUserGender());
        MyDataEditor.commit();


        mFragmentMng = getSupportFragmentManager();


        Bundle bundle = getIntent().getExtras();
        nStartFragment = (int) bundle.getSerializable("StartFragment");
        nStartByNoti = (int) bundle.getSerializable("Noti");
        nStartByNew = (int) bundle.getSerializable("New");

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
/*
        for(int i = 0; i < 755; i++)
        {
            double  lon = Math.random() * (1.072271) + 126.611512;

         //   Log.d("123123123", "   " + lon);
            double  lat = Math.random() * (0.836468) + 37.077091;
         //  Log.d("123123123", "   " + lat);

            mMyData.setUserDist(LocationFunc.getInstance().getDistance(lat,lon, REF_LAT, REF_LON,"meter"));
            Log.d("123123123", "   " + mMyData.getUserDist());

        }*/

        // 구독 상태
        CommonFunc.getInstance().CheckSubStatus();


        RefreshBtn = (ImageButton)findViewById(R.id.RefreshBtn);

        RefreshBtn.setVisibility(View.GONE);

        if(RefreshTest == true)
        {
            RefreshBtn.setVisibility(View.VISIBLE);
            RefreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseData.getInstance().RefreshUserData(mActivity);
                }
            });
        }



        SharedPreferences pref = getSharedPreferences("ExecByNoti", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        OFFAPP = false;

        if (mMyData.getUserIdx() == null) {
            CommonFunc.getInstance().restartApp(getApplicationContext());
        }

/*        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle Analyticsbundle = new_img Bundle();
        Analyticsbundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        Analyticsbundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Name");
        Analyticsbundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, Analyticsbundle);*/

        //AddDummy(32);

        mMyData.mContext = getApplicationContext();
        mMyData.mMainActivity = mActivity;

        if (mMyData.arrReportList.size() >= 10)
            ViewReportPop();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PrefSetting", getApplicationContext().MODE_PRIVATE);
        editor = prefs.edit();
        editor.putBoolean("Sound", SettingData.getInstance().IsAlarmSettingSound());
        editor.putBoolean("Vibe", SettingData.getInstance().IsAlarmSettingVibration());
        editor.commit();


        pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
        mMyData.badgecount = pref.getInt("Badge", 0);


        //if ( mMyData.badgecount >= 1)
        {
            mMyData.badgecount = 0;
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count_package_name", "com.dohosoft.talkking");
            intent.putExtra("badge_count_class_name", "com.dohosoft.talkking.LoginActivity");
            intent.putExtra("badge_count", mMyData.badgecount);
            sendBroadcast(intent);
        }

        iv_myPage = findViewById(R.id.iv_mypage);
        txt_title = findViewById(R.id.txt_title);

        logo = findViewById(R.id.iv_logo);

        /*Glide.with(getApplicationContext())
                .load(R.drawable.mypage)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_myPage);
*/

       /* Glide.with(getApplicationContext())
                .load(mMyData.getUserImg())
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new_img CropCircleTransformation(getApplicationContext()))
                .into(iv_myPage);*/

        iv_myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyPageActivity.class));
            }
        });

        CommonFunc.getInstance().Card_Alarm = (ImageView) findViewById(R.id.alarm_favor);
        CommonFunc.getInstance().Chat_Alarm = (ImageView) findViewById(R.id.alarm_chat);
        CommonFunc.getInstance().Fan_Alarm = (ImageView) findViewById(R.id.alarm_fan);
        CommonFunc.getInstance().Mail_Alarm = (ImageView) findViewById(R.id.alarm_mail);


        for (int i = 0; i < mMyData.arrChatNameList.size(); i++) {
            if (!mMyData.arrChatDataList.get(mMyData.arrChatNameList.get(i)).WriterIdx.equals(mMyData.getUserIdx())) {
                if (mMyData.arrChatDataList.get(mMyData.arrChatNameList.get(i)).Check == 0) {
                    bAlarmChat = true;
                    break;
                }
            }
        }

        if(bAlarmChat == true)
        {
            CommonFunc.getInstance().SetChatAlarmVisible(true);
        }
        else
        {
            CommonFunc.getInstance().SetChatAlarmVisible(false);
        }



        CommonFunc.getInstance().Item_Box = (ImageView) findViewById(R.id.iv_itemBox);
        CommonFunc.getInstance().Item_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyJewelBoxActivity.class));
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
            }
        });

        final boolean[] bThreadRun = {false};
        CommonFunc.getInstance().Honey_Box = (ImageView) findViewById(R.id.iv_honeybox);
        CommonFunc.getInstance().SetMailAlarmVisible(false);
        CommonFunc.getInstance().Honey_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunc.getInstance().SetMailAlarmVisible(false);
                startActivity(new Intent(getApplicationContext(), MailboxActivity.class));
            }
        });

        CommonFunc.getInstance().Board_Write = (ImageView) findViewById(R.id.iv_boardwrite);
        CommonFunc.getInstance().Board_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonFunc.getInstance().IsCurrentDateCompare(new Date(mMyData.GetLastBoardWriteTime()), CoomonValueData.BOARD_WRITE_TIME_MIN, 0) == false) {
                    String Desc = CommonFunc.getInstance().GetRemainTimeByFuture(new Date(mMyData.GetLastBoardWriteTime() + (CoomonValueData.BOARD_WRITE_TIME_MIN * CoomonValueData.MIN_MILLI_SECONDS)), true);
                    CommonFunc.getInstance().ShowDefaultPopup(MainActivity.this, "게시판 작성", "도배방지를 위해 " + Desc + " 후에 글을 쓰실 수 있습니다.");
                } else
                    startActivity(new Intent(getApplicationContext(), BoardWriteActivity.class));
            }
        });
        CommonFunc.getInstance().MyBoard_Write = (Button) findViewById(R.id.iv_myboardwrite);
        CommonFunc.getInstance().MyBoard_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BoardMyListActivity.class));

            }
        });

        CommonFunc.getInstance().Filter = findViewById(R.id.ib_filter);
        CommonFunc.getInstance().Filter.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.boardBgColor), PorterDuff.Mode.MULTIPLY);

        CommonFunc.getInstance().Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  startActivity(new_img Intent(getApplicationContext(),MainSettingActivity.class));
                //  overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View v = LayoutInflater.from(mActivity).inflate(R.layout.category_popup, null, false);

                builder.setView(v);
                final AlertDialog filter_dialog = builder.create();

                filter_dialog.show();

                final AdView mAdView;

                final RadioButton rbtn_three;
                final RadioButton rbtn_four;

                final RadioButton cbox_man;
                final RadioButton cbox_woman;

                final Spinner spin_StartAge, spin_EndAge;

                mAdView = (AdView) v.findViewById(R.id.adView);
                CommonFunc.getInstance().ViewAdsBanner(mAdView);



                spin_StartAge = (Spinner) v.findViewById(R.id.spinner1);
                spin_StartAge.setSelection(mMyData.nStartAge - 20);
                // spin_StartAge.setPrompt(String.valueOf(mMyData.nStartAge));
                spin_StartAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        mMyData.nStartAge = position + 20;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spin_EndAge = (Spinner) v.findViewById(R.id.spinner2);
                spin_EndAge.setSelection(mMyData.nEndAge - 20);
                // spin_EndAge.setPrompt(String.valueOf(mMyData.nEndAge));
                spin_EndAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        mMyData.nEndAge = position + 20;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                Switch rbtn_10;
                Switch rbtn_20;
                Switch rbtn_30;
                Switch rbtn_40;

                Button btn_ok = v.findViewById(R.id.btn_save);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        mMyData.setAlarmSettingData(mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration(), mSetting.IsAlarmSettingPop());
                        mMyData.setSettingData(getApplicationContext(), mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting());
                        mFireBaseData.SaveSettingData(mMyData.getUserIdx(), mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration(), mSetting.IsAlarmSettingPop());
                        filter_dialog.dismiss();

                        FirebaseData.getInstance().RefreshUserData(mActivity);
                        //SortDataAge sortData = new SortDataAge();
                        // sortData.execute();

               /*         Intent intent = new_img Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("StartFragment", 0);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);*/

                    }
                });


                Button btn_cancel = v.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filter_dialog.dismiss();
                    }
                });


                //rbtn_three = (RadioButton) v.findViewById(R.id.rbtn_three);
                //rbtn_four = (RadioButton) v.findViewById(R.id.rbtn_four);

                cbox_man = (RadioButton) v.findViewById(R.id.rbtn_man);
                cbox_woman = (RadioButton) v.findViewById(R.id.rbtn_woman);

                switch (mMyData.nSearchMode) {
                    case 0:
                        if (mMyData.getUserGender().equals("여자")) {
                            cbox_man.setChecked(true);
                            cbox_woman.setChecked(false);
                        } else {
                            cbox_man.setChecked(false);
                            cbox_woman.setChecked(true);
                        }
                        break;
                    case 3:
                        cbox_man.setChecked(true);
                        cbox_woman.setChecked(true);
                        break;
                    case 2:
                        cbox_man.setChecked(false);
                        cbox_woman.setChecked(true);
                        break;
                    case 1:
                        cbox_man.setChecked(true);
                        cbox_woman.setChecked(false);
                        break;
                }

                if (mSetting.getnViewSetting() == 0) {
                    //rbtn_three.setChecked(false);
                    //rbtn_four.setChecked(false);
                } else if (mSetting.getnViewSetting() == 1) {
                    //rbtn_three.setChecked(true);
                    //rbtn_four.setChecked(false);
                } else if (mSetting.getnViewSetting() == 2) {
                   // rbtn_three.setChecked(false);
                   // rbtn_four.setChecked(true);
                }


                /*rbtn_three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rbtn_three.isChecked()) {
                            rbtn_three.setChecked(true);
                            rbtn_four.setChecked(false);
                            mSetting.setnViewSetting(1);
                        }
                    }
                });


                rbtn_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rbtn_four.isChecked()) {
                            rbtn_three.setChecked(false);
                            rbtn_four.setChecked(true);
                            mSetting.setnViewSetting(2);
                        }

                    }
                });*/

                cbox_man.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cbox_man.isChecked()) {
                            cbox_woman.setChecked(false);
                            cbox_man.setChecked(true);
                            mSetting.setnSearchSetting(1);
                        }
                    }
                });

                cbox_woman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cbox_man.isChecked()) {
                            cbox_woman.setChecked(true);
                            cbox_man.setChecked(false);
                            mSetting.setnSearchSetting(2);
                        }
                    }
                });



                /*rbtn_10 = (Switch) v.findViewById(R.id.rbtn_10);
                rbtn_20 = (Switch) v.findViewById(R.id.rbtn_20);
                rbtn_30 = (Switch) v.findViewById(R.id.rbtn_30);
                rbtn_40 = (Switch) v.findViewById(R.id.rbtn_40);

                switch (mMyData.nStartAge / 10)
                {
                    case 1:
                        rbtn_10.setChecked(true);
                        rbtn_20.setChecked(false);
                        rbtn_30.setChecked(false);
                        rbtn_40.setChecked(false);
                        break;
                    case 2:
                        rbtn_10.setChecked(false);
                        rbtn_20.setChecked(true);
                        rbtn_30.setChecked(false);
                        rbtn_40.setChecked(false);
                        break;
                    case 3:
                        rbtn_10.setChecked(false);
                        rbtn_20.setChecked(false);
                        rbtn_30.setChecked(true);
                        rbtn_40.setChecked(false);
                        break;
                    case 4:
                        rbtn_10.setChecked(false);
                        rbtn_20.setChecked(false);
                        rbtn_30.setChecked(false);
                        rbtn_40.setChecked(true);
                        break;
                }*/


                /*RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rbtn_three.isChecked())
                            mSetting.setnViewSetting(1);
                        else if (rbtn_four.isChecked())
                            mSetting.setnViewSetting(2);
                    }
                };
*/

            }
        });


        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        mUIData.setHeight(height);
        mUIData.setWidth(width);
        mUIData.setLlp_ListItem(new LinearLayout.LayoutParams(width, height / 7));

        //int mHeight = mUIData.getHeight();
        int mWidth = mUIData.getWidth();
        int mHeight = mUIData.getHeight();


        mMyData.skuList.add("gold_10");
        mMyData.skuList.add("gold_20");
        mMyData.skuList.add("gold_50");
        mMyData.skuList.add("gold_100");
        mMyData.skuList.add("gold_200");
        mMyData.skuList.add("gold_300");
        mMyData.skuList.add("gold_500");
        mMyData.skuList.add("gold_1000");

        mMyData.querySkus.putStringArrayList("ITEM_ID_LIST", mMyData.skuList);

/*        mMyData.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
        //mMyData.mRewardedVideoAd.loadAd("ca-app-pub-4020702622451243/5818242350",
        mMyData.mRewardedVideoAd.loadAd("ca-app-pub-4020702622451243/3514842138",
                new AdRequest.Builder().build());*/

      /*  Prepare prepareJob = new_img Prepare();

        prepareJob.execute();*/


       // boolean bCheckConnt = mMyData.CheckConnectDate();

         /*if (CommonFunc.getInstance().IsCurrentDateCompare(new Date(-1 * mMyData.ConnectDate), CoomonValueData.DAILY_CONNECT_CHECK, 0)) {

            mMyData.ConnectDate = -1 * CommonFunc.getInstance().GetCurrentTime();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table;

            if (mMyData.getUserGender().equals("여자")) {
                table = database.getReference("Users").child("Woman").child(mMyData.getUserIdx());
            } else {
                table = database.getReference("Users").child("Man").child(mMyData.getUserIdx());
            }

            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("ConnectDate", mMyData.ConnectDate);
            table.updateChildren(updateMap);

            table = database.getReference("SimpleData").child(mMyData.getUserIdx());
            table.updateChildren(updateMap);

            String alertTitle = "종료";
            View ConnV = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app, null, false);

            final AlertDialog ConnDialog = new AlertDialog.Builder(this).setView(ConnV).create();
            ConnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ConnDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            ConnDialog.show();

            final TextView txt_Title;
            txt_Title = (TextView) ConnV.findViewById(R.id.title);
            txt_Title.setText("출석 체크 보상");
            final TextView txt_Body;
            txt_Body = (TextView) ConnV.findViewById(R.id.msg);
            txt_Body.setText(CoomonValueData.getInstance().Login + "\n" + mUIData.getAdReward()[mMyData.getGrade()] + "코인 획득 하였습니다");
            // txt_Body.setText("톡킹을 다운로드 해주셔서 감사합니다" + "\n" + "여러분들의 외로움을 해결해드리기 위해 "+ "\n" + "최선을 다하겠습니다");


            final Button btn_exit;
            final Button btn_no;

            btn_exit = (Button) ConnV.findViewById(R.id.btn_yes);
            btn_exit.setText("확인");
            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnDialog.dismiss();
                }
            });

            btn_no = (Button) ConnV.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnDialog.dismiss();
                }
            });
            btn_no.setVisibility(View.GONE);


            mMyData.setUserHoney(mMyData.getUserHoney() + mUIData.getAdReward()[mMyData.getGrade()]);
            mMyData.setPoint(mUIData.getAdReward()[mMyData.getGrade()]);

            if (CoomonValueData.getInstance().Notice != null && !CoomonValueData.getInstance().Notice.equals("")) {
                View NoticeV = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app, null, false);

                final AlertDialog NoticeDialog = new AlertDialog.Builder(this).setView(NoticeV).create();
                NoticeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                NoticeDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                NoticeDialog.show();

                final TextView txt_NoticeTitle;
                txt_NoticeTitle = (TextView) NoticeV.findViewById(R.id.title);
                txt_NoticeTitle.setText("알림");
                final TextView txt_NoticeBody;
                txt_NoticeBody = (TextView) NoticeV.findViewById(R.id.msg);
                txt_NoticeBody.setText(CoomonValueData.getInstance().Notice);

                final Button btn_NoticeExit;
                final Button btn_NoticeNo;

                btn_NoticeExit = (Button) NoticeV.findViewById(R.id.btn_yes);
                btn_NoticeExit.setText("확인");
                btn_NoticeExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NoticeDialog.dismiss();
                    }
                });

                btn_NoticeNo = (Button) NoticeV.findViewById(R.id.btn_no);
                btn_NoticeNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NoticeDialog.dismiss();
                    }
                });
                btn_NoticeNo.setVisibility(View.GONE);
                NoticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface var1) {

                    }

                });
            }

        }*/

        if (nStartByNew == 1) {

            View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app, null, false);

            final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            dialog.show();

            final TextView txt_Title;
            txt_Title = (TextView) v.findViewById(R.id.title);
            txt_Title.setText("신규 가입");
            final TextView txt_Body;
            txt_Body = (TextView) v.findViewById(R.id.msg);
            txt_Body.setText(CoomonValueData.getInstance().SignUp);

            final Button btn_exit;
            final Button btn_no;

            btn_exit = (Button) v.findViewById(R.id.btn_yes);
            btn_exit.setText("확인");
            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btn_no = (Button) v.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btn_no.setVisibility(View.GONE);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface var1) {

                }

            });

            nStartByNew = 0;
        }


        txt_home = findViewById(R.id.tv_home);
        txt_cardList = findViewById(R.id.tv_favor);
        txt_chatList = findViewById(R.id.tv_chat);
        txt_fan = findViewById(R.id.tv_fan);
        txt_board = findViewById(R.id.tv_board);

        ib_home = findViewById(R.id.ib_home);


        iv_heartCnt = findViewById(R.id.iv_heart_cnt);
        iv_heartCnt.setVisibility(View.GONE);

        txt_heartCnt = findViewById(R.id.tv_heart_cnt);
        txt_heartCnt.setVisibility(View.GONE);

        iv_fanCnt = findViewById(R.id.iv_fan_cnt);
        iv_fanCnt.setVisibility(View.GONE);

        txt_fanCnt = findViewById(R.id.tv_fan_cnt);
        txt_fanCnt.setVisibility(View.GONE);

        //ib_home.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

        View.OnClickListener homeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_myPage.setVisibility(View.VISIBLE);
                logo.setVisibility(View.VISIBLE);
                txt_title.setVisibility(TextView.GONE);


                iv_heartCnt.setVisibility(View.GONE);
                txt_heartCnt.setVisibility(View.GONE);
                iv_fanCnt.setVisibility(View.GONE);
                txt_fanCnt.setVisibility(View.GONE);


                mMyData.SetCurFrag(0);
                //getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, homeFragment, "HomeFragment").commit();
                //ib_home.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

                setImageAlpha(255, 100, 100, 100, 100);
                SetButtonColor(0);
                SetFontColor(0);
                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.HOME_ACTIVITY);
                ib_home.setSelected(!v.isSelected());
                txt_home.setSelected(!v.isSelected());
            }
        };
        ib_home.setOnClickListener(homeListener);
        txt_home.setOnClickListener(homeListener);

        homeFragment = new HomeFragment();
        LoadFanData();
        boardFragment = new BoardFragment();
        LoadCardData();
        LoadChatData();

        View.OnClickListener boardListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_myPage.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("게시판");
                mMyData.SetCurFrag(4);

                iv_heartCnt.setVisibility(View.GONE);
                txt_heartCnt.setVisibility(View.GONE);
                iv_fanCnt.setVisibility(View.GONE);
                txt_fanCnt.setVisibility(View.GONE);

                if (boardFragment == null)
                    boardFragment = new BoardFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, boardFragment, "BoardFragment").commit();

                ib_board.setSelected(!v.isSelected());
                txt_board.setSelected(!v.isSelected());
                setImageAlpha(100, 100, 100, 100, 255);
                SetButtonColor(4);
                SetFontColor(4);
                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.BOARD_ACTIVITY);
            }
        };

        ib_board = findViewById(R.id.ib_board);
        ib_board.setOnClickListener(boardListener);
        txt_board.setOnClickListener(boardListener);

        View.OnClickListener cardListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrepareCard InitCard = new PrepareCard();
                InitCard.execute(0, 0, 0);

               /* getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, cardListFragment, "CardListFragment").commit();
                ib_cardList.setSelected(!v.isSelected());
                txt_cardList.setSelected(!v.isSelected());

                setImageAlpha(100, 255, 100, 100, 100);
                SetButtonColor(1);
                SetFontColor(1);
                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);*/
            }
        };

        ib_cardList = findViewById(R.id.ib_cardlist);
        ib_cardList.setOnClickListener(cardListener);
        txt_cardList.setOnClickListener(cardListener);

        View.OnClickListener chatListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chatListFragment == null) {
                    LoadChatData();
                }
                else {
                    boolean bSetChatData = true;
                    for(int i = 0; i< mMyData.arrChatDataList.size(); i++)
                    {
                        if(mMyData.arrChatDataList.get(mMyData.arrChatNameList.get(i)).Gender == null || mMyData.arrChatDataList.get(mMyData.arrChatNameList.get(i)).Gender.equals("") )
                        {
                            bSetChatData = false;
                            break;
                        }
                    }

                    if(bSetChatData == true)
                    {
                        iv_myPage.setVisibility(View.GONE);
                        logo.setVisibility(View.GONE);
                        iv_heartCnt.setVisibility(View.GONE);
                        txt_heartCnt.setVisibility(View.GONE);
                        iv_fanCnt.setVisibility(View.GONE);
                        txt_fanCnt.setVisibility(View.GONE);

                        txt_title.setVisibility(TextView.VISIBLE);
                        txt_title.setText("채팅 목록");
                        mMyData.SetCurFrag(2);
                        Fragment frg = null;
                        frg = mFragmentMng.findFragmentByTag("ChatListFragment");
                        mFragmentMng.beginTransaction().replace(R.id.frag_container, chatListFragment, "ChatListFragment").commit();

                        CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);

                        ib_chatList.setSelected(!v.isSelected());
                        txt_chatList.setSelected(!v.isSelected());
                        setImageAlpha(100, 100, 255, 100, 100);
                        SetButtonColor(2);
                        SetFontColor(2);
                    }

                }


            }
        };

        ib_chatList = findViewById(R.id.ib_chatlist);
        ib_chatList.setOnClickListener(chatListener);
        txt_chatList.setOnClickListener(chatListener);

        View.OnClickListener fanListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFunc.getInstance().SetFanAlarmVisible(false);

                if (fanFragment == null)
                    LoadFanData();

                else {

                    boolean bFanDataSet = true;
                    for(int i = 0; i< mMyData.arrMyFanDataList.size(); i++)
                    {
                        if(mMyData.arrMyFanDataList.get(mMyData.arrMyFanList.get(i).Idx).Gender == null || mMyData.arrMyFanDataList.get(mMyData.arrMyFanList.get(i).Idx).Gender.equals(""))
                        {
                            bFanDataSet = false;
                            break;
                        }
                    }

                    if(bFanDataSet == true)
                    {

                        iv_myPage.setVisibility(View.GONE);
                        logo.setVisibility(View.GONE);

                        iv_heartCnt.setVisibility(View.VISIBLE);
                        txt_heartCnt.setVisibility(View.VISIBLE);
                        String str = String.format("%,d", mMyData.getRecvHoney() * -1);

                        txt_heartCnt.setText(str);

                        iv_fanCnt.setVisibility(View.VISIBLE);
                        txt_fanCnt.setVisibility(View.VISIBLE);
                        txt_fanCnt.setText(Integer.toString(mMyData.arrMyFanList.size()));

                        txt_title.setVisibility(TextView.VISIBLE);
                        txt_title.setText("내 팬");
                        mMyData.SetCurFrag(3);
                        ib_fan.setSelected(!v.isSelected());
                        txt_fan.setSelected(!v.isSelected());
                        setImageAlpha(100, 100, 100, 255, 100);

                        SetButtonColor(3);
                        SetFontColor(3);
                        Intent intent = new Intent(getApplicationContext(), FanFragment.class);
                        Bundle bundle = new Bundle();

                        intent.putExtra("FanList", mMyData.arrMyFanList);
                        intent.putExtra("FanCount", mMyData.nFanCount);
                        intent.putExtra("StarList", mMyData.arrMyStarList);
                        intent.putExtra("ViewMode", 0);
                        intent.putExtras(bundle);

                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, fanFragment, "FanListFragment").commit();
                        CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
                    }
                }


            }
        };
        ib_fan = findViewById(R.id.ib_fan);
        ib_fan.setOnClickListener(fanListener);
        txt_fan.setOnClickListener(fanListener);


        /*PagerModelManager manager = new_img PagerModelManager();
        manager.addFragment(new_img Rank_NearFragment(),"가까운 순");



        manager.addFragment(new_img Rank_GoldReceiveFragment(),"실시간 인기 순");
        manager.addFragment(new_img Rank_FanRichFragment(),"팬 보유 순");
        manager.addFragment(new_img Rank_NewMemberFragment(),"새로운 순");

        final ModelPagerAdapter adapter = new_img ModelPagerAdapter(getSupportFragmentManager(),manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();
        springIndicator.setViewPager(viewPager);*/

        SetButtonColor(nStartFragment);
        SetFontColor(nStartFragment);

        switch (nStartFragment) {

            case 0:
                if (homeFragment == null)
                    homeFragment = new HomeFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, homeFragment, "HomeFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.HOME_ACTIVITY);


                setImageAlpha(255, 100, 100, 100, 100);
                iv_myPage.setVisibility(View.VISIBLE);
                txt_title.setVisibility(TextView.GONE);

                break;
            case 1:
                if (cardListFragment == null)
                    LoadCardData();

                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, cardListFragment, "CardListFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
                setImageAlpha(100, 255, 100, 100, 100);
                iv_myPage.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("즐겨찾기");
                break;
            case 2:
                if (chatListFragment == null)
                    LoadChatData();

                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, chatListFragment, "ChatListFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
                setImageAlpha(100, 100, 255, 100, 100);
                iv_myPage.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("채팅 목록");
                break;
            case 3:
                if (fanFragment == null)
                    LoadFanData();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, fanFragment, "FanListFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
                setImageAlpha(100, 100, 100, 255, 100);
                iv_myPage.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("내 팬");
                break;
            case 4:
                if (boardFragment == null)
                    boardFragment = new BoardFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, boardFragment, "BoardFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.BOARD_ACTIVITY);
                setImageAlpha(100, 100, 100, 100, 255);
                iv_myPage.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("게시판");
                break;

            default:
                if (homeFragment == null)
                    homeFragment = new HomeFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, homeFragment, "HomeFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.HOME_ACTIVITY);
                setImageAlpha(255, 100, 100, 100, 100);
                break;
        }


        // 111111111

        int BotCount = 10;

        int BotFrequency_Sec = 1000;
     /*   for(int i = 0; i<BotCount; i++) {
            Bot thrBot = new_img Bot(BotFrequency_Sec * 3);
            thrBot.start();
        }*/

    }

    private void LoadChatData() {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

     //   if (mMyData.arrChatNameList.size() == 0 || mMyData.arrChatNameList.size() == mMyData.arrChatDataList.size()) {
        if(chatListFragment == null)
        {
            chatListFragment = new ChatListFragment(getApplicationContext());
            return;
        }


 /*       for (int i = 0; i < mMyData.arrChatNameList.size(); i++) {
            Query data = FirebaseDatabase.getInstance().getReference().child("User").child(mMyData.getUserIdx()).child("SendList").child(mMyData.arrChatNameList.get(i));
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SimpleChatData DBData = dataSnapshot.getValue(SimpleChatData.class);
                    mMyData.arrChatDataList.put(mMyData.arrChatNameList.get(finalI), DBData);

                    if (mMyData.arrChatNameList.size() == mMyData.arrChatDataList.size())
                        chatListFragment = new ChatListFragment(getApplicationContext());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/
    }

    private void LoadCardData() {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

        if (cardListFragment == null)
            cardListFragment = new CardListFragment();


    }

   /* private void LoadStarData() {
        LoadFanData();
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

        if(mMyData.arrMyStarList.size() == 0)
        {
            if(fanFragment == null)
                //fanFragment = new_img FanFragment();
                fanFragment = new_img FanListFragment();
        }

        for(int i = 0; i < mMyData.arrMyStarList.size(); i++)
        {
            Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.arrMyStarList.get(i).Idx);
            final int finalI = i;
            data.addListenerForSingleValueEvent(new_img ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                    mMyData.arrMyStarDataList.put(mMyData.arrMyStarList.get(finalI).Idx, DBData);

                    if(mMyData.arrMyStarDataList.size() == mMyData.arrMyStarList.size())
                    {
                        if(fanFragment == null)
                            fanFragment = new_img FanListFragment();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }*/

    private void LoadFanData() {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

        //if (mMyData.arrMyFanList.size() == 0 || mMyData.arrMyFanDataList.size() == mMyData.arrMyFanList.size()) {
        if (fanFragment == null) {
            fanFragment = new FanListFragment();
            return;
        }
        //  }

     /*   for (int i = 0; i < mMyData.arrMyFanList.size(); i++) {
            Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.arrMyFanList.get(i).Idx);
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    FanData DBData = dataSnapshot.getValue(FanData.class);

                    Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(DBData.Idx);
                    final FanData finalTempFanData = DBData;
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                            mMyData.arrMyFanDataList.put(mMyData.arrMyFanList.get(finalI).Idx, DBData);

                            if (mMyData.arrMyFanDataList.size() == mMyData.arrMyFanList.size()) {
                                if (fanFragment == null)
                                    fanFragment = new FanListFragment();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/
    }

    public void ViewReportPop() {
        String alertTitle = "종료";
        View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app, null, false);

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        final Button btn_exit;
        final Button btn_no;
        final TextView title;
        final TextView msg;

        title = (TextView) v.findViewById(R.id.title);
        title.setText("경고");

        msg = (TextView) v.findViewById(R.id.msg);
        msg.setText("10건의 신고가 들어왔습니다");

        btn_exit = (Button) v.findViewById(R.id.btn_yes);
        btn_exit.setText("확인");
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_no = (Button) v.findViewById(R.id.btn_no);
        btn_no.setVisibility(View.GONE);

    }


    @Override
    public void onBackPressed() {

        String alertTitle = "종료";
        View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app, null, false);

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        final Button btn_exit;
        final Button btn_no;
        final TextView title;
        final AdView mAdView;

        title = (TextView) v.findViewById(R.id.title);
        title.setVisibility(View.GONE);


        btn_exit = (Button) v.findViewById(R.id.btn_yes);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });

        btn_no = (Button) v.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        mAdView = (AdView) v.findViewById(R.id.adView);
        CommonFunc.getInstance().ViewAdsBanner(mAdView);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_MENU:
                    startActivity(new Intent(getApplicationContext(), MyPageActivity.class));
                    return true;
            }

        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View menu_Main = inflater.inflate(R.layout.menu_main, null);
        actionBar.setCustomView(menu_Main);
        Toolbar parent = (Toolbar) menu_Main.getParent();
        parent.setContentInsetsAbsolute(0, 0);


        CheckBox cbMultiSend = (CheckBox) findViewById(R.id.checkBox);
        cbMultiSend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mAppStatus.bCheckMultiSend = isChecked;
            }
        });

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setImageAlpha(int home, int card, int chat, int fan, int board) {
        /*ib_fan.setImageAlpha(fan);
        ib_board.setImageAlpha(board);
        ib_chatList.setImageAlpha(chat);
        ib_cardList.setImageAlpha(card);
        ib_home.setImageAlpha(home);*/

//ib_fan.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

    }

    private void SetButtonColor(int idx) {

        ib_home.setImageResource(R.drawable.home);
        ib_cardList.setImageResource(R.drawable.favor);
        ib_chatList.setImageResource(R.drawable.chat);
        ib_fan.setImageResource(R.drawable.fan);
        ib_board.setImageResource(R.drawable.board);

        switch (idx) {
            case 0:
                ib_home.setImageResource(R.drawable.home_pressed);
                break;
            case 1:
                ib_cardList.setImageResource(R.drawable.favor_pressed);
                break;
            case 2:
                ib_chatList.setImageResource(R.drawable.chat_pressed);
                break;
            case 3:
                ib_fan.setImageResource(R.drawable.fan_pressed);
                break;
            case 4:
                ib_board.setImageResource(R.drawable.board_pressed);
                break;
        }
    }

    private void SetFontColor(int idx) {
        txt_home.setTextColor(nDisableFontColor);
        txt_cardList.setTextColor(nDisableFontColor);
        txt_chatList.setTextColor(nDisableFontColor);
        txt_fan.setTextColor(nDisableFontColor);
        txt_board.setTextColor(nDisableFontColor);

        switch (idx) {
            case 0:
                txt_home.setTextColor(nEnableFontColor);
                break;
            case 1:
                txt_cardList.setTextColor(nEnableFontColor);
                break;
            case 2:
                txt_chatList.setTextColor(nEnableFontColor);
                break;
            case 3:
                txt_fan.setTextColor(nEnableFontColor);
                break;
            case 4:
                txt_board.setTextColor(nEnableFontColor);
                break;
        }
    }

    String RandImg[] = new String[100];

    private void AddDummy(int Count) {

        RandImg[0] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-144474.jpeg?alt=media&token=b990fd33-af19-4268-a996-5b7c7629660d";
        RandImg[1] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-169915.jpeg?alt=media&token=97c43368-0d28-45cc-9b50-f7169424167d";
        RandImg[2] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-206434.jpeg?alt=media&token=8aa6e539-95b7-4157-9a83-c1c6350f4e87";
        RandImg[3] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-247287.jpeg?alt=media&token=6aaca0d4-256b-4df7-b858-4e607fb1a544";
        RandImg[4] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-247322.jpeg?alt=media&token=feabcaf0-c5f4-43a2-bf23-877fd714b076";
        RandImg[5] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-253758.jpeg?alt=media&token=edf88006-5d8c-4f1d-b807-87720aa7fbf7";
        RandImg[6] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-255349.jpeg?alt=media&token=763ed69e-b5af-44ff-b2cf-77f241dd9378";
        RandImg[7] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-256639.jpeg?alt=media&token=8b115c10-79c8-4ac3-9a3e-5f1ece2f40f9";

        RandImg[8] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-324658.jpeg?alt=media&token=871e0556-d1b6-4011-b5ff-906665199cb6";
        RandImg[9] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-341971.jpeg?alt=media&token=a8e68a0b-09f0-4442-ba17-be4f5934e3ce";
        RandImg[10] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-373918.jpeg?alt=media&token=24aad93d-5d35-4297-a724-fd92eb85c830";
        RandImg[11] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-415829.jpeg?alt=media&token=8c3a690d-4ea4-46cc-b9b1-f694b402dda9";
        RandImg[12] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-459947.jpeg?alt=media&token=4c014403-b76f-4f77-a5ac-7830d4460626";
        RandImg[13] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-531126.jpeg?alt=media&token=85910027-aa71-4884-80f2-851a6ba9dc61";
        RandImg[14] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-573299.jpeg?alt=media&token=f406f392-6a61-4b32-8e98-3b50e24caa4e";
        RandImg[15] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-613713.jpeg?alt=media&token=1dbf91af-9449-4a98-8a68-5287aacf7c94";


        RandImg[16] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-765196.jpeg?alt=media&token=01451d3b-4de5-4555-ae91-d376ee5ceb44";
        RandImg[17] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-356185.jpeg?alt=media&token=bfb58f43-6a54-4cbf-9ee8-2591de5486b0";
        RandImg[18] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-264172.jpeg?alt=media&token=033980e0-29f6-4501-9640-f81abf4c0f4a";
        RandImg[19] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-765228.jpeg?alt=media&token=48bafae0-3bd2-43d6-b536-54486a5128b7";
        RandImg[20] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-672444.jpeg?alt=media&token=4987d946-074c-4f29-825b-bed9ed721351";
        RandImg[21] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-458766.jpeg?alt=media&token=69311d64-a613-4af6-ad2a-fe47c4919acb";
        RandImg[22] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-371311.jpeg?alt=media&token=c595419f-df54-4265-ad5a-32fe8a3a41db";
        RandImg[23] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-371160.jpeg?alt=media&token=682b33b7-2971-4dcd-a79d-2e01aff58223";

        RandImg[24] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-347917.jpeg?alt=media&token=3b004d98-edad-4aa9-bf4e-631f4c5b5b97";
        RandImg[25] = "https://firebasestorage.googleapis.com/v0/b/talkking-25dd8.appspot.com/o/dummy%2Fpexels-photo-255349.jpeg?alt=media&token=763ed69e-b5af-44ff-b2cf-77f241dd9378";
        RandImg[26] = "https://cdn.pixabay.com/photo/2018/02/01/17/09/man-3123561_960_720.jpg";
        RandImg[27] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMScn9nnzqZ8r8zK-ktCdu1bEJk0lSZ5sY3IyoC4lVVnp1dwknFQ";
        RandImg[28] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIjtRrDJNIZkRHsnFZQrwqboDJzvb_v7oeieiIlqEqefpdeA6O";
        RandImg[29] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSSXrM7Ptl68GsgzEVwXN2rjIEaeM6dz64wLN4ThcLQ5v9X9eF1";
        RandImg[30] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT5ZKZwdGlbLy-keLVCjcnCMU_T3RjoFbjzkG4gOe0cCN4XkXHq";
        RandImg[31] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTc0EtUi4VWj0rJjecjXSbUL3KTvWaeFBvlWmBKXOjVahSwz67bHQ";


        for (int i = 0; i < Count; i++) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());


            Random random = new Random();

            // DatabaseReference user = table.child( userIdx);
            String Index = Integer.toString(1000 + i);
            DatabaseReference user = table.child(Index);
            user.child("Idx").setValue(Index);

            mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
            user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());

            int nImgIdx = i;//random.nextInt(32);
            user.child("Img").setValue(RandImg[nImgIdx]);


            user.child("ImgGroup0").setValue(RandImg[nImgIdx]);

            for (int j = 1; j < 4; j++)
                user.child("ImgGroup" + Integer.toString(j)).setValue(mMyData.getUserProfileImg(j));

            String strRandName = randomHangulName();

            user.child("NickName").setValue(strRandName);


            Boolean bRand = random.nextBoolean();
            if (bRand == false)
                user.child("Gender").setValue("남자");
            else
                user.child("Gender").setValue("여자");

            String strAge = Integer.toString(random.nextInt(50) + 20);
            user.child("Age").setValue(strAge);

            double lon = random.nextDouble() * 1000;
            lon = Math.random() * (1.072271) + 126.611512;

            double lat = random.nextDouble() * 100;
            lat = Math.random() * (0.836468) + 37.077091;

            user.child("Lon").setValue(lon);
            user.child("Lat").setValue(lat);

            user.child("Dist").setValue(LocationFunc.getInstance().getDistance(lat, lon, REF_LAT, REF_LON, "meter"));

            user.child("SendCount").setValue(mMyData.getSendHoney());
            long recvGold = random.nextInt(10000);
            user.child("RecvGold").setValue(-1 * recvGold);

            user.child("ImgCount").setValue(mMyData.getUserImgCnt());

            long time = CommonFunc.getInstance().GetCurrentTime();
            user.child("Date").setValue(Long.toString(-1 * random.nextLong()));

            user.child("Memo").setValue(mMyData.getUserMemo());

            user.child("RecvMsgReject").setValue(mMyData.nRecvMsgReject ? 1 : 0);


            //user.child("FanCount").setValue(-1 * random.nextInt(10) * UNIQ_FANCOUNT - Integer.parseInt(Index));

            user.child("FanCount").setValue(-1 * Long.valueOf(Index));


            user.child("Point").setValue(random.nextInt(10000));

            user.child("Grade").setValue(random.nextInt(5));
            user.child("BestItem").setValue(random.nextInt(7));
            user.child("Honey").setValue(random.nextInt(10000));

            user.child("NickChangeCnt").setValue(mMyData.NickChangeCnt);


            table = database.getReference("SimpleData");//.child(mMyData.getUserIdx());

            // DatabaseReference user = table.child( userIdx);
            user = table.child(Index);
            user.child("Idx").setValue(Index);


            mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
            user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
            user.child("Img").setValue(RandImg[nImgIdx]);

            user.child("NickName").setValue(strRandName);

            if (bRand == false)
                user.child("Gender").setValue("남자");
            else
                user.child("Gender").setValue("여자");

            user.child("Age").setValue(strAge);

            user.child("Memo").setValue(mMyData.getUserMemo());

            user.child("RecvGold").setValue(-1 * recvGold);
            user.child("SendGold").setValue(mMyData.getSendHoney());

            user.child("Lon").setValue(lon);
            user.child("Lat").setValue(lat);
            user.child("Dist").setValue(LocationFunc.getInstance().getDistance(lat, lon, REF_LAT, REF_LON, "meter"));

            user.child("Date").setValue(Long.toString(-1 * random.nextLong()));
            user.child("FanCount").setValue(-1 * Long.valueOf(Index));

            user.child("Point").setValue(random.nextInt(10000));

            user.child("Grade").setValue(random.nextInt(5));
            user.child("BestItem").setValue(random.nextInt(7));
            user.child("Honey").setValue(random.nextInt(10000));


        }

        /*FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        DatabaseReference data = fierBaseDataInstance.getReference("UserCount");
        data.setValue(170+Count);*/
    }

    public static String randomHangulName() {
        List<String> 성 = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안",
                "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성", "차", "주",
                "우", "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함", "변", "염", "양",
                "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표", "명", "기", "반", "왕", "금",
                "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구", "용");
        List<String> 이름 = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노", "누", "다",
                "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만", "명", "무", "문", "미", "민", "바", "박",
                "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소", "솔", "수", "숙", "순",
                "숭", "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "요", "용", "우", "원", "월", "위",
                "유", "윤", "율", "으", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준",
                "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
                "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림", "봉", "혼", "황", "량", "린", "을", "비",
                "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "휴", "언", "령", "섬", "들", "견", "추", "걸", "삼",
                "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식", "란", "더", "손", "술", "훔", "반", "빈", "실", "직", "흠",
                "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학", "개", "롱", "평", "늘", "늬", "랑", "얀", "향", "울", "련");
        Collections.shuffle(성);
        Collections.shuffle(이름);
        return 성.get(0) + 이름.get(0) + 이름.get(1);
    }


    class Bot extends Thread {
        private int count = 0;  // 카운트 변수

        public Bot() { // 생성자
            System.out.println(getName() + " Created.");
        }

        public Bot(int frequency) { // 생성자
            count = frequency;
        }

        // 쓰레드 start()시 수행되는 메소드
        public void run() {
            while (true) {

                Random random = new Random();
                int temp = random.nextInt(4);

                switch (temp) {
                    case 0:
                        RunCard();
                        break;
                    case 1:
                        RunSendMsg();
                        break;
                    case 2:
                        RunSendHeart();
                        break;
                    case 3:
                        RunWriteBoard();
                        break;

                }

                try {
                    sleep(count);
                } // 0.5초간 sleep
                catch (InterruptedException e) {
                }
            }
        }
    }

    private void RunWriteBoard() {
        Handler mHandler = new Handler(Looper.getMainLooper());

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
                // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
                DatabaseReference data = fierBaseDataInstance.getReference("BoardIdx");
                data.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Long index = mutableData.getValue(Long.class);
                        if (index == null)
                            return Transaction.success(mutableData);

                        index--;

                        mutableData.setValue(index);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        // TODO 환웅 성공 했을때 오는 함수 인듯
                        // TODO 환웅 콜백 함수가 있나?
                        long index = dataSnapshot.getValue(long.class);
                        BoardData.getInstance().BoardIdx = index;

                        BoardMsgDBData sendData = new BoardMsgDBData();
                        Random random = new Random();
                        int tempIdx = random.nextInt(100);

                        sendData.Idx = Integer.toString(1000 + tempIdx);
                        sendData.Msg = randomHangulName();

                        mFireBaseData.SaveBoardData(sendData);

                    }
                });


            }
        }, 0);

    }

    private void RunSendHeart() {
        Handler mHandler = new Handler(Looper.getMainLooper());

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 0);

    }

    private void RunSendMsg() {
        Handler mHandler = new Handler(Looper.getMainLooper());

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "RunSendMsg", Toast.LENGTH_SHORT).show();
            }
        }, 0);

    }

    private void RunCard() {
        Handler mHandler = new Handler(Looper.getMainLooper());

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Random random = new Random();
                int tempIdx = random.nextInt(100);
                String Idx = Integer.toString(1000 + tempIdx);

                int tempTargetIdx = random.nextInt(100);
                String TargetIdx = Integer.toString(1000 + tempTargetIdx);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference table;
                table = database.getReference("User/" + Idx);
                table.child("CardList").child(TargetIdx).setValue(TargetIdx);

            }
        }, 0);

    }

    public class PrepareCard extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            iv_myPage.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);
            iv_heartCnt.setVisibility(View.GONE);
            txt_heartCnt.setVisibility(View.GONE);
            iv_fanCnt.setVisibility(View.GONE);
            txt_fanCnt.setVisibility(View.GONE);

            txt_title.setVisibility(TextView.VISIBLE);
            txt_title.setText("즐겨찾기");

            mMyData.SetCurFrag(1);

            if (cardListFragment == null)
                LoadCardData();

        }

        @Override
        protected Integer doInBackground(Integer... voids) {

            if(mMyData.arrCardNameList.size() == mMyData.arrCarDataList.size())
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, cardListFragment, "CardListFragment").commit();
            }
            else
            {
                for (int i = 0; i < mMyData.arrCardNameList.size(); i++) {
                    Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.arrCardNameList.get(i));
                    final int finalI = i;
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                            mMyData.arrCarDataList.put(mMyData.arrCardNameList.get(finalI), DBData);
                            if(mMyData.arrCardNameList.size() == mMyData.arrCarDataList.size()) {
                                CoomonValueData.getInstance().bMySet_Card = true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, cardListFragment, "CardListFragment").commit();
                            }
                            //CommonFunc.getInstance().CheckMyDataSet(mActivity);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            ib_cardList.setSelected(true);
            txt_cardList.setSelected(true);

            setImageAlpha(100, 255, 100, 100, 100);
            SetButtonColor(1);
            SetFontColor(1);
            CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }



}
