package com.hodo.jjamtalk.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by boram on 2017-08-16.
 */

public class TempBoardData {

    public String Idx;
    public String Img;
    public String NickName;
    public String Age;
    public String Job;
    public String Key;
    public String Date;
    public String Msg;

    public int ReplyCnt;
    public int LikeCnt;
    public int PageCnt;

    //public ArrayList<TempBoard_ReplyData> Reply = new ArrayList<>();
    public Map<String, TempBoard_ReplyData> Reply = new LinkedHashMap<String, TempBoard_ReplyData>();
    public ArrayList<TempBoard_ReplyData> arrReplyList = new ArrayList<>();

    public void setvalue(String Key, TempBoard_ReplyData map)
    {
        this.Reply.put(Key, map);
    }
    public Map<String, TempBoard_ReplyData> getvalue()
    {
        return this.Reply;
    }
}