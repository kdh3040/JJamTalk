package com.hodo.talkking.ViewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.talkking.R;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatListViewHolder extends RecyclerView.ViewHolder {

    public  ImageView imageView, check;
    public  ImageView imageGrade, imageItem;
    public TextView textView,nickname, date;
    public ConstraintLayout linearLayout;

    public ChatListViewHolder(View itemView) {
        super(itemView);

        linearLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout);



        nickname = itemView.findViewById(R.id.tv_nickname);

        imageView = itemView.findViewById(R.id.iv_profile);
        imageGrade = itemView.findViewById(R.id.iv_grade);
        imageItem = itemView.findViewById(R.id.iv_item);

        textView = itemView.findViewById(R.id.tv_content);
        date = itemView.findViewById(R.id.tv_date);
        check = itemView.findViewById(R.id.tv_check);
    }
}
