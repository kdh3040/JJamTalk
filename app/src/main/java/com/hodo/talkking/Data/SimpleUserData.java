package com.hodo.talkking.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

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

    // 카드창
    public String Memo;

    // 팬창
    public int RecvGold;
    public int SendGold;

    // 가까운순
    public double Lat;
    public double Lon;
    public double Dist;
    // 뉴페이스
    public String Date;
    //팬보유순
    public long FanCount;
    //추천순
    public int Point;

    public int BestItem;
    public int Grade;

    public SimpleUserData() {
        Idx = null;
        Token = null;

        Img = null;
        NickName = null;
        Gender = null;
        Age = null;

        Memo = null;

        RecvGold = 0;
        SendGold = 0;

        // 가까운순
        Lat = 0.0d;
        Lon = 0.0d;
        Dist = 0.0d;
        // 뉴페이스
        Date = null;

        //팬보유순
        FanCount = 0;

        //추천순
        Point = 0;

        BestItem = 0;
        Grade = 0;
    }

    public SimpleUserData(Parcel in) {
        Idx = in.readString();
        Token = in.readString();
        Img = in.readString();
        NickName = in.readString();
        Gender = in.readString();
        Age = in.readString();
        Memo = in.readString();
        RecvGold = in.readInt();
        SendGold = in.readInt();
        Lat = in.readDouble();
        Lon = in.readDouble();
        Dist = in.readDouble();
        Date = in.readString();
        FanCount = in.readInt();
        Point = in.readInt();
        BestItem = in.readInt();
        Grade = in.readInt();
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
        parcel.writeString(Memo);

        parcel.writeInt(RecvGold);
        parcel.writeInt(SendGold);

        parcel.writeDouble(Lat);
        parcel.writeDouble(Lon);
        parcel.writeDouble(Dist);
        parcel.writeString(Date);

        parcel.writeLong(FanCount);
        parcel.writeInt(Point);

        parcel.writeInt(BestItem);
        parcel.writeInt(Grade);
    }

}
