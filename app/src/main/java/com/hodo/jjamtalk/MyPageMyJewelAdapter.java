package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
 * Created by mjk on 2017-10-01.
 */

public class MyPageMyJewelAdapter extends RecyclerView.Adapter<MyJewelViewHolder> {

    Context mContext;
    UIData mUIdata = UIData.getInstance();
    MyData mMyData = MyData.getInstance();

    Activity mActivity;

    public MyPageMyJewelAdapter(Context context,Activity activity) {
        super();
        mContext = context;
        mActivity = activity;

    }

    @Override
    public MyJewelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_jewel,null);

        return new MyJewelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyJewelViewHolder holder, final int position) {
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int nSellIdx = mMyData.itemIdx.get(position);
                final int[] nSellCount = {mMyData.itemList.get(nSellIdx)};
                final int nSellGold = mUIdata.getSellJewelValue()[nSellIdx];
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMyData.setUserHoney(mMyData.getUserHoney() + nSellGold);
                        mMyData.itemList.put(nSellIdx, --nSellCount[0]);
                        mMyData.SaveMyItem(nSellIdx, nSellCount[0]);
                        if(nSellCount[0] == 0) {
                            mMyData.itemList.remove(nSellIdx);
                            mMyData.nItemCount--;
                        }

                        mMyData.refreshItemIdex();
                        notifyDataSetChanged();

                    }
                }).setMessage("아이템을 "+  nSellGold  + " 골드에 판매하시겠습니까?");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        int index = mMyData.itemIdx.get(position);
        holder.iv.setImageResource(mUIdata.getJewels()[index]);
        int count = mMyData.itemList.get(index);
        holder.tv.setText("x" + Integer.toString(count));

        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(mUIdata.getWidth()/2,mUIdata.getHeight()/10));

    }

    @Override
    public int getItemCount() {
        return mMyData.nItemCount;
    }
}
