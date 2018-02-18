package com.hodo.talkking.ViewHolder;

import android.content.Context;
import android.graphics.Color;
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
import com.hodo.talkking.Data.BoardMsgClientData;
import com.hodo.talkking.Data.BoardMsgDBData;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.UIData;
import com.hodo.talkking.R;
import com.hodo.talkking.Util.CommonFunc;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardViewHolder extends RecyclerView.ViewHolder {

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

        Glide.with(context)
                .load(dbData.Img)
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(BoardThumnail);

        BoardMsg.setText(dbData.Msg);
        BoardWriter.setText(dbData.NickName);
        if(dbData.Gender.equals("여자"))
            BoardWriter.setTextColor(0xffda1d81);
        else
            BoardWriter.setTextColor(0xff005baf);

        long time = CommonFunc.getInstance().GetCurrentTime();
        Date writeDate = CommonFunc.getInstance().GetStringToDate(dbData.Date, CoomonValueData.DATE_FORMAT);
        Date todayDate = new Date(time);

        if(CommonFunc.getInstance().IsTodayDate(todayDate, writeDate))
        {
            SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_TODAY_DATE_FORMAT);
            BoardDate.setText(ctime.format(new Date(writeDate.getTime())));
        }
        else
        {
            SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_DATE_FORMAT);
            BoardDate.setText(ctime.format(new Date(writeDate.getTime())));
        }

        BoardUserItem.setVisibility(View.VISIBLE);
        if(data.GetDBData().BestItem > 0)
        {
            BoardUserItem.setImageResource(UIData.getInstance().getJewels()[data.GetDBData().BestItem]);
        }
        else
        {
            BoardUserItem.setImageResource(R.mipmap.randombox);
        }


        BoardUserGrade.setImageResource(UIData.getInstance().getGrades()[data.GetDBData().Grade]);
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
    }
}
