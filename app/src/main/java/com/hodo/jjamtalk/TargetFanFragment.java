package com.hodo.jjamtalk;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.kakao.usermgmt.response.model.User;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class TargetFanFragment extends Fragment {

    RecyclerView recyclerView;
    TargetFanAdapter fanListAdapter;
    Context mContext;

    private UserData stTargetData;

    public TargetFanFragment(UserData TargetData) {

        stTargetData = TargetData;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_fan,container,false);

        recyclerView = view.findViewById(R.id.rv_fanlist);
        fanListAdapter = new TargetFanAdapter(getContext(), stTargetData);

        recyclerView.setAdapter(fanListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),1,false));
        return view;
    }



}