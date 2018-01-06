package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.BoardMsgClientData;
import com.hodo.jjamtalk.Data.BoardMsgDBData;
import com.hodo.jjamtalk.Data.UIData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.CommonFunc;
import com.hodo.jjamtalk.Util.RecyclerItemClickListener;
import com.hodo.jjamtalk.ViewHolder.BoardViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class BoardFragment extends Fragment {

    // 파이어베이스 인스턴스
    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    // 보드 인스턴트 데이터
    private BoardData mBoardInstanceData = BoardData.getInstance();
    // UI 인스턴트 데이터
    private UIData mUIData = UIData.getInstance();
    // FragmentView 데이터
    private View mFragmentView = null;
    // 보드 리스트 아답터
    BoardListAdapter BoradListAdapter = new BoardListAdapter();

    // 보드 리스트 UI
    RecyclerView BoardSlotListRectcler;
    Button WriteButton, MyWriteListButton;

    private class BoardListAdapter extends RecyclerView.Adapter<BoardViewHolder> {
        @Override
        public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.content_board,parent,false);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/7));

            return new BoardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BoardViewHolder holder, final int position) {
            //holder.idTextView.setText("호근 ,37, 20km");
            BoardMsgClientData BoardData =  mBoardInstanceData.BoardList.get(position);
            if(BoardData == null)
                 return;
            BoardMsgDBData dbData = BoardData.GetDBData();
            holder.idTextView.setText(dbData.NickName + ", " + dbData.Age);// + ", " +  mBoardClientData.arrBoardList.get(position).Dist);
            holder.messageTextView.setText(dbData.Msg);
            holder.likeCount.setText("좋아요 : " + BoardData.LikeCnt);
            holder.replyCount.setText("댓글수 : " + BoardData.ReplyCnt);
            holder.writeDate.setText("쓴 날자 : " + dbData.Date);
            //holder.iv_profile.setImageResource(R.drawable.bg1);
            Glide.with(getContext())
                    .load(dbData.Img)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mBoardInstanceData.BoardList.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mFragmentView != null) {
            // Fragment가 다시 새로 만들어 질때 갱신
            //CommonFunc.getInstance().refreshFragMent(this);
        }
        else
        {
            mFragmentView = inflater.inflate(R.layout.fragment_board,container,false);
            BoardSlotListRectcler = (RecyclerView)mFragmentView.findViewById(R.id.board_recy);
            BoardSlotListRectcler.setAdapter(BoradListAdapter);
            BoardSlotListRectcler.setLayoutManager(new LinearLayoutManager(getContext()));

            WriteButton = (Button)mFragmentView.findViewById(R.id.btn_write);
            MyWriteListButton = (Button)mFragmentView.findViewById(R.id.btn_mylist);

            BoardSlotListRectcler.addOnItemTouchListener(GetBoradListClickFunc());
            WriteButton.setOnClickListener(GetWriteBoradFunc());
            MyWriteListButton.setOnClickListener(GetMyWriteBoradListFunc());
        }
        return mFragmentView;
    }

    private RecyclerView.OnItemTouchListener GetBoradListClickFunc()
    {
        RecyclerView.OnItemTouchListener returnFunc =
        new RecyclerItemClickListener(getContext(), BoardSlotListRectcler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), BoardItemActivity.class);
                BoardMsgClientData boardMsgData =  mBoardInstanceData.BoardList.get(position);
                if(boardMsgData == null)
                    return;

                // Intent 하면서 변수를 넘겨준다. TODO 환웅 키값을 넘겨주는게 좋지 않을까..
                intent.putExtra("Target", position);
                boardMsgData.PlusViewCount();

                // 조회수 갱신
                mFireBaseData.PushBoardViewCount(boardMsgData.GetDBData().Key);

                startActivity(intent);
            }
            @Override
            public void onLongItemClick(View view, int position) {}
        });

        return returnFunc;
    }

    private View.OnClickListener GetWriteBoradFunc()
    {
        View.OnClickListener returnFunc = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),BoardWriteActivity.class));
            }
        };

        return returnFunc;
    }

    private View.OnClickListener GetMyWriteBoradListFunc()
    {
        View.OnClickListener returnFunc = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),BoardMyListActivity.class));
            }
        };

        return returnFunc;
    }
}
