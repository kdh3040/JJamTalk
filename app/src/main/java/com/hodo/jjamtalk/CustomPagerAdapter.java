package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;

/**
 * Created by mjk on 2017. 8. 16..
 */

public class CustomPagerAdapter extends PagerAdapter{

    private MyData mMyData = MyData.getInstance();
    private UserData stTargetData;

    String[] strImgGroup = new String[4];

    Context mContext;
    LayoutInflater mLayoutInflater;
    int[] mResources = {R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4};

    public CustomPagerAdapter(Context context, UserData TargetData) {
        mContext = context;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        stTargetData = TargetData;

        strImgGroup[0] = stTargetData.ImgGroup1;
        strImgGroup[1] = stTargetData.ImgGroup2;
        strImgGroup[2] = stTargetData.ImgGroup3;
        strImgGroup[3] = stTargetData.ImgGroup4;

    }

    @Override
    public int getCount() {
        return stTargetData.ImgCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item,container,false);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.imageView);


            Glide.with(mContext)
                    //.load(mMyData.strProfileImg[position])
                    .load(strImgGroup[position])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(imageView);


        container.addView(itemView);

        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
