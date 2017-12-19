package com.hodo.jjamtalk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.ViewHolder.MyLikeViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by mjk on 2017. 8. 28..
 */

public class MyLikeAdapter extends RecyclerView.Adapter<MyLikeViewHolder> {
    Context mContext;
    UIData mUIData = UIData.getInstance();

    private MyData mMyData = MyData.getInstance();

    public MyLikeAdapter(Context context) {
        mContext = context;

    }

    @Override
    public MyLikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_my_like,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //유저페이지 액티비티
            }
        });

        return new MyLikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyLikeViewHolder holder, final int position) {

/*
        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/7)));
        holder.imageView.setImageResource(R.mipmap.hdvd);
        holder.tv_nickname.setText("상희");
        holder.tv_honeycount.setText("1000꿀");
        holder.tv_rank.setText("13위");
*/


        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/7)));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mContext.startActivity(new Intent(mContext,UserPageActivity.class));

                Intent intent = new Intent(mContext, UserPageActivity.class);
                Bundle bundle = new Bundle();

        /*        for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrMyStarDataList.get(position).FanList.entrySet())
                    mMyData.arrMyStarDataList.get(position).arrFanList.add(entry.getValue());

                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrMyStarDataList.get(position).StarList.entrySet())
                    mMyData.arrMyStarDataList.get(position).arrStarList.add(entry.getValue());*/

                bundle.putSerializable("Target", mMyData.arrMyStarDataList.get(position));

                intent.putExtras(bundle);

                view.getContext().startActivity(intent);


            }
        });
        //holder.imageView.setImageResource(R.mipmap.hdvd);

        Glide.with(mContext)
                .load(mMyData.arrMyStarDataList.get(position).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .thumbnail(0.1f)
                .into(holder.imageView);

        holder.tv_nickname.setText(mMyData.arrMyStarList.get(position).Nick);
        holder.tv_rank.setText((position + 1) + "위");

        int SendCnt = mMyData.arrMyStarList.get(position).Count * -1;
        holder.tv_honeycount.setText(Integer.toString(SendCnt) + "골드");


    }

    @Override
    public int getItemCount() {
        return mMyData.arrMyStarDataList.size();
    }
}
