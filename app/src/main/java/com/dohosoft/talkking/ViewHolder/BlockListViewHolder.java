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

public class BlockListViewHolder extends RecyclerView.ViewHolder {

    public Button btn_unblock;
    public ImageView Img_Profile;
    public TextView txt_Name;

    public BlockListViewHolder(View itemView) {
        super(itemView);
        btn_unblock = itemView.findViewById(R.id.btn_unblock);
        Img_Profile = itemView.findViewById(R.id.Block_Profile);
        txt_Name = itemView.findViewById(R.id.Block_Info);
    }
}
