package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.ViewHolder.GridUserViewHolder;

/**
 * Created by mjk on 2017. 8. 21..
 */

public class Rank_HoneyReceiveAdapter extends RecyclerView.Adapter<GridUserViewHolder> {

    Context mContext;
    private AppStatus mAppStatus = AppStatus.getInstance();
    private UIData mUIData = UIData.getInstance();

    public Rank_HoneyReceiveAdapter(Context context) {
        mContext = context;
    }

    @Override
    public GridUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user,parent,false);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(mAppStatus.bCheckMultiSend == false)
                    mContext.startActivity(new Intent(mContext,UserPageActivity.class));*/
            }
        });

        return new GridUserViewHolder(view);


    }

    @Override
    public void onBindViewHolder(GridUserViewHolder holder, int position) {

        holder.iv_profile.setLayoutParams(new RelativeLayout.LayoutParams(mUIData.getWidth()/3,(int)((mUIData.getWidth()/3)*1.2)));
        holder.textView.setLayoutParams(new RelativeLayout.LayoutParams(mUIData.getWidth()/3,(int)((mUIData.getWidth()/3)*0.2)));
        holder.textView.setText("100만 꿀");
        holder.iv_honey_rank.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
