package com.hodo.jjamtalk.Data;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.usermgmt.response.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by boram on 2017-08-04.
 */

public class MyData {

    private static MyData _Instance;
    private BlockData blockList;

    public static MyData getInstance()
    {
        if(_Instance == null)
            _Instance = new MyData();

        return  _Instance;
    }

    public ArrayList<UserData> arrUserMan_Near = new ArrayList<>();
    public ArrayList<UserData> arrUserWoman_Near = new ArrayList<>();
    public ArrayList<UserData> arrUserAll_Near = new ArrayList<>();

    public ArrayList<UserData> arrUserMan_New = new ArrayList<>();
    public ArrayList<UserData> arrUserWoman_New = new ArrayList<>();
    public ArrayList<UserData> arrUserAll_New = new ArrayList<>();

    public ArrayList<UserData> arrUserMan_Send = new ArrayList<>();
    public ArrayList<UserData> arrUserWoman_Send = new ArrayList<>();
    public ArrayList<UserData> arrUserAll_Send = new ArrayList<>();

    public ArrayList<UserData> arrUserMan_Recv = new ArrayList<>();
    public ArrayList<UserData> arrUserWoman_Recv = new ArrayList<>();
    public ArrayList<UserData> arrUserAll_Recv = new ArrayList<>();

    public ArrayList<FanData> arrMyFanList = new ArrayList<>();
    public ArrayList<UserData> arrMyFanDataList = new ArrayList<>();

    public ArrayList<FanData> arrMyStarList = new ArrayList<>();
    public ArrayList<UserData> arrMyStarDataList = new ArrayList<>();


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

    public int nSearchMode;
    public int nAlarmMode = 7;
    public int nViewMode =1;
    public int nRecvMsg = 0;

    public int nImgCount;
    public String[] strProfileImg = new String[4];


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

    public ArrayList<String> arrSendNameList = new ArrayList<>();
    public ArrayList<SendData> arrSendDataList = new ArrayList<>();

    public ArrayList<String> arrCardNameList = new ArrayList<>();
    public ArrayList<UserData> arrCardList = new ArrayList<>();

    private MyData()
    {
        strImg = null;
        strNick = null;
        strGender = null;
        strAge = null;
        lLon = 0f;
        lLat = 0f;
        nHoney = 0;
        strDate = null;

        for(int i = 0 ; i< 4; i++)
        {
            strProfileImg[i] = "http://imagescdn.gettyimagesbank.com/500/14/730/414/0/512600801.jpg";
        }
//        strProfileImg = null;

        strMemo = null;

    }

    public void setMyData(String _UserIdx, int _UserImgCount, String _UserImg, String _UserImgGroup0, String _UserImgGroup1, String _UserImgGroup2, String _UserImgGroup3,
                          String _UserNick, String _UserGender, String _UserAge, Double _UserLon, Double _UserLat,
                          int _UserHoney,  int _UserSendCount,  int _UserRecvCount,  String _UserDate,
                          String _UserMemo, int _UserRecvMsg)
    {
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

        strProfileImg[0] = _UserImg;
        strProfileImg[1] = _UserImgGroup1;
        strProfileImg[2] = _UserImgGroup2;
        strProfileImg[3] = _UserImgGroup3;

        nRecvMsg = _UserRecvMsg;
    }

    public void setUserIdx(String userIdx) {
        strIdx = userIdx;
    }
    public String getUserIdx() {
        return strIdx;
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

    public void setUserLon(Double userLon) { lLon = userLon;}
    public Double getUserLon() {
        return lLon;
    }

    public void setUserLat(Double userLat) {
        lLat = userLat;
    }
    public Double getUserLat() { return lLat;   }

    public void setUserHoney(int userHoney) {
        nHoney = userHoney;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("User/"+ strIdx);

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


    public void setnRecvMsg(int option) {
        nRecvMsg = option;
    }
    public int getnRecvMsg() {
        return nRecvMsg;
    }



    public void setSendHoneyCnt(int sendHoneyCnt) {
        nSendCount -= sendHoneyCnt;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;

        table = database.getReference("User/"+ strIdx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("SendCount", nSendCount);
        table.updateChildren(updateMap);
    }

    public void setRecvHoneyCnt(int recvHoneyCnt) {
        nRecvCount -= recvHoneyCnt;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("User/"+ strIdx);

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

    public boolean makeSendList(UserData _UserData, String _strSend)
    {
        boolean rtValue = false;

        UserData SaveUserData = _UserData;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user, targetuser;
        table = database.getReference("SendList");

        user = table.child(strIdx);
        targetuser = table.child(_UserData.Idx);

        String strCheckName = strIdx + "_" + _UserData.Idx;
        SendData tempMySave = new SendData();
        tempMySave.strTargetImg = getUserImg();
        tempMySave.strTargetNick = getUserNick();
        tempMySave.strSendName = strCheckName;
        tempMySave.strTargetMsg = _strSend.toString();

        SendData tempTargetSave = new SendData();
        tempTargetSave.strTargetNick = _UserData.NickName;
        tempTargetSave.strTargetImg = _UserData.Img;
        tempTargetSave.strSendName = strCheckName;
        tempTargetSave.strTargetMsg = _strSend.toString();

        if(!arrSendNameList.contains(strCheckName)) {
            user.push().setValue(tempTargetSave);
            targetuser.push().setValue(tempMySave);
            rtValue = true;

        }
        else
            return rtValue;

        return rtValue;
    }

    public void getSendList() {
        String MyID =  strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("SendList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa =0;
                SendData SendList= dataSnapshot.getValue(SendData.class);
                if(!arrSendNameList.contains(SendList.strSendName)) {
                    arrSendNameList.add(SendList.strSendName);
                    arrSendDataList.add(SendList);
                }
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int saa =0;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa =0;
                SendData SendList= dataSnapshot.getValue(SendData.class);
                int index = arrSendNameList.indexOf(SendList.strSendName);
                arrSendNameList.remove(index);

                for(Iterator<SendData> it = arrSendDataList.iterator(); it.hasNext() ; )
                {
                    String value = it.next().strSendName;

                    if(value.equals(SendList.strSendName))
                    {
                        it.remove();
                    }
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa =0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }


    public boolean makeCardList(UserData _UserData)
    {
        boolean rtValue = false;

        UserData SaveUserData = _UserData;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("CardList");

        user = table.child(strIdx).child(_UserData.Idx);

        if(!arrCardNameList.contains(_UserData.Idx)) {
            user.setValue(_UserData);
            rtValue = true;
        }
        else
            return rtValue;

        return rtValue;

    }

    public void getCardList() {
        String MyID =  strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("CardList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa =0;
                UserData CardList= dataSnapshot.getValue(UserData.class);
                if(!arrCardNameList.contains(CardList.Idx))
                    arrCardNameList.add(CardList.Idx);
                    arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa =0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa =0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void setProfileData(Editable memo) {
        strMemo = memo.toString();
    }

    public void setSettingData(int SearchMode, int AlarmMode, int ViewMode, int RecvMsg) {
        nSearchMode = SearchMode;
        nAlarmMode= AlarmMode;
        nViewMode = ViewMode;
        nRecvMsg = RecvMsg;
    }

    public void getGiftData(String Idx) {

        arrMyStarDataList.clear();

        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");
        Log.d("!!!!!!", "getMyStarData  " + arrMyStarList.size());

            strTargetIdx = Idx;

            Log.d("!!!!!!", "size OK  " + arrMyStarList.size());

            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserData tempUserData = dataSnapshot.getValue(UserData.class);
                    arrGiftUserDataList.add(tempUserData);

                    int i = arrGiftUserDataList.size();
                    for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.StarList.entrySet()) {
                        //  if(!arrMyStarDataList.get(finalI).arrStarList.contains(entry.getValue().Nick))
                        arrGiftUserDataList.get(i-1).arrStarList.add(entry.getValue());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }

    public void getGiftHoneyList() {
        String MyID =  strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("GiftHoneyList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa =0;
                SendData SendList= dataSnapshot.getValue(SendData.class);
                arrGiftHoneyNameList.add(SendList.strSendName);
                arrGiftHoneyDataList.add(SendList);
                getGiftData(SendList.strSendName);
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa =0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa =0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getRecvHoneyList() {
        String MyID =  strIdx;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("RecvHoneyList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa =0;
                SendData SendList= dataSnapshot.getValue(SendData.class);
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

                int saa =0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa =0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getSendHoneyList() {
        String MyID =  strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("SendHoneyList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa =0;
                SendData SendList= dataSnapshot.getValue(SendData.class);
                    arrSendHoneyNameList.add(SendList.strSendName);
                    arrSendHoneyDataList.add(SendList);
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa =0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa =0;
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

        long now = System.currentTimeMillis();
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

        makeGiftHoneyList(_UserData,SendHoneyCnt,SendMsg);

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

        long now = System.currentTimeMillis();
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

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String getTime = sdf.format(date);

        tempMySave.strSendDate = getTime;

        targetuser.push().setValue(tempMySave);
        rtValue = true;


        return rtValue;
    }

    public void makeBlockList(SendData blockList) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user, target;
        table = database.getReference("BlockList");


        BlockData tempData = new BlockData();

        tempData.strTargetImg = blockList.strTargetImg;
        tempData.strTargetNick = blockList.strTargetNick;
        tempData.strTargetMsg = blockList.strTargetMsg;
        tempData.strSendName = blockList.strSendName;

        BlockData targetData = new BlockData();

        targetData.strTargetImg = getUserImg();
        targetData.strTargetNick = getUserNick();
        targetData.strTargetMsg = getUserMemo();
        targetData.strSendName = getUserIdx();

        int idx = blockList.strSendName.indexOf("_");
        String temp1 = blockList.strSendName.substring(0, idx);
        String temp2 = blockList.strSendName.substring(idx+1);

        if(getUserIdx().equals(temp1))
        {
            tempData.strTargetName = temp2;
            targetData.strTargetName = temp1;
        }
        else
        {
            tempData.strTargetName = temp1;
            targetData.strTargetName = temp2;
        }

        user = table.child(strIdx);
        user.push().setValue(tempData);

        target = database.getReference("BlockedList").child(tempData.strTargetName );
        //target = table
        target.push().setValue(targetData);
    }

    public void getBlockList() {
        String MyID =  strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("BlockList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa =0;
                blockList= dataSnapshot.getValue(BlockData.class);
                arrBlockDataList.add(blockList);
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa =0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa =0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    public void getBlockedList() {
        String MyID =  strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("BlockedList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa =0;
                blockList= dataSnapshot.getValue(BlockData.class);
                arrBlockedDataList.add(blockList);
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int saa =0;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                int saa =0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


    public void delBlockList(SendData blockList) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user, target;
        table = database.getReference("BlockList");


        BlockData tempData = new BlockData();

        tempData.strTargetImg = blockList.strTargetImg;
        tempData.strTargetNick = blockList.strTargetNick;
        tempData.strTargetMsg = blockList.strTargetMsg;
        tempData.strSendName = blockList.strSendName;

        BlockData targetData = new BlockData();

        targetData.strTargetImg = getUserImg();
        targetData.strTargetNick = getUserNick();
        targetData.strTargetMsg = getUserMemo();
        targetData.strSendName = getUserIdx();

        int idx = blockList.strSendName.indexOf("_");
        String temp1 = blockList.strSendName.substring(0, idx);
        String temp2 = blockList.strSendName.substring(idx+1);

        if(getUserIdx().equals(temp1))
        {
            tempData.strTargetName = temp2;
            targetData.strTargetName = temp1;
        }
        else
        {
            tempData.strTargetName = temp1;
            targetData.strTargetName = temp2;
        }

        user = table.child(strIdx);
        user.push().setValue(tempData);

        target = database.getReference("BlockedList").child(tempData.strTargetName );
        //target = table
        target.push().setValue(targetData);
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
                        TempSettingData stRecvData = new TempSettingData ();
                        stRecvData = dataSnapshot.getValue(TempSettingData.class);
                        if(stRecvData != null) {
                            nSearchMode = stRecvData.SearchMode;
                            nAlarmMode = stRecvData.AlarmMode;
                            nViewMode = stRecvData.ViewMode;
                            nRecvMsg = stRecvData.RecvMsg;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });

    }
    public void makeFanList(UserData stTargetData, int SendCount) {


        int nTotalSendCnt = 0;
        for(int i=0; i<arrSendHoneyDataList.size(); i++)
        {
            if(arrSendHoneyDataList.get(i).strTargetNick.equals(stTargetData.NickName))
                nTotalSendCnt -= arrSendHoneyDataList.get(i).nSendHoney;
        }

        nTotalSendCnt -= SendCount;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("User/"+ stTargetData.Idx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("Count", nTotalSendCnt);
        updateMap.put("Nick", getUserNick());
        updateMap.put("Idx", getUserIdx());
        table.child("FanList").child(getUserIdx()).updateChildren(updateMap);

    }

    public void getMyfanData() {
        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");


        //user.addChildEventListener(new ChildEventListener() {
        for(int i=0; i<arrMyFanList.size(); i++)
        {
            strTargetIdx = arrMyFanList.get(i).Idx;

            final int finalI = i;
            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int saa =0;
                    UserData tempUserData = dataSnapshot.getValue(UserData.class);
                    arrMyFanDataList.add(tempUserData);

                    for(LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet())
                        arrMyFanDataList.get(finalI).arrFanList.add(entry.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
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
        updateMap.put("Count", nTotalSendCnt);
        updateMap.put("Nick", stTargetData.NickName);
        updateMap.put("Idx", stTargetData.Idx);
        table.child("StarList").child(stTargetData.Idx).updateChildren(updateMap);

        FanData tempStarList = new FanData();
        tempStarList.Nick = stTargetData.NickName;
        tempStarList.Idx = stTargetData.Idx;
        tempStarList.Count = nTotalSendCnt;


        boolean bSame = false;
        for (int i = 0; i < arrMyStarList.size(); i++) {
            if (arrMyStarList.get(i).Nick.equals(tempStarList.Nick)) {
                bSame = true;
                arrMyStarList.get(i).Count = nTotalSendCnt;
                sortStarData();
                break;
            }
        }

        if (bSame == false) {
            arrMyStarList.add(tempStarList);
            sortStarData();
            getMyStarData();
        }

    }


    public void getMyStarData() {

        arrMyStarDataList.clear();

        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");
        Log.d("!!!!!!", "getMyStarData  " + arrMyStarList.size());

        for(int i=0; i<arrMyStarList.size(); i++)
        {
            strTargetIdx = arrMyStarList.get(i).Idx;


            Log.d("!!!!!!", "size OK  " + arrMyStarList.size());
            final int finalI = i;

            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserData tempUserData = dataSnapshot.getValue(UserData.class);
                    arrMyStarDataList.add(tempUserData);

                    for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.StarList.entrySet()) {
                        //  if(!arrMyStarDataList.get(finalI).arrStarList.contains(entry.getValue().Nick))
                        arrMyStarDataList.get(finalI).arrStarList.add(entry.getValue());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }

    }

    public void sortStarData()
    {
        Collections.sort(arrMyStarList);
    }

    public boolean makePublicRoom()
    {
        boolean rtValue = false;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("HHMM");
        String getTime = sdf.format(date);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("PublicRoom");

        user = table.child(getUserIdx()).child(getTime);

        user.push().setValue("asd");
        rtValue = true;

        return rtValue;
    }


}
