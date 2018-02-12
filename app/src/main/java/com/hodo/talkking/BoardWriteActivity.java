package com.hodo.talkking;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hodo.talkking.Data.BoardMsgDBData;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.CommonFunc;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardWriteActivity extends AppCompatActivity {

    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private MyData mMydata = MyData.getInstance();

    Button btn_send;
    EditText txt_Memo;
    Activity mActivity;

    private String BeforeWriteMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);
        mActivity = this;

        txt_Memo = (EditText)findViewById(R.id.Write_txtMemo);
        txt_Memo.setText(CommonFunc.getInstance().LastBoardWrite);
        btn_send = (Button)findViewById(R.id.btn_send);
        btn_send.setText("글 등록하기");

        txt_Memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CommonFunc.getInstance().LastBoardWrite = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder= new AlertDialog.Builder(mActivity);

                //if(mMydata.getUserHoney() > 10)
                {
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //mMydata.setUserHoney(mMydata.getUserHoney() - 10);

                            if(CommonFunc.getInstance().CheckTextMaxLength(txt_Memo.getText().toString(), CoomonValueData.TEXT_MAX_LENGTH_BOARD, BoardWriteActivity.this ,"게시판 글쓰기", true) == false)
                                return;


                            BoardMsgDBData sendData = new BoardMsgDBData();
                            sendData.NickName = mMydata.getUserNick();
                            sendData.Idx = mMydata.getUserIdx();
                            sendData.Img = mMydata.getUserImg();
                            sendData.Msg = txt_Memo.getText().toString();
                            mFireBaseData.SaveBoardData_GetBoardIndex((BoardWriteActivity)mActivity);

                            CommonFunc.getInstance().LastBoardWrite = null;

                        }
                    }).
                            setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).setMessage("작성한 글을 게시하시겠습니까?");
                }
             /*   else
                {
                    builder.setPositiveButton("골드 사러가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getApplicationContext(), BuyHeartActivity.class));
                            finish();
                        }
                    }).
                            setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).setMessage(10 - mMydata.getUserHoney() + "골드가 부족합니다");
                }*/
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
        sendData.Gender = mMydata.getUserGender();
        sendData.Grade = mMydata.getGrade();
        sendData.BestItem = mMydata.GetBestItem();

        mFireBaseData.SaveBoardData(sendData);
    }
}
