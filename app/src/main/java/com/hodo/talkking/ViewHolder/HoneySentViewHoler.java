package com.hodo.talkking.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.talkking.R;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class HoneySentViewHoler extends RecyclerView.ViewHolder {

    public TextView tv_Nickname,tv_Honeycount, tv_Date;
    public ImageView img_Profile;

    public HoneySentViewHoler(View itemView) {
        super(itemView);
        tv_Nickname = itemView.findViewById(R.id.tv_nickname);
        tv_Honeycount = itemView.findViewById(R.id.tv_honeycount);
        tv_Date = itemView.findViewById(R.id.tv_date);
        img_Profile = itemView.findViewById(R.id.iv_profile);

    }
}
