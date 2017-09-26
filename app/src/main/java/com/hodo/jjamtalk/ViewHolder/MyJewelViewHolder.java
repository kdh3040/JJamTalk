package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017-09-21.
 */

public class MyJewelViewHolder extends RecyclerView.ViewHolder {
    public ImageView iv;
    public TextView tv;

    public MyJewelViewHolder(View view) {
        super(view);
        iv= view.findViewById(R.id.iv_jewel);
        tv=view.findViewById(R.id.tv_count);

    }
}
