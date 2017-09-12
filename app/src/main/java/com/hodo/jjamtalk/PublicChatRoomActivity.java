package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hodo.jjamtalk.Data.UIData;


/**
 * Created by mjk on 2017. 9. 12..
 */

public class PublicChatRoomActivity extends AppCompatActivity {

    RelativeLayout rl_public_chat;
    UIData mUIData = UIData.getInstance();
    ImageView iv_host;
    LinearLayout ll_chat;
    ListView listView;
    ImageButton ib_gift;
    PublicChatRoomAdapter pcrAdapter;
    AlertDialog.Builder builder;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chat);

        rl_public_chat = (RelativeLayout)findViewById(R.id.rl_public_chat);
        LinearLayout.LayoutParams lp_rl_public_chat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/5);
        LinearLayout.LayoutParams lp_ll_chat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/15);
        iv_host = (ImageView)findViewById(R.id.iv_host);
        iv_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지 확대
                startActivity(new Intent(getApplicationContext(),ImageActivity.class));
            }
        });

        builder = new AlertDialog.Builder(this);

        ib_gift = (ImageButton)findViewById(R.id.ib_gift);

        ib_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_send_gift,null);


                builder.setView(v);
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        rl_public_chat.setLayoutParams(lp_rl_public_chat);

        ll_chat = (LinearLayout)findViewById(R.id.ll_chat);
        ll_chat.setLayoutParams(lp_ll_chat);

        listView = (ListView)findViewById(R.id.lv_pcr);
        pcrAdapter = new PublicChatRoomAdapter(getApplicationContext(),this);
        //listView.addHeaderView(header);
        //listView.addFooterView(footer);
        listView.setAdapter(pcrAdapter);

    }
}
