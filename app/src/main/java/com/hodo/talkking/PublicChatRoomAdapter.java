package com.hodo.talkking;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by mjk on 2017. 9. 11..
 */

public class PublicChatRoomAdapter extends BaseAdapter {

    Context mContext;
    Activity mActivity;

    String [] strings = {"나무위키에 오신 것을 환영합니다!","나무위키의 토론 URL 시스템과 로딩 시스템이 바뀌었습니다",
            "공식 기록 강수량만 264.1㎜…기상청 \"최고 150㎜\" 예상\n" +
                    "\n" +
                    "거제·통영도 기상 관측 이래 최고 일일 강수량 기록","11일 부산에 기상 관측 이래 가장 많은 비가 내렸다. 당초 기상청 예보보다 100㎜ 이상 많은 수준으로, 기상청은 다시 오보 논란에 휩싸였다.",
            "부연했다.","재 부산(중구 대청동)의 누적 강수량은 264.1㎜다. 이는 하루 동안 부산 지역에 내린 비 가운데 역대 가장 많은 수준이다.이날 1시간 만에 최대 84.0㎜, 83.9㎜씩 비가 내려 관측 이래 가장 많은 시간당 강수량을 기록하기도 했다. 부산",
            "그냥 전국적으로 1mm~에서 한 2천미리 온다 그래?"," 시각 부산 영도의 누적 강수량은 358.5㎜를 기록했다.이날 1시간 만에 최대 84.0㎜, 83.9㎜씩 비가 내려 관측 이래 가장 많은 시간당 강수량을 기록하기도 했다. 부산",
            "하냐..그돈으로 사서쓰라","이날 1시간 만에 최대 84.0㎜, 83.9㎜씩 비가 내려 관측 이래 가장 많은 시간당 강수량을 기록하기도 했다. 부산",
            "드럽게 안오다가 한방에 퍼붓네","이날 1시간 만에 최대 84.0㎜, 83.9㎜씩 비가 내려 관측 이래 가장 많은 시간당 강수량을 기록하기도 했다. 부산",
            "락, 여기저기 오는곳이있겠고, 비올확률이 50% 등등 요상한말만 만들어서 책임회피할려고했는데 반발이 심하니까 지들이 원해서 사준 슈퍼컴"," 시각 부산 영도의 누적 강수량은 358.5㎜를 기록했다.,",
            "그렇타고 ","의 예보보다 많게는 두 배 가까이 차이 나는 수준이다. 기상청은 부산을 포함한 남부지방에 시간당 30㎜ 이상의 장대비가 쏟",
            " 국민들 ","남부 해안지역에 호우경보가 내려진 11일 오전 부산 중구 동광동에서 집중호우로 1∼2층짜리 주택 3채가 잇따라 무너져 119구조대가 추가 붕괴를 막는 작",
            "바꾸던지","\"저기압이 시계 반대 방향으로 회전하며 남쪽 먼바다에서 따뜻하고 습한 공기들을 경남 쪽으로 끌어올렸다\"며 \"여기에 ",
            "이젠 중계도 못한다","그러면서 \"마치 자동차가 급브레이크를 밟았을 때처럼 기류가 육지로 넘어오면서 확연히 느려졌다\"며 \"이때 수렴한 수증기",
            "할머님들이 허리 쑤시는","이날 1시간 만에 최대 84.0㎜, 83.9㎜씩 비가 내려 관측 이래 가장 많은 시간당 강수량을 기록하기도 했다. 부산"};


    public PublicChatRoomAdapter(Context context,Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return 24;
    }

    @Override
    public Object getItem(int i) {
        return strings[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        if(view != null){
            v= view;
        }else {

            v = LayoutInflater.from(mContext).inflate(R.layout.content_pcr_chat_item, viewGroup, false);
        }
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View dialog_ban = LayoutInflater.from(mContext).inflate(R.layout.dialog_public_chat_ban,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setView(dialog_ban);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        //ImageView iv = v.findViewById(R.id.iv_rank);
        //iv.setImageResource(R.mipmap.edit_icon);
        TextView tv_rank = v.findViewById(R.id.tv_rank);




        tv_rank.setTextColor(Color.parseColor("#000000"));

        tv_rank.setText("1231");
        TextView tv_nickname = v.findViewById(R.id.tv_nickname);



        tv_nickname.setText("nickname :");
        TextView tv_msg = v.findViewById(R.id.tv_msg);
        tv_msg.setText((String) getItem(i));









        return v;
    }
}
