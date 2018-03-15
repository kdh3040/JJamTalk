package com.hodo.talkking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Util.CommonFunc;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class BlockListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BlockListAdapter blockListAdapter;
    private MyData mMyData = MyData.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_block);

        blockListAdapter = new BlockListAdapter(getApplicationContext());
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_blocklist);

        recyclerView.setAdapter(blockListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),1,false));

        mMyData.SetCurFrag(0);

    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        MyData.getInstance().SetCurFrag(0);

        if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
            pref.getInt("Badge", mMyData.badgecount );

            if (mMyData.badgecount >= 1) {
                mMyData.badgecount = 0;
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count_package_name", "com.hodo.talkking");
                intent.putExtra("badge_count_class_name", "com.hodo.talkking.LoginActivity");
                intent.putExtra("badge_count", mMyData.badgecount);
                sendBroadcast(intent);
            }
        }
    }
}
