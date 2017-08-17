package com.hodo.jjamtalk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hodo.jjamtalk.Data.MyData;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class HeartActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private TextView txt_heartStatus;
    private ListView HeartChargeList;

    ArrayList<HeartItem> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_charge);

        txt_heartStatus = (TextView)findViewById(R.id.Heart_MyHeart);
        txt_heartStatus.setText("현재 보유 꿀 갯수 : " + mMyData.getUserHoney());

        HeartChargeList = (ListView)findViewById(R.id.Heart_list);

        list = new ArrayList<HeartItem>();

        HeartItem mHeartItem;
        mHeartItem = new HeartItem(R.drawable.heart_icon, "하트 50개 5천원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.heart_icon, "하트 70개 7천원");
        list.add(mHeartItem);

        mHeartItem = new HeartItem(R.drawable.heart_icon, "하트 100개 1만원");
        list.add(mHeartItem);

        HeartItemAdapter adapter = new HeartItemAdapter(this, R.layout.content_cash_charge, list);

        HeartChargeList.setAdapter(adapter);
    }
    public void refreshHearCnt()
    {
        txt_heartStatus.setText("현재 보유 꿀 갯수 : " + mMyData.getUserHoney());
    }


    class HeartItem {

        int Img_Heart;
        String txt_Heart;

        //회색글자 처리 뜸 원인불명
        public HeartItem(int Icon, String Name) {

            Img_Heart = Icon;
            txt_Heart = Name;
        }
    }

    class HeartItemAdapter extends BaseAdapter{
        Context con;
        LayoutInflater inflater;
        ArrayList<HeartItem> arrData;
        int nLayout;

        private  MyData mMyData = MyData.getInstance();
        public HeartItemAdapter(Context context, int layout, ArrayList<HeartItem> arrD)
        {
            con = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arrData = arrD;
            nLayout = layout;
        }

        @Override
        public int getCount(){
            return arrData.size();
        }

        @Override
        public Object getItem(int i) {
            return arrData.get(i).txt_Heart;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if(view == null)
                view = inflater.inflate(nLayout, viewGroup, false);

            ImageView img = (ImageView)view.findViewById(R.id.Charge_Img);
            img.setImageResource(arrData.get(i).Img_Heart);

            final TextView txt = (TextView) view.findViewById(R.id.Charge_Txt);
            txt.setText(arrData.get(i).txt_Heart);

            txt.setOnClickListener(new TextView.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    Toast.makeText(con, txt.getText(), Toast.LENGTH_SHORT).show();

                    switch (i)
                    {
                        case 0:
                            mMyData.setUserHoney(mMyData.getUserHoney() + 50);
                            refreshHearCnt();
                            break;
                        case 1:
                            mMyData.setUserHoney(mMyData.getUserHoney() + 70);
                            refreshHearCnt();
                            break;
                        case 2:
                            mMyData.setUserHoney(mMyData.getUserHoney() + 100);
                            refreshHearCnt();
                            break;
                    }
                }
            });

            return view;
        }


    }


}
