package com.hodo.jjamtalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by mjk on 2017-09-21.
 */

public class MyJewelAdapter extends BaseAdapter {
    Context mContext;
    int jewels[];
    LayoutInflater inflater;


    public MyJewelAdapter(Context context,int [] jewels) {
        super();
        mContext = context;
        this.jewels= jewels;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return jewels.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.item_dialog_my_jewel_list,null);
        ImageView iv =view.findViewById(R.id.iv_jewel);

        iv.setImageResource(jewels[i]);


        return view;
    }
}
