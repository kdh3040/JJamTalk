package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;

import org.json.JSONException;
import org.json.JSONObject;




import java.util.ArrayList;

import com.google.common.collect.Lists;

import java.util.List;



import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class MainActivity extends AppCompatActivity {
    SpringIndicator springIndicator;
    ScrollerViewPager viewPager;
    ImageButton ib_cardList,ib_chatList,ib_board;
    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private MyData mMyData = MyData.getInstance();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);


        viewPager = (ScrollerViewPager)findViewById(R.id.view_pager);
        springIndicator = (SpringIndicator)findViewById(R.id.indicator);
        ib_board = (ImageButton)findViewById(R.id.ib_board);
        ib_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BoardActivity.class));
            }
        });
        ib_cardList = (ImageButton)findViewById(R.id.ib_cardlist);
        ib_cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CardListActivity.class));
            }
        });
        ib_chatList = (ImageButton)findViewById(R.id.ib_chatlist);
        ib_chatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChatListActivity.class));
            }
        });
        //m_GridView = (GridView)findViewById(R.id.main_gridview);

        PagerModelManager manager = new PagerModelManager();
        manager.addFragment(new NearFragment(),"Near");
        manager.addFragment(new NewMemberFragment(),"New");
        manager.addFragment(new HotFragment(),"Hot");
        manager.addFragment(new RankFragment(),"Rank");

        final ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);
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

        ImageButton button = (ImageButton)findViewById(R.id.iv_mypage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyPageActivity.class));
            }
        });

        return true;
    }
}
