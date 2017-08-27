package com.hodo.jjamtalk;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class FanActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FanListAdapter fanListAdapter;
    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);
        mContext = this;
        recyclerView = (RecyclerView)findViewById(R.id.rv_fanlist);
        fanListAdapter = new FanListAdapter(this);

        recyclerView.setAdapter(fanListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
