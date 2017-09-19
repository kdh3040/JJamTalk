package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by mjk on 2017. 9. 18..
 */

public class BuyJewelActivity extends AppCompatActivity {
    Button btn_openbox;
    Activity mActivity;

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_jewel);
        mActivity = this;
        mContext = getApplicationContext();
        btn_openbox = (Button)findViewById(R.id.btn_openbox);


        //잔액부족일때

        btn_openbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AlertDialog.Builder  builder = new AlertDialog.Builder(mActivity);

                    View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_jewelbox_opened,null);
                    Button btn_confirm = v.findViewById(R.id.btn_confirm);
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //dialog.cancel();
                        }
                    });
                    builder.setView(v);

                    AlertDialog dialog = builder.create();
                    dialog.show();


                    Toast.makeText(getApplicationContext(),"꿀이 부족합니다",Toast.LENGTH_LONG).show();


            }
        });



    }
}
