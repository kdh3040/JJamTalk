package com.dohosoft.talkking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dohosoft.talkking.Data.BoardMsgClientData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.NotiData;
import com.dohosoft.talkking.ViewHolder.NotiListViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class NotiListAdapter extends RecyclerView.Adapter<NotiListViewHolder> {

    private MyData mMyData = MyData.getInstance();

    Context mContext;
    public TextView txt_empty;

    public NotiListAdapter(Context context, TextView empty) {
        super();
        mContext = context;
        txt_empty = empty;
    }

    @Override
    public NotiListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_notilist,parent,false);

        if(mMyData.arrNotiDataList.size() == 0)
        {
            txt_empty.setVisibility(View.VISIBLE);
        }
        else {
            txt_empty.setVisibility(View.GONE);
        }

        return new NotiListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotiListViewHolder holder, final int position) {


        Glide.with(mContext)
                .load(R.drawable.notice_list)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.Img_Icon);

        holder.txt_Title.setText(mMyData.arrNotiDataList.get(position).Title);
        holder.txt_Date.setText(mMyData.arrNotiDataList.get(position).Date);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.Noti_Date:
                    case R.id.Noti_Icon:
                    case R.id.Noti_Title:

                        Intent intent = new Intent(mContext, NotiBodyActivity.class);
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                        intent.putExtra("Target", position);

                        mContext.startActivity(intent);

                        break;
                    }
                }
        };


        holder.txt_Title.setOnClickListener(listener);
        holder.txt_Date.setOnClickListener(listener);
        holder.Img_Icon.setOnClickListener(listener);


    }

    @Override
    public int getItemCount() {
        return  mMyData.arrNotiDataList.size();
    }
}
