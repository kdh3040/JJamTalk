package com.hodo.jjamtalk.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by boram on 2017-08-16.
 */

public class BoardMsgClientData {

    private BoardMsgDBData mDBData;
    public int LikeCnt;

    public ArrayList<TempBoard_ReplyData> ReplyList = new ArrayList<>();
    public ArrayList<BoardLikeData> LikeList = new ArrayList<>();

    public BoardMsgClientData(BoardMsgDBData data)
    {
        SetDBdata(data);

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

        LikeCnt = mDBData.Like.size();
    }
    public void ChangeListCount(Boolean plus)
    {
        if(plus)
            LikeCnt += 1;
        else
            LikeCnt -= 1;
    }

    public void AddLikeData(BoardLikeData data)
    {
        LikeList.add(data);
        mDBData.Like.put(data.Idx, data);
    }
    public void RemoveLikeData(String idx)
    {
        for(BoardLikeData data : LikeList)
        {
            if(data.Idx.equals(idx))
            {
                LikeList.remove(data);
                break;
            }
        }
        mDBData.Like.remove(idx);
    }
    public Boolean IsLikeUser(String idx)
    {
        for(BoardLikeData data : LikeList)
        {
            if(data.Idx.equals(idx))
                return true;
        }

        return false;
    }
}
