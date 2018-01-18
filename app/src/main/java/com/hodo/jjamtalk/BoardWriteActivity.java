package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.BoardMsgDBData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Firebase.FirebaseData;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardWriteActivity extends AppCompatActivity {

    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private MyData mMydata = MyData.getInstance();

    Button btn_send;
    EditText txt_Memo;
    Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);
        mActivity = this;

        txt_Memo = (EditText)findViewById(R.id.Write_txtMemo);

        btn_send = (Button)findViewById(R.id.btn_send);
        if(mMydata.getUserHoney() > 10){
            btn_send.setText("10골드를 소비하여 글 등록");
        }
        else
        {
            btn_send.setText(10 - mMydata.getUserHoney()+ "골드가 부족합니다");
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder= new AlertDialog.Builder(mActivity);

                if(mMydata.getUserHoney() > 10){
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mMydata.setUserHoney(mMydata.getUserHoney() - 10);

                            BoardMsgDBData sendData = new BoardMsgDBData();
                            sendData.NickName = mMydata.getUserNick();
                            sendData.Idx = mMydata.getUserIdx();
                            sendData.Img = mMydata.getUserImg();
                            sendData.Msg = txt_Memo.getText().toString();
                            mFireBaseData.SaveBoardData_GetBoardIndex((BoardWriteActivity)mActivity);

                        }
                    }).
                            setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).setMessage("작성한 글을 게시하시겠습니까?");
                }
                else
                {
                    builder.setPositiveButton("골드 사러가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
                            finish();
                        }
                    }).
                            setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).setMessage(10 - mMydata.getUserHoney() + "골드가 부족합니다");
                }
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }

    public void SendBoard()
    {
        BoardMsgDBData sendData = new BoardMsgDBData();

        sendData.NickName = mMydata.getUserNick();
        sendData.Idx = mMydata.getUserIdx();
        sendData.Img = mMydata.getUserImg();
        sendData.Msg = txt_Memo.getText().toString();

        mFireBaseData.SaveBoardData(sendData);
    }
}
