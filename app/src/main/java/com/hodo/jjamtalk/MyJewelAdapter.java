package com.hodo.jjamtalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.ViewHolder.MyJewelViewHolder;

/**
 * Created by mjk on 2017-09-21.
 */

class MyJewelAdapter extends BaseAdapter {
    Context mContext;


    public MyJewelAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_my_jewel_list,viewGroup,false);
        ImageView iv =v.findViewById(R.id.iv_jewel);
        TextView tv = v.findViewById(R.id.tv_count);
        iv.setImageResource(R.drawable.gold);
        tv.setText("x3");

        return v;
    }
}
