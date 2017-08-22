package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class BlockListViewHolder extends RecyclerView.ViewHolder {

    public Button btn_unblock;

    public BlockListViewHolder(View itemView) {
        super(itemView);
        btn_unblock = itemView.findViewById(R.id.btn_unblock);
    }
}
