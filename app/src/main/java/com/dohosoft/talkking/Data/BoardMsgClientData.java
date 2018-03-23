package com.dohosoft.talkking.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by boram on 2017-08-16.
 */

public class BoardMsgClientData {

    private SimpleUserData mSimpleUserData;
    private BoardMsgDBData mDBData;

    public ArrayList<BoardReportData> ReportList = new ArrayList<>();

    public BoardMsgClientData(BoardMsgDBData data)
    {
        SetDBdata(data);

        for(LinkedHashMap.Entry<String, BoardReportData> entry : mDBData.ReportList.entrySet())
            ReportList.add(entry.getValue());
    }

    public void SetBoardSimpleUserData(SimpleUserData data)
    {
        mSimpleUserData = data;
    }

    public SimpleUserData GetBoardSimpleUserData()
    {
        return mSimpleUserData;
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
