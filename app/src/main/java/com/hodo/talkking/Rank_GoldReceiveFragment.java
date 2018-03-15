package com.hodo.talkking;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by mjk on 2017. 8. 21..
 */

public class Rank_GoldReceiveFragment extends Fragment {

    RecyclerView recyclerView;
    private SettingData mSettingData = SettingData.getInstance();
    public SimpleUserData stTargetData = new SimpleUserData();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    private Rank_GoldReceiveAdapter HotAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_honey_receive,container,false);
        HotAdapter = new Rank_GoldReceiveAdapter(getContext());
        HotAdapter.setHasStableIds(true);
        recyclerView = view.findViewById(R.id.rank_honey_receive);
        recyclerView.setAdapter(HotAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),mSettingData.getViewCount()));


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        CommonFunc.getInstance().ShowToast(view.getContext(), position+"번 째 아이템 클릭", true);
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

                            if(mCommon.getClickStatus() == false)
                                mCommon.getUserData(getActivity(), stTargetData);

                 /*           Intent intent = new_img Intent(view.getContext(), UserPageActivity.class);
                            Bundle bundle = new_img Bundle();

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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                   // Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    FirebaseData.getInstance().GetHotData(HotAdapter, nSize, false);
                }
            }
        });

        return view;
    }

    public Rank_GoldReceiveFragment() {
        super();
    }
}
