package com.hodo.jjamtalk.Data;

import java.util.ArrayList;

/**
 * Created by User0 on 2017-09-18.
 */

public class PublicRoomChatData {
    public String strFrom;
    public String strTo;
    public String strMsg;
    public Long lTime;
    public String strImg;
    public String strId;

    public PublicRoomChatData(){

    }

    public PublicRoomChatData(String from_person, String to_person, String message, long nowTime, String image_URL){
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

    public void setMsg(String Msg) {
        strMsg = Msg;
    }
    public String getMsg() {
        return strMsg;
    }

    public void setFrom(String From) {
        strFrom = From;
    }

    public String getFrom() {
        return strFrom;
    }
}
