package com.hodo.jjamtalk.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by boram on 2017-08-05.
 */

public class SimpleUserData implements Serializable,Parcelable {

    private static final long  serialVersionUID = 2L;

    public String Idx;
    public String Token;

    public String Img;
    public String NickName;
    public String Gender;
    public String Age;

    // 가까운순
    public double Lat;
    public double Lon;

    // 뉴페이스
    public String Date;

    //팬보유순
    public int FanCount;

    //추천순
    public int Point;

    public SimpleUserData() {

    }

    public SimpleUserData(Parcel in) {
        Idx = in.readString();
        Token = in.readString();
        Img = in.readString();
        NickName = in.readString();
        Gender = in.readString();
        Age = in.readString();
        Lat = in.readDouble();
        Lon = in.readDouble();
        Date = in.readString();
        FanCount = in.readInt();
        Point = in.readInt();
    }

    public static final Creator<SimpleUserData> CREATOR = new Creator<SimpleUserData>() {
        @Override
        public SimpleUserData createFromParcel(Parcel in) {
            return new SimpleUserData(in);
        }

        @Override
        public SimpleUserData[] newArray(int size) {
            return new SimpleUserData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Idx);
        parcel.writeString(Token);
        parcel.writeString(Img);
        parcel.writeString(NickName);
        parcel.writeString(Gender);
        parcel.writeString(Age);
        parcel.writeDouble(Lat);
        parcel.writeDouble(Lon);
        parcel.writeString(Date);

        parcel.writeInt(FanCount);
        parcel.writeInt(Point);
    }

}
