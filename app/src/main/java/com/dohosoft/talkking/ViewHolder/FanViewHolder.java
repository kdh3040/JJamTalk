package com.dohosoft.talkking.ViewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dohosoft.talkking.R;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class FanViewHolder extends RecyclerView.ViewHolder{


    public TextView nickname,giftranking,giftCount;
    public ImageView imageView;
    public ImageView imageGrade, imageBest;
    public ConstraintLayout constraintLayout;



    public FanViewHolder(View itemView) {

        super(itemView);
        constraintLayout = itemView.findViewById(R.id.layout_fan);
        nickname = itemView.findViewById(R.id.tv_nickname);
        giftranking = itemView.findViewById(R.id.tv_gift_ranking);
        giftCount = itemView.findViewById(R.id.tv_gift_count);
        imageView = itemView.findViewById(R.id.iv_fan);
        imageGrade = itemView.findViewById(R.id.iv_grade);
        imageBest = itemView.findViewById(R.id.iv_item);



    }
}
