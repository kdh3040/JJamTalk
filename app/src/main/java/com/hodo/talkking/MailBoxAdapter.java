package com.hodo.talkking;

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
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Util.CommonFunc;
import com.hodo.talkking.ViewHolder.MailboxViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class MailBoxAdapter extends RecyclerView.Adapter<MailboxViewHolder>{
    Context mContext;
    Activity mActivity;
    private MyData mMyData = MyData.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

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
                .thumbnail(0.1f)
                .bitmapTransform(new CropCircleTransformation(mActivity))
                .into(holder.imageView);

        holder.SendDate.setText(mMyData.arrGiftHoneyDataList.get(position).strSendDate);

        holder.textView.setText(Integer.toString(mMyData.arrGiftHoneyDataList.get(position).nSendHoney));

        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View giftView = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_open_mail,null);

                final ImageView popup_ImageView  = giftView.findViewById(R.id.iv_mailfrom);
                TextView popup_textcount  = giftView.findViewById(R.id.tv_count );
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

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMyData.arrGiftHoneyDataList.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                btnChatStart.setOnClickListener(new View.OnClickListener() {
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
