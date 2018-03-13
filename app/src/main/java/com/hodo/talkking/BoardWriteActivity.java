package com.hodo.talkking;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

                CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                    public void YesListener() {
                        if(CommonFunc.getInstance().IsStringEmptyCheck(txt_Memo.getText().toString()))
                        {
                            CommonFunc.getInstance().ShowToast(BoardWriteActivity.this ,"게시글의 내용이 없습니다.",false);
                            return;
                        }

                        if(CommonFunc.getInstance().CheckTextMaxLength(txt_Memo.getText().toString(), CoomonValueData.TEXT_MAX_LENGTH_BOARD, BoardWriteActivity.this ,"게시판 글쓰기", true) == false)
                            return;


                        BoardMsgDBData sendData = new BoardMsgDBData();
                        sendData.Idx = mMydata.getUserIdx();
                        sendData.Msg = txt_Memo.getText().toString();
                        mFireBaseData.SaveBoardData_GetBoardIndex((BoardWriteActivity)mActivity);

                        CommonFunc.getInstance().LastBoardWrite = null;
                    }
                };
                CommonFunc.getInstance().ShowDefaultPopup(BoardWriteActivity.this, listener, null, "게시판 글쓰기","작성한 글을 게시하시겠습니까?\n 10분에 한번 씩 작성할 수 있습니다.", "네", "취소" );

            }
        });

    }

    public void SendBoard()
    {
        BoardMsgDBData sendData = new BoardMsgDBData();

        sendData.Idx = mMydata.getUserIdx();
        sendData.Msg = txt_Memo.getText().toString();

        mFireBaseData.SaveBoardData(sendData);
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

            if (mMydata.badgecount >= 1) {
                mMydata.badgecount = 0;
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count_package_name", "com.hodo.talkking");
                intent.putExtra("badge_count_class_name", "com.hodo.talkking.LoginActivity");
                intent.putExtra("badge_count", mMydata.badgecount);
                sendBroadcast(intent);
            }
        }
    }
}
