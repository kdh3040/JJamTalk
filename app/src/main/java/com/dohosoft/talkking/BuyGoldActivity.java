package com.dohosoft.talkking;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyUserMetadata;
import com.adcolony.sdk.AdColonyZone;
import com.android.vending.billing.IInAppBillingService;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.fpang.lib.FpangSession;
import com.fpang.lib.SessionCallback;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import static com.dohosoft.talkking.Data.CoomonValueData.REWARD_ADCOLONY;
import static com.dohosoft.talkking.Data.CoomonValueData.REWARD_ADMOB;
import static com.dohosoft.talkking.Data.CoomonValueData.ZONE_ID;
import static com.dohosoft.talkking.Data.CoomonValueData.APP_ID;
import static com.dohosoft.talkking.Data.CoomonValueData.REWARD_UNITY;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class BuyGoldActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private UIData mUIData = UIData.getInstance();
    private TextView tv_mycoin;
    private Button btn_10, btn_30, btn_50, btn_100, btn_300, btn_500, btn_1000;
    private Button Free_Adcolony, Free_Unity, Free_AdMob, Free_AdSync;
    private Activity mActivity;


    ArrayList<HeartItem> list;

    private CommonFunc mCommon = CommonFunc.getInstance();
    private  Boolean bLoadAd = false;

    private UnityAdsListener unityAdsListener = new UnityAdsListener();
    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listener;
    private AdColonyAdOptions ad_options;


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMyData.mService != null) {
            unbindService(mMyData.mServiceConn);
            mMyData.mServiceConn = null;
            mMyData.mService = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        mMyData.SetCurFrag(0);

        if (ad == null || ad.isExpired())
        {
            AdColony.requestInterstitial( CoomonValueData.getInstance().ZONE_ID, listener, ad_options );
        }


        android.app.AlertDialog.Builder mDialog = null;
        mDialog = new android.app.AlertDialog.Builder(this);

        if (!MyData.getInstance().verSion.equals(MyData.getInstance().marketVersion)) {
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

        else
        {
            if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

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

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_charge);

        mActivity = this;
        mMyData.SetCurFrag(0);

        UnityAds.initialize(BuyGoldActivity.this, "1793710", unityAdsListener, true );

        FpangSession.init(BuyGoldActivity.this);
        FpangSession.setUserId (mMyData.getUserIdx());

        AdColonyAppOptions app_options = new AdColonyAppOptions()
                .setUserID( mMyData.getUserIdx());
        AdColony.configure( this, app_options, CoomonValueData.getInstance().APP_ID, CoomonValueData.getInstance().ZONE_ID );

        AdColonyUserMetadata metadata = new AdColonyUserMetadata()
                .setUserAge( 26 )
                .setUserEducation( AdColonyUserMetadata.USER_EDUCATION_BACHELORS_DEGREE )
                .setUserGender( AdColonyUserMetadata.USER_MALE );

        ad_options = new AdColonyAdOptions()
                .enableConfirmationDialog( true )
                .enableResultsDialog( true )
                .setUserMetadata( metadata );



        AdColony.setRewardListener( new AdColonyRewardListener()
        {
            @Override
            public void onReward( AdColonyReward reward )
            {
                /** Query reward object for info here */
                Log.d( CoomonValueData.getInstance().TAG, "onReward" );
            }
        } );


        listener = new AdColonyInterstitialListener()
        {
            /** Ad passed back in request filled callback, ad can now be shown */
            @Override
            public void onRequestFilled( AdColonyInterstitial ad )
            {
                BuyGoldActivity.this.ad = ad;
                Free_Adcolony.setEnabled( true );
                Log.d( CoomonValueData.getInstance().TAG, "onRequestFilled" );
            }

            /** Ad request was not filled */
            @Override
            public void onRequestNotFilled( AdColonyZone zone )
            {
                Log.d( CoomonValueData.getInstance().TAG, "onRequestNotFilled");
            }

            /** Ad opened, reset UI to reflect state change */
            @Override
            public void onOpened( AdColonyInterstitial ad )
            {
                Free_Adcolony.setEnabled( false );
                Log.d( CoomonValueData.getInstance().TAG, "onOpened" );


                CommonFunc.getInstance().ShowLoadingPage(BuyGoldActivity.this, "로딩중");

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

                        CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, "보상", REWARD_ADCOLONY + "코인을 획득 하였습니다.");
                        mMyData.setUserHoney(mMyData.getUserHoney() +REWARD_ADCOLONY);

                        mMyData.setPoint(REWARD_ADCOLONY);
                        refreshHearCnt();

                        loadRewardedVideoAd();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            /** Request a new ad if ad is expiring */
            @Override
            public void onExpiring( AdColonyInterstitial ad )
            {
                Free_Adcolony.setEnabled( false );
                AdColony.requestInterstitial( CoomonValueData.getInstance().ZONE_ID, this, ad_options );
                Log.d( CoomonValueData.getInstance().TAG, "onExpiring" );
            }
        };


        mMyData.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(BuyGoldActivity.this);
        mMyData.mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {


                CommonFunc.getInstance().ShowLoadingPage(BuyGoldActivity.this, "로딩중");

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

                        CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, "보상", REWARD_ADMOB + "코인을 획득 하였습니다.");
                        mMyData.setUserHoney(mMyData.getUserHoney() +REWARD_ADMOB);

                        mMyData.setPoint(REWARD_ADMOB);
                        refreshHearCnt();

                        loadRewardedVideoAd();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





                // Reward the user.
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                int aaa = 0;
                aaa ++;
            }

            @Override
            public void onRewardedVideoAdClosed() {
                int aaa = 0;
                aaa ++;
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                int aaa = 0;
                aaa ++;
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                int aaa = 0;
                aaa ++;
            }

            @Override
            public void onRewardedVideoAdOpened() {
                int aaa = 0;
                aaa ++;
            }

            @Override
            public void onRewardedVideoStarted() {
                int aaa = 0;
                aaa ++;
            }
        });


        loadRewardedVideoAd();

        /*mMyData.mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());*/

/*        mMyData.mRewardedVideoAd.loadAd("ca-app-pub-4020702622451243/3514842138",
                new AdRequest.Builder().build());*/



        if(mMyData.mServiceConn == null) {
            mMyData.mServiceConn = new ServiceConnection() {
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mMyData.mService = null;
                }

                @Override
                public void onServiceConnected(ComponentName name,
                                               IBinder service) {
                    mMyData.mService = IInAppBillingService.Stub.asInterface(service);

                    //loadRewardedVideoAd(BuyGoldActivity.this);

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
                            if (mMyData.sku.equals("gold_10")) mMyData.strGold[0] = mMyData.price;
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



        tv_mycoin = (TextView)findViewById(R.id.tv_mycoin);
        tv_mycoin.setText("보유 코인 : " + mMyData.getUserHoney());

        mMyData.SetCurFrag(0);


        Free_AdMob = (Button)findViewById(R.id.button14);
        Free_AdMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMyData.mRewardedVideoAd.isLoaded() == false)
                {
                    CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this,"무료 코인 충전", "광고 준비중입니다");
                }
                else
                {
                    if(CommonFunc.getInstance().IsCurrentDateCompare(new Date(mMyData.GetLastAdsTime()), 30, 1) == false)
                    {
                        // TODO 환웅
                        CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this,"무료 코인 충전", "30초에 한번씩 충전 할 수 있습니다.");
                        return;
                    }
                    else
                    {
                        CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                            public void YesListener() {

                                mMyData.SetLastAdsTime(CommonFunc.getInstance().GetCurrentTime());
                                mMyData.mRewardedVideoAd.show();
                            }
                        };

                        CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, listener, null, "무료 코인 충전", "광고를 보시고 " + REWARD_ADMOB +"코인을 획득 하시겠습니까?", "예", "아니요");
                    }

                }
            }
        });

        Free_Adcolony = (Button)findViewById(R.id.button16);
        Free_Adcolony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();

            }
        });
        Free_Unity = (Button)findViewById(R.id.button15);
        Free_Unity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!UnityAds.isReady("rewardedVideo"))
                {
                    CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this,"무료 코인 충전", "광고 준비중입니다");
                }
                else
                {
                    if(CommonFunc.getInstance().IsCurrentDateCompare(new Date(mMyData.UnityAdsTime), 30, 1) == false)
                    {
                        // TODO 환웅
                        CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this,"무료 코인 충전", "30초에 한번씩 충전 할 수 있습니다.");
                        return;
                    }
                    else
                    {
                        CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                            public void YesListener() {

                                mMyData.UnityAdsTime = CommonFunc.getInstance().GetCurrentTime();
                                UnityAds.show(BuyGoldActivity.this, "rewardedVideo");

                                //mMyData.mRewardedVideoAd.show();
                            }
                        };

                        CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, listener, null, "무료 코인 충전", "광고를 보시고 " + REWARD_UNITY +"코인을 획득 하시겠습니까?", "예", "아니요");
                    }

                }
            }
        });
        Free_AdSync = (Button)findViewById(R.id.button17);
        Free_AdSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FpangSession.showAdsyncList (BuyGoldActivity.this,"무료골드");
            }
        });
        FpangSession.withdrawUserPointTest (BuyGoldActivity.this, new SessionCallback(){
            public void onResult(Context ctx, Object result) {
                final int point = (int)result;
                if (point >= 0) {

                    CommonFunc.getInstance().ShowLoadingPage(BuyGoldActivity.this, "로딩중");

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
                            CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, "보상", point + "코인을 획득 하였습니다.");
                            mMyData.setUserHoney(mMyData.getUserHoney() + point);
                            mMyData.setPoint(point);
                            refreshHearCnt();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    // mMyData.setUserHoney(mMyData.getUserHoney() + point);
                    //setPoint(point);
                }     else {
                    Log.e("AdSync", "조회 오류");
                }          // wait indicator 닫기
            }
        }, "Test");


        btn_10 = (Button)findViewById(R.id.btn_10);
        btn_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(getApplicationContext(), mMyData.skuGold[0]);
            }
        });

        btn_30 = (Button)findViewById(R.id.btn_30);
        btn_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(getApplicationContext(), mMyData.skuGold[1]);
            }
        });

        btn_50 = (Button)findViewById(R.id.btn_50);
        btn_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(getApplicationContext(),mMyData.skuGold[2]);
            }
        });

        btn_100 = (Button)findViewById(R.id.btn_100);
        btn_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(getApplicationContext(),mMyData.skuGold[3]);
            }
        });

        btn_300 = (Button)findViewById(R.id.btn_300);
        btn_300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(getApplicationContext(),mMyData.skuGold[4]);
            }
        });

        btn_500 = (Button)findViewById(R.id.btn_500);
        btn_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(getApplicationContext(),mMyData.skuGold[5]);
            }
        });

        btn_1000 = (Button)findViewById(R.id.btn_1000);
        btn_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyGoldByGoogle(getApplicationContext(),mMyData.skuGold[6]);
            }
        });
    }


    public void refreshHearCnt()
    {
        tv_mycoin.setText("보유 코인 : " + mMyData.getUserHoney());
    }


    class HeartItem {

        int Img_Heart;
        String txt_Heart;

        //회색글자 처리 뜸 원인불명
        public HeartItem(int Icon, String Name) {

            Img_Heart = Icon;
            txt_Heart = Name;
        }
    }

    class HeartItemAdapter extends BaseAdapter{
        Context con;
        LayoutInflater inflater;
        ArrayList<HeartItem> arrData;
        int nLayout;

        private  MyData mMyData = MyData.getInstance();
        public HeartItemAdapter(Context context, int layout, ArrayList<HeartItem> arrD)
        {
            con = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arrData = arrD;
            nLayout = layout;
        }

        @Override
        public int getCount(){
            return arrData.size();
        }

        @Override
        public Object getItem(int i) {
            return arrData.get(i).txt_Heart;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if(view == null)
                view = inflater.inflate(nLayout, viewGroup, false);

            ImageView img = (ImageView)view.findViewById(R.id.Charge_Img);
            img.setImageResource(arrData.get(i).Img_Heart);
            final Button btn = (Button)view.findViewById(R.id.charge_Txt);

            btn.setText(arrData.get(i).txt_Heart);

            btn.setOnClickListener(new TextView.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(con, txt.getText(), Toast.LENGTH_SHORT).show();

                    String tempStrGold = null;
                    switch (i)
                    {
                        case 0:
                            tempStrGold = mMyData.skuGold[0];
                            break;
                        case 1:
                            tempStrGold = mMyData.skuGold[1];
                            break;
                        case 2:
                            tempStrGold = mMyData.skuGold[2];
                            break;
                        case 3:
                            tempStrGold = mMyData.skuGold[3];
                            break;
                        case 4:
                            tempStrGold = mMyData.skuGold[4];
                            break;
                        case 5:
                            tempStrGold = mMyData.skuGold[5];
                            break;
                        case 6:
                            tempStrGold = mMyData.skuGold[6];
                            break;


                    }

                    try {
                        mMyData.buyIntentBundle = mMyData.mService.getBuyIntent(3, getPackageName(),
                                tempStrGold, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    mMyData.pendingIntent = mMyData.buyIntentBundle.getParcelable("BUY_INTENT");
                    if(mMyData.pendingIntent != null)
                    {
                        try {
                            startIntentSenderForResult(mMyData.pendingIntent.getIntentSender(),
                                    1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                                    Integer.valueOf(0));
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });

            return view;
        }
    }

    private void BuyGoldByGoogle(Context context, String tempStrGold)
    {
        try {
            if(mMyData.mService == null)
            {
                CommonFunc.getInstance().ShowToast(context, "결제에 실패하였습니다. 다시 시도해주세요.", true);
                return;
            }
            mMyData.buyIntentBundle = mMyData.mService.getBuyIntent(3, getPackageName(),
                    tempStrGold, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(mMyData.buyIntentBundle == null)
        {
            CommonFunc.getInstance().ShowToast(context, "결제에 실패하였습니다. 다시 시도해주세요.", true);
            return;
        }

        mMyData.pendingIntent = mMyData.buyIntentBundle.getParcelable("BUY_INTENT");
        if(mMyData.pendingIntent != null)
        {
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
                    setBuyGold(sku);

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int response = mMyData.mService.consumePurchase(3, getPackageName(), strToken);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });



                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setBuyGold(String ID)
    {
        int nPrice = 0;
        if(ID.equals(mMyData.skuGold[0]))
        {
            nPrice = 100;
        }
        else if(ID.equals(mMyData.skuGold[1]))
        {
            nPrice = 330;
        }
        else if(ID.equals(mMyData.skuGold[2]))
        {
            nPrice = 570;
        }
        else if(ID.equals(mMyData.skuGold[3]))
        {
            nPrice = 1200;
        }
        else if(ID.equals(mMyData.skuGold[4]))
        {
            nPrice = 3750;
        }
        else if(ID.equals(mMyData.skuGold[5]))
        {
            nPrice = 6500;
        }
        else if(ID.equals(mMyData.skuGold[6]))
        {
            nPrice = 15000;
        }

        CommonFunc.getInstance().ShowLoadingPage(BuyGoldActivity.this, "로딩중");

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
                CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, "보상", finalNPrice + "코인을 획득 하였습니다.");
                mMyData.setUserHoney(mMyData.getUserHoney() + finalNPrice);
                mMyData.setPoint(finalNPrice);
                refreshHearCnt();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void loadRewardedVideoAd() {
        mMyData.mRewardedVideoAd.loadAd("ca-app-pub-4020702622451243/3514842138",
                new AdRequest.Builder().build());

    }


    public class UnityAdsListener implements IUnityAdsListener{

        @Override
        public void onUnityAdsReady(String s) {


        }

        @Override
        public void onUnityAdsStart(String s) {

        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {

            if(finishState != UnityAds.FinishState.SKIPPED)
            {

                CommonFunc.getInstance().ShowLoadingPage(BuyGoldActivity.this, "로딩중");

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
                        CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, "보상", REWARD_UNITY + "코인을 획득 하였습니다.");
                        mMyData.setUserHoney(mMyData.getUserHoney() + REWARD_UNITY);
                        mMyData.setPoint(REWARD_UNITY);
                        refreshHearCnt();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

        }
    }
}
