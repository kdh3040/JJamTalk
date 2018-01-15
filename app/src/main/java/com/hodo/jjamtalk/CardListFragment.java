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
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Data.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
            int i = position;
            //holder.image.setImageResource(R.mipmap.girl1);

            Glide.with(mContext)
                    .load(mMyData.arrCardNameList.get(position).Img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .thumbnail(0.1f)
                    .into(holder.image);

            if(mMyData.arrCardNameList.get(position).Count != 0)
                holder.imageSymbol.setVisibility(View.INVISIBLE);

            if(mMyData.arrCardNameList.get(i).Memo == null || mMyData.arrCardNameList.get(i).Memo.equals(""))
                holder.textView_memo.setText("안녕하세요");
            else
                holder.textView_memo.setText(mMyData.arrCardNameList.get(i).Memo);
            holder.textView_memo.setBackgroundResource(R.drawable.inbox2);


            holder.textView.setText(mMyData.arrCardNameList.get(i).Nick);// + ", " + mMyData.arrCardNameList.get(i).Age + "세");
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
                            table.child(mMyData.arrCardNameList.get(position).Idx).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().removeValue();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });

                            mMyData.arrCardNameList.remove(position);

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

                    mMyData.arrCardNameList.get(position).Count = 1;

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference table;
                    table = database.getReference("User/" + mMyData.getUserIdx());

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("Count",  mMyData.arrCardNameList.get(position).Count);
                    table.child("CardList").child(mMyData.arrCardNameList.get(position).Idx).updateChildren(updateMap);

                    getMyCardData(position);

                    //stTargetData = arrTargetData.get(position);

                }
            });

        }

        @Override
        public int getItemCount() {
            return mMyData.arrCardNameList.size();
        }

        public void moveCardPage(int position)
        {
            stTargetData = mMyData.mapMyCardData.get(mMyData.arrCardNameList.get(position).Idx);
            Intent intent = new Intent(mContext, UserPageActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", stTargetData);
 /*           intent.putExtra("FanList", stTargetData.arrFanList);
            intent.putExtra("FanCount", stTargetData.FanCount);

            intent.putExtra("StarList", stTargetData.arrStarList);*/

            intent.putExtras(bundle);

            startActivity(intent);
        }


        public void getMyCardData(final int position) {
            final String strTargetIdx = mMyData.arrCardNameList.get(position).Idx;
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

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.StarList.entrySet()) {
                            mMyData.mapMyCardData.get(strTargetIdx).arrStarList.add(entry.getValue());
                        }

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                            mMyData.mapMyCardData.get(strTargetIdx).arrFanList.add(entry.getValue());
                        }

                        moveCardPage(position);
                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageSymbol;
            public ImageView image;
            public TextView textView, textView_memo;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                imageSymbol = (ImageView)itemView.findViewById(R.id.cardlist_newSymbol);
                image = (ImageView)itemView.findViewById(R.id.iv_my_card);
                textView = (TextView)itemView.findViewById(R.id.tv_nickname);
                textView_memo = (TextView)itemView.findViewById(R.id.tv_memo);
                linearLayout = itemView.findViewById(R.id.layout_mycard_item);

            }
        }
    }
}
