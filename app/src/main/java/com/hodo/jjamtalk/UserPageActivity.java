package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.UserPage_btnRegister:
                        //ClickBtnSendHeart();
                        break;
                    case R.id.UserPage_btnGift:
                        //ClickBtnSendHeart();
                        break;
                    case R.id.UserPage_btnLike:
                        //ClickBtnSendHeart();
                        break;
                    case R.id.UserPage_btnMessage:
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
}
