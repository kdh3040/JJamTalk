package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by mjk on 2017. 8. 4..
 */

public class MyProfileFragment extends AppCompatActivity {

    ImageButton btn_Setting,btn_my_profile;
    Button btn_heart;
    ImageView img_Mypic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile_from_main);

        btn_Setting = (ImageButton)findViewById(R.id.btn_setting);
        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SettingActivity.class));

            }
        });
        btn_heart = (Button)findViewById(R.id.btn_heart);
        btn_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HeartActivity.class));
            }
        });

        img_Mypic = (ImageView)findViewById(R.id.img_mypic);
        img_Mypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ClickedMyPicActivity.class));
            }
        });
        btn_my_profile = (ImageButton)findViewById(R.id.ib_my_profile);
        btn_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
            }
        });
    }
}
