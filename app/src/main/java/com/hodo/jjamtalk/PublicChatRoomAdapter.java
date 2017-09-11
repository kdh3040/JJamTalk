package com.hodo.jjamtalk;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by mjk on 2017. 9. 11..
 */

public class PublicChatRoomAdapter extends BaseAdapter {

    Context mContext;

    String [] strings = {"fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd",
            "fgdfgdfggdsqwhsd","fslkahgi29hekvfd8123rkjnkvdfv82lkn3lfjvksdavfsfsafsdfsdfwgredgdgfqhhhdfgdgdgdgfgdfgd"};


    public PublicChatRoomAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 24;
    }

    @Override
    public Object getItem(int i) {
        return strings[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        if(view != null){
            v= view;
        }else {

            v = LayoutInflater.from(mContext).inflate(R.layout.content_pcr_chat_item, viewGroup, false);
        }
        //ImageView iv = v.findViewById(R.id.iv_rank);
        //iv.setImageResource(R.mipmap.edit_icon);
        TextView tv = v.findViewById(R.id.tv_rank_nickname_msg);

        SpannableString spannableString = SpannableString.valueOf("1"+"nickname: "+getItem(i));
        spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#ff0000")), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(spannableString);





        return v;
    }
}
