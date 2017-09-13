package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.Data.MyData;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

/**
 * Created by mjk on 2017. 9. 13..
 */

public class HomeFragment extends Fragment {

    SpringIndicator springIndicator;
    ScrollerViewPager viewPager;
    View fragView;

/*    private static HomeFragment _Instance;
    public static HomeFragment getInstance() {
        if (_Instance == null)
            _Instance = new HomeFragment();

        return _Instance;
    }
    private HomeFragment() {}*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (fragView!= null) {

        }
        else
        {
            fragView = inflater.inflate(R.layout.fragment_home,container,false);
            viewPager = (ScrollerViewPager)fragView.findViewById(R.id.view_pager);
            springIndicator = (SpringIndicator)fragView.findViewById(R.id.indicator);

            PagerModelManager manager = new PagerModelManager();

            manager.addFragment(new Rank_NearFragment(),"가까운 순");
            manager.addFragment(new Rank_HoneyReceiveFragment(),"실시간 인기 순");
            manager.addFragment(new Rank_RichFragment(),"팬 보유 순");
            manager.addFragment(new Rank_NewMemberFragment(),"새로운 순");

            final ModelPagerAdapter adapter = new ModelPagerAdapter(getFragmentManager(),manager);
            viewPager.setAdapter(adapter);
            viewPager.fixScrollSpeed();
            springIndicator.setViewPager(viewPager);

        }


        return fragView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 데이터 저장
        outState.putInt("data", 1);
    }

}
