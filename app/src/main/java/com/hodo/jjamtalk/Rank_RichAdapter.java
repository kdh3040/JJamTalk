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
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.Util.LocationFunc;
import com.hodo.jjamtalk.ViewHolder.GridUserViewHolder;

import java.util.ArrayList;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class Rank_RichAdapter extends RecyclerView.Adapter<GridUserViewHolder> {
    Context mContext;

    private SettingData mSetting = SettingData.getInstance();
    private LocationFunc mLocFunc = LocationFunc.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private UIData mUIData = UIData.getInstance();

    public UserData stTargetData = new UserData();
    private ArrayList<UserData> arrTargetData_Man = new ArrayList<>();
    private ArrayList<UserData> arrTargetData_Woman = new ArrayList<>();
    private ArrayList<UserData> arrTargetData_All = new ArrayList<>();

    public Rank_RichAdapter(Context context) {
        mContext = context;
    }

    @Override
    public GridUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user,parent,false);
        return new GridUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridUserViewHolder holder, final int position) {

        holder.iv_profile.setLayoutParams(new RelativeLayout.LayoutParams(mUIData.getWidth()/2,(mUIData.getWidth()/2)));
        holder.textView.setLayoutParams(new RelativeLayout.LayoutParams(mUIData.getWidth()/3,(int)((mUIData.getWidth()/3)*0.2)));

        holder.iv_profile.setImageResource(R.mipmap.girl1);
        holder.textView.setVisibility(View.INVISIBLE);

        Log.d("Guide !!!! ", "Start");
        int i = position;

        switch (mSetting.getnSearchSetting())
        {
            //  남자 탐색
            case 1:
                float Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserMan_Rank.get(i).Lat, mMyData.arrUserMan_Rank.get(i).Lon);
                Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);
                holder.iv_honey_rank.setImageResource(R.drawable.platinum_bee);

                //holder.textView.setText(/*mMyData.arrUserMan_Rank.get(i).NickName + ", " + mMyData.arrUserMan_Rank.get(i).Age + "세, " + (int)Dist + "km"*/mMyData.getUserRank()+"");
                Glide.with(mContext)
                        .load(mMyData.arrUserMan_Rank.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            // 여자 탐색
            case 2:
                Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserWoman_Rank.get(i).Lat, mMyData.arrUserWoman_Rank.get(i).Lon);
                Log.d("Guide !!!! ", "Case 2 : "+ (int)Dist);
                holder.iv_honey_rank.setImageResource(R.drawable.gold_bee);

                //holder.textView.setText(mMyData.getUserRank()+"");
                Glide.with(mContext)
                        .load(mMyData.arrUserWoman_Rank.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            case 3:
                Log.d("Guide !!!! ", "Case 3");
                Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserAll_Rank.get(i).Lat, mMyData.arrUserAll_Rank.get(i).Lon);

                holder.iv_honey_rank.setImageResource(R.drawable.bronze_bee);
                Glide.with(mContext)
                        .load(mMyData.arrUserAll_Rank.get(i).Img)
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
            Log.d("Guide !!!! ", "getItem 1");
            rtValue = mMyData.arrUserMan_Rank.size();
        } else if (mSetting.getnSearchSetting() == 2) {
            Log.d("Guide !!!! ", "getItem 2");
            rtValue = mMyData.arrUserWoman_Rank.size();
        } else if (mSetting.getnSearchSetting() == 3) {
            Log.d("Guide !!!! ", "getItem 3");
            rtValue = mMyData.arrUserAll_Rank.size();
        }
        return rtValue;
    }

}
