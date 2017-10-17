package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.ViewHolder.MyJewelViewHolder;

/**
 * Created by mjk on 2017-10-01.
 */

public class MyPageMyJewelAdapter extends RecyclerView.Adapter<MyJewelViewHolder> {

    Context mContext;
    UIData mUIdata = UIData.getInstance();
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
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setMessage("해당 아이템을 파시겠습니까?   "+mUIdata.getSellJewelValue()[position]+"꿀");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.iv.setImageResource(mUIdata.getJewels()[position]);
        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(mUIdata.getWidth()/4,mUIdata.getHeight()/4));
        holder.tv.setText("x3");



    }

    @Override
    public int getItemCount() {
        return 8;
    }
}
