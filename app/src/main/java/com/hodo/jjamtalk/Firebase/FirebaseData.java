package com.hodo.jjamtalk.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.TempBoardData;
import com.hodo.jjamtalk.Data.TempBoard_ReplyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.AwsFunc;
import com.kakao.usermgmt.response.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
        final DatabaseReference user = table.child(mMyData.getUserIdx());
        user.child("Idx").setValue(mMyData.getUserIdx());
        mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
        user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
        user.child("Img").setValue(mMyData.getUserImg());

        for(int i=0; i<mMyData.arrImgList.size(); i++)
            user.child("ImgGroup"+Integer.toString(i)).setValue(mMyData.getUserImgList(i));

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

        user.child("Memo").setValue(mMyData.getUserMemo());
        user.child("Scool").setValue(mMyData.getUserSchool());
        user.child("Company").setValue(mMyData.getUserCompany());
        user.child("Title").setValue(mMyData.getUserTitle());

    }
    public boolean SaveBoardData(String strMemo) {

        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board");

        TempBoardData sendData = new TempBoardData();

        sendData.Idx = mMyData.getUserIdx();
        sendData.NickName = mMyData.getUserNick();
        sendData.Age = mMyData.getUserAge();
        sendData.Img = mMyData.getUserImg();
        sendData.Job = mMyData.getUserCompany();
        //sendData. = mMyData.getUserImg();

        long time = System.currentTimeMillis();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");

        sendData.Date = ctime.format(new Date(time));
        sendData.Msg = strMemo;

        table.push().setValue(sendData);

        return  true;
    }



    public boolean SaveBoardReplyData(TempBoard_ReplyData strMemo) {

        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board");

        TempBoardData sendData = new TempBoardData();

        sendData.Idx = strMemo.Idx;
        sendData.NickName = strMemo.NickName;
        sendData.Age = strMemo.Age;
        sendData.Img = strMemo.Img;
        //sendData. = mMyData.getUserImg();

        long time = System.currentTimeMillis();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");

        sendData.Date = ctime.format(new Date(time));
        sendData.Msg = strMemo.Msg;

        table.push().setValue(sendData);

        return  true;
    }

    public void setHeart(UserData stTargetData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Users").child(stTargetData.Gender);
        final DatabaseReference user = table.child(stTargetData.Idx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("Heart", stTargetData.Heart+1);
        user.updateChildren(updateMap);


    }

    public void setHoney(UserData stTargetData, int nGiftCnt) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Users").child(stTargetData.Gender);
        final DatabaseReference user = table.child(stTargetData.Idx);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("Honey", stTargetData.Honey+nGiftCnt);
        user.updateChildren(updateMap);
    }

}
