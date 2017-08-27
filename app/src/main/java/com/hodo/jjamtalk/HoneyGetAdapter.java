package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.ViewHolder.HoneyGetViewHolder;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class HoneyGetAdapter extends RecyclerView.Adapter<HoneyGetViewHolder>{

    private MyData mMyData = MyData.getInstance();

    Context mContext;


    public HoneyGetAdapter(Context context) {
        super();
        mContext= context;
    }

    @Override
    public HoneyGetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_honey,parent,false);
        return new HoneyGetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HoneyGetViewHolder holder, int position) {
        //holder.tv_honeycount.setText("3000");
        //holder.tv_nickname.setText("나애리");

        holder.tv_honeycount.setText(Integer.toString(mMyData.arrRecvHoneyDataList.get(position).nSendHoney));
        holder.tv_nickname.setText(mMyData.arrRecvHoneyDataList.get(position).strTargetNick);

        holder.iv_profile.setImageResource(R.mipmap.girl1);

        Glide.with(mContext)
                .load(mMyData.arrRecvHoneyDataList.get(position).strTargetImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_profile);

    }

    @Override
    public int getItemCount() {
        return mMyData.arrRecvHoneyDataList.size();
    }
}