package com.dohosoft.talkking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.dohosoft.talkking.Data.BoardData;
import com.dohosoft.talkking.Data.BoardMsgClientData;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.FanData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.RecyclerItemClickListener;
import com.dohosoft.talkking.ViewHolder.BoardViewHolder;

import java.util.LinkedHashMap;

import static com.dohosoft.talkking.Data.CoomonValueData.REPORT_BOARD_DELETE;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardFragment extends Fragment {
    // 현재 내 데이터
    private MyData mMyData = MyData.getInstance();
    // 파이어베이스 인스턴스
    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    // 보드 인스턴트 데이터
    private BoardData mBoardInstanceData = BoardData.getInstance();
    // UI 인스턴트 데이터
    private UIData mUIData = UIData.getInstance();
    // FragmentView 데이터
    private View mFragmentView = null;
    // 보드 리스트 아답터
    BoardListAdapter BoradListAdapter = new BoardListAdapter();

    // 보드 글쓴이 데이터
    private UserData _BoardWriterData = new UserData();
    // 보드 리스트 UI
    RecyclerView BoardSlotListRecycler;
    Button WriteButton, MyWriteListButton;

    private AdView mAdView;


    public enum BOARD_SCROLL_STATE_TYPE {
        NONE,
        TOP,
        CENTER,
        BOTTOM
    }

    private void refreshFragMent() {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.detach(this).attach(this).commit();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if (CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND || CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

            mMyData.SetCurFrag(4);
        }
    }


    public class BoardListAdapter extends RecyclerView.Adapter<BoardViewHolder> {
        public BOARD_SCROLL_STATE_TYPE BoardScrollState = BOARD_SCROLL_STATE_TYPE.NONE;
        public Boolean BoardDataLoding = false;
        private BoardViewHolder ViewHolder = null;

        @Override
        public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.content_board, parent, false);

            return new BoardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BoardViewHolder holder, final int position) {
            BoardMsgClientData BoardData = mBoardInstanceData.BoardList.get(position);
            holder.SetBoardViewHolder(getContext(), BoardData, BoardData.GetDBData().Idx.equals(mMyData.getUserIdx()), false);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.board_writer:
                        case R.id.board_msg:
                        case R.id.board_write_date:
                        case R.id.board_thumnail:
                            if (CommonFunc.getInstance().getClickStatus() == false)
                                getBoardWriterData(position);
                            break;

                        case R.id.board_report: {

                            CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                                public void YesListener() {
                                    BoardMsgClientData data = mBoardInstanceData.BoardList.get(position);
                                    mBoardInstanceData.RemoveBoard(data.GetDBData().BoardIdx);
                                    if (data.ReportList.size() >= REPORT_BOARD_DELETE)
                                        FirebaseData.getInstance().RemoveBoard(data.GetDBData().BoardIdx);
                                    else
                                        FirebaseData.getInstance().ReportBoard(data.GetDBData().BoardIdx, mMyData.getUserIdx());

                                    refreshFragMent();
                                }
                            };

                            CommonFunc.getInstance().ShowDefaultPopup(getContext(), listener, null, "신고", "신고하시겠습니까?", "예", "아니요");
                            break;
                        }
                    }
                }
            };

            holder.SetBoardHolderListener(listener);
        }

        @Override
        public int getItemCount() {
            return mBoardInstanceData.BoardList.size();
        }

        // 글쓴이 페이지로 이동하는 함수
        public void moveWriterPage(UserData stTargetData) {
            Intent intent = new Intent(getContext(), UserPageActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", stTargetData);
 /*           intent.putExtra("FanList", stTargetData.arrFanList);
            intent.putExtra("FanCount", stTargetData.FanCount);

            intent.putExtra("StarList", stTargetData.arrStarList);*/

            intent.putExtras(bundle);

            startActivity(intent);

            CommonFunc.getInstance().DismissLoadingPage();
            CommonFunc.getInstance().setClickStatus(false);

        }

        // 글쓴이 데이터 받아오는 함수
        public void getBoardWriterData(final int position) {
            CommonFunc.getInstance().setClickStatus(true);
            CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");

            final String strTargetIdx = mBoardInstanceData.BoardList.get(position).GetDBData().Idx;

            if (strTargetIdx.equals(mMyData.getUserIdx())) {
                CommonFunc.getInstance().DismissLoadingPage();
                CommonFunc.getInstance().setClickStatus(false);
                CommonFunc.getInstance().ShowToast(getContext(), "글쓴이가 본인 입니다", false);
            } else {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference table = null;

                String tempGender = mMyData.mapGenderData.get(strTargetIdx);
                if (tempGender == null || tempGender.equals("")) {

                    table = database.getReference("GenderList");
                    table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String tempValue = dataSnapshot.getValue(String.class);
                            mMyData.mapGenderData.put(strTargetIdx, tempValue);

                            DatabaseReference table = null;
                            if (tempValue.equals("여자")) {
                                table = database.getReference("Users").child("Woman");
                            } else {
                                table = database.getReference("Users").child("Man");
                            }

                            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int saa = 0;
                                    UserData tempUserData = dataSnapshot.getValue(UserData.class);
                                    if (tempUserData != null) {

                                        if (CommonFunc.getInstance().CheckUserData(tempUserData,dataSnapshot.getKey())) {

                                            _BoardWriterData = tempUserData;

                     /*   for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                            _BoardWriterData.arrStarList.add(entry.getValue());
                        }*/

                                            for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                                                _BoardWriterData.arrFanList.add(entry.getValue());
                                            }

                                            if (_BoardWriterData.arrFanList.size() == 0) {
                                                moveWriterPage(_BoardWriterData);
                                            } else {
                                                CommonFunc.getInstance().SortByRecvHeart(_BoardWriterData);

                                                for (int i = 0; i < _BoardWriterData.arrFanList.size(); i++) {
                                                    Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(_BoardWriterData.arrFanList.get(i).Idx);
                                                    final FanData finalTempFanData = _BoardWriterData.arrFanList.get(i);
                                                    final int finalI = i;
                                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                                                            if (DBData != null) {
                                                                _BoardWriterData.arrFanData.put(finalTempFanData.Idx, DBData);

                                                                if (finalI == _BoardWriterData.arrFanList.size() - 1) {
                                                                    moveWriterPage(_BoardWriterData);
                                                                }
                                                            } else {
                                                                CommonFunc.getInstance().ShowToast(getContext(), "사용자가 없습니다.", false);
                                                                CommonFunc.getInstance().DismissLoadingPage();
                                                                CommonFunc.getInstance().setClickStatus(false);
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }
                                        } else {
                                            CommonFunc.getInstance().ShowToast(getContext(), "사용자가 없습니다.", false);
                                            CommonFunc.getInstance().DismissLoadingPage();
                                            CommonFunc.getInstance().setClickStatus(false);
                                        }
                                    } else {
                                        CommonFunc.getInstance().ShowToast(getContext(), "사용자가 없습니다.", false);
                                        CommonFunc.getInstance().DismissLoadingPage();
                                        CommonFunc.getInstance().setClickStatus(false);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }


                    });

                } else {
                    if (tempGender.equals("여자")) {
                        table = database.getReference("Users").child("Woman");
                    } else {
                        table = database.getReference("Users").child("Man");
                    }

                    table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int saa = 0;
                            UserData tempUserData = dataSnapshot.getValue(UserData.class);
                            if (tempUserData != null) {
                                if (CommonFunc.getInstance().CheckUserData(tempUserData,dataSnapshot.getKey())) {

                                    _BoardWriterData = tempUserData;

                     /*   for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                            _BoardWriterData.arrStarList.add(entry.getValue());
                        }*/

                                    for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                                        _BoardWriterData.arrFanList.add(entry.getValue());
                                    }

                                    if (_BoardWriterData.arrFanList.size() == 0) {
                                        moveWriterPage(_BoardWriterData);
                                    } else {
                                        CommonFunc.getInstance().SortByRecvHeart(_BoardWriterData);

                                        for (int i = 0; i < _BoardWriterData.arrFanList.size(); i++) {
                                            Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(_BoardWriterData.arrFanList.get(i).Idx);
                                            final FanData finalTempFanData = _BoardWriterData.arrFanList.get(i);
                                            final int finalI = i;
                                            data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                                                    if (DBData != null) {
                                                        _BoardWriterData.arrFanData.put(finalTempFanData.Idx, DBData);

                                                        if (finalI == _BoardWriterData.arrFanList.size() - 1) {
                                                            moveWriterPage(_BoardWriterData);
                                                        }
                                                    } else {
                                                        CommonFunc.getInstance().ShowToast(getContext(), "사용자가 없습니다.", false);
                                                        CommonFunc.getInstance().DismissLoadingPage();
                                                        CommonFunc.getInstance().setClickStatus(false);
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }
                                } else {
                                    CommonFunc.getInstance().DismissLoadingPage();
                                    CommonFunc.getInstance().ShowToast(getContext(), "사용자가 없습니다.", false);
                                    CommonFunc.getInstance().setClickStatus(false);
                                }
                            } else {
                                CommonFunc.getInstance().DismissLoadingPage();
                                CommonFunc.getInstance().ShowToast(getContext(), "사용자가 없습니다.", false);
                                CommonFunc.getInstance().setClickStatus(false);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }


            }

        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mFragmentView != null) {
            // Fragment가 다시 새로 만들어 질때 갱신
            //CommonFunc.getInstance().refreshFragMent(this);
            BoradListAdapter.notifyDataSetChanged();
            //refreshFragMent();

            if(mAdView != null)
            {
                CommonFunc.getInstance().ViewAdsBanner(mAdView);
            }

        } else {
            mFragmentView = inflater.inflate(R.layout.fragment_board, container, false);
            BoardSlotListRecycler = (RecyclerView) mFragmentView.findViewById(R.id.board_recy);

            mAdView = mFragmentView.findViewById(R.id.adBottomView);
            CommonFunc.getInstance().ViewAdsBanner(mAdView);

            BoardSlotListRecycler.setAdapter(BoradListAdapter);
            BoardSlotListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            BoradListAdapter.notifyDataSetChanged();
            BoardSlotListRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
                /*@Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                    if (lastVisibleItemPosition == itemTotalCount) {
                        Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    }
                }*/
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {


                    if (!recyclerView.canScrollVertically(-1)) {
                        if (mBoardInstanceData.BoardList.size() <= 0)
                            return;

                        if (BoradListAdapter.BoardDataLoding == false) {
                            CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                            BoradListAdapter.BoardDataLoding = true;
                            BoardMsgClientData lastBoardData = mBoardInstanceData.BoardList.get(0);
                            FirebaseData.getInstance().GetBoardData(BoradListAdapter, mBoardInstanceData.TopBoardIdx, true);
                        }
                    } else if (!recyclerView.canScrollVertically(1)) {
                        if (CoomonValueData.getInstance().BoardPastLoading == false)
                            return;

                        if (mBoardInstanceData.BoardList.size() <= 0)
                            return;

                        if (BoradListAdapter.BoardDataLoding == false) {
                            CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                            BoradListAdapter.BoardDataLoding = true;
                            FirebaseData.getInstance().GetBoardData(BoradListAdapter, mBoardInstanceData.BottomBoardIdx, false);
                        }
                    } else
                        BoradListAdapter.BoardScrollState = BOARD_SCROLL_STATE_TYPE.CENTER;

                }
            });
        }

        if (mBoardInstanceData.BoardList.size() <= 0) {
            if (BoradListAdapter.BoardDataLoding == false) {
                CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                BoradListAdapter.BoardDataLoding = true;
                FirebaseData.getInstance().GetBoardData(BoradListAdapter, mBoardInstanceData.TopBoardIdx, true);
            }
        }


        return mFragmentView;
    }

    private RecyclerView.OnItemTouchListener GetBoradListClickFunc() {
        RecyclerView.OnItemTouchListener returnFunc =
                new RecyclerItemClickListener(getContext(), BoardSlotListRecycler, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), BoardItemActivity.class);
                        BoardMsgClientData boardMsgData = mBoardInstanceData.BoardList.get(position);
                        if (boardMsgData == null)
                            return;

                        // Intent 하면서 변수를 넘겨준다. TODO 환웅 키값을 넘겨주는게 좋지 않을까..
                        intent.putExtra("Target", position);

                        // 조회수 갱신
                        // mFireBaseData.PushBoardViewCount(boardMsgData.GetDBData().Key);

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                });

        return returnFunc;
    }


}
