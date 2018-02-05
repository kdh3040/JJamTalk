package com.hodo.talkking;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by boram on 2018-01-11.
 */

public class Custom_ViewPager extends android.support.v4.view.ViewPager {

    String TAG = "ImageView_Viewer_Custom_ViewPager";



    public Custom_ViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try{
            return super.onInterceptTouchEvent(event);

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;



    }

}
