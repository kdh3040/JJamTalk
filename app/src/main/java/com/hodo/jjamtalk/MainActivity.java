package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.common.collect.Lists;

import java.util.List;


import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class MainActivity extends AppCompatActivity {
    SpringIndicator springIndicator;
    ScrollerViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        viewPager = (ScrollerViewPager)findViewById(R.id.view_pager);
        springIndicator = (SpringIndicator)findViewById(R.id.indicator);

        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(GuideFragment.class, getBgRes(),getTitles());

        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);

        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();
        springIndicator.setViewPager(viewPager);


    }
    private List<String> getTitles(){
        return Lists.newArrayList("Near", "Hot", "Rank", "New");
    }

    private List<Integer> getBgRes(){
        return Lists.newArrayList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View menu_Main  = inflater.inflate(R.layout.menu_main,null);
        actionBar.setCustomView(menu_Main);
        Toolbar parent = (Toolbar)menu_Main.getParent();
        parent.setContentInsetsAbsolute(0,0);

        Button button = (Button)findViewById(R.id.btn_profile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyPageActivity.class));
            }
        });

        return true;
    }
}
