package com.dohosoft.talkking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Util.CommonFunc;

/**
 * Created by mjk on 2017. 8. 16..
 */

public class ImageViewPager extends AppCompatActivity {

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

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this, stTargetData, index);

        Custom_ViewPager mViewPager = (Custom_ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

    }


}
