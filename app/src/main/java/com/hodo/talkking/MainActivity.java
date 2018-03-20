package com.hodo.talkking;

import android.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hodo.talkking.Data.BoardData;
import com.hodo.talkking.Data.BoardMsgDBData;
import com.hodo.talkking.Data.ChatData;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.FanData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SettingData;
import com.hodo.talkking.Data.SimpleChatData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.AppStatus;
import com.hodo.talkking.Util.CommonFunc;
import com.hodo.talkking.Util.LocationFunc;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.hodo.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;
import static com.hodo.talkking.Data.CoomonValueData.OFFAPP;
import static com.hodo.talkking.Data.CoomonValueData.REF_LAT;
import static com.hodo.talkking.Data.CoomonValueData.REF_LON;
import static com.hodo.talkking.Data.CoomonValueData.UNIQ_FANCOUNT;
import static com.kakao.usermgmt.StringSet.id;

public class MainActivity extends AppCompatActivity {

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

    //ImageButton ib_pcr_open;
    ImageButton ib_buy_jewel;

    ImageView iv_refresh;

    ImageView logo;

    TextView tv_MainTitle;
    LinearLayout layout_lowbar,layout_topbar;
    BoardFragment boardFragment;



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

    // 눌렀을때의 폰트 색상
    private int nEnableFontColor = Color.BLACK;
    // 안눌러졌을때의 폰트 색상
    private int nDisableFontColor = Color.GRAY;

    private FirebaseAnalytics mFirebaseAnalytics;

    public class Prepare extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(mMyData.mServiceConn == null)
            {
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
                            mMyData.skuDetails = mMyData.mService.getSkuDetails(3,getPackageName(), "inapp", mMyData.querySkus);
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
                                if (mMyData.sku.equals("gold_10")) mMyData.strGold[0] = mMyData.price;
                                else if (mMyData.sku.equals("gold_20")) mMyData.strGold[1]= mMyData.price;
                                else if (mMyData.sku.equals("gold_50")) mMyData.strGold[2] = mMyData.price;
                                else if (mMyData.sku.equals("gold_100")) mMyData.strGold[3] = mMyData.price;
                                else if (mMyData.sku.equals("gold_200")) mMyData.strGold[4] = mMyData.price;
                                else if (mMyData.sku.equals("gold_500")) mMyData.strGold[5] = mMyData.price;
                                else if (mMyData.sku.equals("gold_1000")) mMyData.strGold[6] = mMyData.price;
                            }
                        }

                    }
                };

                Intent serviceIntent =
                        new Intent("com.android.vending.billing.InAppBillingService.BIND");
                serviceIntent.setPackage("com.android.vending");
                bindService(serviceIntent, mMyData.mServiceConn , Context.BIND_AUTO_CREATE);
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
            mMyData.arrUserWoman_Near_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Near, mMyData.nStartAge, mMyData.nEndAge );
            mMyData.arrUserMan_Near_Age = mMyData.SortData_Age(mMyData.arrUserMan_Near, mMyData.nStartAge, mMyData.nEndAge );

            mMyData.arrUserAll_Recv_Age= mMyData.SortData_Age(mMyData.arrUserAll_Recv, mMyData.nStartAge, mMyData.nEndAge);
            mMyData.arrUserWoman_Recv_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Recv, mMyData.nStartAge, mMyData.nEndAge );
            mMyData.arrUserMan_Recv_Age = mMyData.SortData_Age(mMyData.arrUserMan_Recv, mMyData.nStartAge, mMyData.nEndAge );

            mMyData.arrUserAll_New_Age = mMyData.SortData_Age(mMyData.arrUserAll_New, mMyData.nStartAge, mMyData.nEndAge);
            mMyData.arrUserWoman_New_Age = mMyData.SortData_Age(mMyData.arrUserWoman_New, mMyData.nStartAge, mMyData.nEndAge );
            mMyData.arrUserMan_New_Age = mMyData.SortData_Age(mMyData.arrUserMan_New, mMyData.nStartAge, mMyData.nEndAge );

            mMyData.arrUserAll_Send_Age = mMyData.SortData_Age(mMyData.arrUserAll_Send, mMyData.nStartAge, mMyData.nEndAge);
            mMyData.arrUserWoman_Send_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Send, mMyData.nStartAge, mMyData.nEndAge );
            mMyData.arrUserMan_Send_Age = mMyData.SortData_Age(mMyData.arrUserMan_Send, mMyData.nStartAge, mMyData.nEndAge );

            return 0;
        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME);
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

        if(mMyData.getUserIdx() == null)
        {
            CommonFunc.getInstance().restartApp(getApplicationContext());
        }

        if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
            pref.getInt("Badge", mMyData.badgecount );

            if ( mMyData.badgecount >= 1)
            {
                mMyData.badgecount = 0;
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count_package_name", "com.hodo.talkking");
                intent.putExtra("badge_count_class_name", "com.hodo.talkking.LoginActivity");
                intent.putExtra("badge_count", mMyData.badgecount);
                sendBroadcast(intent);
            }


            if (mMyData.GetCurFrag() == 2) {
                Fragment frg = null;
                frg = mFragmentMng.findFragmentByTag("ChatListFragment");
                final FragmentTransaction ft = mFragmentMng.beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
 /*           if(mMyData.GetCurFrag() == 3)
            {
                Fragment frg = null;
                frg = mFragmentMng.findFragmentByTag("FanListFragment");
                final FragmentTransaction ft = mFragmentMng.beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }*/
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMng = getSupportFragmentManager();
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-8954582850495744~7252454040");

        Bundle bundle = getIntent().getExtras();
        nStartFragment = (int) bundle.getSerializable("StartFragment");
        nStartByNoti = (int) bundle.getSerializable("Noti");


        OFFAPP = false;
        if(nStartByNoti == 1)
            return;



        if(mMyData.getUserIdx() == null)
        {
           CommonFunc.getInstance().restartApp(getApplicationContext());
        }

/*        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle Analyticsbundle = new_img Bundle();
        Analyticsbundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        Analyticsbundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Name");
        Analyticsbundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, Analyticsbundle);*/

        //AddDummy(100);

        mMyData.mContext = getApplicationContext();
        mMyData.mActivity = mActivity;

        if(mMyData.arrReportList.size() >= 10)
            ViewReportPop();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PrefSetting", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Sound",SettingData.getInstance().IsAlarmSettingSound());
        editor.putBoolean("Vibe",SettingData.getInstance().IsAlarmSettingVibration());
        editor.commit();


        SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
        pref.getInt("Badge", mMyData.badgecount );


        if ( mMyData.badgecount >= 1)
        {
            mMyData.badgecount = 0;
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count_package_name", "com.hodo.talkking");
            intent.putExtra("badge_count_class_name", "com.hodo.talkking.LoginActivity");
            intent.putExtra("badge_count", mMyData.badgecount);
            sendBroadcast(intent);
        }



        logo = findViewById(R.id.iv_logo);



        iv_myPage = findViewById(R.id.iv_mypage);
        txt_title = findViewById(R.id.txt_title);

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
                startActivity(new Intent(getApplicationContext(),MyPageActivity.class));
            }
        });

        CommonFunc.getInstance().Card_Alarm = (ImageView)findViewById(R.id.alarm_favor);
        CommonFunc.getInstance().Chat_Alarm = (ImageView)findViewById(R.id.alarm_chat);
        CommonFunc.getInstance().Fan_Alarm = (ImageView)findViewById(R.id.alarm_fan);
        CommonFunc.getInstance().Mail_Alarm = (ImageView)findViewById(R.id.alarm_mail);
        CommonFunc.getInstance().Item_Box = (ImageView)findViewById(R.id.iv_itemBox);
        CommonFunc.getInstance().Item_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyJewelBoxActivity.class));
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
            }
        });

        final boolean[] bThreadRun = {false};
        CommonFunc.getInstance().Honey_Box = (ImageView)findViewById(R.id.iv_honeybox);
        CommonFunc.getInstance().SetMailAlarmVisible(false);
        CommonFunc.getInstance().Honey_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunc.getInstance().SetMailAlarmVisible(false);
                startActivity(new Intent(getApplicationContext(),MailboxActivity.class));
            }
        });

        CommonFunc.getInstance().Board_Write = (ImageView)findViewById(R.id.iv_boardwrite);
        CommonFunc.getInstance().Board_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonFunc.getInstance().IsCurrentDateCompare(new Date(mMyData.GetLastBoardWriteTime()), CoomonValueData.BOARD_WRITE_TIME_MIN) == false)
                {
                    String Desc = CommonFunc.getInstance().GetRemainTimeByFuture(new Date(mMyData.GetLastBoardWriteTime() + (CoomonValueData.BOARD_WRITE_TIME_MIN * CoomonValueData.MIN_MILLI_SECONDS)), true);
                    CommonFunc.getInstance().ShowDefaultPopup(MainActivity.this, "게시판 작성", "남은시간 " + Desc);
                }
                else
                    startActivity(new Intent(getApplicationContext(),BoardWriteActivity.class));
            }
        });
        CommonFunc.getInstance().MyBoard_Write = (Button)findViewById(R.id.iv_myboardwrite);
        CommonFunc.getInstance().MyBoard_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BoardMyListActivity.class));

            }
        });

        CommonFunc.getInstance().Filter = findViewById(R.id.ib_filter);
        CommonFunc.getInstance().Filter.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.boardBgColor), PorterDuff.Mode.MULTIPLY);

        CommonFunc.getInstance().Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //  startActivity(new_img Intent(getApplicationContext(),MainSettingActivity.class));
                //  overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View v = LayoutInflater.from(mActivity).inflate(R.layout.category_popup,null,false);

                builder.setView(v);
                final AlertDialog filter_dialog = builder.create();

                filter_dialog.show();


                final RadioButton rbtn_three;
                final RadioButton rbtn_four;

                final CheckBox cbox_man;
                final CheckBox cbox_woman;

                final Spinner spin_StartAge, spin_EndAge;

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
                        mMyData.setSettingData(mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration(),  mSetting.IsAlarmSettingPop());
                        mFireBaseData.SaveSettingData(mMyData.getUserIdx(), mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration(), mSetting.IsAlarmSettingPop());
                        filter_dialog.dismiss();

                        SortDataAge sortData = new SortDataAge();
                        sortData.execute();

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


                rbtn_three = (RadioButton) v.findViewById(R.id.rbtn_three);
                rbtn_four = (RadioButton) v.findViewById(R.id.rbtn_four);

                cbox_man = (CheckBox) v.findViewById(R.id.rbtn_man);
                cbox_woman = (CheckBox) v.findViewById(R.id.rbtn_woman);

                switch (mMyData.nSearchMode)
                {
                    case 0:
                        if(mMyData.getUserGender().equals("여자"))
                        {
                            cbox_man.setChecked(true);
                            cbox_woman.setChecked(false);
                        }
                        else
                        {
                            cbox_man.setChecked(false);
                            cbox_woman.setChecked(true);
                        }
                        break;
                    case 3:
                        cbox_man.setChecked(true);
                        cbox_woman.setChecked(true);
                        break;
                    case 1:
                        cbox_man.setChecked(true);
                        cbox_woman.setChecked(false);
                        break;
                    case 2:
                        cbox_man.setChecked(false);
                        cbox_woman.setChecked(true);
                        break;
                }

                if(mSetting.getnViewSetting() == 0)
                {
                    rbtn_three.setChecked(false);
                    rbtn_four.setChecked(false);
                }
                else if(mSetting.getnViewSetting() == 1)
                {
                    rbtn_three.setChecked(true);
                    rbtn_four.setChecked(false);
                }
                else if(mSetting.getnViewSetting() == 2)
                {
                    rbtn_three.setChecked(false);
                    rbtn_four.setChecked(true);
                }


                rbtn_three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rbtn_three.isChecked()) {
                            rbtn_three.setChecked(true);
                            rbtn_four.setChecked(false);
                            mSetting.setnViewSetting(1);
                        }
                    }
                });


                rbtn_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rbtn_four.isChecked())
                        {
                            rbtn_three.setChecked(false);
                            rbtn_four.setChecked(true);
                            mSetting.setnViewSetting(2);
                        }

                    }
                });

                cbox_man.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(cbox_man.isChecked())
                        {
                            if(cbox_woman.isChecked())
                                mSetting.setnSearchSetting(3);
                            else
                                mSetting.setnSearchSetting(1);
                        }
                        else {
                            cbox_woman.setChecked(true);
                            mSetting.setnSearchSetting(2);
                        }
                    }
                });

                cbox_woman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(cbox_woman.isChecked())
                        {
                            if(cbox_man.isChecked())
                                mSetting.setnSearchSetting(3);
                            else
                                mSetting.setnSearchSetting(2);
                        }
                        else {
                            cbox_man.setChecked(true);
                            mSetting.setnSearchSetting(1);
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




                RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(rbtn_three.isChecked())
                            mSetting.setnViewSetting(1);
                        else if(rbtn_four.isChecked())
                            mSetting.setnViewSetting(2);
                    }
                };




            }
        });


        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        mUIData.setHeight(height);
        mUIData.setWidth(width);
        mUIData.setLlp_ListItem(new LinearLayout.LayoutParams(width,height/7));

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

        mMyData.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
        mMyData.mRewardedVideoAd.loadAd("ca-app-pub-8954582850495744/9112330627",
                new AdRequest.Builder().build());

      /*  Prepare prepareJob = new_img Prepare();

        prepareJob.execute();*/

        boolean bCheckConnt = mMyData.CheckConnectDate();
        if(bCheckConnt == true)
        {
            String alertTitle = "종료";
            View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app,null,false);

            final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

            final TextView txt_Title;
            txt_Title = (TextView)v.findViewById(R.id.title);
            txt_Title.setText("출석 체크 보상");
            final TextView txt_Body;
            txt_Body = (TextView)v.findViewById(R.id.msg);
            txt_Body.setText(mUIData.getAdReward()[mMyData.getGrade()]+"코인 획득!");

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
                public void onDismiss (DialogInterface var1){
                    mMyData.setUserHoney(mMyData.getUserHoney()+mUIData.getAdReward()[mMyData.getGrade()]);
                    mMyData.setPoint(mUIData.getAdReward()[mMyData.getGrade()]);
                }

            });

            bCheckConnt = false;
        }

        // Toast.makeText(getApplicationContext(),"width: "+width+"height: "+ height,Toast.LENGTH_LONG).show();
        /*ib_pcr_open = (ImageButton)findViewById(R.id.ib_pcr_open);
        ib_pcr_open.setOnClickListener(new_img View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMyData.nPublicRoomStatus == 0)
                {
                    AlertDialog.Builder builder = new_img AlertDialog.Builder(mActivity);
                    View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_pcr_open,null,false);
                    //v.getBackground().setAlpha(0);


                    final RadioButton btn_Member_50 =  v.findViewById(R.id.member_50);
                    final RadioButton btn_Member_100 =  v.findViewById(R.id.member_100);
                    final RadioButton btn_Member_200 =  v.findViewById(R.id.member_200);

                    final int[] publicRoomMemberCnt = new_img int[1];

                    final RadioButton btn_time_30 =  v.findViewById(R.id.time_30);
                    final RadioButton btn_time_60 =  v.findViewById(R.id.time_60);
                    final RadioButton btn_time_120 =  v.findViewById(R.id.time_120);

                    final int[] publicRoomTime = new_img int[1];

                    RadioButton.OnClickListener optionOnClickListener = new_img RadioButton.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            if(btn_Member_50.isChecked())
                                publicRoomMemberCnt[0] = 50;
                            else if(btn_Member_100.isChecked())
                                publicRoomMemberCnt[0] = 100;
                            else if(btn_Member_200.isChecked())
                                publicRoomMemberCnt[0] = 200;

                            else if(btn_time_30.isChecked())
                                publicRoomTime[0] = 30;
                            else if(btn_time_60.isChecked())
                                publicRoomTime[0] = 60;
                            else if(btn_time_120.isChecked())
                                publicRoomTime[0] = 120;

                        }
                    };

                    btn_time_30.setOnClickListener(optionOnClickListener);
                    btn_time_60.setOnClickListener(optionOnClickListener);
                    btn_time_120.setOnClickListener(optionOnClickListener);

                    btn_Member_50.setOnClickListener(optionOnClickListener);
                    btn_Member_100.setOnClickListener(optionOnClickListener);
                    btn_Member_200.setOnClickListener(optionOnClickListener);



                    builder.setView(v);
                    final AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new_img ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    Button btn_open_pcr = v.findViewById(R.id.btn_open_pcr);
                    btn_open_pcr.setOnClickListener(new_img View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            boolean rtValuew = mMyData.makePublicRoom(publicRoomMemberCnt[0], publicRoomTime[0]);

                            Intent intent= new_img Intent(getApplicationContext(), PublicChatRoomHostActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                }


                else
                {
                    Intent intent= new_img Intent(getApplicationContext(), PublicChatRoomHostActivity.class);
                    intent.putExtra("RoomLimit", mMyData.nPublicRoomLimit);
                    intent.putExtra("RoomTime", mMyData.nPublicRoomTime);
                    startActivity(intent);
                }
            }
        });*/


        txt_home = findViewById(R.id.tv_home);
        txt_cardList  = findViewById(R.id.tv_favor);
        txt_chatList= findViewById(R.id.tv_chat);
        txt_fan= findViewById(R.id.tv_fan);
        txt_board = findViewById(R.id.tv_board);

        ib_home = findViewById(R.id.ib_home);

        //ib_home.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

        ib_home.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                iv_myPage.setVisibility(View.VISIBLE);
                logo.setVisibility(View.VISIBLE);
                txt_title.setVisibility(TextView.GONE);

                mMyData.SetCurFrag(0);
                //getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment, "HomeFragment").commit();
                //ib_home.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

                setImageAlpha(255,100,100,100,100);
                SetButtonColor(0);
                SetFontColor(0);
                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.HOME_ACTIVITY);
                view.setSelected(!view.isSelected());
                if(view.isSelected()){


                }else{

                }

            }
        });

        //tv_MainTitle = (TextView)findViewById(R.id.tv_maintitle);


        //layout_topbar = (LinearLayout)findViewById(R.id.layout_topbar);
        //layout_lowbar = (LinearLayout)findViewById(R.id.layout_lowbar);

        homeFragment = new HomeFragment();
        LoadFanData();
        boardFragment = new BoardFragment();
        LoadCardData();
        LoadChatData();

        ib_board = findViewById(R.id.ib_board);
        // ib_board.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

        ib_board.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                iv_myPage.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("게시판");
                mMyData.SetCurFrag(4);

                if(boardFragment == null)
                    boardFragment = new BoardFragment();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,boardFragment, "BoardFragment").commit();

                view.setSelected(!view.isSelected());
                setImageAlpha(100,100,100,100,255);
                SetButtonColor(4);
                SetFontColor(4);
                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.BOARD_ACTIVITY);

            }
        });
        ib_cardList = findViewById(R.id.ib_cardlist);
        //ib_cardList.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);
        ib_cardList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                iv_myPage.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("즐겨찾기");
                mMyData.SetCurFrag(1);
                if(cardListFragment == null)
                    LoadCardData();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cardListFragment, "CardListFragment").commit();
                view.setSelected(!view.isSelected());

                setImageAlpha(100,255,100,100,100);
                SetButtonColor(1);
                SetFontColor(1);
                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);

                if(view.isSelected()){
                    int a = 0;

                }else{
                    int b = 0;
                }
                //startActivity(new_img Intent(getApplicationContext(),CardListActivity.class));
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

            }
        });
        ib_chatList = findViewById(R.id.ib_chatlist);
        //ib_chatList.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);
        ib_chatList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                CommonFunc.getInstance().SetChatAlarmVisible(false);
                iv_myPage.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("채팅 목록");
                mMyData.SetCurFrag(2);
                Fragment frg = null;
                frg = mFragmentMng.findFragmentByTag("ChatListFragment");

                if(chatListFragment == null)
                    LoadChatData();
                else
                    mFragmentMng.beginTransaction().replace(R.id.frag_container,chatListFragment, "ChatListFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
                // mCommon.mFragmentManager.beginTransaction().replace(R.id.frag_container,chatListFragment).commit();

                /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack("ChatListFragment").replace(R.id.frag_container,cardListFragment).commit();
                transaction.add(R.id.frag_container,chatListFragment, "ChatListFragment");
                transaction.commit();*/
                // getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,chatListFragment).commit();
                view.setSelected(!view.isSelected());
                setImageAlpha(100,100,255,100,100);
                SetButtonColor(2);
                SetFontColor(2);
                /*

                ib_fan.setImageResource(R.drawable.btn_fan_normal);
                ib_board.setImageResource(R.drawable.btn_board_normal);
                ib_chatList.setImageResource(R.drawable.btn_chat_selected);
                ib_cardList.setImageResource(R.drawable.btn_card_normal);
                ib_home.setImageResource(R.drawable.btn_home_normal);*/

                //startActivity(new_img Intent(getApplicationContext(),ChatListActivity.class));
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

            }
        });
        ib_fan = findViewById(R.id.ib_fan);
        //ib_fan.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);
        ib_fan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                CommonFunc.getInstance().SetFanAlarmVisible(false);
                iv_myPage.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);

                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("내 팬");
                mMyData.SetCurFrag(3);
                view.setSelected(!view.isSelected());
                setImageAlpha(100,100,100,255,100);

                SetButtonColor(3);
                SetFontColor(3);
/*
                ib_fan.setImageResource(R.drawable.btn_fan_selected);
                ib_board.setImageResource(R.drawable.btn_board_normal);
                ib_chatList.setImageResource(R.drawable.btn_chat_normal);
                ib_cardList.setImageResource(R.drawable.btn_card_normal);
                ib_home.setImageResource(R.drawable.btn_home_normal);*/

                //startActivity(new_img Intent(getApplicationContext(),FanActivity.class));
         /*       Intent intent = new_img Intent(getApplicationContext(), FanActivity.class);
                intent.putExtra("ViewMode", 0);
                startActivity(intent);
*/
                Intent intent = new Intent(getApplicationContext(), FanFragment.class);
                Bundle bundle = new Bundle();

                intent.putExtra("FanList", mMyData.arrMyFanList);
                intent.putExtra("FanCount", mMyData.nFanCount);

                //intent.putExtra("FanData", mMyData.arrMyFanDataList);

                intent.putExtra("StarList", mMyData.arrMyStarList);
                //intent.putExtra("StarData", mMyData.arrMyStarDataList);

  /*              bundle.putSerializable("Target", stTargetData);
                intent.putExtra("FanList", stTargetData.arrFanList);
                intent.putExtra("StarList", stTargetData.arrStarList);*/
                intent.putExtra("ViewMode", 0);
                intent.putExtras(bundle);
                if(fanFragment == null)
                    LoadFanData();
                else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, fanFragment, "FanListFragment").commit();
                }

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);

                //startActivity(intent);
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);



            }
        });


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

        switch (nStartFragment)
        {

            case 0:
                if(homeFragment == null)
                    homeFragment = new HomeFragment();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment, "HomeFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.HOME_ACTIVITY);


                setImageAlpha(255,100,100,100,100);
                iv_myPage.setVisibility(View.VISIBLE);
                txt_title.setVisibility(TextView.GONE);

                break;
            case 1:
                if(cardListFragment == null)
                    LoadCardData();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cardListFragment, "CardListFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
                setImageAlpha(100,255,100,100,100);
                iv_myPage.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("즐겨찾기");
                break;
            case 2:
                if(chatListFragment == null)
                    LoadChatData();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,chatListFragment, "ChatListFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
                setImageAlpha(100,100,255,100,100);
                iv_myPage.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("채팅 목록");
                break;
            case 3:
                if(fanFragment == null)
                    LoadFanData();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fanFragment, "FanListFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.NONE);
                setImageAlpha(100,100,100,255,100);
                iv_myPage.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("내 팬");
                break;
            case 4:
                if(boardFragment == null)
                    boardFragment = new BoardFragment();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,boardFragment, "BoardFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.BOARD_ACTIVITY);
                setImageAlpha(100,100,100,100,255);
                iv_myPage.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("게시판");
                break;

            default:
                if(homeFragment == null)
                    homeFragment = new HomeFragment();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment,"HomeFragment").commit();

                CommonFunc.getInstance().SetActivityTopRightBtn(CommonFunc.ACTIVITY_TYPE.HOME_ACTIVITY);
                setImageAlpha(255,100,100,100,100);
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

        if(mMyData.arrChatNameList.size() == 0 || mMyData.arrChatNameList.size() == mMyData.arrChatDataList.size())
        {
            chatListFragment = new ChatListFragment(getApplicationContext());
            return;
        }


        for(int i = 0; i < mMyData.arrChatNameList.size(); i++)
        {
            Query data = FirebaseDatabase.getInstance().getReference().child("User").child(mMyData.getUserIdx()).child("SendList").child(mMyData.arrChatNameList.get(i));
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SimpleChatData DBData = dataSnapshot.getValue(SimpleChatData.class);
                    mMyData.arrChatDataList.put(mMyData.arrChatNameList.get(finalI), DBData);

                    if(mMyData.arrChatNameList.size() == mMyData.arrChatDataList.size())
                        chatListFragment = new ChatListFragment(getApplicationContext());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void LoadCardData() {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

        if(mMyData.arrCardNameList.size() == 0)
            cardListFragment = new CardListFragment();

        for(int i = 0; i < mMyData.arrCardNameList.size(); i++)
        {
            Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.arrCardNameList.get(i));
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                    mMyData.arrCarDataList.put(mMyData.arrCardNameList.get(finalI), DBData);

                    if(mMyData.arrCarDataList.size() == mMyData.arrCardNameList.size())
                        cardListFragment = new CardListFragment();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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

        if(mMyData.arrMyFanList.size() == 0 || mMyData.arrMyFanDataList.size() == mMyData.arrMyFanList.size())
        {
            if(fanFragment == null) {
                fanFragment = new FanListFragment();
                return;
            }
        }

        for(int i = 0; i < mMyData.arrMyFanList.size(); i++)
        {
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

                            if(mMyData.arrMyFanDataList.size() == mMyData.arrMyFanList.size())
                            {
                                if(fanFragment == null)
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
        }
    }

    public void ViewReportPop()
    {
        String alertTitle = "종료";
        View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app,null,false);

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        final Button btn_exit;
        final Button btn_no;
        final TextView title;
        final TextView msg;

        title =  (TextView) v.findViewById(R.id.title);
        title.setText("경고");

        msg =  (TextView) v.findViewById(R.id.msg);
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
    public void onBackPressed(){

        String alertTitle = "종료";
        View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app,null,false);

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        final Button btn_exit;
        final Button btn_no;
        final TextView title;
        final AdView mAdView;

        title =  (TextView) v.findViewById(R.id.title);
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


        mAdView = (AdView)v.findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_MENU:
                    startActivity(new Intent(getApplicationContext(),MyPageActivity.class));
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

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View menu_Main  = inflater.inflate(R.layout.menu_main,null);
        actionBar.setCustomView(menu_Main);
        Toolbar parent = (Toolbar)menu_Main.getParent();
        parent.setContentInsetsAbsolute(0,0);



        CheckBox cbMultiSend = (CheckBox)findViewById(R.id.checkBox);
        cbMultiSend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mAppStatus.bCheckMultiSend = isChecked;
            }
        });

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setImageAlpha(int home, int card, int chat, int fan, int board ){
        /*ib_fan.setImageAlpha(fan);
        ib_board.setImageAlpha(board);
        ib_chatList.setImageAlpha(chat);
        ib_cardList.setImageAlpha(card);
        ib_home.setImageAlpha(home);*/

//ib_fan.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

    }

    private void SetButtonColor(int idx)
    {

        ib_home.setImageResource(R.drawable.home);
        ib_cardList.setImageResource(R.drawable.favor);
        ib_chatList.setImageResource(R.drawable.chat);
        ib_fan.setImageResource(R.drawable.fan);
        ib_board.setImageResource(R.drawable.board);

        switch (idx)
        {
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

    private void SetFontColor(int idx)
    {
        txt_home.setTextColor(nDisableFontColor);
        txt_cardList.setTextColor(nDisableFontColor);
        txt_chatList.setTextColor(nDisableFontColor);
        txt_fan.setTextColor(nDisableFontColor);
        txt_board.setTextColor(nDisableFontColor);

        switch (idx)
        {
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

    String RandImg[] = new String [100];

    private void AddDummy(int Count)
    {

        RandImg[0] = "https://cdn.pixabay.com/photo/2017/07/20/21/48/portrait-2523916_960_720.jpg";
        RandImg[1] = "https://cdn.pixabay.com/photo/2014/07/03/23/20/making-a-face-383391_960_720.jpg";
        RandImg[2] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQA6CuJdoyKGP5oGe8lrqCV_IwuoBErScZuUUwoYZ39zGkUfpqokw";
        RandImg[3] = "https://cdn.pixabay.com/photo/2017/03/30/21/34/portrait-2189787_960_720.jpg";
        RandImg[4] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR0i4zzU2SdB2tHGBHQ8PAkYE9NBWB0dG0KJ9dS_XnolhACILB5";
        RandImg[5] = "https://cdn.pixabay.com/photo/2015/12/06/15/34/guy-1079623_960_720.jpg";
        RandImg[6] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTd0TN695P_D6xEQLHuD8IsZmkyhiKqKB9uEFZheXqZy0Wdgz55Q";
        RandImg[7] = "https://cdn.pixabay.com/photo/2015/07/28/19/21/person-864804_960_720.jpg";

        RandImg[8] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcFnRY5kdiB4Wu4ek76QQYUTtX755sjkHWOG3Xpfs38Y9UcIhp";
        RandImg[9] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRfTpD1mrqkj3wwzCnZTws_UIMEeVUShx1Cc7dqpqwaxxJc0NfSuA";
        RandImg[10] = "https://cdn.pixabay.com/photo/2016/03/17/16/50/person-1263323_960_720.jpg";
        RandImg[11] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJ7GumcCQMXEHyypqfzfZx1D6Tb-gUHZMHfN3ldndicvkStg6t";
        RandImg[12] = "https://cdn.pixabay.com/photo/2015/02/09/12/13/apple-629581_960_720.jpg";
        RandImg[13] = "https://cdn.pixabay.com/photo/2017/12/01/01/25/autumn-2989943_960_720.jpg";
        RandImg[14] = "https://cdn.pixabay.com/photo/2016/08/03/10/41/seagull-1566323_960_720.jpg";
        RandImg[15] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsoqhHmogled-rW7km3MO251N4-yjaM12faA4c_e6ioGDXTJSD";


        RandImg[16] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVXL22N5bWqcocWkoi6ztv3rnIK_R1iyZ2nvIVztOihB5lIccv";
        RandImg[17] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKiJMnOcE5VrIbpBEyOkJ6rHaldgAH-d7Z4ps7lRJULFCAwXGPtw";
        RandImg[18] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTGxwiFB7ZuQecKM1nd83ZhBJ0y-cNHcrhFY12_dj1BbjL9OJmeug";
        RandImg[19] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdIJL8X53dMq8WEH07cjd8nPvjp66TQR8Ia26I83q2XNe-xXzt";
        RandImg[20] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_60MRDkVdMpV9m2FcqymjH9aFGA_0HwI6vuI4-zVN6UGNyN2zag";
        RandImg[21] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSjrknXxPfvTTfMbeUPuHg9Yys05Gk5LmZTSSFd-ju41QQiq0zvg";
        RandImg[22] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYm0Ieyu-q1Nwf81mW-tgnZq0j4cRQm-HXncnAff401ovJJaA7bA";
        RandImg[23] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8QNDZi9t9GRBLaGcBNpL1_0LIAW0-WoJq0dZ9OIpFYONTt46g";

        RandImg[24] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTk2rQpc80hjIITyfTe-WXXZ2L3pj1UYGYGUwv0gXzDsJwOkIrh";
        RandImg[25] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTs8NDsBYk5WrSzyCw-i38SBfNw-eF3t57EXxxYJEMBFnBi85lHyQ";
        RandImg[26] = "https://cdn.pixabay.com/photo/2018/02/01/17/09/man-3123561_960_720.jpg";
        RandImg[27] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMScn9nnzqZ8r8zK-ktCdu1bEJk0lSZ5sY3IyoC4lVVnp1dwknFQ";
        RandImg[28] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIjtRrDJNIZkRHsnFZQrwqboDJzvb_v7oeieiIlqEqefpdeA6O";
        RandImg[29] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSSXrM7Ptl68GsgzEVwXN2rjIEaeM6dz64wLN4ThcLQ5v9X9eF1";
        RandImg[30] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT5ZKZwdGlbLy-keLVCjcnCMU_T3RjoFbjzkG4gOe0cCN4XkXHq";
        RandImg[31] = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTc0EtUi4VWj0rJjecjXSbUL3KTvWaeFBvlWmBKXOjVahSwz67bHQ";


        for(int i = 0; i < Count; i++)
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());


            Random random = new Random();

            // DatabaseReference user = table.child( userIdx);
            String Index = Integer.toString(1000 + i);
            DatabaseReference user = table.child(Index);
            user.child("Idx").setValue(Index);

            mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
            user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());

            int nImgIdx = random.nextInt(32);
            user.child("Img").setValue(RandImg[nImgIdx]);


            user.child("ImgGroup0").setValue(RandImg[nImgIdx]);

            for(int j=1; j< 4 ; j++)
                user.child("ImgGroup"+Integer.toString(j)).setValue(mMyData.getUserProfileImg(j));

            String strRandName = randomHangulName();

            user.child("NickName").setValue(strRandName );


            Boolean bRand = random.nextBoolean();
            if(bRand == false)
                user.child("Gender").setValue("남자");
            else
                user.child("Gender").setValue("여자");

            String strAge = Integer.toString(random.nextInt(50)+20);
            user.child("Age").setValue(strAge);

            double lon = random.nextDouble() * 1000;
            lon = Math.random() * (1.072271) + 126.611512;

            double lat = random.nextDouble() * 100;
            lat =Math.random() * (0.836468) + 37.077091;

            user.child("Lon").setValue(lon);
            user.child("Lat").setValue(lat);

            user.child("Dist").setValue(LocationFunc.getInstance().getDistance(lat, lon, REF_LAT, REF_LON,"meter"));

            user.child("SendCount").setValue(mMyData.getSendHoney());
            long recvGold = random.nextInt(10000);
            user.child("RecvGold").setValue(-1* recvGold);

            user.child("ImgCount").setValue(mMyData.getUserImgCnt());

            long time = CommonFunc.getInstance().GetCurrentTime();
            user.child("Date").setValue(Long.toString( -1 * random.nextLong()));

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

            if(bRand == false)
                user.child("Gender").setValue("남자");
            else
                user.child("Gender").setValue("여자");

            user.child("Age").setValue(strAge);

            user.child("Memo").setValue(mMyData.getUserMemo());

            user.child("RecvGold").setValue(-1* recvGold);
            user.child("SendGold").setValue(mMyData.getSendHoney());

            user.child("Lon").setValue(lon);
            user.child("Lat").setValue(lat);
            user.child("Dist").setValue(LocationFunc.getInstance().getDistance(lat, lon, REF_LAT, REF_LON,"meter"));

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
        private int count=0;  // 카운트 변수
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

                switch (temp)
                {
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

                try { sleep(count); } // 0.5초간 sleep
                catch (InterruptedException e) {}
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
                        if(index == null)
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

}
