package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.ViewHolder.HoneySentViewHoler;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class HoneySentAdapter extends RecyclerView.Adapter<HoneySentViewHoler>{

    Context mContext;
    private MyData mMyData = MyData.getInstance();

    public HoneySentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public HoneySentViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_honey,parent,false);
        return new HoneySentViewHoler(view);
    }

    @Override
    public void onBindViewHolder(HoneySentViewHoler holder, int position) {
        holder.tv_Honeycount.setText(Integer.toString(mMyData.arrSendHoneyDataList.get(position).nSendHoney));
        holder.tv_Nickname.setText(mMyData.arrSendHoneyDataList.get(position).strTargetNick);
        holder.tv_Date.setText(mMyData.arrSendHoneyDataList.get(position).strSendDate);
        Glide.with(mContext)
                .load(mMyData.arrSendHoneyDataList.get(position).strTargetImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_Profile);

    }

    @Override
    public int getItemCount() {
        return mMyData.arrSendHoneyDataList.size();
    }
}
