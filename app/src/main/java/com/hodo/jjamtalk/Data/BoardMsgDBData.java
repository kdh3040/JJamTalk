package com.hodo.jjamtalk.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HwanWoong on 2018-01-06.
 */

public class BoardMsgDBData {
    public String Idx;
    public String Img;
    public String NickName;
    public String Age;
    public String Job;
    public String Key;
    public String Date;
    public String Msg;
    public String ViewCount;

    public Map<String, TempBoard_ReplyData> Reply = new LinkedHashMap<String, TempBoard_ReplyData>();
    public Map<String, BoardLikeData> Like = new LinkedHashMap<String, BoardLikeData>();
}
