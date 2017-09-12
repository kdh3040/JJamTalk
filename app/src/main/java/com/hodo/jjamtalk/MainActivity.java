package com.hodo.jjamtalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Data.UIData;
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
    ImageButton ib_home,ib_honey,ib_cardList,ib_chatList,ib_board,ib_myPage,ib_fan;
    ImageView iv_refresh,iv_honeybox;
    TextView tv_MainTitle;
    LinearLayout layout_lowbar,layout_topbar;


    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private UIData mUIData = UIData.getInstance();
    private SettingData mSetting = SettingData.getInstance();

    ArrayList<Class> arrFragment = new ArrayList<>();



    @Override
    public void onBackPressed(){
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                String alertTitle = "종료";
                new AlertDialog.Builder(this)
                        .setTitle(alertTitle)
                        .setMessage("프로그램을 종료하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
        }
        return super.onKeyDown(keyCode, event);
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

        mUIData.setHeight(height);
        mUIData.setWidth(width);
        mUIData.setLlp_ListItem(new LinearLayout.LayoutParams(width,height/7));

        //int mHeight = mUIData.getHeight();
        int mWidth = mUIData.getWidth();
        int mHeight = mUIData.getHeight();

        Toast.makeText(getApplicationContext(),"width: "+width+"height: "+ height,Toast.LENGTH_LONG).show();

        iv_honeybox = (ImageView)findViewById(R.id.iv_honeybox);
        iv_honeybox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MailboxActivity.class));

            }
        });



        viewPager = (ScrollerViewPager)findViewById(R.id.view_pager);
        springIndicator = (SpringIndicator)findViewById(R.id.indicator);



        iv_refresh = (ImageView)findViewById(R.id.iv_refresh);






        tv_MainTitle = (TextView)findViewById(R.id.tv_maintitle);


        ib_myPage = (ImageButton)findViewById(R.id.ib_mypage);

        ib_myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyPageActivity.class));
                overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

            }
        });


        layout_topbar = (LinearLayout)findViewById(R.id.layout_topbar);




        layout_lowbar = (LinearLayout)findViewById(R.id.layout_lowbar);





        ib_board = (ImageButton)findViewById(R.id.ib_board);

        ib_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BoardActivity.class));
                overridePendingTransition(R.anim.rightin_activity,R.anim.not_move_activity);



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
        ib_fan = (ImageButton)findViewById(R.id.ib_fan);

        ib_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),FanActivity.class));
         /*       Intent intent = new Intent(getApplicationContext(), FanActivity.class);
                intent.putExtra("ViewMode", 0);
                startActivity(intent);
*/
                Intent intent = new Intent(getApplicationContext(), FanActivity.class);
                Bundle bundle = new Bundle();
  /*              bundle.putSerializable("Target", stTargetData);
                intent.putExtra("FanList", stTargetData.arrFanList);
                intent.putExtra("StarList", stTargetData.arrStarList);*/
                intent.putExtra("ViewMode", 0);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });




        PagerModelManager manager = new PagerModelManager();
        manager.addFragment(new Rank_NearFragment(),"가까운 순");



        manager.addFragment(new Rank_HoneyReceiveFragment(),"실시간 인기 순");
        manager.addFragment(new Rank_RichFragment(),"팬 보유 순");
        manager.addFragment(new Rank_NewMemberFragment(),"새로운 순");

        final ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();
        springIndicator.setViewPager(viewPager);
    }



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
