package com.hodo.jjamtalk.Data;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hodo.jjamtalk.R;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class UIData {

    private static UIData _Instance;



    public static UIData getInstance()
    {
        if(_Instance == null)
            _Instance = new UIData();

        return  _Instance;
    }

    private int width;
    private int height;
    private int jewels[] = {R.drawable.silver,R.drawable.gold1,R.drawable.pearl,R.drawable.opal,R.drawable.emerald,R.drawable.sapphire,R.drawable.ruby,R.drawable.diamond};
    private int sellJewelValue[] = {5,5,5,5,7,7,10,20};

    private LinearLayout.LayoutParams llp_ListItem;

    private RelativeLayout.LayoutParams rlp;

    public RelativeLayout.LayoutParams getRLP(float w,float h){
        return new RelativeLayout.LayoutParams((int)(getWidth()*w),(int)(getHeight()*h));

    }
    public FrameLayout.LayoutParams getFLP(int w,float h){
        return new FrameLayout.LayoutParams((int)(getWidth()*w),(int)(getHeight()*h));
    }

    public LinearLayout.LayoutParams getLLP_ListItem(){
        return llp_ListItem;
    }


    public int getHeight() {
        return height;
    }

    public int[] getJewels(){
        return jewels;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLlp_ListItem(LinearLayout.LayoutParams lp){
        this.llp_ListItem = lp;
    }

    public int [] getSellJewelValue() {
        return sellJewelValue;
    }
}
