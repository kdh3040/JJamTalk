package com.hodo.talkking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SettingData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Util.AppStatus;
import com.hodo.talkking.Util.LocationFunc;
import com.hodo.talkking.ViewHolder.GridUserViewHolder;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class Rank_NewMemberAdapter extends RecyclerView.Adapter<GridUserViewHolder> {

    Context mContext;

    private SettingData mSetting = SettingData.getInstance();
    private LocationFunc mLocFunc = LocationFunc.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private UIData mUIData = UIData.getInstance();


    public Rank_NewMemberAdapter(Context context) {
        mContext = context;
    }

    @Override
    public GridUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user,parent,false);
        return new GridUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridUserViewHolder holder, final int position) {

        /*holder.iv_profile.setLayoutParams(new_img RelativeLayout.LayoutParams(mUIData.getWidth()/mSetting.getViewCount(),((mUIData.getWidth()/mSetting.getViewCount()))));
        //holder.textView.setLayoutParams(new_img RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)((mUIData.getWidth()/mSetting.getViewCount())*0.2)));

        //holder.textView.setText("뉴멤버, 25, 20km");
        holder.iv_profile.setImageResource(R.mipmap.ic_launcher);
        RelativeLayout.LayoutParams lpForIcon = new_img RelativeLayout.LayoutParams((int)(mUIData.getWidth()/mSetting.getViewCount()*0.2),(int)(mUIData.getWidth()/mSetting.getViewCount()*0.2));
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForIcon.setMargins(10,2,2,2);
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        holder.iv_honey_rank.setLayoutParams(lpForIcon);
        holder.iv_honey_rank.setImageResource(R.mipmap.ic_new);

*/
        holder.iv_profile.setLayoutParams(new RelativeLayout.LayoutParams(mUIData.getWidth()/mSetting.getViewCount(),mUIData.getWidth()/mSetting.getViewCount()));

        RelativeLayout.LayoutParams lpForTextView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)((mUIData.getWidth()/mSetting.getViewCount())*0.2));
        lpForTextView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForTextView.addRule(RelativeLayout.RIGHT_OF,R.id.iv_honey_rank);
        holder.textView.setLayoutParams(lpForTextView);

        RelativeLayout.LayoutParams lpForIcon = new RelativeLayout.LayoutParams((int)(mUIData.getWidth()/mSetting.getViewCount()*0.2),(int)(mUIData.getWidth()/mSetting.getViewCount()*0.2));
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


        holder.iv_honey_rank.setLayoutParams(lpForIcon);
        holder.iv_honey_rank.setImageResource(R.drawable.new_img);

        RelativeLayout.LayoutParams lpForBgTxt  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)((mUIData.getWidth()/mSetting.getViewCount())*0.2));
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        holder.bg_tv.setLayoutParams(lpForBgTxt);



        int i = position;

        switch (mSetting.getnSearchSetting())
        {

            //  남자 탐색
            case 1:
                holder.textView.setText("(" + mMyData.arrUserMan_New_Age.get(i).Age+"세)"+mMyData.arrUserMan_New_Age.get(i).NickName);

                Glide.with(mContext)
                        .load(mMyData.arrUserMan_New_Age.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            // 여자 탐색
            case 2:
                holder.textView.setText("(" + mMyData.arrUserWoman_New_Age.get(i).Age+"세)"+mMyData.arrUserWoman_New_Age.get(i).NickName+mMyData.arrUserWoman_New_Age.get(i).NickName);

                Glide.with(mContext)
                        .load(mMyData.arrUserWoman_New_Age.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            case 0:
            case 3:
                //Log.d("Guide !!!! ", "Case 3");
                holder.textView.setText("(" + mMyData.arrUserAll_New_Age.get(i).Age+"세)"+mMyData.arrUserAll_New_Age.get(i).NickName);
                Glide.with(mContext)
                        .load(mMyData.arrUserAll_New_Age.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            default:
                break;
        }
    }

    @Override
    public long getItemId(int position){
        long rtValue = 0;
        if (mSetting.getnSearchSetting() == 0 || mSetting.getnSearchSetting() == 3 ) {
            rtValue = Long.valueOf(mMyData.arrUserAll_New_Age.get(position).Idx);
        } else if (mSetting.getnSearchSetting() == 1) {
            rtValue  = Long.valueOf(mMyData.arrUserMan_New_Age.get(position).Idx);
        } else if (mSetting.getnSearchSetting() == 2) {
            rtValue = Long.valueOf(mMyData.arrUserWoman_New_Age.get(position).Idx);
        }
        return rtValue;
    }

    @Override
    public int getItemCount() {
        int rtValue = 0;
    /*    if (mSetting.getnSearchSetting() == 0 || mSetting.getnSearchSetting() == 3 ) {
            Log.d("Guide !!!! ", "getItem 1");
            rtValue = mMyData.arrUserAll_New.size();
        } else if (mSetting.getnSearchSetting() == 1) {
            Log.d("Guide !!!! ", "getItem 3");
            rtValue = mMyData.arrUserMan_New.size();
        } else if (mSetting.getnSearchSetting() == 2) {
            Log.d("Guide !!!! ", "getItem 2");
            rtValue = mMyData.arrUserWoman_New.size();
        }*/

        if (mSetting.getnSearchSetting() == 0 || mSetting.getnSearchSetting() == 3 ) {
            //Log.d("Guide !!!! ", "getItem 3");
            rtValue = mMyData.arrUserAll_New_Age.size();
        } else if (mSetting.getnSearchSetting() == 1) {
            //Log.d("Guide !!!! ", "getItem 1");
            rtValue = mMyData.arrUserMan_New_Age.size();
        } else if (mSetting.getnSearchSetting() == 2) {
            //Log.d("Guide !!!! ", "getItem 2");
            rtValue = mMyData.arrUserWoman_New_Age.size();
        }
        return rtValue;
    }
}
