package com.hodo.jjamtalk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.ViewHolder.GridUserViewHolder;

/**
 * Created by mjk on 2017. 8. 10..
 */

class HotAdapter extends RecyclerView.Adapter <GridUserViewHolder>
{
    Context mContext;

    public HotAdapter(Context context) {
        mContext = context;
    }

    @Override
    public GridUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,UserPageActivity.class));
            }
        });

        return new GridUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridUserViewHolder holder, int position) {
        holder.textView.setText("핫멤버, 25, 20km");
        holder.imageView.setImageResource(R.mipmap.home_icon);
    }



    @Override
    public int getItemCount() {
        return 20;
    }
}


