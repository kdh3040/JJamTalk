package com.hodo.jjamtalk;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SimpleUserData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class FanListFragment extends Fragment {
    @Nullable


    private MyData mMyData = MyData.getInstance();
    public UserData stTargetData = new UserData();
    private ArrayList<UserData> arrTargetData = new ArrayList<>();

    RecyclerView fan_recylerview;
    private MyFanListAdapter fanListAdapter = new MyFanListAdapter();
    private Context mContext;
    private UIData mUIData = UIData.getInstance();

    View fragView;

    private void refreshFragMent()
    {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.detach(this).attach(this).commit();
    }


    public FanListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (fragView!= null) {
            fanListAdapter.notifyDataSetChanged();
        }
        else
        {
            fragView = inflater.inflate(R.layout.fragment_fan_list,container,false);
            fan_recylerview = fragView.findViewById(R.id.fanlist_recy);
            fan_recylerview.setAdapter(fanListAdapter);
            fan_recylerview.setLayoutManager(new LinearLayoutManager(getContext()));
            fanListAdapter.notifyDataSetChanged();
            mContext = getContext();
        }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return fragView;
    }

    private class MyFanListAdapter extends RecyclerView.Adapter<MyFanListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_my_fan, parent, false);

            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getMyfanData(position);

                }
            });
            //holder.imageView.setImageResource(R.mipmap.hdvd);

            String i = mMyData.arrMyFanList.get(position).Idx;

            Glide.with(mContext)
                    .load(mMyData.arrMyFanDataList.get(i).Img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .thumbnail(0.1f)
                    .into(holder.img);

            if(mMyData.arrMyFanDataList.get(i).BestItem == 0)
                //imgBestItem.setImageResource(R.drawable.gold);
                holder.imageItem.setVisibility(View.INVISIBLE);
            else
            {
                holder.imageItem.setVisibility(View.VISIBLE);
                holder.imageItem.setImageResource(mUIData.getJewels()[mMyData.arrMyFanDataList.get(i).BestItem - 1]);
            }

            holder.imageGrade.setImageResource(mUIData.getGrades()[mMyData.arrMyFanDataList.get(i).Grade]);

            holder.textNick.setText(mMyData.arrMyFanDataList.get(i).NickName);
            holder.textRank.setText((position + 1) + "위");

            int RecvCnt = mMyData.arrMyFanList.get(position).RecvGold * -1;
            holder.textCount.setText(Integer.toString(RecvCnt) + "골드");

        }

        @Override
        public int getItemCount() {
            return mMyData.arrMyFanDataList.size();
        }

        public void moveFanPage(int position) {
            String i = mMyData.arrMyFanList.get(position).Idx;

            Intent intent = new Intent(mContext, UserPageActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", mMyData.mapMyFanData.get(mMyData.arrMyFanDataList.get(i).Idx));
            intent.putExtras(bundle);

            mContext.startActivity(intent);
        }


        public void getMyfanData(final int position) {
            String i = mMyData.arrMyFanList.get(position).Idx;

            final String strTargetIdx = mMyData.arrMyFanDataList.get(i).Idx;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = null;
            table = database.getReference("User");

            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int saa = 0;
                    UserData tempUserData = dataSnapshot.getValue(UserData.class);
                    if (tempUserData != null) {
                        mMyData.mapMyFanData.put(strTargetIdx, tempUserData);

                        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                            mMyData.mapMyFanData.get(strTargetIdx).arrStarList.add(entry.getValue());
                        }

                        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.FanList.entrySet()) {
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
        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView img;
            public TextView textRank, textNick, textCount;
            public ConstraintLayout constraintLayout;

            public ImageView imageGrade, imageItem;

            public ViewHolder(View itemView) {
                super(itemView);
                img = (ImageView)itemView.findViewById(R.id.iv_fan);
                textRank = (TextView)itemView.findViewById(R.id.tv_gift_ranking);
                textNick = (TextView)itemView.findViewById(R.id.tv_nickname);
                textCount = (TextView)itemView.findViewById(R.id.tv_gift_count);
                constraintLayout = itemView.findViewById(R.id.layout_fan);
                imageGrade = itemView.findViewById(R.id.iv_grade);
                imageItem= itemView.findViewById(R.id.iv_item);
            }
        }
    }
}
