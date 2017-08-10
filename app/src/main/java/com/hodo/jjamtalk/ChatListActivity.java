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

import com.hodo.jjamtalk.ViewHolder.ChatListViewHolder;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatListActivity extends AppCompatActivity {
    RecyclerView chatListRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        chatListRecyclerView = (RecyclerView)findViewById(R.id.chat_list_recy);

        chatListRecyclerView.setAdapter(new ChatListAdapter());
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this,1,false));

    }

    private class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {
        @Override
        public ChatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_chat_list,parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),ChatRoomActivity.class));
                }
            });

            return new ChatListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatListViewHolder holder, int position) {
            holder.textView.setText("안녕하세요");
            holder.imageView.setImageResource(R.mipmap.girl1);

        }

        @Override
        public int getItemCount() {
            return 30;
        }
    }


}
