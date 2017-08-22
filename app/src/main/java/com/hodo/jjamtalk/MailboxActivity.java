package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class MailboxActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MailBoxAdapter mailBoxAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);
        mailBoxAdapter = new MailBoxAdapter(this);
        recyclerView = (RecyclerView)findViewById(R.id.rv_mailbox);
        recyclerView.setAdapter(mailBoxAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("모두받기");
        menu.add("삭제");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
