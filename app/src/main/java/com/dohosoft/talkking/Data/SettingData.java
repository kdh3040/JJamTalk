package com.dohosoft.talkking.Data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by boram on 2017-08-10.
 */

public class SettingData {

    private MyData mMyData = MyData.getInstance();

    private static SettingData _Instance;

    private int nSearchSetting = 0;
    private int nAlarmSetting = 0;
    private int nViewSetting = 0;
    private boolean nRecvMsgReject = false;
    public static SettingData getInstance()
    {
        if(_Instance == null)
            _Instance = new SettingData();

        return  _Instance;
    }
    private SettingData()
    {
    }

    public boolean IsAlarmSettingSound()
    {
        return mMyData.nAlarmSetting_Sound;
    }

    public boolean IsAlarmSettingVibration()
    {
        return mMyData.nAlarmSetting_Vibration;
    }
    public boolean IsAlarmSettingPop()
    {
        return mMyData.nAlarmSetting_Pop;
    }

    public void setRecvMsgRejectSetting(boolean recvMsgReject)
    {
        nRecvMsgReject = recvMsgReject;
        mMyData.nRecvMsgReject = recvMsgReject;
    }
    public boolean IsRecyMsgRejectSetting(){return nRecvMsgReject;}

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
            if(mMyData.getUserGender() != null)
            {
                if(mMyData.getUserGender().equals("여자"))
                    rtValue = 1;
                else
                    rtValue = 2;
            }
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
/*       if(mMyData.nViewMode == 0)
        {
           rtValue = 1;
        }
        else*/
            rtValue = mMyData.nViewMode;

        return  rtValue;
    }
    public int getViewCount()
    {

        int rtValue = 0;
/*
        if(mMyData.nViewMode == 0)
            rtValue = 2;
        else if(mMyData.nViewMode == 1)
            rtValue = 3;
        else if(mMyData.nViewMode == 2)
            rtValue = 4;
*/


        rtValue = 3;
            return  rtValue;
    }
}
