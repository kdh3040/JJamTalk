package com.dohosoft.talkking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SettingData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Util.AppStatus;
import com.dohosoft.talkking.Util.LocationFunc;
import com.dohosoft.talkking.ViewHolder.GridUserViewHolder;

import java.util.ArrayList;

import static com.dohosoft.talkking.Data.CoomonValueData.UNIQ_FANCOUNT;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class Rank_FanRichAdapter extends RecyclerView.Adapter<GridUserViewHolder> {
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

    public Rank_FanRichAdapter(Context context) {
        mContext = context;
    }


    @Override
    public GridUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user,parent,false);
        return new GridUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridUserViewHolder holder, final int position) {

        holder.iv_profile.setLayoutParams(new RelativeLayout.LayoutParams(mUIData.getWidth()/mSetting.getViewCount(),(mUIData.getWidth()/mSetting.getViewCount())));

        RelativeLayout.LayoutParams lpForTextView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)((mUIData.getWidth()/mSetting.getViewCount())*0.2));
        lpForTextView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForTextView.addRule(RelativeLayout.RIGHT_OF,R.id.iv_honey_rank);
        holder.textView.setLayoutParams(lpForTextView);

        //순위 레이아웃

        RelativeLayout.LayoutParams lpForIvRank = new RelativeLayout.LayoutParams((int)(mUIData.getWidth()/mSetting.getViewCount()*0.3),(int)(mUIData.getWidth()/mSetting.getViewCount()*0.3));
        lpForIvRank.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForIvRank.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        holder.iv_rank.setLayoutParams(lpForIvRank);

        //holder.iv_honey_rank.setLayoutParams(new_img RelativeLayout.LayoutParams((int)(mUIData.getWidth()/mSetting.getViewCount()*0.2),(int)(mUIData.getWidth()/mSetting.getViewCount()*0.2)));
        holder.iv_profile.setImageResource(R.drawable.image);

        RelativeLayout.LayoutParams lpForIcon = new RelativeLayout.LayoutParams((int)(mUIData.getWidth()/mSetting.getViewCount()*0.2),(int)(mUIData.getWidth()/mSetting.getViewCount()*0.2));
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        holder.iv_honey_rank.setLayoutParams(lpForIcon);

        RelativeLayout.LayoutParams lpForBgTxt  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)((mUIData.getWidth()/mSetting.getViewCount())*0.2));
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        holder.bg_tv.setLayoutParams(lpForBgTxt);


        //holder.textView.setVisibility(View.INVISIBLE);

        //Log.d("Guide !!!! ", "Start");
        int i = position;

        switch (mSetting.getnSearchSetting())
        {
           /* case 0:
            case 3:
                //   float Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserMan_Send.get(i).Lat, mMyData.arrUserMan_Send.get(i).Lon);
                //  Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);
                holder.iv_honey_rank.setImageResource(R.drawable.ic_fan);

                holder.textView.setText(-1* mMyData.arrUserAll_Send.get(i).FanCount+"명");
                Glide.with(mContext)
                        .load(mMyData.arrUserAll_Send.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            case 1:
                //   float Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserMan_Send.get(i).Lat, mMyData.arrUserMan_Send.get(i).Lon);
                //  Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);
                holder.iv_honey_rank.setImageResource(R.drawable.ic_fan);

                holder.textView.setText(-1 * mMyData.arrUserMan_Send.get(i).FanCount+"명");
                Glide.with(mContext)
                        .load(mMyData.arrUserMan_Send.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            // 여자 탐색
            case 2:

              //  Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserWoman_Send.get(i).Lat, mMyData.arrUserWoman_Send.get(i).Lon,"kilometer");
              //  Log.d("Guide !!!! ", "Case 2 : "+ (int)Dist);
                holder.iv_honey_rank.setImageResource(R.drawable.ic_fan);

                //  Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserWoman_Send.get(i).Lat, mMyData.arrUserWoman_Send.get(i).Lon,"kilometer");
                //  Log.d("Guide !!!! ", "Case 2 : "+ (int)Dist);



                holder.textView.setText(-1 * mMyData.arrUserWoman_Send.get(i).FanCount+"명");
                Glide.with(mContext)
                        .load(mMyData.arrUserWoman_Send.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;*/

            //  남자 탐색
            case 1:
             //   float Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserMan_Send.get(i).Lat, mMyData.arrUserMan_Send.get(i).Lon);
              //  Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);
                holder.iv_honey_rank.setImageResource(R.drawable.fan_white);

                holder.textView.setText(-1 * (mMyData.arrUserMan_Send_Age.get(i).FanCount / UNIQ_FANCOUNT) +"명");
                Glide.with(mContext)
                        .load(mMyData.arrUserMan_Send_Age.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.image)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            // 여자 탐색
            case 2:
              //  Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserWoman_Send.get(i).Lat, mMyData.arrUserWoman_Send.get(i).Lon,"kilometer");
              //  Log.d("Guide !!!! ", "Case 2 : "+ (int)Dist);
                holder.iv_honey_rank.setImageResource(R.drawable.fan_white);

                holder.textView.setText(-1 * (mMyData.arrUserWoman_Send_Age.get(i).FanCount / UNIQ_FANCOUNT) +"명");
                Glide.with(mContext)
                        .load(mMyData.arrUserWoman_Send_Age.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.image)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            case 0:
            case 3:
                //Log.d("Guide !!!! ", "Case 3");
              //  Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserAll_Send.get(i).Lat, mMyData.arrUserAll_Send.get(i).Lon,"kilometer");

                holder.textView.setText(-1 * (mMyData.arrUserAll_Send_Age.get(i).FanCount / UNIQ_FANCOUNT) +"명");
                holder.iv_honey_rank.setImageResource(R.drawable.fan_white);



                Glide.with(mContext)
                        .load(mMyData.arrUserAll_Send_Age.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.image)
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
            rtValue = Long.valueOf(mMyData.arrUserAll_Send_Age.get(position).Idx);
        } else if (mSetting.getnSearchSetting() == 1) {
            rtValue  = Long.valueOf(mMyData.arrUserMan_Send_Age.get(position).Idx);
        } else if (mSetting.getnSearchSetting() == 2) {
            rtValue = Long.valueOf(mMyData.arrUserWoman_Send_Age.get(position).Idx);
        }
        return rtValue;
    }


    @Override
    public int getItemCount() {
        int rtValue = 0;
   /*    if (mSetting.getnSearchSetting() == 0 || mSetting.getnSearchSetting() == 3) {
            Log.d("Guide !!!! ", "getItem 3");
            rtValue = mMyData.arrUserAll_Send.size();
        }
       else if (mSetting.getnSearchSetting() == 1) {
           Log.d("Guide !!!! ", "getItem 1");
           rtValue = mMyData.arrUserMan_Send.size();
       } else if (mSetting.getnSearchSetting() == 2) {
           Log.d("Guide !!!! ", "getItem 2");
           rtValue = mMyData.arrUserWoman_Send.size();
       }
*/
        if (mSetting.getnSearchSetting() == 0 || mSetting.getnSearchSetting() == 3) {
            //Log.d("Guide !!!! ", "getItem 1");
            rtValue = mMyData.arrUserAll_Send_Age.size();
        } else if (mSetting.getnSearchSetting() == 1) {
            //Log.d("Guide !!!! ", "getItem 3");
            rtValue = mMyData.arrUserMan_Send_Age.size();
        } else if (mSetting.getnSearchSetting() == 2) {
            //Log.d("Guide !!!! ", "getItem 2");
            rtValue = mMyData.arrUserWoman_Send_Age.size();
        }
        return rtValue;
    }

}
