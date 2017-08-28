package com.hodo.jjamtalk;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.BlockData;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.kakao.usermgmt.response.model.User;

import java.util.LinkedHashMap;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class FanActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FanListAdapter fanListAdapter;
    Context mContext;

    private MyData mMyData = MyData.getInstance();

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
