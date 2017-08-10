package com.hodo.jjamtalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class HotFragment extends Fragment {

    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        recyclerView = view.findViewById(R.id.hot_recyclerview);
        recyclerView.setAdapter(new HotAdapter(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        return view;
    }

    public HotFragment() {
        super();
    }

}
