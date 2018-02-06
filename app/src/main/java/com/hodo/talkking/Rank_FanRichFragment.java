package com.hodo.talkking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hodo.talkking.Data.BoardMsgClientData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SettingData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.AppStatus;
import com.hodo.talkking.Util.CommonFunc;
import com.hodo.talkking.Util.RecyclerItemClickListener;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class Rank_FanRichFragment extends Fragment {

    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    public SimpleUserData stTargetData = new SimpleUserData();
    private SettingData mSettingData = SettingData.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();
    private Rank_FanRichAdapter FanAdapter;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        FanAdapter = new Rank_FanRichAdapter(getContext());

        recyclerView = view.findViewById(R.id.rank_recyclerview);
        recyclerView.setAdapter(FanAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),mSettingData.getViewCount()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(view.getContext(), position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        if(mAppStatus.bCheckMultiSend == false) {
                            switch (mSettingData.getnSearchSetting())
                            {
                      /*          case 0:
                                case 3:
                                    stTargetData = mMyData.arrUserAll_Send.get(position);
                                    break;
                                case 1:
                                    stTargetData = mMyData.arrUserMan_Send.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_Send.get(position);
                                    break;*/

                                case 1:
                                    stTargetData = mMyData.arrUserMan_Send_Age.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_Send_Age.get(position);
                                    break;
                                case 0:
                                case 3:
                                    stTargetData = mMyData.arrUserAll_Send_Age.get(position);
                                    break;
                            }

                            mCommon.getUserData(getActivity(), stTargetData);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                    }
                }));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                    Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    FirebaseData.getInstance().GetFanData(FanAdapter, nSize, false);
                }
            }
        });


        return view;
    }

    public Rank_FanRichFragment() {
        super();
    }
}
