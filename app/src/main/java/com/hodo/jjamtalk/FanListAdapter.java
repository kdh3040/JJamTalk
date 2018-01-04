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

public class FanListAdapter extends RecyclerView.Adapter<FanViewHolder>{

    Context mContext;
    UIData mUIData = UIData.getInstance();
    private MyData mMyData = MyData.getInstance();

    public FanListAdapter(Context context) {
        mContext = context;

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

                /*Intent intent = new Intent(mContext, UserPageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Target", mMyData.arrMyFanDataList.get(position));
                intent.putExtras(bundle);

                view.getContext().startActivity(intent);
*/
                getMyfanData(position);

            }
        });
        //holder.imageView.setImageResource(R.mipmap.hdvd);

        Glide.with(mContext)
                .load(mMyData.arrMyFanList.get(position).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .thumbnail(0.1f)
                .into(holder.imageView);

        holder.nickname.setText(mMyData.arrMyFanList.get(position).Nick);
        holder.giftranking.setText((position + 1) + "위");

        int SendCnt = mMyData.arrMyFanList.get(position).Count * -1;
        holder.giftCount.setText(Integer.toString(SendCnt) + "골드");

    }

    @Override
    public int getItemCount() {
        return mMyData.arrMyFanList.size();
    }

    public void moveFanPage(int position)
    {
        Intent intent = new Intent(mContext, UserPageActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("Target", mMyData.mapMyFanData.get(mMyData.arrMyFanList.get(position).Idx));
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }


    public void getMyfanData(final int position) {
        final String strTargetIdx = mMyData.arrMyFanList.get(position).Idx;
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
                    mMyData.mapMyFanData.put(strTargetIdx, tempUserData);

                    for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.StarList.entrySet()) {
                        mMyData.mapMyFanData.get(strTargetIdx).arrStarList.add(entry.getValue());
                    }

                    for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                        mMyData.mapMyFanData.get(strTargetIdx).arrFanList.add(entry.getValue());
                    }

                    moveFanPage(position);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

}
