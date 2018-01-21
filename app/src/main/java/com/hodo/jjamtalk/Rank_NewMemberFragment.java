package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Data.SimpleUserData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.Util.CommonFunc;
import com.hodo.jjamtalk.Util.RecyclerItemClickListener;

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


    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newmember, container, false);
        recyclerView = view.findViewById(R.id.newmember_recyclerview);
        recyclerView.setAdapter(new Rank_NewMemberAdapter(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),mSettingData.getViewCount()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(view.getContext(), position + "번 째 아이템 클릭", Toast.LENGTH_SHORT).show();
                       if (mAppStatus.bCheckMultiSend == false) {
                            switch (mSetting.getnSearchSetting()) {
                                case 1:
                                    stTargetData = mMyData.arrUserMan_New.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_New.get(position);
                                    break;
                                case 3:
                                    stTargetData = mMyData.arrUserAll_New.get(position);
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

        return view;
    }

    public Rank_NewMemberFragment() {
        super();
    }
}
