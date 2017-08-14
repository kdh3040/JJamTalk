package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;

import java.util.ArrayList;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class CardListActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    public UserData stTargetData = new UserData();
    private ArrayList<UserData> arrTargetData = new ArrayList<>();

    RecyclerView card_recylerview;
    private CardListAdapter cardListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        card_recylerview = (RecyclerView) findViewById(R.id.cardlist_recy);
        card_recylerview.setAdapter(new CardListAdapter());
        card_recylerview.setLayoutManager(new GridLayoutManager(this,3));
    }

    private class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_user,parent,false);

            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            int i = position;
            holder.image.setImageResource(R.mipmap.girl1);
            holder.textView.setText(mMyData.arrCardList.get(i).NickName + ", " + mMyData.arrCardList.get(i).Age + "세");
            arrTargetData.add(mMyData.arrCardList.get(i));

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //startActivity(new Intent(getApplicationContext(),UserPageActivity.class));
                    stTargetData = arrTargetData.get(position);
                    Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                    intent.putExtra("Target", stTargetData);
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return mMyData.arrCardList.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView image;
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                image = (ImageView)itemView.findViewById(R.id.iv_user);
                textView = (TextView)itemView.findViewById(R.id.tv_user);

            }
        }
    }
}
