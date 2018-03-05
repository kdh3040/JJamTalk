package com.hodo.talkking;

import android.app.Activity;
import android.content.Intent;
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
import com.hodo.talkking.Data.FanData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Util.CommonFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 9. 14..
 */

public class UserFanActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    private UserData stTargetData;
    //public ArrayList<FanData> FanList = new ArrayList<>();
    //public ArrayList<UserData> FanData = new ArrayList<>();
    private UIData mUIData = UIData.getInstance();
    private MyData mMyData = MyData.getInstance();

    Activity mActivity;
    RecyclerView UserFanRecycler;

    private void SortByRecvHeart()
    {
         Map<String, FanData> tempDataMap = new LinkedHashMap<String, FanData>(stTargetData.FanList);

        Iterator it = sortByValue(tempDataMap).iterator();
        stTargetData.FanList.clear();
        stTargetData.arrFanList.clear();
        while(it.hasNext()) {
            String temp = (String) it.next();
            stTargetData.FanList.put(temp, tempDataMap.get(temp));
            stTargetData.arrFanList.add(tempDataMap.get(temp));
        }
    }

    public static List sortByValue(final Map map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());
        Collections.sort(list,new Comparator() {

            public int compare(Object o1,Object o2) {
                FanData g1 = (FanData)map.get(o1);
                FanData g2 = (FanData)map.get(o2);

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

        UserFanRecycler = (RecyclerView)findViewById(R.id.user_fan_list);
        UserFanRecycler.setAdapter(new UserFanActivity.UserFanListAdapter());
        UserFanRecycler.setLayoutManager(new LinearLayoutManager(this));

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        TextView txt_Title = findViewById(R.id.txt_title);
        txt_Title.setText(stTargetData.NickName + "님의 팬");

    }

    private class UserFanListAdapter extends RecyclerView.Adapter<UserFanListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_my_fan,parent,false);
          //  view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/7));

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
          //  holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (mUIData.getHeight() / 7)));
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(CommonFunc.getInstance().getClickStatus()==false)
                        getMyfanData(position);

                }
            });
            //holder.imageView.setImageResource(R.mipmap.hdvd);

            String i = stTargetData.arrFanList.get(position).Idx;

            Glide.with(mActivity)
                    .load(stTargetData.arrFanData.get(i).Img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mActivity))
                    .thumbnail(0.1f)
                    .into(holder.img);

            holder.textNick.setText(stTargetData.arrFanData.get(i).NickName);
            holder.textRank.setText((position + 1) + "위");

            holder.imgItem.setVisibility(View.VISIBLE);

            if(stTargetData.arrFanData.get(i).BestItem == 0)
            {
                holder.imgItem.setImageResource(R.mipmap.randombox);
            }
                //holder.imageItem.setImageResource(mUIData.getJewels()[mMyData.arrCarDataList.get(i).BestItem]);

            else {
                holder.imgItem.setImageResource(mUIData.getJewels()[stTargetData.arrFanData.get(i).BestItem]);
            }

            holder.imgGrade.setImageResource(mUIData.getGrades()[stTargetData.arrFanData.get(i).Grade]);

            int RecvCnt = stTargetData.arrFanList.get(position).RecvGold;
            holder.textCount.setText(Integer.toString(RecvCnt));

        }


        @Override
        public int getItemCount() {
            return stTargetData.FanList.size();
        }

        public void moveFanPage(int position) {
            CommonFunc.getInstance().setClickStatus(false);
            String i = stTargetData.arrFanList.get(position).Idx;

            Intent intent = new Intent(mActivity, UserPageActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", stTargetData.mapFanData.get(i));
            intent.putExtras(bundle);

            mActivity.startActivity(intent);
        }


        public void getMyfanData(final int position) {
            CommonFunc.getInstance().setClickStatus(true);
            String i = stTargetData.arrFanList.get(position).Idx;

            final String strTargetIdx = stTargetData.FanList.get(i).Idx;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = null;
            table = database.getReference("User");

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

                        if(stTargetData.mapFanData.get(strTargetIdx).arrFanList.size() == 0)
                        {
                            moveFanPage(position);
                        }
                        else
                        {
                            for(int i = 0 ;i < stTargetData.mapFanData.get(strTargetIdx).arrFanList.size(); i++)
                            {
                                Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(stTargetData.mapFanData.get(strTargetIdx).arrFanList.get(i).Idx);
                                final FanData finalTempFanData = stTargetData.mapFanData.get(strTargetIdx).arrFanList.get(i);
                                final int finalI = i;
                                data.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                                        if(DBData != null)
                                        {
                                            stTargetData.mapFanData.get(strTargetIdx).arrFanData.put(finalTempFanData.Idx, DBData);

                                            if( finalI == stTargetData.mapFanData.get(strTargetIdx).arrFanList.size() -1)
                                            {
                                                moveFanPage(position);
                                            }
                                        }
                                        else
                                        {
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


                    }

                    else
                        CommonFunc.getInstance().setClickStatus(false);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView img;
            public TextView textRank, textNick, textCount;
            public ConstraintLayout constraintLayout;
            public ImageView imgGrade, imgItem;


            public ViewHolder(View itemView) {
                super(itemView);
                img = (ImageView)itemView.findViewById(R.id.iv_fan);
                imgGrade= (ImageView)itemView.findViewById(R.id.iv_grade);
                imgItem = (ImageView)itemView.findViewById(R.id.iv_item);

                textRank = (TextView)itemView.findViewById(R.id.tv_gift_ranking);
                textNick = (TextView)itemView.findViewById(R.id.tv_nickname);
                textCount = (TextView)itemView.findViewById(R.id.tv_gift_count);
                constraintLayout = itemView.findViewById(R.id.layout_fan);
            }
        }
    }
}
