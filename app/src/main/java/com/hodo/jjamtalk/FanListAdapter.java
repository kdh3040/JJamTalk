package com.hodo.jjamtalk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.ViewHolder.FanViewHolder;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class FanListAdapter extends RecyclerView.Adapter<FanViewHolder>{

    Context mContext;
    UIData mUIData = UIData.getInstance();

    public FanListAdapter(Context context) {
        mContext = context;

    }

    @Override
    public FanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_my_fan,parent,false);



        return new FanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FanViewHolder holder, int position) {
        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/7)));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,UserPageActivity.class));



            }
        });
        holder.nickname.setText("아이유");
        holder.giftranking.setText("1위");
        holder.giftCount.setText("313123꿀");
        holder.imageView.setImageResource(R.mipmap.hdvd);

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
