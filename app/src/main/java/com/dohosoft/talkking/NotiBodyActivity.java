package com.dohosoft.talkking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dohosoft.talkking.Data.MyData;

public class NotiBodyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_body);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int nTargetIdx = intent.getIntExtra("Target", 0);

        TextView Body = (TextView)findViewById(R.id.Noti_Body);
        Body.setText(MyData.getInstance().arrNotiDataList.get(nTargetIdx).Body);

    }
}
