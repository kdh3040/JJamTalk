package com.hodo.talkking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.talkking.Data.FanData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.Data.UserData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by mjk on 2017. 8. 28..
 */

public class TargetLikeAdapter extends RecyclerView.Adapter<TargetLikeViewHolder> {
    Context mContext;
    UIData mUIData = UIData.getInstance();

    private UserData tempLikeData = new UserData();
    private UserData stTargetData = new UserData();

    public TargetLikeAdapter(Context context,  UserData TargetData) {
        mContext = context;
        stTargetData = TargetData;
    }

    @Override
    public TargetLikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_image_only,parent,false);

        return new TargetLikeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(TargetLikeViewHolder holder, final int position) {

        String i = stTargetData.arrFanList.get(position).Idx;
        Glide.with(mContext)
                .load(stTargetData.arrFanData.get(i).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .thumbnail(0.1f)
                .into(holder.iv_liked);

        /*holder.iv_liked.setOnClickListener(new_img View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetLikeData(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return stTargetData.arrFanList.size();
    }

    public void moveLikePage(int position)
    {
        Intent intent = new Intent(mContext, UserPageActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("Target", tempLikeData.mapStarData.get(stTargetData.arrFanList.get(position).Idx));
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }

    public void getTargetLikeData(final int position) {
        final String strTargetIdx = stTargetData.arrFanList.get(position).Idx;

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
                            tempLikeData.mapStarData.put(strTargetIdx, tempUserData);

             /*               for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet())
                                tempLikeData.mapStarData.get(strTargetIdx).arrStarList.add(entry.getValue());*/

                            for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet())
                                tempLikeData.mapStarData.get(strTargetIdx).arrFanList.add(entry.getValue());

                            moveLikePage(position);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
            }
        }
}
