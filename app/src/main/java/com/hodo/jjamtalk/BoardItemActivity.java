package com.hodo.jjamtalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardItemActivity extends AppCompatActivity{


    RecyclerView recyclerView_board_reply;
    Button btn_send;
    ImageButton ib_vote_like,ib_warn;
    EditText et_reply;
    LinearLayout imageViewLayout;
    Toolbar toolbar;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);

        //toolbar.setTitle("허허참");
        //toolbar.setTitleTextColor(Color.parseColor("#ff0099cc"));

        //setSupportActionBar(toolbar);





        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.board_icon);
        //setSupportActionBar(toolbar);


        btn_send = (Button) findViewById(R.id.btn_send);

        ib_vote_like = (ImageButton) findViewById(R.id.ib_vote_like);
        ib_vote_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"좋아요를 눌렀습니다",Toast.LENGTH_SHORT).show();
            }
        });

        et_reply = (EditText)findViewById(R.id.et_reply);

        recyclerView_board_reply = (RecyclerView)findViewById(R.id.recyclerview_board_reply);

        imageViewLayout = (LinearLayout)findViewById(R.id.layout_imageview);
        imageViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ImageViewPager.class));
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board_item,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_report:
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).
                        setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).setMessage("이 사람을 신고하시겠습니까?")
                        .setTitle("신고하기");
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

        }
        return true;
    }

}
