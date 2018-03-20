package com.hodo.talkking.Data;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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

    public long TopBoardIdx = 0;
    public long BottomBoardIdx = -Long.MAX_VALUE;

    public long loadingCount = 0;
    public long TempTopBoardIdx = 0;
    public long TempBottomBoardIdx = -Long.MAX_VALUE;

    public void ClearBoardData()
    {
        BoardList.clear();
        MyBoardList.clear();
    }

    public void AddBoardMyData(BoardMsgDBData dbData)
    {
        BoardMsgClientData ClientData  = GetBoardMsgClientData(dbData.BoardIdx, true);
        if(ClientData == null)
        {
            ClientData = new BoardMsgClientData(dbData);
            MyBoardList.add(ClientData);
            Collections.sort(MyBoardList, new BoardSort());
        }
        else
            ClientData.SetDBdata(dbData);
    }
    public void AddBoardData(BoardMsgDBData dbData)
    {
        BoardMsgClientData ClientData  = GetBoardMsgClientData(dbData.BoardIdx, false);

        if(BottomBoardIdx < dbData.BoardIdx)
            BottomBoardIdx = dbData.BoardIdx;

        if(TopBoardIdx > dbData.BoardIdx)
            TopBoardIdx = dbData.BoardIdx;

        if(ClientData == null)
        {
            ClientData = new BoardMsgClientData(dbData);
            if(ClientData.IsReportUser(MyData.getInstance().getUserIdx()))
                return;

            if(CoomonValueData.getInstance().BoardLoadingDate > 0 && dbData.Date < CoomonValueData.getInstance().BoardLoadingDate)
            {
                CoomonValueData.getInstance().BoardPastLoading = false;
                return;
            }

            BoardList.add(ClientData);
            Collections.sort(BoardList, new BoardSort());
        }
        else
        {
            if(ClientData.IsReportUser(MyData.getInstance().getUserIdx()))
                return;
            ClientData.SetDBdata(dbData);
        }
    }

    public void AddBoardData(BoardMsgDBData dbData, Boolean myData)
    {
        BoardMsgClientData ClientData  = GetBoardMsgClientData(dbData.BoardIdx, myData);

        ArrayList<BoardMsgClientData> tempList;
        if(myData)
            tempList = MyBoardList;
        else
            tempList = BoardList;

        if(ClientData == null)
        {
            ClientData = new BoardMsgClientData(dbData);
            tempList.add(ClientData);
            Collections.sort(tempList, new BoardSort());
        }
        else
            ClientData.SetDBdata(dbData);
    }

    public void AddBoardSimpleUserData(SimpleUserData simpleUserData)
    {
        if(simpleUserData == null)
            return;

        for(BoardMsgClientData data : BoardList)
        {
            if(data.GetDBData().Idx.equals(simpleUserData.Idx))
                data.SetBoardSimpleUserData(simpleUserData);
        }

        for(BoardMsgClientData data : MyBoardList)
        {
            if(data.GetDBData().Idx.equals(simpleUserData.Idx))
                data.SetBoardSimpleUserData(simpleUserData);
        }
    }

    public BoardMsgClientData GetBoardMsgClientData(long boardIdx, Boolean myData)
    {
        if(myData)
        {
            for(BoardMsgClientData data : MyBoardList)
            {
                if(data.GetDBData().BoardIdx == boardIdx)
                    return data;
            }
        }
        else
        {
            for(BoardMsgClientData data : BoardList)
            {
                if(data.GetDBData().BoardIdx == boardIdx)
                    return data;
            }

        }
        return null;
    }

    public void RemoveBoard(long boardIdx)
    {
        for(BoardMsgClientData data : BoardList)
        {
            if(data.GetDBData().BoardIdx == boardIdx)
            {
                BoardList.remove(data);
                break;
            }
        }

        for(BoardMsgClientData data : MyBoardList)
        {
            if(data.GetDBData().BoardIdx == boardIdx)
            {
                MyBoardList.remove(data);
                break;
            }
        }
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

