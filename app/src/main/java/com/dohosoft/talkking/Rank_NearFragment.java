package com.dohosoft.talkking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SettingData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.AppStatus;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.RecyclerItemClickListener;


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
    public UserData stTargetData = new UserData();
    private SettingData mSettingData = SettingData.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    RecyclerView recyclerView;

    private Rank_NearAdapter NearAdopter;


    public Rank_NearFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near, container, false);

        NearAdopter = new Rank_NearAdapter(getContext());
        NearAdopter.setHasStableIds(true);

        recyclerView = view.findViewById(R.id.near_recyclerview);
        recyclerView.setAdapter(NearAdopter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mSettingData.getViewCount()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //   Toast.makeText(view.getContext(), position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        if (mAppStatus.bCheckMultiSend == false) {

                            stTargetData = mMyData.arrUserAll_Near_Age.get(position);


                            if (mCommon.getClickStatus() == false)
                                mCommon.MoveUserPage(getActivity(), stTargetData);
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
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                    //  Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                       CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                       FirebaseData.getInstance().GetNearData(NearAdopter, nSize, false);
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

}
