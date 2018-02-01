package com.hodo.jjamtalk.Data;

import android.app.usage.UsageEvents;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hodo.jjamtalk.CardListFragment;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.CommonFunc;
import com.kakao.usermgmt.response.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.hodo.jjamtalk.MainActivity.mFragmentMng;

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

    public  Map<String, UserData> mapChatTargetData = new LinkedHashMap<String, UserData>();

    public int nFanCount;
    public ArrayList<FanData> arrMyFanList = new ArrayList<>();
    public  Map<String, SimpleUserData> arrMyFanDataList = new LinkedHashMap<String, SimpleUserData>();
    public  Map<String, UserData> mapMyFanData = new LinkedHashMap<String, UserData>();

    public ArrayList<StarData> arrMyStarList = new ArrayList<>();
    public  Map<String, SimpleUserData> arrMyStarDataList = new LinkedHashMap<String, SimpleUserData>();
    public  Map<String, UserData> mapMyStarData = new LinkedHashMap<String, UserData>();

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
    public boolean nAlarmSetting_Sound = false;
    public boolean nAlarmSetting_Vibration = false;
    public int nViewMode = 1;
    public int nRecvMsg = 0;

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

    public  String strDownUri;

    public  boolean bChatRefresh = false;

    private int nCurVisibleFrag;
    public  int nStartAge, nEndAge;
    public  int nMyAge;

    public int Point;
    public int Grade;

    public int ConnectDate;
    public long LastBoardWriteTime;

    private MyData() {
        strImg = null;
        strNick = null;
        strGender = null;
        strAge = null;
        lLon = 0f;
        lLat = 0f;
        nHoney = 0;
        strDate = null;


        for (int i = 0; i < 4; i++) {
            strProfileImg[i] = "http://imagescdn.gettyimagesbank.com/500/14/730/414/0/512600801.jpg";
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

    }

    public void setMyData(String _UserIdx, int _UserImgCount, String _UserImg, String _UserImgGroup0, String _UserImgGroup1, String _UserImgGroup2, String _UserImgGroup3,
                          String _UserNick, String _UserGender, String _UserAge, Double _UserLon, Double _UserLat,
                          int _UserHoney, int _UserSendCount, int _UserRecvCount, String _UserDate,
                          String _UserMemo, int _UserRecvMsg, int _UserPublicRoomStatus , int _UserPublicRoomName, int _UserPublicRoomLimit, int _UserPublicRoomTime,
                          int _UserItemCount, int _UserItem1, int _UserItem2, int _UserItem3, int _UserItem4, int _UserItem5, int _UserItem6, int _UserItem7, int _UserItem8, int _UserBestItem,
                          int _UserPoint, int _UserGrade, int _UserConnDate, long _UserLastBoardWriteTime) {
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
        else if(getPoint() <= 300)
            setGrade(1);
        else if(getPoint() <= 500)
            setGrade(2);
        else if(getPoint() <= 1000)
            setGrade(3);
        else if(getPoint() <= 2000)
            setGrade(4);
        else if(getPoint() <= 5000)
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

    public boolean makeSendList(UserData _UserData, String _strSend) {
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

        SimpleChatData tempTargetSave = new SimpleChatData();
        tempTargetSave.ChatRoomName = strCheckName;
        tempTargetSave.Idx = _UserData.Idx;
        tempTargetSave.Nick = _UserData.NickName;
        tempTargetSave.Img = _UserData.Img;
        tempTargetSave.Msg = _strSend.toString();
        tempTargetSave.Grade = _UserData.Grade;
        tempTargetSave.BestItem = _UserData.BestItem;

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
                    arrChatNameList.add(SendList.ChatRoomName);
                    arrChatDataList.put(SendList.ChatRoomName, SendList);
                }
                //arrCardList.add(CardList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int saa = 0;
                SimpleChatData SendList = dataSnapshot.getValue(SimpleChatData.class);
                    arrChatDataList.put(SendList.ChatRoomName, SendList);

              if(GetCurFrag() == 2)
              {
                  Fragment frg = null;
                  frg = mFragmentMng.findFragmentByTag("ChatListFragment");
                  final FragmentTransaction ft = mFragmentMng.beginTransaction();
                  ft.detach(frg);
                  ft.attach(frg);
                  ft.commit();
              }

              else if(GetCurFrag() == 5) {
                  SendList.Check = 1;
                  arrChatDataList.put(SendList.ChatRoomName, SendList);
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

    public boolean makeCardList(final UserData target) {
        boolean rtValue = false;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("User/" + getUserIdx());


        for (int i = 0; i < arrCardNameList.size(); i++) {
            if (arrCardNameList.get(i).equals(target.Idx))
                return rtValue;
        }

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


    public void setProfileData(Editable memo) {
        strMemo = memo.toString();
    }

    public void setSettingData(int SearchMode, int ViewMode, int RecvMsg, boolean alarmSetting_Sound, boolean alarmSetting_Vibration) {
        nSearchMode = SearchMode;
        nAlarmSetting_Sound = alarmSetting_Sound;
        nAlarmSetting_Vibration = alarmSetting_Vibration;
        nViewMode = ViewMode;
        nRecvMsg = RecvMsg;
    }

    public void getGiftData(String Idx) {

       // arrMyStarDataList.clear();

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
                SendData SendList = dataSnapshot.getValue(SendData.class);
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
                            nSearchMode = stRecvData.SearchMode;
                            nViewMode = stRecvData.ViewMode;
                            nRecvMsg = stRecvData.RecvMsg;
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


    public void getFanList() {
        String MyID = strIdx;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                arrMyFanList.add(tempFanData);
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



    public void makeFanList(UserData stTargetData, int SendCount) {

        int nTotalSendCnt = 0;
        for (int i = 0; i < arrSendHoneyDataList.size(); i++) {
            if (arrSendHoneyDataList.get(i).strTargetNick.equals(stTargetData.NickName))
                nTotalSendCnt -= arrSendHoneyDataList.get(i).nSendHoney;
        }

        nTotalSendCnt -= SendCount;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table;
        table = database.getReference("User/" + stTargetData.Idx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("RecvGold", nTotalSendCnt);
        updateMap.put("Idx", getUserIdx());
        updateMap.put("Img", getUserImg());
        table.child("FanList").child(getUserIdx()).updateChildren(updateMap);

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
        return nFanCount;
    }

    public int SetBestItem() {
        int rtValue = 0;
        if(itemList.size() == 0)
            return  rtValue;

        TreeMap<Integer,Integer> tm = new TreeMap<Integer,Integer>(itemList);
        Iterator<Integer> iteratorKey = tm.descendingKeySet().iterator(); //키값 내림차순 정렬
        rtValue = iteratorKey.next();
        //System.out.println(key+","+tm.get(key));
        return rtValue + 1;
    }

    public void makeLastMSG(UserData  tempData, String Roomname, String strMsg, String lTime) {
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

    public int GetBestItem()
    {
        return bestItem;
    }

    public long GetLastBoardWriteTime(){return LastBoardWriteTime;}
    public void SetLastBoardWriteTime(long time){LastBoardWriteTime = time;}
}

