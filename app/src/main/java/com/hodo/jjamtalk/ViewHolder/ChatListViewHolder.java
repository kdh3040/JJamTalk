package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatListViewHolder extends RecyclerView.ViewHolder {

    public  ImageView imageView;
    public TextView textView;

    public ChatListViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_chat_list);
        textView = itemView.findViewById(R.id.tv_chat_list);

    }
}
