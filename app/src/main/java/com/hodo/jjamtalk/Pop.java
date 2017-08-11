package com.hodo.jjamtalk;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class Pop extends PopView{
    private final Context context;
    private final LayoutInflater inflater;
    private final View root;
    private ViewGroup mTrack;

    public Pop(View anchor) {
        super(anchor);
        context  = anchor.getContext();
        inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root  = (ViewGroup) inflater.inflate(R.layout.alert_test, null);
        setContentView(root);
        mTrack    = (ViewGroup) root.findViewById(R.id.viewRow); //팝업 View의 내용을 추가한 LinearLayout
    }
    public void show () {
        preShow(); //상속받은 PopView의 메서드
        int[] location   = new int[2];
        anchor.getLocationOnScreen(location);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.showAtLocation(this.anchor, Gravity.CENTER, 0, 0); //가운데 정렬 하여 보임
    }
}



