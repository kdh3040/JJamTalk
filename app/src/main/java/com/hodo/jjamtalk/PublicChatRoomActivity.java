package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hodo.jjamtalk.Data.UIData;

/**
 * Created by mjk on 2017. 9. 11..
 */

public class PublicChatRoomActivity extends AppCompatActivity{
    ListView listView;
    PublicChatRoomAdapter pcrAdapter;
    Context mContext;
    Activity mActivity = this;
    ImageView iv_gift;
    ImageView iv_host;
    UIData mUIData = UIData.getInstance();
    LinearLayout ll_chat;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chat);
        mContext = getApplicationContext();

        //View header = getLayoutInflater().inflate(R.layout.header_board_item,null,false);
        //View footer = getLayoutInflater().inflate(R.layout.footer_pcr,null,false);
        //RelativeLayout.LayoutParams lp_iv_host = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/3);
        //RelativeLayout.LayoutParams lp_ll_chat = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/10);
        iv_host = (ImageView)findViewById(R.id.iv_host);
        //iv_host.setLayoutParams(lp_iv_host);

        //ll_chat = (LinearLayout)findViewById(R.id.ll_chat);
        //ll_chat.setLayoutParams(lp_ll_chat);

        listView = (ListView)findViewById(R.id.lv_pcr);
        pcrAdapter = new PublicChatRoomAdapter(mContext,this);
        //listView.addHeaderView(header);
        //listView.addFooterView(footer);
        listView.setAdapter(pcrAdapter);




        //pcrAdapter = new PublicChatRoomAdapter(mContext,this);
        //listView.setAdapter(pcrAdapter);
        iv_gift = (ImageView)findViewById(R.id.iv_gift);
        iv_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View giftView = LayoutInflater.from(mContext).inflate(R.layout.alert_send_gift,null);
                builder.setView(giftView);
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_public_chat,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.banlist:
                startActivity(new Intent(getApplicationContext(),PublicChatOptionMenuBanActivity.class));
                break;
            case R.id.current_user:
                startActivity(new Intent(getApplicationContext(),PublicChatOptionMenuUserListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
