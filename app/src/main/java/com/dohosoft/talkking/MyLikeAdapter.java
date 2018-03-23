package com.dohosoft.talkking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.dohosoft.talkking.Data.FanData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.ViewHolder.MyLikeViewHolder;

import java.util.LinkedHashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by mjk on 2017. 8. 28..
 */

public class MyLikeAdapter extends RecyclerView.Adapter<MyLikeViewHolder> {
    Context mContext;
    UIData mUIData = UIData.getInstance();

    private MyData mMyData = MyData.getInstance();

    public MyLikeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyLikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_my_like,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //유저페이지 액티비티
            }
        });

        return new MyLikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyLikeViewHolder holder, final int position) {

        String i = mMyData.arrMyStarList.get(position).Idx;

        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/7)));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getMyLikeData(position);

            }
        });
        //holder.imageView.setImageResource(R.mipmap.hdvd);

        Glide.with(mContext)
                .load(mMyData.arrMyStarDataList.get(i).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .thumbnail(0.1f)
                .into(holder.imageView);

        holder.tv_nickname.setText(mMyData.arrMyStarDataList.get(i).NickName);
        holder.tv_rank.setText((position + 1) + "위");

        int SendCnt = mMyData.arrMyStarList.get(position).SendGold * -1;
        holder.tv_honeycount.setText(Integer.toString(SendCnt) + "하트");
    }

    @Override
    public int getItemCount() {
        return mMyData.arrMyStarDataList.size();
    }

    public void moveLikePage(int position)
    {
        String i = mMyData.arrMyStarList.get(position).Idx;

        Intent intent = new Intent(mContext, UserPageActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("Target", mMyData.mapMyStarData.get(mMyData.arrMyStarDataList.get(i).Idx));
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }


    public void getMyLikeData(final int position) {
        String i = mMyData.arrMyStarList.get(position).Idx;
        final String strTargetIdx = mMyData.arrMyStarDataList.get(i).Idx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int saa = 0;
                UserData tempUserData = dataSnapshot.getValue(UserData.class);
                if(tempUserData != null)
                {
                    mMyData.mapMyStarData.put(strTargetIdx, tempUserData);

                /*    for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                        mMyData.mapMyStarData.get(strTargetIdx).arrStarList.add(entry.getValue());
                    }*/

                    for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                        mMyData.mapMyStarData.get(strTargetIdx).arrFanList.add(entry.getValue());
                    }

                    moveLikePage(position);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


}
