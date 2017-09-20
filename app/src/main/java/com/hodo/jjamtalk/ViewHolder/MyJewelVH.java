package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.R;


/**
 * Created by mjk on 2017-09-20.
 */

public class MyJewelVH extends RecyclerView.ViewHolder {

    public MyJewelVH(View itemView) {
        super(itemView);
        ImageView jewel = itemView.findViewById(R.id.iv_jewel);
        TextView count = itemView.findViewById(R.id.tv_count);

    }
}
