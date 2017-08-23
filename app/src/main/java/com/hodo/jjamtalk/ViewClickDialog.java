package com.hodo.jjamtalk;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mjk on 2017. 8. 23..
 */

public class ViewClickDialog extends Dialog implements View.OnClickListener {

    private static final int Layout = R.layout.dialog_view_click;

    private Context mContext;

    TextView tv_see,tv_camera,tv_album,tv_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Layout);

        tv_see = (TextView)findViewById(R.id.tv_see);
        tv_camera = (TextView)findViewById(R.id.tv_camera);
        tv_album = (TextView)findViewById(R.id.tv_album);
        tv_delete = (TextView)findViewById(R.id.tv_delete);

        tv_see.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
        tv_album.setOnClickListener(this);
        tv_delete.setOnClickListener(this);

    }

    public ViewClickDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_see:

                break;
            case R.id.tv_camera:



                break;
            case R.id.tv_album:

                break;
            case R.id.tv_delete:

                break;

        }

    }
}
