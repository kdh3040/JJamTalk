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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.ViewHolder.MailboxViewHolder;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class MailBoxAdapter extends RecyclerView.Adapter<MailboxViewHolder>{
    Context mContext;
    Activity mActivity;
    private MyData mMyData = MyData.getInstance();
    UIData mUIData = UIData.getInstance();
    public UserData stTargetData = new UserData();

    public MailBoxAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public MailboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.content_mailbox_item,parent,false);
        return new MailboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MailboxViewHolder holder, final int position) {
        stTargetData = mMyData.arrGiftUserDataList.get(position);

        Glide.with(mActivity)
                .load(mMyData.arrGiftHoneyDataList.get(position).strTargetImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        holder.SendDate.setText(mMyData.arrGiftHoneyDataList.get(position).strSendDate);

        holder.textView.setText(Integer.toString(mMyData.arrGiftHoneyDataList.get(position).nSendHoney));

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View giftView = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_open_mail,null);

                ImageView popup_ImageView  = giftView.findViewById(R.id.iv_mailfrom);
                TextView popup_textcount  = giftView.findViewById(R.id.tv_mailbox_sendcount);
                TextView popup_textMsg  = giftView.findViewById(R.id.tv_mailbox_sendmsg);

                Glide.with(mActivity)
                        .load(mMyData.arrGiftHoneyDataList.get(position).strTargetImg)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(popup_ImageView);

                popup_textcount.setText(Integer.toString(mMyData.arrGiftHoneyDataList.get(position).nSendHoney));
                popup_textMsg.setText(mMyData.arrGiftHoneyDataList.get(position).strTargetMsg);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(giftView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button btnChatStart = giftView.findViewById(R.id.btn_accept_honey);
                Button btnOK = giftView.findViewById(R.id.btn_ok);

                btnChatStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    // 대화방 만들기 또는 대화방으로 이동
                        boolean rtValuew = mMyData.makeSendList(stTargetData, mMyData.arrGiftHoneyDataList.get(position).strTargetMsg);
                        //mActivity.startActivity(new Intent(mActivity,ChatListActivity.class));
                        mActivity.finish();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserPageActivity.class);
                Bundle bundle = new Bundle();


                bundle.putSerializable("Target", stTargetData);
                intent.putExtra("FanList", stTargetData.arrFanList);
                intent.putExtra("FanCount", stTargetData.FanCount);

                intent.putExtra("StarList", stTargetData.arrStarList);
                intent.putExtras(bundle);

                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMyData.arrGiftHoneyDataList.size();
    }
}
