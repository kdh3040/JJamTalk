package com.hodo.jjamtalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    /*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        card_recylerview = (RecyclerView) findViewById(R.id.cardlist_recy);
        card_recylerview.setAdapter(new CardListAdapter());
        card_recylerview.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/
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
                    .load(mMyData.arrCardList.get(position).Img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .thumbnail(0.1f)
                    .into(holder.image);

            if(mMyData.arrCardNameList.get(position).Count != 0)
                holder.imageSymbol.setVisibility(View.INVISIBLE);

            holder.textView.setText(mMyData.arrCardList.get(i).NickName + ", " + mMyData.arrCardList.get(i).Age + "세");
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder br = new AlertDialog.Builder(mContext);
                    br.setTitle("내 카드에서 삭제하시겠습니까?");

                    br.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 취소 구현
                        }
                    }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference table;
                            table = database.getReference("User/" + mMyData.getUserIdx()+ "/CardList/");
                            table.child(mMyData.arrCardList.get(position).Idx).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().removeValue();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });

                            mMyData.arrCardNameList.remove(position);
                            mMyData.arrCardList.remove(position);

                            refreshFragMent();
                     /*   Intent in = new Intent(getContext(), MainActivity.class);
                        startActivity(in);*/
                        }
                    });
                    AlertDialog dialog = br.create();
                    dialog.show();
                    return false;

                }
            });
            arrTargetData.add(mMyData.arrCardList.get(i));

            //holder.linearLayout.setLayoutParams(mUIData.getLLP_ListItem());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //startActivity(new Intent(getApplicationContext(),UserPageActivity.class));
                    mMyData.arrCardNameList.get(position).Count = 1;

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference table;
                    table = database.getReference("User/" + mMyData.getUserIdx());

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("Count",  mMyData.arrCardNameList.get(position).Count);
                    table.child("CardList").child(arrTargetData.get(position).Idx).updateChildren(updateMap);


                    stTargetData = arrTargetData.get(position);
                    Intent intent = new Intent(getContext(), UserPageActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("Target", stTargetData);
                    intent.putExtra("FanList", stTargetData.arrFanList);
                    intent.putExtra("FanCount", stTargetData.FanCount);

                    intent.putExtra("StarList", stTargetData.arrStarList);

                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });

        }



        @Override
        public int getItemCount() {
            return mMyData.arrCardList.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageSymbol;
            public ImageView image;
            public TextView textView;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                imageSymbol = (ImageView)itemView.findViewById(R.id.cardlist_newSymbol);
                image = (ImageView)itemView.findViewById(R.id.iv_my_card);
                textView = (TextView)itemView.findViewById(R.id.tv_nickname);
                linearLayout = itemView.findViewById(R.id.layout_mycard_item);

            }
        }
    }
}
