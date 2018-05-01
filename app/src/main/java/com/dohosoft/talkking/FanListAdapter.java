package com.dohosoft.talkking;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.dohosoft.talkking.Data.FanData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.ViewHolder.FanViewHolder;

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
        //holder.constraintLayout.setLayoutParams(new_img LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(mUIData.getHeight()/7)));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mMyData.arrMyFanList.get(position).Idx.equals(mMyData.getUserIdx()))
                {
                    if(CommonFunc.getInstance().getClickStatus()==false)
                    {
                        getMyfanData(position);
                    }
                }
                else
                    CommonFunc.getInstance().ShowToast(mContext, "본인 입니다", false);



            }
        });
        //holder.imageView.setImageResource(R.mipmap.hdvd);

        String i = mMyData.arrMyFanList.get(position).Idx;

        Glide.with(mContext)
                .load(mMyData.arrMyFanDataList.get(i).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .thumbnail(0.1f)
                .into(holder.imageView);


        holder.nickname.setText(mMyData.arrMyFanDataList.get(i).NickName);
        holder.giftranking.setText((position + 1) + "위");

        int RecvCnt = mMyData.arrMyFanList.get(position).RecvGold;
        holder.giftCount.setText(Integer.toString(RecvCnt));

    }

    @Override
    public int getItemCount() {
        return mMyData.arrMyFanDataList.size();
    }

    public void moveFanPage(int position)
    {
        CommonFunc.getInstance().setClickStatus(false);

        String i = mMyData.arrMyFanList.get(position).Idx;

        Intent intent = new Intent(mContext, UserPageActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("Target", mMyData.mapMyFanData.get(mMyData.arrMyFanDataList.get(i).Idx));
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }


    public void getMyfanData(final int position) {
        CommonFunc.getInstance().setClickStatus(true);
        String i = mMyData.arrMyFanList.get(position).Idx;

        final String strTargetIdx = mMyData.arrMyFanDataList.get(i).Idx;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;

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
                            if(tempUserData != null)
                            {
                                mMyData.mapMyFanData.put(strTargetIdx, tempUserData);

             /*       for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                        mMyData.mapMyFanData.get(strTargetIdx).arrStarList.add(entry.getValue());
                    }*/

                                for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                                    mMyData.mapMyFanData.get(strTargetIdx).arrFanList.add(entry.getValue());
                                }

                                if(mMyData.mapMyFanData.get(strTargetIdx).arrFanList.size() == 0)
                                {
                                    moveFanPage(position);
                                }
                                else
                                {
                                    for(int i = 0 ;i < mMyData.mapMyFanData.get(strTargetIdx).arrFanList.size(); i++)
                                    {
                                        Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.mapMyFanData.get(strTargetIdx).arrFanList.get(i).Idx);
                                        final FanData finalTempFanData = mMyData.mapMyFanData.get(strTargetIdx).arrFanList.get(i);
                                        final int finalI = i;
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                                                if(DBData != null)
                                                {
                                                    mMyData.mapMyFanData.get(strTargetIdx).arrFanData.put(finalTempFanData.Idx, DBData);

                                                    if( finalI == mMyData.mapMyFanData.get(strTargetIdx).arrFanList.size() -1)
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
                            {
                                CommonFunc.getInstance().ShowToast(mContext, "사용자가 없습니다.", false);
                                CommonFunc.getInstance().setClickStatus(false);
                            }



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
        }
        else
        {
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
                    if(tempUserData != null)
                    {
                        mMyData.mapMyFanData.put(strTargetIdx, tempUserData);

             /*       for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                        mMyData.mapMyFanData.get(strTargetIdx).arrStarList.add(entry.getValue());
                    }*/

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                            mMyData.mapMyFanData.get(strTargetIdx).arrFanList.add(entry.getValue());
                        }

                        if(mMyData.mapMyFanData.get(strTargetIdx).arrFanList.size() == 0)
                        {
                            moveFanPage(position);
                        }
                        else
                        {
                            for(int i = 0 ;i < mMyData.mapMyFanData.get(strTargetIdx).arrFanList.size(); i++)
                            {
                                Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.mapMyFanData.get(strTargetIdx).arrFanList.get(i).Idx);
                                final FanData finalTempFanData = mMyData.mapMyFanData.get(strTargetIdx).arrFanList.get(i);
                                final int finalI = i;
                                data.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                                        if(DBData != null)
                                        {
                                            mMyData.mapMyFanData.get(strTargetIdx).arrFanData.put(finalTempFanData.Idx, DBData);

                                            if( finalI == mMyData.mapMyFanData.get(strTargetIdx).arrFanList.size() -1)
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
                    {
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
