package com.hodo.talkking.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by boram on 2017-08-27.
 */

public class ReportedData implements Serializable,Parcelable {
    private static final long  serialVersionUID = 1L;

    public int ReportType;

    public static final Creator<ReportedData> CREATOR = new Creator<ReportedData>() {
        @Override
        public ReportedData createFromParcel(Parcel in) {
            return new ReportedData(in);
        }

        @Override
        public ReportedData[] newArray(int size) {
            return new ReportedData[size];
        }
    };

    public ReportedData(Parcel in) {
        ReportType = in.readInt();
    }

    public ReportedData() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ReportType);
    }
}
