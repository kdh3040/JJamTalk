package com.hodo.jjamtalk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hodo.jjamtalk.Data.ChatData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SendData;
import com.hodo.jjamtalk.Data.UserData;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatRoomActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private static final int REQUEST_IMAGE = 1001;
    Button btn_send,btn_plus;
    EditText txt_msg;

    Context context=this;
    DatabaseReference mRef;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<ChatData, ChatViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;
    SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm");
    SendData tempChatData;

    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        ImageView image_profile,image_sent;
        TextView message;

        TextView sender;
        TextView time;

        //회색글자 처리 뜸 원인불명
        public ChatViewHolder(View itemView) {
            super(itemView);
            image_profile = (ImageView)itemView.findViewById(R.id.imageView);
            image_sent = (ImageView)itemView.findViewById(R.id.iv_sent);
            sender = (TextView)itemView.findViewById(R.id.nickname);
            message =(TextView)itemView.findViewById(R.id.message);
            time = (TextView)itemView.findViewById(R.id.time);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent intent = getIntent();

        tempChatData = (SendData) intent.getExtras().getSerializable("ChatData");

        mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(tempChatData.strSendName);

        txt_msg = (EditText)findViewById(R.id.et_msg);


        btn_plus = (Button)findViewById(R.id.btn_plus);

        recyclerView = (RecyclerView) findViewById(R.id.chat_list);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatData, ChatViewHolder>(
                ChatData.class,
                R.layout.content_chat_data,
                ChatViewHolder.class,
                mRef){



            @Override
            protected ChatData parseSnapshot(DataSnapshot snapshot) {

                ChatData chat_message = super.parseSnapshot(snapshot);
                if(chat_message != null){
                    chat_message.setId(snapshot.getKey());
                }
                return chat_message;
            }
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, ChatData chat_message, int position) {
                //Log.d("hngpic","popVH");

                //mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                if( chat_message.getMsg() != null){

                    viewHolder.message.setText(chat_message.getMsg());
                    viewHolder.message.setVisibility(TextView.VISIBLE);
                    viewHolder.image_sent.setVisibility(ImageView.GONE);


                }else{
            /*        Glide.with(getApplicationContext())
                            .load(chat_message.getimage_URL().toString())
                            .into(viewHolder.image_sent);*/
                    viewHolder.image_sent.setVisibility(ImageView.VISIBLE);
                    viewHolder.message.setVisibility(TextView.GONE);


                }
                Date mDate = new Date(chat_message.gettime());
                String date= mFormat.format(mDate);
                viewHolder.sender.setText(chat_message.getFrom()+" "+date);
            }
        };
        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });


        recyclerView.setAdapter(firebaseRecyclerAdapter);


        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                //View framelayout = findViewById(R.id.framelayout_chatroom);
                View popup= inflater.inflate(R.layout.popup_chatroom,null);

                builder.setView(popup);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Button btn_gal = popup.findViewById(R.id.btn_gallery);
                btn_gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_IMAGE);

                    }
                });
            }
        });
        btn_send = (Button)findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = txt_msg.getText().toString();
                long nowTime =System.currentTimeMillis();
                if(txt_msg.getText() == null){
                    return;
                }else{
                    ChatData chat_Data = new ChatData(mMyData.getUserNick(), tempChatData.strTargetNick, message, nowTime, null);
                    mRef.push().setValue(chat_Data);
                    txt_msg.setText("");

                }
            }
        });



    }
}
