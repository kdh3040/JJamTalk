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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
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

/**
 * Created by mjk on 2017. 8. 4..
 */

public class BuyGoldActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private UIData mUIData = UIData.getInstance();
    private TextView tv_mycoin;
    private Button btn_10, btn_30, btn_50, btn_100, btn_300, btn_500, btn_1000;
    private Button Free_1, Free_2;
    private Activity mActivity;


    ArrayList<HeartItem> list;

    private CommonFunc mCommon = CommonFunc.getInstance();


    private  Boolean bLoadAd = false;


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMyData.mService != null) {
            unbindService(mMyData.mServiceConn);
            mMyData.mRewardedVideoAd = null;
            mMyData.mServiceConn = null;
            mMyData.mService = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        mMyData.SetCurFrag(0);

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

                    loadRewardedVideoAd(getApplicationContext());

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

        Free_1 = (Button)findViewById(R.id.button14);
        Free_1.setVisibility(View.GONE);
        Free_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMyData.mRewardedVideoAd.isLoaded() == false)
                {
                    CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this,"무료 코인 충전", "한 시간에 한번만 충전 할 수 있습니다.");
                }
                else
                {
                    CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                        public void YesListener() {
                            if(CommonFunc.getInstance().IsCurrentDateCompare(new Date(mMyData.GetLastAdsTime()), 60) == false)
                            {
                                // TODO 환웅
                                CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this,"무료 코인 충전", "한 시간에 한번만 충전 할 수 있습니다.");
                                return;
                            }

                            mMyData.SetLastAdsTime(CommonFunc.getInstance().GetCurrentTime());
                            mMyData.mRewardedVideoAd.show();
                        }
                    };

                    CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, listener, null, "무료 코인 충전", "광고를 보시고 " + mUIData.getAdReward()[mMyData.getGrade()] +"코인을 획득 하시겠습니까?", "예", "아니요");
                }
            }
        });

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


        /*ServiceConnection mServiceConn = new_img ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //서비스 연결성공

                mService = IInAppBillingService.Stub.asInterface(service);


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                //서비스 연결해제

                mService = null;

            }
        };//
*/

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

                mMyData.setUserHoney(mMyData.getUserHoney() + finalNPrice);
                mMyData.setPoint(finalNPrice);
                refreshHearCnt();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void loadRewardedVideoAd(Context mContext) {

        if(mMyData.mRewardedVideoAd == null)
        {
            mMyData.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
            mMyData.mRewardedVideoAd.loadAd("ca-app-pub-4020702622451243/3514842138",
                    new AdRequest.Builder().build());
          /*  mMyData.mRewardedVideoAd.loadAd("ca-app-pub-4020702622451243/5818242350",
                    new AdRequest.Builder().build());*/

            loadRewardedVideoAd(mContext);
        }
        else
        {
            mMyData.mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewarded(RewardItem reward) {

                    CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, "보상", mUIData.getAdReward()[mMyData.getGrade()] + "코인을 획득 하였습니다.");
                    mMyData.setUserHoney(mMyData.getUserHoney() + mUIData.getAdReward()[mMyData.getGrade()]);
                    refreshHearCnt();
                    // Reward the user.
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    int aaa = 0;
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    int aaa = 0;
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int errorCode) {
                    int aaa = 0;
                }

                @Override
                public void onRewardedVideoAdLoaded() {
                    int aaa = 0;
                }

                @Override
                public void onRewardedVideoAdOpened() {
                    int aaa = 0;
                }

                @Override
                public void onRewardedVideoStarted() {
                    int aaa = 0;
                }
            });
        }


    }

}
