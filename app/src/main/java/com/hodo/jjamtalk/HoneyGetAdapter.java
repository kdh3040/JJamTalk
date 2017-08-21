package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.ViewHolder.HoneyGetViewHolder;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class HoneyGetAdapter extends RecyclerView.Adapter<HoneyGetViewHolder>{

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
        holder.tv_honeycount.setText("3000");
        holder.tv_nickname.setText("나애리");


        // holder.iv_profile.setImageResource(R.mipmap.girl1);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
