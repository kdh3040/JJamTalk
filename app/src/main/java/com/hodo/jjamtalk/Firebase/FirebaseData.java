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

    public void GetUserData()
    {
        DatabaseReference refMan, refWoman;
        refMan = FirebaseDatabase.getInstance().getReference().child("Users").child("남자");
        refMan.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.mUserManData.add(stRecvData);
                                Log.d("Main Man : ", mMyData.mUserManData.get(i).NickName);

                            }
                            i++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });

        refWoman = FirebaseDatabase.getInstance().getReference().child("Users").child("여자");
        refWoman.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.mUserWomanData.add(stRecvData);
                                Log.d("Main Woman : ", mMyData.mUserWomanData.get(i).NickName);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });

    }

    public void GetMyData()
    {
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        String strMyIdx = mAwsFunc.GetUserIdx(Auth.getCurrentUser().getEmail());
        strMyIdx = "78";
        DatabaseReference ref;

        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("여자").child(strMyIdx);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                            UserData stRecvData = new UserData ();
                            stRecvData = dataSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.setMyData(stRecvData.Idx, stRecvData.Img, stRecvData.NickName, stRecvData.Gender, stRecvData.Age, stRecvData.Lon, stRecvData.Lat, stRecvData.Heart, stRecvData.Rank);
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });

        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("남자").child(strMyIdx);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                            UserData stRecvData = new UserData ();
                            stRecvData = dataSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.setMyData(stRecvData.Idx, stRecvData.Img, stRecvData.NickName, stRecvData.Gender, stRecvData.Age, stRecvData.Lon, stRecvData.Lat, stRecvData.Heart, stRecvData.Rank);
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });
    }

    public void SaveData(String userIdx)
    {
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
