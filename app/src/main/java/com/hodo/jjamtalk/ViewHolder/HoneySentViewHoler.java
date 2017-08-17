package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class HoneySentViewHoler extends RecyclerView.ViewHolder {

    public TextView tv_Nickname,tv_Honeycount;

    public HoneySentViewHoler(View itemView) {
        super(itemView);
        tv_Nickname = itemView.findViewById(R.id.tv_nickname);
        tv_Honeycount = itemView.findViewById(R.id.tv_honeycount);

    }
}
