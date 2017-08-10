package com.hodo.jjamtalk.Data;

import android.widget.ArrayAdapter;

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

    public ArrayList<UserData> mUserAllData = new ArrayList<>();
    public ArrayList<UserData> mUserManData = new ArrayList<>();
    public ArrayList<UserData> mUserWomanData = new ArrayList<>();

    private ArrayList<UserData> arrUserMan_Near = new ArrayList<UserData>();
    private ArrayList<UserData> arrUserWoman_Near = new ArrayList<UserData>();
    private ArrayList<UserData> arrUserAll_Near = new ArrayList<UserData>();


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

    public int nDate;

    private MyData()
    {
    }

    public void setMyData(String _UserIdx, String _UserImg, String _UserNick, String _UserGender, String _UserAge, Double _UserLon, Double _UserLat, int _UserHeart, int _UserHot, int _UserRank, int _UserDate)
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
        nDate = _UserDate;
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

    public void setUserDate(int userDate) {
        nDate = userDate;
    }
    public int getUserDate() {
        return nDate;
    }
}
