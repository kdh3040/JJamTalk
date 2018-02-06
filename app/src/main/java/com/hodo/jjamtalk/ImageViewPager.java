package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hodo.jjamtalk.Data.UserData;

/**
 * Created by mjk on 2017. 8. 16..
 */

public class ImageViewPager extends AppCompatActivity {

    private UserData stTargetData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview_pager);

        Bundle bundle = getIntent().getExtras();
        stTargetData = (UserData)bundle.getSerializable("Target");

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this, stTargetData);

        Custom_ViewPager mViewPager = (Custom_ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }
}
