package com.dohosoft.talkking.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dohosoft.talkking.R;

/**
 * Created by mjk on 2017-09-21.
 */

public class MyJewelViewHolder extends RecyclerView.ViewHolder {
    public ImageView iv;
    public TextView tv;
    public LinearLayout linearLayout;

    public MyJewelViewHolder(View view) {
        super(view);
        linearLayout = view.findViewById(R.id.ll);
        iv= view.findViewById(R.id.iv_jewel);
        tv=view.findViewById(R.id.tv_count);

    }
}
