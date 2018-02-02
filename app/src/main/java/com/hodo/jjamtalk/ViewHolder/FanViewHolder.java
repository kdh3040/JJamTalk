package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class FanViewHolder extends RecyclerView.ViewHolder{


    public TextView nickname,giftranking,giftCount;
    public ImageView imageView;
    public ImageView imageGrade, imageBest;
    public LinearLayout linearLayout;



    public FanViewHolder(View itemView) {

        super(itemView);
        linearLayout = itemView.findViewById(R.id.layout_fan);
        nickname = itemView.findViewById(R.id.tv_nickname);
        giftranking = itemView.findViewById(R.id.tv_gift_ranking);
        giftCount = itemView.findViewById(R.id.tv_gift_count);
        imageView = itemView.findViewById(R.id.iv_fan);
        imageGrade = itemView.findViewById(R.id.iv_grade);
        imageBest = itemView.findViewById(R.id.iv_item);



    }
}
