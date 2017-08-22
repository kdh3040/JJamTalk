package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.ViewHolder.BlockListViewHolder;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class BlockListAdapter extends RecyclerView.Adapter<BlockListViewHolder> {

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
    public void onBindViewHolder(BlockListViewHolder holder, int position) {

        holder.btn_unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //차단 해제
            }
        });



    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
