package com.dohosoft.talkking.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardReplyPrivateHolder extends RecyclerView.ViewHolder {

    public TextView messageTextView;
    public ImageView imageView;

    public BoardReplyPrivateHolder(View itemView) {
        super(itemView);

        //messageTextView = (TextView)itemView.findViewById(R.id.Reply_txtMemo);
        //imageView = (ImageView)itemView.findViewById(R.id.Reply_ImgProfile);

    }
}
