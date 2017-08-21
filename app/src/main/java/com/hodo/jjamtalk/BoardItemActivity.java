package com.hodo.jjamtalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.TempBoardData;
import com.hodo.jjamtalk.Data.TempBoard_ReplyData;
import com.hodo.jjamtalk.ViewHolder.BoardReplyViewHolder;
import com.hodo.jjamtalk.ViewHolder.BoardViewHolder;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardItemActivity extends AppCompatActivity{

    private MyData mMyData = MyData.getInstance();
    private BoardData mBoardData = BoardData.getInstance();

    RecyclerView recyclerView_board_reply;
    Button btn_send;
    ImageButton ib_vote_like,ib_warn;
    EditText et_reply;
    LinearLayout imageViewLayout;
    Toolbar toolbar;


    TextView tv_Name, tv_Info, tv_Date, tv_Memo;
    ImageView iv_Profile;

    int nTargetIdx;

    BoardItemActivity.ReplyAdapter Adapter = new BoardItemActivity.ReplyAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);

        //toolbar.setTitle("허허참");
        //toolbar.setTitleTextColor(Color.parseColor("#ff0099cc"));

        //setSupportActionBar(toolbar);

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.board_icon);
        //setSupportActionBar(toolbar);
        et_reply = (EditText)findViewById(R.id.et_reply);

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TempBoard_ReplyData tempReply = new TempBoard_ReplyData();
                tempReply.Msg = et_reply.getText().toString();
                tempReply.Age = mMyData.getUserAge();
                tempReply.Idx = mMyData.getUserIdx();
                tempReply.NickName = mMyData.getUserNick();
                tempReply.Img = mMyData.getUserImg();

                mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.add(tempReply);
                Adapter.notifyDataSetChanged();
            }
        });


        ib_vote_like = (ImageButton) findViewById(R.id.ib_vote_like);
        ib_vote_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"좋아요를 눌렀습니다",Toast.LENGTH_SHORT).show();
            }
        });



        recyclerView_board_reply = (RecyclerView)findViewById(R.id.recyclerview_board_reply);
        recyclerView_board_reply.setAdapter(Adapter);
        recyclerView_board_reply.setLayoutManager(new LinearLayoutManager(this));

        tv_Name = (TextView)findViewById(R.id.tv_nickname);
        tv_Info = (TextView)findViewById(R.id.tv_info);
        tv_Date = (TextView)findViewById(R.id.tv_date);
        tv_Memo = (TextView)findViewById(R.id.tv_content);
        iv_Profile = (ImageView)findViewById(R.id.iv_profile);


        Intent intent = getIntent();
        nTargetIdx = intent.getIntExtra("Target", 0);
        tv_Name.setText(mBoardData.arrBoardList.get(nTargetIdx).NickName);
        tv_Info.setText(mBoardData.arrBoardList.get(nTargetIdx).Job);
        tv_Date.setText(mBoardData.arrBoardList.get(nTargetIdx).Date);
        tv_Memo.setText(mBoardData.arrBoardList.get(nTargetIdx).Msg);
        Glide.with(getApplicationContext())
                .load(mBoardData.arrBoardList.get(nTargetIdx).Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_Profile);

        imageViewLayout = (LinearLayout)findViewById(R.id.layout_imageview);
        imageViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ImageViewPager.class));
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board_item,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_report:
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).
                        setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).setMessage("이 사람을 신고하시겠습니까?")
                        .setTitle("신고하기");
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

        }
        return true;
    }


    private class ReplyAdapter extends RecyclerView.Adapter<BoardReplyViewHolder> {
        @Override
        public BoardReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_board_reply,parent,false);

            return new BoardReplyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(BoardReplyViewHolder holder, final int position) {
            //holder.idTextView.setText("호근 ,37, 20km");
        //    for(int i =0; i<mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.size(); i++)
            {
                holder.idTextView.setText(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).NickName + ", " + mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Age);// + ", " +  mBoardData.arrBoardList.get(position).Dist);
                holder.messageTextView.setText(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Msg);
                //holder.imageView.setImageResource(R.drawable.bg1);
                Glide.with(getApplicationContext())
                        .load(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView);
            }

        }

        @Override
        public int getItemCount() {
            return mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.size();
        }
    }
}
