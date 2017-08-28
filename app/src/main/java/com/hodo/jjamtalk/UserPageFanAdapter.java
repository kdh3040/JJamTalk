package com.hodo.jjamtalk;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class UserPageFanAdapter extends BaseAdapter {

    ArrayList<UserPageFan> arrayList = new ArrayList<>();

    Context mContext;
    public UserPageFanAdapter(Context context) {
        super();
        mContext = context;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //구현





        return view;
    }
}
