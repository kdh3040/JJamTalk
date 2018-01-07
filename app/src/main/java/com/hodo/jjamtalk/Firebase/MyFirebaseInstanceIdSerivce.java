package com.hodo.jjamtalk.Firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hodo.jjamtalk.Data.MyData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by boram on 2017-07-19.
 */

public class MyFirebaseInstanceIdSerivce extends FirebaseInstanceIdService {
    private final static String TAG = "FCM_ID";
    private MyData mMyData = MyData.getInstance();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference table = database.getReference("User");
        if(mMyData != null)
        {
            final DatabaseReference user = table.child(mMyData.getUserIdx());
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("Token", refreshedToken);
            table.updateChildren(updateMap);
            mMyData.setUserToken(refreshedToken);
        }
        /*final DatabaseReference user = table.child(mMyData.getUserIdx());
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("Token", refreshedToken);
        table.updateChildren(updateMap);
        mMyData.setUserToken(refreshedToken);*/

        Log.d(TAG, "FirebaseInstanceId Refreshed token: " + refreshedToken);
    }
}
