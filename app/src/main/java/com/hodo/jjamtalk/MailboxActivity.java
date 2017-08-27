package com.hodo.jjamtalk;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

        menu.add(10,1,1,"전체 받기");
        menu.add(10,2,2,"전체 삭제");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case 1:

                break;
            case 2:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("선물 전체 삭제하기");
                alertDialogBuilder.setMessage("삭제하면 되돌릴 수 없습니다")
                        .setCancelable(true)
                        .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("삭제하기",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(),"삭제되었습니다",Toast.LENGTH_LONG).show();

                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}