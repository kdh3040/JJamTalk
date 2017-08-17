package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mjk on 2017. 8. 17..
 */

public class HoneyGetFragment extends Fragment {





    public HoneyGetFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_honey_get,container,false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.rv_honey_get);
        HoneyGetAdapter honeyGetAdapter = new HoneyGetAdapter(getContext());
        recyclerView.setAdapter(honeyGetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),1,false));

        return view;
    }
}
