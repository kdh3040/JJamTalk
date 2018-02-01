package com.hodo.jjamtalk.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.kakao.usermgmt.response.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

    public int RecvMsgReject;

    public String Date;

    public String SendMSG;
    public String Memo;

    public int PublicRoomStatus = 0;
    public int PublicRoomName = 0;
    public int PublicRoomLimit = 0;
    public int PublicRoomTime = 0;

    public int ImgCount = 1;
    public String ImgGroup0;
    public String ImgGroup1;
    public String ImgGroup2;
    public String ImgGroup3;
    public String ImgGroup4;

    public int ItemCount;
    public int Item_1;
    public int Item_2;
    public int Item_3;
    public int Item_4;
    public int Item_5;
    public int Item_6;
    public int Item_7;
    public int Item_8;

    public int BestItem;

    public  ArrayList<UserData> arrChatTargetData = new ArrayList<>();

    public int FanCount;
    public  Map<String, SimpleUserData> FanList = new LinkedHashMap<String, SimpleUserData>();
    public  ArrayList<SimpleUserData> arrFanList = new ArrayList<>();
    public  ArrayList<UserData> arrFanData = new ArrayList<>();
    public  Map<String, UserData> mapFanData = new LinkedHashMap<String, UserData>();

    //public  ArrayList<StarData>StarList = new ArrayList<>();
    public  Map<String, SimpleUserData> StarList = new LinkedHashMap<String, SimpleUserData>();
    public  ArrayList<SimpleUserData> arrStarList = new ArrayList<>();
    public  ArrayList<UserData> arrStarData = new ArrayList<>();
    public  Map<String, UserData> mapStarData = new LinkedHashMap<String, UserData>();

    public  Map<String, String> CardList = new LinkedHashMap<String, String>();
    public  ArrayList<String> arrCardList = new ArrayList<>();
    public  ArrayList<UserData> arrCardData = new ArrayList<>();

    public int Point;
    public int Grade;

    public int ConnectDate;
    public long LastBoardWriteTime;

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
        RecvMsgReject = in.readInt();
        Date = in.readString();
        SendMSG = in.readString();
        Memo = in.readString();

        PublicRoomStatus = in.readInt();
        PublicRoomName = in.readInt();
        PublicRoomLimit = in.readInt();
        PublicRoomTime = in.readInt();

        ImgCount = in.readInt();

        ImgGroup0 = in.readString();
        ImgGroup1 = in.readString();
        ImgGroup2 = in.readString();
        ImgGroup3 = in.readString();
        ImgGroup4 = in.readString();

        ItemCount = in.readInt();
        Item_1 = in.readInt();
        Item_2 = in.readInt();
        Item_3 = in.readInt();
        Item_4 = in.readInt();
        Item_5 = in.readInt();
        Item_6 = in.readInt();
        Item_7 = in.readInt();
        Item_8 = in.readInt();
        BestItem = in.readInt();

        arrChatTargetData = (ArrayList<UserData>) in.readSerializable();

        FanCount = in.readInt();
        FanList = (HashMap<String, SimpleUserData>) in.readSerializable();
        arrFanList = (ArrayList<SimpleUserData>) in.readSerializable();
        arrFanData = (ArrayList<UserData>) in.readSerializable();
        mapFanData = (HashMap<String, UserData>) in.readSerializable();

        StarList  = (HashMap<String, SimpleUserData>) in.readSerializable();
        arrStarList = (ArrayList<SimpleUserData>) in.readSerializable();
        arrStarData = (ArrayList<UserData>) in.readSerializable();
        mapStarData= (HashMap<String, UserData>) in.readSerializable();

        arrCardList = (ArrayList<String>) in.readSerializable();
        arrCardData = (ArrayList<UserData>) in.readSerializable();

        Point = in.readInt();
        Grade = in.readInt();

        ConnectDate = in.readInt();
        LastBoardWriteTime = in.readLong();

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
        parcel.writeInt(RecvMsgReject);
        parcel.writeString(Date);
        parcel.writeString(SendMSG);
        parcel.writeString(Memo);
        parcel.writeInt(PublicRoomStatus);
        parcel.writeInt(PublicRoomName);
        parcel.writeInt(PublicRoomLimit);
        parcel.writeInt(PublicRoomTime);
        parcel.writeInt(ImgCount);
        parcel.writeString(ImgGroup0);
        parcel.writeString(ImgGroup1);
        parcel.writeString(ImgGroup2);
        parcel.writeString(ImgGroup3);
        parcel.writeString(ImgGroup4);

        parcel.writeInt(ItemCount);
        parcel.writeInt(Item_1);
        parcel.writeInt(Item_2);
        parcel.writeInt(Item_3);
        parcel.writeInt(Item_4);
        parcel.writeInt(Item_5);
        parcel.writeInt(Item_6);
        parcel.writeInt(Item_7);
        parcel.writeInt(Item_8);
        parcel.writeInt(BestItem);

        parcel.writeSerializable(arrChatTargetData);

        parcel.writeInt(FanCount);

        parcel.writeSerializable((Serializable) FanList);
        parcel.writeSerializable(arrFanList);
        parcel.writeSerializable(arrFanData);
        parcel.writeSerializable((Serializable) mapFanData);

        parcel.writeSerializable((Serializable) StarList);
        parcel.writeSerializable(arrStarList);
        parcel.writeSerializable(arrStarData);
        parcel.writeSerializable((Serializable) mapStarData);

        parcel.writeSerializable(arrCardList);
        parcel.writeSerializable(arrCardData);

        parcel.writeInt(Point);
        parcel.writeInt(Grade);

        parcel.writeInt(ConnectDate);
        parcel.writeLong(LastBoardWriteTime);
    }

}
