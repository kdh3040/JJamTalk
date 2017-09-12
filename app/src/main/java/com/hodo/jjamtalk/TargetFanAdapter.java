package com.hodo.jjamtalk;

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
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.ViewHolder.FanViewHolder;

import java.util.LinkedHashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class TargetFanAdapter extends RecyclerView.Adapter<FanViewHolder>{

    Context mContext;
    UIData mUIData = UIData.getInstance();
    private UserData stTargetData;

    public TargetFanAdapter(Context context, UserData TargetData) {
        mContext = context;
        stTargetData = TargetData;
        getTargetfanData();
    }

    @Override
    public FanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_my_fan,parent,false);



        return new FanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FanViewHolder holder, final int position) {
        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/7)));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mContext.startActivity(new Intent(mContext,UserPageActivity.class));

                Intent intent = new Intent(mContext, UserPageActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("Target", stTargetData.arrFanData.get(position));
                intent.putExtra("FanList", stTargetData.arrFanData.get(position).arrFanList);
                intent.putExtra("StarList", stTargetData.arrFanData.get(position).arrFanList);
                intent.putExtras(bundle);

                view.getContext().startActivity(intent);


            }
        });
        //holder.imageView.setImageResource(R.mipmap.hdvd);

        Glide.with(mContext)
                .load(stTargetData.arrFanData.get(position).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .thumbnail(0.1f)
                .into(holder.imageView);

        holder.nickname.setText(stTargetData.arrFanList.get(position).Nick);
        holder.giftranking.setText((position + 1) + "위");

        int SendCnt = stTargetData.arrFanList.get(position).Count * -1;
        holder.giftCount.setText(Integer.toString(SendCnt) + "꿀");

        /*holder.nickname.setText("아이유");
        holder.giftranking.setText("1위");
        holder.giftCount.setText("313123꿀");
        holder.imageView.setImageResource(R.mipmap.hdvd);*/

    }

    @Override
    public int getItemCount() {
        return stTargetData.arrFanData.size();
    }


    public void getTargetfanData() {
        String strTargetIdx;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("User");

        //user.addChildEventListener(new ChildEventListener() {
        for (int i = 0; i < stTargetData.arrFanList.size(); i++) {
            strTargetIdx = stTargetData.arrFanList.get(i).Idx;

            if (strTargetIdx != null)
            {

                final int finalI = i;
                table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int saa = 0;
                        UserData tempUserData = dataSnapshot.getValue(UserData.class);
                        stTargetData.arrFanData.add(tempUserData);

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet())
                            stTargetData.arrFanData.get(finalI).arrFanList.add(entry.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
            }
        }
    }

}
