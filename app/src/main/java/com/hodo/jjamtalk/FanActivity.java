package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class FanActivity extends AppCompatActivity {
    SpringIndicator indicator;
    ScrollerViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);
        indicator = (SpringIndicator) findViewById(R.id.indicator_fan);

        viewPager = (ScrollerViewPager) findViewById(R.id.vp_fan);
        PagerModelManager manager = new PagerModelManager();

        manager.addFragment(new MyFanFragment(),"내 팬");
        manager.addFragment(new MyLikeFragment(),"내가 좋아하는");

        final ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);


    }
}
