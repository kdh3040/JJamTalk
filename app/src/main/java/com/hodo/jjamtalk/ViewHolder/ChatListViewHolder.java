package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatListViewHolder extends RecyclerView.ViewHolder {

    public  ImageView imageView;
    public TextView textView,nickname;
    public LinearLayout linearLayout;

    public ChatListViewHolder(View itemView) {
        super(itemView);
        linearLayout = (LinearLayout)itemView.findViewById(R.id.layout_chat_list);
        nickname = itemView.findViewById(R.id.tv_nickname);
        imageView = itemView.findViewById(R.id.iv_chat_list);
        textView = itemView.findViewById(R.id.tv_chat_list);

    }
}
