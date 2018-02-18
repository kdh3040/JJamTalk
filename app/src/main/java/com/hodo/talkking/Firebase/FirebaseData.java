package com.hodo.talkking.Firebase;

import android.app.Activity;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.hodo.talkking.BoardFragment;
import com.hodo.talkking.BoardWriteActivity;
import com.hodo.talkking.Data.BoardData;
import com.hodo.talkking.Data.BoardMsgDBData;
import com.hodo.talkking.Data.BoardReportData;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.TempBoard_ReplyData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Rank_FanRichAdapter;
import com.hodo.talkking.Rank_GoldReceiveAdapter;
import com.hodo.talkking.Rank_NearAdapter;
import com.hodo.talkking.Rank_NewMemberAdapter;
import com.hodo.talkking.Util.AwsFunc;
import com.hodo.talkking.Util.CommonFunc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.hodo.talkking.Data.CoomonValueData.LOAD_MAIN_COUNT;

import static com.hodo.talkking.Data.CoomonValueData.FIRST_LOAD_BOARD_COUNT;
import static com.hodo.talkking.Data.CoomonValueData.LOAD_BOARD_COUNT;
import static com.hodo.talkking.Data.CoomonValueData.MAIN_ACTIVITY_BOARD;
import static com.hodo.talkking.MainActivity.mFragmentMng;

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

    public String  CreateUserIdx(final String Uid)
    {
        final long[] tempVal = {0};
        final String[] rtStr = new String[1];

        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        DatabaseReference data = fierBaseDataInstance.getReference("UserCount");
        data.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long index = mutableData.getValue(Long.class);
                if(index == null)
                    return Transaction.success(mutableData);

                index++;

                mutableData.setValue(index);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // TODO 환웅 성공 했을때 오는 함수 인듯
                // TODO 환웅 콜백 함수가 있나?
                tempVal[0] = dataSnapshot.getValue(long.class);
                rtStr[0] = Long.toString(tempVal[0]);

                mMyData.setUserIdx(rtStr[0]);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference table = database.getReference("UserIdx");
                final DatabaseReference user = table.child(Uid);
                user.setValue(rtStr[0]);


            }
        });

        return rtStr[0];
    }

    public String  GetUserIdx(final String Uid)
    {
        final long[] tempVal = {0};
        final String[] rtStr = new String[1];

        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        Query data = FirebaseDatabase.getInstance().getReference().child("UserIdx").child(Uid);

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rtStr[0] = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rtStr[0];
    }


    public void   DelUser(final String Uid, final  String Idx)
    {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        DatabaseReference data = fierBaseDataInstance.getReference("UserIdx").child(Uid);
        data.removeValue();

        data = fierBaseDataInstance.getReference("User").child(Idx);
        data.removeValue();

        data = fierBaseDataInstance.getReference("UserIdx_History");
        final DatabaseReference user = data;
        user.push().child(Uid).setValue(Idx);

    }

    public void SaveData(String userIdx) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());


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

        user.child("RecvMsgReject").setValue(mMyData.nRecvMsgReject ? 1 : 0);

        user.child("FanCount").setValue(-1 * mMyData.getFanCount());

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
        user.child("FanCount").setValue(-1 * mMyData.getFanCount());

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
        DatabaseReference table;
        table = database.getReference( "/ChatData/").child(Idx);
        table.removeValue();
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

    public void SaveSettingData(String userIdx, int SearchMode, int ViewMode, boolean RecvMsgReject, boolean alarmSetting_Sound, boolean alarmSetting_Vibration) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("Setting");

        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(mMyData.getUserIdx());

        user.child("SearchMode").setValue(SearchMode);
        user.child("ViewMode").setValue(ViewMode);
        user.child("RecvMsgReject").setValue((RecvMsgReject) ? 1 : 0);
        user.child("AlarmMode_Sound").setValue(alarmSetting_Sound);
        user.child("AlarmMode_Vibration").setValue(alarmSetting_Vibration);
        user.child("StartAge").setValue(mMyData.nStartAge);
        user.child("EndAge").setValue(mMyData.nEndAge);

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

                mActivity.finish();

                Fragment frg = null;
                frg = mFragmentMng.findFragmentByTag("BoardFragment");
                final FragmentTransaction ft = mFragmentMng.beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commitAllowingStateLoss();


               // mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_BOARD);
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
                if(dataSnapshot == null)
                    return;
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
                if(dataSnapshot == null)
                    return;
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

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot == null)
                    return;
                long topIndex = BoardData.getInstance().TopBoardIdx;
                long bottomIndex = BoardData.getInstance().BottomBoardIdx;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    BoardData.getInstance().AddBoardData(postSnapshot, false);
                }
                UpdateBoardAdapter.BoardDataLoding = false;

                if(topIndex != BoardData.getInstance().TopBoardIdx || bottomIndex != BoardData.getInstance().BottomBoardIdx)
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


    Rank_GoldReceiveAdapter UpdateHotAdapter = null;
    public void GetHotData(Rank_GoldReceiveAdapter updateAdapter, int lastVisibleCount, Boolean top)
    {
        UpdateHotAdapter = updateAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = null;
        if(top)
            data = fierBaseDataInstance.getReference().child("HotMember").orderByChild("Point"). startAt(0).endAt(LOAD_MAIN_COUNT );
        else
            data = fierBaseDataInstance.getReference().child("HotMember").orderByChild("Point").limitToFirst(lastVisibleCount + LOAD_MAIN_COUNT);// . startAt(lastVisibleCount + LOAD_MAIN_COUNT).endAt(lastVisibleCount + LOAD_MAIN_COUNT + LOAD_MAIN_COUNT); // TODO 환웅 게시판의 마지막에 있는 친구 인덱스를 가져 온다.

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    SimpleUserData cTempData = new SimpleUserData();
                    cTempData = postSnapshot.getValue(SimpleUserData.class);
                    if(cTempData != null) {
                        if (!cTempData.Idx.equals(mMyData.getUserIdx())) {
                            if (cTempData.Img == null)
                                cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                            boolean bExist = false;

                            for (int j = 0; j < mMyData.arrUserAll_Recv.size(); j++) {
                                if (mMyData.arrUserAll_Recv.get(j).Idx.equals(cTempData.Idx)) {
                                    bExist = true;
                                    break;
                                }
                            }

                            if (bExist == false) {
                                mMyData.arrUserAll_Recv.add(cTempData);
                                if (mMyData.arrUserAll_Recv.get(i).Gender.equals("여자")) {
                                    mMyData.arrUserWoman_Recv.add(mMyData.arrUserAll_Recv.get(i));
                                } else {
                                    mMyData.arrUserMan_Recv.add(mMyData.arrUserAll_Recv.get(i));
                                }

                                mMyData.arrUserAll_Recv_Age = mMyData.SortData_Age(mMyData.arrUserAll_Recv, mMyData.nStartAge, mMyData.nEndAge);
                                mMyData.arrUserWoman_Recv_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Recv, mMyData.nStartAge, mMyData.nEndAge);
                                mMyData.arrUserMan_Recv_Age = mMyData.SortData_Age(mMyData.arrUserMan_Recv, mMyData.nStartAge, mMyData.nEndAge);
                            }
                            i++;
                        }
                    }
                }

                UpdateHotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    Rank_FanRichAdapter UpdateFanAdapter = null;
    public void GetFanData(Rank_FanRichAdapter updateAdapter, int lastVisibleCount, Boolean top)
    {
        UpdateFanAdapter = updateAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.
        Query data = null;
        if(top)
            data = fierBaseDataInstance.getReference().child("SimpleData").orderByChild("FanCount"). startAt(0).endAt(LOAD_MAIN_COUNT );
        else
            data = fierBaseDataInstance.getReference().child("SimpleData").orderByChild("FanCount").limitToFirst(lastVisibleCount + LOAD_MAIN_COUNT);// . startAt(lastVisibleCount + LOAD_MAIN_COUNT).endAt(lastVisibleCount + LOAD_MAIN_COUNT + LOAD_MAIN_COUNT); // TODO 환웅 게시판의 마지막에 있는 친구 인덱스를 가져 온다.

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    SimpleUserData cTempData = new SimpleUserData();
                    cTempData = postSnapshot.getValue(SimpleUserData.class);
                    if(cTempData != null) {

                        if (!cTempData.Idx.equals(mMyData.getUserIdx())) {
                            if (cTempData.Img == null)
                                cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                            boolean bExist = false;

                            for (int j = 0; j < mMyData.arrUserAll_Send.size(); j++) {
                                if (mMyData.arrUserAll_Send.get(j).Idx.equals(cTempData.Idx)) {
                                    bExist = true;
                                    break;
                                }
                            }

                            if (bExist == false) {
                                mMyData.arrUserAll_Send.add(cTempData);
                                if (mMyData.arrUserAll_Send.get(i).Gender.equals("여자")) {
                                    mMyData.arrUserWoman_Send.add(mMyData.arrUserAll_Send.get(i));
                                } else {
                                    mMyData.arrUserMan_Send.add(mMyData.arrUserAll_Send.get(i));
                                }

                                mMyData.arrUserAll_Send_Age = mMyData.SortData_Age(mMyData.arrUserAll_Send, mMyData.nStartAge, mMyData.nEndAge);
                                mMyData.arrUserWoman_Send_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Send, mMyData.nStartAge, mMyData.nEndAge);
                                mMyData.arrUserMan_Send_Age = mMyData.SortData_Age(mMyData.arrUserMan_Send, mMyData.nStartAge, mMyData.nEndAge);
                            }
                            i++;
                        }
                    }

                }

                UpdateFanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    Rank_NearAdapter UpdateNearAdapter = null;
    public void GetNearData(Rank_NearAdapter updateAdapter, int lastVisibleCount, Boolean top)
    {
        UpdateNearAdapter = updateAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        // 현재 내가 바라 보고 있는 게시판 데이터를 가져온다.

        Double lStartLon = mMyData.getUserLon() - 10;
        Double lStartLat = mMyData.getUserLat() - 10;

        Double lEndLon = mMyData.getUserLon() + 10;
        Double IEndLat = mMyData.getUserLon() + 10;

        Query data = null;
        if(top)
            data = fierBaseDataInstance.getReference().child("SimpleData").orderByChild("Lon")
                    .startAt(lStartLon).endAt(lEndLon).limitToFirst(LOAD_MAIN_COUNT);
        else
            data = fierBaseDataInstance.getReference().child("SimpleData").orderByChild("Lon")
                    .startAt(lStartLon).endAt(lEndLon).limitToFirst(lastVisibleCount + LOAD_MAIN_COUNT);

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    SimpleUserData cTempData = new SimpleUserData();
                    cTempData = postSnapshot.getValue(SimpleUserData.class);
                    if(cTempData != null) {

                        if(!cTempData.Idx.equals(mMyData.getUserIdx()))
                        {

                            if(cTempData.Img == null)
                                cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                            boolean bExist = false;

                            for(int j = 0 ; j< mMyData.arrUserAll_Near.size(); j++)
                            {
                                if(mMyData.arrUserAll_Near.get(j).Idx.equals(cTempData.Idx))
                                {
                                    bExist = true;
                                    break;
                                }
                            }

                            if(bExist == false)
                            {
                                mMyData.arrUserAll_Near.add(cTempData);
                                if(mMyData.arrUserAll_Near.get(i).Gender.equals("여자"))
                                {
                                    mMyData.arrUserWoman_Near.add(mMyData.arrUserAll_Near.get(i));
                                }
                                else {
                                    mMyData.arrUserMan_Near.add(mMyData.arrUserAll_Near.get(i));
                                }

                                mMyData.arrUserAll_Near_Age = mMyData.SortData_Age(mMyData.arrUserAll_Near, mMyData.nStartAge, mMyData.nEndAge );
                                mMyData.arrUserWoman_Near_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Near, mMyData.nStartAge, mMyData.nEndAge );
                                mMyData.arrUserMan_Near_Age = mMyData.SortData_Age(mMyData.arrUserMan_Near, mMyData.nStartAge, mMyData.nEndAge );
                            }
                            i++;
                        }
                    }

                }

                UpdateNearAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    Rank_NewMemberAdapter UpdateNewAdapter = null;
    public void GetNewData(Rank_NewMemberAdapter updateAdapter, int lastVisibleCount, Boolean top)
    {
        UpdateNewAdapter = updateAdapter;
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        long time = CommonFunc.getInstance().GetCurrentTime();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
        int nTodayDate =  Integer.parseInt(ctime.format(new Date(time)));
        int nStartDate = nTodayDate - 7;

        Query data = null;
        if(top)
            data = fierBaseDataInstance.getReference().child("SimpleData").orderByChild("FanCount"). startAt(0).endAt(LOAD_MAIN_COUNT );
        else
            data = fierBaseDataInstance.getReference().child("SimpleData").orderByChild("Date").startAt(Integer.toString(nStartDate)).endAt(Integer.toString(nTodayDate))
                    .limitToFirst(lastVisibleCount + LOAD_MAIN_COUNT);

        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    SimpleUserData cTempData = new SimpleUserData();
                    cTempData = postSnapshot.getValue(SimpleUserData.class);
                    if(cTempData != null) {
                        if (!cTempData.Idx.equals(mMyData.getUserIdx())) {
                            if (cTempData.Img == null)
                                cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                            boolean bExist = false;

                            for (int j = 0; j < mMyData.arrUserAll_New.size(); j++) {
                                if (mMyData.arrUserAll_New.get(j).Idx.equals(cTempData.Idx)) {
                                    bExist = true;
                                    break;
                                }
                            }

                            if (bExist == false) {
                                mMyData.arrUserAll_New.add(cTempData);
                                if (mMyData.arrUserAll_New.get(i).Gender.equals("여자")) {
                                    mMyData.arrUserWoman_New.add(mMyData.arrUserAll_New.get(i));
                                } else {
                                    mMyData.arrUserMan_New.add(mMyData.arrUserAll_New.get(i));
                                }

                                mMyData.arrUserAll_New_Age = mMyData.SortData_Age(mMyData.arrUserAll_New, mMyData.nStartAge, mMyData.nEndAge);
                                mMyData.arrUserWoman_New_Age = mMyData.SortData_Age(mMyData.arrUserWoman_New, mMyData.nStartAge, mMyData.nEndAge);
                                mMyData.arrUserMan_New_Age = mMyData.SortData_Age(mMyData.arrUserMan_New, mMyData.nStartAge, mMyData.nEndAge);
                            }
                            i++;
                        }
                    }


                }

                UpdateNewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void SaveLastAdsTime(long time) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.DATE_FORMAT);

        DatabaseReference myTable = database.getReference("User").child(MyData.getInstance().getUserIdx()).child("LastAdsTime");
        myTable.setValue(Long.valueOf(time));
    }
}