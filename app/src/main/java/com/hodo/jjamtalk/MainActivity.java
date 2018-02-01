package com.hodo.jjamtalk;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.BoardMsgClientData;
import com.hodo.jjamtalk.Data.BoardMsgDBData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Data.SimpleChatData;
import com.hodo.jjamtalk.Data.SimpleUserData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.Util.CommonFunc;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.hodo.jjamtalk.Data.CoomonValueData.FIRST_LOAD_BOARD_COUNT;
import static com.hodo.jjamtalk.Data.CoomonValueData.MAIN_ACTIVITY_BOARD;
import static com.hodo.jjamtalk.Data.CoomonValueData.MAIN_ACTIVITY_HOME;
import static com.hodo.jjamtalk.Data.CoomonValueData.REPORT_BOARD_DELETE;

public class MainActivity extends AppCompatActivity {

    ImageView ib_cardList;
    ImageView ib_chatList;
    ImageView ib_board;
    ImageView iv_myPage;
    ImageView ib_fan;
    ImageView itembox;
    //ImageButton ib_pcr_open;
    ImageView ib_filter;
    ImageButton ib_buy_jewel;
    ImageView ib_home;
    ImageView iv_refresh,iv_honeybox;
    TextView tv_MainTitle;
    LinearLayout layout_lowbar,layout_topbar;
    BoardFragment boardFragment;



    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private UIData mUIData = UIData.getInstance();
    private SettingData mSetting = SettingData.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    ArrayList<Class> arrFragment = new ArrayList<>();
    private CardListFragment cardListFragment;
    private ChatListFragment chatListFragment;
    private FanListFragment fanFragment;
    private HomeFragment homeFragment;// = HomeFragment.getInstance();

    public static Context mContext;
    public static Activity mActivity;
    public static android.support.v4.app.FragmentManager mFragmentMng;

    public int nStartFragment = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMng = getSupportFragmentManager();
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-7666588215496282~7894630064");

        itembox= findViewById(R.id.iv_itemBox);
        itembox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyJewelBoxActivity.class));
            }
        });


        Bundle bundle = getIntent().getExtras();
        nStartFragment = (int) bundle.getSerializable("StartFragment");

        iv_myPage = findViewById(R.id.iv_mypage);


        Glide.with(getApplicationContext())
                .load(mMyData.getUserImg())
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .into(iv_myPage);

        iv_myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyPageActivity.class));
            }
        });



        final Display display = getWindowManager().getDefaultDisplay();
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


        boolean bCheckConnt = mMyData.CheckConnectDate();
        if(bCheckConnt == true)
        {
            String alertTitle = "종료";
            View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app,null,false);

            final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

            final TextView txt_Title;
            txt_Title = (TextView)v.findViewById(R.id.title);
            txt_Title.setText("출석 체크 보상");
            final TextView txt_Body;
            txt_Body = (TextView)v.findViewById(R.id.msg);
            txt_Body.setText("매일 20골드 추가");

            final Button btn_exit;
            final Button btn_no;

            btn_exit = (Button) v.findViewById(R.id.btn_yes);
            btn_exit.setText("확인");
            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btn_no = (Button) v.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btn_no.setVisibility(View.GONE);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss (DialogInterface var1){
                    mMyData.setUserHoney(mMyData.getUserHoney()+20);
                    mMyData.setPoint(20);
                }

            });

            bCheckConnt = false;
        }


        ib_filter = findViewById(R.id.ib_filter);
        ib_filter.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.boardBgColor), PorterDuff.Mode.MULTIPLY);

        ib_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  startActivity(new Intent(getApplicationContext(),MainSettingActivity.class));
              //  overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View v = LayoutInflater.from(mActivity).inflate(R.layout.category_popup,null,false);

                builder.setView(v);
                final AlertDialog filter_dialog = builder.create();
                filter_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                filter_dialog.show();


                 final RadioButton rbtn_two;
                 final RadioButton rbtn_three;
                 final RadioButton rbtn_four;

                final Switch rbtn_man;
                final Switch rbtn_woman;

                Switch rbtn_10;
                Switch rbtn_20;
                Switch rbtn_30;
                Switch rbtn_40;

                Button btn_ok = v.findViewById(R.id.btn_save);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMyData.setSettingData(mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.getnRecvMsg(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration());
                        mFireBaseData.SaveSettingData(mMyData.getUserIdx(), mSetting.getnSearchSetting(), mSetting.getnViewSetting(), mSetting.getnRecvMsg(), mSetting.IsAlarmSettingSound(), mSetting.IsAlarmSettingVibration());
                        filter_dialog.dismiss();

                        mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME);

               /*         Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("StartFragment", 0);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);*/

                    }
                });


                Button btn_cancel = v.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filter_dialog.dismiss();
                    }
                });


                rbtn_two = (RadioButton) v.findViewById(R.id.rbtn_two);
                rbtn_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rbtn_two.isChecked())
                            mSetting.setnViewSetting(0);
                    }
                });

                rbtn_three = (RadioButton) v.findViewById(R.id.rbtn_three);
                rbtn_three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rbtn_three.isChecked())
                            mSetting.setnViewSetting(1);
                    }
                });

                rbtn_four = (RadioButton) v.findViewById(R.id.rbtn_four);
                rbtn_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rbtn_four.isChecked())
                            mSetting.setnViewSetting(2);
                    }
                });

                rbtn_man = (Switch) v.findViewById(R.id.rbtn_man);
                rbtn_woman = (Switch) v.findViewById(R.id.rbtn_woman);

                rbtn_10 = (Switch) v.findViewById(R.id.rbtn_10);
                rbtn_20 = (Switch) v.findViewById(R.id.rbtn_20);
                rbtn_30 = (Switch) v.findViewById(R.id.rbtn_30);
                rbtn_40 = (Switch) v.findViewById(R.id.rbtn_40);



                RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(rbtn_two.isChecked())
                            mSetting.setnViewSetting(0);
                        else if(rbtn_three.isChecked())
                            mSetting.setnViewSetting(1);
                        else if(rbtn_four.isChecked())
                            mSetting.setnViewSetting(2);
                    }
                };

                rbtn_man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                        if(bChecked == true) {
                            if(rbtn_woman.isChecked() == true)
                                mSetting.setnSearchSetting(3);
                            else
                                mSetting.setnSearchSetting(1);
                        }
                        else
                            mSetting.setnSearchSetting(2);
                    }
                });

                rbtn_woman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                        if(bChecked == true)
                        {
                            if(rbtn_man.isChecked() == true)
                                mSetting.setnSearchSetting(3);
                            else
                                mSetting.setnSearchSetting(2);
                        }
                        else
                            mSetting.setnSearchSetting(1);

                    }
                });


            }
        });



        Toast.makeText(getApplicationContext(),"width: "+width+"height: "+ height,Toast.LENGTH_LONG).show();
        /*ib_pcr_open = (ImageButton)findViewById(R.id.ib_pcr_open);
        ib_pcr_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mMyData.nPublicRoomStatus == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_pcr_open,null,false);
                    //v.getBackground().setAlpha(0);


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



                    builder.setView(v);
                    final AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    Button btn_open_pcr = v.findViewById(R.id.btn_open_pcr);
                    btn_open_pcr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            boolean rtValuew = mMyData.makePublicRoom(publicRoomMemberCnt[0], publicRoomTime[0]);

                            Intent intent= new Intent(getApplicationContext(), PublicChatRoomHostActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                }


                else
                {
                    Intent intent= new Intent(getApplicationContext(), PublicChatRoomHostActivity.class);
                    intent.putExtra("RoomLimit", mMyData.nPublicRoomLimit);
                    intent.putExtra("RoomTime", mMyData.nPublicRoomTime);
                    startActivity(intent);
                }
            }
        });*/


        ib_home = findViewById(R.id.ib_home);
        //ib_home.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

        ib_home.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                ib_filter.setVisibility(View.VISIBLE);
                mMyData.SetCurFrag(0);
                //getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment).commit();
                //ib_home.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

                setImageAlpha(255,100,100,100,100);

                view.setSelected(!view.isSelected());
                if(view.isSelected()){


                }else{

                }

            }
        });

        iv_honeybox = (ImageView)findViewById(R.id.iv_honeybox);
        //iv_honeybox.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.postBox), PorterDuff.Mode.MULTIPLY);
        iv_honeybox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MailboxActivity.class));
                overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

            }
        });

        tv_MainTitle = (TextView)findViewById(R.id.tv_maintitle);


        layout_topbar = (LinearLayout)findViewById(R.id.layout_topbar);
        layout_lowbar = (LinearLayout)findViewById(R.id.layout_lowbar);

        homeFragment = new HomeFragment();
        LoadFanData();
        boardFragment = new BoardFragment();
        LoadCardData();
        LoadChatData();

        ib_board = findViewById(R.id.ib_board);
       // ib_board.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);

        ib_board.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                ib_filter.setVisibility(View.INVISIBLE);
                mMyData.SetCurFrag(4);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,boardFragment).commit();
                view.setSelected(!view.isSelected());
                setImageAlpha(100,100,100,100,255);


            }
        });
        ib_cardList = findViewById(R.id.ib_cardlist);
        //ib_cardList.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);
        ib_cardList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                ib_filter.setVisibility(View.INVISIBLE);
                mMyData.SetCurFrag(1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cardListFragment).commit();
                view.setSelected(!view.isSelected());

                setImageAlpha(100,255,100,100,100);

                if(view.isSelected()){
                    int a = 0;

                }else{
                    int b = 0;
                }
                //startActivity(new Intent(getApplicationContext(),CardListActivity.class));
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

            }
        });
        ib_chatList = findViewById(R.id.ib_chatlist);
        //ib_chatList.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);
        ib_chatList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                ib_filter.setVisibility(View.INVISIBLE);
                mMyData.SetCurFrag(2);
                Fragment frg = null;
                frg = mFragmentMng.findFragmentByTag("ChatListFragment");

                mFragmentMng.beginTransaction().replace(R.id.frag_container,chatListFragment, "ChatListFragment").commit();

               // mCommon.mFragmentManager.beginTransaction().replace(R.id.frag_container,chatListFragment).commit();

                /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack("ChatListFragment").replace(R.id.frag_container,cardListFragment).commit();
                transaction.add(R.id.frag_container,chatListFragment, "ChatListFragment");
                transaction.commit();*/
               // getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,chatListFragment).commit();
                view.setSelected(!view.isSelected());
                setImageAlpha(100,100,255,100,100);
                /*

                ib_fan.setImageResource(R.drawable.btn_fan_normal);
                ib_board.setImageResource(R.drawable.btn_board_normal);
                ib_chatList.setImageResource(R.drawable.btn_chat_selected);
                ib_cardList.setImageResource(R.drawable.btn_card_normal);
                ib_home.setImageResource(R.drawable.btn_home_normal);*/

                //startActivity(new Intent(getApplicationContext(),ChatListActivity.class));
                //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

            }
        });
        ib_fan = findViewById(R.id.ib_fan);
        //ib_fan.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.botItem), PorterDuff.Mode.MULTIPLY);
        ib_fan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                ib_filter.setVisibility(View.INVISIBLE);
                mMyData.SetCurFrag(3);
                view.setSelected(!view.isSelected());
                setImageAlpha(100,100,100,255,100);
/*
                ib_fan.setImageResource(R.drawable.btn_fan_selected);
                ib_board.setImageResource(R.drawable.btn_board_normal);
                ib_chatList.setImageResource(R.drawable.btn_chat_normal);
                ib_cardList.setImageResource(R.drawable.btn_card_normal);
                ib_home.setImageResource(R.drawable.btn_home_normal);*/

                //startActivity(new Intent(getApplicationContext(),FanActivity.class));
         /*       Intent intent = new Intent(getApplicationContext(), FanActivity.class);
                intent.putExtra("ViewMode", 0);
                startActivity(intent);
*/
                Intent intent = new Intent(getApplicationContext(), FanFragment.class);
                Bundle bundle = new Bundle();

                intent.putExtra("FanList", mMyData.arrMyFanList);
                intent.putExtra("FanCount", mMyData.nFanCount);

                //intent.putExtra("FanData", mMyData.arrMyFanDataList);

                intent.putExtra("StarList", mMyData.arrMyStarList);
                //intent.putExtra("StarData", mMyData.arrMyStarDataList);

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



        manager.addFragment(new Rank_GoldReceiveFragment(),"실시간 인기 순");
        manager.addFragment(new Rank_FanRichFragment(),"팬 보유 순");
        manager.addFragment(new Rank_NewMemberFragment(),"새로운 순");

        final ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();
        springIndicator.setViewPager(viewPager);*/
        switch (nStartFragment)
        {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment).commit();
                setImageAlpha(255,100,100,100,100);
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cardListFragment).commit();
                setImageAlpha(100,255,100,100,100);
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,chatListFragment).commit();
                setImageAlpha(100,100,255,100,100);
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fanFragment).commit();
                setImageAlpha(100,100,100,255,100);
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,boardFragment).commit();
                setImageAlpha(100,100,100,100,255);
                break;

            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,homeFragment).commit();
                setImageAlpha(255,100,100,100,100);
                break;
        }


    }

    private void LoadChatData() {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

        if(mMyData.arrChatNameList.size() == 0)
            chatListFragment = new ChatListFragment(getApplicationContext());

        for(int i = 0; i < mMyData.arrChatNameList.size(); i++)
        {
            Query data = FirebaseDatabase.getInstance().getReference().child("User").child(mMyData.getUserIdx()).child("SendList").child(mMyData.arrChatNameList.get(i));
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SimpleChatData DBData = dataSnapshot.getValue(SimpleChatData.class);
                    mMyData.arrChatDataList.put(mMyData.arrChatNameList.get(finalI), DBData);

                    if(mMyData.arrChatNameList.size() == mMyData.arrChatDataList.size())
                        chatListFragment = new ChatListFragment(getApplicationContext());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void LoadCardData() {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

        if(mMyData.arrCardNameList.size() == 0)
            cardListFragment = new CardListFragment();

        for(int i = 0; i < mMyData.arrCardNameList.size(); i++)
        {
            Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.arrCardNameList.get(i));
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                        SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                    mMyData.arrCarDataList.put(mMyData.arrCardNameList.get(finalI), DBData);

                    if(mMyData.arrCarDataList.size() == mMyData.arrCardNameList.size())
                        cardListFragment = new CardListFragment();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

   /* private void LoadStarData() {
        LoadFanData();
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

        if(mMyData.arrMyStarList.size() == 0)
        {
            if(fanFragment == null)
                //fanFragment = new FanFragment();
                fanFragment = new FanListFragment();
        }

        for(int i = 0; i < mMyData.arrMyStarList.size(); i++)
        {
            Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.arrMyStarList.get(i).Idx);
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                    mMyData.arrMyStarDataList.put(mMyData.arrMyStarList.get(finalI).Idx, DBData);

                    if(mMyData.arrMyStarDataList.size() == mMyData.arrMyStarList.size())
                    {
                        if(fanFragment == null)
                            fanFragment = new FanListFragment();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }*/

    private void LoadFanData() {
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();

        if(mMyData.arrMyFanList.size() == 0)
        {
            if(fanFragment == null)
                fanFragment = new FanListFragment();
        }

        for(int i = 0; i < mMyData.arrMyFanList.size(); i++)
        {
            Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.arrMyFanList.get(i).Idx);
            final int finalI = i;
            data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                    mMyData.arrMyFanDataList.put(mMyData.arrMyFanList.get(finalI).Idx, DBData);

                    if(mMyData.arrMyFanDataList.size() == mMyData.arrMyFanList.size())
                    {
                        if(fanFragment == null)
                            fanFragment = new FanListFragment();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed(){

        String alertTitle = "종료";
        View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_exit_app,null,false);

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        final Button btn_exit;
        final Button btn_no;
        final TextView title;
        final AdView mAdView;

        title =  (TextView) v.findViewById(R.id.title);
        title.setVisibility(View.GONE);


        btn_exit = (Button) v.findViewById(R.id.btn_yes);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pid = android.os.Process.myPid(); android.os.Process.killProcess(pid);
            }
        });

        btn_no = (Button) v.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        mAdView = (AdView)v.findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


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



        CheckBox cbMultiSend = (CheckBox)findViewById(R.id.checkBox);
        cbMultiSend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mAppStatus.bCheckMultiSend = isChecked;
            }
        });

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setImageAlpha(int home, int card, int chat, int fan, int board ){
        ib_fan.setImageAlpha(fan);
        ib_board.setImageAlpha(board);
        ib_chatList.setImageAlpha(chat);
        ib_cardList.setImageAlpha(card);
        ib_home.setImageAlpha(home);



    }


}
