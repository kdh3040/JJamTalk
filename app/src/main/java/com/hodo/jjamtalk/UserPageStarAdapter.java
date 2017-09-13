package com.hodo.jjamtalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.Data.FanData;

import java.util.ArrayList;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class UserPageStarAdapter extends BaseAdapter {

    ArrayList<FanData> arrayList = new ArrayList<>();

    Context mContext;
    public UserPageStarAdapter(Context context, ArrayList<FanData> fanList) {
        super();
        mContext = context;
        arrayList = fanList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //구현
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.content_user_page_fan_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView FanGrade = (ImageView) convertView.findViewById(R.id.Userpage_Fanlist_Grade) ;
        // ImageView FanRank = (ImageView) convertView.findViewById(R.id.Userpage_Fanlist_Rank) ;
        TextView FanName = (TextView) convertView.findViewById(R.id.Userpage_Fanlist_Name) ;

/*        Glide.with(mContext)
                .load(arrayList.get(position).)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iconImageView);*/

        FanName.setText(arrayList.get(position).Nick);

        return convertView;
    }
}
