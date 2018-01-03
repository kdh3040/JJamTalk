package com.hodo.jjamtalk.Data;

import java.util.ArrayList;

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

    public  ArrayList<BoardMsgData> arrBoardList = new ArrayList<>();
    public  ArrayList<BoardMsgData> arrBoardMyList = new ArrayList<>();
}

