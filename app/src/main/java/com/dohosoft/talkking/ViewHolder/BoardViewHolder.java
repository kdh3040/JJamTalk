package com.dohosoft.talkking.ViewHolder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dohosoft.talkking.Data.BoardMsgClientData;
import com.dohosoft.talkking.Data.BoardMsgDBData;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.R;
import com.dohosoft.talkking.Util.CommonFunc;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.dohosoft.talkking.Data.CoomonValueData.TEXTCOLOR_MAN;
import static com.dohosoft.talkking.Data.CoomonValueData.TEXTCOLOR_WOMAN;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardViewHolder extends RecyclerView.ViewHolder {

    private MyData mMyData = MyData.getInstance();
    public TextView BoardMsg, BoardWriter, BoardDate;
    public ImageView BoardThumnail, BoardUserItem, BoardUserGrade;//, BoardUserGender;
    public Button BoardDeleteButton;
    public ImageButton BoardReportButton;
    private ConstraintLayout BoardLayout;

    public BoardViewHolder(View itemView) {
        super(itemView);
        BoardMsg = (TextView) itemView.findViewById(R.id.board_msg);
        BoardWriter = (TextView) itemView.findViewById(R.id.board_writer);
        BoardDate = (TextView) itemView.findViewById(R.id.board_write_date);
        BoardThumnail = (ImageView) itemView.findViewById(R.id.board_thumnail);
        BoardDeleteButton = (Button) itemView.findViewById(R.id.board_delete);
        BoardReportButton = (ImageButton) itemView.findViewById(R.id.board_report);
        BoardUserItem = (ImageView) itemView.findViewById(R.id.user_item);
        BoardUserGrade = (ImageView) itemView.findViewById(R.id.user_grade);
        //BoardUserGender = (ImageView) itemView.findViewById(R.id.user_gender);
    }

    public void SetBoardViewHolder(Context context, BoardMsgClientData data, Boolean mine, Boolean deleteEnable)
    {
        if(data == null)
            return;
        final BoardMsgDBData dbData = data.GetDBData();
        final SimpleUserData simpleUserData = data.GetBoardSimpleUserData();

        String boardMsg = CommonFunc.getInstance().RemoveEnterString(dbData.Msg, 8);

        BoardMsg.setText(boardMsg);


        long time = CommonFunc.getInstance().GetCurrentTime();
        Date todayDate = new Date(time);

        if(CommonFunc.getInstance().IsTodayDate(todayDate, new Date(dbData.Date)))
        {
            SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_TODAY_DATE_FORMAT);
            BoardDate.setText(ctime.format(dbData.Date));
        }
        else
        {
            SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_DATE_FORMAT);
            BoardDate.setText(ctime.format(dbData.Date));
        }



        if(simpleUserData != null)
        {
            if(simpleUserData.Idx.equals(mMyData.getUserIdx()))
            {
                BoardWriter.setText(mMyData.getUserNick()  + " (" + mMyData.getUserAge() + "세 )");// + ", " + mMyData.arrCardNameList.get(i).Age + "세");
                if(mMyData.getUserGender().equals("여자"))
                    BoardWriter.setTextColor(TEXTCOLOR_WOMAN);
                else
                    BoardWriter.setTextColor(TEXTCOLOR_MAN);

                BoardUserItem.setVisibility(View.VISIBLE);

                if(mMyData.getUserBestItem() == 0)
                {
                    BoardUserItem.setVisibility(View.GONE);
                }
                else
                    BoardUserItem.setImageResource(UIData.getInstance().getJewels()[mMyData.getUserBestItem()]);

                BoardUserGrade.setVisibility(View.VISIBLE);
                BoardUserGrade.setImageResource(UIData.getInstance().getGrades()[mMyData.getGrade()]);


                Glide.with(context)
                        .load(mMyData.getUserImg())
                        .bitmapTransform(new CropCircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(BoardThumnail);
            }
            else
            {
                BoardWriter.setText(simpleUserData.NickName+ " (" + simpleUserData.Age + "세)");// + ", " + mMyData.arrCardNameList.get(i).Age + "세");
                if(simpleUserData.Gender.equals("여자"))
                    BoardWriter.setTextColor(TEXTCOLOR_WOMAN);
                else
                    BoardWriter.setTextColor(TEXTCOLOR_MAN);

                BoardUserItem.setVisibility(View.VISIBLE);

                if(simpleUserData.BestItem == 0)
                {
                    BoardUserItem.setVisibility(View.GONE);
                }
                else
                    BoardUserItem.setImageResource(UIData.getInstance().getJewels()[simpleUserData.BestItem]);

                BoardUserGrade.setVisibility(View.VISIBLE);
                BoardUserGrade.setImageResource(UIData.getInstance().getGrades()[simpleUserData.Grade]);


                Glide.with(context)
                        .load(simpleUserData.Img)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(BoardThumnail);
            }
        }
        else
        {
            BoardWriter.setText("없는유저");
            BoardUserItem.setVisibility(View.INVISIBLE);
            BoardUserGrade.setVisibility(View.INVISIBLE);
        }


      /*  if(data.GetDBData().Gender == null)
            BoardUserGender.setVisibility(View.INVISIBLE);
        else
        {
            BoardUserGender.setVisibility(View.VISIBLE);
            BoardUserGender.setImageResource(UIData.getInstance().getGenderIcon(data.GetDBData().Gender));
        }*/


        Log.d("!!!!!!", "!!!!!!!!!!!" + dbData.Msg);

        if(mine)
        {
            BoardReportButton.setVisibility(View.GONE);
            BoardDeleteButton.setVisibility(View.INVISIBLE);

            if(deleteEnable)
            {
                BoardDeleteButton.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            BoardReportButton.setVisibility(View.VISIBLE);
            BoardDeleteButton.setVisibility(View.GONE);
        }
    }

    public void SetBoardHolderListener(View.OnClickListener listener)
    {
        BoardMsg.setOnClickListener(listener);
        BoardThumnail.setOnClickListener(listener);
        BoardWriter.setOnClickListener(listener);
        BoardDate.setOnClickListener(listener);
        BoardDeleteButton.setOnClickListener(listener);
        BoardReportButton.setOnClickListener(listener);

        View.OnLongClickListener longClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true; // 다음 이벤트 계속 진행 false, 이벤트 완료 true
            }
        };

        BoardMsg.setOnLongClickListener(longClick);
        BoardThumnail.setOnLongClickListener(longClick);
        BoardWriter.setOnLongClickListener(longClick);
        BoardDate.setOnLongClickListener(longClick);
        BoardDeleteButton.setOnLongClickListener(longClick);
        BoardReportButton.setOnLongClickListener(longClick);
    }
}
