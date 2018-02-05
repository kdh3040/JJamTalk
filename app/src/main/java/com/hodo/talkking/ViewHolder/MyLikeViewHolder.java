package com.hodo.talkking.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hodo.talkking.R;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class MyLikeViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public TextView tv_nickname,tv_honeycount,tv_rank;
    public LinearLayout linearLayout;


    public MyLikeViewHolder(View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.layout_fan);
        imageView = itemView.findViewById(R.id.iv_my_like);
        tv_nickname = itemView.findViewById(R.id.tv_nickname);
        tv_honeycount =itemView.findViewById(R.id.tv_gift_count);
        tv_rank =itemView.findViewById(R.id.tv_gift_ranking);

    }
}
