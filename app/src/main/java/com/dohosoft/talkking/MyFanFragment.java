package com.dohosoft.talkking;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dohosoft.talkking.Data.MyData;

/**
 * Created by mjk on 2017. 8. 27..
 */

public class MyFanFragment extends Fragment {

    RecyclerView recyclerView;
    FanListAdapter fanListAdapter;
    Context mContext;

    private MyData mMyData = MyData.getInstance();

    public MyFanFragment(){


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_fan,container,false);
        view.setTag("fanlist");



        recyclerView = view.findViewById(R.id.rv_fanlist);
        fanListAdapter = new FanListAdapter(getContext());

        recyclerView.setAdapter(fanListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),1,false));
        fanListAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        fanListAdapter.notifyDataSetChanged();
    }


}
