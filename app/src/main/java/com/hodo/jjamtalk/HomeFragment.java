package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import github.chenupt.springindicator.SpringIndicator;

import static com.hodo.jjamtalk.R.id.vp;

/**
 * Created by mjk on 2017. 9. 13..
 */

public class HomeFragment extends Fragment {

    SpringIndicator springIndicator;
    ViewPager viewPager;
    TextView tv_near,tv_hot,tv_fan,tv_new;
    private TabLayout tabLayout;
    View fragView;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fragView!= null) {

        }
        else
        {
            fragView = inflater.inflate(R.layout.fragment_home,container,false);
            tabLayout = fragView.findViewById(R.id.tabLayout);

            tabLayout.addTab(tabLayout.newTab().setText("가까운 순"));
            tabLayout.addTab(tabLayout.newTab().setText("실시간 인기순"));
            tabLayout.addTab(tabLayout.newTab().setText("팬 보유순"));
            tabLayout.addTab(tabLayout.newTab().setText("뉴페이스"));

            viewPager = (ViewPager)fragView.findViewById(vp);

            viewPager.setAdapter(new TabPagerAdapter(getFragmentManager(),tabLayout.getTabCount()));
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


        return fragView;
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter {
        private  int tabCount;
        public TabPagerAdapter(FragmentManager fm,int tabCount) {
            super(fm);
            this.tabCount =tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new Rank_NearFragment();
                case 1:
                    return new Rank_HoneyReceiveFragment();
                case 2:
                    return new Rank_RichFragment();
                case 3:
                    return new Rank_NewMemberFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
