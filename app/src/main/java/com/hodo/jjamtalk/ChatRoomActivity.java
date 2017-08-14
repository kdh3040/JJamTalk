package com.hodo.jjamtalk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatRoomActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1001;
    Button btn_send,btn_plus;
    Context context=this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        btn_plus = (Button)findViewById(R.id.btn_plus);

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                //View framelayout = findViewById(R.id.framelayout_chatroom);

                View popup= inflater.inflate(R.layout.popup_chatroom,null);

                builder.setView(popup);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Button btn_gal = popup.findViewById(R.id.btn_gallery);
                btn_gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                        intent.setType("image/*");

                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_IMAGE);

                    }
                });


            }
        });
    }
}
