package com.hodo.jjamtalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by mjk on 2017. 8. 23..
 */

public class BoardListAdapter extends BaseAdapter{

    Context mContext;

    int [] in = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,1,1,1,1,1,1,1,1,1,1,1,1,1};

    public BoardListAdapter(Context context) {
        mContext = context;

    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int i) {
        return in[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView;
        if(view != null){
            mView =view;
        }else{
            mView = LayoutInflater.from(mContext).inflate(R.layout.testlist,viewGroup,false);
        }
        TextView tv = mView.findViewById(R.id.tv_test);
        tv.setText(getItem(i)+"");
        return mView;
    }
}
