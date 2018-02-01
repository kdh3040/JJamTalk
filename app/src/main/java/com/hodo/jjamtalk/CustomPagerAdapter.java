package com.hodo.jjamtalk;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.CommonFunc;

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

    int[] mResources = {R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4};
    private PhotoViewAttacher mAttacher;


    private CommonFunc mCommon = CommonFunc.getInstance();

    public CustomPagerAdapter(Context context, UserData TargetData) {
        mContext = context;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        stTargetData = TargetData;

        strImgGroup[0] = stTargetData.ImgGroup0;
        strImgGroup[1] = stTargetData.ImgGroup1;
        strImgGroup[2] = stTargetData.ImgGroup2;
        strImgGroup[3] = stTargetData.ImgGroup3;

    }

    public void addOnPageChangeListener (ViewPager.OnPageChangeListener listener)
    {

    }


    @Override
    public int getCount() {
       return stTargetData.ImgCount + 1;
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

        if(position == 1)
        {
            mAdView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);


            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    int aaa = 0;
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                    int aaa = 0;
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                    int aaa = 0;
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                    int aaa = 0;
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the user is about to return
                    // to the app after tapping on an ad.
                    int aaa = 0;
                }
            });
        }
        else if(position == 0)
        {
            imageView.setVisibility(View.VISIBLE);
            mAdView.setVisibility(View.GONE);

            Glide.with(mContext).load(strImgGroup[position])
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

            Glide.with(mContext).load(strImgGroup[position-1])
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
