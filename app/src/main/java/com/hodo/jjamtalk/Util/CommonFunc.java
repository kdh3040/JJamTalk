package com.hodo.jjamtalk.Util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SimpleUserData;
import com.hodo.jjamtalk.MainActivity;
import com.hodo.jjamtalk.R;

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

    private MyData mMyData = MyData.getInstance();

    public void refreshMainActivity(Activity mActivity, int StartFragMent)
    {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra("StartFragment", StartFragMent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
    }

}
