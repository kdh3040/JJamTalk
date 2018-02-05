package com.hodo.talkking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class HoneySentHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    HoneySentAdapter honeySentAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honey_sent);
        recyclerView = (RecyclerView)findViewById(R.id.rv_honey_sent);
        honeySentAdapter = new HoneySentAdapter(getApplicationContext());
        recyclerView.setAdapter(honeySentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
