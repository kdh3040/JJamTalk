package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class HoneyActivity extends AppCompatActivity {

    Button btn_honey_get,btn_honey_sent;
    FragmentManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honey);

        final HoneyGetFragment honeyGetFragment = new HoneyGetFragment();
        final HoneySentFragment honeySentFragment = new HoneySentFragment();

        manager = getSupportFragmentManager();

        manager.beginTransaction().replace(R.id.fl_honey,honeySentFragment).commit();

        btn_honey_get = (Button)findViewById(R.id.btn_honey_get);
        btn_honey_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction().replace(R.id.fl_honey,honeyGetFragment).commit();
            }
        });

        btn_honey_sent = (Button)findViewById(R.id.btn_honey_sent);
        btn_honey_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction().replace(R.id.fl_honey,honeySentFragment).commit();
            }
        });
    }
}
