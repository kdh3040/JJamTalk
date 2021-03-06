package com.dohosoft.talkking;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.ViewHolder.MailboxViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        //stTargetData = mMyData.arrGiftUserDataList.get(position);

        Glide.with(mActivity)
                .load( mMyData.arrGiftUserDataList.get(mMyData.arrGiftHoneyDataList.get(position).strSendName).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .bitmapTransform(new CropCircleTransformation(mActivity))
                .into(holder.imageView);

        Date sendDate = new Date(mMyData.arrGiftHoneyDataList.get(position).strSendDate);
        if(CommonFunc.getInstance().IsTodayDate(new Date(CommonFunc.getInstance().GetCurrentTime()), sendDate))
        {
            SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_TODAY_DATE_FORMAT);
            holder.SendDate.setText(ctime.format(sendDate));
        }
        else
        {
            SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_DATE_FORMAT);
            holder.SendDate.setText(ctime.format(sendDate));
        }

        holder.textView.setText(Integer.toString(mMyData.arrGiftHoneyDataList.get(position).nSendHoney));

        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View giftView = LayoutInflater.from(view.getContext()).inflate(R.layout.alert_open_mail,null);

                //final ImageView popup_ImageView  = giftView.findViewById(R.id.iv_mailfrom);
                TextView popup_textcount  = giftView.findViewById(R.id.tv_count );
                TextView popup_textMsg  = giftView.findViewById(R.id.msg);
                TextView popup_textNickname = giftView.findViewById(R.id.tv_mailfrom);
                /*Glide.with(mActivity)
                        .load(mMyData.arrGiftHoneyDataList.get(position).strTargetImg)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(popup_ImageView);*/

                popup_textcount.setText(Integer.toString(mMyData.arrGiftHoneyDataList.get(position).nSendHoney));
                popup_textMsg.setText(mMyData.arrGiftHoneyDataList.get(position).strTargetMsg);
                popup_textNickname.setText( mMyData.arrGiftUserDataList.get(mMyData.arrGiftHoneyDataList.get(position).strSendName).NickName);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(giftView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button btnChatStart = giftView.findViewById(R.id.confirm);
                Button btnOK = giftView.findViewById(R.id.btn_ok);

                btnOK.setVisibility(View.GONE);
               /* btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMyData.arrGiftUserDataList.remove(mMyData.arrGiftHoneyDataList.get(position).strSendName);
                        mMyData.arrGiftHoneyDataList.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });*/

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


                bundle.putSerializable("Target", mMyData.arrGiftUserDataList.get(mMyData.arrGiftHoneyDataList.get(position).strSendName));
                intent.putExtra("FanList", mMyData.arrGiftUserDataList.get(mMyData.arrGiftHoneyDataList.get(position).strSendName).arrFanList);
                intent.putExtra("FanCount", mMyData.arrGiftUserDataList.get(mMyData.arrGiftHoneyDataList.get(position).strSendName).FanCount);

                intent.putExtra("StarList", mMyData.arrGiftUserDataList.get(mMyData.arrGiftHoneyDataList.get(position).strSendName).arrStarList);
                intent.putExtras(bundle);

                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMyData.arrGiftUserDataList.size();
    }
}
