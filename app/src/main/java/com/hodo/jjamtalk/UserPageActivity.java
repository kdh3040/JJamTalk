package com.hodo.jjamtalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hodo.jjamtalk.Data.UserData;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class UserPageActivity extends AppCompatActivity {
    private UserData stTargetData;

    private TextView txtProfile;
    private TextView txtMemo;
    //private TextView txtProfile;

    private Button btnRegister;
    private Button btnGift;
    private Button btnLike;
    private Button btnMessage;

    private ImageView imgProfile;
    final Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        Intent intent = getIntent();
        stTargetData = (UserData)intent.getExtras().getSerializable("Target");

        txtProfile = (TextView)findViewById(R.id.UserPage_txtProfile);
        txtProfile.setText(stTargetData.NickName + ",  " + stTargetData.Age);
        txtMemo = (TextView)findViewById(R.id.UserPage_txtMemo);
        //private TextView txtProfile;

        imgProfile = (ImageView)findViewById(R.id.UserPage_ImgProfile);

        btnRegister = (Button) findViewById(R.id.UserPage_btnRegister);
        btnGift = (Button) findViewById(R.id.UserPage_btnGift);
        btnLike = (Button) findViewById(R.id.UserPage_btnLike);
        btnMessage = (Button) findViewById(R.id.UserPage_btnMessage);


        View.OnClickListener listener = new View.OnClickListener()
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.UserPage_btnRegister:
                        buildalertDialog("내카드에 등록", "내 카드에 등록하시겠습니까?","등록한다!");

                        //ClickBtnSendHeart();
                        break;
                    case R.id.UserPage_btnGift:

                        View giftView = inflater.inflate(R.layout.alert_send_heart,null);

                        builder.setView(giftView);

                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        Button btn_gift_send= giftView.findViewById(R.id.btn_gift_send);
                        btn_gift_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        Button btn_gift_cancel= giftView.findViewById(R.id.btn_gift_cancel);
                        btn_gift_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();

                            }
                        });
                        //ClickBtnSendHeart();
                        break;
                    case R.id.UserPage_btnLike:

                        Toast.makeText(context,"좋아요를 눌렀습니다",Toast.LENGTH_SHORT).show();


                        //ClickBtnSendHeart();
                        break;
                    case R.id.UserPage_btnMessage:

                        View view1= inflater.inflate(R.layout.alert_send_msg,null);
                        Button btn_cancel = view1.findViewById(R.id.btn_cancel);

                        builder.setView(view1);

                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();

                            }
                        });


                        Button btn_send = view1.findViewById(R.id.btn_send);
                        btn_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                        //ClickBtnSendHeart();
                        break;
                }
            }
        };

        btnRegister.setOnClickListener(listener);
        btnGift.setOnClickListener(listener);
        btnLike.setOnClickListener(listener);
        btnMessage.setOnClickListener(listener);

    }

    private void buildalertDialog(String s, String s1, String s2) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(s);
        alertDialogBuilder.setMessage(s1)
                .setCancelable(true)
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton(s2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"succeed",Toast.LENGTH_SHORT).show();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
