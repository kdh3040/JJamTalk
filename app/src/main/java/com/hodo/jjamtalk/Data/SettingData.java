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

    public static SettingData getInstance()
    {
        if(_Instance == null)
            _Instance = new SettingData();

        return  _Instance;
    }
    private SettingData()
    {
        if(mMyData.getUserGender().equals("여자"))
            nSearchSetting = 1;
        else
            nSearchSetting = 2;

        nAlarmSetting = 0;
    }

    public  void setnAlarmSetting(int Option)
    {
        nAlarmSetting = Option;
    }
    public int getnAlarmSetting()
    {
        return  nAlarmSetting;
    }

    public  void setnSearchSetting(int Option)
    {
        nSearchSetting = Option;
    }
    public int getnSearchSetting()
    {
        return  nSearchSetting;
    }

}
