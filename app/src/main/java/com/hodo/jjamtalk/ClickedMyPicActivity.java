package com.hodo.jjamtalk;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.Data.MyData;

import org.w3c.dom.Text;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class ClickedMyPicActivity extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private TextView txt_Memo;
    private ImageView Img_Profile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_mypic);

        txt_Memo = (TextView)findViewById(R.id.MyPic_Memo);
        Img_Profile = (ImageView)findViewById(R.id.MyPic_Img);

        txt_Memo.setText(mMyData.getUserNick() + ", "+ mMyData.getUserAge() + "\n" + mMyData.getUserMemo());
    }
}
