package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Data.SimpleUserData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.Util.CommonFunc;
import com.hodo.jjamtalk.Util.RecyclerItemClickListener;

import java.util.LinkedHashMap;

/**
 * Created by mjk on 2017. 8. 21..
 */

public class Rank_GoldReceiveFragment extends Fragment {

    RecyclerView recyclerView;
    private SettingData mSettingData = SettingData.getInstance();
    public SimpleUserData stTargetData = new SimpleUserData();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_honey_receive,container,false);
        recyclerView = view.findViewById(R.id.rank_honey_receive);
        recyclerView.setAdapter(new Rank_GoldReceiveAdapter(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),mSettingData.getViewCount()));


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(view.getContext(), position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                        if(mAppStatus.bCheckMultiSend == false) {

                            switch (mSettingData.getnSearchSetting())
                            {
                      /*          case 0:
                                case 3:
                                    stTargetData = mMyData.arrUserAll_Recv.get(position);
                                    break;
                                case 1:
                                    stTargetData = mMyData.arrUserMan_Recv.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_Recv.get(position);
                                    break;*/

                                case 1:
                                    stTargetData = mMyData.arrUserMan_Recv_Age.get(position);
                                    break;
                                case 2:
                                    stTargetData = mMyData.arrUserWoman_Recv_Age.get(position);
                                    break;
                                case 0:
                                case 3:
                                    stTargetData = mMyData.arrUserAll_Recv_Age.get(position);
                                    break;
                            }

                            mCommon.getUserData(getActivity(), stTargetData);

                 /*           Intent intent = new Intent(view.getContext(), UserPageActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putSerializable("Target", stTargetData);
                            intent.putExtra("FanList", stTargetData.arrFanList);
                            intent.putExtra("FanCount", stTargetData.FanCount);

                            intent.putExtra("StarList", stTargetData.arrStarList);
                            intent.putExtras(bundle);

                            view.getContext().startActivity(intent);*/
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                    }
                }));

        return view;
    }

    public Rank_GoldReceiveFragment() {
        super();
    }
}
