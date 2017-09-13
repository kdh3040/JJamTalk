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


    public HomeFragment() {

    }

    @Nullable
    @Override


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_home,container,false);
        tabLayout = fragView.findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("가까운 순"));
        tabLayout.addTab(tabLayout.newTab().setText("실시간 인기순"));
        tabLayout.addTab(tabLayout.newTab().setText("팬 보유순"));
        tabLayout.addTab(tabLayout.newTab().setText("뉴페이스"));

        viewPager = (ViewPager)fragView.findViewById(vp);



        /*View.OnClickListener movePageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int tag = (int) v.getTag();
                viewPager.setCurrentItem(tag);
            }
        };*/



        /*tv_near =(TextView)fragView.findViewById(R.id.tv_near);
        //tv_new.setOnClickListener(movePageListener);
        //tv_near.setTag(0);
        tv_hot =(TextView)fragView.findViewById(R.id.tv_hot);
        //tv_new.setOnClickListener(movePageListener);
        //tv_hot.setTag(1);
        tv_fan =(TextView)fragView.findViewById(R.id.tv_fan_many);
        //tv_new.setOnClickListener(movePageListener);
        //tv_fan.setTag(2);
        tv_new =(TextView)fragView.findViewById(R.id.tv_new);
        //tv_new.setOnClickListener(movePageListener);
        //tv_new.setTag(3);*/



        //springIndicator = (SpringIndicator)fragView.findViewById(R.id.indicator);

        /*PagerModelManager manager = new PagerModelManager();

        manager.addFragment(new Rank_NearFragment(),"가까운 순");
        manager.addFragment(new Rank_HoneyReceiveFragment(),"실시간 인기 순");
        manager.addFragment(new Rank_RichFragment(),"팬 보유 순");
        manager.addFragment(new Rank_NewMemberFragment(),"새로운 순");*/


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
        //viewPager.fixScrollSpeed();
        //springIndicator.setViewPager(viewPager);
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
