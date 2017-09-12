package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Util.RecyclerItemClickListener;
import com.hodo.jjamtalk.ViewHolder.BoardViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardActivity extends AppCompatActivity {

    private BoardData mBoardData = BoardData.getInstance();

    RecyclerView recyclerView;
    Button btn_write,btn_myList;
    UIData mUIData = UIData.getInstance();

    int nPosition;
    LinearLayout contentlayout;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        recyclerView = (RecyclerView)findViewById(R.id.board_recy);
        recyclerView.setAdapter(new BoardAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        btn_write = (Button)findViewById(R.id.btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BoardWriteActivity.class));
            }
        });
        btn_myList = (Button)findViewById(R.id.btn_mylist);
        btn_myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BoardMyListActivity.class));
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class BoardAdapter extends RecyclerView.Adapter<BoardViewHolder> {
        @Override
        public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_board,parent,false);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/7));

            return new BoardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BoardViewHolder holder, final int position) {
            //holder.idTextView.setText("호근 ,37, 20km");
            holder.idTextView.setText(mBoardData.arrBoardList.get(position).NickName + ", " + mBoardData.arrBoardList.get(position).Age);// + ", " +  mBoardData.arrBoardList.get(position).Dist);
            holder.messageTextView.setText(mBoardData.arrBoardList.get(position).Msg);
            //holder.iv_profile.setImageResource(R.drawable.bg1);
            Glide.with(getApplicationContext())
                    .load(mBoardData.arrBoardList.get(position).Img)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mBoardData.arrBoardList.size();
        }
    }
}
