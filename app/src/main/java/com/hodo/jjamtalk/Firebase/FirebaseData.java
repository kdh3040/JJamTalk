package com.hodo.jjamtalk.Firebase;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.AwsFunc;
import com.kakao.usermgmt.response.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by boram on 2017-08-05.
 */

public class FirebaseData {

    private static FirebaseData _Instance;
    private MyData mMyData = MyData.getInstance();
    private AwsFunc mAwsFunc = AwsFunc.getInstance();

    public static FirebaseData getInstance()
    {
        if(_Instance == null)
            _Instance = new FirebaseData();

        return  _Instance;
    }

    private FirebaseData()
    {

    }

    public void SaveData(String userIdx) {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Users").child(mMyData.getUserGender());

        userIdx = Integer.toString(rand.nextInt(100));

        // DatabaseReference user = table.child( userIdx);
        DatabaseReference user = table.child(mMyData.getUserIdx());
        user.child("Idx").setValue(mMyData.getUserIdx());
        user.child("Img").setValue(mMyData.getUserImg());
        user.child("NickName").setValue(mMyData.getUserNick());
        user.child("Gender").setValue(mMyData.getUserGender());
        user.child("Age").setValue(mMyData.getUserAge());

        user.child("Lon").setValue(mMyData.getUserLon());
        user.child("Lat").setValue(mMyData.getUserLat());

        user.child("Rank").setValue(rand.nextInt(100));
        user.child("Hot").setValue(rand.nextInt(30));

        long time = System.currentTimeMillis();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
        user.child("Date").setValue(ctime.format(new Date(time)));

    }
}
