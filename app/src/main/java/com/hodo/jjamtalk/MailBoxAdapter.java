package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.ViewHolder.MailboxViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class MailBoxAdapter extends RecyclerView.Adapter<MailboxViewHolder>{
    Context mContext;
    Activity mActivity;
    private MyData mMyData = MyData.getInstance();
    UIData mUIData = UIData.getInstance();

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
        return mMyData.arrGiftHoneyDataList.size();
    }
}
