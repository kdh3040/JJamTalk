package com.dohosoft.talkking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.dohosoft.talkking.Data.FanData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.ViewHolder.FanViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class TargetFanAdapter extends RecyclerView.Adapter<FanViewHolder>{

    Context mContext;
    UIData mUIData = UIData.getInstance();
    private ArrayList<FanData> stTargetData;

    private UserData tempFanData = new UserData();
    private UserData tempSendUserData = new UserData();

    public TargetFanAdapter(Context context, ArrayList<FanData> TargetData) {
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
                if(CommonFunc.getInstance().getClickStatus() == false)
                    getTargetfanData(position);
            }
        });

       /*Glide.with(mContext)
                .load(stTargetData.get(position).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new_img CropCircleTransformation(mContext))
                .thumbnail(0.1f)
                .into(holder.imageView);

        holder.nickname.setText(stTargetData.get(position).NickName);
        holder.giftranking.setText((position + 1) + "위");

        int RecvCnt = stTargetData.get(position).RecvGold;
        holder.giftCount.setText(Integer.toString(RecvCnt) + "하트");

        holder.imageBest.setVisibility(View.VISIBLE);

        if(stTargetData.get(position).BestItem == 0)
        {
            holder.imageBest.setImageResource(R.mipmap.randombox);
        }
            //holder.imageItem.setImageResource(mUIData.getJewels()[mMyData.arrCarDataList.get(i).BestItem]);

        else {
            holder.imageBest.setImageResource(mUIData.getJewels()[stTargetData.get(position).BestItem]);
        }

        holder.imageGrade.setImageResource(mUIData.getGrades()[stTargetData.get(position).Grade]);*/

    }

    @Override
    public int getItemCount() {
        return stTargetData.size();
    }


    public void moveFanPage(int position)
    {
        CommonFunc.getInstance().setClickStatus(false);

        Intent intent = new Intent(mContext, UserPageActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("Target", tempFanData.mapFanData.get(stTargetData.get(position).Idx));
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }


    public void getTargetfanData(final int position) {
        CommonFunc.getInstance().setClickStatus(true);
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

                     /*       for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet())
                                tempFanData.mapFanData.get(strTargetIdx).arrStarList.add(entry.getValue());
*/
                            for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet())
                                tempFanData.mapFanData.get(strTargetIdx).arrFanList.add(entry.getValue());


                            if(tempFanData.mapFanData.get(strTargetIdx).arrFanList.size() == 0)
                            {
                                moveFanPage(position);
                            }
                            else
                            {
                                for(int i = 0 ;i < tempFanData.mapFanData.get(strTargetIdx).arrFanList.size(); i++)
                                {
                                    Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(tempFanData.mapFanData.get(strTargetIdx).arrFanList.get(i).Idx);
                                    final FanData finalTempFanData = tempFanData.mapFanData.get(strTargetIdx).arrFanList.get(i);
                                    final int finalI = i;
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                                            if(DBData != null)
                                            {
                                                tempFanData.mapFanData.get(strTargetIdx).arrFanData.put(finalTempFanData.Idx, DBData);

                                                if( finalI == tempFanData.mapFanData.get(strTargetIdx).arrFanList.size() -1)
                                                {
                                                    moveFanPage(position);
                                                }
                                            }
                                            else{
                                                CommonFunc.getInstance().ShowToast(mContext, "사용자가 없습니다.", false);
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
    }

}
