package com.hodo.talkking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Data.UserData;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class ClickedMyPicActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    UIData mUIdata = UIData.getInstance();

    private TextView txtProfile;
    private TextView txtMemo;
    private TextView txtDistance;
    private TextView tv_liked;
    //private TextView tv_like;

    private ImageButton btnRegister;
    private ImageButton btnGiftHoney;
    private ImageButton btnGiftJewel;

    private ImageButton btnShare;
    private ImageButton btnMessage;
    private ImageView imgFan;
    private ImageView imgProfile;
    private ImageView imgBestItem;
    private ImageView imgGrade;

    RecyclerView listView_like, listView_liked;
    final Context context = this;
    LinearLayout stickers_holder;
    UIData mUIData = UIData.getInstance();
    Activity mActivity;

    private ImageView bg_fan;
    private SwipeRefreshLayout refreshlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_clicked_mypic);
        setContentView(R.layout.activity_user_page);
        //refreshlayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        /*refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // RefreshData(refreshlayout);
                refreshlayout.setRefreshing(false);
            }
        });*/

        mActivity = this;

        mMyData.SetCurFrag(0);

        txtProfile = (TextView) findViewById(R.id.UserPage_txtProfile);
        txtProfile.setText(mMyData.getUserNick() + ",  " + mMyData.getUserAge()+"세");

        txtMemo = (TextView) findViewById(R.id.UserPage_txtMemo);
        txtMemo.setText(mMyData.getUserMemo());

        btnShare = (ImageButton)findViewById(R.id.UserPage_btnShared);
        btnShare.setVisibility(View.GONE);
        /*btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "안녕하세요 같이 놀아요.\n흥톡에 로그인해 맘에 드는지 확인해보세요 \n" + mMyData.strDownUri;

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                // intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                Intent chooser = Intent.createChooser(intent, "타이틀");
                startActivity(chooser);
            }
        });*/
        ImageView iv_distance = findViewById(R.id.icon_distance);
        iv_distance.setVisibility(View.GONE);
        txtDistance = (TextView) findViewById(R.id.UserPage_txtDistance);
        txtDistance.setVisibility(View.GONE);

/*        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_like.setText(mMyData.getUserNick()+"님을 좋아하는 사람들");*/

        //imgFan = (ImageView) findViewById()

        imgProfile = (ImageView)findViewById(R.id.UserPage_ImgProfile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData TempSendUserData = new UserData();
                TempSendUserData.ImgCount = mMyData.nImgCount;

                TempSendUserData.ImgGroup0 = mMyData.getUserProfileImg(0);
                TempSendUserData.ImgGroup1 = mMyData.getUserProfileImg(1);
                TempSendUserData.ImgGroup2 = mMyData.getUserProfileImg(2);
                TempSendUserData.ImgGroup3 = mMyData.getUserProfileImg(3);

                Intent intent = new Intent(getApplicationContext(), MyImageViewPager.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Target", TempSendUserData);
                bundle.putSerializable("Index", 0);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        Glide.with(getApplicationContext())
                .load(mMyData.getUserProfileImg(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);


        imgBestItem = (ImageView)findViewById(R.id.iv_item);
        imgBestItem.setVisibility(View.VISIBLE);

        if(mMyData.bestItem == 0)
        {
            imgBestItem.setImageResource(R.mipmap.randombox);
        }
        else
        {
            imgBestItem.setImageResource(mUIdata.getJewels()[mMyData.bestItem]);
        }

        imgGrade = (ImageView)findViewById(R.id.iv_rank);
        imgGrade.setImageResource(mUIdata.getGrades()[mMyData.Grade]);

        final GestureDetector gestureDetector = new GestureDetector(ClickedMyPicActivity.this,new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });

        imgFan=findViewById(R.id.ic_fan);
        imgFan.setVisibility(View.VISIBLE);
        bg_fan= findViewById(R.id.bg_fan);
        bg_fan.setVisibility(View.VISIBLE);


        if(mMyData.arrMyFanList.size() != 0) {
            //tv_like = findViewById(R.id.tv_like);
            //tv_like.setText(stTargetData.NickName+"님을 좋아하는 사람들");
            listView_like = (RecyclerView) findViewById(R.id.lv_like);
            UserData tempData = new UserData();
            tempData.arrFanList = mMyData.arrMyFanList;
            tempData.arrFanData = mMyData.arrMyFanDataList;
            TargetLikeAdapter likeAdapter = new TargetLikeAdapter(getApplicationContext(), tempData);
            listView_like.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            listView_like.setAdapter(likeAdapter);
        }
        else
        {
            bg_fan.setVisibility(View.GONE);
        }


        //LinearLayout layout = (LinearLayout) findViewById(R.id.ll_fan);
        //ImageView divide_Fan = (ImageView)findViewById(R.id.divide_fan);
        //divide_Fan.setVisibility(View.GONE);
        /*if(mMyData.arrMyFanList.size() == 0 && mMyData.arrMyStarList.size() == 0 ) {

            imgFan.setVisibility(View.GONE);
            divide_Fan.setVisibility(View.GONE);
        }

        //LinearLayout layoutFanLike = (LinearLayout) findViewById(R.id.ll_fan_like);
        //LinearLayout layoutFanLiked = (LinearLayout) findViewById(R.id.ll_fan_liked);

        if(mMyData.arrMyFanList.size() != 0)
        {
            //tv_like = findViewById(R.id.tv_like);
            //tv_like.setText(mMyData.getUserNick()+"님을 좋아하는 사람들");
            listView_like = (RecyclerView) findViewById(R.id.lv_like);
            LikeAdapter likeAdapter = new LikeAdapter(this);
            listView_like.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

            listView_like.setAdapter(likeAdapter);

            listView_like.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
            {

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    if(gestureDetector.onTouchEvent(e))
                    {
                        Intent intent = new Intent(getApplicationContext(), FanClubActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Target", TempSendUserData);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });

        }
        else
            //layoutFanLike.setVisibility(View.GONE);


       if(mMyData.arrMyStarList.size() != 0)
        {
            tv_liked = findViewById(R.id.tv_liked);
            tv_liked.setText(mMyData.getUserNick() +"님이 좋아하는 사람들");

            listView_liked = (RecyclerView) findViewById(R.id.lv_liked);
            LikedAdapter LikedAdapter = new LikedAdapter(this);
            listView_liked.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

            listView_liked.setAdapter(LikedAdapter);

            listView_liked.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
            {

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    if(gestureDetector.onTouchEvent(e))
                    {
                       Intent intent = new Intent(getApplicationContext(), FanClubActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Target", TempSendUserData);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });
        }
        else*/
            //layoutFanLiked.setVisibility(View.GONE);

        //View Divide_Btn = (View)findViewById(R.id.Divide_Btn);
        //Divide_Btn.setVisibility(View.GONE);

        btnRegister = findViewById(R.id.UserPage_btnRegister);
        btnRegister.setVisibility(View.GONE);

        btnGiftHoney =  findViewById(R.id.UserPage_btnGiftHoney);
        btnGiftHoney.setVisibility(View.GONE);

/*        btnGiftJewel = findViewById(R.id.UserPage_btnGiftJewel);
        btnGiftJewel.setVisibility(View.GONE);*/

        btnMessage =  findViewById(R.id.UserPage_btnMessage);
        btnMessage.setVisibility(View.GONE);

    }


}
