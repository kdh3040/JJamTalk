package com.hodo.talkking.Data;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.reward.RewardedVideoAd;
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
import com.hodo.talkking.ChatRoomActivity;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.R;
import com.hodo.talkking.UserPageActivity;
import com.hodo.talkking.Util.CommonFunc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.hodo.talkking.MainActivity.mFragmentMng;

/**
 * Created by boram on 2017-08-04.
 */

public class MyData {

    private static MyData _Instance;
    private BlockData blockList;
    private int fanCount;
    //  private FirebaseData mFireBase = FirebaseData.getInstance();


    public static MyData getInstance() {
        if (_Instance == null)
            _Instance = new MyData();

        return _Instance;
    }

    public ArrayList<SimpleUserData> arrUserMan_Near = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserWoman_Near = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserAll_Near = new ArrayList<>();

    public ArrayList<SimpleUserData> arrUserMan_New = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserWoman_New = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserAll_New = new ArrayList<>();

    public ArrayList<SimpleUserData> arrUserMan_Send = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserWoman_Send = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserAll_Send = new ArrayList<>();

    public ArrayList<SimpleUserData> arrUserMan_Recv = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserWoman_Recv = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserAll_Recv = new ArrayList<>();


    public ArrayList<SimpleUserData> arrUserMan_Near_Age = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserWoman_Near_Age = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserAll_Near_Age = new ArrayList<>();

    public ArrayList<SimpleUserData> arrUserMan_New_Age = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserWoman_New_Age = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserAll_New_Age = new ArrayList<>();

    public ArrayList<SimpleUserData> arrUserMan_Send_Age = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserWoman_Send_Age = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserAll_Send_Age = new ArrayList<>();

    public ArrayList<SimpleUserData> arrUserMan_Recv_Age = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserWoman_Recv_Age = new ArrayList<>();
    public ArrayList<SimpleUserData> arrUserAll_Recv_Age = new ArrayList<>();


    public  Map<String, UserData> mapChatTargetData = new LinkedHashMap<String, UserData>();

    public int nFanCount;
    public ArrayList<FanData> arrMyFanList = new ArrayList<>();
    public  Map<String, FanData> arrMyFanRecvList = new LinkedHashMap<String, FanData>();
    public  Map<String, SimpleUserData> arrMyFanDataList = new LinkedHashMap<String, SimpleUserData>();
    public  Map<String, UserData> mapMyFanData = new LinkedHashMap<String, UserData>();

    public ArrayList<StarData> arrMyStarList = new ArrayList<>();
    public  Map<String, SimpleUserData> arrMyStarDataList = new LinkedHashMap<String, SimpleUserData>();
    public  Map<String, UserData> mapMyStarData = new LinkedHashMap<String, UserData>();

    private String strUid;
    private String strIdx;
    private String strToken;

    private String strImg;

    private String strNick;
    private String strGender;
    private String strAge;

    private double lLat;
    private double lLon;

    public int nHoney;
    public int nSendCount;
    public int nRecvCount;

    public String strDate;

    private String strMemo;

    public int nSearchMode = 0;
    public boolean nAlarmSetting_Sound = false;
    public boolean nAlarmSetting_Vibration = false;
    public int nViewMode = 1;
    public boolean nRecvMsgReject = false;

    public int nPublicRoomStatus = 0;
    public int nPublicRoomName = 0;
    public int nPublicRoomLimit = 0;
    public int nPublicRoomTime = 0;

    public int nImgCount;
    public String[] strProfileImg = new String[4];

    public int nItemCount;
    public int item_1;
    public int item_2;
    public int item_3;
    public int item_4;
    public int item_5;
    public int item_6;
    public int item_7;
    public int item_8;

    public int bestItem;

    public ArrayList<String> arrBlockNameList = new ArrayList<>();
    public ArrayList<BlockData> arrBlockDataList = new ArrayList<>();

    public ArrayList<BlockData> arrBlockedDataList = new ArrayList<>();

    public ArrayList<String> arrGiftHoneyNameList = new ArrayList<>();
    public ArrayList<SendData> arrGiftHoneyDataList = new ArrayList<>();
    public ArrayList<UserData> arrGiftUserDataList = new ArrayList<>();

    public ArrayList<String> arrRecvHoneyNameList = new ArrayList<>();
    public ArrayList<SendData> arrRecvHoneyDataList = new ArrayList<>();

    public ArrayList<String> arrSendHoneyNameList = new ArrayList<>();
    public ArrayList<SendData> arrSendHoneyDataList = new ArrayList<>();

    public ArrayList<String> arrChatNameList = new ArrayList<>();
    public Map<String, SimpleChatData> arrChatDataList = new LinkedHashMap<String, SimpleChatData>();

    public ArrayList<String> arrCardNameList = new ArrayList<>();
    public  Map<String, SimpleUserData> arrCarDataList = new LinkedHashMap<String, SimpleUserData>();
    public  Map<String, UserData> mapMyCardData = new LinkedHashMap<String, UserData>();

    public Map<Integer, Integer> itemList = new HashMap<Integer, Integer>();
    public ArrayList<Integer> itemIdx = new ArrayList<>();

    public  Uri urSaveUri;
    public  int nSaveUri;

    public  String strImgLodingUri;
    public  String strDownUri;
    public  String strBannerID = "ca-app-pub-8954582850495744/1996257938";

    public  boolean bChatRefresh = false;

    private int nCurVisibleFrag;
    public  int nMyAge;

    public int Point;
    public int Grade;

    public int ConnectDate;
    public long LastBoardWriteTime;
    public long LastAdsTime;

    public int nStartAge, nEndAge;

    public RewardedVideoAd mRewardedVideoAd;
    public RewardedVideoAd mRewardedVideoAd2;

    public  IInAppBillingService mService;
    public  ServiceConnection mServiceConn;

    public ArrayList<String> skuList = new ArrayList<String>();
    public Bundle skuDetails = new Bundle();
    public Bundle querySkus = new Bundle();
    public Bundle buyIntentBundle = new Bundle();
    public PendingIntent pendingIntent;
    public String[] strGold = new String[7];
    public String[] skuGold = {"gold_10", "gold_20", "gold_50", "gold_100", "gold_200", "gold_500", "gold_1000"};
    public  String sku = null;
    public String price = null;

    public Context mContext;
    public Activity mActivity;

    public int nReportedCnt;
    public ArrayList<ReportedData> arrReportList = new ArrayList<>();

    public String ANDROID_ID;
    public volatile  static UUID uuid;


    public int badgecount;

    public int NickChangeCnt;

    private MyData() {
        strUid = null;
        strImg = null;
        strNick = null;
        strGender = null;
        strAge = null;
        lLon = 0f;
        lLat = 0f;
        nHoney = 0;
        strDate = null;


        for (int i = 0; i < 4; i++) {
            strProfileImg[i] = "1";
        }

        nFanCount = 0;
        nItemCount = 0;
        item_1 = 0;
        item_2 = 0;
        item_3 = 0;
        item_4 = 0;
        item_5 = 0;
        item_6 = 0;
        item_7 = 0;
        item_8 = 0;
//        strProfileImg = null;

        strMemo = null;

        nCurVisibleFrag = 0;

        Point = 0;
        ConnectDate = 0;

        for(int i =0; i < 8; i++)
        {
            itemList.put( i, 0);
        }

        nReportedCnt = 0;
        badgecount = 0;

        mService = null;
        mServiceConn= null;

        NickChangeCnt = 0;
    }

    public void Clear()
    {
        arrUserMan_Near.clear();
        arrUserWoman_Near.clear();
        arrUserAll_Near .clear();

        arrUserMan_New.clear();
        arrUserWoman_New.clear();
        arrUserAll_New.clear();

        arrUserMan_Send.clear();
        arrUserWoman_Send.clear();
        arrUserAll_Send.clear();

        arrUserMan_Recv.clear();
        arrUserWoman_Recv.clear();
        arrUserAll_Recv.clear();


        arrUserMan_Near_Age.clear();
        arrUserWoman_Near_Age.clear();
        arrUserAll_Near_Age.clear();

        arrUserMan_New_Age.clear();
        arrUserWoman_New_Age.clear();
        arrUserAll_New_Age.clear();

        arrUserMan_Send_Age.clear();
        arrUserWoman_Send_Age.clear();
        arrUserAll_Send_Age.clear();

        arrUserMan_Recv_Age.clear();
        arrUserWoman_Recv_Age.clear();
        arrUserAll_Recv_Age.clear();
        
        mapChatTargetData.clear();

        arrMyFanList.clear();
        arrMyFanDataList.clear();
        mapMyFanData.clear();

        arrMyStarList.clear();
        arrMyStarDataList.clear();
        mapMyStarData.clear();


        arrBlockNameList.clear();
        arrBlockDataList.clear();

        arrBlockedDataList.clear();

        arrGiftHoneyNameList.clear();
        arrGiftHoneyDataList.clear();
        arrGiftUserDataList.clear();

        arrRecvHoneyNameList.clear();
        arrRecvHoneyDataList.clear();

        arrSendHoneyNameList.clear();
        arrSendHoneyDataList.clear();

        arrChatNameList.clear();
        arrChatDataList.clear();

        arrCardNameList.clear();
        arrCarDataList.clear();
        mapMyCardData.clear();


        nFanCount = 0;
        
        strUid = null;
        strIdx= null;
        strToken= null;

        strImg= null;

        strNick= null;
        strGender= null;
        strAge= null;

        lLat = 0;
        lLon= 0;

        nHoney= 0;
        nSendCount = 0;
        nRecvCount = 0;

        strDate = null;

        strMemo = null;

        nSearchMode = 0;
        nAlarmSetting_Sound = false;
        nAlarmSetting_Vibration = false;
        nViewMode = 1;
        nRecvMsgReject = false;

        nImgCount = 0;
        for(int i = 0 ; i < 4; i++)
        {
            strProfileImg[i] = "1";
        }


        nItemCount = 0;
        item_1 = 0;
        item_2 = 0;
        item_3 = 0;
        item_4 = 0;
        item_5 = 0;
        item_6 = 0;
        item_7 = 0;
        item_8 = 0;

        bestItem = 0;

        for(int i =0; i < 8; i++)
        {
            itemList.put( i, 0);
        }

        itemIdx = null;

        NickChangeCnt = 0;
    }


    public void setMyData(String _UserUid, String _UserIdx, int _UserImgCount, String _UserImg, String _UserImgGroup0, String _UserImgGroup1, String _UserImgGroup2, String _UserImgGroup3,
                          String _UserNick, String _UserGender, String _UserAge, Double _UserLon, Double _UserLat,
                          int _UserHoney, int _UserSendCount, int _UserRecvCount, String _UserDate,
                          String _UserMemo, int _UserRecvMsgReject, int _UserPublicRoomStatus , int _UserPublicRoomName, int _UserPublicRoomLimit, int _UserPublicRoomTime,
                          int _UserItemCount, int _UserItem1, int _UserItem2, int _UserItem3, int _UserItem4, int _UserItem5, int _UserItem6, int _UserItem7, int _UserItem8, int _UserBestItem,
                          int _UserPoint, int _UserGrade, int _UserConnDate, long _UserLastBoardWriteTime, long _UserLastAdsTime, int _UserNickChangeCnt) {

        strUid = _UserUid;
        strIdx = _UserIdx;
        strToken = FirebaseInstanceId.getInstance().getToken();

        //strToken = com.google.firebase.iid.FirebaseInstanceId.getInstance().getToken();


        strNick = _UserNick;
        strGender = _UserGender;
        strAge = _UserAge;
        lLon = _UserLon;
        lLat = _UserLat;
        nHoney = _UserHoney;
        strDate = _UserDate;

        strMemo = _UserMemo;

        nSendCount = _UserSendCount;
        nRecvCount = _UserRecvCount;

        nImgCount = _UserImgCount;

        strImg = _UserImg;

        if(_UserImgGroup0 == null || _UserImgGroup0.equals(""))
            _UserImgGroup0 = "1";
        if(_UserImgGroup1 == null || _UserImgGroup1.equals(""))
            _UserImgGroup1 = "1";
        if(_UserImgGroup2 == null || _UserImgGroup2.equals(""))
            _UserImgGroup2 = "1";
        if(_UserImgGroup3 == null ||_UserImgGroup3.equals(""))
            _UserImgGroup3 = "1";

        strProfileImg[0] = _UserImgGroup0;
        strProfileImg[1] = _UserImgGroup1;
        strProfileImg[2] = _UserImgGroup2;
        strProfileImg[3] = _UserImgGroup3;

        nRecvMsgReject = _UserRecvMsgReject == 0 ? false : true;

        nPublicRoomStatus = _UserPublicRoomStatus;
        nPublicRoomName = _UserPublicRoomName;
        nPublicRoomLimit = _UserPublicRoomLimit;
        nPublicRoomTime = _UserPublicRoomTime;


        nItemCount = _UserItemCount;
        item_1 = _UserItem1;
        if(item_1 != 0) {
            itemList.put(0, item_1);
        }
        item_2 = _UserItem2;
        if(item_2 != 0) {
            itemList.put(1, item_2);
        }

        item_3 = _UserItem3;
        if(item_3 != 0)
        {
            itemList.put(2, item_3);
        }

        item_4 = _UserItem4;
        if(item_4 != 0)
        {
            itemList.put(3, item_4);
        }

        item_5 = _UserItem5;
        if(item_5 != 0)
        {
            itemList.put(4, item_5);
        }

        item_6 = _UserItem6;
        if(item_6 != 0)
        {
            itemList.put(5, item_6);
        }

        item_7 = _UserItem7;
        if(item_7 != 0)
        {
            itemList.put(6, item_7);
        }

        item_8 = _UserItem8;
        if(item_8 != 0)
        {
            itemList.put(7, item_8);
        }

        bestItem = _UserBestItem;

        Set<Integer> set = itemList.keySet();
        Iterator<Integer> key = set.iterator();

        while (key.hasNext())
        {
            int idx = key.next();
            itemIdx.add(idx);
        }

        Point = _UserPoint;
        Grade = _UserGrade;

        ConnectDate = _UserConnDate;
        LastBoardWriteTime = _UserLastBoardWriteTime;
        LastAdsTime = _UserLastAdsTime;

        nStartAge = (Integer.parseInt(getUserAge()) / 10) * 10;
        nEndAge = nStartAge + 19;

        NickChangeCnt = _UserNickChangeCnt;
    }

    public void refreshItemIdex()
    {
        bestItem = SetBestItem();
        itemIdx.clear();

        Set<Integer> set = itemList.keySet();
        Iterator<Integer> key = set.iterator();

        while (key.hasNext())
        {
            int idx = key.next();
            itemIdx.add(idx);

        }
        SaveMyItem();
    }


    public void setGrade(int nGrade){
        Grade = nGrade;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;

        table = database.getReference("User/" + getUserIdx());
        table.child("Grade").setValue(Grade);

        table = database.getReference("SimpleData/" + getUserIdx());
        table.child("Grade").setValue(Grade);
    }

    public int getGrade(){return Grade;}

    public void setPoint(int nPoint){
        Point += nPoint;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;

        if(getUserIdx().equals("") || getUserIdx() == null)
        {
            return;
        }

        table = database.getReference("User/" + getUserIdx());
        table.child("Point").setValue(Point);
        table.child("ConnectDate").setValue(ConnectDate);

        table = database.getReference("SimpleData/" + getUserIdx());
        table.child("Point").setValue(Point);
        table.child("ConnectDate").setValue(ConnectDate);

        SetMyGrade();
    }
    public int getPoint(){return Point;}

    public void SetMyGrade() {
        if(getPoint() <= 100)
            setGrade(0);
        else if(getPoint() <= 200)
            setGrade(1);
        else if(getPoint() <= 300)
            setGrade(2);
        else if(getPoint() <= 500)
            setGrade(3);
        else if(getPoint() <= 1000)
            setGrade(4);
        else if(getPoint() <= 2000)
            setGrade(5);
    }


    public void SetCurFrag(int Frag)
    {
        nCurVisibleFrag = Frag;
    }
    public int GetCurFrag()
    {
        return  nCurVisibleFrag;
    }
    public void setUserIdx(String userIdx) {
        strIdx = userIdx;
    }

    public String getUserIdx() {
        return strIdx;
    }

    public String getUserUid() {
        return strUid;
    }

    public void setUserToken(String userToken) {
        strToken = userToken;
    }

    public String getUserToken() {
        return strToken;
    }

    public void setUserImgCnt(int userCnt) {
        nImgCount = userCnt;
    }

    public int getUserImgCnt() {
        return nImgCount;
    }

    public void setUserImg(String userImg) {
        strImg = userImg;
    }

    public String getUserImg() {
        return strImg;
    }

    public void setUserProfileImg(int Index, String userImg) {
        strProfileImg[Index] = userImg;
    }

    public String getUserProfileImg(int Index) {
        return strProfileImg[Index];
    }

    public void delUserProfileImg(int Index, String userImg) {
        switch (Index)
        {
            case 0:
                strImg = strProfileImg[1];
                strProfileImg[0] = strProfileImg[1];
                strProfileImg[1] = strProfileImg[2];
                strProfileImg[2] = strProfileImg[3];
                strProfileImg[3] = userImg;
                break;
            case 1:
                strProfileImg[0] = strProfileImg[0];
                strProfileImg[1] = strProfileImg[2];
                strProfileImg[2] = strProfileImg[3];
                strProfileImg[3] = userImg;
                break;
            case 2:
                strProfileImg[0] = strProfileImg[0];
                strProfileImg[1] = strProfileImg[1];
                strProfileImg[2] = strProfileImg[3];
                strProfileImg[3] = userImg;
                break;
            case 3:
                strProfileImg[0] = strProfileImg[0];
                strProfileImg[1] = strProfileImg[1];
                strProfileImg[2] = strProfileImg[2];
                strProfileImg[3] = userImg;
                break;
        }
    }

    public void setUserNick(String userNick) {
        strNick = userNick;
    }

    public String getUserNick() {
        return strNick;
    }

    public void setUserGender(String userGender) {
        strGender = userGender;
    }

    public String getUserGender() {
        return strGender;
    }

    public void setUserAge(String userAge) {
        strAge = userAge;
    }

    public String getUserAge() {
        return strAge;
    }

    public void setUserLon(Double userLon) {
        lLon = userLon;
    }

    public Double getUserLon() {
        return lLon;
    }

    public void setUserLat(Double userLat) {
        lLat = userLat;
    }

    public Double getUserLat() {
        return lLat;
    }

    public void setUserHoney(int userHoney) {
        nHoney = userHoney;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("User/" + strIdx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("Honey", nHoney);
        table.updateChildren(updateMap);


    }

    public int getUserHoney() {
        return nHoney;
    }

    public int getRecvHoney() {
        return nRecvCount;
    }

    public int getSendHoney() {
        return nSendCount;
    }


    public void setSendHoneyCnt(int sendHoneyCnt) {
        nSendCount -= sendHoneyCnt;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;

        table = database.getReference("User/" + strIdx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("SendCount", nSendCount);
        table.updateChildren(updateMap);
    }

    public void setRecvHoneyCnt(int recvHoneyCnt) {
        nRecvCount -= recvHoneyCnt;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("User/" + strIdx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("RecvCount", nRecvCount);
        table.updateChildren(updateMap);
    }

    public void setUserMemo(String userMemo) {
        strMemo = userMemo;
    }

    public String getUserMemo() {
        return strMemo;
    }

    public boolean makeSendList(UserData _UserData, String _strSend, int _SendCount) {
        boolean rtValue = false;

        UserData SaveUserData = _UserData;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user, targetuser;
        table = database.getReference("User");

        user = table.child(strIdx).child("SendList");
        targetuser = table.child(_UserData.Idx).child("SendList");

        String strCheckName = strIdx + "_" + _UserData.Idx;
        String strCheckName1 = _UserData.Idx + "_" + strIdx;

        SimpleChatData tempMySave = new SimpleChatData();
        tempMySave.ChatRoomName = strCheckName;
        tempMySave.Msg = _strSend.toString();
        tempMySave.Nick = getUserNick();
        tempMySave.Idx = getUserIdx();
        tempMySave.Img = getUserImg();
        tempMySave.Grade = getGrade();
        tempMySave.BestItem = bestItem;
        tempMySave.Check = 1;
        tempMySave.SendHeart = _SendCount;

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String formatStr = sdf.format(date);

        tempMySave.Date = formatStr;

        SimpleChatData tempTargetSave = new SimpleChatData();
        tempTargetSave.ChatRoomName = strCheckName;
        tempTargetSave.Idx = _UserData.Idx;
        tempTargetSave.Nick = _UserData.NickName;
        tempTargetSave.Img = _UserData.Img;
        tempTargetSave.Msg = _strSend.toString();
        tempTargetSave.Grade = _UserData.Grade;
        tempTargetSave.BestItem = _UserData.BestItem;
        tempTargetSave.Date = formatStr;
        tempTargetSave.SendHeart = _SendCount;

        tempTargetSave.Check = 0;

        if (!arrChatNameList.contains(strCheckName) && !arrChatNameList.contains(strCheckName1) ) {
            user.child(strCheckName).setValue(tempTargetSave);
            targetuser.child(strCheckName).setValue(tempMySave);
            rtValue = true;

        } else
            return rtValue;

        return rtValue;
    }

    public static void set_Instance(MyData _Instance) {
        MyData._Instance = _Instance;
    }

    public void getReportedCnt() {
        String MyID = strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("Reported");
        user = table.child(strIdx);


        user.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                ReportedData tempData = new ReportedData();
                tempData = dataSnapshot.getValue(ReportedData.class);
                arrReportList.add(tempData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int tempData;
                ReportedData tempAddData = new ReportedData();
                tempAddData = dataSnapshot.getValue(ReportedData.class);
                arrReportList.add(tempAddData);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa = 0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getFanList() {
        String MyID = strIdx;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("User");
        user = table.child(strIdx).child("FanList");


        user.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                FanData tempFanData = new FanData();
                tempFanData = dataSnapshot.getValue(FanData.class);
               // arrMyFanList.add(tempFanData);
                if (!arrMyFanList.contains(tempFanData.Idx)) {
                    arrMyFanList.add(tempFanData);
                    arrMyFanRecvList.put(tempFanData.Idx, tempFanData);

                    Query data = database.getReference().child("SimpleData").child(tempFanData.Idx);
                    final FanData finalTempFanData = tempFanData;
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                            arrMyFanDataList.put(finalTempFanData.Idx, DBData);

                            CommonFunc.getInstance().SetFanAlarmVisible(true);
                            if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND) {
                                if(GetCurFrag() == 3)
                                {
                                    Fragment frg = null;
                                    frg = mFragmentMng.findFragmentByTag("FanListFragment");
                                    final FragmentTransaction ft = mFragmentMng.beginTransaction();
                                    ft.detach(frg);
                                    ft.attach(frg);
                                    ft.commit();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                FanData SendList = dataSnapshot.getValue(FanData.class);

                boolean bEqual = false;
                int Idx = 0;
                for(int i=0; i<arrMyFanList.size(); i++)
                {
                    if(arrMyFanList.get(i).Idx.equals(SendList.Idx))
                    {
                        bEqual = true;
                        Idx = i;
                        break;
                    }
                }
                if(bEqual == false)
                {
                    arrMyFanList.add(SendList);
                    arrMyFanRecvList.put(SendList.Idx, SendList);

                    Query data = database.getReference().child("SimpleData").child(SendList.Idx);
                    final FanData finalTempFanData = SendList;
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                            arrMyFanDataList.put(finalTempFanData.Idx, DBData);

              /*              CommonFunc.getInstance().SetFanAlarmVisible(true);
                            if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND) {
                                if(GetCurFrag() == 3)
                                {
                                    Fragment frg = null;
                                    frg = mFragmentMng.findFragmentByTag("FanListFragment");
                                    final FragmentTransaction ft = mFragmentMng.beginTransaction();
                                    ft.detach(frg);
                                    ft.attach(frg);
                                    ft.commit();
                                }
                            }*/

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                else
                {
                    /*int nRecv = arrMyFanDataList.get(SendList.Idx).RecvGold;
                    SendList.RecvGold += nRecv;*/
                /*    arrMyFanDataList.put(SendList.Idx, SendList);*/

                    Query data = database.getReference().child("SimpleData").child(SendList.Idx);
                    final FanData finalTempFanData = SendList;
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                            arrMyFanDataList.put(finalTempFanData.Idx, DBData);

              /*              CommonFunc.getInstance().SetFanAlarmVisible(true);
                            if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND) {
                                if(GetCurFrag() == 3)
                                {
                                    Fragment frg = null;
                                    frg = mFragmentMng.findFragmentByTag("FanListFragment");
                                    final FragmentTransaction ft = mFragmentMng.beginTransaction();
                                    ft.detach(frg);
                                    ft.attach(frg);
                                    ft.commit();
                                }
                            }*/

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                CommonFunc.getInstance().SetFanAlarmVisible(true);
                if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND) {
                    if(GetCurFrag() == 3)
                    {
                        Fragment frg = null;
                        frg = mFragmentMng.findFragmentByTag("FanListFragment");
                        final FragmentTransaction ft = mFragmentMng.beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa = 0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


    public void getSendList() {
        String MyID = strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("User");
        user = table.child(strIdx).child("SendList");

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                SimpleChatData SendList = dataSnapshot.getValue(SimpleChatData.class);
                if (!arrChatNameList.contains(SendList.ChatRoomName)) {
                    CommonFunc.getInstance().SetChatAlarmVisible(true);
                    arrChatNameList.add(SendList.ChatRoomName);
                    arrChatDataList.put(SendList.ChatRoomName, SendList);

                    if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND) {
                        if (GetCurFrag() == 2) {
                            Fragment frg = null;
                            frg = mFragmentMng.findFragmentByTag("ChatListFragment");
                            final FragmentTransaction ft = mFragmentMng.beginTransaction();
                            ft.detach(frg);
                            ft.attach(frg);
                            ft.commit();
                        }
                    }
                }
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                CommonFunc.getInstance().SetChatAlarmVisible(true);
                SimpleChatData SendList = dataSnapshot.getValue(SimpleChatData.class);
                arrChatDataList.put(SendList.ChatRoomName, SendList);

                ActivityManager activityManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);


                if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND) {

   /*                 if(info.get(0).topActivity.getClassName().equals(ChatRoomActivity.class.getName()) == false)
                    {
                        CommonFunc.getInstance().PlayVibration(mContext);
                        CommonFunc.getInstance().PlayAlramSound(mContext, R.raw.katalk);

        *//*                if(GetCurFrag() == 2 || GetCurFrag() == 5)
                        {

                        }
                        else
                        {
                            if(SendList.SendHeart == 0)
                                CommonFunc.getInstance().ShowMsgPopup(mContext, SendList);
                            else
                                CommonFunc.getInstance().ShowGiftPopup(mContext, SendList);
                        }*//*

                    }
*/
                    if (GetCurFrag() == 2) {
                        Fragment frg = null;
                        frg = mFragmentMng.findFragmentByTag("ChatListFragment");
                        final FragmentTransaction ft = mFragmentMng.beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                    } else if (GetCurFrag() == 5) {
                        SendList.Check = 1;
                        arrChatDataList.put(SendList.ChatRoomName, SendList);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
       /*         int saa = 0;
                SendData SendList = dataSnapshot.getValue(SendData.class);
                int index = arrSendNameList.indexOf(SendList.strSendName);
                arrSendNameList.remove(index);

                for (Iterator<SendData> it = arrSendDataList.iterator(); it.hasNext(); ) {
                    String value = it.next().strSendName;

                    if (value.equals(SendList.strSendName)) {
                        it.remove();
                    }
                }*/

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }

    public void getImageLoading() {
        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("ImgUrl");

        table.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int saa = 0;
                String tempUserData = dataSnapshot.getValue(String.class);
                strImgLodingUri = tempUserData;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getDownUrl() {
        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("DownUrl");

        table.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int saa = 0;
                String tempUserData = dataSnapshot.getValue(String.class);
                strDownUri = tempUserData;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getAdBannerID() {
        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("BannerID");

        table.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int saa = 0;
                String tempUserData = dataSnapshot.getValue(String.class);
                strBannerID = tempUserData;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public boolean makeCardList(final UserData target) {
        boolean rtValue = false;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("User/" + getUserIdx());


        if(IsCardList(target.Idx))
            return false;

        CommonFunc.getInstance().SetCardAlarmVisible(true);
        arrCardNameList.add(target.Idx);
        table.child("CardList").child(target.Idx).setValue(target.Idx);

        rtValue = true;

        Query data = database.getReference().child("SimpleData").child(target.Idx);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);

                arrCarDataList.put(target.Idx, DBData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rtValue;

    }
    public void removeCardList(final UserData target) {

        arrCardNameList.remove(target.Idx);
        arrCarDataList.remove(target.Idx);
    }


    public boolean IsCardList(String idx) {
        for (int i = 0; i < arrCardNameList.size(); i++) {
            if (arrCardNameList.get(i).equals(idx))
                return true;
        }

        return false;
    }


    public void setMemo(Editable memo) {
        strMemo = memo.toString();
    }

    public void setSettingData(int SearchMode, int ViewMode, boolean recvMsgReject, boolean alarmSetting_Sound, boolean alarmSetting_Vibration) {
        nSearchMode = SearchMode;
        nAlarmSetting_Sound = alarmSetting_Sound;
        nAlarmSetting_Vibration = alarmSetting_Vibration;
        nViewMode = ViewMode;
        nRecvMsgReject = recvMsgReject;
    }

    public void getGiftData(String Idx) {

        // arrMyStarDataList.clear();

        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");
        //Log.d("!!!!!!", "getMyStarData  " + arrMyStarList.size());

        strTargetIdx = Idx;

        //Log.d("!!!!!!", "size OK  " + arrMyStarList.size());

        table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserData tempUserData = dataSnapshot.getValue(UserData.class);
                arrGiftUserDataList.add(tempUserData);

                int i = arrGiftUserDataList.size();
           /*     for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                    //  if(!arrMyStarDataList.get(finalI).arrStarList.contains(entry.getValue().Nick))
                    arrGiftUserDataList.get(i - 1).arrStarList.add(entry.getValue());
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getGiftHoneyList() {
        String MyID = strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("GiftHoneyList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                CommonFunc.getInstance().SetMailAlarmVisible(true);
                SendData SendList = dataSnapshot.getValue(SendData.class);
                arrGiftHoneyNameList.add(SendList.strSendName);
                arrGiftHoneyDataList.add(SendList);
                getGiftData(SendList.strSendName);
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                CommonFunc.getInstance().SetMailAlarmVisible(true);
                SendData SendList = dataSnapshot.getValue(SendData.class);
                arrGiftHoneyNameList.add(SendList.strSendName);
                arrGiftHoneyDataList.add(SendList);
                getGiftData(SendList.strSendName);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa = 0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getRecvHoneyList() {
        String MyID = strIdx;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("RecvHoneyList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                SendData SendList = dataSnapshot.getValue(SendData.class);
                SendList.strFireBaseKey = dataSnapshot.getKey();

                arrRecvHoneyNameList.add(SendList.strSendName);
                arrRecvHoneyDataList.add(SendList);
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                int saa = 0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getSendHoneyList() {
        String MyID = strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("SendHoneyList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                SendData SendList = dataSnapshot.getValue(SendData.class);
                arrSendHoneyNameList.add(SendList.strSendName);
                arrSendHoneyDataList.add(SendList);
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa = 0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public boolean makeSendHoneyList(UserData _UserData, int SendHoneyCnt, String SendMsg) {
        boolean rtValue = false;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("SendHoneyList");

        user = table.child(strIdx);

        SendData tempTargetSave = new SendData();
        tempTargetSave.strTargetNick = _UserData.NickName;
        tempTargetSave.strTargetImg = _UserData.Img;
        tempTargetSave.strSendName = _UserData.Idx;
        tempTargetSave.nSendHoney = SendHoneyCnt;
        tempTargetSave.strTargetMsg = SendMsg;

        long now = CommonFunc.getInstance().GetCurrentTime();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String getTime = sdf.format(date);

        tempTargetSave.strSendDate = getTime;

        user.push().setValue(tempTargetSave);
        rtValue = true;


        return rtValue;
    }

    public boolean makeRecvHoneyList(UserData _UserData, int SendHoneyCnt, String SendMsg) {
        boolean rtValue = false;

        makeGiftHoneyList(_UserData, SendHoneyCnt, SendMsg);

        UserData SaveUserData = _UserData;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, targetuser;
        table = database.getReference("RecvHoneyList");

        targetuser = table.child(_UserData.Idx);

        SendData tempMySave = new SendData();
        tempMySave.strTargetImg = getUserImg();
        tempMySave.strTargetNick = getUserNick();
        tempMySave.strSendName = getUserIdx();
        tempMySave.nSendHoney = SendHoneyCnt;
        tempMySave.strTargetMsg = SendMsg;

        long now = CommonFunc.getInstance().GetCurrentTime();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String getTime = sdf.format(date);

        tempMySave.strSendDate = getTime;

        targetuser.push().setValue(tempMySave);
        rtValue = true;


        return rtValue;
    }

    public boolean makeGiftHoneyList(UserData _UserData, int SendHoneyCnt, String SendMsg) {
        boolean rtValue = false;

        UserData SaveUserData = _UserData;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, targetuser;
        table = database.getReference("GiftHoneyList");

        targetuser = table.child(_UserData.Idx);

        SendData tempMySave = new SendData();
        tempMySave.strTargetImg = getUserImg();
        tempMySave.strTargetNick = getUserNick();
        tempMySave.strSendName = getUserIdx();
        tempMySave.nSendHoney = SendHoneyCnt;
        tempMySave.strTargetMsg = SendMsg;

        long now = CommonFunc.getInstance().GetCurrentTime();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String getTime = sdf.format(date);

        tempMySave.strSendDate = getTime;

        targetuser.push().setValue(tempMySave);
        rtValue = true;


        return rtValue;
    }

    public void makeBlockList(SimpleChatData blockList) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user, target;
        table = database.getReference("BlockList");

        BlockData tempData = new BlockData();

        tempData.Img = blockList.Img;
        tempData.NickName = blockList.Nick;
        tempData.Idx = blockList.Idx;

        BlockData targetData = new BlockData();

        targetData.Img = getUserImg();
        targetData.NickName = getUserNick();
        targetData.Idx = getUserIdx();

        user = table.child(strIdx);
        user.child(tempData.Idx).setValue(tempData);

        target = database.getReference("BlockedList").child(tempData.Idx);
        //target = table
        target.child(getUserIdx()).setValue(targetData);
    }

    public void getBlockList() {
        String MyID = strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("BlockList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                blockList = dataSnapshot.getValue(BlockData.class);
                arrBlockDataList.add(blockList);
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa = 0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getBlockedList() {
        String MyID = strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("BlockedList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                blockList = dataSnapshot.getValue(BlockData.class);
                arrBlockedDataList.add(blockList);
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa = 0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


    public void delBlockList(BlockData blockList) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user, target;
        table = database.getReference("BlockList");


        user = table.child(getUserIdx()).child(blockList.Idx);
        user.removeValue();

        target = database.getReference("BlockedList").child(blockList.Idx);
        target.child(getUserIdx()).removeValue();
    }

    public void getSetting() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("Setting");
        user = table.child(strIdx);


        user.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        TempSettingData stRecvData = new TempSettingData();
                        stRecvData = dataSnapshot.getValue(TempSettingData.class);
                        if (stRecvData != null) {

                            if(stRecvData.StartAge == 0 || stRecvData.EndAge == 0)
                            {
                                nStartAge = (Integer.parseInt(getUserAge()) / 10) * 10;
                                nEndAge = nStartAge + 19;
                            }
                            else
                            {
                                nStartAge = stRecvData.StartAge;
                                nEndAge = stRecvData.EndAge;
                            }

                            nSearchMode = stRecvData.SearchMode;
                            nViewMode = stRecvData.ViewMode;
                            nRecvMsgReject = stRecvData.RecvMsgReject == 0 ? false : true;
                            nAlarmSetting_Sound = stRecvData.AlarmMode_Sound;
                            nAlarmSetting_Vibration = stRecvData.AlarmMode_Vibration;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });

    }

    public void makeFanList(final UserData stTargetData, int SendCount) {

        int nTotalSendCnt = 0;


        if(stTargetData.FanList.size() == 0)
        {
            FanData tempFan = new FanData();
            tempFan.Idx = getUserIdx();
     /*       tempFan.NickName = getUserNick();
            tempFan.BestItem = GetBestItem();
            tempFan.Grade = getGrade();
            tempFan.Img = getUserImg();*/
            tempFan.RecvGold = SendCount;
            nTotalSendCnt = SendCount;
            stTargetData.FanList.put(getUserIdx(), tempFan);
            stTargetData.arrFanList.add(tempFan);

            SimpleUserData tempData = new SimpleUserData();
            tempData.Idx = getUserIdx();
            tempData.Token = getUserToken();
            tempData.Img = getUserImg();
            tempData.NickName = getUserNick();
            tempData.Gender = getUserGender();
            tempData.Age = getUserAge();
            tempData.Memo =getUserMemo();
            tempData.RecvGold = getRecvHoney();
            tempData.SendGold =getSendHoney();
            tempData.Lat = getUserLat();
            tempData.Lon = getUserLon();
            tempData.Date = strDate;
            tempData.FanCount = getFanCount();
            tempData.Point = getPoint();
            tempData.BestItem = bestItem;
            tempData.Grade = getGrade();
            stTargetData.arrFanData.put(getUserIdx(), tempData);


            FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
            DatabaseReference data = fierBaseDataInstance.getReference("User").child(stTargetData.Idx).child("FanCount");
            data.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Long index = mutableData.getValue(Long.class);
                    if (index == null) {
                        mutableData.setValue(-1);
                        return Transaction.success(mutableData);
                    }

                    index--;

                    mutableData.setValue(index);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    stTargetData.FanCount = dataSnapshot.getValue(Long.class);

                    for(int i = 0; i < arrUserAll_Send_Age.size(); i++)
                    {
                        if(arrUserAll_Send_Age.get(i).Idx.equals(stTargetData.Idx)) {
                            arrUserAll_Send_Age.get(i).FanCount = stTargetData.FanCount;
                            break;
                        }
                    }

                    if(stTargetData.Gender.equals("여자"))
                    {
                        for(int i = 0; i < arrUserWoman_Send_Age.size(); i++)
                        {
                            if(arrUserWoman_Send_Age.get(i).Idx.equals(stTargetData.Idx)) {
                                arrUserWoman_Send_Age.get(i).FanCount = stTargetData.FanCount;
                                break;
                            }
                        }
                    }
                    else
                    {
                        for(int i = 0; i < arrUserMan_Send_Age.size(); i++)
                        {
                            if(arrUserMan_Send_Age.get(i).Idx.equals(stTargetData.Idx)) {
                                arrUserMan_Send_Age.get(i).FanCount = stTargetData.FanCount;
                                break;
                            }
                        }
                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference table;
                    table = database.getReference("User/" + stTargetData.Idx);

                    Map<String, Object> updateFanCountMap = new HashMap<>();
                    updateFanCountMap.put("FanCount", stTargetData.FanCount);
                    table.updateChildren(updateFanCountMap);

                    table = database.getReference("SimpleData/" + stTargetData.Idx);

                    updateFanCountMap.put("FanCount", stTargetData.FanCount);
                    table.updateChildren(updateFanCountMap);

                }
            });
        }
        else
        {
            Set<String> keySet = stTargetData.FanList.keySet();
            Iterator iterator = keySet.iterator();
            boolean bExist = false;
            while(iterator.hasNext()){
                String element = (String) iterator.next();

                if(stTargetData.FanList.get(element).Idx.equals(getUserIdx()))
                {
                    bExist = true;
                    break;
                }
            }


            if(bExist == true)
            {
                stTargetData.FanList.get(getUserIdx()).RecvGold += SendCount;
                nTotalSendCnt = stTargetData.FanList.get(getUserIdx()).RecvGold;
            }

            else
            {
                FanData tempFan = new FanData();
                tempFan.Idx = getUserIdx();
 /*               tempFan.NickName = getUserNick();
                tempFan.BestItem = GetBestItem();
                tempFan.Grade = getGrade();
                tempFan.Img = getUserImg();*/
                tempFan.RecvGold = SendCount;
                nTotalSendCnt = SendCount;
                stTargetData.FanList.put(getUserIdx(), tempFan);
                stTargetData.arrFanList.add(tempFan);

                SimpleUserData tempData = new SimpleUserData();
                tempData.Idx = getUserIdx();
                tempData.Token = getUserToken();
                tempData.Img = getUserImg();
                tempData.NickName = getUserNick();
                tempData.Gender = getUserGender();
                tempData.Age = getUserAge();
                tempData.Memo =getUserMemo();
                tempData.RecvGold = getRecvHoney();
                tempData.SendGold =getSendHoney();
                tempData.Lat = getUserLat();
                tempData.Lon = getUserLon();
                tempData.Date = strDate;
                tempData.FanCount = getFanCount();
                tempData.Point = getPoint();
                tempData.BestItem = bestItem;
                tempData.Grade = getGrade();
                stTargetData.arrFanData.put(getUserIdx(), tempData);


                FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
                DatabaseReference data = fierBaseDataInstance.getReference("User").child(stTargetData.Idx).child("FanCount");
                data.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Long index = mutableData.getValue(Long.class);
                    if (index == null) {
                        mutableData.setValue(-1);
                        return Transaction.success(mutableData);
                    }
                        index--;

                        mutableData.setValue(index);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {


                        stTargetData.FanCount = dataSnapshot.getValue(Long.class);

                        for(int i = 0; i < arrUserAll_Send_Age.size(); i++)
                        {
                            if(arrUserAll_Send_Age.get(i).Idx.equals(stTargetData.Idx)) {
                                arrUserAll_Send_Age.get(i).FanCount = stTargetData.FanCount;
                                break;
                            }
                        }

                        if(stTargetData.Gender.equals("여자"))
                        {
                            for(int i = 0; i < arrUserWoman_Send_Age.size(); i++)
                            {
                                if(arrUserWoman_Send_Age.get(i).Idx.equals(stTargetData.Idx)) {
                                    arrUserWoman_Send_Age.get(i).FanCount = stTargetData.FanCount;
                                    break;
                                }
                            }
                        }
                        else
                        {
                            for(int i = 0; i < arrUserMan_Send_Age.size(); i++)
                            {
                                if(arrUserMan_Send_Age.get(i).Idx.equals(stTargetData.Idx)) {
                                    arrUserMan_Send_Age.get(i).FanCount = stTargetData.FanCount;
                                    break;
                                }
                            }
                        }

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference table;
                        table = database.getReference("User/" + stTargetData.Idx);

                        Map<String, Object> updateFanCountMap = new HashMap<>();
                        updateFanCountMap.put("FanCount", stTargetData.FanCount);
                        table.updateChildren(updateFanCountMap);

                        table = database.getReference("SimpleData/" + stTargetData.Idx);

                        updateFanCountMap.put("FanCount", stTargetData.FanCount);
                        table.updateChildren(updateFanCountMap);

                    }
                });
            }
        }



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;

        table = database.getReference("User/" + stTargetData.Idx).child("FanList");
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("RecvGold", nTotalSendCnt);
        updateMap.put("Idx", getUserIdx());
        updateMap.put("NickName", getUserNick());
        updateMap.put("BestItem", GetBestItem());
        updateMap.put("Grade", getGrade());
        updateMap.put("Img", getUserImg());
        table.child(getUserIdx()).updateChildren(updateMap);

        //getMyfanData(stTargetData.Idx);

        //stTargetData.Count = nTotalSendCnt;

/*
        SimpleUserData tempStarList = new SimpleUserData();
        tempStarList.Idx = stTargetData.Idx;
        tempStarList.RecvGold = nTotalSendCnt;
        tempStarList.NickName = getUserNick();
        tempStarList.Img = getUserImg();
*/


        //arrMyFanList.add(tempStarList);

    }

    public void makeStarList(UserData stTargetData, int SendCount) {

        int nTotalSendCnt = 0;
        for (int i = 0; i < arrSendHoneyDataList.size(); i++) {
            if (arrSendHoneyDataList.get(i).strTargetNick.equals(stTargetData.NickName))
                nTotalSendCnt -= arrSendHoneyDataList.get(i).nSendHoney;
        }

        nTotalSendCnt -= SendCount;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("User/" + getUserIdx());

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("SendGold", nTotalSendCnt);
        updateMap.put("Idx", stTargetData.Idx);
        table.child("StarList").child(stTargetData.Idx).updateChildren(updateMap);
        //

        final StarData tempStarList = new StarData();
        tempStarList.Idx = stTargetData.Idx;
        tempStarList.SendGold = nTotalSendCnt;

        boolean bSame = false;
        for (int i = 0; i < arrMyStarList.size(); i++) {
            if (arrMyStarList.get(i).equals(tempStarList.Idx)) {
                bSame = true;
                arrMyStarList.get(i).SendGold = nTotalSendCnt;
                // sortStarData();
                break;
            }
        }

        if (bSame == false) {
            arrMyStarList.add(tempStarList);
            // arrMyStarDataList.put(tempStarList.Idx, tempStarList);
            //sortStarData();
            // getMyStarData(tempStarList.Idx);

            Query data = database.getReference().child("SimpleData").child(tempStarList.Idx);
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                    arrMyStarDataList.put(tempStarList.Idx, DBData);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }
/*
    public void getMyStarData(String TargetIdx) {
        final String strTargetIdx =TargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int saa = 0;
                UserData tempUserData = dataSnapshot.getValue(UserData.class);
                if(tempUserData != null)
                {
                    mapMyStarData.put(strTargetIdx, tempUserData);

                    for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                        mapMyStarData.get(strTargetIdx).arrFanList.add(entry.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getMyStarData() {
        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        arrMyStarDataList.clear();

        //user.addChildEventListener(new ChildEventListener() {
        for (int i = 0; i < arrMyStarList.size(); i++) {
            strTargetIdx = arrMyStarList.get(i).Idx;

            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int saa = 0;
                    UserData tempUserData = dataSnapshot.getValue(UserData.class);
                    arrMyStarDataList.add(tempUserData);

                    int idx = 0;
                    for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                        if( idx < arrMyStarDataList.size()) {
                            arrMyStarDataList.get(idx).arrStarList.add(entry.getValue());
                            idx++;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
    }*/

 /*   public void sortStarData() {
        Collections.sort(arrMyStarList);
    }*/

    public boolean makePublicRoom(int RoomLimit, int RoomTime) {
        boolean rtValue = false;

        long now = CommonFunc.getInstance().GetCurrentTime();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHMM");
        String getTime = sdf.format(date);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, RoomName, RoomData;
        table = database.getReference("PublicRoomList");
        RoomName = table.child(getUserIdx()).child(getTime);

        PublicRoomData tempPRD = new PublicRoomData();
        tempPRD.CurRoomName = getTime;
        tempPRD.CurRoomStatus = 1;
        tempPRD.nEndTime = Integer.parseInt(getTime) + RoomTime;
        tempPRD.strImg = getUserImg();
        RoomName.setValue(tempPRD);

        //RoomName.child("UserList").push().setValue(getUserIdx());
        RoomName.child("UserList").push().setValue(getUserNick());

        nPublicRoomName =  Integer.parseInt(tempPRD.CurRoomName);
        nPublicRoomLimit =  RoomLimit;
        nPublicRoomTime =  RoomTime;


        RoomData = database.getReference("PublicRoomData").child(getUserIdx()).child(tempPRD.CurRoomName);
        long nowTime =CommonFunc.getInstance().GetCurrentTime();

        PublicRoomChatData tempPRDChatData = new PublicRoomChatData(getUserIdx(), null, getUserNick()+"의 공개채팅방입니다.", nowTime, getUserImg());
        RoomData.push().setValue(tempPRDChatData);

        rtValue = true;

        return rtValue;
    }


    public void MonitorPublicRoomStatus() {
        String MyID = strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("PublicRoomList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PublicRoomData stRecvData = new PublicRoomData();
                stRecvData = dataSnapshot.getValue(PublicRoomData.class);
                if(stRecvData != null)
                    setUserPublicRoomStatus(stRecvData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                delUserPublicRoomStatus();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }

    public void DestroyPRD() {
        nPublicRoomLimit = 0;
        nPublicRoomName = 0;
        nPublicRoomStatus = 0;
        nPublicRoomTime = 0;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Ref = database.getReference("PublicRoomList");
        Ref.child(getUserIdx()).removeValue();
    }

    public  void delUserPublicRoomStatus()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());
        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(getUserIdx());

        user.child("PublicRoomStatus").setValue(0);
        user.child("PublicRoomName").setValue(0);
        user.child("PublicRoomLimit").setValue(0);
        user.child("PublicRoomTime").setValue(0);
    }

    public void setUserPublicRoomStatus(PublicRoomData userPublicRoom) {
        nPublicRoomStatus = userPublicRoom.CurRoomStatus;
        nPublicRoomName = Integer.parseInt(userPublicRoom.CurRoomName);
        nPublicRoomLimit = userPublicRoom.nRoomLimit;
        nPublicRoomTime = userPublicRoom.nEndTime;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());
        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(getUserIdx());

        user.child("PublicRoomStatus").setValue(nPublicRoomStatus);
        user.child("PublicRoomName").setValue(nPublicRoomName);
        user.child("PublicRoomLimit").setValue(nPublicRoomLimit);
        user.child("PublicRoomTime").setValue(nPublicRoomTime);
    }

    public void setAnotherPublicRoomList(UserData stTargetData) {
        ArrayList<String> arrUserList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("PublicRoomList").child(stTargetData.Idx).child(Integer.toString(stTargetData.PublicRoomName)).child("UserList");
        table.push().setValue(getUserNick());
        //table.push().setValue(getUserIdx());
    }

    public void setMyItem(int myItem) {
        switch (myItem)
        {
            case 0:
            {
                if(item_1 == 0) {
                    nItemCount++;
                }

                item_1++;
                itemList.put(0, item_1);

                break;
            }
            case 1:
            {
                if(item_2 == 0){
                    nItemCount++;
                }

                item_2++;
                itemList.put(1, item_2);

                break;
            }
            case 2:
            {
                if(item_3 == 0){
                    nItemCount++;
                }

                item_3++;
                itemList.put(2, item_3);

                break;
            }
            case 3:
            {
                if(item_4 == 0){
                    nItemCount++;
                }

                item_4++;
                itemList.put(3, item_4);

                break;
            }
            case 4:
            {
                if(item_5 == 0){
                    nItemCount++;
                }

                item_5++;
                itemList.put(4, item_5);

                break;
            }
            case 5:
            {
                if(item_6 == 0){
                    nItemCount++;
                }

                item_6++;
                itemList.put(5, item_6);
                break;
            }
            case 6:
            {
                if(item_7 == 0){
                    nItemCount++;
                }

                item_7++;
                itemList.put(6, item_7);

                break;
            }
            case 7:
            {
                if(item_8 == 0){
                    nItemCount++;
                }

                item_8++;
                itemList.put(7, item_8);
                break;
            }
        }

        bestItem = SetBestItem();

        refreshItemIdex();
        //   SaveMyItem();
    }

    public void SaveMyItem()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());
        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(getUserIdx());

        user.child("ItemCount").setValue(nItemCount);
        user.child("Item_1").setValue(item_1);
        user.child("Item_2").setValue(item_2);
        user.child("Item_3").setValue(item_3);
        user.child("Item_4").setValue(item_4);
        user.child("Item_5").setValue(item_5);
        user.child("Item_6").setValue(item_6);
        user.child("Item_7").setValue(item_7);
        user.child("Item_8").setValue(item_8);
        user.child("BestItem").setValue(bestItem);

        table = database.getReference("SimpleData");//.child(mMyData.getUserIdx());
        final DatabaseReference SimpleUser = table.child(getUserIdx());
        SimpleUser.child("BestItem").setValue(bestItem);

    }

    public void SaveMyItem(int idx, int count)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());
        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(getUserIdx());

        switch (idx)
        {
            case 0:
                item_1 = count;
                user.child("Item_1").setValue(item_1);
                break;
            case 1:
                item_2 = count;
                user.child("Item_2").setValue(item_2);
                break;
            case 2:
                item_3 = count;
                user.child("Item_3").setValue(item_3);
                break;
            case 3:
                item_4 = count;
                user.child("Item_4").setValue(item_4);
                break;
            case 4:
                item_5 = count;
                user.child("Item_5").setValue(item_5);
                break;
            case 5:
                item_6 = count;
                user.child("Item_6").setValue(item_6);
                break;
            case 6:
                item_7 = count;
                user.child("Item_7").setValue(item_7);
                break;
            case 7:
                item_8 = count;
                user.child("Item_8").setValue(item_8);
                break;

        }
        user.child("ItemCount").setValue(nItemCount);
        user.child("BestItem").setValue(bestItem);

        table = database.getReference("SimpleData");//.child(mMyData.getUserIdx());
        final DatabaseReference SimpleUser = table.child(getUserIdx());
        SimpleUser.child("BestItem").setValue(bestItem);
    }

    public int getFanCount() {
        return arrMyFanList.size();
    }

    public int SetBestItem() {
        int rtValue = 0;
        if(itemList.size() == 0)
            return  rtValue;

        Map<Integer, Integer> tempItemList = new HashMap<Integer, Integer>();

        for (int i=0 ;i < itemList.size(); i++)
        {
            if(itemList.get(i) != 0)
                tempItemList.put(i, itemList.get(i));
        }

        TreeMap<Integer,Integer> tm = new TreeMap<Integer,Integer>(tempItemList);
        Iterator<Integer> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
        rtValue = iteratorKey.next();
        bestItem = rtValue;
        //System.out.println(key+","+tm.get(key));
        return rtValue;
    }

    public void makeLastMSG(UserData  tempData, String Roomname, String strMsg, String lTime, int SendCount) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());

        final DatabaseReference user = table.child(strIdx).child("SendList").child(Roomname);
        final DatabaseReference targetuser = table.child(tempData.Idx).child("SendList").child(Roomname);

        String strLastMsg = strMsg;
        String strLastImg = strImg;
        String strLastTime = lTime;
        int nCheckMsg = 0;

        SimpleChatData tempMySave = new SimpleChatData();
        tempMySave.ChatRoomName = Roomname;
        tempMySave.Msg = strLastMsg;
        tempMySave.Nick = getUserNick();
        tempMySave.Idx = getUserIdx();
        tempMySave.Img = getUserImg();
        tempMySave.Grade = getGrade();
        tempMySave.BestItem = bestItem;
        tempMySave.Date = strLastTime;
        tempMySave.Check = 0;
        tempMySave.SendHeart = SendCount;


        SimpleChatData tempTargetSave = new SimpleChatData();
        tempTargetSave.ChatRoomName = Roomname;
        tempTargetSave.Msg = strLastMsg;
        tempTargetSave.Nick = tempData.NickName;
        tempTargetSave.Idx = tempData.Idx;
        tempTargetSave.Img = tempData.Img;
        tempTargetSave.Grade = tempData.Grade;
        tempTargetSave.BestItem = tempData.BestItem;
        tempTargetSave.Date = strLastTime;
        tempTargetSave.Check = 1;
        tempTargetSave.SendHeart = SendCount;

        user.setValue(tempTargetSave);
        targetuser.setValue(tempMySave);
    }

    public boolean CheckConnectDate() {
        boolean rtValue = false;

        long time = CommonFunc.getInstance().GetCurrentTime();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        int nTodayTime = Integer.parseInt( (date.format(new Date(time))).toString());


        int nLastConn = (ConnectDate);

        if(nTodayTime - nLastConn >= 1)
        {
            rtValue = true;
            ConnectDate = nTodayTime;

        }


        return rtValue;
    }

    public ArrayList<SimpleUserData> SortData_Age(ArrayList<SimpleUserData> arrData, int Start, int End)
    {
        ArrayList<SimpleUserData> rtData = new ArrayList<SimpleUserData>();
        for(int i = 0; i < arrData.size(); i++)
        {
            int nDataAge = Integer.parseInt(arrData.get(i).Age);

            if( nDataAge >= Start && nDataAge < End)
            {
                rtData.add(arrData.get(i));
            }
        }

        return rtData;
    }

    public int getUserBestItem()
    {
        return bestItem;
    }

    public long GetLastBoardWriteTime(){return LastBoardWriteTime;}
    public void SetLastBoardWriteTime(long time){LastBoardWriteTime = time;}

    public long GetLastAdsTime(){return LastAdsTime;}
    public void SetLastAdsTime(long time)
    {
        FirebaseData.getInstance().SaveLastAdsTime(time);
        LastAdsTime = time;
    }
}

