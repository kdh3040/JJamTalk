package com.hodo.jjamtalk.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by boram on 2017-08-27.
 */

public class FanData implements Serializable,Parcelable {
    private static final long  serialVersionUID = 1L;

    public String Idx;
    public int RecvGold;

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
        RecvGold = in.readInt();
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
        parcel.writeInt(RecvGold);
    }
}
