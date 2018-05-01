package com.dohosoft.talkking;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Util.CommonFunc;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class MailboxActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MailBoxAdapter mailBoxAdapter;
    private MyData mMyData = MyData.getInstance();
    private TextView txt_empty;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);
        mailBoxAdapter = new MailBoxAdapter(this);

        txt_empty = (TextView)findViewById(R.id.txt_empty);

        if(mMyData.arrGiftHoneyDataList.size() == 0)
        {
            txt_empty.setVisibility(View.VISIBLE);
        }
        else {
            txt_empty.setVisibility(View.GONE);
        }

        recyclerView = (RecyclerView)findViewById(R.id.rv_mailbox);
        recyclerView.setAdapter(mailBoxAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMyData.SetCurFrag(0);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //overridePendingTransition(R.anim.not_move_activity,R.anim.not_move_activity);
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        mMyData.SetCurFrag(0);

        android.app.AlertDialog.Builder mDialog = null;
        mDialog = new android.app.AlertDialog.Builder(this);

        if (!MyData.getInstance().verSion.equals(MyData.getInstance().marketVersion)) {
            mDialog.setMessage("업데이트 후 사용해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("업데이트 바로가기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent marketLaunch = new Intent(
                                            Intent.ACTION_VIEW);
                                    marketLaunch.setData(Uri
                                            //.parse("https://play.google.com/store/apps/details?id=패키지명 적으세요"));
                                            .parse("https://play.google.com/store/apps/details?id=com.dohosoft.talkking"));

                                    startActivity(marketLaunch);
                                    System.exit(0);
                                }
                            });
            android.app.AlertDialog alert = mDialog.create();
            alert.setTitle("안 내");
            alert.show();
        }

        else
        {
            if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
                mMyData.badgecount = pref.getInt("Badge", 1 );

                if (mMyData.badgecount >= 1) {
                    mMyData.badgecount = 0;
                    Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                    intent.putExtra("badge_count_package_name", "com.dohosoft.talkking");
                    intent.putExtra("badge_count_class_name", "com.dohosoft.talkking.LoginActivity");
                    intent.putExtra("badge_count", mMyData.badgecount);
                    sendBroadcast(intent);
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(10,1,1,"전체 삭제");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case android.R.id.home:
                onBackPressed();
                break;
            case 1:
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference Ref = database.getReference("GiftHoneyList");
                Ref.child(mMyData.getUserIdx()).removeValue();
                mMyData.arrGiftHoneyNameList.clear();
                mMyData.arrGiftHoneyDataList.clear();
                mMyData.arrGiftUserDataList.clear();
                mailBoxAdapter.notifyDataSetChanged();

//                Toast.makeText(getApplicationContext(),"삭제되었습니다",Toast.LENGTH_LONG).show();
                break;
            case 2:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("전체 삭제하기");
                alertDialogBuilder.setCancelable(true)
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
                                        //Toast.makeText(getApplicationContext(),"삭제되었습니다",Toast.LENGTH_LONG).show();

                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
