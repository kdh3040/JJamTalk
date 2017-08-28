package com.hodo.jjamtalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        mContext = this;
        card_recylerview = (RecyclerView) findViewById(R.id.cardlist_recy);
        card_recylerview.setAdapter(new CardListAdapter());
        card_recylerview.setLayoutManager(new LinearLayoutManager(this));
    }

    private class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_my_card,parent,false);

            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            int i = position;
            holder.image.setImageResource(R.mipmap.girl1);

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
                            //확인 구현
                        }
                    });
                    AlertDialog dialog = br.create();
                    dialog.show();
                    return false;

                }
            });
            arrTargetData.add(mMyData.arrCardList.get(i));

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //startActivity(new Intent(getApplicationContext(),UserPageActivity.class));
                    stTargetData = arrTargetData.get(position);
                    Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("Target", stTargetData);
                    intent.putExtra("FanList", stTargetData.arrFanList);
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
            public ImageView image;
            public TextView textView;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                image = (ImageView)itemView.findViewById(R.id.iv_my_card);
                textView = (TextView)itemView.findViewById(R.id.tv_nickname);
                linearLayout = itemView.findViewById(R.id.layout_my_card);

            }
        }
    }
}
