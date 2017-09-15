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
import com.hodo.jjamtalk.ViewHolder.FanViewHolder;

import java.util.LinkedHashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class FanListAdapter extends RecyclerView.Adapter<FanViewHolder>{

    Context mContext;
    UIData mUIData = UIData.getInstance();
    private MyData mMyData = MyData.getInstance();

    public FanListAdapter(Context context) {
        mContext = context;

    }

    @Override
    public FanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_my_fan,parent,false);



        return new FanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FanViewHolder holder, final int position) {
        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/7)));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mContext.startActivity(new Intent(mContext,UserPageActivity.class));

                Intent intent = new Intent(mContext, UserPageActivity.class);
                Bundle bundle = new Bundle();

                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrMyFanDataList.get(position).FanList.entrySet())
                    mMyData.arrMyFanDataList.get(position).arrFanList.add(entry.getValue());

                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrMyFanDataList.get(position).StarList.entrySet())
                    mMyData.arrMyFanDataList.get(position).arrStarList.add(entry.getValue());

                bundle.putSerializable("Target", mMyData.arrMyFanDataList.get(position));
       /*         intent.putExtra("FanList", mMyData.arrMyFanDataList.get(position).arrFanList);
                intent.putExtra("StarList", mMyData.arrMyFanDataList.get(position).arrStarList);*/
                intent.putExtras(bundle);

                view.getContext().startActivity(intent);


            }
        });
        //holder.imageView.setImageResource(R.mipmap.hdvd);

        Glide.with(mContext)
                .load(mMyData.arrMyFanDataList.get(position).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .thumbnail(0.1f)
                .into(holder.imageView);

        holder.nickname.setText(mMyData.arrMyFanList.get(position).Nick);
        holder.giftranking.setText((position + 1) + "위");

        int SendCnt = mMyData.arrMyFanList.get(position).Count * -1;
        holder.giftCount.setText(Integer.toString(SendCnt) + "꿀");

        /*holder.nickname.setText("아이유");
        holder.giftranking.setText("1위");
        holder.giftCount.setText("313123꿀");
        holder.imageView.setImageResource(R.mipmap.hdvd);*/

    }

    @Override
    public int getItemCount() {
        return mMyData.arrMyFanDataList.size();
    }
}
