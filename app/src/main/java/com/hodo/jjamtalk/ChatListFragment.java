package com.hodo.jjamtalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SendData;
import com.hodo.jjamtalk.Data.SimpleUserData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.ViewHolder.ChatListViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatListFragment extends Fragment {
    RecyclerView chatListRecyclerView;
    private MyData mMyData = MyData.getInstance();
    //private ArrayList<String> arrChatNameData = new ArrayList<>();
    //private ArrayList<SendData> arrChatData = new ArrayList<>();
    Menu mMenu;
    Context mTempContext;
    Context mContext;
    UIData mUIData = UIData.getInstance();
    LinearLayout layout_chatlist;

    View fragView;

    private void refreshFragMent()
    {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.detach(this).attach(this).commit();
    }

    ChatListAdapter mAdapter = new ChatListAdapter();

    public ChatListFragment(Context Context) {
        mTempContext = Context;
    }

    /*
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            chatListRecyclerView = (RecyclerView)findViewById(R.id.chat_list_recy);

            chatListRecyclerView.setAdapter(mAdapter);
            chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter.notifyDataSetChanged();

        }
    */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();


        if (fragView!= null) {
            mAdapter.notifyDataSetChanged();
        }
        else
        {
            fragView = inflater.inflate(R.layout.fragment_chat_list,container,false);
            fragView.setTag("ChatListFragment");
            chatListRecyclerView = fragView.findViewById(R.id.chat_list_recy);

            chatListRecyclerView.setAdapter(mAdapter);
            chatListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter.notifyDataSetChanged();
        }

        return fragView;
    }
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

    }*/


    private class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {
        @Override
        public ChatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.content_chat_list,parent,false);

            view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/7));
            return new ChatListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatListViewHolder holder, final int position) {
            int i = position;


            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.imageView);


            holder.linearLayout.setLongClickable(true);

            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_exit_app, null, false);


                    final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(v).create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    final Button btn_exit;
                    final Button btn_no;
                    final TextView tv_title;
                    final TextView tv_msg;

                    tv_title = v.findViewById(R.id.title);
                    tv_msg = v.findViewById(R.id.msg);

                    tv_title.setText("채팅방 삭제");
                    tv_msg.setText("삭제를 하면 대화 내용 및 채팅방 정보가 모두 삭제됩니다.");

                    btn_exit = (Button) v.findViewById(R.id.btn_yes);
                    btn_exit.setText("삭제");
                    btn_exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference table;
                            table = database.getReference("User/" + mMyData.getUserIdx()+ "/SendList/");
                            table.child(mMyData.arrSendDataList.get(position).strSendName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().removeValue();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            mMyData.arrSendDataList.remove(position);
                            mMyData.arrSendNameList.remove(position);

                         //   mMyData.arrChatTargetData.remove(position);
                         //   mMyData.arrMyChatTargetList.remove(position);

                            refreshFragMent();
                            dialog.dismiss();
                        }
                    });

                    btn_no = (Button) v.findViewById(R.id.btn_no);
                    btn_no.setText("취소");
                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    return false;
                }
            });


/*            String strDate = mMyData.arrSendDataList.get(i).strSendDate;
            holder.date.setText(strDate);*/

            //holder.textView.setText(mMyData.arrSendDataList.get(i).strTargetNick + "님과의 채팅방입니다");

            holder.date.setText(mMyData.arrSendDataList.get(i).strSendDate);
            holder.nickname.setText(mMyData.arrSendDataList.get(i).strTargetNick);

            if(mMyData.arrSendDataList.get(i).strTargetMsg.equals(""))
                holder.textView.setText(mMyData.arrSendDataList.get(i).strTargetNick + "님과의 채팅방입니다");
            else
                holder.textView.setText(mMyData.arrSendDataList.get(i).strTargetMsg);


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getChatTargetData(position);


                    //finish();



                }
            });
        }

        @Override
        public int getItemCount() {
            return mMyData.arrSendDataList.size();
            //return mMyData.arrChatTargetData.size();
        }

        String strTargetIdx;

        public void moveChatPage(int position)
        {
            SendData mSendData = mMyData.arrSendDataList.get(position);
            Intent intent = new Intent(getContext(),ChatRoomActivity.class);
            intent.putExtra("Position", position);
            intent.putExtra("ChatData", mSendData);
            intent.putExtra("ChatIdx", strTargetIdx);

            startActivity(intent);
        }


        public void getChatTargetData(final int position) {

            String[] strIdx = mMyData.arrSendDataList.get(position).strSendName.split("_");

            if(strIdx[0].equals(mMyData.getUserIdx()))
            {
                strTargetIdx = strIdx[1];
            }
            else
            {
                strTargetIdx = strIdx[0];
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = null;
            table = database.getReference("User");

            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int saa = 0;
                    UserData tempUserData = dataSnapshot.getValue(UserData.class);
                    if(tempUserData != null)
                    {
                        mMyData.mapChatTargetData.put(strTargetIdx, tempUserData);

                        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                            mMyData.mapChatTargetData.get(strTargetIdx).arrStarList.add(entry.getValue());
                        }

                        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.FanList.entrySet()) {
                            mMyData.mapChatTargetData.get(strTargetIdx).arrFanList.add(entry.getValue());
                        }

                        RefreshUserChatSimpleData(tempUserData, position);
                        moveChatPage(position);
                        notifyDataSetChanged();
                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }

    }

    public void RefreshUserChatSimpleData(UserData stTargetData, int position) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());

        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(mMyData.getUserIdx()).child("CardList").child(stTargetData.Idx);
        user.child("Age").setValue(stTargetData.Age);
        user.child("FanCount").setValue(stTargetData.FanCount);
        user.child("Gender").setValue(stTargetData.Gender);
        user.child("Idx").setValue(stTargetData.Idx);
        user.child("Img").setValue(stTargetData.Img);
        user.child("Lon").setValue(stTargetData.Lon);
        user.child("Lat").setValue(stTargetData.Lat);
        user.child("Memo").setValue(stTargetData.Memo);
        user.child("NickName").setValue(stTargetData.NickName);

        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성

        user.child("Point").setValue(Integer.valueOf(Integer.toString(rand.nextInt(100))));
        user.child("RecvGold").setValue(stTargetData.RecvCount);
        user.child("SendGold").setValue(stTargetData.SendCount);


        mMyData.arrSendDataList.get(position).strTargetImg = stTargetData.Img;
        mMyData.arrSendDataList.get(position).strTargetNick= stTargetData.NickName;

    }

}
