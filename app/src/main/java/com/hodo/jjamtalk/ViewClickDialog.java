package com.hodo.jjamtalk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hodo.jjamtalk.Data.MyData;

/**
 * Created by mjk on 2017. 8. 23..
 */

public class ViewClickDialog extends Dialog {

    private static final int Layout = R.layout.dialog_view_click;
    private MyData mMyData = MyData.getInstance();

    private Context mContext;

    TextView tv_see,tv_camera,tv_album,tv_delete;
    int ImgIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Layout);

        tv_see = (TextView)findViewById(R.id.tv_see);
        tv_camera = (TextView)findViewById(R.id.tv_camera);
        tv_album = (TextView)findViewById(R.id.tv_album);
        tv_delete = (TextView)findViewById(R.id.tv_delete);



    }

    public ViewClickDialog(@NonNull Context context, int index) {
        super(context);
        mContext = context;
        ImgIndex = index;
    }
/*
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_see:
                mContext.startActivity(new Intent(mContext,ImageViewPager.class));
                dismiss();
                break;
            case R.id.tv_camera:



                break;
            case R.id.tv_album:

                break;
            case R.id.tv_delete:
            Log.d("####", "!!###  : " + ImgIndex);
                break;

        }

    }*/

}
