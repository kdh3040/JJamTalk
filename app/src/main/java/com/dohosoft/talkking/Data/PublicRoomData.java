package com.dohosoft.talkking.Data;

/**
 * Created by User0 on 2017-09-18.
 */

public class PublicRoomData {
    //public ArrayList<String> arrUserList = new_img ArrayList<>();
    public int nRoomLimit;
    public int nEndTime;
    public String strImg;
    public String CurRoomName;
    public int CurRoomStatus;

    public PublicRoomData(){

    }

    public void settime(int time){
        nEndTime = time;
    }
    public int gettime(){
        return nEndTime;
    }

    public void setImg(String img) {
        strImg = img;
    }
    public String getImg() {
        return strImg;
    }

}