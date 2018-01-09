package com.hodo.jjamtalk.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by boram on 2017-08-16.
 */

public class BoardMsgClientData {

    private BoardMsgDBData mDBData;
    public int ReplyCnt;
    public int LikeCnt;

    // Client에서 사용 하는 변수 (DB데이터랑은 완전 다름)
    public int ClientViewCount;

    public ArrayList<TempBoard_ReplyData> ReplyList = new ArrayList<>();
    public ArrayList<BoardLikeData> LikeList = new ArrayList<>();

    public BoardMsgClientData(BoardMsgDBData data)
    {
        SetDBdata(data);

        for(LinkedHashMap.Entry<String, TempBoard_ReplyData> entry : mDBData.Reply.entrySet())
            ReplyList.add(entry.getValue());

        for(LinkedHashMap.Entry<String, BoardLikeData> entry : mDBData.Like.entrySet())
            LikeList.add(entry.getValue());
    }

    public BoardMsgDBData GetDBData()
    {
        return mDBData;
    }

    public void SetDBdata(BoardMsgDBData data)
    {
        mDBData = data;
        if(mDBData.ViewCount == null)
            mDBData.ViewCount = "0";

        ClientViewCount = Integer.parseInt(mDBData.ViewCount);
        ReplyCnt = mDBData.Reply.size();
        LikeCnt = mDBData.Like.size();
    }

    public void PlusViewCount()
    {
        ClientViewCount++;
    }

    public void ChangeListCount(Boolean plus)
    {
        if(plus)
            LikeCnt += 1;
        else
            LikeCnt -= 1;
    }

    public void ChangeReplyCount(Boolean plus)
    {
        if(plus)
            ReplyCnt += 1;
        else
            ReplyCnt -= 1;
    }

    public Boolean IsLikeUser(String idx)
    {
        Iterator keyData = mDBData.Like.keySet().iterator();

        String key;
        while(keyData.hasNext())
        {
            key = (String)keyData.next();
            BoardLikeData data = (BoardLikeData)mDBData.Like.get(key);
            if(data.Idx.equals(idx))
                return true;
        }
        return false;
    }
}
