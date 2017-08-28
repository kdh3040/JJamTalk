package com.hodo.jjamtalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SendData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.ViewHolder.ChatListViewHolder;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatListActivity extends AppCompatActivity {
    RecyclerView chatListRecyclerView;
    private MyData mMyData = MyData.getInstance();
    private ArrayList<String> arrChatNameData = new ArrayList<>();
    private ArrayList<SendData> arrChatData = new ArrayList<>();
    Menu mMenu;
    Context mContext;
    UIData mUIData = UIData.getInstance();
    LinearLayout layout_chatlist;

    ChatListAdapter mAdapter = new ChatListAdapter();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        mContext = this;
        chatListRecyclerView = (RecyclerView)findViewById(R.id.chat_list_recy);

        chatListRecyclerView.setAdapter(mAdapter);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this,1,false));
        mAdapter.notifyDataSetChanged();

    }



    private class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {
        @Override
        public ChatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_chat_list,parent,false);
            int width = mUIData.getWidth()/4;
            Toast.makeText(getApplicationContext(),"width:"+width,Toast.LENGTH_LONG).show();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,width);
            view.setLayoutParams(lp);


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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.imageView.setBackground(new ShapeDrawable(new OvalShape()));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.imageView.setClipToOutline(true);
            }
            //holder.imageView.setImageResource(R.mipmap.girl1);

            Glide.with(mContext)
                    .load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .bitmapTransform(new CropCircleTransformation(ChatListActivity.this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.imageView);


            holder.linearLayout.setLongClickable(true);

            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder br = new AlertDialog.Builder(mContext);
                    br.setTitle("채팅방에서 나가시겠습니까?");
                    br.setMessage("나가기를 하면 대화 내용 및 채팅방 정보가 모두 삭제됩니다.");
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
                    dialog.show();
                    return false;
                }
            });




            arrChatNameData.add(mMyData.arrSendDataList.get(i).strSendName);
            arrChatData.add(mMyData.arrSendDataList.get(i));
            holder.textView.setText(mMyData.arrSendDataList.get(i).strTargetNick + "님과의 채팅방입니다");
            holder.nickname.setText(mMyData.arrSendDataList.get(i).strTargetNick);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String strCharName = arrChatNameData.get(position);
                    SendData mSendData = arrChatData.get(position);

                    Intent intent = new Intent(getApplicationContext(),ChatRoomActivity.class);
                    intent.putExtra("ChatData", mSendData);
                    startActivity(intent);
                    //finish();

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
