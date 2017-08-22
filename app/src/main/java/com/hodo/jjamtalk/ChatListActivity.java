package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SendData;
import com.hodo.jjamtalk.ViewHolder.ChatListViewHolder;

import java.util.ArrayList;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatListActivity extends AppCompatActivity {
    RecyclerView chatListRecyclerView;
    private MyData mMyData = MyData.getInstance();
    private ArrayList<String> arrChatNameData = new ArrayList<>();
    private ArrayList<SendData> arrChatData = new ArrayList<>();
    Menu mMenu;

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


            return new ChatListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatListViewHolder holder, final int position) {
            int i = position;

            holder.imageView.setImageResource(R.mipmap.girl1);



            arrChatNameData.add(mMyData.arrSendDataList.get(i).strSendName);
            arrChatData.add(mMyData.arrSendDataList.get(i));
            holder.textView.setText(mMyData.arrSendDataList.get(i).strTargetNick + "님과의 채팅방입니다");
            holder.nickname.setText(mMyData.arrSendDataList.get(i).strTargetNick);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String strCharName = arrChatNameData.get(position);
                    SendData mSendData = arrChatData.get(position);

                    Intent intent = new Intent(getApplicationContext(),ChatRoomActivity.class);
                    intent.putExtra("ChatData", mSendData);
                    startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mMyData.arrSendNameList.size();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_list,menu);

        //View v = menu.findItem(R.id.tv_leave).getActionView();





        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch(curId){
            case R.id.clickAll:







                //setDateVisiblity(false);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
