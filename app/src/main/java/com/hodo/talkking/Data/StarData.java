package com.hodo.talkking.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by boram on 2017-08-27.
 */

public class StarData implements Serializable,Parcelable {
    private static final long  serialVersionUID = 1L;

    public String Idx;
    public int SendGold;

    public static final Creator<StarData> CREATOR = new Creator<StarData>() {
        @Override
        public StarData createFromParcel(Parcel in) {
            return new StarData(in);
        }

        @Override
        public StarData[] newArray(int size) {
            return new StarData[size];
        }
    };

    public StarData(Parcel in) {
        Idx = in.readString();
        SendGold = in.readInt();
    }

    public StarData() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Idx);
        parcel.writeInt(SendGold);
    }
}
