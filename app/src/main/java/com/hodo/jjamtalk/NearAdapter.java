package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.ViewHolder.GridUserViewHolder;

import static com.hodo.jjamtalk.R.mipmap.girl1;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class NearAdapter extends RecyclerView.Adapter<GridUserViewHolder> {
    Context mContext;

    public NearAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public GridUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user,parent,false);

        return new GridUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridUserViewHolder holder, int position) {
        holder.textView.setText("아이유, 25, 20km");
        holder.imageView.setImageResource(R.mipmap.girl1);


    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
