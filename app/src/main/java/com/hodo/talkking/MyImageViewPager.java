package com.hodo.talkking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Util.CommonFunc;

/**
 * Created by mjk on 2017. 8. 16..
 */

public class MyImageViewPager extends AppCompatActivity {

    private UserData stTargetData;
    private CommonFunc mCommon = CommonFunc.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.imageview_ad);
        //loadInterstitialAd();

        setContentView(R.layout.imageview_pager);

        Bundle bundle = getIntent().getExtras();
        stTargetData = (UserData)bundle.getSerializable("Target");
        int index = (int)bundle.getSerializable("Index");

        MyCustomPagerAdapter mCustomPagerAdapter = new MyCustomPagerAdapter(this, stTargetData, index);

        Custom_ViewPager mViewPager = (Custom_ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
/*        mViewPager.addOnPageChangeListener(new_img ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int aaa = 0;
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1)
                    mCommon.loadInterstitialAd(getApplicationContext());
                int aaa = 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

    }


}
