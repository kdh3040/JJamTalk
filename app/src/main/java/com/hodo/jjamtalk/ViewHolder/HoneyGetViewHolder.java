package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class HoneyGetViewHolder extends RecyclerView.ViewHolder {

    public ImageView iv_profile;
    public TextView tv_nickname,tv_honeycount, tv_date;

    public HoneyGetViewHolder(View itemView) {
        super(itemView);
        iv_profile = itemView.findViewById(R.id.iv_profile);
        tv_nickname = itemView.findViewById(R.id.tv_nickname);
        tv_honeycount = itemView.findViewById(R.id.tv_honeycount);
        tv_date = itemView.findViewById(R.id.tv_date);
    }
}
