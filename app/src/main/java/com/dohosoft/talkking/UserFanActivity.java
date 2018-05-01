package com.dohosoft.talkking;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.dohosoft.talkking.Data.FanData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Util.CommonFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.dohosoft.talkking.Data.CoomonValueData.TEXTCOLOR_MAN;
import static com.dohosoft.talkking.Data.CoomonValueData.TEXTCOLOR_WOMAN;

/**
 * Created by mjk on 2017. 9. 14..
 */

public class UserFanActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    private UserData stTargetData;
    //public ArrayList<FanData> FanList = new_img ArrayList<>();
    //public ArrayList<UserData> FanData = new_img ArrayList<>();
    private UIData mUIData = UIData.getInstance();
    private MyData mMyData = MyData.getInstance();

    Activity mActivity;
    RecyclerView UserFanRecycler;

    private TextView txt_FanCount, txt_heartCount;


    private void SortByRecvHeart() {
        Map<String, FanData> tempDataMap = new LinkedHashMap<String, FanData>(stTargetData.FanList);

        Iterator it = sortByValue(tempDataMap).iterator();
        stTargetData.FanList.clear();
        stTargetData.arrFanList.clear();
        while (it.hasNext()) {
            String temp = (String) it.next();
            stTargetData.FanList.put(temp, tempDataMap.get(temp));
            stTargetData.arrFanList.add(tempDataMap.get(temp));
        }
    }

    public static List sortByValue(final Map map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                FanData g1 = (FanData) map.get(o1);
                FanData g2 = (FanData) map.get(o2);

                Object v1 = g1.RecvGold;
                Object v2 = g2.RecvGold;
                return ((Comparable) v2).compareTo(v1);
            }
        });
        // Collections.reverse(list); // 주석시 오름차순
        return list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_user_fan_club);

        Intent intent = getIntent();
        Bundle bundle = this.getIntent().getExtras();
        stTargetData = (UserData) bundle.getSerializable("Target");

        mMyData.SetCurFrag(0);

        SortByRecvHeart();

        UserFanRecycler = (RecyclerView) findViewById(R.id.user_fan_list);
        UserFanRecycler.setAdapter(new UserFanActivity.UserFanListAdapter());
        UserFanRecycler.setLayoutManager(new LinearLayoutManager(this));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        TextView txt_Title = findViewById(R.id.txt_title);
        txt_Title.setText(stTargetData.NickName + "님의 팬");

        ImageView backBtn = (ImageView) findViewById(R.id.iv_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txt_FanCount = findViewById(R.id.txt_fanCount);
        txt_FanCount.setText(Integer.toString(stTargetData.FanList.size()));

        txt_heartCount = (TextView) findViewById(R.id.tv_heart);
        String str = String.format("%,d", stTargetData.RecvGold * -1);

        txt_heartCount.setText(str);
    }

    private class UserFanListAdapter extends RecyclerView.Adapter<UserFanListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_my_fan, parent, false);
            //view.setLayoutParams(new_img LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/7));

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            //  holder.linearLayout.setLayoutParams(new_img LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (mUIData.getHeight() / 7)));
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (CommonFunc.getInstance().getClickStatus() == false)
                        getMyfanData(position);

                }
            });
            //holder.imageView.setImageResource(R.mipmap.hdvd);

            holder.imgAlarm.setVisibility(View.GONE);
            holder.imgNew.setVisibility(View.GONE);

         /*   if(stTargetData.arrFanList.get(position).Check == 1)
            {
                holder.imgNew.setVisibility(View.VISIBLE);
            }
            else if(stTargetData.arrFanList.get(position).Check == 2)
            {
                holder.imgAlarm.setVisibility(View.VISIBLE);
            }*/

            String i = stTargetData.arrFanList.get(position).Idx;

            Glide.with(mActivity)
                    .load(stTargetData.arrFanData.get(i).Img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mActivity))
                    .thumbnail(0.1f)
                    .into(holder.img);

            holder.textNick.setText(stTargetData.arrFanData.get(i).NickName + " (" + stTargetData.arrFanData.get(i).Age + "세)");// + ", " + mMyData.arrCardNameList.get(i).Age + "세");ata.arrCardNameList.get(i).Age + "세");

            if (stTargetData.arrFanData.get(i).Gender.equals("여자"))
                holder.textNick.setTextColor(TEXTCOLOR_WOMAN);
            else
                holder.textNick.setTextColor(TEXTCOLOR_MAN);

            if (position < 3) {
                holder.textRank.setVisibility(View.INVISIBLE);
                holder.imgRank.setVisibility(View.VISIBLE);
                switch (position) {
                    case 0:
                        holder.imgRank.setImageResource(R.drawable.fan_mark1_80);
                        break;
                    case 1:
                        holder.imgRank.setImageResource(R.drawable.fan_mark2_80);
                        break;
                    case 2:
                        holder.imgRank.setImageResource(R.drawable.fan_mark3_80);
                        break;
                }


            } else {
                holder.imgRank.setVisibility(View.INVISIBLE);
                holder.textRank.setVisibility(View.VISIBLE);
                holder.textRank.setText(Integer.toString(position + 1) + "위");

            }


            holder.imgItem.setVisibility(View.VISIBLE);

            if (stTargetData.arrFanData.get(i).BestItem == 0) {
                holder.imgItem.setVisibility(View.GONE);
            } else
                holder.imgItem.setImageResource(mUIData.getJewels()[stTargetData.arrFanData.get(i).BestItem]);

            holder.imgGrade.setImageResource(mUIData.getGrades()[stTargetData.arrFanData.get(i).Grade]);

            int RecvCnt = stTargetData.arrFanList.get(position).RecvGold;
            holder.textCount.setText(Integer.toString(RecvCnt));

            //holder.textheartCount.setText(Long.toString(stTargetData.arrFanData.get(i).RecvGold));

        }


        @Override
        public int getItemCount() {
            return stTargetData.FanList.size();
        }

        public void moveFanPage(int position) {
            String i = stTargetData.arrFanList.get(position).Idx;

            Intent intent = new Intent(mActivity, UserPageActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", stTargetData.mapFanData.get(i));
            intent.putExtras(bundle);

            mActivity.startActivity(intent);

            CommonFunc.getInstance().DismissLoadingPage();
            CommonFunc.getInstance().setClickStatus(false);

        }


        public void getMyfanData(final int position) {
            CommonFunc.getInstance().ShowLoadingPage(mActivity, "로딩중");
            CommonFunc.getInstance().setClickStatus(true);

            String i = stTargetData.arrFanList.get(position).Idx;

            final String strTargetIdx = stTargetData.FanList.get(i).Idx;

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = null;


            if (strTargetIdx.equals(mMyData.getUserIdx())) {
                CommonFunc.getInstance().DismissLoadingPage();
                CommonFunc.getInstance().setClickStatus(false);
                CommonFunc.getInstance().ShowToast(getApplicationContext(), "본인 입니다", false);
            } else {

                String tempGender = mMyData.mapGenderData.get(strTargetIdx);
                if (tempGender == null || tempGender.equals("")) {
                    table = database.getReference("GenderList");
                    table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String tempValue = dataSnapshot.getValue(String.class);
                            mMyData.mapGenderData.put(strTargetIdx, tempValue);

                            DatabaseReference table = null;
                            if (tempValue.equals("여자")) {
                                table = database.getReference("Users").child("Woman");
                            } else {
                                table = database.getReference("Users").child("Man");
                            }

                            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int saa = 0;
                                    UserData tempUserData = dataSnapshot.getValue(UserData.class);
                                    if (tempUserData != null) {
                                        stTargetData.mapFanData.put(strTargetIdx, tempUserData);

                                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                                            stTargetData.mapFanData.get(strTargetIdx).arrFanList.add(entry.getValue());
                                        }

                                        if (stTargetData.mapFanData.get(strTargetIdx).arrFanList.size() == 0) {
                                            moveFanPage(position);
                                        } else {

                                            CommonFunc.getInstance().SortByRecvHeart(stTargetData.mapFanData.get(strTargetIdx));

                                            for (int i = 0; i < stTargetData.mapFanData.get(strTargetIdx).arrFanList.size(); i++) {
                                                Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(stTargetData.mapFanData.get(strTargetIdx).arrFanList.get(i).Idx);
                                                final FanData finalTempFanData = stTargetData.mapFanData.get(strTargetIdx).arrFanList.get(i);
                                                final int finalI = i;
                                                data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                                                        if (DBData != null) {
                                                            stTargetData.mapFanData.get(strTargetIdx).arrFanData.put(finalTempFanData.Idx, DBData);

                                                            if (finalI == stTargetData.mapFanData.get(strTargetIdx).arrFanList.size() - 1) {
                                                                moveFanPage(position);
                                                            }
                                                        } else {
                                                            CommonFunc.getInstance().ShowToast(getApplicationContext(), "사용자가 없습니다.", false);
                                                            CommonFunc.getInstance().setClickStatus(false);
                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }


                                    } else
                                        CommonFunc.getInstance().setClickStatus(false);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }

                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }


                    });
                } else {
                    if (tempGender.equals("여자")) {
                        table = database.getReference("Users").child("Woman");
                    } else {
                        table = database.getReference("Users").child("Man");
                    }

                    table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int saa = 0;
                            UserData tempUserData = dataSnapshot.getValue(UserData.class);
                            if (tempUserData != null) {
                                stTargetData.mapFanData.put(strTargetIdx, tempUserData);

                                for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                                    stTargetData.mapFanData.get(strTargetIdx).arrFanList.add(entry.getValue());
                                }

                                if (stTargetData.mapFanData.get(strTargetIdx).arrFanList.size() == 0) {
                                    moveFanPage(position);
                                } else {

                                    CommonFunc.getInstance().SortByRecvHeart(stTargetData.mapFanData.get(strTargetIdx));

                                    for (int i = 0; i < stTargetData.mapFanData.get(strTargetIdx).arrFanList.size(); i++) {
                                        Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(stTargetData.mapFanData.get(strTargetIdx).arrFanList.get(i).Idx);
                                        final FanData finalTempFanData = stTargetData.mapFanData.get(strTargetIdx).arrFanList.get(i);
                                        final int finalI = i;
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                                                if (DBData != null) {
                                                    stTargetData.mapFanData.get(strTargetIdx).arrFanData.put(finalTempFanData.Idx, DBData);

                                                    if (finalI == stTargetData.mapFanData.get(strTargetIdx).arrFanList.size() - 1) {
                                                        moveFanPage(position);
                                                    }
                                                } else {
                                                    CommonFunc.getInstance().ShowToast(getApplicationContext(), "사용자가 없습니다.", false);
                                                    CommonFunc.getInstance().setClickStatus(false);
                                                }


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }


                            } else
                                CommonFunc.getInstance().setClickStatus(false);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }

                    });

                }
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView img, imgRank;
            public TextView textRank, textNick, textCount;
            public ConstraintLayout constraintLayout;
            public ImageView imgGrade, imgItem, imgNew, imgAlarm;


            public ViewHolder(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.iv_fan);
                imgRank = (ImageView) itemView.findViewById(R.id.iv_rank_mark);
                imgGrade = (ImageView) itemView.findViewById(R.id.iv_grade);
                imgItem = (ImageView) itemView.findViewById(R.id.iv_item);

                imgNew = (ImageView) itemView.findViewById(R.id.iv_new);
                imgAlarm = (ImageView) itemView.findViewById(R.id.red_alarm);

                textRank = (TextView) itemView.findViewById(R.id.tv_gift_ranking);
                textNick = (TextView) itemView.findViewById(R.id.tv_nickname);
                textCount = (TextView) itemView.findViewById(R.id.tv_gift_count);

                constraintLayout = itemView.findViewById(R.id.layout_fan);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        android.app.AlertDialog.Builder mDialog = null;
        mDialog = new android.app.AlertDialog.Builder(this);

        if (!mMyData.verSion.equals(mMyData.marketVersion)) {
            mDialog.setMessage("업데이트 후 사용해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("업데이트 바로가기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent marketLaunch = new Intent(
                                            Intent.ACTION_VIEW);
                                    marketLaunch.setData(Uri
                                            //.parse("https://play.google.com/store/apps/details?id=패키지명 적으세요"));
                                            .parse("https://play.google.com/store/apps/details?id=com.dohosoft.talkking"));

                                    startActivity(marketLaunch);
                                    System.exit(0);
                                }
                            });
            android.app.AlertDialog alert = mDialog.create();
            alert.setTitle("안 내");
            alert.show();
        }

        else
        {
            if (CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
                mMyData.badgecount = pref.getInt("Badge", 1);

                if (mMyData.badgecount >= 1) {
                    mMyData.badgecount = 0;
                    Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                    intent.putExtra("badge_count_package_name", "com.dohosoft.talkking");
                    intent.putExtra("badge_count_class_name", "com.dohosoft.talkking.LoginActivity");
                    intent.putExtra("badge_count", mMyData.badgecount);
                    sendBroadcast(intent);
                }
            }
        }


        mMyData.SetCurFrag(0);
    }
}
