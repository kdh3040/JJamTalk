package com.hodo.jjamtalk.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by boram on 2017-07-19.
 */

public class MyFirebaseInstanceIdSerivce extends FirebaseInstanceIdService {
    private final static String TAG = "FCM_ID";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FirebaseInstanceId Refreshed token: " + refreshedToken);
    }
}
