/*
 * Copyright 2015 chenupt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hodo.jjamtalk;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.SettingData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.AppStatus;
import com.hodo.jjamtalk.Util.LocationFunc;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by chenupt@gmail.com on 2015/1/31.
 * Description TODO
 */
public class GuideFragment extends Fragment {

    private SettingData mSetting = SettingData.getInstance();

    private int bgRes;
    private ImageView imageView;

    private MainAdapter mainAdapter;
    RecyclerView recyclerView;

    private LocationFunc mLocFunc = LocationFunc.getInstance();
    private MyData mMyData = MyData.getInstance();
    private AppStatus mAppStatus = AppStatus.getInstance();

    private int UserManCnt;
    private int UserWomanCnt;
    private int UserAllCnt;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //bgRes = getArguments().getInt("data");
        //mainAdapter = new MainAdapter();

        UserManCnt = mMyData.arrUserMan_Rank.size();
        UserWomanCnt = mMyData.arrUserWoman_Rank.size();
        UserAllCnt = mMyData.arrUserAll_Rank.size();

        initData();
    }

    private void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        recyclerView = view.findViewById(R.id.recy_view);
        recyclerView.setAdapter(new MainAdapter());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d ( "Guide @@@", "페이지 전환 3  "  + view.getContext());

        //imageView = (ImageView) getView().findViewById(R.id.image);
        //imageView.setBackgroundResource(bgRes);
        //gridView.setAdapter((ListAdapter) mainAdapter);
    }
    class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_user,parent,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(mAppStatus.bCheckMultiSend == false) {
                       startActivity(new Intent(getContext(), UserPageActivity.class));

                   }


                }
            });

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Log.d("Guide !!!! ", "Start");
            int i = position;

            switch (mSetting.getnSearchSetting())
            {
                //  남자 탐색
                case 1:
                    float Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserMan_Rank.get(i).Lat, mMyData.arrUserMan_Rank.get(i).Lon);
                    Log.d("Guide !!!! ", "Case 1 : "+ (int)Dist);
                    holder.textView.setText(mMyData.arrUserMan_Rank.get(i).NickName + ", " + mMyData.arrUserMan_Rank.get(i).Age + "세, " + (int)Dist + "km");
                    break;
                // 여자 탐색
                case 2:
                    Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserWoman_Rank.get(i).Lat, mMyData.arrUserWoman_Rank.get(i).Lon);
                    Log.d("Guide !!!! ", "Case 2 : "+ (int)Dist);
                    holder.textView.setText(mMyData.arrUserWoman_Rank.get(i).NickName + ", " + mMyData.arrUserWoman_Rank.get(i).Age + "세, " + (int)Dist + "km");
                    break;
                case 3:
                    Log.d("Guide !!!! ", "Case 3");
                    Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), mMyData.arrUserAll_Rank.get(i).Lat, mMyData.arrUserAll_Rank.get(i).Lon);
                    holder.textView.setText(mMyData.arrUserAll_Rank.get(i).NickName + ", " + mMyData.arrUserAll_Rank.get(i).Age + "세, " + (int)Dist + "km");
                    break;
                default:
                    break;
            }

        }

        @Override
        public int getItemCount() {
            int rtValue = 0;
            if (mSetting.getnSearchSetting() == 1) {
                Log.d("Guide !!!! ", "getItem 1");
                rtValue = mMyData.arrUserMan_Rank.size();
            } else if (mSetting.getnSearchSetting() == 2) {
                Log.d("Guide !!!! ", "getItem 2");
                rtValue = mMyData.arrUserWoman_Rank.size();
            } else if (mSetting.getnSearchSetting() == 3) {
                Log.d("Guide !!!! ", "getItem 3");
                rtValue = mMyData.arrUserAll_Rank.size();
            }
            return rtValue;
        }


        public  class ViewHolder extends RecyclerView.ViewHolder{

            public ImageView image;
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                image = (ImageView)itemView.findViewById(R.id.iv_user);
                textView = (TextView)itemView.findViewById(R.id.tv_user);

            }
        }
    }


}
