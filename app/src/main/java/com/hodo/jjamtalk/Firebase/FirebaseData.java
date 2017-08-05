package com.hodo.jjamtalk.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hodo.jjamtalk.Data.MyData;

import java.util.Random;

/**
 * Created by boram on 2017-08-05.
 */

public class FirebaseData {

    private static FirebaseData _Instance;
    private MyData mMyData = MyData.getInstance();

    public static FirebaseData getInstance()
    {
        if(_Instance == null)
            _Instance = new FirebaseData();

        return  _Instance;
    }

    private FirebaseData()
    {

    }

    public void SaveData(String userIdx)
    {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Users");

        userIdx = Integer.toString(rand.nextInt(100));

        DatabaseReference user = table.child( userIdx);
        user.child("NickName").setValue(mMyData.getUserNick());
        user.child("Age").setValue(mMyData.getUserAge());
        user.child("Gender").setValue(mMyData.getUserGender());
        user.child("Img").setValue(mMyData.getUserImg());

        user.child("Lon").setValue(mMyData.getUserLon());
        user.child("Lat").setValue(mMyData.getUserLat());



        user.child("Rank").setValue(rand.nextInt(100));
        user.child("Heart").setValue(rand.nextInt(30));

    }

    public Query getQuery(DatabaseReference databaseReference, int select)
    {
        Query resultQuery = null;
        switch (select)
        {
            case 1:
                resultQuery = databaseReference.child("Heart");
                break;
        }
        return resultQuery;
    }
}
