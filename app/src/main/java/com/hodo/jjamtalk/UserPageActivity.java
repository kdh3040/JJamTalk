package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class UserPageActivity extends AppCompatActivity {

    Button btn_send_msg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        btn_send_msg = (Button)findViewById(R.id.btn_send_msg);
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pop pop = new Pop(view);
                pop.show();
            }
        });
    }
}
