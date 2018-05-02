package com.dohosoft.talkking.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dohosoft.talkking.R;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class NotiListViewHolder extends RecyclerView.ViewHolder {

    public ImageView Img_Icon;
    public TextView txt_Title;
    public TextView txt_Date;

    public NotiListViewHolder(View itemView) {
        super(itemView);
        Img_Icon = itemView.findViewById(R.id.Noti_Icon);
        txt_Title = itemView.findViewById(R.id.Noti_Title);
        txt_Date = itemView.findViewById(R.id.Noti_Date);
    }
}
