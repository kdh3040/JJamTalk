package com.hodo.jjamtalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SendData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.ViewHolder.ChatListViewHolder;

import java.util.ArrayList;

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

    public ChatListFragment() {

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






            //LinearLayout.LayoutParams lpForLL = new LinearLayout.LayoutParams((int)(mUIData.getWidth()*0.6),(int)(mUIData.getWidth()*0.2));
            //holder.imageView.setLayoutParams(lp);
            //holder.linearLayout.setLayoutParams(lp);

            //holder.ll_text.setLayoutParams(lpForLL);

            /*GradientDrawable drawable = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                drawable = (GradientDrawable)getDrawable(R.drawable.background_rounding);
            }*/

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.imageView.setBackground(new ShapeDrawable(new OvalShape()));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.imageView.setClipToOutline(true);
            }*/
            //holder.imageView.setImageResource(R.mipmap.girl1);

            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(mMyData.arrChatTargetData.get(position).Img)
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
                    /*
                    AlertDialog.Builder br = new AlertDialog.Builder(getActivity());
                    br.setTitle("채팅방 삭제");
                    br.setMessage("삭제를 하면 대화 내용 및 채팅방 정보가 모두 삭제됩니다.");
                    br.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 취소 구현
                        }
                    }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //확인 구현
                        }
                    });
                    AlertDialog dialog = br.create();
                    dialog.show();*/
                    return false;
                }
            });




/*            arrChatNameData.add(mMyData.arrSendDataList.get(i).strSendName);
            arrChatData.add(mMyData.arrSendDataList.get(i));*/
            String strDate = mMyData.arrSendDataList.get(i).strSendDate;

            holder.date.setText(strDate);
            //holder.textView.setText(mMyData.arrSendDataList.get(i).strTargetNick + "님과의 채팅방입니다");
            //holder.nickname.setText(mMyData.arrSendDataList.get(i).strTargetNick);

            holder.textView.setText(mMyData.arrChatTargetData.get(i).NickName + "님과의 채팅방입니다");
            holder.nickname.setText(mMyData.arrChatTargetData.get(i).NickName);


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //String strCharName = arrChatNameData.get(position);
                    //SendData mSendData = arrChatData.get(position);
                    SendData mSendData = mMyData.arrSendDataList.get(position);
                    int idx = position;
                    Intent intent = new Intent(getContext(),ChatRoomActivity.class);
                    intent.putExtra("ChatData", mSendData);
                    intent.putExtra("ChatIdx", idx);
                    startActivity(intent);
                    //finish();

                }
            });
        }

        @Override
        public int getItemCount() {
            //return mMyData.arrSendNameList.size();
            return mMyData.arrChatTargetData.size();
        }
    }

/*
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
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
*/
}
