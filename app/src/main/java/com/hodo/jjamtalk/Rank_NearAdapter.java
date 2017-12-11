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
import com.hodo.jjamtalk.Util.LocationFunc;
import com.hodo.jjamtalk.ViewHolder.GridUserViewHolder;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class  Rank_NearAdapter extends RecyclerView.Adapter<GridUserViewHolder> {
    Context mContext;

    private SettingData mSetting = SettingData.getInstance();
    private LocationFunc mLocFunc = LocationFunc.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private UIData mUIData = UIData.getInstance();


    public Rank_NearAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public GridUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user,parent,false);
      /*  view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAppStatus.bCheckMultiSend == false) {

                    Intent intent = new Intent(mContext, UserPageActivity.class);
                    intent.putExtra("Target", stTargetData);
                    mContext.startActivity(intent);
                }
                    //mContext.startActivity(new Intent(mContext,UserPageActivity.class));
            }
        });
*/


        return new GridUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridUserViewHolder holder, final int position) {


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mUIData.getWidth()/mSetting.getViewCount(),(mUIData.getWidth()/mSetting.getViewCount()));
        holder.iv_profile.setLayoutParams(lp);
        //lp.setMargins(3,3,3,3);
        RelativeLayout.LayoutParams lpForTextView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)((mUIData.getWidth()/mSetting.getViewCount())*0.2));
        lpForTextView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForTextView.addRule(RelativeLayout.RIGHT_OF,R.id.iv_honey_rank);
        holder.textView.setLayoutParams(lpForTextView);

        holder.iv_honey_rank.setImageResource(R.drawable.icon_location);
        RelativeLayout.LayoutParams lpForIcon = new RelativeLayout.LayoutParams((int)(mUIData.getWidth()/mSetting.getViewCount()*0.2),(int)(mUIData.getWidth()/mSetting.getViewCount()*0.2));
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        holder.iv_honey_rank.setLayoutParams(lpForIcon);




        int i = position;

        switch (mSetting.getnSearchSetting())
        {
            //  남자 탐색
            case 1:
                float Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserMan_Near.get(i).Lat, mMyData.arrUserMan_Near.get(i).Lon);
                Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);
                holder.textView.setText(/*mMyData.arrUserMan_Near.get(i).NickName + ", " + mMyData.arrUserMan_Near.get(i).Age + "세, " + */(int)Dist + "km");
                Glide.with(mContext)
                        .load(mMyData.arrUserMan_Near.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            // 여자 탐색
            case 2:
                Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserWoman_Near.get(i).Lat, mMyData.arrUserWoman_Near.get(i).Lon);
                Log.d("Guide !!!! ", "Case 2 : "+ (int)Dist);
                holder.textView.setText(/*mMyData.arrUserWoman_Near.get(i).NickName + ", " + mMyData.arrUserWoman_Near.get(i).Age + "세, " + */(int)Dist + "km");
                Glide.with(mContext)
                        .load(mMyData.arrUserWoman_Near.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);

                break;
            case 3:
                Log.d("Guide !!!! ", "Case 3");
                Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserAll_Near.get(i).Lat, mMyData.arrUserAll_Near.get(i).Lon);
                holder.textView.setText(/*mMyData.arrUserAll_Near.get(i).NickName + ", " + mMyData.arrUserAll_Near.get(i).Age + "세, " + */(int)Dist + "km");
                Glide.with(mContext)
                        .load(mMyData.arrUserAll_Near.get(i).Img)
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
            rtValue = mMyData.arrUserMan_Near.size();
        } else if (mSetting.getnSearchSetting() == 2) {
            Log.d("Guide !!!! ", "getItem 2");
            rtValue = mMyData.arrUserWoman_Near.size();
        } else if (mSetting.getnSearchSetting() == 3) {
            Log.d("Guide !!!! ", "getItem 3");
            rtValue = mMyData.arrUserAll_Near.size();
        }
        return rtValue;
    }
}
