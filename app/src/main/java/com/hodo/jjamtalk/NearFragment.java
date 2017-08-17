package com.hodo.jjamtalk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.Util.RecyclerItemClickListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NearFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearFragment extends Fragment {

    private MyData mMyData = MyData.getInstance();
    private SettingData mSetting = SettingData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    public UserData stTargetData = new UserData();

    RecyclerView recyclerView;
    NearAdapter nearAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public NearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NearFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NearFragment newInstance(String param1, String param2) {
        NearFragment fragment = new NearFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near, container, false);
        recyclerView = view.findViewById(R.id.near_recyclerview);
        recyclerView.setAdapter(new NearAdapter(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                     //   Toast.makeText(view.getContext(), position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        if(mAppStatus.bCheckMultiSend == false) {
                            switch (mSetting.getnSearchSetting())
                            {
                                case 1:
                                    stTargetData = mMyData.arrUserMan_Near.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_Near.get(position);
                                    break;
                                case 3:
                                    stTargetData = mMyData.arrUserAll_Near.get(position);
                                    break;
                            }

                            Log.d("Guide !!!! ", "Start : " + position);
                            Intent intent = new Intent(view.getContext(), UserPageActivity.class);
                            intent.putExtra("Target", stTargetData);
                            view.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                    }
                }));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

}
