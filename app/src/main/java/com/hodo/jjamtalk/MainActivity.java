package com.hodo.jjamtalk;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.AppStatus;

import java.util.ArrayList;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class MainActivity extends AppCompatActivity {
    SpringIndicator springIndicator;
    ScrollerViewPager viewPager;
    ImageButton ib_home,ib_honey,ib_cardList,ib_chatList,ib_board;
    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();

    ArrayList<Class> arrFragment = new ArrayList<>();

    @Override
    public void onBackPressed(){
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Toast.makeText(getApplicationContext(),"width: "+width+"height: "+ height,Toast.LENGTH_LONG).show();



        viewPager = (ScrollerViewPager)findViewById(R.id.view_pager);
        springIndicator = (SpringIndicator)findViewById(R.id.indicator);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width/5,LinearLayout.LayoutParams.MATCH_PARENT);

        ib_home = (ImageButton) findViewById(R.id.ib_home);
        ib_home.setLayoutParams(lp);

        //ib_home.setLayoutParams(params);
        ib_board = (ImageButton)findViewById(R.id.ib_board);
        ib_board.setLayoutParams(lp);
        ib_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BoardActivity.class));

            }
        });
        ib_cardList = (ImageButton)findViewById(R.id.ib_cardlist);
        ib_cardList.setLayoutParams(lp);
        ib_cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CardListActivity.class));

            }
        });
        ib_chatList = (ImageButton)findViewById(R.id.ib_chatlist);
        ib_chatList.setLayoutParams(lp);
        ib_chatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChatListActivity.class));

            }
        });
        ib_honey = (ImageButton)findViewById(R.id.ib_honey);
        ib_honey.setLayoutParams(lp);
        ib_honey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HoneyActivity.class));
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

    /*private List<String> getTitles(){
        return Lists.newArrayList("Near", "Hot", "Rank", "New");
    }

    private List<Integer> getBgRes(){
        return Lists.newArrayList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
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

        CheckBox cbMultiSend = (CheckBox)findViewById(R.id.checkBox);
        cbMultiSend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mAppStatus.bCheckMultiSend = isChecked;
            }
        });

        return true;
    }
}
