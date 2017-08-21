package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.TempBoardData;
import com.hodo.jjamtalk.Util.RecyclerItemClickListener;
import com.hodo.jjamtalk.ViewHolder.BoardViewHolder;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardMyListActivity extends AppCompatActivity {
    private MyData mMyData = MyData.getInstance();
    private BoardData mBoardData = BoardData.getInstance();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_mylist);




        recyclerView = (RecyclerView)findViewById(R.id.board_Mylist);
        recyclerView.setAdapter(new BoardMyListActivity.BoardMyListAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), BoardItemActivity.class);
                        intent.putExtra("Target", position);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                    }
                }));

    }


    private class BoardMyListAdapter extends RecyclerView.Adapter<BoardViewHolder> {
        @Override
        public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_board,parent,false);

            return new BoardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BoardViewHolder holder, final int position) {
            //holder.idTextView.setText("호근 ,37, 20km");
            holder.idTextView.setText(mBoardData.arrBoardMyList.get(position).NickName + ", " + mBoardData.arrBoardMyList.get(position).Age);// + ", " +  mBoardData.arrBoardList.get(position).Dist);
            holder.messageTextView.setText(mBoardData.arrBoardMyList.get(position).Msg);
            //holder.iv_profile.setImageResource(R.drawable.bg1);
            Glide.with(getApplicationContext())
                    .load(mBoardData.arrBoardMyList.get(position).Img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mBoardData.arrBoardMyList.size();
        }
    }
}
