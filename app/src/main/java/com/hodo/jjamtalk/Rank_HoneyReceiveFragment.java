package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.Data.SettingData;

/**
 * Created by mjk on 2017. 8. 21..
 */

public class Rank_HoneyReceiveFragment extends Fragment {

    RecyclerView recyclerView;
    private SettingData mSettingData = SettingData.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_honey_receive,container,false);
        recyclerView = view.findViewById(R.id.rank_honey_receive);
        recyclerView.setAdapter(new Rank_HoneyReceiveAdapter(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),mSettingData.getViewCount()));
        return view;
    }
}
