package com.dohosoft.talkking.Data;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dohosoft.talkking.R;

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

    private String Items[] = {"랜덤박스", "오팔", "진주", "자수정", "사파이어", "에메랄드", "루비", "다이아몬드"};


    private String ItemProb[] = {"50%", "50%", "25%", "13.5%", "7%", "3.5%", "0.85%", "0.15%"};


    private String ItemBlar[] = {"귀족을 뜻하는 보석", "- 힘 -", "- 우아함 -", "- 귀족 -", "- 신성함 -", "- 불멸 -", "- 정열 -", " - 킹! -"};


    private String ItemsReference[] = {"페레가모 구두", "구찌 드레스", "헤르메스 가방", "롤렉스 시계", "다이아몬드", "페라리", "럭셔리 요트", "전용기"};
    private int jewelGifts[] = { R.drawable.randombox, R.drawable.opal_gift,R.drawable.pearl_gift,R.drawable.amethyst_gift,R.drawable.sapphire_gift,R.drawable.emerald_gift,R.drawable.ruby_gift,R.drawable.diamond_gift};
    private int jewels[] = { R.drawable.randombox, R.drawable.opal,R.drawable.pearl,R.drawable.amethyst,R.drawable.sapphire,R.drawable.emerald,R.drawable.ruby,R.drawable.diamond};
    private int grades[] = {R.drawable.rank_bronze,R.drawable.rank_silver,R.drawable.rank_gold,R.drawable.rank_diamond,R.drawable.rank_vip,R.drawable.rank_vvip};
    private int sellJewelValue[] = {4,4,4,4,4,4,4,4};
    private int genderIcon[] = {R.drawable.rank_bronze, R.drawable.rank_silver};
    private int ad_Reward = 50;

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

    public int[] getJewelGifts(){
        return jewelGifts;
    }

    public int[] getJewels(){
        return jewels;
    }
    public String[] getItemBlar(){
        return ItemBlar;
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

    public int getAdReward(){
        return ad_Reward;
    }
}
