package com.hodo.talkking.Data;

/**
 * Created by boram on 2017-08-14.
 */

public class ChatData {
    private MyData mMyData = MyData.getInstance();

    public String from;
    public String to;
    public String msg;
    public String time;
    public String img;
    public String strId;
    public int Check;

    public ChatData(){

    }


    public ChatData(String from_person, String to_person, String message, String nowTime, String image_URL, int check){
        from= from_person;
        to = to_person;
        msg = message;
        time= nowTime;
        img = image_URL;
        Check = check;
    }
    public String gettime(){
        return time;
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
