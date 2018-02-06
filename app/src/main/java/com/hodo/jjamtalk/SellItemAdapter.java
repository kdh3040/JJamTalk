package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.ViewHolder.MyJewelViewHolder;

/**
 * Created by mjk on 2018-01-08.
 */

public class SellItemAdapter extends RecyclerView.Adapter<SellItemViewHolder> {

    Context mContext;
    UIData mUIdata = UIData.getInstance();
    MyData mMyData = MyData.getInstance();

    Activity mActivity;

    public SellItemAdapter(Context context,Activity activity) {
        super();
        mContext = context;
        mActivity = activity;

    }

    @Override
    public SellItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_sell_item,null);
        return new SellItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SellItemViewHolder holder, int position) {
        holder.iv.setImageResource(mUIdata.getJewels()[position]);
        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(mUIdata.getWidth()/2,mUIdata.getHeight()/20));

    }

    @Override
    public int getItemCount() {
        return 8;
    }
}
