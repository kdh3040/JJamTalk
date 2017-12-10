package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.ViewHolder.MyJewelViewHolder;

/**
 * Created by mjk on 2017. 9. 26..
 */

public class MyPageJewelAdapter extends RecyclerView.Adapter<MyJewelViewHolder> {

    Context mContext;
    UIData mUIdata = UIData.getInstance();
    MyData mMyData = MyData.getInstance();

    public MyPageJewelAdapter(Context context) {

        super();
        mContext = context;
    }

    @Override
    public MyJewelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_jewel,parent,false);
        return new MyJewelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyJewelViewHolder holder, int position) {
        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(mUIdata.getWidth()/11, ViewGroup.LayoutParams.MATCH_PARENT));
        holder.iv.setImageResource(mUIdata.getJewels()[position]);

        /*
        for( position < 8 ; position++)
            if(mMyData.sad[position] != 0)
                  그려라
                          나가자

         드로우 리스트 새로 만들어서 관리
         */


  /*      if(mMyData.item_1 != 0)
            holder.iv.setImageResource(mUIdata.getJewels()[0]);
        else if(mMyData.item_2 != 0)
            holder.iv.setImageResource(mUIdata.getJewels()[1]);
        else if(mMyData.item_3 != 0)
            holder.iv.setImageResource(mUIdata.getJewels()[2]);
        else if(mMyData.item_4 != 0)
            holder.iv.setImageResource(mUIdata.getJewels()[3]);

        else if(mMyData.item_5 != 0)
            holder.iv.setImageResource(mUIdata.getJewels()[4]);
        else  if(mMyData.item_6 != 0)
            holder.iv.setImageResource(mUIdata.getJewels()[5]);
        else if(mMyData.item_7 != 0)
            holder.iv.setImageResource(mUIdata.getJewels()[7]);
        else  if(mMyData.item_8 != 0)
            holder.iv.setImageResource(mUIdata.getJewels()[7]);*/
        //holder.iv.setImageResource(mUIdata.getJewels()[0]);
        //holder.iv.setImageResource(mUIdata.getJewels()[7]);

        holder.tv.setText("x3");
    }

    @Override
    public int getItemCount() {
        //return mUIdata.getJewels().length;
        return mMyData.nItemCount;2
    }
}
