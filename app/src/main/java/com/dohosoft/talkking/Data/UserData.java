package com.dohosoft.talkking.Data;

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

public class UserData implements Serializable,Parcelable {

    private static final long  serialVersionUID = 2L;

    public String Uid;
    public String Idx;
    public String Token;

    public String Img;
    public String NickName;
    public String Gender;
    public String Age;

    public double Lat;
    public double Lon;
    public double Dist;

    public int Honey;
    public int Heart;
    public int Rank;
    public int SendCount;
    public long RecvGold;

    public int RecvMsgReject;

    public long Date;

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

    public long FanCount;

    public  ArrayList<FanData> arrFanList = new ArrayList<>();
    public  Map<String, FanData> FanList = new LinkedHashMap<String, FanData>();
    public  Map<String, SimpleUserData> arrFanData = new LinkedHashMap<String, SimpleUserData>();
    public  Map<String, UserData> mapFanData = new LinkedHashMap<String, UserData>();

    //public  ArrayList<StarData>StarList = new_img ArrayList<>();
    public  Map<String, SimpleUserData> StarList = new LinkedHashMap<String, SimpleUserData>();
    public  ArrayList<SimpleUserData> arrStarList = new ArrayList<>();
    public  ArrayList<UserData> arrStarData = new ArrayList<>();
    public  Map<String, UserData> mapStarData = new LinkedHashMap<String, UserData>();

    public  Map<String, String> CardList = new LinkedHashMap<String, String>();
    public  ArrayList<String> arrCardList = new ArrayList<>();
    public  ArrayList<UserData> arrCardData = new ArrayList<>();

    public  Map<String, SimpleChatData> SendList = new LinkedHashMap<String, SimpleChatData>();
    public ArrayList<SimpleChatData> tempChatData = new ArrayList<>();

    public int Point;
    public int Grade;

    public long ConnectDate;
    public long LastBoardWriteTime;
    public long LastAdsTime;

    public int ReportedCnt;
    public int NickChangeCnt;

    public int SubStatus;
    public long SubDate;

    public int Rejoin;
    public UserData() {

    }

    public UserData(Parcel in) {


        Uid = in.readString();
        Idx = in.readString();
        Token = in.readString();
        Img = in.readString();
        NickName = in.readString();
        Gender = in.readString();
        Age = in.readString();
        Lat = in.readDouble();
        Lon = in.readDouble();
        Dist = in.readDouble();
        Honey = in.readInt();
        Heart = in.readInt();
        Rank = in.readInt();
        SendCount = in.readInt();
        RecvGold = in.readLong();
        RecvMsgReject = in.readInt();
        Date = in.readLong();
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

        FanCount = in.readLong();

        arrFanList = (ArrayList<FanData>) in.readSerializable();
        arrFanData = (HashMap<String, SimpleUserData>) in.readSerializable();
        FanList = (HashMap<String, FanData>) in.readSerializable();
        mapFanData = (HashMap<String, UserData>) in.readSerializable();

        StarList  = (HashMap<String, SimpleUserData>) in.readSerializable();
        arrStarList = (ArrayList<SimpleUserData>) in.readSerializable();
        arrStarData = (ArrayList<UserData>) in.readSerializable();
        mapStarData= (HashMap<String, UserData>) in.readSerializable();

        arrCardList = (ArrayList<String>) in.readSerializable();
        arrCardData = (ArrayList<UserData>) in.readSerializable();

        SendList = (HashMap<String, SimpleChatData>) in.readSerializable();
        tempChatData = (ArrayList<SimpleChatData>) in.readSerializable();


        Point = in.readInt();
        Grade = in.readInt();

        ConnectDate = in.readLong();
        LastBoardWriteTime = in.readLong();
        LastAdsTime = in.readLong();

        ReportedCnt = in.readInt();
        NickChangeCnt = in.readInt();

        SubStatus = in.readInt();
        SubDate = in.readLong();

        Rejoin = in.readInt();
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
        parcel.writeString(Uid);
        parcel.writeString(Idx);
        parcel.writeString(Token);
        parcel.writeString(Img);
        parcel.writeString(NickName);
        parcel.writeString(Gender);
        parcel.writeString(Age);
        parcel.writeDouble(Lat);
        parcel.writeDouble(Lon);
        parcel.writeDouble(Dist);
        parcel.writeInt(Honey);
        parcel.writeInt(Heart);
        parcel.writeInt(Rank);
        parcel.writeInt(SendCount);
        parcel.writeLong(RecvGold);
        parcel.writeInt(RecvMsgReject);
        parcel.writeLong(Date);
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

        parcel.writeLong(FanCount);


        parcel.writeSerializable(arrFanList);
        parcel.writeSerializable((Serializable) arrFanData);
        parcel.writeSerializable((Serializable) FanList);
        parcel.writeSerializable((Serializable) mapFanData);

        parcel.writeSerializable((Serializable) StarList);
        parcel.writeSerializable(arrStarList);
        parcel.writeSerializable(arrStarData);
        parcel.writeSerializable((Serializable) mapStarData);

        parcel.writeSerializable(arrCardList);
        parcel.writeSerializable(arrCardData);

        parcel.writeSerializable((Serializable) SendList );
        parcel.writeSerializable(tempChatData);


        parcel.writeInt(Point);
        parcel.writeInt(Grade);

        parcel.writeLong(ConnectDate);
        parcel.writeLong(LastBoardWriteTime);
        parcel.writeLong(LastAdsTime);

        parcel.writeInt(ReportedCnt);
        parcel.writeInt(NickChangeCnt);

        parcel.writeInt(SubStatus);
        parcel.writeLong(SubDate);

        parcel.writeInt(Rejoin);
    }

/*    public long getConnectDate() {
        return ConnectDate;
    }*/


}
