package com.hodo.jjamtalk.Data;

import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by boram on 2017-08-10.
 */

public class SettingData {

    private MyData mMyData = MyData.getInstance();

    private static SettingData _Instance;

    private int nSearchSetting = 0;
    private int nAlarmSetting = 0;
    private int nViewSetting = 0;

    public static SettingData getInstance()
    {
        if(_Instance == null)
            _Instance = new SettingData();

        return  _Instance;
    }
    private SettingData()
    {
    }

    public  void setnAlarmSetting(int Option)
    {
        nAlarmSetting = Option;
        mMyData.nAlarmMode = nAlarmSetting;
    }
    public int getnAlarmSetting()
    {
        int rtValue = 0;

/*        if(mMyData.nAlarmMode == 0)
        {
            rtValue = 7;
        }
        else*/
            rtValue = mMyData.nAlarmMode;

        return   rtValue;
    }

    public  void setnSearchSetting(int Option)
    {
        nSearchSetting = Option;
        mMyData.nSearchMode = nSearchSetting;
    }
    public int getnSearchSetting()
    {
        int rtValue = 0;
        if(mMyData.nSearchMode == 0)
        {
            if(mMyData.getUserGender().equals("여자"))
                rtValue = 1;
            else
                rtValue = 2;
        }
        else
            rtValue = mMyData.nSearchMode;

        return  rtValue;
    }

    public  void setnViewSetting(int Option)
    {
        nViewSetting = Option;
        mMyData.nViewMode = nViewSetting;
    }
    public int getnViewSetting()
    {
        int rtValue = 0;
        if(mMyData.nViewMode == 0)
        {
           rtValue = 1;
        }
        else
            rtValue = mMyData.nViewMode;

        return  rtValue;
    }
    public int getViewCount()
    {
        int rtValue = 0;
        if(mMyData.nViewMode == 0)
            rtValue = 2;
        else if(mMyData.nViewMode == 1)
            rtValue = 3;
        else if(mMyData.nViewMode == 2)
            rtValue = 4;

            return  rtValue;
    }
}
