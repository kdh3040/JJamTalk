package com.hodo.talkking;

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
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.ViewHolder.FanViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class TargetFanAdapter extends RecyclerView.Adapter<FanViewHolder>{

    Context mContext;
    UIData mUIData = UIData.getInstance();
    private ArrayList<SimpleUserData> stTargetData;

    private UserData tempFanData = new UserData();
    private UserData tempSendUserData = new UserData();

    public TargetFanAdapter(Context context, ArrayList<SimpleUserData> TargetData) {
        mContext = context;
        stTargetData = TargetData;
    }

    @Override
    public FanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_my_fan,parent,false);

        return new FanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FanViewHolder holder, final int position) {
        holder.constraintLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/7)));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetfanData(position);
            }
        });

       Glide.with(mContext)
                .load(stTargetData.get(position).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .thumbnail(0.1f)
                .into(holder.imageView);

        holder.nickname.setText(stTargetData.get(position).NickName);
        holder.giftranking.setText((position + 1) + "위");

        int RecvCnt = stTargetData.get(position).RecvGold * -1;
        holder.giftCount.setText(Integer.toString(RecvCnt) + "하트");


        if(stTargetData.get(position).BestItem == 0)
            //holder.imageItem.setImageResource(mUIData.getJewels()[mMyData.arrCarDataList.get(i).BestItem]);
            holder.imageBest.setVisibility(View.INVISIBLE);
        else {
            holder.imageBest.setVisibility(View.VISIBLE);
            holder.imageBest.setImageResource(mUIData.getJewels()[stTargetData.get(position).BestItem - 1]);
        }

        holder.imageGrade.setImageResource(mUIData.getGrades()[stTargetData.get(position).Grade]);

    }

    @Override
    public int getItemCount() {
        return stTargetData.size();
    }


    public void moveFanPage(int position)
    {
        Intent intent = new Intent(mContext, UserPageActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("Target", tempFanData.mapFanData.get(stTargetData.get(position).Idx));
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }


    public void getTargetfanData(final int position) {

        final String strTargetIdx = stTargetData.get(position).Idx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

            if (strTargetIdx != null)
            {
                table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int saa = 0;
                        UserData tempUserData = dataSnapshot.getValue(UserData.class);
                        if(tempUserData != null)
                        {
                            tempFanData.mapFanData.put(strTargetIdx, tempUserData);

                            for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet())
                                tempFanData.mapFanData.get(strTargetIdx).arrStarList.add(entry.getValue());

                            for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.FanList.entrySet())
                                tempFanData.mapFanData.get(strTargetIdx).arrFanList.add(entry.getValue());

                            moveFanPage(position);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }


                });
            }
    }

}
