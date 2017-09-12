package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodo.jjamtalk.Data.UserData;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class TargetLikeFragment extends Fragment{

    public TargetLikeFragment(UserData TargetData) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_like,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_my_like);
        recyclerView.setAdapter(new TargetLikeAdapter(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),1,false));


        return view;

    }
}
