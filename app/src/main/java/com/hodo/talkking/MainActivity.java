package com.hodo.talkking;

import android.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.hodo.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;
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
            Intent intent = new Intent(mActivity, MainActivity.class);
            intent.putExtra("StartFragment", 0);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
            mActivity.finish();
            mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
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
            int pid = android.os.Process.myPid(); android.os.Process.killProcess(pid);
        }

        if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

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


        if(mMyData.getUserIdx() == null)
        {
            int pid = android.os.Process.myPid(); android.os.Process.killProcess(pid);
        }

/*        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle Analyticsbundle = new Bundle();
        Analyticsbundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        Analyticsbundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Name");
        Analyticsbundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, Analyticsbundle);*/

      //  AddDummy(1000);

        mMyData.mContext = getApplicationContext();
        mMyData.mActivity = mActivity;

        if(mMyData.arrReportList.size() >= 10)
            ViewReportPop();

        if ( mMyData.badgecount >= 1)
        {
            mMyData.badgecount = 0;
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count_package_name", "com.hodo.talkking");
            intent.putExtra("badge_count_class_name", "com.hodo.talkking.LoginActivity");
            intent.putExtra("badge_count", mMyData.badgecount);
            sendBroadcast(intent);
        }

        Bundle bundle = getIntent().getExtras();
        nStartFragment = (int) bundle.getSerializable("StartFragment");

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
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
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
                overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
            }
        });

        CommonFunc.getInstance().Honey_Box = (ImageView)findViewById(R.id.iv_honeybox);
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



                //  startActivity(new Intent(getApplicationContext(),MainSettingActivity.class));
                //  overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View v = LayoutInflater.from(mActivity).inflate(R.layout.category_popup,null,false);

                builder.setView(v);
                final AlertDialog filter_dialog = builder.create();
                filter_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                        mMyData.setSettingData(mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration());
                        mFireBaseData.SaveSettingData(mMyData.getUserIdx(), mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.IsRecyMsgRejectSetting(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration());
                        filter_dialog.dismiss();

                        SortDataAge sortData = new SortDataAge();
                        sortData.execute();

               /*         Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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

      /*  Prepare prepareJob = new Prepare();

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
            txt_Body.setText("매일 "+mUIData.getAdReward()[mMyData.getGrade()]+"골드 추가");

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
        ib_pcr_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMyData.nPublicRoomStatus == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_pcr_open,null,false);
                    //v.getBackground().setAlpha(0);


                    final RadioButton btn_Member_50 =  v.findViewById(R.id.member_50);
                    final RadioButton btn_Member_100 =  v.findViewById(R.id.member_100);
                    final RadioButton btn_Member_200 =  v.findViewById(R.id.member_200);

                    final int[] publicRoomMemberCnt = new int[1];

                    final RadioButton btn_time_30 =  v.findViewById(R.id.time_30);
                    final RadioButton btn_time_60 =  v.findViewById(R.id.time_60);
                    final RadioButton btn_time_120 =  v.findViewById(R.id.time_120);

                    final int[] publicRoomTime = new int[1];

                    RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener(){

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
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    Button btn_open_pcr = v.findViewById(R.id.btn_open_pcr);
                    btn_open_pcr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            boolean rtValuew = mMyData.makePublicRoom(publicRoomMemberCnt[0], publicRoomTime[0]);

                            Intent intent= new Intent(getApplicationContext(), PublicChatRoomHostActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                }


                else
                {
                    Intent intent= new Intent(getApplicationContext(), PublicChatRoomHostActivity.class);
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

                if(view.isSelected()){
                    int a = 0;

                }else{
                    int b = 0;
                }
                //startActivity(new Intent(getApplicationContext(),CardListActivity.class));
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

                //startActivity(new Intent(getApplicationContext(),ChatListActivity.class));
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
                txt_title.setText("나의 팬");
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

                //startActivity(new Intent(getApplicationContext(),FanActivity.class));
         /*       Intent intent = new Intent(getApplicationContext(), FanActivity.class);
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

                //startActivity(intent);
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);



            }
        });


        /*PagerModelManager manager = new PagerModelManager();
        manager.addFragment(new Rank_NearFragment(),"가까운 순");



        manager.addFragment(new Rank_GoldReceiveFragment(),"실시간 인기 순");
        manager.addFragment(new Rank_FanRichFragment(),"팬 보유 순");
        manager.addFragment(new Rank_NewMemberFragment(),"새로운 순");

        final ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);
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



                setImageAlpha(255,100,100,100,100);
                iv_myPage.setVisibility(View.VISIBLE);
                txt_title.setVisibility(TextView.GONE);

                break;
            case 1:
                if(cardListFragment == null)
                    LoadCardData();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cardListFragment, "CardListFragment").commit();

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

                setImageAlpha(100,100,100,255,100);
                iv_myPage.setVisibility(View.GONE);
                txt_title.setVisibility(TextView.VISIBLE);
                txt_title.setText("나의 팬");
                break;
            case 4:
                if(boardFragment == null)
                    boardFragment = new BoardFragment();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,boardFragment, "BoardFragment").commit();

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

                setImageAlpha(255,100,100,100,100);
                break;
        }
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
                //fanFragment = new FanFragment();
                fanFragment = new FanListFragment();
        }

        for(int i = 0; i < mMyData.arrMyStarList.size(); i++)
        {
            Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.arrMyStarList.get(i).Idx);
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                    mMyData.arrMyStarDataList.put(mMyData.arrMyStarList.get(finalI).Idx, DBData);

                    if(mMyData.arrMyStarDataList.size() == mMyData.arrMyStarList.size())
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
                int pid = android.os.Process.myPid(); android.os.Process.killProcess(pid);
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

        ib_home.setImageResource(R.drawable.icon_home);
        ib_cardList.setImageResource(R.drawable.favor);
        ib_chatList.setImageResource(R.drawable.chat);
        ib_fan.setImageResource(R.drawable.myfan);
        ib_board.setImageResource(R.drawable.board);

        switch (idx)
        {
            case 0:
                ib_home.setImageResource(R.drawable.icon_home_dark);
                break;
            case 1:
                ib_cardList.setImageResource(R.drawable.favor_dark);
                break;
            case 2:
                ib_chatList.setImageResource(R.drawable.chat_pink);
                break;
            case 3:
                ib_fan.setImageResource(R.drawable.myfan_dark);
                break;
            case 4:
                ib_board.setImageResource(R.drawable.board_dark);
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

        RandImg[0] = "http://t1.daumcdn.net/liveboard/samanco/201508/1440308700523.jpg";
        RandImg[1] = "http://sccdn.chosun.com/news/html/2014/02/24/20140225010024585001677110.jpg";
        RandImg[2] = "http://ojsfile.ohmynews.com/STD_IMG_FILE/2015/1030/IE001887744_STD.jpg";
        RandImg[3] = "https://image.mycelebs.com/celeb/sq/338_sq_01.jpg";
        RandImg[4] = "https://www.fashionseoul.com/wp-content/uploads/2017/09/201709018_PARK-2.jpg";
        RandImg[5] = "http://topclass.chosun.com/news_img/1801/1801_008_1.jpg";
        RandImg[6] = "http://www.munhwanews.com/news/photo/201710/84896_140798_3411.png";
        RandImg[7] = "http://img.insight.co.kr/static/2017/10/10/700/dohwc3nz5310m669r6h0.jpg";

        RandImg[8] = "http://pds.joins.com/news/component/htmlphoto_mmdata/201702/28/1bbf3439-8fd7-4e9b-9350-49611fc80773.jpg";
        RandImg[9] = "http://img.etoday.co.kr/pto_db/2017/09/20170923031414_1130037_600_818.jpg";
        RandImg[10] = "http://pds.joins.com/news/component/htmlphoto_mmdata/201704/19/32965f70-b593-4ff7-8457-b17a7942f1b5.jpg";
        RandImg[11] = "http://mn.kbs.co.kr/data/news/2017/06/30/3507703_6DL.jpg";
        RandImg[12] = "http://thumb.mt.co.kr/06/2017/12/2017120809455886841_2.jpg";
        RandImg[13] = "http://img.insight.co.kr/static/2016/12/09/700/56T850II6R5Z9XMSV8OU.jpg";
        RandImg[14] = "http://pds.joins.com/news/component/htmlphoto_mmdata/201107/04/htm_2011070423063950005010-001.JPG";
        RandImg[15] = "http://www.breaknews.com/imgdata/breaknews_com/201310/2013102320493836.jpg";


        RandImg[16] = "http://www.xportsnews.com/contents/images/upload/article/2018/0207/1517989405454336.jpg";
        RandImg[17] = "http://img.tenasia.hankyung.com/webwp_kr/wp-content/uploads/2017/02/2017020915275410864-540x768.jpg";
        RandImg[18] = "http://news20.busan.com/content/image/2017/12/31/20171231000035_0.jpg";
        RandImg[19] = "http://t1.daumcdn.net/movie/gaia/e5a448f6b23d007098671cad031034b1362e5ce5";
        RandImg[20] = "data:image/jpeg;base64,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";
        RandImg[21] = "http://static.hubzum.zumst.com/hubzum/2017/07/20/13/cfabf934227d4075971e039ba956eb3d.jpg";
        RandImg[22] = "http://cdnweb01.wikitree.co.kr/webdata/editor/201504/06/img_20150406162556_2f7b3842.jpg";
        RandImg[23] = "http://pds.joins.com/news/component/htmlphoto_mmdata/201702/27/e2368163-f9ce-4a72-b888-d94b3bd323d9.jpg";

        RandImg[24] = "http://cfile21.uf.tistory.com/image/999C293359F1A9BE072954";
        RandImg[25] = "http://fimg2.pann.com/new/download.jsp?FileID=23967595";
        RandImg[26] = "http://mblogthumb4.phinf.naver.net/20160221_275/cindy________1455992120406YLDrt_JPEG/61.jpg?type=w2";
        RandImg[27] = "http://fimg2.pann.com/new/download.jsp?FileID=23159739";
        RandImg[28] = "http://cfile221.uf.daum.net/image/1247444050A990B4274981";
        RandImg[29] = "http://fimg2.pann.com/new/download.jsp?FileID=24086349";
        RandImg[30] = "http://image.kdramastars.com/data/images/full/222735/jung-woo-sung.jpg";
        RandImg[31] = "http://www.discovery-expedition.co.kr/shop6/shop/data/editor/1339463187.png";


        for(int i = 0; i < Count; i++)
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());


            Random random = new Random();

            // DatabaseReference user = table.child( userIdx);
            String Index = Integer.toString(171 + i);
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

            user.child("Lon").setValue(mMyData.getUserLon());
            user.child("Lat").setValue(mMyData.getUserLat());

            user.child("SendCount").setValue(mMyData.getSendHoney());
            user.child("RecvCount").setValue(mMyData.getRecvHoney());

            user.child("ImgCount").setValue(mMyData.getUserImgCnt());

            long time = CommonFunc.getInstance().GetCurrentTime();
            SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
            user.child("Date").setValue(ctime.format(new Date(time)));

            user.child("Memo").setValue(mMyData.getUserMemo());

            user.child("RecvMsgReject").setValue(mMyData.nRecvMsgReject ? 1 : 0);


            user.child("FanCount").setValue(-1 * random.nextInt(10));

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

            user.child("RecvGold").setValue(mMyData.getRecvHoney());
            user.child("SendGold").setValue(mMyData.getSendHoney());

            user.child("Lon").setValue(mMyData.getUserLon());
            user.child("Lat").setValue(mMyData.getUserLat());


            user.child("Date").setValue(ctime.format(new Date(time)));
            user.child("FanCount").setValue(-1 * random.nextInt(10));

            user.child("Point").setValue(random.nextInt(10000));

            user.child("Grade").setValue(random.nextInt(5));
            user.child("BestItem").setValue(random.nextInt(7));
            user.child("Honey").setValue(random.nextInt(10000));


        }

        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        DatabaseReference data = fierBaseDataInstance.getReference("UserCount");
        data.setValue(170+Count);
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


}
