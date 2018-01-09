package com.hodo.jjamtalk;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by mjk on 2018-01-08.
 */

public class SellItemViewHolder extends RecyclerView.ViewHolder {

    ImageView iv;
    EditText et;
    public LinearLayout linearLayout;

    public SellItemViewHolder(View itemView) {
        super(itemView);

        iv= itemView.findViewById(R.id.iv_sell);
        et=itemView.findViewById(R.id.et_sell);
        linearLayout =itemView.findViewById(R.id.layout);
    }
}
