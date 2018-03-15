package com.hodo.talkking.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HwanWoong on 2018-01-06.
 */

public class BoardMsgDBData {
    public String Idx;
    public long Date;
    public String Msg;
    public long BoardIdx;

    public Map<String, BoardReportData> ReportList = new LinkedHashMap<String, BoardReportData>();
}
