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
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hodo.jjamtalk.Data.ChatData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.PublicRoomChatData;
import com.hodo.jjamtalk.Data.UIData;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mjk on 2017. 9. 11..
 */

public class PublicChatRoomHostActivity extends AppCompatActivity{

    private MyData mMyData = MyData.getInstance();

    ListView listView;
    PublicChatRoomAdapter pcrAdapter;
    Context mContext;
    Activity mActivity = this;
    Button btn_options;
    ImageView iv_host;
    UIData mUIData = UIData.getInstance();
    LinearLayout ll_chat;

    Button btn_send;
    EditText txt_msg;

    RelativeLayout rl_public_chat;


    DatabaseReference mRef;
    FirebaseRecyclerAdapter<PublicRoomChatData, PublicChatRoomHostActivity.PublicHostChatViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;
    SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm");
    RecyclerView recyclerView;

    int RoomLimit,RoomTime;


    class taskThread extends Thread{
        @Override
        public void run() {

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHMM");
            String getTime = sdf.format(date);

            if(mMyData.nPublicRoomTime  - Integer.parseInt(getTime) < 0)
            {
                mMyData.DestroyPRD();
            }
                try {
                    Thread.sleep(500);
                }catch(Exception e) {}
        }
    }


        public static class PublicHostChatViewHolder extends RecyclerView.ViewHolder{

        ImageView image_profile,image_sent;
        TextView message;

        TextView sender;
        TextView time;

        //회색글자 처리 뜸 원인불명
        public PublicHostChatViewHolder(View itemView) {
            super(itemView);
            image_profile = (ImageView)itemView.findViewById(R.id.ChatRoom_Img);
            image_sent = (ImageView)itemView.findViewById(R.id.iv_sent);
            //sender = (TextView)itemView.findViewById(R.id.ChatRoom_nickname);
            message =(TextView)itemView.findViewById(R.id.message);
            time = (TextView)itemView.findViewById(R.id.time);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chat_host);
        mContext = getApplicationContext();

        Thread thread = new taskThread();
        thread.start();

        Intent intent = new Intent(this.getIntent());
        RoomLimit  = intent.getIntExtra("RoomLimit", 1);
        RoomTime  = intent.getIntExtra("RoomTime", 1);

        mRef = FirebaseDatabase.getInstance().getReference().child("PublicRoomData").child(mMyData.getUserIdx()).child(Integer.toString(mMyData.nPublicRoomName));
        txt_msg = (EditText)findViewById(R.id.Public_Host_Chat_etText);
        btn_send = (Button)findViewById(R.id.Public_Host_Chat_btnSend);

        recyclerView = (RecyclerView) findViewById(R.id.public_host_chat_list);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PublicRoomChatData, PublicChatRoomHostActivity.PublicHostChatViewHolder>(
                PublicRoomChatData.class,
                R.layout.content_chat_data,
                PublicChatRoomHostActivity.PublicHostChatViewHolder.class,
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
            protected void populateViewHolder(PublicChatRoomHostActivity.PublicHostChatViewHolder viewHolder, PublicRoomChatData chat_message, int position) {
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


        //View header = getLayoutInflater().inflate(R.layout.header_board_item,null,false);
        //View footer = getLayoutInflater().inflate(R.layout.footer_pcr,null,false);
        rl_public_chat = (RelativeLayout)findViewById(R.id.rl_public_chat);
        LinearLayout.LayoutParams lp_rl_public_chat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/5);
        LinearLayout.LayoutParams lp_ll_chat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/15);
        iv_host = (ImageView)findViewById(R.id.iv_host);
        iv_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지 확대
                startActivity(new Intent(mContext,ImageActivity.class));
            }
        });

        iv_host.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.popup_pcr_host_iv_click,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

                builder.setView(v);
                final AlertDialog dialog = builder.create();
                dialog.show();

                return true;
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
                    PublicRoomChatData chat_Data = new PublicRoomChatData(mMyData.getUserNick(), mMyData.getUserNick(), message, nowTime, null);
                    mRef.push().setValue(chat_Data);
                    txt_msg.setText("");

                }
            }
        });

       // listView_like = (ListView)findViewById(R.id.lv_pcr);
       // pcrAdapter = new PublicChatRoomAdapter(mContext,this);
        //listView_like.addHeaderView(header);
        //listView_like.addFooterView(footer);
       // listView_like.setAdapter(pcrAdapter);




        //pcrAdapter = new PublicChatRoomAdapter(mContext,this);
        //listView_like.setAdapter(pcrAdapter);
        //btn_options = (Button)findViewById(R.id.btn_options);
        /*btn_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View giftView = LayoutInflater.from(mContext).inflate(R.layout.alert_send_gift,null);
                builder.setView(giftView);
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_public_chat,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.banlist:
                startActivity(new Intent(getApplicationContext(),PublicChatOptionMenuBanActivity.class));
                break;
            case R.id.current_user:
                startActivity(new Intent(getApplicationContext(),PublicChatOptionMenuUserListActivity.class));
                break;
            case R.id.finish:
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("토크방 끝내기");
                alertDialogBuilder.setMessage("토크방을 끝내시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //확인 구현
                                mMyData.DestroyPRD();
                                finish();
                            }
                        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
