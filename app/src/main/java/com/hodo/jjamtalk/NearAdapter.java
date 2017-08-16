package com.hodo.jjamtalk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.Util.LocationFunc;
import com.hodo.jjamtalk.ViewHolder.GridUserViewHolder;

import java.util.ArrayList;

import static com.hodo.jjamtalk.R.mipmap.girl1;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class NearAdapter extends RecyclerView.Adapter<GridUserViewHolder> {
    Context mContext;
    public UserData stTargetData = new UserData();
    private ArrayList<UserData> arrTargetData_Man = new ArrayList<>();
    private ArrayList<UserData> arrTargetData_Woman = new ArrayList<>();
    private ArrayList<UserData> arrTargetData_All = new ArrayList<>();

    private SettingData mSetting = SettingData.getInstance();
    private LocationFunc mLocFunc = LocationFunc.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();

    public NearAdapter(Context context) {
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
       // holder.textView.setText("아이유, 25, 20km");

       // holder.imageView.setImageResource(R.mipmap.girl1);

        int i = position;

        switch (mSetting.getnSearchSetting())
        {
            //  남자 탐색
            case 1:
                float Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserMan_Near.get(i).Lat, mMyData.arrUserMan_Near.get(i).Lon);
                Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);
                holder.textView.setText(mMyData.arrUserMan_Near.get(i).NickName + ", " + mMyData.arrUserMan_Near.get(i).Age + "세, " + (int)Dist + "km");
                Glide.with(mContext)
                        .load(mMyData.arrUserMan_Near.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.imageView);

                stTargetData = mMyData.arrUserMan_Near.get(i);
                arrTargetData_Man.add(stTargetData);
                break;
            // 여자 탐색
            case 2:
                Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserWoman_Near.get(i).Lat, mMyData.arrUserWoman_Near.get(i).Lon);
                Log.d("Guide !!!! ", "Case 2 : "+ (int)Dist);
                holder.textView.setText(mMyData.arrUserWoman_Near.get(i).NickName + ", " + mMyData.arrUserWoman_Near.get(i).Age + "세, " + (int)Dist + "km");
                Glide.with(mContext)
                        .load(mMyData.arrUserWoman_Near.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.imageView);

                stTargetData = mMyData.arrUserWoman_Near.get(i);
                arrTargetData_Woman.add(stTargetData);
                break;
            case 3:
                Log.d("Guide !!!! ", "Case 3");
                Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserAll_Near.get(i).Lat, mMyData.arrUserAll_Near.get(i).Lon);
                holder.textView.setText(mMyData.arrUserAll_Near.get(i).NickName + ", " + mMyData.arrUserAll_Near.get(i).Age + "세, " + (int)Dist + "km");
                Glide.with(mContext)
                        .load(mMyData.arrUserAll_Near.get(i).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(holder.imageView);

                stTargetData = mMyData.arrUserAll_Near.get(i);
                arrTargetData_All.add(stTargetData);
                break;
            default:
                break;
        }


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAppStatus.bCheckMultiSend == false) {
                    switch (mSetting.getnSearchSetting())
                    {
                        case 1:
                            stTargetData = arrTargetData_Man.get(position);
                            break;
                        case 2:
                            stTargetData = arrTargetData_Woman.get(position);
                            break;
                        case 3:
                            stTargetData = arrTargetData_All.get(position);
                            break;
                    }

                    Log.d("Guide !!!! ", "Start : " + position);
                    Intent intent = new Intent(mContext, UserPageActivity.class);
                    intent.putExtra("Target", stTargetData);
                    mContext.startActivity(intent);
                }
                //mContext.startActivity(new Intent(mContext,UserPageActivity.class));
            }
        });


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
