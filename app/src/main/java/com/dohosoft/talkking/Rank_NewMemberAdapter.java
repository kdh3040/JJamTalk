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
import com.dohosoft.talkking.Util.AppStatus;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.LocationFunc;
import com.dohosoft.talkking.ViewHolder.GridUserViewHolder;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user, parent, false);
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
        holder.iv_profile.setLayoutParams(new RelativeLayout.LayoutParams(mUIData.getWidth() / mSetting.getViewCount(), mUIData.getWidth() / mSetting.getViewCount()));

        RelativeLayout.LayoutParams lpForTextView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) ((mUIData.getWidth() / mSetting.getViewCount()) * 0.2));
        lpForTextView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForTextView.addRule(RelativeLayout.RIGHT_OF, R.id.iv_honey_rank);
        holder.textView.setLayoutParams(lpForTextView);

        holder.iv_rank.setVisibility(View.GONE);

        RelativeLayout.LayoutParams lpForIcon = new RelativeLayout.LayoutParams((int) (mUIData.getWidth() / mSetting.getViewCount() * 0.2), (int) (mUIData.getWidth() / mSetting.getViewCount() * 0.2));
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


        holder.iv_honey_rank.setLayoutParams(lpForIcon);
        holder.iv_honey_rank.setImageResource(R.drawable.new_img);

        RelativeLayout.LayoutParams lpForBgTxt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) ((mUIData.getWidth() / mSetting.getViewCount()) * 0.2));
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpForBgTxt.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        holder.bg_tv.setLayoutParams(lpForBgTxt);


        int i = position;

        holder.textWhen.setText(CommonFunc.getInstance().GetUserDate(mMyData.arrUserAll_New_Age.get(i).ConnectDate));


        //Log.d("Guide !!!! ", "Case 3");
        holder.textView.setText("(" + mMyData.arrUserAll_New_Age.get(i).Age + "세)" + mMyData.arrUserAll_New_Age.get(i).NickName);
        Glide.with(mContext)
                .load(mMyData.arrUserAll_New_Age.get(i).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image)
                .thumbnail(0.1f)
                .into(holder.iv_profile);

    }

    @Override
    public long getItemId(int position) {
        long rtValue = 0;
        rtValue = Long.valueOf(mMyData.arrUserAll_New_Age.get(position).Idx);

        return rtValue;
    }

    @Override
    public int getItemCount() {
        int rtValue = 0;

        rtValue = mMyData.arrUserAll_New_Age.size();
        return rtValue;
    }
}
