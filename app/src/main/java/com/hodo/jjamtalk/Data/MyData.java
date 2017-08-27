package com.hodo.jjamtalk.Data;

import android.support.annotation.NonNull;
import android.text.Editable;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    public ArrayList<UserData> arrUserMan_Hot = new ArrayList<>();
    public ArrayList<UserData> arrUserWoman_Hot = new ArrayList<>();
    public ArrayList<UserData> arrUserAll_Hot = new ArrayList<>();

    public ArrayList<UserData> arrUserMan_Rank = new ArrayList<>();
    public ArrayList<UserData> arrUserWoman_Rank = new ArrayList<>();
    public ArrayList<UserData> arrUserAll_Rank = new ArrayList<>();


    private String strIdx;
    private String strToken;
    private String strImg;
    private String strNick;
    private String strGender;
    private String strAge;

    private double lLat;
    private double lLon;

    public int nHeart;
    public int nHoney;
    public int nRank;

    public String strDate;

    private String strMemo;
    private String strSchool;
    private String strCompany;
    private String strTitle;

    public int nSearchMode;
    public int nAlarmMode;
    public int nViewMode;

    public ArrayList<String> arrImgList = new ArrayList<>();

    public ArrayList<String> arrBlockNameList = new ArrayList<>();
    public ArrayList<BlockData> arrBlockDataList = new ArrayList<>();

    public ArrayList<BlockData> arrBlockedDataList = new ArrayList<>();


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
        nHeart = 0;
        nHoney = 0;
        nRank = 0;
        strDate = null;

        strMemo = null;
        strSchool = null;
        strCompany = null;
        strTitle = null;
    }

    public void setMyData(String _UserIdx, String _UserImg, String _UserImgGroup0, String _UserImgGroup1, String _UserImgGroup2, String _UserImgGroup3, String _UserImgGroup4,
                          String _UserNick, String _UserGender, String _UserAge, Double _UserLon, Double _UserLat, int _UserHeart, int _UserHoney, int _UserRank, String _UserDate,
                          String _UserMemo, String _UserSchool, String _UserCompany, String _UserTitle)
    {
        strIdx = _UserIdx;
        strToken = FirebaseInstanceId.getInstance().getToken();

        //strToken = com.google.firebase.iid.FirebaseInstanceId.getInstance().getToken();


        strImg = _UserImg;
        strNick = _UserNick;
        strGender = _UserGender;
        strAge = _UserAge;
        lLon = _UserLon;
        lLat = _UserLat;
        nHeart = _UserHeart;
        nHoney = _UserHoney;
        nRank = _UserRank;
        strDate = _UserDate;

        strMemo = _UserMemo;
        strSchool = _UserSchool;
        strCompany = _UserCompany;
        strTitle = _UserTitle;

        arrImgList.add(_UserImgGroup0);
        arrImgList.add(_UserImgGroup1);
        arrImgList.add(_UserImgGroup2);
        arrImgList.add(_UserImgGroup3);
        arrImgList.add(_UserImgGroup4);
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

    public void setUserImg(String userImg) {
        strImg = userImg;
    }
    public String getUserImg() {
        return strImg;
    }

    public void setUserImgList(String userImg) {
        arrImgList.add(userImg);
    }
    public String getUserImgList(int i) {
        return arrImgList.get(i);
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

    public void setUserHeart(int userHeart) {
        nHeart = userHeart;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        if(strGender.equals("여자"))
            table = database.getReference("Users/여자/"+ strIdx);
        else
            table = database.getReference("Users/남자/"+ strIdx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("Heart", nHeart);
        table.updateChildren(updateMap);

    }
    public int getUserHeart() {
        return nHeart;
    }

    public void setUserHoney(int userHoney) {
        nHoney = userHoney;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        if(strGender.equals("여자"))
            table = database.getReference("Users/여자/"+ strIdx);
        else
            table = database.getReference("Users/남자/"+ strIdx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("Honey", nHoney);
        table.updateChildren(updateMap);

    }
    public int getUserHoney() {
        return nHoney;
    }

    public void setUserRank(int userRank) {
        nRank = userRank;
    }
    public int getUserRank() {
        return nRank;
    }

    public void setUserMemo(String userMemo) {
        strMemo = userMemo;
    }
    public String getUserMemo() {
        return strMemo;
    }

    public void setUserSchool(String userSchool) {
        strSchool = userSchool;
    }
    public String getUserSchool() {
        return strSchool;
    }

    public void setUserCompany(String userCompany) {
        strCompany = userCompany;
    }
    public String getUserCompany() {
        return strCompany;
    }

    public void setUserTitle(String userTitle) {
        strTitle = userTitle;
    }
    public String getUserTitle() {
        return strTitle;
    }

    public boolean makeSendList(UserData _UserData, Editable _strSend)
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

    public void setProfileData(Editable memo, Editable school, Editable company, Editable title) {
        strMemo = memo.toString();
        strSchool = school.toString();
        strCompany = company.toString();
        strTitle = title.toString();
    }

    public void setSettingData(int SearchMode, int AlarmMode, int ViewMode) {
        nSearchMode = SearchMode;
        nAlarmMode= AlarmMode;
        nViewMode = ViewMode;
    }

    public void getRecvHoneyList() {
        String MyID =  strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user;
        table = database.getReference("RecvHoneyList");
        user = table.child(strIdx);

        user.addChildEventListener(new ChildEventListener() {
            int i = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int saa =0;
                SendData SendList= dataSnapshot.getValue(SendData.class);
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
    public boolean makeSendHoneyList(UserData _UserData, int SendHoneyCnt) {
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

        user.push().setValue(tempTargetSave);
        rtValue = true;


        return rtValue;
    }

    public boolean makeRecvHoneyList(UserData _UserData, int SendHoneyCnt) {
        boolean rtValue = false;

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
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });

    }
}
