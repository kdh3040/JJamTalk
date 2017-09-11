package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class MailboxViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout linearLayout;
    public ImageView imageView;
    public TextView textView;



    public MailboxViewHolder(View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.layout_mailbox);
        imageView = itemView.findViewById(R.id.iv_my_card);
        textView = itemView.findViewById(R.id.tv_honeycount);

    }
}
