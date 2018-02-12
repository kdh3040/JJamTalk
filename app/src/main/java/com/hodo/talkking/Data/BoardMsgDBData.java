package com.hodo.talkking.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HwanWoong on 2018-01-06.
 */

public class BoardMsgDBData {
    // TODO 환웅 유저의 최소단위 클래스가 하나 필요할듯
    public String Idx;
    public String Img;
    public String NickName;
    public String Date;
    public String Msg;
    public long BoardIdx;
    public int BestItem;
    public int Grade;
    public String Gender;


    public Map<String, BoardReportData> ReportList = new LinkedHashMap<String, BoardReportData>();
}
