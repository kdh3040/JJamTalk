package com.hodo.talkking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SettingData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.AppStatus;
import com.hodo.talkking.Util.CommonFunc;
import com.hodo.talkking.Util.RecyclerItemClickListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Rank_NearFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Rank_NearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Rank_NearFragment extends Fragment {

    private MyData mMyData = MyData.getInstance();
    private SettingData mSetting = SettingData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    public SimpleUserData stTargetData = new SimpleUserData();
    private SettingData mSettingData = SettingData.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    RecyclerView recyclerView;
    Rank_NearAdapter rankNearAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Rank_NearAdapter NearAdopter;


    public Rank_NearFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Rank_NearFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Rank_NearFragment newInstance(String param1, String param2) {
        Rank_NearFragment fragment = new Rank_NearFragment();
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
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),mSettingData.getViewCount());


        NearAdopter = new Rank_NearAdapter(getContext());


        recyclerView.setAdapter(NearAdopter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                     //   Toast.makeText(view.getContext(), position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        if(mAppStatus.bCheckMultiSend == false) {
                            switch (mSetting.getnSearchSetting())
                            {
                          /*      case 0:
                                case 3:
                                    stTargetData = mMyData.arrUserAll_Near.get(position);
                                    break;
                                case 1:
                                    stTargetData = mMyData.arrUserMan_Near.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_Near.get(position);
                                    break;*/


                                case 1:
                                    stTargetData = mMyData.arrUserMan_Near_Age.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_Near_Age.get(position);
                                    break;
                                case 0:
                                case 3:
                                    stTargetData = mMyData.arrUserAll_Near_Age.get(position);
                                    break;
                            }

                            if(mCommon.getClickStatus() == false)
                                mCommon.getUserData(getActivity(), stTargetData);
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
                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                  //  Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    FirebaseData.getInstance().GetNearData(NearAdopter, nSize, false);
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

}
