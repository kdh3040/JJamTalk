package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.ViewHolder.GridUserViewHolder;

/**
 * Created by mjk on 2017. 8. 21..
 */

public class Rank_GoldReceiveAdapter extends RecyclerView.Adapter<GridUserViewHolder> {

    Context mContext;
    private AppStatus mAppStatus = AppStatus.getInstance();
    private UIData mUIData = UIData.getInstance();
    private SettingData mSetting = SettingData.getInstance();
    private MyData mMyData = MyData.getInstance();

    public Rank_GoldReceiveAdapter(Context context) {
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

        holder.iv_profile.setLayoutParams(new RelativeLayout.LayoutParams(mUIData.getWidth()/mSetting.getViewCount(),mUIData.getWidth()/mSetting.getViewCount()));

        RelativeLayout.LayoutParams lpForTextView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)((mUIData.getWidth()/mSetting.getViewCount())*0.2));
        lpForTextView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForTextView.addRule(RelativeLayout.RIGHT_OF,R.id.iv_honey_rank);
        holder.textView.setLayoutParams(lpForTextView);

        RelativeLayout.LayoutParams lpForIcon = new RelativeLayout.LayoutParams((int)(mUIData.getWidth()/mSetting.getViewCount()*0.2),(int)(mUIData.getWidth()/mSetting.getViewCount()*0.2));
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        holder.iv_honey_rank.setLayoutParams(lpForIcon);





        //holder.textView.setText("100만 꿀");
        //holder.iv_honey_rank.setVisibility(View.INVISIBLE);

        Log.d("Guide !!!! ", "Start");
        int i = position;

        switch (mSetting.getnSearchSetting())
        {
            //  남자 탐색
            case 1:

                holder.textView.setText(-1*mMyData.arrUserMan_Recv.get(i).RecvCount+"개");
                Glide.with(mContext)
                        .load(mMyData.arrUserMan_Recv.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            // 여자 탐색
            case 2:
                holder.textView.setText(-1*mMyData.arrUserWoman_Recv.get(i).RecvCount+"개");
                Glide.with(mContext)
                        .load(mMyData.arrUserWoman_Recv.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            case 3:
                holder.textView.setText(-1*mMyData.arrUserAll_Recv.get(i).RecvCount+"개");
                Glide.with(mContext)
                        .load(mMyData.arrUserAll_Recv.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {

        int rtValue = 0;
        if (mSetting.getnSearchSetting() == 1) {
            rtValue = mMyData.arrUserMan_Recv.size();
        } else if (mSetting.getnSearchSetting() == 2) {
            rtValue = mMyData.arrUserWoman_Recv.size();
        } else if (mSetting.getnSearchSetting() == 3) {
            rtValue = mMyData.arrUserAll_Recv.size();
        }
        return rtValue;
    }
}
