package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.BoardMsgClientData;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.SimpleUserData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.CommonFunc;
import com.hodo.jjamtalk.ViewHolder.BoardViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.hodo.jjamtalk.Data.CoomonValueData.MAIN_ACTIVITY_BOARD;

/**
 * Created by mjk on 2017. 9. 14..
 */

public class UserFanActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    private UserData stTargetData;
    public ArrayList<FanData> FanList = new ArrayList<>();
    public ArrayList<UserData> FanData = new ArrayList<>();
    private UIData mUIData = UIData.getInstance();

    Activity mActivity;
    RecyclerView UserFanRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_user_fan_club);

        UserFanRecycler = (RecyclerView)findViewById(R.id.user_fan_list);
        UserFanRecycler.setAdapter(new UserFanActivity.UserFanListAdapter());
        UserFanRecycler.setLayoutManager(new LinearLayoutManager(this));

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        Intent intent = getIntent();
        Bundle bundle = this.getIntent().getExtras();
        stTargetData = (UserData) bundle.getSerializable("Target");

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
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getMyfanData(position);

                }
            });
            //holder.imageView.setImageResource(R.mipmap.hdvd);

            String i = stTargetData.arrFanList.get(position).Idx;

            Glide.with(mActivity)
                    .load(stTargetData.mapFanData.get(i).Img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mActivity))
                    .thumbnail(0.1f)
                    .into(holder.img);

            holder.textNick.setText(stTargetData.mapFanData.get(i).NickName);
            holder.textRank.setText((position + 1) + "위");

            int RecvCnt = stTargetData.arrFanList.get(position).RecvGold * -1;
            holder.textCount.setText(Integer.toString(RecvCnt) + "골드");

        }


        @Override
        public int getItemCount() {
            return stTargetData.arrFanList.size();
        }

        public void moveFanPage(int position) {
            String i = stTargetData.arrFanList.get(position).Idx;

            Intent intent = new Intent(mActivity, UserPageActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", stTargetData.mapFanData.get(i));
            intent.putExtras(bundle);

            mActivity.startActivity(intent);
        }


        public void getMyfanData(final int position) {
            String i = stTargetData.arrFanList.get(position).Idx;

            final String strTargetIdx = stTargetData.mapFanData.get(i).Idx;
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



                 /*       for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.FanList.entrySet()) {
                            stTargetData.mapFanData.get(strTargetIdx).arrFanList.add(entry.getValue());
                        }*/

                        moveFanPage(position);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView img;
            public TextView textRank, textNick, textCount;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                img = (ImageView)itemView.findViewById(R.id.iv_fan);
                textRank = (TextView)itemView.findViewById(R.id.tv_gift_ranking);
                textNick = (TextView)itemView.findViewById(R.id.tv_nickname);
                textCount = (TextView)itemView.findViewById(R.id.tv_gift_count);
                linearLayout = itemView.findViewById(R.id.layout_fan);
            }
        }
    }
}
