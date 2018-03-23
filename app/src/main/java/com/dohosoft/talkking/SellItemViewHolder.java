package com.dohosoft.talkking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mjk on 2018-01-08.
 */

public class SellItemViewHolder extends RecyclerView.ViewHolder {

    ImageView iv;
    TextView tv;

    EditText et;
    public LinearLayout linearLayout;

    public SellItemViewHolder(View itemView) {
        super(itemView);

        iv= itemView.findViewById(R.id.iv_sell);
        tv = itemView.findViewById(R.id.tv_count);
        //et=itemView.findViewById(R.id.et_sell);
        linearLayout =itemView.findViewById(R.id.layout);
    }
}
