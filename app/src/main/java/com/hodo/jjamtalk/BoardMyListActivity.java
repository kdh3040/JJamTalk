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
import com.hodo.jjamtalk.Util.RecyclerItemClickListener;
import com.hodo.jjamtalk.ViewHolder.BoardViewHolder;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardMyListActivity extends AppCompatActivity {
    private MyData mMyData = MyData.getInstance();
    private BoardData mBoardInstanceData = BoardData.getInstance();

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
                            final AlertDialog.Builder builder= new AlertDialog.Builder(mActivity);
                            builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    BoardMsgClientData data =  mBoardInstanceData.MyBoardList.get(position);
                                    mBoardInstanceData.RemoveMyBoard(data.GetDBData().BoardIdx);
                                    FirebaseData.getInstance().RemoveBoard(data.GetDBData().BoardIdx);
                                    // 게시판 갱신이 필요

                                    FirebaseData.getInstance().GetInitBoardData();
                                    FirebaseData.getInstance().GetInitMyBoardData();
                                    Intent intent = new Intent(BoardMyListActivity.this, MainActivity.class);
                                    intent.putExtra("StartFragment", 4);
                                    startActivity(intent);
                                    finish();

                                }
                            }).
                                    setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    }).setMessage("작성한 글을 제거하시겠습니까?");
                            AlertDialog dialog = builder.create();
                            dialog.show();
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
