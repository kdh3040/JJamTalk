package com.hodo.jjamtalk.Data;

import java.util.ArrayList;

/**
 * Created by User0 on 2017-09-18.
 */

public class PublicRoomData {
    public ArrayList<String> arrUserList = new ArrayList<>();
    public int nEndTime;
    public String strImg;
    public String CurRoomName;
    public boolean CurRoomStatus;

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
