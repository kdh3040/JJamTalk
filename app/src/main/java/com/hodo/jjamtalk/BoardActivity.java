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

import com.hodo.jjamtalk.ViewHolder.BoardViewHolder;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        recyclerView = (RecyclerView)findViewById(R.id.board_recy);
        recyclerView.setAdapter(new BoardAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class BoardAdapter extends RecyclerView.Adapter<BoardViewHolder> {
        @Override
        public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_board,parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),BoardItemActivity.class));
                }
            });
            return new BoardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BoardViewHolder holder, int position) {
            holder.idTextView.setText("호근 ,37, 20km");
            holder.messageTextView.setText("날씨가 무덥네요");
            holder.imageView.setImageResource(R.drawable.bg1);
        }

        @Override
        public int getItemCount() {
            return 15;
        }
    }
}
