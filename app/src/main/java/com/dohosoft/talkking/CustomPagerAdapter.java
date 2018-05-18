package com.dohosoft.talkking;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Util.CommonFunc;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by mjk on 2017. 8. 16..
 */

public class CustomPagerAdapter extends PagerAdapter{

    private MyData mMyData = MyData.getInstance();
    private UserData stTargetData;

    String[] strImgGroup = new String[4];

    Context mContext;
    LayoutInflater mLayoutInflater;
    private AdView mAdView;

    //int[] mResources = {R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4};
    private PhotoViewAttacher mAttacher;

    private CommonFunc mCommon = CommonFunc.getInstance();

    private ArrayList<String> tempImg = new ArrayList<>();
    private int nStartIdx ;
    public CustomPagerAdapter(Context context, UserData TargetData, int index) {
        mContext = context;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        stTargetData = TargetData;

        if(!stTargetData.ImgGroup0.equals("1"))
            tempImg.add(stTargetData.ImgGroup0);
        if(!stTargetData.ImgGroup1.equals("1"))
            tempImg.add(stTargetData.ImgGroup1);
        if(!stTargetData.ImgGroup2.equals("1"))
            tempImg.add(stTargetData.ImgGroup2);
        if(!stTargetData.ImgGroup3.equals("1"))
            tempImg.add(stTargetData.ImgGroup3);

        nStartIdx = index;
/*
       switch (index)
        {
            case 0:
                strImgGroup[0] = stTargetData.ImgGroup0;
                strImgGroup[1] = stTargetData.ImgGroup1;
                strImgGroup[2] = stTargetData.ImgGroup2;
                strImgGroup[3] = stTargetData.ImgGroup3;
                break;
            case 1:
                strImgGroup[0] = stTargetData.ImgGroup1;
                strImgGroup[1] = stTargetData.ImgGroup0;
                strImgGroup[2] = stTargetData.ImgGroup2;
                strImgGroup[3] = stTargetData.ImgGroup3;
                break;
            case 2:
                strImgGroup[0] = stTargetData.ImgGroup2;
                strImgGroup[1] = stTargetData.ImgGroup0;
                strImgGroup[2] = stTargetData.ImgGroup1;
                strImgGroup[3] = stTargetData.ImgGroup3;
                break;
            case 3:
                strImgGroup[0] = stTargetData.ImgGroup3;
                strImgGroup[1] = stTargetData.ImgGroup0;
                strImgGroup[2] = stTargetData.ImgGroup1;
                strImgGroup[3] = stTargetData.ImgGroup2;
                break;
        }*/


    }

    public void addOnPageChangeListener (ViewPager.OnPageChangeListener listener)
    {

    }


    @Override
    public int getCount() {
        if(MyData.getInstance().IsViewAds() == true)
        {
            return tempImg.size() + 1;
        }
        else
        {
            return tempImg.size();
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item,container,false);
        final ImageView imageView = (ImageView)itemView.findViewById(R.id.imageView);
        mAdView = (AdView)itemView.findViewById(R.id.adView);

        if(MyData.getInstance().IsViewAds() == true)
        {
            if(position == 1)
            {
                CommonFunc.getInstance().ViewAdsBanner(mAdView);
                imageView.setVisibility(View.GONE);
            }
            else if(position == 0)
            {
                imageView.setVisibility(View.VISIBLE);
                mAdView.setVisibility(View.GONE);

                Glide.with(mContext).load(tempImg.get(0))
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                                if (mAttacher != null) {
                                    mAttacher.update();
                                } else {
                                    mAttacher = new PhotoViewAttacher(imageView);
                                    // mAttacher.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                return false;
                            }
                        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }

            else
            {
                imageView.setVisibility(View.VISIBLE);
                mAdView.setVisibility(View.GONE);

                Glide.with(mContext).load(tempImg.get(position-1))
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                                if (mAttacher != null) {
                                    mAttacher.update();
                                } else {
                                    mAttacher = new PhotoViewAttacher(imageView);
                                    // mAttacher.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                                return false;
                            }
                        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }
        }

        else
        {
            imageView.setVisibility(View.VISIBLE);
            mAdView.setVisibility(View.GONE);

            Glide.with(mContext).load(tempImg.get(position))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                            if (mAttacher != null) {
                                mAttacher.update();
                            } else {
                                mAttacher = new PhotoViewAttacher(imageView);
                                // mAttacher.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                            return false;
                        }
                    }).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

        }

            //mCommon.loadInterstitialAd(mContext);







            container.addView(itemView);



        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
