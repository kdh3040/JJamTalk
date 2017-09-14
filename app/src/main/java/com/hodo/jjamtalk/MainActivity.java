package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.AppStatus;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton ib_home,ib_honey,ib_cardList,ib_chatList,ib_board,ib_myPage,ib_fan,ib_pcr_open;
    ImageView iv_refresh,iv_honeybox;
    TextView tv_MainTitle;
    LinearLayout layout_lowbar,layout_topbar;
    BoardFragment boardFragment;
    Activity mActivity;


    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private UIData mUIData = UIData.getInstance();
    private SettingData mSetting = SettingData.getInstance();

    ArrayList<Class> arrFragment = new ArrayList<>();
    private CardListFragment cardListFragment;
    private ChatListFragment chatListFragment;
    private FanFragment fanFragment;
    private HomeFragment homeFragment;// = HomeFragment.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        mActivity = this;

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
        homeFragment = new HomeFragment();



        Toast.makeText(getApplicationContext(),"width: "+width+"height: "+ height,Toast.LENGTH_LONG).show();
        ib_pcr_open = (ImageButton)findViewById(R.id.ib_pcr_open);
        ib_pcr_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_pcr_open,null);

                final RadioButton btn_Member_50 =  v.findViewById(R.id.member_50);
                final RadioButton btn_Member_100 =  v.findViewById(R.id.member_100);
                final RadioButton btn_Member_200 =  v.findViewById(R.id.member_200);

                final int[] publicRoomMemberCnt = new int[1];

                final RadioButton btn_time_30 =  v.findViewById(R.id.time_30);
                final RadioButton btn_time_60 =  v.findViewById(R.id.time_60);
                final RadioButton btn_time_120 =  v.findViewById(R.id.time_120);

                final int[] publicRoomTime = new int[1];

                RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        if(btn_Member_50.isChecked())
                            publicRoomMemberCnt[0] = 50;
                        else if(btn_Member_100.isChecked())
                            publicRoomMemberCnt[0] = 100;
                        else if(btn_Member_200.isChecked())
                            publicRoomMemberCnt[0] = 200;

                        else if(btn_time_30.isChecked())
                            publicRoomTime[0] = 30;
                        else if(btn_time_60.isChecked())
                            publicRoomTime[0] = 60;
                        else if(btn_time_120.isChecked())
                            publicRoomTime[0] = 120;

                    }
                };

                btn_time_30.setOnClickListener(optionOnClickListener);
                btn_time_60.setOnClickListener(optionOnClickListener);
                btn_time_120.setOnClickListener(optionOnClickListener);

                btn_Member_50.setOnClickListener(optionOnClickListener);
                btn_Member_100.setOnClickListener(optionOnClickListener);
                btn_Member_200.setOnClickListener(optionOnClickListener);


                Button btn_open_pcr = v.findViewById(R.id.btn_open_pcr);
                btn_open_pcr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean rtValuew = mMyData.makePublicRoom();

                        Intent intent= new Intent(getApplicationContext(), PublicChatRoomHostActivity.class);
                        intent.putExtra("RoomLimit", publicRoomMemberCnt[0]);
                        intent.putExtra("RoomTime", publicRoomTime[0]);
                        startActivity(intent);
                    }
                });
                builder.setView(v);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        ib_home = (ImageButton)findViewById(R.id.ib_home);

        ib_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment).commit();

            }
        });

        iv_honeybox = (ImageView)findViewById(R.id.iv_honeybox);
        iv_honeybox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MailboxActivity.class));
                overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

            }
        });



        /*viewPager = (ScrollerViewPager)findViewById(R.id.view_pager);
        springIndicator = (SpringIndicator)findViewById(R.id.indicator);*/










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



        fanFragment = new FanFragment(this);
        boardFragment = new BoardFragment();
        cardListFragment = new CardListFragment();
        chatListFragment = new ChatListFragment();
        ib_board = (ImageButton)findViewById(R.id.ib_board);

        ib_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,boardFragment).commit();
                //startActivity(new Intent(getApplicationContext(),BoardActivity.class));
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);



            }
        });
        ib_cardList = (ImageButton)findViewById(R.id.ib_cardlist);

        ib_cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cardListFragment).commit();
                //startActivity(new Intent(getApplicationContext(),CardListActivity.class));
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

            }
        });
        ib_chatList = (ImageButton)findViewById(R.id.ib_chatlist);

        ib_chatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,chatListFragment).commit();
                //startActivity(new Intent(getApplicationContext(),ChatListActivity.class));
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

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
                Intent intent = new Intent(getApplicationContext(), FanFragment.class);
                Bundle bundle = new Bundle();

                intent.putExtra("FanList", mMyData.arrMyFanList);
                intent.putExtra("FanData", mMyData.arrMyFanDataList);

                intent.putExtra("StarList", mMyData.arrMyStarList);
                intent.putExtra("StarData", mMyData.arrMyStarDataList);

  /*              bundle.putSerializable("Target", stTargetData);
                intent.putExtra("FanList", stTargetData.arrFanList);
                intent.putExtra("StarList", stTargetData.arrStarList);*/
                intent.putExtra("ViewMode", 0);
                intent.putExtras(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fanFragment).commit();

                //startActivity(intent);
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);



            }
        });




        /*PagerModelManager manager = new PagerModelManager();
        manager.addFragment(new Rank_NearFragment(),"가까운 순");



        manager.addFragment(new Rank_HoneyReceiveFragment(),"실시간 인기 순");
        manager.addFragment(new Rank_RichFragment(),"팬 보유 순");
        manager.addFragment(new Rank_NewMemberFragment(),"새로운 순");

        final ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();
        springIndicator.setViewPager(viewPager);*/
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment).commit();

    }

    @Override
    public void onBackPressed(){

        String alertTitle = "종료";
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage("프로그램을 종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();

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
