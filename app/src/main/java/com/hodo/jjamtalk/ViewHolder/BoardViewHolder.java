package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardViewHolder extends RecyclerView.ViewHolder {

    public TextView idTextView,messageTextView, likeCount, replyCount;
    public ImageView imageView;

    public BoardViewHolder(View itemView) {
        super(itemView);
        idTextView = (TextView)itemView.findViewById(R.id.tv_board_id);
        messageTextView = (TextView)itemView.findViewById(R.id.tv_board_msg);
        imageView = (ImageView)itemView.findViewById(R.id.iv_board);
        likeCount = (TextView)itemView.findViewById(R.id.tv_board_like_count);
        replyCount = (TextView)itemView.findViewById(R.id.tv_board_reply_count);
    }
}
