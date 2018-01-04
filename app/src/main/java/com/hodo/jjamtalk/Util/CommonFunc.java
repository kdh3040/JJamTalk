package com.hodo.jjamtalk.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by woong on 2018-01-05.
 */

public class CommonFunc {

    private static CommonFunc _Instance;

    public static CommonFunc getInstance() {
        if (_Instance == null)
            _Instance = new CommonFunc();

        return _Instance;
    }

    private CommonFunc()
    {

    }

    public void refreshFragMent(Fragment fragment)
    {
        FragmentTransaction trans = fragment.getFragmentManager().beginTransaction();
        trans.detach(fragment).attach(fragment).commit();
    }
}
