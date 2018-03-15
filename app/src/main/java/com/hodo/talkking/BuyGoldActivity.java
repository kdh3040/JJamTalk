package com.hodo.talkking;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Util.CommonFunc;

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
    private TextView txt_heartStatus;
    private ListView HeartChargeList;
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
            mMyData.mServiceConn = null;
            mMyData.mService = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
            pref.getInt("Badge", mMyData.badgecount );

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_charge);

        mActivity = this;

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

        loadRewardedVideoAd(getApplicationContext());

        txt_heartStatus = (TextView)findViewById(R.id.Heart_MyHeart);
        txt_heartStatus.setText("보유 코인 : " + mMyData.getUserHoney());

        HeartChargeList = (ListView)findViewById(R.id.Heart_list);

        list = new ArrayList<HeartItem>();
        mMyData.SetCurFrag(0);

        HeartItem mHeartItem;

        mHeartItem = new HeartItem(R.drawable.buy_10, "900원");
        list.add(mHeartItem);
        mHeartItem = new HeartItem(R.drawable.buy_30, "2900원");
        list.add(mHeartItem);
        mHeartItem = new HeartItem(R.drawable.buy_50, "4900원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.buy_100, "9900원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.buy_300, "29000원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.buy_500, "49000원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.buy_1000, "99000원");
        list.add(mHeartItem);


        HeartItemAdapter adapter = new HeartItemAdapter(this, R.layout.content_cash_charge, list);

        HeartChargeList.setAdapter(adapter);


        Free_1 = (Button)findViewById(R.id.btn_Free_1);
        Free_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMyData.mRewardedVideoAd.isLoaded() == false)
                {
                    CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this,"무료 코인 충전", "하루에 한번만 충전 할 수 있습니다.");
                }
                else
                {
                    CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                        public void YesListener() {
                            if(CommonFunc.getInstance().IsCurrentDateCompare(new Date(mMyData.GetLastAdsTime()), 1440) == false)
                            {
                                // TODO 환웅
                                CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this,"무료 코인 충전", "하루에 한번만 충전 할 수 있습니다.");
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
        txt_heartStatus.setText("보유 코인 : " + mMyData.getUserHoney());
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
            nPrice = 10;
        }
        else if(ID.equals(mMyData.skuGold[1]))
        {
            nPrice = 33;
        }
        else if(ID.equals(mMyData.skuGold[2]))
        {
            nPrice = 57;
        }
        else if(ID.equals(mMyData.skuGold[3]))
        {
            nPrice = 120;
        }
        else if(ID.equals(mMyData.skuGold[4]))
        {
            nPrice = 375;
        }
        else if(ID.equals(mMyData.skuGold[5]))
        {
            nPrice = 650;
        }
        else if(ID.equals(mMyData.skuGold[6]))
        {
            nPrice = 1500;
        }


        mMyData.setUserHoney(mMyData.getUserHoney() +  nPrice);
        mMyData.setPoint( nPrice);
        refreshHearCnt();
    }

    public void loadRewardedVideoAd(Context mContext) {

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
