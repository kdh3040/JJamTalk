package com.dohosoft.talkking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.FanData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SimpleChatData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.UIData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.ViewHolder.ChatListViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.dohosoft.talkking.Data.CoomonValueData.TEXTCOLOR_MAN;
import static com.dohosoft.talkking.Data.CoomonValueData.TEXTCOLOR_WOMAN;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatListFragment extends Fragment {
    RecyclerView chatListRecyclerView;
    private MyData mMyData = MyData.getInstance();
    //private ArrayList<String> arrChatNameData = new_img ArrayList<>();
    //private ArrayList<SendData> arrChatData = new_img ArrayList<>();
    Menu mMenu;
    Context mTempContext;
    Context mContext;
    UIData mUIData = UIData.getInstance();
    LinearLayout layout_chatlist;

    View fragView;
    private TextView txt_empty;

    public void refreshFragMent()
    {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.detach(this).attach(this).commit();
    }

    public  void refresh()
    {
        SortByChatDate();
        mAdapter.notifyDataSetChanged();
    }

    ChatListAdapter mAdapter = new ChatListAdapter();

    public ChatListFragment() {SortByChatDate();}

    @SuppressLint("ValidFragment")
    public ChatListFragment(Context Context) {
        SortByChatDate();
        mTempContext = Context;
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.FOREGROUND) {

            mMyData.SetCurFrag(2);
        }
    }

    /*
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            chatListRecyclerView = (RecyclerView)findViewById(R.id.chat_list_recy);

            chatListRecyclerView.setAdapter(mAdapter);
            chatListRecyclerView.setLayoutManager(new_img LinearLayoutManager(this));
            mAdapter.notifyDataSetChanged();

        }
    */


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();


        if (fragView!= null) {

            if(mMyData.arrChatDataList.size() == 0)
            {
                txt_empty.setVisibility(View.VISIBLE);
            }
            else {
                txt_empty.setVisibility(View.GONE);
                CommonFunc.getInstance().RefreshChatListData(mAdapter);
            }

           /* SortByChatDate();
            mAdapter.notifyDataSetChanged();*/
        }
        else
        {
            fragView = inflater.inflate(R.layout.fragment_chat_list,container,false);
            fragView.setTag("ChatListFragment");

            txt_empty = fragView.findViewById(R.id.txt_empty);
            if(mMyData.arrChatDataList.size() == 0)
            {
                txt_empty.setVisibility(View.VISIBLE);
            }
            else
                txt_empty.setVisibility(View.GONE);

            chatListRecyclerView = fragView.findViewById(R.id.chat_list_recy);

            chatListRecyclerView.setAdapter(mAdapter);
            chatListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            SortByChatDate();
            mAdapter.notifyDataSetChanged();
        }
        return fragView;
    }

    private void SortByChatDate()
    {
        Map<String, SimpleChatData> tempDataMap = new LinkedHashMap<String, SimpleChatData>(mMyData.arrChatDataList);
        //tempDataMap = mMyData.arrMyFanDataList;
        Iterator it = sortByValue(tempDataMap).iterator();
        mMyData.arrChatDataList.clear();
        mMyData.arrChatNameList.clear();
        while(it.hasNext()) {
            String temp = (String) it.next();
            System.out.println(temp + " = " + mMyData.arrChatDataList.get(temp));
            mMyData.arrChatDataList.put(temp, tempDataMap.get(temp));
            mMyData.arrChatNameList.add(tempDataMap.get(temp).ChatRoomName);

        }
    }

    public static List sortByValue(final Map map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());
        Collections.sort(list,new Comparator() {

            public int compare(Object o1,Object o2) {
                SimpleChatData g1 = (SimpleChatData)map.get(o1);
                SimpleChatData g2 = (SimpleChatData)map.get(o2);

                Object v1 = g1.Date;
                Object v2 = g2.Date;
                return ((Comparable) v2).compareTo(v1);
            }
        });
        // Collections.reverse(list); // 주석시 오름차순
        return list;
    }

    public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {
        @Override
        public ChatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.content_chat_list,parent,false);

            //view.setLayoutParams(new_img RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mUIData.getHeight()/7));
            return new ChatListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatListViewHolder holder, final int position) {
            int i = position;

            final String str = mMyData.arrChatNameList.get(i);


            Glide.with(mContext)
                    //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                    .load(mMyData.arrChatDataList.get(str).Img)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(holder.imageView);

            holder.imageItem.setVisibility(View.VISIBLE);

            if(mMyData.arrChatDataList.get(str).BestItem == 0)
            {
                holder.imageItem.setVisibility(View.GONE);
            }
            else
             holder.imageItem.setImageResource(mUIData.getJewels()[mMyData.arrChatDataList.get(str).BestItem]);

            holder.imageGrade.setImageResource(mUIData.getGrades()[mMyData.arrChatDataList.get(str).Grade]);

            holder.linearLayout.setLongClickable(true);

            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_exit_app, null, false);


                    final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(v).create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    final Button btn_exit;
                    final Button btn_no;
                    final TextView tv_title;
                    final TextView tv_msg;

                    tv_title = v.findViewById(R.id.title);
                    tv_msg = v.findViewById(R.id.msg);

                    tv_title.setText("채팅방 삭제");
                    tv_msg.setText("삭제를 하면 대화 내용 및 채팅방 정보가" + "\n" + "모두 삭제됩니다.");

                    btn_exit = (Button) v.findViewById(R.id.btn_yes);
                    btn_exit.setText("삭제");
                    btn_exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference table;
                            table = database.getReference("User/" + mMyData.getUserIdx()+ "/SendList/");
                            table.child(mMyData.arrChatDataList.get(str).ChatRoomName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().removeValue();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            mMyData.arrChatDataList.remove( mMyData.arrChatNameList.get(position));
                            mMyData.arrChatNameList.remove(position);


                         //   mMyData.arrChatTargetData.remove(position);
                         //   mMyData.arrMyChatTargetList.remove(position);

                            refreshFragMent();
                            dialog.dismiss();
                        }
                    });

                    btn_no = (Button) v.findViewById(R.id.btn_no);
                    btn_no.setText("취소");
                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    return false;
                }
            });


/*            String strDate = mMyData.arrSendDataList.get(i).strSendDate;
            holder.date.setText(strDate);*/

            //holder.textView.setText(mMyData.arrSendDataList.get(i).strTargetNick + "님과의 채팅방입니다");

            long time = CommonFunc.getInstance().GetCurrentTime();
            Date writeDate = new Date(mMyData.arrChatDataList.get(str).Date);
            Date todayDate = new Date(time);

            if(CommonFunc.getInstance().IsTodayDate(todayDate, writeDate))
            {
                SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_TODAY_DATE_FORMAT);
                holder.date.setText(ctime.format(writeDate));
            }
            else
            {
                SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_DATE_FORMAT);
                holder.date.setText(ctime.format(writeDate));
            }


            holder.nickname.setText(mMyData.arrChatDataList.get(str).Nick + " (" + mMyData.arrChatDataList.get(str).Age + "세)");// + ", " + mMyData.arrCardNameList.get(i).Age + "세");

            if(mMyData.arrChatDataList.get(str).Gender == null || mMyData.arrChatDataList.get(str).Gender.equals(""))
            {
                SortByChatDate();
                mAdapter.notifyDataSetChanged();
            }

            if(mMyData.arrChatDataList.get(str).Gender.equals("여자"))
                holder.nickname.setTextColor(TEXTCOLOR_WOMAN);
            else
                holder.nickname.setTextColor(TEXTCOLOR_MAN);

            if(mMyData.arrChatDataList.get(str).Check == 0)
            {
                if(!mMyData.arrChatDataList.get(str).WriterIdx.equals(mMyData.getUserIdx()))
                    holder.check.setVisibility(View.VISIBLE);
                else
                    holder.check.setVisibility(View.INVISIBLE);
            }

            else
                holder.check.setVisibility(View.INVISIBLE);

            if(mMyData.arrChatDataList.get(str).Msg.equals(""))
                holder.textView.setText(mMyData.arrChatDataList.get(str).Nick + "님과의 채팅방입니다");
            else
                holder.textView.setText(mMyData.arrChatDataList.get(str).Msg);


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(CommonFunc.getInstance().getClickStatus() == false)
                    {
                        getChatTargetData(position);
                    }

                    //finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMyData.arrChatDataList.size();
            //return mMyData.arrChatTargetData.size();
        }

        String strTargetIdx;

        public void moveChatPage(UserData user, int pos)
        {
            Intent intent = new Intent(getContext(),ChatRoomActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("Target", user);
            bundle.putSerializable("Position", pos);
            intent.putExtras(bundle);
            startActivity(intent);

            CommonFunc.getInstance().DismissLoadingPage();
            CommonFunc.getInstance().setClickStatus(false);
        }


        public void getChatTargetData(final int position) {

            CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
            CommonFunc.getInstance().setClickStatus(true);
            
            String str = mMyData.arrChatNameList.get(position);

            String[] strIdx = mMyData.arrChatDataList.get(str).ChatRoomName.split("_");

            if(strIdx[0].equals(mMyData.getUserIdx()))
            {
                strTargetIdx = strIdx[1];
            }
            else
            {
                strTargetIdx = strIdx[0];
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference table = null;
            table = database.getReference("User");

            table.child(strTargetIdx).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int saa = 0;
                    final UserData tempUserData = dataSnapshot.getValue(UserData.class);
                    if(tempUserData != null)
                    {
                        mMyData.mapChatTargetData.put(strTargetIdx, tempUserData);

                      /*  for (LinkedHashMap.Entry<String, SimpleUserData> entry : tempUserData.StarList.entrySet()) {
                            mMyData.mapChatTargetData.get(strTargetIdx).arrStarList.add(entry.getValue());
                        }*/

                        for (LinkedHashMap.Entry<String, FanData> entry : tempUserData.FanList.entrySet()) {
                            mMyData.mapChatTargetData.get(strTargetIdx).arrFanList.add(entry.getValue());
                        }

                        if(mMyData.mapChatTargetData.get(strTargetIdx).arrFanList.size() == 0)
                        {
                            //RefreshUserChatSimpleData(tempUserData, position);
                            moveChatPage(mMyData.mapChatTargetData.get(strTargetIdx), position);
                            notifyDataSetChanged();
                        }

                        else
                        {
                            CommonFunc.getInstance().SortByRecvHeart(mMyData.mapChatTargetData.get(strTargetIdx));

                            for(int i = 0 ;i < mMyData.mapChatTargetData.get(strTargetIdx).arrFanList.size(); i++)
                            {
                                Query data = FirebaseDatabase.getInstance().getReference().child("SimpleData").child(mMyData.mapChatTargetData.get(strTargetIdx).arrFanList.get(i).Idx);
                                final FanData finalTempFanData = mMyData.mapChatTargetData.get(strTargetIdx).arrFanList.get(i);
                                final int finalI = i;
                                data.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        SimpleUserData DBData = dataSnapshot.getValue(SimpleUserData.class);
                                        if(DBData != null)
                                        {
                                            mMyData.mapChatTargetData.get(strTargetIdx).arrFanData.put(finalTempFanData.Idx, DBData);

                                            if( finalI == mMyData.mapChatTargetData.get(strTargetIdx).arrFanList.size() -1)
                                            {
                                               // RefreshUserChatSimpleData(tempUserData, position);
                                                moveChatPage(mMyData.mapChatTargetData.get(strTargetIdx), position);
                                                notifyDataSetChanged();
                                            }
                                        }
                                        else
                                        {
                                            CommonFunc.getInstance().ShowToast(mContext, "사용자가 없습니다.", false);
                                            CommonFunc.getInstance().setClickStatus(false);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }


                    }

                    else
                        CommonFunc.getInstance().setClickStatus(false);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }

    }

    public void RefreshUserChatSimpleData(UserData stTargetData, int position) {

        String  str = mMyData.arrChatNameList.get(position);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("User");//.child(mMyData.getUserIdx());
        // DatabaseReference user = table.child( userIdx);
        final DatabaseReference user = table.child(mMyData.getUserIdx()).child("SendList").child(str);

        user.child("Nick").setValue(stTargetData.NickName);
        user.child("Img").setValue(stTargetData.Img);
        user.child("Check").setValue(1);

        mMyData.arrChatDataList.get(str).Img = stTargetData.Img;
        mMyData.arrChatDataList.get(str).Nick= stTargetData.NickName;
        mMyData.arrChatDataList.get(str).Check= 1;

    }

}
