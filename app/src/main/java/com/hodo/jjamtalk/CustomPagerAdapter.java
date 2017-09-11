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

/**
 * Created by mjk on 2017. 8. 16..
 */

public class CustomPagerAdapter extends PagerAdapter{

    private MyData mMyData = MyData.getInstance();

    Context mContext;
    LayoutInflater mLayoutInflater;
    int[] mResources = {R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4};

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mMyData.arrImgList.size();
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
                .load(mMyData.arrImgList.get(position))
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
