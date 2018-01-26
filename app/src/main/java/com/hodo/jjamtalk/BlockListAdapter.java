package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SendData;
import com.hodo.jjamtalk.ViewHolder.BlockListViewHolder;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class BlockListAdapter extends RecyclerView.Adapter<BlockListViewHolder> {

    private MyData mMyData = MyData.getInstance();

    Context mContext;


    public BlockListAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public BlockListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_blocklist,parent,false);


        return new BlockListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlockListViewHolder holder, final int position) {


        Glide.with(mContext)
                .load(mMyData.arrBlockDataList.get(position).Img)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.Img_Profile);

        holder.txt_Name.setText(mMyData.arrBlockDataList.get(position).NickName);
        holder.btn_unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //차단 해제
                mMyData.delBlockList(mMyData.arrBlockDataList.get(position));
                mMyData.arrBlockDataList.remove(position);
                Toast.makeText(mContext, position + "번", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return  mMyData.arrBlockDataList.size();
    }
}
