package com.hodo.jjamtalk.Data;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by boram on 2017-08-16.
 */

public class BoardData {

    private static BoardData _Instance;

    public static BoardData getInstance()
    {
        if(_Instance == null)
            _Instance = new BoardData();

        return  _Instance;
    }

    private BoardData()
    {
    }

    public long BoardIdx = 0;
    public ArrayList<BoardMsgClientData> BoardList = new ArrayList<>();
    public ArrayList<BoardMsgClientData> MyBoardList = new ArrayList<>();

    public void AddBoardData(DataSnapshot dataSnapshot)
    {
        BoardMsgDBData DBData = dataSnapshot.getValue(BoardMsgDBData.class);
        BoardMsgClientData ClientData  = GetBoardMsgClientData(DBData.BoardIdx);

        if(ClientData == null)
        {
            ClientData = new BoardMsgClientData(DBData);
            BoardList.add(ClientData);
            Collections.sort(BoardList, new BoardSort());
        }
        else
            ClientData.SetDBdata(DBData);
    }

    public BoardMsgClientData GetBoardMsgClientData(long boardIdx)
    {
        for(BoardMsgClientData data : BoardList)
        {
            if(data.GetDBData().BoardIdx == boardIdx)
                return data;
        }

        return null;
    }

    public void AddMyBoardData(DataSnapshot dataSnapshot)
    {
        BoardMsgDBData DBData = dataSnapshot.getValue(BoardMsgDBData.class);
        BoardMsgClientData ClientData  = GetMyBoardMsgClientData(DBData.BoardIdx);
        if(ClientData == null)
        {
            ClientData = new BoardMsgClientData(DBData);
            MyBoardList.add(ClientData);
        }
        else
            ClientData.SetDBdata(DBData);
    }

    public BoardMsgClientData GetMyBoardMsgClientData(long boardIdx)
    {
        for(BoardMsgClientData data : MyBoardList)
        {
            if(data.GetDBData().BoardIdx == boardIdx)
                return data;
        }

        return null;
    }
}

class BoardSort implements Comparator<BoardMsgClientData>{
    @Override
    public int compare(BoardMsgClientData a, BoardMsgClientData b)
    {
        if(a.GetDBData().BoardIdx > b.GetDBData().BoardIdx )
            return 1;
        else if(a.GetDBData().BoardIdx < b.GetDBData().BoardIdx )
            return -1;
        else
            return 0;
    }
}

