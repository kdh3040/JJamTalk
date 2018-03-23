package com.dohosoft.talkking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017-12-19.
 */

public class LikeViewHolder extends RecyclerView.ViewHolder{

    ImageView iv_image;
    public LikeViewHolder(View itemView) {
        super(itemView);

        iv_image = itemView.findViewById(R.id.iv_image);
    }
}
