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
import com.hodo.talkking.ViewHolder.GridUserViewHolder;

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
                    mContext.startActivity(new_img Intent(mContext,UserPageActivity.class));*/
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

        RelativeLayout.LayoutParams lpForBgTxt  = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)((mUIData.getWidth()/mSetting.getViewCount())*0.2));
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        holder.bg_tv.setLayoutParams(lpForBgTxt);


        //holder.textView.setText("100만 꿀");
        //holder.iv_honey_rank.setVisibility(View.INVISIBLE);

        //Log.d("Guide !!!! ", "Start");
        int i = position;

        switch (mSetting.getnSearchSetting())
        {
          /*  case 0:
            case 3:
                //holder.textView.setText(-1*mMyData.arrUserAll_Recv.get(i).RecvCount+"개");
                holder.textView.setText(mMyData.arrUserAll_Recv.get(i).NickName);//+ " " + mMyData.arrUserAll_Recv.get(i).Age+"세");
                Glide.with(mContext)
                        .load(mMyData.arrUserAll_Recv.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            //  남자 탐색
            case 1:

                //holder.textView.setText(-1*mMyData.arrUserMan_Recv.get(i).RecvCount+"개");
                holder.textView.setText(mMyData.arrUserMan_Recv.get(i).NickName);// + " " + mMyData.arrUserMan_Recv.get(i).Age+"세");
                Glide.with(mContext)
                        .load(mMyData.arrUserMan_Recv.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            // 여자 탐색
            case 2:
                //holder.textView.setText(-1*mMyData.arrUserWoman_Recv.get(i).RecvCount+"개");
                holder.textView.setText(mMyData.arrUserWoman_Recv.get(i).NickName);// + " " + mMyData.arrUserWoman_Recv.get(i).Age+"세");
                Glide.with(mContext)
                        .load(mMyData.arrUserWoman_Recv.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
*/

            case 1:
                //   float Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserMan_Send.get(i).Lat, mMyData.arrUserMan_Send.get(i).Lon);
                //  Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);
                holder.iv_honey_rank.setImageResource(R.drawable.heart_white);
                String str = String.format("%,d", mMyData.arrUserMan_Recv_Age.get(i).RecvGold * -1);

                holder.textView.setText(str);
                Glide.with(mContext)
                        .load(mMyData.arrUserMan_Recv_Age.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            // 여자 탐색
            case 2:
                //  Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserWoman_Send.get(i).Lat, mMyData.arrUserWoman_Send.get(i).Lon,"kilometer");
                //  Log.d("Guide !!!! ", "Case 2 : "+ (int)Dist);
                holder.iv_honey_rank.setImageResource(R.drawable.heart_white);

                str = String.format("%,d", mMyData.arrUserWoman_Recv_Age.get(i).RecvGold * -1);

                holder.textView.setText(str);

                Glide.with(mContext)
                        .load(mMyData.arrUserWoman_Recv_Age.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.iv_profile);
                break;
            case 0:
            case 3:
                //Log.d("Guide !!!! ", "Case 3");
                //  Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserAll_Send.get(i).Lat, mMyData.arrUserAll_Send.get(i).Lon,"kilometer");
                holder.iv_honey_rank.setImageResource(R.drawable.heart_white);

                str = String.format("%,d", mMyData.arrUserAll_Recv_Age.get(i).RecvGold * -1);
                holder.textView.setText(str);

                Glide.with(mContext)
                        .load(mMyData.arrUserAll_Recv_Age.get(i).Img)
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
            rtValue = Long.valueOf(mMyData.arrUserAll_Recv_Age.get(position).Idx);
        } else if (mSetting.getnSearchSetting() == 1) {
            rtValue  = Long.valueOf(mMyData.arrUserMan_Recv_Age.get(position).Idx);
        } else if (mSetting.getnSearchSetting() == 2) {
            rtValue = Long.valueOf(mMyData.arrUserWoman_Recv_Age.get(position).Idx);
        }
        return rtValue;
    }

    @Override
    public int getItemCount() {

        int rtValue = 0;

     /*   if (mSetting.getnSearchSetting() == 0 || mSetting.getnSearchSetting() == 3) {
            Log.d("Guide !!!! ", "getItem 3");
            rtValue = mMyData.arrUserAll_Recv.size();
        }
        else if (mSetting.getnSearchSetting() == 1) {
            Log.d("Guide !!!! ", "getItem 1");
            rtValue = mMyData.arrUserMan_Recv.size();
        } else if (mSetting.getnSearchSetting() == 2) {
            Log.d("Guide !!!! ", "getItem 2");
            rtValue = mMyData.arrUserWoman_Recv.size();
        }*/
        if (mSetting.getnSearchSetting() == 0 || mSetting.getnSearchSetting() == 3) {
            Log.d("Guide !!!! ", "getItem 3");
            rtValue = mMyData.arrUserAll_Recv_Age.size();
        }
        else if (mSetting.getnSearchSetting() == 1) {
            Log.d("Guide !!!! ", "getItem 1");
            rtValue = mMyData.arrUserMan_Recv_Age.size();
        } else if (mSetting.getnSearchSetting() == 2) {
            Log.d("Guide !!!! ", "getItem 2");
            rtValue = mMyData.arrUserWoman_Recv_Age.size();
        }
        return rtValue;
    }
}
