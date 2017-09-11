package com.hodo.jjamtalk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by mjk on 2017. 9. 11..
 */

public class PublicChatRoom extends AppCompatActivity{
    ListView listView;
    PublicChatRoomAdapter pcrAdapter;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chat);
        mContext = getApplicationContext();
        listView = (ListView)findViewById(R.id.lv_pcr);
        pcrAdapter = new PublicChatRoomAdapter(mContext,this);
        listView.setAdapter(pcrAdapter);
    }
}
