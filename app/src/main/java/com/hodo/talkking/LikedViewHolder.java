package com.hodo.talkking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017-12-19.
 */

public class LikedViewHolder extends RecyclerView.ViewHolder {

    ImageView iv_liked;

    public LikedViewHolder(View itemView) {
        super(itemView);

       iv_liked =  itemView.findViewById(R.id.iv_image);

    }
}
