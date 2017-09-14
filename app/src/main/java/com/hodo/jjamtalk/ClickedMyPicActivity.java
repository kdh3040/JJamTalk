package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UIData;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class ClickedMyPicActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private TextView txt_Memo;
    private ImageView Img_Profile;
    LinearLayout sticker_holder;
    UIData mUIdata = UIData.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_mypic);

        txt_Memo = (TextView)findViewById(R.id.tv_memo);
        Img_Profile = (ImageView)findViewById(R.id.MyPic_Img);
        Img_Profile.setLayoutParams(mUIdata.getRLP(1,0.6f));
        sticker_holder = (LinearLayout)findViewById(R.id.stickers_holder);
        sticker_holder.setLayoutParams(mUIdata.getFLP(1,0.1f));

        Glide.with(getApplicationContext())
                .load(mMyData.getUserImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Img_Profile);

        txt_Memo.setText(mMyData.getUserNick() + ", "+ mMyData.getUserAge() + "\n" + mMyData.getUserMemo());


    }
}
