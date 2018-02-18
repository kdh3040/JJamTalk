package com.hodo.talkking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.talkking.Data.MyData;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class LikedAdapter extends RecyclerView.Adapter<LikedViewHolder> {

    private MyData mMyData = MyData.getInstance();

    Context mContext;
    public LikedAdapter(Context context) {
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
    }*/

    @Override
    public LikedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_image_only,parent,false);
        return new LikedViewHolder(view);


    }

    @Override
    public void onBindViewHolder(LikedViewHolder holder, int position) {

        String i = mMyData.arrMyStarList.get(position).Idx;


        Glide.with(mContext)
                .load(mMyData.arrMyStarDataList.get(i).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.iv_liked);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mMyData.arrMyStarDataList.size();
    }
/*
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

        TextView FanName = (TextView) convertView.findViewById(R.id.Userpage_Fanlist_Name) ;

/*        Glide.with(mContext)
                .load(arrayList.get(position).)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iconImageView);

        FanName.setText(arrayList.get(position).Nick);

        return convertView;
    }*/
}