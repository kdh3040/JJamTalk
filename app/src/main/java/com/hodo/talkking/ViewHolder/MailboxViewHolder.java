package com.hodo.talkking.ViewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hodo.talkking.R;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class MailboxViewHolder extends RecyclerView.ViewHolder{

    public ConstraintLayout constraintLayout;
    public ImageView imageView,bg;
    public TextView textView;
    public TextView SendDate;
    public LinearLayout layout;



    public MailboxViewHolder(View itemView) {
        super(itemView);
        bg=itemView.findViewById(R.id.bg);
        constraintLayout = itemView.findViewById(R.id.layout_mailbox);
        imageView = itemView.findViewById(R.id.iv_my_card);
        textView = itemView.findViewById(R.id.tv_honeycount);
        SendDate = itemView.findViewById(R.id.tv_senddate);

    }
}
