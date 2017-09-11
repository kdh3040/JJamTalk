package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
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
        holder.imageView.setImageResource(R.drawable.male2);








    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
