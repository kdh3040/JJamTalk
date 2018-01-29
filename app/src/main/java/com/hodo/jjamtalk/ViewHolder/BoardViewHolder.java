package com.hodo.jjamtalk.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.BoardLikeData;
import com.hodo.jjamtalk.Data.BoardMsgClientData;
import com.hodo.jjamtalk.Data.BoardMsgDBData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.R;

import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardViewHolder extends RecyclerView.ViewHolder {

    public TextView BoardMsg, BoardWriter, BoardDate;
    public ImageView BoardThumnail;
    public ImageButton BoardDeleteButton;
    public Button BoardReportButton;

    public BoardViewHolder(View itemView) {
        super(itemView);
        BoardMsg = (TextView) itemView.findViewById(R.id.board_msg);
        BoardWriter = (TextView) itemView.findViewById(R.id.board_writer);
        BoardDate = (TextView) itemView.findViewById(R.id.board_write_date);
        BoardThumnail = (ImageView) itemView.findViewById(R.id.board_thumnail);
        BoardDeleteButton = (ImageButton) itemView.findViewById(R.id.board_delete);
        BoardReportButton = (Button) itemView.findViewById(R.id.board_report);
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
        BoardDate.setText(dbData.Date);

        Log.d("!!!!!!", "!!!!!!!!!!!" + dbData.Msg);

        if(mine)
        {
            BoardReportButton.setVisibility(View.GONE);
            BoardDeleteButton.setVisibility(View.GONE);

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
