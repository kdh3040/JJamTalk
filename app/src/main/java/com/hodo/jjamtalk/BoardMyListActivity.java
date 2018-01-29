package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.BoardLikeData;
import com.hodo.jjamtalk.Data.BoardMsgClientData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.CommonFunc;
import com.hodo.jjamtalk.Util.RecyclerItemClickListener;
import com.hodo.jjamtalk.ViewHolder.BoardViewHolder;

import static com.hodo.jjamtalk.Data.CoomonValueData.MAIN_ACTIVITY_BOARD;
import static com.hodo.jjamtalk.Data.CoomonValueData.MAIN_ACTIVITY_CHAT;
import static com.hodo.jjamtalk.Data.CoomonValueData.REPORT_BOARD_DELETE;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardMyListActivity extends AppCompatActivity {
    private MyData mMyData = MyData.getInstance();
    private BoardData mBoardInstanceData = BoardData.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    Activity mActivity;
    RecyclerView MyBoardSlotListRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_board_mylist);

        MyBoardSlotListRecycler = (RecyclerView)findViewById(R.id.board_Mylist);
        MyBoardSlotListRecycler.setAdapter(new BoardMyListActivity.BoardMyListAdapter());
        MyBoardSlotListRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    private class BoardMyListAdapter extends RecyclerView.Adapter<BoardViewHolder> {
        @Override
        public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_board,parent,false);

            return new BoardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final BoardViewHolder holder, final int position) {
            holder.SetBoardViewHolder(getApplicationContext(), mBoardInstanceData.MyBoardList.get(position), true, true);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.board_delete:
                        {
                            View.OnClickListener yesListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BoardMsgClientData data =  mBoardInstanceData.MyBoardList.get(position);
                                    mBoardInstanceData.RemoveBoard(data.GetDBData().BoardIdx);
                                    FirebaseData.getInstance().RemoveBoard(data.GetDBData().BoardIdx);

                                    FirebaseData.getInstance().GetInitBoardData();
                                    FirebaseData.getInstance().GetInitMyBoardData();

                                    mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_BOARD);
                                }
                            };

                            CommonFunc.getInstance().ShowDefaultPopup(mActivity, yesListener, "삭제", "작성한 글을 제거하시겠습니까?", "예", "아니요");
                        }
                            break;
                        default:
                            break;
                    }
                }
            };

            holder.SetBoardHolderListener(listener);
        }

        @Override
        public int getItemCount() {
            return mBoardInstanceData.MyBoardList.size();
        }
    }
}
