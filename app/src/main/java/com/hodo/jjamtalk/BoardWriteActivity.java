package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
    ImageView img_Profile1, img_Profile2, img_Profile3;
    Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);
        mActivity = this;

        txt_Memo = (EditText)findViewById(R.id.Write_txtMemo);

        img_Profile1 = (ImageView)findViewById(R.id.Write_Img1);
        img_Profile2 = (ImageView)findViewById(R.id.Write_Img2);
        img_Profile3 = (ImageView)findViewById(R.id.Write_Img3);

        btn_send = (Button)findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(mActivity);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BoardMsgDBData sendData = new BoardMsgDBData();

                        sendData.NickName = mMydata.getUserNick();
                        sendData.Idx = mMydata.getUserIdx();
                        sendData.Img = mMydata.getUserImg();
                        sendData.Msg = txt_Memo.getText().toString();

                        mFireBaseData.SaveBoardData_1((BoardWriteActivity)mActivity);
                        txt_Memo.setText("");
                        //startActivity(new Intent(getApplicationContext(),BoardActivity.class));
                        //finish();
                    }
                }).
                        setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).setMessage("작성한 글을 게시하시겠습니까?");
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

        mFireBaseData.SaveBoardData_2(sendData);
    }
}
