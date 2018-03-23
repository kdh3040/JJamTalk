package com.dohosoft.talkking;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dohosoft.talkking.Data.FanData;

import java.util.ArrayList;

/**
 * Created by mjk on 2017. 8. 28..
 */

public class TargetLikeFragment extends Fragment{

    private ArrayList<FanData> stTargetData;

    public TargetLikeFragment()
    {

    }
    @SuppressLint("ValidFragment")
    public TargetLikeFragment(ArrayList<FanData> TargetData) {
        stTargetData = TargetData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_like,container,false);
/*        RecyclerView recyclerView = view.findViewById(R.id.rv_my_like);
        recyclerView.setAdapter(new_img TargetLikeAdapter(getContext(), stTargetData));
        recyclerView.setLayoutManager(new_img LinearLayoutManager(getContext(),1,false));*/


        return view;

    }
}
