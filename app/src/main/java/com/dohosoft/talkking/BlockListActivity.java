package com.dohosoft.talkking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Util.CommonFunc;

/**
 * Created by mjk on 2017. 8. 22..
 */

public class BlockListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BlockListAdapter blockListAdapter;
    private MyData mMyData = MyData.getInstance();
    public TextView txt_empty;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_block);

        txt_empty = (TextView)findViewById(R.id.txt_empty);

        if(mMyData.arrBlockDataList.size() == 0)
        {
            txt_empty.setVisibility(View.VISIBLE);
        }
        else {
            txt_empty.setVisibility(View.GONE);
        }


        blockListAdapter = new BlockListAdapter(getApplicationContext(), txt_empty);
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
            mMyData.badgecount = pref.getInt("Badge", 1);

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
