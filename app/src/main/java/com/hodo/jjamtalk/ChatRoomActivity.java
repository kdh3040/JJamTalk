package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hodo.jjamtalk.Data.ChatData;
import com.hodo.jjamtalk.Data.MyData;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatRoomActivity extends AppCompatActivity {

    private Button btnSend, btnOption;
    private EditText txtMsg;

    private MyData mMyData = MyData.getInstance();
    DatabaseReference mRef;


    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        ImageView image_profile,image_sent;
        TextView message;

        TextView sender;
        TextView time;

        //회색글자 처리 뜸 원인불명
        public ChatViewHolder(View itemView) {
            super(itemView);
            image_profile = (ImageView)itemView.findViewById(R.id.imageView);
           // image_sent = (ImageView)itemView.findViewById(R.id.iv_sent);
          //  sender = (TextView)itemView.findViewById(R.id.nickname);
          //  message =(TextView)itemView.findViewById(R.id.message);

            time = (TextView)itemView.findViewById(R.id.time);

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        btnSend = (Button)findViewById(R.id.Chat_btnSend);
        btnOption = (Button)findViewById(R.id.Chat_btnOption);
        txtMsg = (EditText) findViewById(R.id.Chat_txtMsg);
       // recyclerView = (RecyclerView) findViewById(R.id.chatroom_RecylerView);

        Intent intent = getIntent();
        String strRoomName =  intent.getStringExtra("ChatName");

        mRef = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(strRoomName);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = txtMsg.getText().toString();
                long nowTime =System.currentTimeMillis();
                if(message == null){
                    return;
                }else{
                    ChatData chat_data = new ChatData(mMyData.getUserNick(), "Target", message, nowTime, null);
                    mRef.push().setValue(chat_data);
                    txtMsg.setText("");

                }
            }
        });

    }
}
