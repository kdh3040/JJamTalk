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
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.AppStatus;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.RecyclerItemClickListener;

/**
 * Created by mjk on 2017. 8. 21..
 */

public class Rank_HotFragment extends Fragment {

    RecyclerView recyclerView;
    private SettingData mSettingData = SettingData.getInstance();
    public UserData stTargetData = new UserData();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    private Rank_HotAdapter RecvAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_honey_receive, container, false);
        RecvAdapter = new Rank_HotAdapter(getContext());
        RecvAdapter.setHasStableIds(true);
        recyclerView = view.findViewById(R.id.rank_honey_receive);
        recyclerView.setAdapter(RecvAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mSettingData.getViewCount()));


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //CommonFunc.getInstance().ShowToast(view.getContext(), position+"번 째 아이템 클릭", true);
                        if (mAppStatus.bCheckMultiSend == false) {
                            stTargetData = mMyData.arrUserAll_Hot_Age.get(position);

                            if (mCommon.getClickStatus() == false)
                                mCommon.MoveUserPage(getActivity(), stTargetData);
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
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                    // Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                      CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                     FirebaseData.getInstance().GetHotData(RecvAdapter, false);
                }
            }
        });

        return view;
    }

    public Rank_HotFragment() {
        super();
    }
}
