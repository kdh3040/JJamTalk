package com.hodo.jjamtalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SimpleUserData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class CardListFragment extends Fragment {
    @Nullable


    private MyData mMyData = MyData.getInstance();
    public UserData stTargetData = new UserData();
    private ArrayList<UserData> arrTargetData = new ArrayList<>();

    RecyclerView card_recylerview;
    private CardListAdapter cardListAdapter = new CardListAdapter();
    private Context mContext;
    private UIData mUIData = UIData.getInstance();

    View fragView;

    private void refreshFragMent()
    {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.detach(this).attach(this).commit();
    }


    public CardListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (fragView!= null) {
            cardListAdapter.notifyDataSetChanged();
        }
        else
        {
            fragView = inflater.inflate(R.layout.fragment_card_list,container,false);
            card_recylerview = fragView.findViewById(R.id.cardlist_recy);
            card_recylerview.setAdapter(cardListAdapter);
            card_recylerview.setLayoutManager(new LinearLayoutManager(getContext()));
            cardListAdapter.notifyDataSetChanged();
            mContext = getContext();
        }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return fragView;
    }

    private class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_my_card,parent,false);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/7));

            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final String i = mMyData.arrCardNameList.get(position);
            //holder.image.setImageResource(R.mipmap.girl1);

            Glide.with(mContext)
                    .load(mMyData.arrCarDataList.get(i).Img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .thumbnail(0.1f)
                    .into(holder.image);

            //if(mMyData.arrCardNameList.get(position).Count != 0)
                holder.imageSymbol.setVisibility(View.INVISIBLE);

            if(mMyData.arrCarDataList.get(i).Memo == null || mMyData.arrCarDataList.get(i).Memo.equals(""))
                holder.textView_memo.setText("안녕하세요");
            else
                holder.textView_memo.setText(mMyData.arrCarDataList.get(i).Memo);
            holder.textView_memo.setBackgroundResource(R.drawable.inbox2);

   /*         holder.imageItem.setVisibility(View.GONE);
            holder.imageGrade.setVisibility(View.GONE);*/

           // holder.imageItem.setImageResource(mUIData.getJewels()[mMyData.arrCarDataList.get(i).BestItem -1 ]);
         //   holder.imageGrade.setImageResource(mUIData.getGrades()[mMyData.arrCarDataList.get(i).Grade]);


            holder.textView.setText(mMyData.arrCarDataList.get(i).NickName);// + ", " + mMyData.arrCardNameList.get(i).Age + "세");

            holder.textView.setText(mMyData.arrCarDataList.get(i).NickName);// + ", " + mMyData.arrCardNameList.get(i).Age + "세");
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_exit_app, null, false);


                    final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(v).create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    final Button btn_exit;
                    final Button btn_no;
                    final TextView tv_title;
                    final TextView tv_msg;

                    tv_title = v.findViewById(R.id.title);
                    tv_msg = v.findViewById(R.id.msg);

                    tv_title.setText("삭제");
                    tv_msg.setText("내 카드에서 삭제하시겠습니까?");

                    btn_exit = (Button) v.findViewById(R.id.btn_yes);
                    btn_exit.setText("삭제");
                    btn_exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference table;
                            table = database.getReference("User/" + mMyData.getUserIdx()+ "/CardList/");
                            table.child(mMyData.arrCarDataList.get(i).Idx).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().removeValue();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });
                            mMyData.arrCardNameList.remove(position);
                            mMyData.arrCarDataList.remove(i);

                            refreshFragMent();
                            dialog.dismiss();
                        }
                    });

                    btn_no = (Button) v.findViewById(R.id.btn_no);
                    btn_no.setText("취소");
                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    return false;
                }
            });

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //mMyData.arrCardNameList.get(position).Count = 1;

           /*         FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference table;
                    table = database.getReference("User/" + mMyData.getUserIdx());

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("Count",  mMyData.arrCardNameList.get(position).Count);
                    table.child("CardList").child(mMyData.arrCardNameList.get(position).Idx).updateChildren(updateMap);*/

                    getMyCardData(position);

                    //stTargetData = arrTargetData.get(position);

                }
            });

        }

        @Override
        public int getItemCount() {
            return mMyData.arrCarDataList.size();
        }

        public void moveCardPage(int position)
        {
            final String i = mMyData.arrCardNameList.get(position);
            stTargetData = mMyData.mapMyCardData.get(mMyData.arrCarDataList.get(i).Idx);
            Intent intent = new Intent(mContext, UserPageActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", stTargetData);
 /*           intent.putExtra("FanList", stTargetData.arrFanList);
            intent.putExtra("FanCount", stTargetData.FanCount);

            intent.putExtra("StarList", stTargetData.arrStarList);*/

            intent.putExtras(bundle);
            startActivity(intent);
            notifyDataSetChanged();
        }


        public void getMyCardData(final int position) {
            final String i = mMyData.arrCardNameList.get(position);
            final String strTargetIdx = mMyData.arrCarDataList.get(i).Idx;
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
                        mMyData.mapMyCardData.put(strTargetIdx, tempUserData);

                        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                            mMyData.mapMyCardData.get(strTargetIdx).arrStarList.add(entry.getValue());
                        }

                        for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.FanList.entrySet()) {
                            mMyData.mapMyCardData.get(strTargetIdx).arrFanList.add(entry.getValue());
                        }

                        RefreshUserCardSimpleData(tempUserData, position);
                        moveCardPage(position);
                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }


        public void RefreshUserCardSimpleData(UserData stTargetData, int position) {

            final String i = mMyData.arrCardNameList.get(position);
          /*  FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());

            // DatabaseReference user = table.child( userIdx);
            final DatabaseReference user = table.child(mMyData.getUserIdx()).child("CardList").child(stTargetData.Idx);
            user.child("Age").setValue(stTargetData.Age);
            user.child("FanCount").setValue(stTargetData.FanCount);
            user.child("Gender").setValue(stTargetData.Gender);
            user.child("Idx").setValue(stTargetData.Idx);
            user.child("Img").setValue(stTargetData.Img);
            user.child("Lon").setValue(stTargetData.Lon);
            user.child("Lat").setValue(stTargetData.Lat);
            user.child("Memo").setValue(stTargetData.Memo);
            user.child("NickName").setValue(stTargetData.NickName);

            Random rand = new Random();
            rand.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성

            user.child("Point").setValue(Integer.valueOf(Integer.toString(rand.nextInt(100))));
            user.child("RecvGold").setValue(stTargetData.RecvCount);
            user.child("SendGold").setValue(stTargetData.SendCount);*/


            mMyData.arrCarDataList.get(i).Img = stTargetData.Img;
            mMyData.arrCarDataList.get(i).NickName = stTargetData.NickName;
            mMyData.arrCarDataList.get(i).Memo = stTargetData.Memo;

        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageSymbol;
            public ImageView image;
         //   public ImageView imageItem, imageGrade;
            public TextView textView, textView_memo;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                imageSymbol = (ImageView)itemView.findViewById(R.id.cardlist_newSymbol);
                image = (ImageView)itemView.findViewById(R.id.iv_my_card);

               // imageItem = (ImageView)itemView.findViewById(R.id.iv_item);
              //  imageGrade = (ImageView)itemView.findViewById(R.id.iv_grade);

                textView = (TextView)itemView.findViewById(R.id.tv_nickname);
                textView_memo = (TextView)itemView.findViewById(R.id.tv_memo);
                linearLayout = itemView.findViewById(R.id.layout_mycard_item);

            }
        }
    }
}
