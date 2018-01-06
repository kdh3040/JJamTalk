package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.BoardLikeData;
import com.hodo.jjamtalk.Data.BoardMsgClientData;
import com.hodo.jjamtalk.Data.BoardMsgDBData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.TempBoard_ReplyData;
import com.hodo.jjamtalk.Firebase.FirebaseData;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardItemActivity extends AppCompatActivity {

    // 현재 내 데이터
    private MyData mMyData = MyData.getInstance();
    // 모든 보드 데이터 -> 현재 바라보고 있는 데이터로 변경 예정
    private BoardMsgClientData mBoardClientData = null;
    private BoardData mBoardInstanceData = BoardData.getInstance();
    // 파이어베이스 인스턴스
    private FirebaseData mFireBaseData = FirebaseData.getInstance();

    // 뷰
    ListView BoardUIList;
    BoardReplyListAdapter BoardReplyListAdapter;

    // 상단본문
    TextView MasterName, MasterInfo, BoardWriteDate, BoardNote, BoardViewCount;
    ImageView MasterProfile;
    ImageButton LikeButton;
    RecyclerView BoardReplyRecyler, BoardLikeUserRecyler;

    private Boolean mLike = false;
    private int mBoardIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item); // activity_board_item 뷰 불러오기

        Intent intent = getIntent();
        mBoardIndex = intent.getIntExtra("Target", 0);
        mBoardClientData = mBoardInstanceData.BoardList.get(mBoardIndex);
        mLike = false;
        for (BoardLikeData data : mBoardClientData.LikeList) {
            if (data.Idx.equals(mMyData.getUserIdx()))
                mLike = true;
        }

        // 댓글 리스트 추가
        BoardUIList = (ListView) findViewById(R.id.listview_board_reply);
        BoardReplyListAdapter = new BoardReplyListAdapter(getApplicationContext());
        BoardUIList.addHeaderView(getLayoutInflater().inflate(R.layout.header_board_item, null, false));
        BoardUIList.addFooterView(getLayoutInflater().inflate(R.layout.footer_board_item, null, false));
        BoardUIList.setAdapter(BoardReplyListAdapter);

        // 상단 본문 데이터
        SetHeaderData();
        SetHeaderButtonData();
    }

    private void SetHeaderData() {
        BoardMsgDBData dbData = mBoardClientData.GetDBData();

        MasterName = (TextView) findViewById(R.id.tv_nickname);
        MasterInfo = (TextView) findViewById(R.id.tv_info);
        BoardWriteDate = (TextView) findViewById(R.id.tv_date);
        BoardNote = (TextView) findViewById(R.id.tv_content);
        BoardViewCount = (TextView) findViewById(R.id.tv_pagecount);
        MasterProfile = (ImageView)findViewById(R.id.iv_profile);

        MasterName.setText(dbData.NickName);
        MasterInfo.setText(dbData.Age + "세");
        BoardWriteDate.setText(dbData.Date);
        BoardNote.setText(dbData.Msg);
        BoardViewCount.setText("조회수" + mBoardClientData.ClientViewCount);
        Glide.with(getApplicationContext())
                .load(dbData.Img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(MasterProfile);
    }

    private void SetHeaderButtonData() {
        LikeButton = (ImageButton) findViewById(R.id.ib_vote_like);
        LikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLike) {
                    mFireBaseData.RemoveBoardLikeData(mBoardClientData.GetDBData().Key, mMyData.getUserIdx());
                    mBoardClientData.LikeCnt--;
                } else {
                    BoardLikeData sendData = new BoardLikeData();

                    sendData.Idx = mMyData.getUserIdx();
                    sendData.Img = mMyData.getUserImg();

                    mFireBaseData.SaveBoardLikeData(mBoardClientData.GetDBData().Key, sendData);
                    mBoardClientData.LikeCnt++;
                }
                mLike = !mLike;
                RefreshLikeIcon();
            }
        });

        RefreshLikeIcon();
    }

    private void RefreshLikeIcon() {
        if (mLike)
            LikeButton.setImageResource(R.drawable.mycard_icon);
        else
            LikeButton.setImageResource(R.drawable.mycard_empty_icon);
    }

    private void SetFooterButtonData() {
        /*btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TempBoard_ReplyData tempReply = new TempBoard_ReplyData();
                tempReply.Msg = "test";
                tempReply.Age = mMyData.getUserAge();
                tempReply.Idx = mMyData.getUserIdx();
                tempReply.NickName = mMyData.getUserNick();
                tempReply.Img = mMyData.getUserImg();
                tempReply.Key = mBoardInstanceData.arrBoardList.get(nTargetIdx).Key;

                mFireBaseData.SaveBoardReplyData(tempReply);
            }
        });*/
    }
}








        /*RecyclerView recyclerView_board_reply;
    RecyclerView recyclerView_board_reply_private;
    Button btn_send;

    TextView tv_Like, tv_Reply, tv_pagecount;
    ImageButton ib_vote_like,ib_warn;

    EditText et_reply;
    LinearLayout imageViewLayout;
    Toolbar toolbar;
    BoardReplyListAdapter adapter;


    TextView tv_Name, tv_Info, tv_Date, tv_Memo;
    ImageView iv_Profile;

    int nTargetIdx;
    boolean bReply;

    int nPosition;*/

    //BoardItemActivity.ReplyAdapter Adapter = new BoardItemActivity.ReplyAdapter();


        /*
        //toolbar = (Toolbar) findViewById(R.id.toolbar);

        //toolbar.setTitle("허허참");
        //toolbar.setTitleTextColor(Color.parseColor("#ff0099cc"));

        //setSupportActionBar(toolbar);

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.board_icon);
        //setSupportActionBar(toolbar);
        Intent intent = getIntent();
        nTargetIdx = intent.getIntExtra("Target", 0);

        mBoardData.arrBoardList.get(nTargetIdx).PageCnt += 1;

        bReply = false;
        et_reply = (EditText)findViewById(R.id.et_reply);
        et_reply.setOnKeyListener(new EditMessageOnKeyListener());

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
                tempReply.Key =  mBoardData.arrBoardList.get(nTargetIdx).Key;

                if(bReply == true)
                {
                    setReplyData(nPosition, tempReply);
                    nPosition = 0;
                    bReply = false;
                }
                else
                {
                    mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.add(tempReply);
                }

                mBoardData.arrBoardList.get(nTargetIdx).Reply.put(tempReply.Key, tempReply);
                mFireBaseData.SaveBoardReplyData(tempReply);
                Adapter.notifyDataSetChanged();
                et_reply.setText("");

                InputMethodManager im = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(et_reply.getWindowToken(), 0);

            }
        });

        ib_vote_like = (ImageButton) findViewById(R.id.ib_vote_like);
        ib_vote_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"좋아요를 눌렀습니다",Toast.LENGTH_SHORT).show();
                mBoardData.arrBoardList.get(nTargetIdx).LikeCnt += 1;
            }
        });

        tv_Like = (TextView) findViewById(R.id.tv_like);
        tv_Like.setText(Integer.toString(mBoardData.arrBoardList.get(nTargetIdx).LikeCnt));

        tv_Reply = (TextView) findViewById(R.id.tv_reply);
        tv_Reply.setText(Integer.toString( mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.size()));

        tv_pagecount = (TextView) findViewById(R.id.tv_pagecount);
        tv_pagecount.setText("조회수 : " + Integer.toString( mBoardData.arrBoardList.get(nTargetIdx).PageCnt));

        tv_Name = (TextView)findViewById(R.id.tv_nickname);
        tv_Info = (TextView)findViewById(R.id.tv_info);
        tv_Date = (TextView)findViewById(R.id.tv_date);
        tv_Memo = (TextView)findViewById(R.id.tv_content);
        iv_Profile = (ImageView)findViewById(R.id.iv_profile);

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

        //setReplyData();
        /*
            recyclerView_board_reply = (RecyclerView)findViewById(R.id.recyclerview_board_reply);
            recyclerView_board_reply.setAdapter(Adapter);

        //스크롤 못하게
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };

        recyclerView_board_reply.setLayoutManager(linearLayoutManager);

        recyclerView_board_reply.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView_board_reply, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        et_reply.setText(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).NickName);
                        nPosition = position;

                        InputMethodManager im = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(et_reply.getWindowToken(), 1);

                        bReply = true;
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void setReplyData(int index, TempBoard_ReplyData reply) {
//        for(HashMap.Entry<String, TempBoard_ReplyData> entry : mBoardData.arrBoardList.get(nTargetIdx).Reply.entrySet())
            mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.add(index+1, reply);
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
            View view;

            if(bReply == true)
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_board_reply_reply,parent,false);

            else
            {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_board_reply,parent,false);
            }

            return new BoardReplyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BoardReplyViewHolder holder, final int position) {
            //holder.idTextView.setText("호근 ,37, 20km");

            if (bReply == true) {
                holder.idTextView.setText(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).NickName + ", " + mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Age);// + ", " +  mBoardData.arrBoardList.get(position).Dist);
                holder.messageTextView.setText(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Msg);
                //holder.imageView.setImageResource(R.drawable.bg1);
                Glide.with(getApplicationContext())
                        .load(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView);

                bReply = false;

            } else {
                if (mMyData.getUserIdx().equals(mBoardData.arrBoardList.get(nTargetIdx).Idx) || mMyData.getUserIdx().equals(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Idx)) {
                    holder.idTextView.setText(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).NickName + ", " + mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Age);// + ", " +  mBoardData.arrBoardList.get(position).Dist);
                    holder.messageTextView.setText(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Msg);
                    //holder.imageView.setImageResource(R.drawable.bg1);
                    Glide.with(getApplicationContext())
                            .load(mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.get(position).Img)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imageView);
                }
                else {
                    holder.idTextView.setText("비밀 댓글");
                    holder.messageTextView.setText("댓글은 본인만 확인 가능합니다.");
                    holder.imageView.setImageResource(R.drawable.icon_camera);
                }
            }


        }

        @Override
        public int getItemCount() {

            return mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.size();
        }
    }

    class EditMessageOnKeyListener implements View.OnKeyListener{

        @Override
        public boolean onKey(View view, int keycode, KeyEvent event) {

           if(event.getAction() == KeyEvent.KEYCODE_ENTER){
                TempBoard_ReplyData tempReply = new TempBoard_ReplyData();
                tempReply.Msg = et_reply.getText().toString();
                tempReply.Age = mMyData.getUserAge();
                tempReply.Idx = mMyData.getUserIdx();
                tempReply.NickName = mMyData.getUserNick();
                tempReply.Img = mMyData.getUserImg();
                tempReply.Key =  mBoardData.arrBoardList.get(nTargetIdx).Key;

                mBoardData.arrBoardList.get(nTargetIdx).arrReplyList.add(tempReply);
                mBoardData.arrBoardList.get(nTargetIdx).Reply.put(tempReply.Key, tempReply);
                mFireBaseData.SaveBoardReplyData(tempReply);
                Adapter.notifyDataSetChanged();
                et_reply.setText("");

                InputMethodManager im = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(et_reply.getWindowToken(), 0);
            }

            return false;
        }*/

