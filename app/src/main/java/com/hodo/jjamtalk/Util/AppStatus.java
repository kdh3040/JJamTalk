package com.hodo.jjamtalk.Util;

/**
 * Created by boram on 2017-08-10.
 */

public class AppStatus {


    private static AppStatus _Instance;

    public static AppStatus getInstance() {
        if (_Instance == null)
            _Instance = new AppStatus();

        return _Instance;
    }

    private AppStatus()
    {

    }

    public boolean bCheckMultiSend = false;

}
