package com.hodo.jjamtalk.Data;

import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by boram on 2017-08-04.
 */

public class MyData {

    private static MyData _Instance;

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
    private String strImg;
    private String strNick;
    private String strGender;
    private String strAge;

    private double lLat;
    private double lLon;

    public int nHeart;
    public int nHot;
    public int nRank;

    public String strDate;

    public ArrayList<String> arrSendNameList = new ArrayList<>();
    public ArrayList<UserData> arrSendList = new ArrayList<>();

    public ArrayList<String> arrCardNameList = new ArrayList<>();
    public ArrayList<UserData> arrCardList = new ArrayList<>();

    private MyData()
    {
    }

    public void setMyData(String _UserIdx, String _UserImg, String _UserNick, String _UserGender, String _UserAge, Double _UserLon, Double _UserLat, int _UserHeart, int _UserHot, int _UserRank, String _UserDate)
    {
        strIdx = _UserIdx;
        strImg = _UserImg;
        strNick = _UserNick;
        strGender = _UserGender;
        strAge = _UserAge;
        lLon = _UserLon;
        lLat = _UserLat;
        nHeart = _UserHeart;
        nHot = _UserHot;
        nRank = _UserRank;
        strDate = _UserDate;
    }

    public void setUserIdx(String userIdx) {
        strIdx = userIdx;
    }
    public String getUserIdx() {
        return strIdx;
    }

    public void setUserImg(String userImg) {
        strImg = userImg;
    }
    public String getUserImg() {
        return strImg;
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

    public void setUserHeart(int userHeart) { nHeart = userHeart;}
    public int getUserHeart() {
        return nHeart;
    }

    public void setUserHot(int userHot) { nHot = userHot;}
    public int getUserHot() {
        return nHot;
    }

    public void setUserRank(int userRank) {
        nRank = userRank;
    }
    public int getUserRank() {
        return nRank;
    }

    public void setUserDate(String userDate) {
        strDate = userDate;
    }
    public String getUserDate() {
        return strDate;
    }

    public boolean makeSendList(UserData _UserData)
    {
        boolean rtValue = false;

        UserData SaveUserData = _UserData;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table, user, targetuser;
        table = database.getReference("SendList");

        user = table.child(strIdx).child(_UserData.Idx);
        targetuser = table.child(_UserData.Idx).child(strIdx);

        String strCheckName = strIdx + "_" + _UserData.Idx;

        if(!arrSendNameList.contains(strCheckName)) {
            user.setValue(strCheckName);
            targetuser.setValue(strCheckName);
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
                String SendList= dataSnapshot.getValue(String.class);
                if(!arrSendNameList.contains(SendList))
                    arrSendNameList.add(SendList);
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

    public void getSendData() {
    }
}
