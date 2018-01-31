package com.hodo.jjamtalk.Firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hodo.jjamtalk.BoardFragment;
import com.hodo.jjamtalk.BoardWriteActivity;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.BoardMsgDBData;
import com.hodo.jjamtalk.Data.BoardReportData;
import com.hodo.jjamtalk.Data.CoomonValueData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.TempBoard_ReplyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Data.BoardLikeData;
import com.hodo.jjamtalk.MainActivity;
import com.hodo.jjamtalk.R;
import com.hodo.jjamtalk.Util.AwsFunc;
import com.hodo.jjamtalk.Util.CommonFunc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.hodo.jjamtalk.Data.CoomonValueData.FIRST_LOAD_BOARD_COUNT;
import static com.hodo.jjamtalk.Data.CoomonValueData.LOAD_BOARD_COUNT;
import static com.hodo.jjamtalk.Data.CoomonValueData.MAIN_ACTIVITY_BOARD;

/**
 * Created by boram on 2017-08-05.
 */

public class FirebaseData {

    private static FirebaseData _Instance;
    private MyData mMyData = MyData.getInstance();
    private AwsFunc mAwsFunc = AwsFunc.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

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

        long time = CommonFunc.getInstance().GetCurrentTime();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
        user.child("Date").setValue(ctime.format(new Date(time)));

        user.child("Memo").setValue(mMyData.getUserMemo());

        user.child("RecvMsg").setValue(mMyData.getnRecvMsg());

        user.child("FanCount").setValue(mMyData.getFanCount());

        // 심플 디비 저장
        SaveSimpleData();
    }

    public void SaveSimpleData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("SimpleData");//.child(mMyData.getUserIdx());

        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(mMyData.getUserIdx());
        user.child("Idx").setValue(mMyData.getUserIdx());

        mMyData.setUserToken(FirebaseInstanceId.getInstance().getToken());
        user.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
        user.child("Img").setValue(mMyData.getUserImg());

        user.child("NickName").setValue(mMyData.getUserNick());
        user.child("Gender").setValue(mMyData.getUserGender());
        user.child("Age").setValue(mMyData.getUserAge());

        user.child("Memo").setValue(mMyData.getUserMemo());

        user.child("RecvGold").setValue(mMyData.getRecvHoney());
        user.child("SendGold").setValue(mMyData.getSendHoney());

        user.child("Lon").setValue(mMyData.getUserLon());
        user.child("Lat").setValue(mMyData.getUserLat());


        long time = CommonFunc.getInstance().GetCurrentTime();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
        user.child("Date").setValue(ctime.format(new Date(time)));
        user.child("FanCount").setValue(mMyData.getFanCount());

        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성

        user.child("Point").setValue(mMyData.getPoint());

        user.child("Grade").setValue(mMyData.getGrade());
        user.child("BestItem").setValue(mMyData.bestItem);


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
    public void GetWriteAfterData(final Activity mActivity)
    {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = FirebaseDatabase.getInstance().getReference().child("Board").limitToFirst(FIRST_LOAD_BOARD_COUNT);

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    BoardData.getInstance().AddBoardData(postSnapshot, false);
                }

                mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_BOARD);
   /*
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra("StartFragment", 4);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(intent);
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void GetInitBoardData()
    {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = FirebaseDatabase.getInstance().getReference().child("Board").limitToFirst(FIRST_LOAD_BOARD_COUNT);

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    BoardData.getInstance().AddBoardData(postSnapshot, false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void GetInitMyBoardData()
    {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        Query data = FirebaseDatabase.getInstance().getReference().child("Board").orderByChild("Idx").equalTo(mMyData.getUserIdx());

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    BoardData.getInstance().AddBoardData(postSnapshot, true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // 게시판 관련 함수 (환웅)
    BoardFragment.BoardListAdapter UpdateBoardAdapter = null;
    public void GetBoardData(BoardFragment.BoardListAdapter updateBoardAdapter, long startIdx, Boolean top)
    {
        UpdateBoardAdapter = updateBoardAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = null;
        if(top)
            data = fierBaseDataInstance.getReference().child("Board").orderByChild("BoardIdx"). startAt(startIdx - LOAD_BOARD_COUNT).endAt(startIdx );
        else
            data = fierBaseDataInstance.getReference().child("Board").orderByChild("BoardIdx"). startAt(startIdx).endAt(startIdx + LOAD_BOARD_COUNT); // TODO 환웅 게시판의 마지막에 있는 친구 인덱스를 가져 온다.

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    BoardData.getInstance().AddBoardData(postSnapshot, false);
                }
                UpdateBoardAdapter.BoardDataLoding = false;
                UpdateBoardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private BoardWriteActivity WriteActivity = null;
    public void SaveBoardData_GetBoardIndex(final BoardWriteActivity activity) {
        WriteActivity = activity;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        DatabaseReference data = fierBaseDataInstance.getReference("BoardIdx");
        data.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long index = mutableData.getValue(Long.class);
                if(index == null)
                    return Transaction.success(mutableData);

                index--;

                mutableData.setValue(index);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // TODO 환웅 성공 했을때 오는 함수 인듯
                // TODO 환웅 콜백 함수가 있나?
                long index = dataSnapshot.getValue(long.class);
                BoardData.getInstance().BoardIdx = index;
                WriteActivity.SendBoard();


                BoardData.getInstance().ClearBoardData();
                FirebaseData.getInstance().GetInitMyBoardData();
                FirebaseData.getInstance().GetWriteAfterData(activity);
            }
        });
    }

    public boolean SaveBoardData(BoardMsgDBData sendData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.DATE_FORMAT);

        DatabaseReference writeBoardTable = database.getReference("Board").child(Long.toString(BoardData.getInstance().BoardIdx));

        long currentTime = CommonFunc.getInstance().GetCurrentTime();
        sendData.Date = ctime.format(new Date(currentTime));
        sendData.BoardIdx = BoardData.getInstance().BoardIdx;
        BoardData.getInstance().AddBoardData(sendData, true);
        writeBoardTable.setValue(sendData);

        DatabaseReference myTable = database.getReference("User").child(MyData.getInstance().getUserIdx()).child("LastBoardWriteTime");
        MyData.getInstance().SetLastBoardWriteTime(currentTime);
        myTable.setValue(Long.valueOf(currentTime));

        return  true;
    }

    public void RemoveBoard(long boardIdx)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board").child(Long.toString(boardIdx));
        table.removeValue();
    }

    public void ReportBoard(long boardIdx, String Idx)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Board").child(Long.toString(boardIdx)).child("ReportList").child(Idx);

        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.DATE_FORMAT);

        BoardReportData sendData = new BoardReportData();
        sendData.Idx = mMyData.getUserIdx();
        long time = CommonFunc.getInstance().GetCurrentTime();
        sendData.Date = ctime.format(new Date(time));

        table.setValue(sendData);
    }
}
