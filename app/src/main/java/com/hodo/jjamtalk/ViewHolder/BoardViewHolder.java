package com.hodo.jjamtalk.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public TextView BoardMsg, BoardWriter, BoardDate, BoardLikeCount;
    public ImageView BoardThumnail;
    public ImageButton BoardLikeButton;
    public Button BoardDeleteButton;

    public BoardViewHolder(View itemView) {
        super(itemView);
        BoardMsg = (TextView) itemView.findViewById(R.id.board_msg);
        BoardWriter = (TextView) itemView.findViewById(R.id.board_writer);
        BoardDate = (TextView) itemView.findViewById(R.id.board_write_date);
        BoardLikeCount = (TextView) itemView.findViewById(R.id.board_like_count);
        BoardThumnail = (ImageView) itemView.findViewById(R.id.board_thumnail);
        BoardLikeButton = (ImageButton) itemView.findViewById(R.id.board_like_button);
        //BoardDeleteButton = (Button) itemView.findViewById(R.id.board_delete);
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
        BoardLikeCount.setText("좋아요 : " + data.LikeCnt);



        if(mine)
        {
            BoardLikeButton.setVisibility(ImageView.INVISIBLE);
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics());
            BoardLikeButton.setLayoutParams(new LinearLayout.LayoutParams(width,ViewGroup.LayoutParams.MATCH_PARENT, 1));

            if(deleteEnable)
            {
                /*BoardDeleteButton.setVisibility(ImageView.VISIBLE);
                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, context.getResources().getDisplayMetrics());
                BoardDeleteButton.setLayoutParams(new LinearLayout.LayoutParams(width,ViewGroup.LayoutParams.MATCH_PARENT, 1));*/
            }
        }
        else
        {
            BoardLikeButton.setVisibility(ImageView.VISIBLE);
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, context.getResources().getDisplayMetrics());
            BoardLikeButton.setLayoutParams(new LinearLayout.LayoutParams(width,ViewGroup.LayoutParams.MATCH_PARENT, 1));

           /* BoardDeleteButton.setVisibility(ImageView.INVISIBLE);
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 0, context.getResources().getDisplayMetrics());
            BoardDeleteButton.setLayoutParams(new LinearLayout.LayoutParams(width,ViewGroup.LayoutParams.MATCH_PARENT, 1));*/
        }
        RefreshLikeIcon(data);
    }

    private void RefreshLikeIcon(BoardMsgClientData boardData) {
        if (boardData.IsLikeUser(MyData.getInstance().getUserIdx()))
            BoardLikeButton.setImageResource(R.drawable.mycard_icon);
        else
            BoardLikeButton.setImageResource(R.drawable.mycard_empty_icon);

        BoardLikeCount.setText("좋아요 : " + boardData.LikeCnt);
    }

    public void SetBoardHolderListener(View.OnClickListener listener)
    {
        BoardMsg.setOnClickListener(listener);
        BoardThumnail.setOnClickListener(listener);
        BoardWriter.setOnClickListener(listener);
        BoardDate.setOnClickListener(listener);
        BoardLikeCount.setOnClickListener(listener);
        BoardLikeButton.setOnClickListener(listener);
        //BoardDeleteButton.setOnClickListener(listener);
    }
}
