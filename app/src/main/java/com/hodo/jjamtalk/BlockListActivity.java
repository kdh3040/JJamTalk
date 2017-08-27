package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class BlockListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BlockListAdapter blockListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_block);

        blockListAdapter = new BlockListAdapter(getApplicationContext());
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_blocklist);

        recyclerView.setAdapter(blockListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),1,false));

    }
}