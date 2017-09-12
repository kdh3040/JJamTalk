package com.hodo.jjamtalk.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by boram on 2017-08-05.
 */

public class UserData implements Serializable,Parcelable {

    private static final long  serialVersionUID = 2L;

    public String Idx;
    public String Token;

    public String Img;
    public String NickName;
    public String Gender;
    public String Age;

    public double Lat;
    public double Lon;

    public int Honey;
    public int Heart;
    public int Rank;
    public int SendCount;
    public int RecvCount;

    public int RecvMsg;

    public String Date;

    public String SendMSG;
    public String Memo;
    //public String School;
    //public String Company;
    //public String Title;

    public int ImgCount = 1;
    public String ImgGroup0;
    public String ImgGroup1;
    public String ImgGroup2;
    public String ImgGroup3;
    public String ImgGroup4;

    public  Map<String, FanData> FanList = new LinkedHashMap<String, FanData>();
    public  ArrayList<FanData> arrFanList = new ArrayList<>();

    public  Map<String, FanData> StarList = new LinkedHashMap<String, FanData>();
    public  ArrayList<FanData> arrStarList = new ArrayList<>();


    public UserData() {

    }

    public UserData(Parcel in) {
        Idx = in.readString();
        Token = in.readString();
        Img = in.readString();
        NickName = in.readString();
        Gender = in.readString();
        Age = in.readString();
        Lat = in.readDouble();
        Lon = in.readDouble();
        Honey = in.readInt();
        Heart = in.readInt();
        Rank = in.readInt();
        SendCount = in.readInt();
        RecvCount = in.readInt();
        Date = in.readString();
        SendMSG = in.readString();
        Memo = in.readString();
        ImgGroup0 = in.readString();
        ImgGroup1 = in.readString();
        ImgGroup2 = in.readString();
        ImgGroup3 = in.readString();
        ImgGroup4 = in.readString();
        ImgCount = in.readInt();
        RecvMsg = in.readInt();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
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
        parcel.writeInt(Honey);
        parcel.writeInt(Heart);
        parcel.writeInt(Rank);
        parcel.writeInt(SendCount);
        parcel.writeInt(RecvCount);
        parcel.writeString(Date);
        parcel.writeString(SendMSG);
        parcel.writeString(Memo);
        parcel.writeString(ImgGroup0);
        parcel.writeString(ImgGroup1);
        parcel.writeString(ImgGroup2);
        parcel.writeString(ImgGroup3);
        parcel.writeString(ImgGroup4);
        parcel.writeInt(ImgCount);
        parcel.writeInt(RecvMsg);

    }

}
