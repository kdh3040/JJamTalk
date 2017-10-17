package com.hodo.jjamtalk;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.PublicRoomChatData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by mjk on 2017. 9. 12..
 */

public class PublicChatRoomActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private UserData stTargetData;
    RelativeLayout rl_public_chat;
    UIData mUIData = UIData.getInstance();
    ImageView iv_host;
    LinearLayout ll_chat;
    ListView listView;
    ImageButton ib_gift;
    PublicChatRoomAdapter pcrAdapter;
    AlertDialog.Builder builder;

    Button btn_send;
    EditText txt_msg;

    DatabaseReference mRef;
    FirebaseRecyclerAdapter<PublicRoomChatData, PublicChatRoomActivity.PublicChatViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;
    SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm");
    RecyclerView recyclerView;



    public static class PublicChatViewHolder extends RecyclerView.ViewHolder{

        ImageView image_profile,image_sent;
        TextView message;

        TextView sender;
        TextView time;

        //회색글자 처리 뜸 원인불명
        public PublicChatViewHolder(View itemView) {
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
        setContentView(R.layout.activity_public_chat);

        Bundle bundle = getIntent().getExtras();
        stTargetData = (UserData) bundle.getSerializable("Target");

        mRef = FirebaseDatabase.getInstance().getReference().child("PublicRoomData").child(stTargetData.Idx).child(Integer.toString(stTargetData.PublicRoomName));
        txt_msg = (EditText)findViewById(R.id.Public_Chat_etText);
        btn_send = (Button)findViewById(R.id.Public_Chat_btnSend);

        recyclerView = (RecyclerView) findViewById(R.id.public_chat_list);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PublicRoomChatData, PublicChatRoomActivity.PublicChatViewHolder>(
                PublicRoomChatData.class,
                R.layout.content_chat_data,
                PublicChatRoomActivity.PublicChatViewHolder.class,
                mRef){

            @Override
            protected PublicRoomChatData parseSnapshot(DataSnapshot snapshot) {

                PublicRoomChatData chat_message = super.parseSnapshot(snapshot);
                if(chat_message != null){
                    chat_message.setId(snapshot.getKey());
                }
                return chat_message;
            }
            @Override
            protected void populateViewHolder(PublicChatRoomActivity.PublicChatViewHolder viewHolder, PublicRoomChatData chat_message, int position) {
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

        rl_public_chat = (RelativeLayout)findViewById(R.id.rl_public_chat);
        LinearLayout.LayoutParams lp_rl_public_chat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/5);
        LinearLayout.LayoutParams lp_ll_chat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/15);
        iv_host = (ImageView)findViewById(R.id.iv_host);
        iv_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지 확대
                startActivity(new Intent(getApplicationContext(),ImageActivity.class));
            }
        });

        builder = new AlertDialog.Builder(this);

        ib_gift = (ImageButton)findViewById(R.id.ib_gift);

        ib_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_send_gift,null);


                builder.setView(v);
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        rl_public_chat.setLayoutParams(lp_rl_public_chat);

        ll_chat = (LinearLayout)findViewById(R.id.ll_chat);
        ll_chat.setLayoutParams(lp_ll_chat);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = txt_msg.getText().toString();
                long nowTime =System.currentTimeMillis();
                if(txt_msg.getText() == null){
                    return;
                }else{
                    PublicRoomChatData chat_Data = new PublicRoomChatData(mMyData.getUserNick(), stTargetData.NickName, message, nowTime, null);
                    mRef.push().setValue(chat_Data);
                    txt_msg.setText("");

                }
            }
        });

 /*       listView = (ListView)findViewById(R.id.lv_pcr);
        pcrAdapter = new PublicChatRoomAdapter(getApplicationContext(),this);
        //listView.addHeaderView(header);
        //listView.addFooterView(footer);
        listView.setAdapter(pcrAdapter);
*/
    }
}
