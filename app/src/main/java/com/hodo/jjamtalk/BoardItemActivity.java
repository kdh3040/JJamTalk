package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.BoardData;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardItemActivity extends AppCompatActivity{

    private BoardData mBoardData = BoardData.getInstance();

    RecyclerView recyclerView_board_reply;
    Button btn_send;
    ImageButton ib_vote_like,ib_warn;
    EditText et_reply;

    TextView tv_Name, tv_Info, tv_Date, tv_Memo;
    ImageView iv_Profile;

    int nTargetIdx;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);

        final Activity mActivity = this;
        btn_send = (Button) findViewById(R.id.btn_send);

        ib_vote_like = (ImageButton) findViewById(R.id.ib_vote_like);
        ib_vote_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"좋아요를 눌렀습니다",Toast.LENGTH_SHORT).show();
            }
        });
        ib_warn = (ImageButton) findViewById(R.id.ib_warn);
        ib_warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder= new AlertDialog.Builder(mActivity);
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

            }
        });
        et_reply = (EditText)findViewById(R.id.et_reply);

        recyclerView_board_reply = (RecyclerView)findViewById(R.id.recyclerview_board_reply);

        tv_Name = (TextView)findViewById(R.id.tv_nickname);
        tv_Info = (TextView)findViewById(R.id.tv_info);
        tv_Date = (TextView)findViewById(R.id.tv_date);
        tv_Memo = (TextView)findViewById(R.id.tv_content);
        iv_Profile = (ImageView)findViewById(R.id.iv_profile);


        Intent intent = getIntent();
        nTargetIdx = intent.getIntExtra("Target", 0);
        tv_Name.setText(mBoardData.arrBoardList.get(nTargetIdx).NickName);
        //tv_Info.setText(mBoardData.arrBoardList.get(nTargetIdx).;
        //tv_Date.setText(mBoardData.arrBoardList.get(nTargetIdx).);
        tv_Memo.setText(mBoardData.arrBoardList.get(nTargetIdx).Msg);

        Glide.with(getApplicationContext())
                .load(mBoardData.arrBoardList.get(nTargetIdx).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_Profile);

    }
}
