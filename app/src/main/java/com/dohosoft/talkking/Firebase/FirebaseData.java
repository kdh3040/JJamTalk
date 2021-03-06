package com.dohosoft.talkking.Firebase;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.util.Log;

import com.dohosoft.talkking.Data.SettingData;
import com.dohosoft.talkking.LoginActivity;
import com.dohosoft.talkking.Rank_HotAdapter;
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
import com.dohosoft.talkking.BoardFragment;
import com.dohosoft.talkking.BoardWriteActivity;
import com.dohosoft.talkking.Data.BoardData;
import com.dohosoft.talkking.Data.BoardMsgDBData;
import com.dohosoft.talkking.Data.BoardReportData;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.TempBoard_ReplyData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Rank_FanRichAdapter;
import com.dohosoft.talkking.Rank_GoldReceiveAdapter;
import com.dohosoft.talkking.Rank_NearAdapter;
import com.dohosoft.talkking.Rank_NewMemberAdapter;
import com.dohosoft.talkking.Util.AwsFunc;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.LocationFunc;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.dohosoft.talkking.Data.CoomonValueData.FIRST_LOAD_MAIN_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.HOTMEMBER_LOAD_MAIN_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.LOAD_MAIN_COUNT;

import static com.dohosoft.talkking.Data.CoomonValueData.FIRST_LOAD_BOARD_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.LOAD_BOARD_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;
import static com.dohosoft.talkking.Data.CoomonValueData.UNIQ_FANCOUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.bMyLoc;
import static com.dohosoft.talkking.Data.CoomonValueData.bMySet;
import static com.dohosoft.talkking.Data.CoomonValueData.bRefreshSetFan;
import static com.dohosoft.talkking.Data.CoomonValueData.bRefreshSetHot;
import static com.dohosoft.talkking.Data.CoomonValueData.bRefreshSetNear;
import static com.dohosoft.talkking.Data.CoomonValueData.bRefreshSetNew;
import static com.dohosoft.talkking.Data.CoomonValueData.bRefreshSetRecv;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetHot;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetNear;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetNew;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetRecv;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetRich;
import static com.dohosoft.talkking.MainActivity.mFragmentMng;

/**
 * Created by boram on 2017-08-05.
 */

public class FirebaseData {

    private static FirebaseData _Instance;
    private MyData mMyData = MyData.getInstance();
    private AwsFunc mAwsFunc = AwsFunc.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    public static FirebaseData getInstance() {
        if (_Instance == null)
            _Instance = new FirebaseData();

        return _Instance;
    }

    private FirebaseData() {

    }

    public String CreateUserIdx(final String Uid) {
        final long[] tempVal = {0};
        final String[] rtStr = new String[1];

        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        DatabaseReference data = fierBaseDataInstance.getReference("UserCount");
        data.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long index = mutableData.getValue(Long.class);
                if (index == null)
                    return Transaction.success(mutableData);

                index++;

                mutableData.setValue(index);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // TODO 환웅 성공 했을때 오는 함수 인듯
                // TODO 환웅 콜백 함수가 있나?
                tempVal[0] = dataSnapshot.getValue(long.class);
                rtStr[0] = Long.toString(tempVal[0]);

                mMyData.setUserIdx(rtStr[0]);

            }
        });

        return rtStr[0];
    }

    public String GetUserIdx(final String Uid) {
        final long[] tempVal = {0};
        final String[] rtStr = new String[1];

        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        Query data = FirebaseDatabase.getInstance().getReference().child("UserIdx").child(Uid);

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rtStr[0] = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rtStr[0];
    }


    public void DelUser(final String Uid, final String Idx) {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        DatabaseReference data = fierBaseDataInstance.getReference("UserIdx").child(Uid);
        data.removeValue();

        data = fierBaseDataInstance.getReference("UserIdx_History");

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(Uid, Idx);
        data.updateChildren(updateMap);


        data = fierBaseDataInstance.getReference("SimpleData").child(mMyData.getUserIdx());
        final DatabaseReference SimpleToken = data.child("Token");
        Map<String, Object> updateTokenMap = new HashMap<>();
        updateTokenMap.put("Token", "0");
        data.updateChildren(updateTokenMap);

        CommonFunc.getInstance().UpdateValue(CoomonValueData.getInstance().DATA_SIMPLEDATA, mMyData.getUserGender(), Idx, updateMap);
        CommonFunc.getInstance().UpdateValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), Idx, updateMap);

        if (mMyData.getUserGender().equals("여자")) {
            data = fierBaseDataInstance.getReference("Users").child("Woman").child(mMyData.getUserIdx());
            final DatabaseReference UserToken = data.child("Token");
            data.updateChildren(updateTokenMap);
            //UserToken.setValue("0");
        } else {
            data = fierBaseDataInstance.getReference("Users").child("Man").child(mMyData.getUserIdx());
            final DatabaseReference UserToken = data.child("Token");
            data.updateChildren(updateTokenMap);
            //UserToken.setValue("0");
        }
    }


    public void SaveUsersMyData(String userIdx) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());

        DatabaseReference user = table.child(userIdx);


        if (FirebaseInstanceId.getInstance() == null || FirebaseInstanceId.getInstance().getToken() == null || FirebaseInstanceId.getInstance().getToken().equals("")) {
            System.exit(0);
        } else {
            mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
            user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
        }

        user.child("Idx").setValue(mMyData.getUserIdx());

        user.child("Img").setValue(mMyData.getUserImg());

        for (int i = 0; i < 4; i++)
            user.child("ImgGroup" + Integer.toString(i)).setValue(mMyData.getUserProfileImg(i));

        if (mMyData.getUserProfileImg(0).equals("1")) {
            mMyData.setUserProfileImg(0, mMyData.getUserImg());
            user.child("ImgGroup0").setValue(mMyData.getUserImg());
        }

        user.child("ImgCount").setValue(mMyData.getUserImgCnt());
        if (mMyData.getUserImgCnt() == 0) {
            mMyData.setUserImgCnt(1);
            user.child("ImgCount").setValue(1);
        }

        user.child("NickName").setValue(mMyData.getUserNick());
        user.child("Gender").setValue(mMyData.getUserGender());
        user.child("Age").setValue(mMyData.getUserAge());

        user.child("Lon").setValue(mMyData.getUserLon());
        user.child("Lat").setValue(mMyData.getUserLat());
        user.child("Dist").setValue(mMyData.getUserDist());

        user.child("SendCount").setValue(mMyData.getSendHoney());
        user.child("RecvGold").setValue(mMyData.getRecvHoney());

        user.child("Date").setValue(mMyData.getUserDate());

        user.child("Memo").setValue(mMyData.getUserMemo());

        user.child("RecvMsgReject").setValue(mMyData.nRecvMsgReject ? 1 : 0);

        user.child("FanCount").setValue(mMyData.getFanCount());

        user.child("Point").setValue(mMyData.getPoint());

        user.child("Grade").setValue(mMyData.getGrade());
        user.child("BestItem").setValue(mMyData.bestItem);
        user.child("Honey").setValue(mMyData.getUserHoney());

        user.child("NickChangeCnt").setValue(mMyData.NickChangeCnt);

        long time = CommonFunc.getInstance().GetCurrentTime();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        int nTodayTime = Integer.parseInt((date.format(new Date(time))).toString());
        user.child("ConnectDate").setValue(nTodayTime);

        user.child("Rejoin").setValue(mMyData.Rejoin);
    }


    public void SaveFirstMyData(String userIdx) {

        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Age", mMyData.getUserAge());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "BestItem", mMyData.getUserBestItem());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ConnectDate", mMyData.ConnectDate);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Date", mMyData.getUserDate());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Dist", mMyData.getUserDist());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "FanCount", mMyData.getFanCount());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Gender", mMyData.getUserGender());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Grade", mMyData.getGrade());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Honey", mMyData.getUserHoney());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Idx", mMyData.getUserIdx());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Img", mMyData.getUserImg());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgCount", mMyData.getUserImgCnt());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgGroup0", mMyData.getUserProfileImg(0));
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgGroup1", mMyData.getUserProfileImg(1));
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgGroup2", mMyData.getUserProfileImg(2));
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgGroup3", mMyData.getUserProfileImg(3));
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Lat", mMyData.getUserLat());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Lon", mMyData.getUserLon());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "NickChangeCnt", mMyData.NickChangeCnt);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "NickName", mMyData.getUserNick());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Point", mMyData.getPoint());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "RecvGold", mMyData.nRecvGold);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "RecvMsgReject", mMyData.nRecvMsgReject ? 1 : 0);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "SendCount", mMyData.nSendCount);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_1", mMyData.item_1);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_2", mMyData.item_2);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_3", mMyData.item_3);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_4", mMyData.item_4);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_5", mMyData.item_5);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_6", mMyData.item_6);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_7", mMyData.item_7);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_8", mMyData.item_8);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Memo", mMyData.getUserMemo());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Users");//.child(mMyData.getUserIdx());

        // DatabaseReference user = table.child( userIdx);
        DatabaseReference user;
        if (mMyData.getUserGender().equals("여자")) {
            user = table.child("Woman").child(mMyData.getUserIdx());
        } else {
            user = table.child("Man").child(mMyData.getUserIdx());
        }

        if (FirebaseInstanceId.getInstance() == null || FirebaseInstanceId.getInstance().getToken() == null || FirebaseInstanceId.getInstance().getToken().equals("")) {
            System.exit(0);
        } else {
            mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
            user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
        }

        user.child("Idx").setValue(mMyData.getUserIdx());

        user.child("Img").setValue(mMyData.getUserImg());

        for (int i = 0; i < 4; i++)
            user.child("ImgGroup" + Integer.toString(i)).setValue(mMyData.getUserProfileImg(i));

        if (mMyData.getUserProfileImg(0).equals("1")) {
            mMyData.setUserProfileImg(0, mMyData.getUserImg());
            user.child("ImgGroup0").setValue(mMyData.getUserImg());
        }

        user.child("ImgCount").setValue(mMyData.getUserImgCnt());
        if (mMyData.getUserImgCnt() == 0) {
            mMyData.setUserImgCnt(1);
            user.child("ImgCount").setValue(1);
        }

        user.child("NickName").setValue(mMyData.getUserNick());
        user.child("Gender").setValue(mMyData.getUserGender());
        user.child("Age").setValue(mMyData.getUserAge());

        user.child("Lon").setValue(mMyData.getUserLon());
        user.child("Lat").setValue(mMyData.getUserLat());
        user.child("Dist").setValue(mMyData.getUserDist());

        user.child("SendCount").setValue(mMyData.getSendHoney());
        user.child("RecvGold").setValue(mMyData.getRecvHoney());

        user.child("Date").setValue(mMyData.getUserDate());

        user.child("Memo").setValue(mMyData.getUserMemo());

        user.child("RecvMsgReject").setValue(mMyData.nRecvMsgReject ? 1 : 0);

        user.child("FanCount").setValue(mMyData.getFanCount());

        user.child("Point").setValue(mMyData.getPoint());

        user.child("Grade").setValue(mMyData.getGrade());
        user.child("BestItem").setValue(mMyData.bestItem);
        user.child("Honey").setValue(mMyData.getUserHoney());

        user.child("NickChangeCnt").setValue(mMyData.NickChangeCnt);

        long time = CommonFunc.getInstance().GetCurrentTime();
        /*SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        int nTodayTime = Integer.parseInt( (date.format(new Date(time))).toString());*/
        user.child("ConnectDate").setValue(-1 * time);

        table = database.getReference("GenderList");//.child(mMyData.getUserIdx());
        user = table.child(userIdx);
        user.setValue(mMyData.getUserGender());

        // 심플 디비 저장
        table = database.getReference("SimpleData");//.child(mMyData.getUserIdx());
        user = table.child(userIdx);
        user.child("Idx").setValue(mMyData.getUserIdx());

        mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
        user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
        user.child("Img").setValue(mMyData.getUserImg());

        user.child("NickName").setValue(mMyData.getUserNick());
        user.child("Gender").setValue(mMyData.getUserGender());
        user.child("Age").setValue(mMyData.getUserAge());

        user.child("Memo").setValue(mMyData.getUserMemo());

        user.child("RecvGold").setValue(mMyData.getRecvHoney());
        user.child("SendGold").setValue(mMyData.getSendHoney());

        user.child("Lon").setValue(mMyData.getUserLon());
        user.child("Lat").setValue(mMyData.getUserLat());
        user.child("Dist").setValue(mMyData.getUserDist());
        user.child("Date").setValue(mMyData.getUserDate());

        user.child("FanCount").setValue(mMyData.getFanCount());

        user.child("Point").setValue(mMyData.getPoint());

        user.child("Grade").setValue(mMyData.getGrade());
        user.child("BestItem").setValue(mMyData.bestItem);
        user.child("Honey").setValue(mMyData.getUserHoney());

        user.child("ConnectDate").setValue(CommonFunc.getInstance().GetCurrentTime());

        user.child("Rejoin").setValue(mMyData.Rejoin);


        SaveUsersMyData(mMyData.getUserIdx());
    }

    public void SaveData(String userIdx) {

        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "SendList", mMyData.arrChatDataList);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "FanList", mMyData.arrMyFanRecvList);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "CardList", mMyData.arrCarDataList);


        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Age", mMyData.getUserAge());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "BestItem", mMyData.getUserBestItem());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ConnectDate", mMyData.ConnectDate);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Date", mMyData.getUserDate());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Dist", mMyData.getUserDist());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "FanCount", mMyData.getFanCount());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Gender", mMyData.getUserGender());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Grade", mMyData.getGrade());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Honey", mMyData.getUserHoney());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Idx", mMyData.getUserIdx());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Img", mMyData.getUserImg());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgCount", mMyData.getUserImgCnt());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgGroup0", mMyData.getUserProfileImg(0));
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgGroup1", mMyData.getUserProfileImg(1));
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgGroup2", mMyData.getUserProfileImg(2));
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "ImgGroup3", mMyData.getUserProfileImg(3));
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Lat", mMyData.getUserLat());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Lon", mMyData.getUserLon());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "NickChangeCnt", mMyData.NickChangeCnt);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "NickName", mMyData.getUserNick());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Point", mMyData.getPoint());
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "RecvGold", mMyData.nRecvGold);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "RecvMsgReject", mMyData.nRecvMsgReject ? 1 : 0);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "SendCount", mMyData.nSendCount);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_1", mMyData.item_1);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_2", mMyData.item_2);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_3", mMyData.item_3);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_4", mMyData.item_4);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_5", mMyData.item_5);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_6", mMyData.item_6);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_7", mMyData.item_7);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Item_8", mMyData.item_8);
        CommonFunc.getInstance().SetValue(CoomonValueData.getInstance().DATA_USERS, mMyData.getUserGender(), userIdx, "Memo", mMyData.getUserMemo());


        Map<String, Object> updateMap = new HashMap<>();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Users");//.child(mMyData.getUserIdx());

        // DatabaseReference user = table.child( userIdx);

        DatabaseReference user;
        if (mMyData.getUserGender().equals("여자")) {
            user = table.child("Woman").child(mMyData.getUserIdx());
        } else {
            user = table.child("Man").child(mMyData.getUserIdx());
        }

        if (FirebaseInstanceId.getInstance() == null || FirebaseInstanceId.getInstance().getToken() == null || FirebaseInstanceId.getInstance().getToken().equals("")) {
            System.exit(0);
        } else {
            mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
            //user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
            updateMap.put("Token", mMyData.getUserToken());
        }

        updateMap.put("Idx", mMyData.getUserIdx());
        updateMap.put("ImgCount", mMyData.getUserImgCnt());

        //user.child("Idx").setValue(mMyData.getUserIdx());
        // user.child("ImgCount").setValue(mMyData.getUserImgCnt());
        if (mMyData.getUserImgCnt() == 0) {
            mMyData.setUserImgCnt(1);
            updateMap.put("ImgCount", 1);
            // user.child("ImgCount").setValue(1);
        }

        //user.child("Img").setValue(mMyData.getUserImg());
        updateMap.put("Img", mMyData.getUserImg());

        for (int i = 0; i < 4; i++) {
            updateMap.put("ImgGroup" + Integer.toString(i), mMyData.getUserProfileImg(i));
        }
        //user.child("ImgGroup" + Integer.toString(i)).setValue(mMyData.getUserProfileImg(i));

        if (mMyData.getUserProfileImg(0).equals("1")) {
            mMyData.setUserProfileImg(0, mMyData.getUserImg());
            //user.child("ImgGroup0").setValue(mMyData.getUserImg());
            updateMap.put("ImgGroup0", mMyData.getUserImg());

        }
        updateMap.put("NickName", mMyData.getUserNick());
        updateMap.put("Gender", mMyData.getUserGender());
        updateMap.put("Age", mMyData.getUserAge());

        //    user.child("NickName").setValue(mMyData.getUserNick());
        //      user.child("Gender").setValue(mMyData.getUserGender());
//        user.child("Age").setValue(mMyData.getUserAge());

        updateMap.put("Lon", mMyData.getUserLon());
        updateMap.put("Lat", mMyData.getUserLat());
        updateMap.put("Dist", mMyData.getUserDist());

//        user.child("Lon").setValue(mMyData.getUserLon());
        //      user.child("Lat").setValue(mMyData.getUserLat());
        //    user.child("Dist").setValue(mMyData.getUserDist());

        updateMap.put("SendCount", mMyData.getSendHoney());
        updateMap.put("RecvGold", mMyData.getRecvHoney());

        //user.child("SendCount").setValue(mMyData.getSendHoney());
        //user.child("RecvGold").setValue(mMyData.getRecvHoney());


        updateMap.put("Memo", mMyData.getUserMemo());
        //user.child("Memo").setValue(mMyData.getUserMemo());

        updateMap.put("RecvMsgReject", mMyData.nRecvMsgReject ? 1 : 0);
        //user.child("RecvMsgReject").setValue(mMyData.nRecvMsgReject ? 1 : 0);

        updateMap.put("FanCount", -1 * (UNIQ_FANCOUNT * mMyData.arrMyFanList.size() + Long.valueOf(mMyData.getUserIdx())));
        //user.child("FanCount").setValue(-1 * (UNIQ_FANCOUNT * mMyData.arrMyFanList.size() + Long.valueOf(mMyData.getUserIdx())));

        updateMap.put("Point", mMyData.getPoint());
        //user.child("Point").setValue(mMyData.getPoint());

        updateMap.put("Grade", mMyData.getGrade());
        updateMap.put("BestItem", mMyData.bestItem);
        updateMap.put("Honey", mMyData.getUserHoney());

        //user.child("Grade").setValue(mMyData.getGrade());
        //user.child("BestItem").setValue(mMyData.bestItem);
        //user.child("Honey").setValue(mMyData.getUserHoney());

        updateMap.put("NickChangeCnt", mMyData.NickChangeCnt);
        //user.child("NickChangeCnt").setValue(mMyData.NickChangeCnt);

//        long time = CommonFunc.getInstance().GetCurrentTime();
  //      user.child("ConnectDate").setValue(-1 * time);

        user.updateChildren(updateMap);


        table = database.getReference("GenderList");//.child(mMyData.getUserIdx());
        user = table.child(userIdx);
        user.setValue(mMyData.getUserGender());


        // 심플 디비 저장
        SaveSimpleData();
    }

    public void SaveSimpleData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("SimpleData");//.child(mMyData.getUserIdx());

        Map<String, Object> updateMap = new HashMap<>();


        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(mMyData.getUserIdx());

        if (FirebaseInstanceId.getInstance() == null || FirebaseInstanceId.getInstance().getToken() == null || FirebaseInstanceId.getInstance().getToken().equals("")) {
            System.exit(0);
        } else {
            mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
            // user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
            updateMap.put("Token", mMyData.getUserToken());
        }

        updateMap.put("Idx", mMyData.getUserIdx());
        updateMap.put("Img", mMyData.getUserImg());

        // user.child("Idx").setValue(mMyData.getUserIdx());
        //user.child("Img").setValue(mMyData.getUserImg());

        updateMap.put("NickName", mMyData.getUserNick());
        updateMap.put("Gender", mMyData.getUserGender());
        updateMap.put("Age", mMyData.getUserAge());

        //  user.child("NickName").setValue(mMyData.getUserNick());
        //  user.child("Gender").setValue(mMyData.getUserGender());
        //  user.child("Age").setValue(mMyData.getUserAge());

        updateMap.put("Memo", mMyData.getUserMemo());

        // user.child("Memo").setValue(mMyData.getUserMemo());
        updateMap.put("SendCount", mMyData.getSendHoney());
        updateMap.put("RecvGold", mMyData.getRecvHoney());

        // user.child("RecvGold").setValue(mMyData.getRecvHoney());
        //  user.child("SendGold").setValue(mMyData.getSendHoney());
        updateMap.put("Lon", mMyData.getUserLon());
        updateMap.put("Lat", mMyData.getUserLat());
        updateMap.put("Dist", mMyData.getUserDist());

        // user.child("Lon").setValue(mMyData.getUserLon());
        //  user.child("Lat").setValue(mMyData.getUserLat());
        // user.child("Dist").setValue(mMyData.getUserDist());

        updateMap.put("FanCount", -1 * (UNIQ_FANCOUNT * mMyData.arrMyFanList.size() + Long.valueOf(mMyData.getUserIdx())));

        // user.child("FanCount").setValue(-1 * (UNIQ_FANCOUNT * mMyData.arrMyFanList.size() + Long.valueOf(mMyData.getUserIdx())));

        updateMap.put("Point", mMyData.getPoint());
        //user.child("Point").setValue(mMyData.getPoint());

        updateMap.put("Grade", mMyData.getGrade());
        updateMap.put("BestItem", mMyData.bestItem);
        updateMap.put("Honey", mMyData.getUserHoney());

        user.updateChildren(updateMap);

        // user.child("Grade").setValue(mMyData.getGrade());
        //   user.child("BestItem").setValue(mMyData.bestItem);
        //   user.child("Honey").setValue(mMyData.getUserHoney());

    }


    public boolean SaveBoardReplyData(TempBoard_ReplyData strMemo) {

        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board").child(strMemo.Key).child("Reply");

        TempBoard_ReplyData tempData = new TempBoard_ReplyData();

        tempData.Idx = strMemo.Idx;
        tempData.NickName = strMemo.NickName;
        tempData.Age = strMemo.Age;
        tempData.Img = strMemo.Img;
        tempData.Msg = strMemo.Msg;

        table.push().setValue(tempData);

        return true;
    }

    public void DelChatData(String Idx) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("/ChatData/").child(Idx);
        table.removeValue();
    }

    public void DelSendData(String Idx) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query queryRef = database.getReference("SendList").child(mMyData.getUserIdx()).orderByChild("strSendName").equalTo(Idx);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                snapshot.getRef().setValue(null);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SaveSettingData(String userIdx, int SearchMode, int ViewMode, boolean RecvMsgReject, boolean alarmSetting_Sound, boolean alarmSetting_Vibration, boolean alarmSetting_Popup) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Setting");

        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(mMyData.getUserIdx());

        user.child("SearchMode").setValue(SearchMode);
        user.child("ViewMode").setValue(ViewMode);
        user.child("RecvMsgReject").setValue((RecvMsgReject) ? 1 : 0);
        user.child("AlarmMode_Sound").setValue(alarmSetting_Sound);
        user.child("AlarmMode_Vibration").setValue(alarmSetting_Vibration);
        user.child("AlarmMode_Popup").setValue(alarmSetting_Popup);
        user.child("StartAge").setValue(mMyData.nStartAge);
        user.child("EndAge").setValue(mMyData.nEndAge);

        SaveData(mMyData.getUserIdx());

    }

    public void GetWriteAfterData(final Activity mActivity) {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = FirebaseDatabase.getInstance().getReference().child("Board").limitToFirst(FIRST_LOAD_BOARD_COUNT);

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                BoardData.getInstance().loadingCount = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BoardData.getInstance().loadingCount = dataSnapshot.getChildrenCount();
                    BoardMsgDBData DBData = postSnapshot.getValue(BoardMsgDBData.class);
                    BoardData.getInstance().AddBoardData(DBData);

                    Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(DBData.Idx);

                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null)
                                return;

                            SimpleUserData simpleUserData = dataSnapshot.getValue(SimpleUserData.class);
                            BoardData.getInstance().AddBoardSimpleUserData(simpleUserData);

                            BoardData.getInstance().loadingCount--;
                            if (BoardData.getInstance().loadingCount <= 0) {
                                mActivity.finish();
                                Fragment frg = null;
                                frg = mFragmentMng.findFragmentByTag("BoardFragment");
                                final FragmentTransaction ft = mFragmentMng.beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commitAllowingStateLoss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                // mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_BOARD);
   /*
                Intent intent = new_img Intent(mActivity, MainActivity.class);
                intent.putExtra("StartFragment", 4);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(intent);
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void GetInitBoardData() {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = FirebaseDatabase.getInstance().getReference().child("Board").limitToFirst(FIRST_LOAD_BOARD_COUNT);

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null)
                    return;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BoardMsgDBData DBData = postSnapshot.getValue(BoardMsgDBData.class);
                    BoardData.getInstance().AddBoardData(DBData);

                    Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(DBData.Idx);

                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null)
                                return;

                            SimpleUserData simpleUserData = dataSnapshot.getValue(SimpleUserData.class);
                            BoardData.getInstance().AddBoardSimpleUserData(simpleUserData);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void GetInitMyBoardData() {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        Query data = FirebaseDatabase.getInstance().getReference().child("Board").orderByChild("Idx").equalTo(mMyData.getUserIdx());

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null)
                    return;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BoardMsgDBData DBData = postSnapshot.getValue(BoardMsgDBData.class);
                    BoardData.getInstance().AddBoardMyData(DBData);

                    Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(DBData.Idx);

                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null)
                                return;

                            SimpleUserData simpleUserData = dataSnapshot.getValue(SimpleUserData.class);
                            BoardData.getInstance().AddBoardSimpleUserData(simpleUserData);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // 게시판 관련 함수 (환웅)
    BoardFragment.BoardListAdapter UpdateBoardAdapter = null;

    public void GetBoardData(BoardFragment.BoardListAdapter updateBoardAdapter, long startIdx, Boolean top) {
        UpdateBoardAdapter = updateBoardAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = null;
        if (top)
            data = fierBaseDataInstance.getReference().child("Board").orderByChild("BoardIdx").startAt(startIdx - LOAD_BOARD_COUNT).endAt(startIdx);
        else
            data = fierBaseDataInstance.getReference().child("Board").orderByChild("BoardIdx").startAt(startIdx).endAt(startIdx + LOAD_BOARD_COUNT); // TODO 환웅 게시판의 마지막에 있는 친구 인덱스를 가져 온다.


        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null)
                    return;

                if (dataSnapshot.getChildrenCount() <= 0) {
                    CommonFunc.getInstance().DismissLoadingPage();
                    UpdateBoardAdapter.BoardDataLoding = false;
                    return;
                }
                BoardData.getInstance().TempTopBoardIdx = BoardData.getInstance().TopBoardIdx;
                BoardData.getInstance().TempBottomBoardIdx = BoardData.getInstance().BottomBoardIdx;


                BoardData.getInstance().loadingCount = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BoardData.getInstance().loadingCount = dataSnapshot.getChildrenCount();
                    BoardMsgDBData DBData = postSnapshot.getValue(BoardMsgDBData.class);
                    BoardData.getInstance().AddBoardData(DBData);

                    Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(DBData.Idx);

                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null)
                                return;
                            BoardData.getInstance().loadingCount--;
                            SimpleUserData simpleUserData = dataSnapshot.getValue(SimpleUserData.class);
                            BoardData.getInstance().AddBoardSimpleUserData(simpleUserData);

                            if (BoardData.getInstance().loadingCount <= 0) {
                                CommonFunc.getInstance().DismissLoadingPage();
                                UpdateBoardAdapter.BoardDataLoding = false;
                                if (BoardData.getInstance().TempTopBoardIdx != BoardData.getInstance().TopBoardIdx || BoardData.getInstance().TempBottomBoardIdx != BoardData.getInstance().BottomBoardIdx)
                                    UpdateBoardAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private BoardWriteActivity WriteActivity = null;

    public void SaveBoardData_GetBoardIndex(final BoardWriteActivity activity) {
        WriteActivity = activity;
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
                WriteActivity.SendBoard();


                BoardData.getInstance().ClearBoardData();
                FirebaseData.getInstance().GetInitMyBoardData();
                FirebaseData.getInstance().GetWriteAfterData(activity);
            }
        });
    }

    public boolean SaveBoardData(BoardMsgDBData sendData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.DATE_FORMAT);

        DatabaseReference writeBoardTable = database.getReference("Board").child(Long.toString(BoardData.getInstance().BoardIdx));

        sendData.Date = CommonFunc.getInstance().GetCurrentTime();
        sendData.BoardIdx = BoardData.getInstance().BoardIdx;
        BoardData.getInstance().AddBoardData(sendData, true);
        writeBoardTable.setValue(sendData);

        DatabaseReference myTable;// = database.getReference("Users");

        if (mMyData.getUserGender().equals("여자")) {
            myTable = database.getReference("Users").child("Woman").child(MyData.getInstance().getUserIdx());//.child("LastBoardWriteTime");
        } else {
            myTable = database.getReference("Users").child("Man").child(MyData.getInstance().getUserIdx());//.child("LastBoardWriteTime");
        }

        MyData.getInstance().SetLastBoardWriteTime(CommonFunc.getInstance().GetCurrentTime());
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("LastBoardWriteTime", CommonFunc.getInstance().GetCurrentTime());

        myTable.updateChildren(updateMap);

        //myTable.setValue(CommonFunc.getInstance().GetCurrentTime());

      /*  DatabaseReference myTable = database.getReference("User").child(MyData.getInstance().getUserIdx()).child("LastBoardWriteTime");
        MyData.getInstance().SetLastBoardWriteTime(CommonFunc.getInstance().GetCurrentTime());
        myTable.setValue(CommonFunc.getInstance().GetCurrentTime());*/

        return true;
    }

    public void RemoveBoard(long boardIdx) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board").child(Long.toString(boardIdx));
        table.removeValue();
    }

    public void ReportBoard(long boardIdx, String Idx) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board").child(Long.toString(boardIdx)).child("ReportList").child(Idx);

        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.DATE_FORMAT);

        BoardReportData sendData = new BoardReportData();
        sendData.Idx = mMyData.getUserIdx();
        long time = CommonFunc.getInstance().GetCurrentTime();
        sendData.Date = ctime.format(new Date(time));

        table.setValue(sendData);
    }

    public void RefreshUserData(Activity activity) {

        CommonFunc.getInstance().ShowLoadingPage(activity, "로딩중");

        RefreshHot initHot = new RefreshHot(activity);
        initHot.execute(0, 0, 0);

        RefreshGoldReceiver initRecv = new RefreshGoldReceiver(activity);
        initRecv.execute(0, 0, 0);

     /*   RefreshFanCount initFan = new RefreshFanCount(activity);
        initFan.execute(0,0,0);*/

        RefreshNear initNear = new RefreshNear(activity);
        initNear.execute(0, 0, 0);

        RefreshNew initNew = new RefreshNew(activity);
        initNew.execute(0, 0, 0);

    }

    public class RefreshHot extends AsyncTask<Integer, Integer, Integer> {
        Activity mActivity;

        public RefreshHot(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyData.arrUserAll_Hot.clear();
            mMyData.arrUserAll_Hot_Age.clear();
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            DatabaseReference ref;
            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("HotMemberIdx").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("HotMemberIdx").child("Woman");
            }

            Query query = ref.limitToLast(HOTMEMBER_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                String tempValue = fileSnapshot.getValue(String.class);

                                if (tempValue != null || !tempValue.equals("")) {
                                    i++;
                                    final DatabaseReference Hotref;
                                    if (SettingData.getInstance().getnSearchSetting() == 1) {
                                        Hotref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man").child(tempValue);
                                    } else {
                                        Hotref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman").child(tempValue);
                                    }


                                    final int finalI = i;
                                    Hotref.addListenerForSingleValueEvent(
                                            new ValueEventListener() {

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    UserData stRecvData = new UserData();
                                                    stRecvData = dataSnapshot.getValue(UserData.class);
                                                    if (stRecvData != null) {
                                                        if (stRecvData != null && stRecvData.Idx != null) {
                                                            if (CommonFunc.getInstance().CheckUserData(stRecvData, dataSnapshot.getKey())) {
                                                                if (stRecvData.Img == null)
                                                                    stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                                                mMyData.arrUserAll_Hot.add(stRecvData);

                                                                mMyData.mapGenderData.put(stRecvData.Idx, stRecvData.Gender);

                                                                if (mMyData.arrUserAll_Hot.size() == HOTMEMBER_LOAD_MAIN_COUNT) {

                                                                    CommonFunc.AscendingObj ascending = new CommonFunc.AscendingObj();

                                                                    Collections.sort(mMyData.arrUserAll_Hot, ascending);

                                                                    mMyData.arrUserAll_Hot_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Hot, mMyData.nStartAge, mMyData.nEndAge);

                                                                    if (mMyData.arrUserAll_Hot.size() > 0)
                                                                        mMyData.HotIndexRef = mMyData.arrUserAll_Hot.get(mMyData.arrUserAll_Hot.size() - 1).Date;

                                                                    bRefreshSetHot = true;
                                                                    if (bRefreshSetHot == true && bRefreshSetNear == true && bRefreshSetFan == true && bRefreshSetNew == true && bRefreshSetRecv == true) {
                                                                        CommonFunc.getInstance().refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME, 0, 0);
                                                                    }
                                                                }

                                                            }
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }


    public class RefreshGoldReceiver extends AsyncTask<Integer, Integer, Integer> {
        Activity mActivity;

        public RefreshGoldReceiver(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyData.arrUserAll_Recv.clear();
            mMyData.arrUserAll_Recv_Age.clear();
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {

            DatabaseReference ref;
            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            Query query = ref.orderByChild("RecvGold").limitToFirst(FIRST_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData cTempData = new UserData();
                                cTempData = fileSnapshot.getValue(UserData.class);
                                if (cTempData != null && cTempData.Idx != null) {
                                    if (CommonFunc.getInstance().CheckUserData(cTempData, fileSnapshot.getKey())) {
                                        if (cTempData.Img == null)
                                            cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_Recv.add(cTempData);
                                        mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);
                                        i++;
                                    }
                                }
                            }

                            mMyData.arrUserAll_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Recv, mMyData.nStartAge, mMyData.nEndAge);

                            if (mMyData.arrUserAll_Recv.size() > 0)
                                mMyData.RecvIndexRef = mMyData.arrUserAll_Recv.get(mMyData.arrUserAll_Recv.size() - 1).RecvGold;

                            bRefreshSetRecv = true;
                            if (bRefreshSetHot == true && bRefreshSetNear == true && bRefreshSetFan == true && bRefreshSetNew == true && bRefreshSetRecv == true) {
                                CommonFunc.getInstance().refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME, 0, 0);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }

    public class RefreshFanCount extends AsyncTask<Integer, Integer, Integer> {
        Activity mActivity;

        public RefreshFanCount(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyData.arrUserAll_Send.clear();
            mMyData.arrUserAll_Send_Age.clear();
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            DatabaseReference ref;

            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            Query query = ref.orderByChild("FanCount").limitToFirst(FIRST_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData cTempData = new UserData();
                                cTempData = fileSnapshot.getValue(UserData.class);

                                if (cTempData != null && cTempData.Idx != null) {
                                    if (CommonFunc.getInstance().CheckUserData(cTempData, fileSnapshot.getKey())) {
                                        if (cTempData.Img == null)
                                            cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_Send.add(cTempData);
                                        mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);
                                        i++;
                                    }
                                }
                            }

                            mMyData.arrUserAll_Send_Age = mMyData.SortData_Age(mMyData.arrUserAll_Send, mMyData.nStartAge, mMyData.nEndAge);

                            if (mMyData.arrUserAll_Send.size() > 0)
                                mMyData.FanCountRef = mMyData.arrUserAll_Send.get(mMyData.arrUserAll_Send.size() - 1).FanCount;

                            bRefreshSetFan = true;
                            if (bRefreshSetHot == true && bRefreshSetNear == true && bRefreshSetFan == true && bRefreshSetNew == true && bRefreshSetRecv == true) {
                                CommonFunc.getInstance().refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME, 0, 0);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                        }
                    });
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }

    public class RefreshNear extends AsyncTask<Integer, Integer, Integer> {
        Activity mActivity;

        public RefreshNear(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyData.arrUserAll_Near.clear();

            mMyData.arrUserAll_Near_Age.clear();
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            DatabaseReference ref;

            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            Query query = ref
                    .orderByChild("Dist").limitToFirst(FIRST_LOAD_MAIN_COUNT);

            final double[] tempDist = {0};
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0, j = 0, k = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData stRecvData = new UserData();
                                stRecvData = fileSnapshot.getValue(UserData.class);
                                if (stRecvData != null) {

                                    if (stRecvData.Lat == 0 || stRecvData.Lon == 0) {
                                        // 위치 못받아오는 애들
                                    } else {
                                        if (CommonFunc.getInstance().CheckUserData(stRecvData, fileSnapshot.getKey())) {
                                            if (stRecvData.Img == null)
                                                stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                            tempDist[0] = stRecvData.Dist;
                                            double Dist = LocationFunc.getInstance().getDistance(mMyData.getUserLat(), mMyData.getUserLon(), stRecvData.Lat, stRecvData.Lon, "meter");
                                            stRecvData.Dist = Dist;

                                            mMyData.arrUserAll_Near.add(stRecvData);
                                            mMyData.mapGenderData.put(stRecvData.Idx, stRecvData.Gender);

                                            i++;
                                        }
                                    }
                                }
                            }

                            if (mMyData.arrUserAll_Near.size() > 0)
                                mMyData.NearDistanceRef = tempDist[0]; //mMyData.arrUserAll_Near.get(mMyData.arrUserAll_Near.size()-1).Dist;

                            CommonFunc.NearSort cNearSort = new CommonFunc.NearSort();
                            Collections.sort(mMyData.arrUserAll_Near, cNearSort);

                            mMyData.arrUserAll_Near_Age = mMyData.SortData_Age(mMyData.arrUserAll_Near, mMyData.nStartAge, mMyData.nEndAge);

                            bRefreshSetNear = true;
                            if (bRefreshSetHot == true && bRefreshSetNear == true && bRefreshSetFan == true && bRefreshSetNew == true && bRefreshSetRecv == true) {
                                CommonFunc.getInstance().refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME, 0, 0);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                        }
                    });
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }

    public class RefreshNew extends AsyncTask<Integer, Integer, Integer> {
        Activity mActivity;

        public RefreshNew(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMyData.arrUserAll_New.clear();

            mMyData.arrUserAll_New_Age.clear();

            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {

            DatabaseReference ref;
            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            Query query = ref.orderByChild("Date").limitToFirst(FIRST_LOAD_MAIN_COUNT);

            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0, j = 0, k = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData stRecvData = new UserData();
                                stRecvData = fileSnapshot.getValue(UserData.class);
                                if (stRecvData != null) {
                                    if (CommonFunc.getInstance().CheckUserData(stRecvData, fileSnapshot.getKey())) {
                                        if (stRecvData.Img == null)
                                            stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_New.add(stRecvData);
                                        mMyData.mapGenderData.put(stRecvData.Idx, stRecvData.Gender);

                                        i++;
                                    }
                                }
                            }

                            mMyData.arrUserAll_New_Age = mMyData.SortData_Age(mMyData.arrUserAll_New, mMyData.nStartAge, mMyData.nEndAge);

                            if (mMyData.arrUserAll_New.size() > 0)
                                mMyData.NewDateRef = mMyData.arrUserAll_New.get(mMyData.arrUserAll_New.size() - 1).Date;

                            bRefreshSetNew = true;
                            if (bRefreshSetHot == true && bRefreshSetNear == true && bRefreshSetFan == true && bRefreshSetNew == true && bRefreshSetRecv == true) {
                                CommonFunc.getInstance().refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME, 0, 0);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                        }
                    });
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }


    Rank_HotAdapter UpdateAdapter = null;

    public void GetHotData(Rank_HotAdapter updateAdapter, Boolean top) {
        UpdateAdapter = updateAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = null;
        if (top) {
            data = fierBaseDataInstance.getReference().child("HotMember").orderByChild("Date").limitToFirst(LOAD_MAIN_COUNT);
        } else {
            DatabaseReference ref;
            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("HotMember").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("HotMember").child("Woman");
            }

            data = ref.orderByChild("Date").startAt(mMyData.HotIndexRef).limitToFirst(LOAD_MAIN_COUNT);
        }
        //data = fierBaseDataInstance.getReference().child("HotMember").orderByChild("Point").startAt(mMyData.HotIndexRef).limitToFirst(LOAD_MAIN_COUNT);


        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserData cTempData = new UserData();
                    cTempData = postSnapshot.getValue(UserData.class);
                    if (cTempData != null && cTempData.Idx != null) {
                        if (CommonFunc.getInstance().CheckUserData(cTempData, postSnapshot.getKey())) {
                            if (cTempData.Img == null)
                                cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                            boolean bExist = false;

                            for (int j = 0; j < mMyData.arrUserAll_Hot.size(); j++) {
                                if (mMyData.arrUserAll_Hot.get(j).Idx.equals(cTempData.Idx)) {
                                    bExist = true;
                                    break;
                                }
                            }

                            if (bExist == false) {
                                mMyData.arrUserAll_Hot.add(cTempData);
                                mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);
                            }
                            i++;
                        }
                    }
                }

                mMyData.arrUserAll_Hot_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Hot, mMyData.nStartAge, mMyData.nEndAge);

                if (mMyData.arrUserAll_Hot.size() > 0)
                    mMyData.HotIndexRef = mMyData.arrUserAll_Hot.get(mMyData.arrUserAll_Hot.size() - 1).Date;
                //mMyData.HotIndexRef = mMyData.arrUserAll_Recv.get(mMyData.arrUserAll_Recv.size()-1).Point;
                CommonFunc.getInstance().DismissLoadingPage();
                UpdateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    Rank_GoldReceiveAdapter UpdateHotAdapter = null;

    public void GetRecvData(Rank_GoldReceiveAdapter updateAdapter, Boolean top) {
        UpdateHotAdapter = updateAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = null;
        if (top) {
            data = fierBaseDataInstance.getReference().child("User").orderByChild("RecvGold").limitToFirst(LOAD_MAIN_COUNT);
        } else {
            DatabaseReference ref;
            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            data = ref.orderByChild("RecvGold").startAt(mMyData.RecvIndexRef).limitToFirst(LOAD_MAIN_COUNT);
        }
        //data = fierBaseDataInstance.getReference().child("HotMember").orderByChild("Point").startAt(mMyData.HotIndexRef).limitToFirst(LOAD_MAIN_COUNT);


        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserData cTempData = new UserData();
                    cTempData = postSnapshot.getValue(UserData.class);
                    if (cTempData != null && cTempData.Idx != null) {
                        if (CommonFunc.getInstance().CheckUserData(cTempData, postSnapshot.getKey())) {
                            if (cTempData.Img == null)
                                cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                            boolean bExist = false;

                            for (int j = 0; j < mMyData.arrUserAll_Recv.size(); j++) {
                                if (mMyData.arrUserAll_Recv.get(j).Idx.equals(cTempData.Idx)) {
                                    bExist = true;
                                    break;
                                }
                            }

                            if (bExist == false) {
                                mMyData.arrUserAll_Recv.add(cTempData);
                                mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);
                            }
                            i++;
                        }
                    }
                }

                mMyData.arrUserAll_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Recv, mMyData.nStartAge, mMyData.nEndAge);

                if (mMyData.arrUserAll_Recv.size() > 0)
                    mMyData.RecvIndexRef = mMyData.arrUserAll_Recv.get(mMyData.arrUserAll_Recv.size() - 1).RecvGold;
                //mMyData.HotIndexRef = mMyData.arrUserAll_Recv.get(mMyData.arrUserAll_Recv.size()-1).Point;
                CommonFunc.getInstance().DismissLoadingPage();
                UpdateHotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    Rank_FanRichAdapter UpdateFanAdapter = null;

    public void GetFanData(Rank_FanRichAdapter updateAdapter, long lastVisibleCount, Boolean top) {
        UpdateFanAdapter = updateAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = null;
        if (top)
            data = fierBaseDataInstance.getReference().child("SimpleData").orderByChild("FanCount").startAt(0).endAt(LOAD_MAIN_COUNT);
        else {
            DatabaseReference ref;
            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            data = ref.orderByChild("FanCount").startAt(mMyData.FanCountRef).limitToFirst(LOAD_MAIN_COUNT);

        }

        //data = FirebaseDatabase.getInstance().getReference().child("TestHotMember").child("Man");

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserData cTempData = new UserData();
                    cTempData = postSnapshot.getValue(UserData.class);
                    if (cTempData != null && cTempData.Idx != null) {

                        if (CommonFunc.getInstance().CheckUserData(cTempData, postSnapshot.getKey())) {
                            if (cTempData.Img == null)
                                cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                            boolean bExist = false;

                            for (int j = 0; j < mMyData.arrUserAll_Send.size(); j++) {
                                if (mMyData.arrUserAll_Send.get(j).Idx.equals(cTempData.Idx)) {
                                    bExist = true;
                                    break;
                                }
                            }

                            if (bExist == false) {
                                mMyData.arrUserAll_Send.add(cTempData);
                                mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);
                            }
                            i++;
                        }
                    }
                }

                mMyData.arrUserAll_Send_Age = mMyData.SortData_Age(mMyData.arrUserAll_Send, mMyData.nStartAge, mMyData.nEndAge);

                if (mMyData.arrUserAll_Send.size() > 0)
                    mMyData.FanCountRef = mMyData.arrUserAll_Send.get(mMyData.arrUserAll_Send.size() - 1).FanCount;

                CommonFunc.getInstance().DismissLoadingPage();
                UpdateFanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    Rank_NearAdapter UpdateNearAdapter = null;

    public void GetNearData(Rank_NearAdapter updateAdapter, int lastVisibleCount, Boolean top) {
        UpdateNearAdapter = updateAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.

        final double[] tempDist = {0};

        Query data = null;
        {
            DatabaseReference ref;
            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }
            data = ref.orderByChild("Dist").startAt(mMyData.NearDistanceRef).limitToFirst(LOAD_MAIN_COUNT);
        }

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserData cTempData = new UserData();
                    cTempData = postSnapshot.getValue(UserData.class);
                    if (cTempData != null && cTempData.Idx != null) {

                        if (cTempData.Lat == 0 || cTempData.Lon == 0) {
                            // 위치 못받아오는 애들
                        } else {
                            if (CommonFunc.getInstance().CheckUserData(cTempData, postSnapshot.getKey())) {

                                if (cTempData.Img == null)
                                    cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                boolean bExist = false;

                                for (int j = 0; j < mMyData.arrUserAll_Near.size(); j++) {
                                    if (mMyData.arrUserAll_Near.get(j).Idx.equals(cTempData.Idx)) {
                                        bExist = true;
                                        break;
                                    }
                                }

                                if (bExist == false) {

                                    tempDist[0] = cTempData.Dist;
                                    double Dist = LocationFunc.getInstance().getDistance(mMyData.getUserLat(), mMyData.getUserLon(), cTempData.Lat, cTempData.Lon, "meter");

                                    cTempData.Dist = Dist;
                                    mMyData.arrUserAll_Near.add(cTempData);
                                    mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);
                                }
                                i++;
                            }
                        }

                    }

                }

                if (mMyData.arrUserAll_Near.size() > 0)
                    //mMyData.NearDistanceRef = mMyData.arrUserAll_Near.get(mMyData.arrUserAll_Near.size()-1).Dist;
                    mMyData.NearDistanceRef = tempDist[0]; //mMyData.arrUserAll_Near.get(mMyData.arrUserAll_Near.size()-1).Dist;


                CommonFunc.NearSort cNearSort = new CommonFunc.NearSort();
                Collections.sort(mMyData.arrUserAll_Near, cNearSort);

                mMyData.arrUserAll_Near_Age = mMyData.SortData_Age(mMyData.arrUserAll_Near, mMyData.nStartAge, mMyData.nEndAge);

                CommonFunc.getInstance().DismissLoadingPage();
                UpdateNearAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    Rank_NewMemberAdapter UpdateNewAdapter = null;

    public void GetNewData(Rank_NewMemberAdapter updateAdapter, int lastVisibleCount, Boolean top) {
        UpdateNewAdapter = updateAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        long time = CommonFunc.getInstance().GetCurrentTime();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
        int nTodayDate = Integer.parseInt(ctime.format(new Date(time)));
        int nStartDate = nTodayDate - 7;

        Query data = null;
        if (top)
            data = fierBaseDataInstance.getReference().child("SimpleData").orderByChild("FanCount").startAt(0).endAt(LOAD_MAIN_COUNT);
        else {
            DatabaseReference ref;
            if (SettingData.getInstance().getnSearchSetting() == 1) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            data = ref.orderByChild("Date").startAt(mMyData.NewDateRef).limitToFirst(LOAD_MAIN_COUNT);
        }


        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserData cTempData = new UserData();
                    cTempData = postSnapshot.getValue(UserData.class);
                    if (cTempData != null && cTempData.Idx != null) {
                        if (CommonFunc.getInstance().CheckUserData(cTempData, postSnapshot.getKey())) {
                            if (cTempData.Img == null)
                                cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                            boolean bExist = false;

                            for (int j = 0; j < mMyData.arrUserAll_New.size(); j++) {
                                if (mMyData.arrUserAll_New.get(j).Idx.equals(cTempData.Idx)) {
                                    bExist = true;
                                    break;
                                }
                            }

                            if (bExist == false) {
                                mMyData.arrUserAll_New.add(cTempData);
                                mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);
                            }
                            i++;
                        }
                    }
                }

                mMyData.arrUserAll_New_Age = mMyData.SortData_Age(mMyData.arrUserAll_New, mMyData.nStartAge, mMyData.nEndAge);

                if (mMyData.arrUserAll_New.size() > 0)
                    mMyData.NewDateRef = mMyData.arrUserAll_New.get(mMyData.arrUserAll_New.size() - 1).Date;

                CommonFunc.getInstance().DismissLoadingPage();
                UpdateNewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SaveLastAdsTime(long time) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.DATE_FORMAT);

        DatabaseReference myTable = database.getReference("Users");

        if (mMyData.getUserGender().equals("여자")) {
            myTable = myTable.child("Woman").child(MyData.getInstance().getUserIdx());
        } else {
            myTable = myTable.child("Man").child(MyData.getInstance().getUserIdx());
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("LastAdsTime", Long.valueOf(time));
        myTable.updateChildren(updateMap);

    }

    public void SaveMyGrade() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myTable = database.getReference("Users");

        if (mMyData.getUserGender().equals("여자")) {
            myTable = myTable.child("Woman").child(MyData.getInstance().getUserIdx());
        } else {
            myTable = myTable.child("Man").child(MyData.getInstance().getUserIdx());
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("Point", mMyData.getPoint());
        updateMap.put("Grade", mMyData.getGrade());
        myTable.updateChildren(updateMap);
    }

    public void SaveMyCoin() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myTable = database.getReference("Users");

        if (MyData.getInstance().getUserGender().equals("여자")) {
            myTable = myTable.child("Woman").child(MyData.getInstance().getUserIdx());
        } else {
            myTable = myTable.child("Man").child(MyData.getInstance().getUserIdx());
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("Honey", MyData.getInstance().getUserHoney());
        myTable.updateChildren(updateMap);
    }

    public void GetMyCoin() {

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
                int tempValue = dataSnapshot.getValue(int.class);
                MyData.getInstance().setUserHoney(tempValue);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void SetSubStatus(Context context, String status) {

        int substatus = 0;

        if (status == null || status.equals(""))
        {
            substatus = 0;
        }
        else if (status.equals(mMyData.skuGold[7])) {
            substatus = CoomonValueData.SUBSTATUS_WEEK;
            CommonFunc.getInstance().ShowToast(context, "1주일 구독하였습니다. 감사합니다", true);
        } else if (status.equals(mMyData.skuGold[8])) {
            substatus = CoomonValueData.SUBSTATUS_MONTH;
            CommonFunc.getInstance().ShowToast(context, "1주일 구독하였습니다. 감사합니다", true);
        } else if (status.equals(mMyData.skuGold[9])) {
            substatus = CoomonValueData.SUBSTATUS_YEAR;
            CommonFunc.getInstance().ShowToast(context, "1주일 구독하였습니다. 감사합니다", true);
        }


        MyData.getInstance().SubStatus = substatus;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myTable = database.getReference("Users");

        if (MyData.getInstance().getUserGender().equals("여자")) {
            myTable = myTable.child("Woman").child(MyData.getInstance().getUserIdx());
        } else {
            myTable = myTable.child("Man").child(MyData.getInstance().getUserIdx());
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("SubStatus",  MyData.getInstance().SubStatus );

        if(MyData.getInstance().SubStatus != 0)
            updateMap.put("SubDate", CommonFunc.getInstance().GetCurrentTime());

        myTable.updateChildren(updateMap);

        SaveMyCoin();
    }

}
