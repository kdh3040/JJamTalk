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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
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

import java.util.ArrayList;
import java.util.Date;

import static com.hodo.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;

public class MainActivity extends AppCompatActivity {

    ImageView ib_cardList;
    ImageView ib_chatList;
    ImageView ib_board;
    ImageView iv_myPage;
    TextView txt_title;
    ImageView ib_fan;
    //ImageButton ib_pcr_open;
    ImageButton ib_buy_jewel;
    ImageView ib_home;
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
                if(CommonFunc.getInstance().IsCurrentDateCompare(new Date(mMyData.GetLastBoardWriteTime()), 15) == false)
                {
                    // TODO 환웅
                    CommonFunc.getInstance().ShowDefaultPopup(MainActivity.this, "게시판 작성", "연속으로 게시판을 작성 할 수 없습니다.");
                    return;
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
                ib_home.setImageResource(R.drawable.icon_home_dark);

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
                ib_board.setImageResource(R.drawable.board_dark);


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
                ib_cardList.setImageResource(R.drawable.favor_dark);

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
                ib_chatList.setImageResource(R.drawable.chat_dark);
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
                ib_fan.setImageResource(R.drawable.myfan_dark);
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


}
