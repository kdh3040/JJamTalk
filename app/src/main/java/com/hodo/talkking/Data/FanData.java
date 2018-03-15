package com.hodo.talkking.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by boram on 2017-08-27.
 */

public class FanData implements Serializable,Parcelable {
    private static final long  serialVersionUID = 1L;

    public String Idx;
/*    public String NickName;
    public int BestItem;
    public int Grade;
    public String Img;*/
    public int RecvGold;
    public int Check;

    public static final Creator<FanData> CREATOR = new Creator<FanData>() {
        @Override
        public FanData createFromParcel(Parcel in) {
            return new FanData(in);
        }

        @Override
        public FanData[] newArray(int size) {
            return new FanData[size];
        }
    };

    public FanData(Parcel in) {
        Idx = in.readString();
   /*     NickName = in.readString();
        BestItem = in.readInt();
        Grade = in.readInt();
        Img = in.readString();*/
        RecvGold = in.readInt();
        Check = in.readInt();
    }

    public FanData() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Idx);
 /*       parcel.writeString(NickName);
        parcel.writeInt(BestItem);
        parcel.writeInt(Grade);
        parcel.writeString(Img);*/
        parcel.writeInt(RecvGold);
        parcel.writeInt(Check);
    }
}
