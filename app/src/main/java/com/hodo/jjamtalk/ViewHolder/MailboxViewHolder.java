package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class MailboxViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView textView;
    public LinearLayout layout;

    public MailboxViewHolder(View itemView) {
        super(itemView);
        layout = itemView.findViewById(R.id.ll_mail_item);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View giftView = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_open_mail,null);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(giftView);
                final AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        imageView = itemView.findViewById(R.id.iv_my_card);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        textView = itemView.findViewById(R.id.tv_honeycount);
    }
}
