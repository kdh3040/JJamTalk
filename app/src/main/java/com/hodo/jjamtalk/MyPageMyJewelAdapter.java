package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.ViewHolder.MyJewelViewHolder;

/**
 * Created by mjk on 2017-10-01.
 */

public class MyPageMyJewelAdapter extends RecyclerView.Adapter<MyJewelViewHolder> {

    Context mContext;
    UIData mUIdata = UIData.getInstance();
    MyData mMyData = MyData.getInstance();

    Activity mActivity;

    public MyPageMyJewelAdapter(Context context,Activity activity) {
        super();
        mContext = context;
        mActivity = activity;

    }

    @Override
    public MyJewelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_jewel,null);

        return new MyJewelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyJewelViewHolder holder, final int position) {
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int nSellIdx = mMyData.itemIdx.get(position);
                final int[] nSellCount = {mMyData.itemList.get(nSellIdx)};
                final int nSellGold = mUIdata.getSellJewelValue()[nSellIdx];
                final String strSellItem = mUIdata.getItems()[nSellIdx];
                final String strSellItemRef = mUIdata.getItemsReference()[nSellIdx];

                if(mUIdata.bSellItemStatus == false)
                {
                    View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_exit_app, null, false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    final AlertDialog dialog = builder.setView(v).create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    TextView tv_title = v.findViewById(R.id.title);
                    tv_title.setText("골드로 교환할까요?");
                    TextView tv_msg = v.findViewById(R.id.msg);

                    tv_msg.setText(nSellGold + "골드로 교환할 수 있습니다.");
                    Button btn_yes = v.findViewById(R.id.btn_yes);
                    btn_yes.setText("네");
                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            mMyData.setUserHoney(mMyData.getUserHoney() + nSellGold);
                            mMyData.itemList.put(nSellIdx, --nSellCount[0]);
                            mMyData.SaveMyItem(nSellIdx, nSellCount[0]);
                            if(nSellCount[0] == 0) {
                                mMyData.itemList.remove(nSellIdx);
                                mMyData.nItemCount--;
                            }

                            mMyData.refreshItemIdex();
                            notifyDataSetChanged();
                            Intent intent = new Intent(mActivity, MyJewelBoxActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mActivity.startActivity(intent);
                            mActivity.finish();
                            mActivity.overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);

                            }
                        });

                        Button btn_no = v.findViewById(R.id.btn_no);
                        btn_no.setText("닫기");
                        btn_no.setOnClickListener(new View.OnClickListener() {

                            @Override

                            public void onClick(View view) {
                                dialog.dismiss();
                            }

                        });
                }

                else
                {
                    mUIdata.nSelItemType = nSellIdx;
                }

            }
        });

        int index = mMyData.itemIdx.get(position);
        holder.iv.setImageResource(mUIdata.getJewels()[index]);
        int count = mMyData.itemList.get(index);
        holder.tv.setText("x" + Integer.toString(count));

        holder.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(mUIdata.getWidth()/1,mUIdata.getHeight()/10));

    }

    @Override
    public int getItemCount() {
        return mMyData.nItemCount;
    }
}
