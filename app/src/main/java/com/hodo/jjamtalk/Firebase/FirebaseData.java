package com.hodo.jjamtalk.Firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.BoardMsgClientData;
import com.hodo.jjamtalk.Data.BoardMsgDBData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.TempBoard_ReplyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Data.BoardLikeData;
import com.hodo.jjamtalk.Util.AwsFunc;

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
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());

        userIdx = Integer.toString(rand.nextInt(100));

        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(mMyData.getUserIdx());
        user.child("Idx").setValue(mMyData.getUserIdx());

        mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
        user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
        user.child("Img").setValue(mMyData.getUserImg());

        for(int i=0; i< 4 ; i++)
            user.child("ImgGroup"+Integer.toString(i)).setValue(mMyData.getUserProfileImg(i));

        user.child("NickName").setValue(mMyData.getUserNick());
        user.child("Gender").setValue(mMyData.getUserGender());
        user.child("Age").setValue(mMyData.getUserAge());

        user.child("Lon").setValue(mMyData.getUserLon());
        user.child("Lat").setValue(mMyData.getUserLat());

        user.child("SendCount").setValue(mMyData.getSendHoney());
        user.child("RecvCount").setValue(mMyData.getRecvHoney());

        user.child("ImgCount").setValue(mMyData.getUserImgCnt());

        long time = System.currentTimeMillis();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
        user.child("Date").setValue(ctime.format(new Date(time)));

        user.child("Memo").setValue(mMyData.getUserMemo());

        user.child("RecvMsg").setValue(mMyData.getnRecvMsg());

        user.child("FanCount").setValue(mMyData.getFanCount());
    }

    public boolean SaveBoardReplyData(TempBoard_ReplyData strMemo) {

        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board").child(strMemo.Key).child("Reply");

        TempBoard_ReplyData tempData = new TempBoard_ReplyData();

        tempData.Idx = strMemo.Idx;
        tempData.NickName = strMemo.NickName;
        tempData.Age = strMemo.Age;
        tempData.Img = strMemo.Img;
        tempData.Msg = strMemo.Msg;

        table.push().setValue(tempData);

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

    public void DelCardList(String Idx)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query queryRef = database.getReference("User").orderByValue().equalTo(Idx);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void DelChatData(String Idx)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query queryRef = database.getReference("ChatData").orderByValue().equalTo(Idx);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void DelSendData(String Idx)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query queryRef = database.getReference("SendList").child(mMyData.getUserIdx()).orderByChild("strSendName").equalTo(Idx);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                snapshot.getRef().setValue(null);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SaveSettingData(String userIdx, int SearchMode, int AlarmMode, int ViewMode, int RecvMsg) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Setting");

        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(mMyData.getUserIdx());

        user.child("SearchMode").setValue(SearchMode);
        user.child("AlarmMode").setValue(AlarmMode);
        user.child("ViewMode").setValue(ViewMode);
        user.child("RecvMsg").setValue(RecvMsg);

        SaveData(mMyData.getUserIdx());

    }


    // 게시판 관련 함수 (환웅)

    public void PushBoardViewCount(String boardkey)
    {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        DatabaseReference data = fierBaseDataInstance.getReference("Board").child(boardkey);
        data.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                BoardMsgDBData boardData = mutableData.getValue(BoardMsgDBData.class);
                if (boardData == null) {
                    return Transaction.success(mutableData);
                }
                if(boardData.ViewCount == null)
                    boardData.ViewCount = "0";

                int viewCount = Integer.parseInt(boardData.ViewCount);
                viewCount++;
                boardData.ViewCount = Integer.toString(viewCount);
                mutableData.setValue(boardData);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // TODO 환웅 성공 했을때 오는 함수 인듯
                BoardData.getInstance().AddBoardData(dataSnapshot);
            }
        });
    }


    /*public boolean SaveBoardData(BoardMsgData sendData) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board").push();

        long time = System.currentTimeMillis();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");

        sendData.Date = ctime.format(new Date(time));
        sendData.Key = table.getKey();

        table.setValue(sendData);

        return  true;
    }*/

    public boolean SaveBoardLikeData(String boardKey, BoardLikeData sendData)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board").child(boardKey).child("Like").child(sendData.Idx);

        long time = System.currentTimeMillis();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");

        BoardLikeData tempData = new BoardLikeData();

        tempData.Idx = sendData.Idx;
        tempData.Img = sendData.Img;
        tempData.Date = ctime.format(new Date(time));

        table.setValue(tempData);

        return  true;
    }

    public boolean RemoveBoardLikeData(String boardKey, String Idx)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board").child(boardKey).child("Like");
        table.child(Idx).removeValue();
        return  true;
    }
}
