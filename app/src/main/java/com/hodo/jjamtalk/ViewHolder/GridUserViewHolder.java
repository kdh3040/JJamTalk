package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class GridUserViewHolder extends RecyclerView.ViewHolder {
    public ImageView iv_profile,iv_honey_rank;
    public TextView textView;
    public GridUserViewHolder(View itemView) {
        super(itemView);
        iv_profile = itemView.findViewById(R.id.iv_user);
        iv_honey_rank = itemView.findViewById(R.id.iv_honey_rank);
        textView = itemView.findViewById(R.id.tv_user);

    }
}
