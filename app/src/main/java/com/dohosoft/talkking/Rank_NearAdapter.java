package com.dohosoft.talkking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SettingData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Util.AppStatus;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.LocationFunc;
import com.dohosoft.talkking.ViewHolder.GridUserViewHolder;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class Rank_NearAdapter extends RecyclerView.Adapter<GridUserViewHolder> {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user, parent, false);
      /*  view.setOnClickListener(new_img View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAppStatus.bCheckMultiSend == false) {

                    Intent intent = new_img Intent(mContext, UserPageActivity.class);
                    intent.putExtra("Target", stTargetData);
                    mContext.startActivity(intent);
                }
                    //mContext.startActivity(new_img Intent(mContext,UserPageActivity.class));
            }
        });
*/


        return new GridUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridUserViewHolder holder, final int position) {


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mUIData.getWidth() / mSetting.getViewCount(), (mUIData.getWidth() / mSetting.getViewCount()));
        holder.iv_profile.setLayoutParams(lp);
        //lp.setMargins(3,3,3,3);
        RelativeLayout.LayoutParams lpForTextView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) ((mUIData.getWidth() / mSetting.getViewCount()) * 0.2));
        lpForTextView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForTextView.addRule(RelativeLayout.RIGHT_OF, R.id.iv_honey_rank);
        holder.textView.setLayoutParams(lpForTextView);

        holder.iv_honey_rank.setImageResource(R.drawable.location_white);
        RelativeLayout.LayoutParams lpForIcon = new RelativeLayout.LayoutParams((int) (mUIData.getWidth() / mSetting.getViewCount() * 0.2), (int) (mUIData.getWidth() / mSetting.getViewCount() * 0.2));
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //온라인표시
        RelativeLayout.LayoutParams lpForIvOnline = new RelativeLayout.LayoutParams((int) (mUIData.getWidth() / mSetting.getViewCount() * 0.2), (int) (mUIData.getWidth() / mSetting.getViewCount() * 0.2));
        lpForIvOnline.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpForIvOnline.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        holder.iv_online.setLayoutParams(lpForIvOnline);


        holder.iv_rank.setVisibility(View.GONE);
        holder.iv_honey_rank.setLayoutParams(lpForIcon);

        RelativeLayout.LayoutParams lpForBgTxt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) ((mUIData.getWidth() / mSetting.getViewCount()) * 0.2));
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        holder.bg_tv.setLayoutParams(lpForBgTxt);


        int i = position;

        if ((mMyData.arrUserAll_Near_Age.get(i).Dist / 1000) < 1.0)
            holder.textView.setText(/*mMyData.arrUserAll_Near.get(i).NickName + ", " + mMyData.arrUserAll_Near.get(i).Age + "세, " + */"1km 이내");
        else
            holder.textView.setText(/*mMyData.arrUserAll_Near.get(i).NickName + ", " + mMyData.arrUserAll_Near.get(i).Age + "세, " + */(int) (mMyData.arrUserAll_Near_Age.get(i).Dist / 1000) + "km");

        String tempUserConnTime =CommonFunc.getInstance().GetUserDate(mMyData.arrUserAll_Near_Age.get(i).ConnectDate);
        if(tempUserConnTime.equals("접속중"))
        {
            holder.textWhen.setVisibility(View.GONE);
            holder.iv_online.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.iv_online.setVisibility(View.GONE);

            holder.textWhen.setVisibility(View.VISIBLE);
            holder.textWhen.setText(tempUserConnTime);
        }


        //holder.textWhen.setText(CommonFunc.getInstance().GetUserDate(mMyData.arrUserAll_Near_Age.get(i).ConnectDate));

        Glide.with(mContext)
                .load(mMyData.arrUserAll_Near_Age.get(i).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image)
                .thumbnail(0.1f)
                .into(holder.iv_profile);


    }

    @Override
    public long getItemId(int position) {
        long rtValue = 0;
        rtValue = Long.valueOf(mMyData.arrUserAll_Near_Age.get(position).Idx);

        return rtValue;
    }

    @Override
    public int getItemCount() {
        int rtValue = 0;

        rtValue = mMyData.arrUserAll_Near_Age.size();

        return rtValue;
    }
}
