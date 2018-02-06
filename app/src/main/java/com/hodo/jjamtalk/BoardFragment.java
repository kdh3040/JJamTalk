package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.BoardLikeData;
import com.hodo.jjamtalk.Data.BoardMsgClientData;
import com.hodo.jjamtalk.Data.BoardMsgDBData;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.CommonFunc;
import com.hodo.jjamtalk.Util.RecyclerItemClickListener;
import com.hodo.jjamtalk.ViewHolder.BoardViewHolder;

import java.util.LinkedHashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

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

    private void refreshFragMent()
    {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.detach(this).attach(this).commit();
    }

    public class BoardListAdapter extends RecyclerView.Adapter<BoardViewHolder> {
        public Boolean BoardDataLoding = false;
        private BoardViewHolder ViewHolder = null;
        @Override
        public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.content_board,parent,false);

            return new BoardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BoardViewHolder holder, final int position) {
            BoardMsgClientData BoardData =  mBoardInstanceData.BoardList.get(position);
            holder.SetBoardViewHolder(getContext(), BoardData, BoardData.GetDBData().Idx.equals(mMyData.getUserIdx()), false);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.board_writer:
                        case R.id.board_msg:
                        case R.id.board_write_date:
                        case R.id.board_thumnail:
                        case R.id.board_like_count:
                            getBoardWriterData(position);
                            break;

                        case R.id.board_like_button:
                            BoardMsgClientData BoardData =  mBoardInstanceData.BoardList.get(position);
                            if(BoardData.IsLikeUser(mMyData.getUserIdx()))
                            {
                                BoardData.RemoveLikeData(mMyData.getUserIdx());
                                mFireBaseData.RemoveBoardLikeData(BoardData.GetDBData().BoardIdx, mMyData.getUserIdx());
                                BoardData.LikeCnt--;
                            }
                            else
                            {
                                BoardLikeData sendData = new BoardLikeData();

                                sendData.Idx = mMyData.getUserIdx();
                                sendData.Img = mMyData.getUserImg();

                                BoardData.AddLikeData(sendData);
                                mFireBaseData.SaveBoardLikeData(BoardData.GetDBData().BoardIdx, sendData);
                                BoardData.LikeCnt++;
                            }
                            refreshFragMent();
                            break;
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
        public void moveWriterPage(UserData stTargetData)
        {
            Intent intent = new Intent(getContext(), UserPageActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", stTargetData);
 /*           intent.putExtra("FanList", stTargetData.arrFanList);
            intent.putExtra("FanCount", stTargetData.FanCount);

            intent.putExtra("StarList", stTargetData.arrStarList);*/

            intent.putExtras(bundle);

            startActivity(intent);
        }

        // 글쓴이 데이터 받아오는 함수
        public void getBoardWriterData(final int position) {
            final String strTargetIdx = mBoardInstanceData.BoardList.get(position).GetDBData().Idx;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = null;
            table = database.getReference("User");

            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int saa = 0;
                    UserData tempUserData = dataSnapshot.getValue(UserData.class);
                    if(tempUserData != null)
                    {
                        _BoardWriterData = tempUserData;

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.StarList.entrySet()) {
                            _BoardWriterData.arrStarList.add(entry.getValue());
                        }

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                            _BoardWriterData.arrFanList.add(entry.getValue());
                        }

                        moveWriterPage(_BoardWriterData);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
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
        }
        else
        {
            mFragmentView = inflater.inflate(R.layout.fragment_board,container,false);
            BoardSlotListRecycler = (RecyclerView)mFragmentView.findViewById(R.id.board_recy);
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
                    if(!recyclerView.canScrollVertically(-1)) {
                        /*if(mBoardInstanceData.BoardList.size() <= 0)
                            return;
                        // Log.d("gggg", "최상단");
                        if(BoradListAdapter.BoardDataLoding == false)
                        {
                            BoradListAdapter.BoardDataLoding = true;
                            BoardMsgClientData lastBoardData = mBoardInstanceData.BoardList.get(0);
                            FirebaseData.getInstance().GetBoardData(BoradListAdapter,lastBoardData.GetDBData().BoardIdx, true);
                        }*/
                    }
                    if(!recyclerView.canScrollVertically(1)) {
                        if(mBoardInstanceData.BoardList.size() <= 0)
                            return;
                        // Log.d("gggg", "최하단");
                        if(BoradListAdapter.BoardDataLoding == false)
                        {
                            BoradListAdapter.BoardDataLoding = true;
                            BoardMsgClientData lastBoardData = mBoardInstanceData.BoardList.get(mBoardInstanceData.BoardList.size() - 1);
                            FirebaseData.getInstance().GetBoardData(BoradListAdapter,lastBoardData.GetDBData().BoardIdx, false);
                        }
                    }
                }
            });




            WriteButton = (Button)mFragmentView.findViewById(R.id.btn_write);
            MyWriteListButton = (Button)mFragmentView.findViewById(R.id.btn_mylist);

            //BoardSlotListRecycler.addOnItemTouchListener(GetBoradListClickFunc());
            WriteButton.setOnClickListener(GetWriteBoradFunc());
            MyWriteListButton.setOnClickListener(GetMyWriteBoradListFunc());
        }
        return mFragmentView;
    }

    private RecyclerView.OnItemTouchListener GetBoradListClickFunc()
    {
        RecyclerView.OnItemTouchListener returnFunc =
        new RecyclerItemClickListener(getContext(), BoardSlotListRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), BoardItemActivity.class);
                BoardMsgClientData boardMsgData =  mBoardInstanceData.BoardList.get(position);
                if(boardMsgData == null)
                    return;

                // Intent 하면서 변수를 넘겨준다. TODO 환웅 키값을 넘겨주는게 좋지 않을까..
                intent.putExtra("Target", position);

                // 조회수 갱신
                // mFireBaseData.PushBoardViewCount(boardMsgData.GetDBData().Key);

                startActivity(intent);
            }
            @Override
            public void onLongItemClick(View view, int position) {}
        });

        return returnFunc;
    }

    private View.OnClickListener GetWriteBoradFunc()
    {
        View.OnClickListener returnFunc = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),BoardWriteActivity.class));
            }
        };

        return returnFunc;
    }

    private View.OnClickListener GetMyWriteBoradListFunc()
    {
        View.OnClickListener returnFunc = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),BoardMyListActivity.class));
            }
        };

        return returnFunc;
    }
}
