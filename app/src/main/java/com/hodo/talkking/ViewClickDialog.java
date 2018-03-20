package com.hodo.talkking;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.hodo.talkking.Data.MyData;

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
        //tv_camera = (TextView)findViewById(R.id.tv_camera);
        tv_album = (TextView)findViewById(R.id.tv_album);
        tv_delete = (TextView)findViewById(R.id.tv_del);



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
                mContext.startActivity(new_img Intent(mContext,ImageViewPager.class));
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
