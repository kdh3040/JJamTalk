package com.hodo.jjamtalk.Data;

import java.util.ArrayList;

/**
 * Created by boram on 2017-08-16.
 */

public class TempBoardData {

    public String Idx;
    public String Img;
    public String NickName;
    public String Age;
    public String Job;
    public String Info;
    public String Date;
    public String Msg;

    public int ReplyCnt;
    public int LikeCnt;

    public ArrayList<TempBoard_ReplyData> arrReplyList = new ArrayList<>();
}
