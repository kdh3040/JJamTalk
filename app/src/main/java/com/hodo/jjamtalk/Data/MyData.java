package com.hodo.jjamtalk.Data;

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

    private String strIdx;
    private String strImg;
    private String strNick;
    private String strGender;
    private String strAge;

    private double lLat;
    private double lLon;

    private int nHot;
    private int nRank;

    private MyData()
    {

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
    public Double getUserLat() {
        return lLat;
    }


    public void setUserHor(int userHot) { nHot = userHot;}
    public int getUserHor() {
        return nHot;
    }

    public void setUserRank(int userRank) {
        nRank = userRank;
    }
    public int getUserRank() {
        return nRank;
    }

}
