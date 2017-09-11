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
    public void onBindViewHolder(final MailboxViewHolder holder, final int position) {
/*        holder.imageView.setImageResource(R.drawable.honeyicon);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View giftView = LayoutInflater.from(mActivity).inflate(R.layout.alert_open_mail,null);

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setView(giftView);
                final AlertDialog dialog = builder.create();
                dialog.show();

            }
        });*/


        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/8)));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mContext.startActivity(new Intent(mContext,UserPageActivity.class));

                View giftView = LayoutInflater.from(mActivity).inflate(R.layout.alert_open_mail,null);

                ImageView popup_ImageView  = giftView.findViewById(R.id.iv_mailfrom);
                TextView popup_textcount  = giftView.findViewById(R.id.tv_mailbox_sendcount);
                TextView popup_textMsg  = giftView.findViewById(R.id.tv_mailbox_sendmsg);

                Glide.with(mActivity)
                        .load(mMyData.arrGiftHoneyDataList.get(position).strTargetImg)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(popup_ImageView);

                popup_textcount.setText(Integer.toString(mMyData.arrGiftHoneyDataList.get(position).nSendHoney));
                popup_textMsg.setText(Integer.toString(mMyData.arrGiftHoneyDataList.get(position).nSendHoney));

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setView(giftView);
                final AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
        //holder.imageView.setImageResource(R.mipmap.hdvd);

        Glide.with(mActivity)
                .load(mMyData.arrGiftHoneyDataList.get(position).strTargetImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        holder.textView.setText(Integer.toString(mMyData.arrGiftHoneyDataList.get(position).nSendHoney));

    }

    @Override
    public int getItemCount() {
        return mMyData.arrGiftHoneyDataList.size();
    }
}
