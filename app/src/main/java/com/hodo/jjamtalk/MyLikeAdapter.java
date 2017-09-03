package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.ViewHolder.MyLikeViewHolder;


/**
 * Created by mjk on 2017. 8. 28..
 */

public class MyLikeAdapter extends RecyclerView.Adapter<MyLikeViewHolder> {
    Context mContext;
    UIData mUIData = UIData.getInstance();

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
    public void onBindViewHolder(MyLikeViewHolder holder, int position) {

        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/7)));
        holder.imageView.setImageResource(R.mipmap.hdvd);
        holder.tv_nickname.setText("상희");
        holder.tv_honeycount.setText("1000꿀");
        holder.tv_rank.setText("13위");

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
