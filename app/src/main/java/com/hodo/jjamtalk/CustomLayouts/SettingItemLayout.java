package com.hodo.jjamtalk.CustomLayouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017-12-02.
 */

public class SettingItemLayout extends LinearLayout{

    LinearLayout bg;
    ImageView symbol;
    TextView text;

    public SettingItemLayout(Context context) {
        super(context);
        initView();

    }



    public SettingItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }


    public SettingItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs,defStyleAttr);
    }

    public void initView(){

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.setting_item,this,false);
        addView(v);

        bg = findViewById(R.id.setting_item_bg);
        symbol = findViewById(R.id.setting_item_iv);
        text = findViewById(R.id.setting_item_tv);
    }

    private void getAttrs(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.LoginButton);

        setTypeArray(typedArray);

    }

    private void getAttrs(AttributeSet attrs,int defStyle){

    }


    public void setTypeArray(TypedArray typedArray) {

        int bg_resId = typedArray.getResourceId(R.styleable.LoginButton_bg, R.color.tp_half);
        bg.setBackgroundResource(bg_resId);

        int symbol_resId = typedArray.getResourceId(R.styleable.LoginButton_bg, R.drawable.icon_fan);
        bg.setBackgroundResource(symbol_resId);

        int textColor = typedArray.getColor(R.styleable.LoginButton_textColor,0);
        text.setTextColor(textColor);

        String text_string = typedArray.getString(R.styleable.LoginButton_text);
        text.setText(text_string);

        typedArray.recycle();

    }
    void setBg(int bg_resID) {

        bg.setBackgroundResource(bg_resID);
    }

    void setSymbol(int symbol_resID) {

        symbol.setImageResource(symbol_resID);
    }

    void setTextColor(int color) {

        text.setTextColor(color);
    }

    void setText(String text_string) {

        text.setText(text_string);

    }


    void setText(int text_resID) {

        text.setText(text_resID);

    }


}


