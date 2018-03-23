package com.dohosoft.talkking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SettingData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.AppStatus;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.RecyclerItemClickListener;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class Rank_NewMemberFragment extends Fragment {

    private MyData mMyData = MyData.getInstance();
    private SettingData mSettingData = SettingData.getInstance();

    private SettingData mSetting = SettingData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    public SimpleUserData stTargetData = new SimpleUserData();
    private CommonFunc mCommon = CommonFunc.getInstance();

    private Rank_NewMemberAdapter NewAdapter;

    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newmember, container, false);

        NewAdapter = new Rank_NewMemberAdapter(getContext());
        NewAdapter.setHasStableIds(true);
        recyclerView = view.findViewById(R.id.newmember_recyclerview);
        recyclerView.setAdapter(NewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),mSettingData.getViewCount()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       // Toast.makeText(view.getContext(), position + "번 째 아이템 클릭", Toast.LENGTH_SHORT).show();
                       if (mAppStatus.bCheckMultiSend == false) {
                            switch (mSetting.getnSearchSetting()) {
                                /*case 0:
                                case 3:
                                    stTargetData = mMyData.arrUserAll_New.get(position);
                                    break;
                                case 1:
                                    stTargetData = mMyData.arrUserMan_New.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_New.get(position);
                                    break;*/

                                case 1:
                                    stTargetData = mMyData.arrUserMan_New_Age.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_New_Age.get(position);
                                    break;
                                case 0:
                                case 3:
                                    stTargetData = mMyData.arrUserAll_New_Age.get(position);
                                    break;
                            }

                            if(mCommon.getClickStatus() == false)
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
                  //  Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                    FirebaseData.getInstance().GetNewData(NewAdapter, nSize, false);
                }
            }
        });

        return view;
    }

    public Rank_NewMemberFragment() {
        super();
    }
}
