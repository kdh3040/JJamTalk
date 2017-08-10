package com.hodo.jjamtalk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.Util.LocationFunc;
import com.hodo.jjamtalk.ViewHolder.GridUserViewHolder;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class NewMemberAdapter extends RecyclerView.Adapter<GridUserViewHolder> {

    Context mContext;
    private SettingData mSetting = SettingData.getInstance();
    private LocationFunc mLocFunc = LocationFunc.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    
    public NewMemberAdapter(Context context) {
        mContext = context;
    }

    @Override
    public GridUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_user,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAppStatus.bCheckMultiSend == false)
                    mContext.startActivity(new Intent(mContext,UserPageActivity.class));
            }
        });

        return new GridUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridUserViewHolder holder, int position) {
        //holder.textView.setText("뉴멤버, 25, 20km");
        holder.imageView.setImageResource(R.mipmap.ic_launcher);

        int i = position;

        switch (mSetting.getnSearchSetting())
        {
            //  남자 탐색
            case 1:
                float Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserMan_New.get(i).Lat, mMyData.arrUserMan_New.get(i).Lon);
                Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);
                holder.textView.setText(mMyData.arrUserMan_New.get(i).NickName + ", " + mMyData.arrUserMan_New.get(i).Age + "세, " + (int)Dist + "km");
                break;
            // 여자 탐색
            case 2:
                Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserWoman_New.get(i).Lat, mMyData.arrUserWoman_New.get(i).Lon);
                Log.d("Guide !!!! ", "Case 2 : "+ (int)Dist);
                holder.textView.setText(mMyData.arrUserWoman_New.get(i).NickName + ", " + mMyData.arrUserWoman_New.get(i).Age + "세, " + (int)Dist + "km");
                break;
            case 3:
                Log.d("Guide !!!! ", "Case 3");
                Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserAll_New.get(i).Lat, mMyData.arrUserAll_New.get(i).Lon);
                holder.textView.setText(mMyData.arrUserAll_New.get(i).NickName + ", " + mMyData.arrUserAll_New.get(i).Age + "세, " + (int)Dist + "km");
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
            rtValue = mMyData.arrUserMan_New.size();
        } else if (mSetting.getnSearchSetting() == 2) {
            Log.d("Guide !!!! ", "getItem 2");
            rtValue = mMyData.arrUserWoman_New.size();
        } else if (mSetting.getnSearchSetting() == 3) {
            Log.d("Guide !!!! ", "getItem 3");
            rtValue = mMyData.arrUserAll_New.size();
        }
        return rtValue;
    }
}
