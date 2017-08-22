package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.ViewHolder.MailboxViewHolder;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class MailBoxAdapter extends RecyclerView.Adapter<MailboxViewHolder>{
    Context mContext;
    Activity mActivity;

    public MailBoxAdapter(Activity activity) {
        mActivity = activity;

    }

    @Override
    public MailboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.content_mailbox_item,parent,false);
        return new MailboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MailboxViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.honeyicon);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View giftView = LayoutInflater.from(mActivity).inflate(R.layout.alert_open_mail,null);

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setView(giftView);
                final AlertDialog dialog = builder.create();
                dialog.show();

            }
        });






    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
