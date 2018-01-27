package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by mjk on 2018-01-26.
 */

public class StartLogo extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                Intent intent = new Intent(StartLogo.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
