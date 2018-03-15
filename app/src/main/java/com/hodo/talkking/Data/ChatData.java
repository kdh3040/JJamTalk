package com.hodo.talkking.Data;

/**
 * Created by boram on 2017-08-14.
 */

public class ChatData {
    private MyData mMyData = MyData.getInstance();

    public String from;
    public String fromIdx;
    public String to;
    public String msg;
    public long time;
    public String img;
    public String strId;
    public int Check;
    public int Heart;

    public ChatData(){

    }


    public ChatData(String from_Idx, String from_person, String to_person, String message, long nowTime, String image_URL, int check, int heart){
        fromIdx = from_Idx;
        from= from_person;
        to = to_person;
        msg = message;
        time= nowTime;
        img = image_URL;
        Check = check;
        Heart = heart;
    }

    public Object getImg() {
        return img;
    }

    public void setId(String id) {
        this.strId = id;
    }

    public String getMsg() {
        return msg;
    }

    public String getFrom() {
        return from;
    }

}
