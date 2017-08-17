package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.ViewHolder.HoneySentViewHoler;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class HoneySentAdapter extends RecyclerView.Adapter<HoneySentViewHoler>{

    Context mContext;

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

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
