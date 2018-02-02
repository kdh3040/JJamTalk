package com.hodo.jjamtalk;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Contacts;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Util.CommonFunc;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    ArrayList<String> skuList = new ArrayList<String>();
    Bundle skuDetails = new Bundle();
    Bundle querySkus = new Bundle();
    Bundle buyIntentBundle = new Bundle();
    String[] strGold = new String[8];
    String[] skuGold = {"gold_10", "gold_20", "gold_50", "gold_100", "gold_200", "gold_300", "gold_500", "gold_1000"};
    String sku = null;
    String price = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_charge);

        mActivity = this;
        loadRewardedVideoAd(getApplicationContext());

        txt_heartStatus = (TextView)findViewById(R.id.Heart_MyHeart);
        txt_heartStatus.setText("보유 골드 : " + mMyData.getUserHoney());

        HeartChargeList = (ListView)findViewById(R.id.Heart_list);

        list = new ArrayList<HeartItem>();






        skuList.add("gold_10");
        skuList.add("gold_20");
        skuList.add("gold_50");
        skuList.add("gold_100");
        skuList.add("gold_200");
        skuList.add("gold_300");
        skuList.add("gold_500");
        skuList.add("gold_1000");

        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
        try {
            skuDetails = mMyData.mService.getSkuDetails(3,getPackageName(), "inapp", querySkus);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        int response = skuDetails.getInt("RESPONSE_CODE");
        if (response == 0) {
            ArrayList<String> responseList
                    = skuDetails.getStringArrayList("DETAILS_LIST");

            for (String thisResponse : responseList) {
                JSONObject object = null;
                try {
                    object = new JSONObject(thisResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    sku = object.getString("productId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    price = object.getString("price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (sku.equals("gold_10")) strGold[0] = price;
                else if (sku.equals("gold_20")) strGold[1]= price;
                else if (sku.equals("gold_50")) strGold[2] = price;
                else if (sku.equals("gold_100")) strGold[3] = price;
                else if (sku.equals("gold_200")) strGold[4] = price;
                else if (sku.equals("gold_300")) strGold[5] = price;
                else if (sku.equals("gold_500")) strGold[6] = price;
                else if (sku.equals("gold_1000")) strGold[7] = price;
            }
        }


        HeartItem mHeartItem;

        mHeartItem = new HeartItem(R.drawable.heart_icon, "10골드 900원");
        list.add(mHeartItem);
        mHeartItem = new HeartItem(R.drawable.heart_icon, "20골드 1700원");
        list.add(mHeartItem);
        mHeartItem = new HeartItem(R.drawable.heart_icon, "50골드 4500원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.heart_icon, "100골드 9100원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.heart_icon, "200골드 17500원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.heart_icon, "300골드 26000원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.heart_icon, "500하트 44000원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.heart_icon, "1000골드 80000원");
        list.add(mHeartItem);

        HeartItemAdapter adapter = new HeartItemAdapter(this, R.layout.content_cash_charge, list);

        HeartChargeList.setAdapter(adapter);


        Free_1 = (Button)findViewById(R.id.btn_Free_1);
        Free_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMyData.mRewardedVideoAd.isLoaded() == false)
                {
                    CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this,"무료 골드 충전", "하루에 한번만 충전 할 수 있습니다.");
                }
                else
                {
                    CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                        public void YesListener() {
                            mMyData.mRewardedVideoAd.show();
                        }
                    };

                    CommonFunc.getInstance().ShowDefaultPopup(BuyGoldActivity.this, listener, "무료 골드 충전", "광고를 보시고 " + mUIData.getAdReward()[mMyData.getGrade()] +"골드를 획득 하시겠습니까?", "예", "아니요");
                }
            }
        });

        /*ServiceConnection mServiceConn = new ServiceConnection() {
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMyData.mService != null) {
            unbindService(mMyData.mServiceConn);
        }
    }

    public void refreshHearCnt()
    {
        txt_heartStatus.setText("보유 골드 : " + mMyData.getUserHoney());
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

            final TextView txt = (TextView) view.findViewById(R.id.Charge_Txt);
            txt.setText(arrData.get(i).txt_Heart);

            txt.setOnClickListener(new TextView.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    Toast.makeText(con, txt.getText(), Toast.LENGTH_SHORT).show();

                    String tempStrGold = null;
                    switch (i)
                    {
                        case 0:
                            tempStrGold = skuGold[0];
                            break;
                        case 1:
                            tempStrGold = skuGold[1];
                            break;
                        case 2:
                            tempStrGold = skuGold[2];
                            break;
                        case 3:
                            tempStrGold = skuGold[3];
                            break;
                        case 4:
                            tempStrGold = skuGold[4];
                            break;
                        case 5:
                            tempStrGold = skuGold[5];
                            break;
                        case 6:
                            tempStrGold = skuGold[6];
                            break;
                        case 7:
                            tempStrGold = skuGold[7];
                            break;

                    }

                    try {
                        buyIntentBundle = mMyData.mService.getBuyIntent(3, getPackageName(),
                                tempStrGold, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                    try {
                        startIntentSenderForResult(pendingIntent.getIntentSender(),
                                1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                                Integer.valueOf(0));
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    refreshHearCnt();
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
                    String price = jo.getString("price");

                    mMyData.setUserHoney(mMyData.getUserHoney() +  Integer.parseInt(price));
                    mMyData.setPoint( Integer.parseInt(price));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadRewardedVideoAd(Context mContext) {

        mMyData.mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {

                int aaa = 0;
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
