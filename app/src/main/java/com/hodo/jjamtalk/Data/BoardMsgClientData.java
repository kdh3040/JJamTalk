package com.hodo.jjamtalk.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by boram on 2017-08-16.
 */

public class BoardMsgClientData {

    private BoardMsgDBData mDBData;

    public ArrayList<BoardReportData> ReportList = new ArrayList<>();

    public BoardMsgClientData(BoardMsgDBData data)
    {
        SetDBdata(data);

        for(LinkedHashMap.Entry<String, BoardReportData> entry : mDBData.ReportList.entrySet())
            ReportList.add(entry.getValue());
    }

    public BoardMsgDBData GetDBData()
    {
        return mDBData;
    }

    public void SetDBdata(BoardMsgDBData data)
    {
        mDBData = data;
    }

    public Boolean IsReportUser(String idx)
    {
        for(BoardReportData data : ReportList)
        {
            if(data.Idx.equals(idx))
                return true;
        }

        return false;
    }
}
