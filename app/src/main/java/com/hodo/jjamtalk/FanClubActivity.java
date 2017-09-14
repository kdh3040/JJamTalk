package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.UserData;

import java.util.ArrayList;

/**
 * Created by mjk on 2017. 9. 14..
 */

public class FanClubActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    private UserData stTargetData;
    public ArrayList<FanData> FanList = new ArrayList<>();
    public ArrayList<UserData> FanData = new ArrayList<>();

    public ArrayList<FanData> StarList = new ArrayList<>();
    public ArrayList<UserData> StarData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_club);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        Intent intent = getIntent();
        Bundle bundle = this.getIntent().getExtras();
        stTargetData = (UserData) bundle.getSerializable("Target");


        tabLayout.addTab(tabLayout.newTab().setText("팬클럽"));
        tabLayout.addTab(tabLayout.newTab().setText("가입한 팬클럽"));

        viewPager = (ViewPager)findViewById(R.id.vp);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount()));
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }
    private class TabPagerAdapter extends FragmentStatePagerAdapter {
        private  int tabCount;
        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount =tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new TargetFanFragment(stTargetData.arrFanList);
                case 1:
                    return new TargetLikeFragment(stTargetData.arrStarList);
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
