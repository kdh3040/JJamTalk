package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;

import java.util.ArrayList;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class FanFragment extends Fragment {

    Activity activity;
    SpringIndicator indicator;
    ScrollerViewPager viewPager;
    int nViewMode;

    private MyData mMyData = MyData.getInstance();
    private UserData stTargetData;
    public ArrayList<FanData> FanList = new ArrayList<>();
    public ArrayList<UserData> FanData = new ArrayList<>();

    public ArrayList<FanData> StarList = new ArrayList<>();
    public ArrayList<UserData> StarData = new ArrayList<>();
    FragmentManager fragmentManager;


    public FanFragment() {

    }

    public FanFragment(Activity activity) {
        super();
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_fan,container,false);
        indicator = (SpringIndicator) fragView.findViewById(R.id.indicator_fan);
        fragmentManager= getFragmentManager();

        viewPager = (ScrollerViewPager) fragView.findViewById(R.id.vp_fan);
        PagerModelManager manager = new PagerModelManager();

        Intent intent = activity.getIntent();

        Bundle bundle = activity.getIntent().getExtras();
        nViewMode = intent.getIntExtra("ViewMode", 0);

        if(nViewMode == 0)
        {
            manager.addFragment(new MyFanFragment(),"내 팬");
            manager.addFragment(new MyLikeFragment(),"내가 좋아하는");
        }
        else
        {
            stTargetData = (UserData) bundle.getSerializable("Target");
            FanList = (ArrayList<FanData>) activity.getIntent().getSerializableExtra("FanList");
            FanData = (ArrayList<UserData>) activity.getIntent().getSerializableExtra("FanData");

            StarList = (ArrayList<FanData>) activity.getIntent().getSerializableExtra("StarList");
            StarData = (ArrayList<UserData>) activity.getIntent().getSerializableExtra("StarData");

            stTargetData.arrFanList = FanList;
            stTargetData.arrFanData= FanData;
            stTargetData.arrStarList = StarList;
            stTargetData.arrStarData = StarData;
            manager.addFragment(new TargetFanFragment(stTargetData),"팬클럽");
            manager.addFragment(new TargetLikeFragment(stTargetData),"가입한 팬클럽");
        }


        final ModelPagerAdapter adapter = new ModelPagerAdapter(fragmentManager,manager);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return fragView;
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fan_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);





        indicator = (SpringIndicator) findViewById(R.id.indicator_fan);

        viewPager = (ScrollerViewPager) findViewById(R.id.vp_fan);
        PagerModelManager manager = new PagerModelManager();

        Intent intent = getIntent();

        Bundle bundle = getIntent().getExtras();
        nViewMode = intent.getIntExtra("ViewMode", 0);

        if(nViewMode == 0)
        {
            FanList = (ArrayList<FanData>) getIntent().getSerializableExtra("FanList");
            FanData = (ArrayList<UserData>) getIntent().getSerializableExtra("FanData");

            StarList = (ArrayList<FanData>) getIntent().getSerializableExtra("StarList");
            StarData = (ArrayList<UserData>) getIntent().getSerializableExtra("StarData");
            manager.addFragment(new MyFanFragment(),"내 팬");
            manager.addFragment(new MyLikeFragment(),"내가 좋아하는");
        }
        else
        {
            stTargetData = (UserData) bundle.getSerializable("Target");
            FanList = (ArrayList<FanData>) getIntent().getSerializableExtra("FanList");
            FanData = (ArrayList<UserData>) getIntent().getSerializableExtra("FanData");

            StarList = (ArrayList<FanData>) getIntent().getSerializableExtra("StarList");
            StarData = (ArrayList<UserData>) getIntent().getSerializableExtra("StarData");

            stTargetData.arrFanList = FanList;
            stTargetData.arrFanData= FanData;
            stTargetData.arrStarList = StarList;
            stTargetData.arrStarData = StarData;
            manager.addFragment(new TargetFanFragment(stTargetData),"팬클럽");
            manager.addFragment(new TargetLikeFragment(stTargetData),"가입한 팬클럽");
        }


        final ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.open_pcr);
        if(nViewMode == 0)
        {
            register.setVisible(true);
        }
        else
        {
            register.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.open_pcr:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        return super.onOptionsItemSelected(item);
    }*/
}
