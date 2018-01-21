package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SimpleUserData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.ViewHolder.MyLikeViewHolder;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class LikeAdapter extends RecyclerView.Adapter<LikeViewHolder> {

    private MyData mMyData = MyData.getInstance();

    Context mContext;

    public LikeAdapter(Context context) {
        super();
        mContext = context;
    }
/*
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }
*/

    @Override
    public LikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_image_only,parent,false);
        return new LikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LikeViewHolder holder, int position) {

        String i = mMyData.arrMyFanList.get(position).Idx;


        Glide.with(mContext)
                .load(mMyData.arrMyFanDataList.get(i).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.iv_image);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mMyData.arrMyFanDataList.size();
    }

/*(
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //구현
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.content_user_page_fan_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView FanGrade = (ImageView) convertView.findViewById(R.id.Userpage_Fanlist_Grade) ;
        //ImageView FanRank = (ImageView) convertView.findViewById(R.id.Userpage_Fanlist_Rank) ;
        TextView FanName = (TextView) convertView.findViewById(R.id.Userpage_Fanlist_Name) ;
/*
      Glide.with(mContext)
                .load(arrayList.get(position).)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iconImageView);

        FanName.setText(arrayList.get(position).Nick);

        return convertView;
    }*/
}
