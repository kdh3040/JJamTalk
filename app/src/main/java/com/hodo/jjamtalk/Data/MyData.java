package com.hodo.jjamtalk.Data;

/**
 * Created by boram on 2017-08-04.
 */

public class MyData {

    private static MyData _Instance;

    public static MyData getInstance()
    {
        if(_Instance == null)
            _Instance = new MyData();

        return  _Instance;
    }

    private String UserIdx;


    private MyData()
    {

    }

    public void setUserIdx(String userIdx) {
        UserIdx = userIdx;
    }

    public String getUserIdx() {
        return UserIdx;
    }
}
