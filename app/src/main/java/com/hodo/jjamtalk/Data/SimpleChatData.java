package com.hodo.jjamtalk.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by boram on 2017-08-14.
 */

public class SimpleChatData implements Serializable,Parcelable {

    private static final long  serialVersionUID = 1L;

    public String ChatRoomName;
    public String Msg;
    public String Nick;
    public String Idx;
    public String Img;
    public String Date;


    public SimpleChatData() {
        ChatRoomName = null;
        Msg = null;
        Nick = null;
        Idx = null;
        Img = null;
        Date = null;
    }

    public SimpleChatData(Parcel in) {
        ChatRoomName = in.readString();
        Msg = in.readString();
        Nick = in.readString();
        Idx = in.readString();
        Img = in.readString();
        Date = in.readString();
    }

    public static final Parcelable.Creator<SimpleChatData> CREATOR = new Parcelable.Creator<SimpleChatData>() {
        @Override
        public SimpleChatData createFromParcel(Parcel in) {
            return new SimpleChatData(in);
        }

        @Override
        public SimpleChatData[] newArray(int size) {
            return new SimpleChatData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ChatRoomName);
        parcel.writeString(Msg);
        parcel.writeString(Nick);
        parcel.writeString(Idx);
        parcel.writeString(Img);
        parcel.writeString(Date);
    }
}
