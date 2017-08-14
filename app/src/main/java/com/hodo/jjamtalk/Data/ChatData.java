package com.hodo.jjamtalk.Data;

/**
 * Created by boram on 2017-08-14.
 */

public class ChatData {
    public String strFrom;
    public String strTo;
    public String strMsg;
    public Long lTime;
    public String strImg;
    public String strId;

    public ChatData(){

    }


    public ChatData(String from_person, String to_person, String message, long nowTime, String image_URL){
        strFrom= from_person;
        strTo = to_person;
        strMsg = message;
        lTime= nowTime;
        strImg = image_URL;

    }
    public Long gettime(){
        return lTime;
    }

    public Object getImg() {
        return strImg;
    }

    public void setId(String id) {
        this.strId = id;
    }

    public String getMsg() {
        return strMsg;
    }

    public String getFrom() {
        return strFrom;
    }

}
