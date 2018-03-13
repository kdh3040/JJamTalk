package com.hodo.talkking.Data;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hodo.talkking.R;

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
    private String ItemProb[] = {"70%", "22%", "4%", "1.7%", "1.4%", "0.6%", "0.05%"};
    private String Items[] = {"랜덤박스", "진주", "오팔", "자수정", "사파이어", "루비", "에메랄드", "다이아몬드"};
    private String ItemsReference[] = {"페레가모 구두", "구찌 드레스", "헤르메스 가방", "롤렉스 시계", "다이아몬드", "페라리", "럭셔리 요트", "전용기"};
    private int jewels[] = { R.drawable.randombox, R.drawable.club_s,R.drawable.dia_s,R.drawable.heart,R.drawable.spade_s,R.drawable.jackface,R.drawable.queenface,R.drawable.kingface};
    private int grades[] = {R.drawable.rank_bronze,R.drawable.rank_silver,R.drawable.rank_gold,R.drawable.rank_diamond,R.drawable.rank_vip,R.drawable.rank_vvip};
    private int sellJewelValue[] = {4,4,4,4,4,4,4,4};
    private int genderIcon[] = {R.drawable.rank_bronze, R.drawable.rank_silver};
    private int ad_Reward[] = {10,15,20,25,30,35};

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

    public String[] getProb(){
        return ItemProb;
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
