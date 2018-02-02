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
    private String Items[] = {"명품 구두", "명품 드레스", "명품 가방", "명품 시계", "보석", "고급 자동차", "고급 요트", "전용기"};
    private String ItemsReference[] = {"페레가모 구두", "구찌 드레스", "헤르메스 가방", "롤렉스 시계", "다이아몬드", "페라리", "럭셔리 요트", "전용기"};
    private int jewels[] = {R.drawable.heel_hng,R.drawable.dress_hng,R.drawable.bag_hng,R.drawable.watch_hng,R.drawable.ring_hng,R.drawable.heart,R.drawable.boat_hng,R.drawable.jet_hng};
    private int grades[] = {R.drawable.rank_bronze,R.drawable.rank_silver,R.drawable.rank_gold,R.drawable.rank_diamond,R.drawable.rank_vip,R.drawable.rank_vvip};
    private int sellJewelValue[] = {5,5,5,5,7,7,10,20};
    private int genderIcon[] = {R.drawable.rank_bronze, R.drawable.rank_silver};
    private int ad_Reward[] = {10,20,30,40,50,60};

    private LinearLayout.LayoutParams llp_ListItem;
    public boolean bSellItemStatus = false;
    public int nSelItemType, nSelItemCount;

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

    public String[] getItemsReference(){
        return ItemsReference;
    }
    public String[] getItems(){
        return Items;
    }

    public int[] getJewels(){
        return jewels;
    }

    public int[] getGrades(){
        return grades;
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

    public int getGenderIcon(String gender)
    {
        if(gender.equals(CoomonValueData.GENDER_MAN))
            return genderIcon[0];
        else
            return genderIcon[1];
    }

    public int[] getAdReward(){
        return ad_Reward;
    }
}
