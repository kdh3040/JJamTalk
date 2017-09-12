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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hodo.jjamtalk.Data.UIData;

/**
 * Created by mjk on 2017. 9. 11..
 */

public class PublicChatRoomHostActivity extends AppCompatActivity{
    ListView listView;
    PublicChatRoomAdapter pcrAdapter;
    Context mContext;
    Activity mActivity = this;
    Button btn_options;
    ImageView iv_host;
    UIData mUIData = UIData.getInstance();
    LinearLayout ll_chat;
    RelativeLayout rl_public_chat;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chat_host);
        mContext = getApplicationContext();

        //View header = getLayoutInflater().inflate(R.layout.header_board_item,null,false);
        //View footer = getLayoutInflater().inflate(R.layout.footer_pcr,null,false);
        rl_public_chat = (RelativeLayout)findViewById(R.id.rl_public_chat);
        LinearLayout.LayoutParams lp_rl_public_chat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/5);
        LinearLayout.LayoutParams lp_ll_chat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/15);
        iv_host = (ImageView)findViewById(R.id.iv_host);
        iv_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지 확대
                startActivity(new Intent(mContext,ImageActivity.class));
            }
        });

        iv_host.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.popup_pcr_host_iv_click,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

                builder.setView(v);
                final AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
        rl_public_chat.setLayoutParams(lp_rl_public_chat);

        ll_chat = (LinearLayout)findViewById(R.id.ll_chat);
        ll_chat.setLayoutParams(lp_ll_chat);

        listView = (ListView)findViewById(R.id.lv_pcr);
        pcrAdapter = new PublicChatRoomAdapter(mContext,this);
        //listView.addHeaderView(header);
        //listView.addFooterView(footer);
        listView.setAdapter(pcrAdapter);




        //pcrAdapter = new PublicChatRoomAdapter(mContext,this);
        //listView.setAdapter(pcrAdapter);
        //btn_options = (Button)findViewById(R.id.btn_options);
        /*btn_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                View giftView = LayoutInflater.from(mContext).inflate(R.layout.alert_send_gift,null);
                builder.setView(giftView);
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });*/
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
