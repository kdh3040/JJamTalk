package com.hodo.talkking;

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

import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Util.CommonFunc;

import github.chenupt.springindicator.SpringIndicator;

import static com.hodo.talkking.R.id.vp;

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

            if (MyData.getInstance().bHotMemberReady == true)
            {
                tabLayout.addTab(tabLayout.newTab().setText("Hot 순"));
            }

            tabLayout.addTab(tabLayout.newTab().setText("팬 보유순"));
            tabLayout.addTab(tabLayout.newTab().setText("가까운 순"));
            tabLayout.addTab(tabLayout.newTab().setText("new 순"));

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

            if (MyData.getInstance().bHotMemberReady == true)
            {
                switch(position){
                    case 0:
                        return new Rank_GoldReceiveFragment();
                    case 1:
                        return new Rank_FanRichFragment();
                    case 2:
                        return new Rank_NearFragment();
                    case 3:
                        return new Rank_NewMemberFragment();
                }
            }

            else
            {
                switch(position){
                    case 0:
                        return new Rank_FanRichFragment();
                    case 1:
                        return new Rank_NearFragment();
                    case 2:
                        return new Rank_NewMemberFragment();
                }
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
