package com.hodo.jjamtalk.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodo.jjamtalk.R;

import org.w3c.dom.Text;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardViewHolder extends RecyclerView.ViewHolder {

    public TextView BoardMsg, BoardWriter, BoardDate, BoardLikeCount;
    public ImageView BoardThumnail;
    public ImageButton BoardLikeButton;

    public BoardViewHolder(View itemView) {
        super(itemView);
        BoardMsg = (TextView) itemView.findViewById(R.id.board_msg);
        BoardWriter = (TextView) itemView.findViewById(R.id.board_writer);
        BoardDate = (TextView) itemView.findViewById(R.id.board_write_date);
        BoardLikeCount = (TextView) itemView.findViewById(R.id.board_like_count);
        BoardThumnail = (ImageView) itemView.findViewById(R.id.board_thumnail);
        BoardLikeButton = (ImageButton) itemView.findViewById(R.id.board_like_button);
    }
}
