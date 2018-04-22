package com.dohosoft.talkking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dohosoft.talkking.Data.BoardData;
import com.dohosoft.talkking.Data.BoardMsgClientData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.ViewHolder.BoardViewHolder;

import static com.dohosoft.talkking.MainActivity.mFragmentMng;

/**
 * Created by mjk on 2017. 8. 14..
 */

public class BoardMyListActivity extends AppCompatActivity {
    private MyData mMyData = MyData.getInstance();
    private BoardData mBoardInstanceData = BoardData.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    Activity mActivity;
    RecyclerView MyBoardSlotListRecycler;

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        MyData.getInstance().SetCurFrag(0);

        if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
            mMyData.badgecount = pref.getInt("Badge",  1);

            if (mMyData.badgecount >= 1) {
                mMyData.badgecount = 0;
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count_package_name", "com.hodo.talkking");
                intent.putExtra("badge_count_class_name", "com.hodo.talkking.LoginActivity");
                intent.putExtra("badge_count", mMyData.badgecount);
                sendBroadcast(intent);
            }
        }
    }

        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_board_mylist);
        MyData.getInstance().SetCurFrag(0);

        MyBoardSlotListRecycler = (RecyclerView)findViewById(R.id.board_Mylist);
        MyBoardSlotListRecycler.setAdapter(new BoardMyListActivity.BoardMyListAdapter());
        MyBoardSlotListRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMyData.SetCurFrag(0);
    }


    private class BoardMyListAdapter extends RecyclerView.Adapter<BoardViewHolder> {
        @Override
        public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_board,parent,false);

            return new BoardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final BoardViewHolder holder, final int position) {
            holder.SetBoardViewHolder(getApplicationContext(), mBoardInstanceData.MyBoardList.get(position), true, true);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.board_delete:
                        {
                            CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                                public void YesListener() {
                                    BoardMsgClientData data =  mBoardInstanceData.MyBoardList.get(position);
                                    mBoardInstanceData.RemoveBoard(data.GetDBData().BoardIdx);
                                    FirebaseData.getInstance().RemoveBoard(data.GetDBData().BoardIdx);

                                    FirebaseData.getInstance().GetInitBoardData();
                                    FirebaseData.getInstance().GetInitMyBoardData();

                                    Fragment frg = null;
                                    frg = mFragmentMng.findFragmentByTag("BoardFragment");
                                    final FragmentTransaction ft = mFragmentMng.beginTransaction();
                                    ft.detach(frg);
                                    ft.attach(frg);
                                    ft.commitAllowingStateLoss();

                                    onBackPressed();
                                }
                            };

                            CommonFunc.getInstance().ShowDefaultPopup(mActivity, listener,null,  "삭제", "작성한 글을 제거하시겠습니까?", "예", "아니요");
                        }
                            break;
                        default:
                            break;
                    }
                }
            };

            holder.SetBoardHolderListener(listener);
        }

        @Override
        public int getItemCount() {
            return mBoardInstanceData.MyBoardList.size();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mMyData.SetCurFrag(4);
    }
}
