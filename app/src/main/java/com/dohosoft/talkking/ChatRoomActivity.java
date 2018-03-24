package com.dohosoft.talkking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.dohosoft.talkking.Data.ChatData;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SimpleChatData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.NotiFunc;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.dohosoft.talkking.MainActivity.mFragmentMng;
import static com.dohosoft.talkking.MyProfileActivity.calculateInSampleSize;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatRoomActivity extends AppCompatActivity {
    private NotiFunc mNotiFunc = NotiFunc.getInstance();
    private MyData mMyData = MyData.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    private UserData stTargetData = new UserData();

    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://talkking-25dd8.appspot.com/");

    private static final int REQUEST_IMAGE = 1001;
    Button btn_send;
    ImageButton btn_plus;
    EditText txt_msg;

    Context context=this;
    DatabaseReference mRef;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<ChatData, ChatViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;
    SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm");

    private ConstraintLayout GiftLayout;

    int     tempPosition;
    SimpleChatData tempChatData = new SimpleChatData();
    String  tempChatRoomName;

    private ProgressBar progressBar;
    private android.app.FragmentManager mFragmentManager;
    static  Uri tempSaveUri;
    static int a = 0;
    private Activity mActivity;
    final String[] postId = {null};
    final boolean[] bPrePare = {false};

    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layout;
        LinearLayout Msg_layout;
        LinearLayout Msg_detail_layout;

        ImageView image_profile, send_Img1;
        ImageView send_Img2;
        TextView message1;
        TextView message2;
        TextView from;

        TextView nickname1;
        TextView to;
        TextView nickname2;
        TextView giftMsg;
        TextView heartCount;
        ImageView heart;

        TextView check;
        TextView date1, date2;

        TextView targetName;

        //TextView send_new,recv_new;

        ImageView Sender_image_profile,Sender_image_sent;
        TextView Sender_message;

        TextView Sender_sender;
        TextView Sender_time;



        ImageView gift_img;
        TextView  gift_from;
        TextView  gift_from_Nickname;
        TextView  gift_to;
        TextView  gift_to_Nickname;
        TextView  gift_Msg;
        ImageView gift_Heart_img;
        TextView  gift_Heart_Cnt;

        ImageView gift_img2;
        TextView  gift_from2;
        TextView  gift_from_Nickname2;
        TextView  gift_to2;
        TextView  gift_to_Nickname2;
        TextView  gift_Msg2;
        ImageView gift_Heart_img2;
        TextView  gift_Heart_Cnt2;
        TextView gift_check2;
        TextView gift_date2;

       // TextView gift_check;
        TextView gift_date;

        TextView send_Img1_date;
        TextView send_Img2_date;
        TextView send_img2_check;

        //회색글자 처리 뜸 원인불명
        public ChatViewHolder(View itemView) {
            super(itemView);
            image_profile = (ImageView)itemView.findViewById(R.id.ChatRoom_Img);
            // image_sent = (ImageView)itemView.findViewById(R.id.iv_sent);
            targetName = (TextView)itemView.findViewById(R.id.ChatRoom_name1);
            message1 =(TextView)itemView.findViewById(R.id.message1);
            message2 = itemView.findViewById(R.id.message2);
            //layout = (LinearLayout)itemView.findViewById(R.id.ChatRoom_layout);
            Msg_layout= (LinearLayout)itemView.findViewById(R.id.ChatRoom_msg_layout);
            //Msg_detail_layout= (LinearLayout)itemView.findViewById(R.id.ChatRoom_msg_detail_layout);
            send_Img1 = (ImageView)itemView.findViewById(R.id.img1);
            send_Img1_date = (TextView)itemView.findViewById(R.id.img_date1);

            from = itemView.findViewById(R.id.from);
            //nickname1 = itemView.findViewById(R.id.from_nickname);
            to = itemView.findViewById(R.id.to);
            nickname2 = itemView.findViewById(R.id.to_nickname);
            giftMsg = itemView.findViewById(R.id.giftmessage);
            heartCount = itemView.findViewById(R.id.heart_count);
            heart = itemView.findViewById(R.id.heart);

            send_Img2 = (ImageView)itemView.findViewById(R.id.img2);
            send_Img2_date = (TextView)itemView.findViewById(R.id.img_date2);
            send_img2_check = (TextView)itemView.findViewById(R.id.img_check);

            check = (TextView)itemView.findViewById(R.id.txt_check);

            date1 = (TextView)itemView.findViewById(R.id.txt_date1);
            date2 = (TextView)itemView.findViewById(R.id.txt_date2);


            gift_img = (ImageView)itemView.findViewById(R.id.bg_gift);
            //gift_from = (TextView)itemView.findViewById(R.id.from);
            //gift_from_Nickname = (TextView)itemView.findViewById(R.id.from_nickname);
            gift_to = (TextView)itemView.findViewById(R.id.to);
            gift_to_Nickname = (TextView)itemView.findViewById(R.id.to_nickname);
            gift_Msg = (TextView) itemView.findViewById(R.id.giftmessage);
            gift_Heart_img = (ImageView)itemView.findViewById(R.id.heart);
            gift_Heart_Cnt = (TextView)itemView.findViewById(R.id.heart_count);
            gift_date = (TextView)itemView.findViewById(R.id.gift_date);

            gift_img2 = (ImageView)itemView.findViewById(R.id.bg_gift2);
            gift_to_Nickname2 = (TextView)itemView.findViewById(R.id.to_nickname2);
            gift_to2= (TextView)itemView.findViewById(R.id.to2);
            gift_Msg2 = (TextView) itemView.findViewById(R.id.giftmessage2);
            gift_Heart_img2 = (ImageView)itemView.findViewById(R.id.heart2);
            gift_Heart_Cnt2 = (TextView)itemView.findViewById(R.id.heart_count2);
            gift_date2 = (TextView)itemView.findViewById(R.id.gift_date2);
            gift_check2= (TextView)itemView.findViewById(R.id.gift_check2);
            //send_new = (TextView)itemView.findViewById(R.id.Send_new);
            //recv_new  = (TextView)itemView.findViewById(R.id.Recv_new);
            //  Sender_image_profile = (ImageView)itemView.findViewById(R.id.Sender_Img);

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mMyData.SetCurFrag(2);

        ChatListFragment frg = null;
        if(mFragmentMng != null)
        {
            frg = (ChatListFragment)mFragmentMng.findFragmentByTag("ChatListFragment");
            if(frg != null) {
                frg.refresh();
            }
        }



        //  mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_CHAT);
     /*   if(tempPosition == -1)
        {
            mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_CHAT);
        }
        else
        {
            ChatListFragment frg = null;
            frg = (ChatListFragment)mFragmentMng.findFragmentByTag("ChatListFragment");
            frg.refresh();
        }*/



    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first


        if(CommonFunc.getInstance().mAppStatus == CommonFunc.AppStatus.RETURNED_TO_FOREGROUND) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("Badge", getApplicationContext().MODE_PRIVATE);
            pref.getInt("Badge", mMyData.badgecount );

            if (mMyData.badgecount >= 1) {
                mMyData.badgecount = 0;
                Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                intent.putExtra("badge_count_package_name", "com.hodo.talkking");
                intent.putExtra("badge_count_class_name", "com.hodo.talkking.LoginActivity");
                intent.putExtra("badge_count", mMyData.badgecount);
                sendBroadcast(intent);
            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);


        mMyData.SetCurFrag(5);

        mActivity = this;

        //  progressBar = (ProgressBar)findViewById(R.id.chat_Progress);
        mFragmentManager = getFragmentManager();

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        stTargetData = (UserData) bundle.getSerializable("Target");
        tempPosition = (int)bundle.getSerializable("Position");
        if(tempPosition == -1)
        {
            tempChatData.ChatRoomName = tempChatRoomName = (String)bundle.getSerializable("RoomName");
            tempChatData.Msg = "";
            tempChatData.Nick = stTargetData.NickName;
            tempChatData.Idx = stTargetData.Idx;
            tempChatData.Img = stTargetData.Img;
            tempChatData.Date = 0;
            tempChatData.Check = 0;
        }
        else
            tempChatData = mMyData.arrChatDataList.get(mMyData.arrChatNameList.get(tempPosition));

        // GiftLayout = (ConstraintLayout)findViewById(R.id.ChatGiftLayout);
        //stTargetData.NickName = tempChatData.strTargetNick;
        //stTargetData.Img= tempChatData.strTargetImg;

        //stTargetData.NickName = mMyData.arrChatTargetData.get(tempChatIdx).NickName;
        //stTargetData.Img= mMyData.arrChatTargetData.get(tempChatIdx).Img;

        mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(tempChatData.ChatRoomName);
        setTitle(stTargetData.NickName+"님과의 채팅방");

        boolean btnSendEnable = true;
        txt_msg = (EditText)findViewById(R.id.et_nick);

        /*final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ConstraintLayout ll = (ConstraintLayout)findViewById(R.id.relativeLayout3);
        ll.setOnClickListener(new_img View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imm.hideSoftInputFromWindow(txt_msg.getWindowToken(), 0);
            }
        });
        */


/*        ChatData chat_Data = new_img ChatData(mMyData.getUserNick(), tempChatData.Nick, tempChatData.Msg, tempChatData.Date, "");
        mRef.push().setValue(chat_Data);*/



        btn_plus = (ImageButton)findViewById(R.id.btn_plus);

        recyclerView = (RecyclerView) findViewById(R.id.chat_list);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatData, ChatViewHolder>(
                ChatData.class,
                R.layout.content_chat_data_constraint,
                ChatViewHolder.class,
                mRef){


            @Override
            protected ChatData parseSnapshot(DataSnapshot snapshot) {

                ChatData chat_message = super.parseSnapshot(snapshot);
                if(chat_message != null){
                    chat_message.setId(snapshot.getKey());
                    if(chat_message.fromIdx.equals(tempChatData.Idx))
                    {
                        chat_message.Check = 1;
                        mRef.child(chat_message.strId).child("Check").setValue(chat_message.Check);
                    }

                }
                return chat_message;
            }

            int a= 0;

            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, final ChatData chat_message, int position) {


                viewHolder.image_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CommonFunc.getInstance().ShowLoadingPage(mActivity, "로딩중");
                        CommonFunc.getInstance().setClickStatus(true);

                        Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("Target", stTargetData);
                        intent.putExtras(bundle);

                        view.getContext().startActivity(intent);

                        CommonFunc.getInstance().DismissLoadingPage();
                        CommonFunc.getInstance().setClickStatus(false);

                    }
                });



                // Log.d("!@#$%", chat_message.getMsg() + "    " + position +"     " + chat_message.from);

                if(chat_message.fromIdx.equals(mMyData.getUserIdx()))
                {
                    //  Log.d("!@#$%", "11111");

                    if (chat_message.Check == 0)
                    {
                        viewHolder.send_img2_check.setVisibility(View.VISIBLE);
                        viewHolder.check.setVisibility(View.VISIBLE);
                        viewHolder.gift_check2.setVisibility(View.VISIBLE);
                    }

                    else
                    {
                        viewHolder.check.setVisibility(View.GONE);
                        viewHolder.send_img2_check.setVisibility(View.GONE);
                        viewHolder.gift_check2.setVisibility(View.GONE);
                    }

                    viewHolder.message1.setVisibility(TextView.GONE);
                    viewHolder.targetName.setVisibility(TextView.GONE);
                    viewHolder.image_profile.setVisibility(View.GONE);
                    viewHolder.send_Img1.setVisibility(TextView.GONE);
                    viewHolder.date1.setVisibility(TextView.GONE);
                    viewHolder.date2.setVisibility(TextView.VISIBLE);

                    viewHolder.gift_img.setVisibility(ImageView.GONE);
                    viewHolder.gift_to.setVisibility(TextView.GONE);
                    viewHolder.gift_to_Nickname.setVisibility(TextView.GONE);
                    viewHolder.gift_Msg.setVisibility(TextView.GONE);
                    viewHolder.gift_Heart_img.setVisibility(ImageView.GONE);
                    viewHolder.gift_Heart_Cnt.setVisibility(TextView.GONE);
                    viewHolder.gift_date.setVisibility(TextView.GONE);


                    long time = CommonFunc.getInstance().GetCurrentTime();
                    Date todayDate = new Date(time);
                    Date writeDate = new Date(chat_message.time);
                    if(CommonFunc.getInstance().IsTodayDate(todayDate, writeDate))
                    {
                        SimpleDateFormat chatTime_1 = new SimpleDateFormat(CoomonValueData.DATE_MONTH_DAY);
                        SimpleDateFormat chatTime_2 = new SimpleDateFormat(CoomonValueData.DATE_HOURS_MIN);
                        String DateTime = "<font color=" + CoomonValueData.CHAT_DATE_TIME_COLOR_MONTH_DAY + ">"+ chatTime_1.format(writeDate) + "<br>" +
                                "</font><font color=" + CoomonValueData.CHAT_DATE_TIME_COLOR_HOURS_MIN + ">" + chatTime_2.format(writeDate) + "</font>";

                        viewHolder.date2.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.send_Img2_date.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.send_Img2_date.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.gift_date2.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                    }
                    else
                    {
                        SimpleDateFormat chatTime = new SimpleDateFormat(CoomonValueData.DATE_HOURS_MIN);
                        String DateTime = "<font color=" + CoomonValueData.CHAT_DATE_TIME_COLOR_HOURS_MIN + ">" + chatTime.format(writeDate) + "</font>";

                        viewHolder.date2.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.send_Img2_date.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.send_Img2_date.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.gift_date2.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                    }

                    if(chat_message.Heart == 0)
                    {
                        viewHolder.gift_img2.setVisibility(ImageView.GONE);
                        viewHolder.gift_to2.setVisibility(TextView.GONE);
                        viewHolder.gift_to_Nickname2.setVisibility(TextView.GONE);
                        viewHolder.gift_Msg2.setVisibility(TextView.GONE);
                        viewHolder.gift_Heart_img2.setVisibility(ImageView.GONE);
                        viewHolder.gift_Heart_Cnt2.setVisibility(TextView.GONE);
                        viewHolder.gift_date2.setVisibility(TextView.GONE);
                        viewHolder.gift_check2.setVisibility(TextView.GONE);


                        viewHolder.send_Img2.setVisibility(View.GONE);
                        viewHolder.send_Img2_date.setVisibility(TextView.GONE);

                        viewHolder.send_Img1_date.setVisibility(View.GONE);

                        viewHolder.date2.setVisibility(TextView.GONE);
                        viewHolder.message2.setVisibility(View.GONE);



                        if( !chat_message.getMsg().equals("")){
                            viewHolder.send_img2_check.setVisibility(View.GONE);
                            viewHolder.date2.setVisibility(TextView.VISIBLE);
                            viewHolder.message2.setVisibility(View.VISIBLE);
                            viewHolder.message2.setText(chat_message.getMsg());
                        }
                        else if( !chat_message.getImg().equals("")){
                            viewHolder.check.setVisibility(View.GONE);
                            viewHolder.send_Img2.setVisibility(View.VISIBLE);
                            viewHolder.send_Img2_date.setVisibility(TextView.VISIBLE);

                            Glide.with(getApplicationContext())
                                    .load(chat_message.getImg())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(viewHolder.send_Img2);

                            viewHolder.send_Img2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getApplicationContext(), ImageViewPager.class);
                                    Bundle bundle = new Bundle();
                                    UserData tempUser = new UserData();
                                    tempUser.ImgCount = 1;
                                    tempUser.Img= tempUser.ImgGroup0 = chat_message.getImg().toString();
                                    tempUser.Img= tempUser.ImgGroup1 = "1";
                                    tempUser.Img= tempUser.ImgGroup2 = "1";
                                    tempUser.Img= tempUser.ImgGroup3 = "1";

                                    bundle.putSerializable("Target", tempUser);
                                    bundle.putSerializable("Index", 0);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                    else
                    {
                        viewHolder.date2.setVisibility(TextView.GONE);
                        viewHolder.check.setVisibility(TextView.GONE);
                        viewHolder.message2.setVisibility(View.GONE);

                        viewHolder.send_Img2.setVisibility(View.GONE);
                        viewHolder.send_Img1_date.setVisibility(TextView.GONE);

                        viewHolder.send_Img2_date.setVisibility(TextView.GONE);
                        viewHolder.send_img2_check.setVisibility(TextView.GONE);

                        viewHolder.gift_img2.setVisibility(ImageView.VISIBLE);
                        //viewHolder.gift_from.setVisibility(TextView.VISIBLE);
                        //viewHolder.gift_from_Nickname.setVisibility(TextView.VISIBLE);
                        //viewHolder.gift_from_Nickname.setText(mMyData.getUserNick());
                        viewHolder.gift_to2.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_to_Nickname2.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_to_Nickname2.setText(stTargetData.NickName);

                        if(chat_message.msg == null || chat_message.msg.equals(""))
                        {
                            viewHolder.gift_Msg2.setVisibility(TextView.GONE);
                        }
                        else
                        {
                            viewHolder.gift_Msg2.setVisibility(TextView.VISIBLE);
                            viewHolder.gift_Msg2.setText(chat_message.msg);
                        }
                        viewHolder.gift_Heart_img2.setVisibility(ImageView.VISIBLE);
                        viewHolder.gift_Heart_Cnt2.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_Heart_Cnt2.setText(Integer.toString(chat_message.Heart));
                        viewHolder.gift_date2.setVisibility(View.VISIBLE);
                    }




                    a = 0;

                }
                else
                {
                    //  Log.d("!@#$%", "22222");

                    viewHolder.message2.setVisibility(View.GONE);
                    viewHolder.send_Img2.setVisibility(View.GONE);

                    viewHolder.targetName.setVisibility(TextView.VISIBLE);
                    viewHolder.targetName.setText(stTargetData.NickName);

                    viewHolder.date2.setVisibility(TextView.GONE);
                    viewHolder.send_Img2_date.setVisibility(TextView.GONE);
                    viewHolder.send_img2_check.setVisibility(TextView.GONE);
                    viewHolder.date1.setVisibility(TextView.GONE);
                    viewHolder.check.setVisibility(TextView.GONE);

                    viewHolder.gift_img2.setVisibility(ImageView.GONE);
                    viewHolder.gift_to2.setVisibility(TextView.GONE);
                    viewHolder.gift_to_Nickname2.setVisibility(TextView.GONE);
                    viewHolder.gift_Msg2.setVisibility(TextView.GONE);
                    viewHolder.gift_Heart_img2.setVisibility(ImageView.GONE);
                    viewHolder.gift_Heart_Cnt2.setVisibility(TextView.GONE);
                    viewHolder.gift_date2.setVisibility(TextView.GONE);
                    viewHolder.gift_check2.setVisibility(TextView.GONE);


                    long time = CommonFunc.getInstance().GetCurrentTime();
                    Date writeDate = new Date(chat_message.time);
                    Date todayDate = new Date(time);

                    if(CommonFunc.getInstance().IsTodayDate(todayDate, writeDate))
                    {
                        SimpleDateFormat chatTime_1 = new SimpleDateFormat(CoomonValueData.DATE_MONTH_DAY);
                        SimpleDateFormat chatTime_2 = new SimpleDateFormat(CoomonValueData.DATE_HOURS_MIN);
                        String DateTime = "<font color=" + CoomonValueData.CHAT_DATE_TIME_COLOR_MONTH_DAY + ">"+ chatTime_1.format(writeDate) + "<br>" +
                                "</font><font color=" + CoomonValueData.CHAT_DATE_TIME_COLOR_HOURS_MIN + ">" + chatTime_2.format(writeDate) + "</font>";

                        viewHolder.date1.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.gift_date.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.send_Img1_date.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                    }
                    else
                    {
                        SimpleDateFormat chatTime = new SimpleDateFormat(CoomonValueData.DATE_HOURS_MIN);
                        String DateTime = "<font color=" + CoomonValueData.CHAT_DATE_TIME_COLOR_HOURS_MIN + ">" + chatTime.format(writeDate) + "</font>";

                        viewHolder.date1.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.gift_date.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                        viewHolder.send_Img1_date.setText(Html.fromHtml(DateTime), TextView.BufferType.SPANNABLE);
                    }










                    viewHolder.image_profile.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext())
                            .load( stTargetData.Img)
                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.1f)
                            .into(viewHolder.image_profile);

                    if (chat_message.Check == 0)
                    {
                        viewHolder.check.setVisibility(View.VISIBLE);
                    }

                    else
                    {
                        viewHolder.check.setVisibility(View.GONE);
                    }

                    if(chat_message.Heart == 0)
                    {
                        viewHolder.gift_img.setVisibility(ImageView.GONE);
                        //viewHolder.gift_from.setVisibility(TextView.GONE);
                        //viewHolder.gift_from_Nickname.setVisibility(TextView.GONE);
                        viewHolder.gift_to.setVisibility(TextView.GONE);
                        viewHolder.gift_to_Nickname.setVisibility(TextView.GONE);
                        viewHolder.gift_Msg.setVisibility(TextView.GONE);
                        viewHolder.gift_Heart_img.setVisibility(ImageView.GONE);
                        viewHolder.gift_Heart_Cnt.setVisibility(TextView.GONE);

                        viewHolder.gift_date.setVisibility(TextView.GONE);


                        viewHolder.gift_img2.setVisibility(ImageView.GONE);
                        viewHolder.gift_to2.setVisibility(TextView.GONE);
                        viewHolder.gift_to_Nickname2.setVisibility(TextView.GONE);
                        viewHolder.gift_Msg2.setVisibility(TextView.GONE);
                        viewHolder.gift_Heart_img2.setVisibility(ImageView.GONE);
                        viewHolder.gift_Heart_Cnt2.setVisibility(TextView.GONE);
                        viewHolder.gift_date2.setVisibility(TextView.GONE);
                        viewHolder.gift_check2.setVisibility(TextView.GONE);


                        if( !chat_message.getMsg().equals("")){
                            viewHolder.date1.setVisibility(TextView.VISIBLE);
                            viewHolder.message1.setVisibility(View.VISIBLE);
                            viewHolder.message1.setText(chat_message.getMsg());

                            viewHolder.send_Img1.setVisibility(TextView.GONE);
                            viewHolder.send_Img1_date.setVisibility(TextView.GONE);
                        }
                        else if( !chat_message.getImg().equals("")){

                            viewHolder.message1.setVisibility(View.GONE);
                            viewHolder.send_Img1.setVisibility(View.VISIBLE);
                            viewHolder.send_Img1_date.setVisibility(View.VISIBLE);
                            viewHolder.date1.setVisibility(View.GONE);

                            Glide.with(getApplicationContext())
                                    .load(chat_message.getImg())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(viewHolder.send_Img1);
                        }

                        viewHolder.send_Img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getApplicationContext(), ImageViewPager.class);
                                Bundle bundle = new Bundle();
                                UserData tempUser = new UserData();
                                tempUser.ImgCount = 1;
                                tempUser.Img= tempUser.ImgGroup0 = chat_message.getImg().toString();
                                tempUser.Img= tempUser.ImgGroup1 = "1";
                                tempUser.Img= tempUser.ImgGroup2 = "1";
                                tempUser.Img= tempUser.ImgGroup3 = "1";

                                bundle.putSerializable("Target", tempUser);
                                bundle.putSerializable("Index", 0);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });


                    }
                    else
                    {

                        viewHolder.date1.setVisibility(TextView.GONE);
                        viewHolder.send_Img1.setVisibility(TextView.GONE);
                        viewHolder.send_Img1_date.setVisibility(TextView.GONE);

                        viewHolder.message1.setVisibility(View.GONE);

                        viewHolder.gift_img.setVisibility(ImageView.VISIBLE);
                        viewHolder.gift_to.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_to_Nickname.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_to_Nickname.setText(mMyData.getUserNick());

                        if(chat_message.msg == null || chat_message.msg.equals(""))
                        {
                            viewHolder.gift_Msg.setVisibility(TextView.GONE);
                        }
                        else
                        {
                            viewHolder.gift_Msg.setVisibility(TextView.VISIBLE);
                            viewHolder.gift_Msg.setText(chat_message.msg);
                        }


                        viewHolder.gift_Heart_img.setVisibility(ImageView.VISIBLE);
                        viewHolder.gift_Heart_Cnt.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_Heart_Cnt.setText(Integer.toString(chat_message.Heart));
                        viewHolder.gift_date.setVisibility(View.VISIBLE);
                    }






                }
            }
        };
        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message1.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });


        recyclerView.setAdapter(firebaseRecyclerAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                //View framelayout = findViewById(R.id.framelayout_chatroom);
                View popup= inflater.inflate(R.layout.popup_chatroom,null);

                builder.setView(popup);
                final AlertDialog dialog_gal_gift = builder.create();
                dialog_gal_gift.show();
                ImageButton btn_gal = popup.findViewById(R.id.btn_gal);
                btn_gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_gal_gift.dismiss();

                        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(ChatRoomActivity.this)
                                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                    @Override
                                    public void onImageSelected(Uri uri) {
                                        // uri 활용
                                        tempSaveUri = uri;
                                        UploadImage_Firebase(tempSaveUri);
                                    }
                                })
                                .create();

                        bottomSheetDialogFragment.show(getSupportFragmentManager());

                     /*   Intent intent = new_img Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image*//*");
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_IMAGE);*/

                    }
                });

                /*Button btn_cam = popup.findViewById(R.id.btn_camera);
                btn_cam.setVisibility(View.GONE);
                btn_cam.setOnClickListener(new_img View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/

                ImageButton btn_gift = popup.findViewById(R.id.btn_gift);
                btn_gift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_gal_gift.dismiss();

                        CommonFunc.HeartGiftPopup_Change_End changeListener = new CommonFunc.HeartGiftPopup_Change_End()
                        {
                            @Override
                            public void EndListener() {
                                startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
                            }
                        };
                        CommonFunc.HeartGiftPopup_Send_End sendListener = new CommonFunc.HeartGiftPopup_Send_End()
                        {
                            @Override
                            public void EndListener(int heartCount, String msg) {

                                mMyData.makeSendHoneyList(stTargetData, heartCount, msg);
                                mMyData.makeRecvHoneyList(stTargetData, heartCount, msg);
                                mMyData.makeCardList(stTargetData);
                                mMyData.setUserHoney(mMyData.getUserHoney() - heartCount);

                                mMyData.setSendHoneyCnt(heartCount);
                                mMyData.makeFanList(ChatRoomActivity.this, stTargetData, heartCount, msg);

                                long nowTime = CommonFunc.getInstance().GetCurrentTime();

                                ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(),  stTargetData.NickName, msg, nowTime, "", 0, heartCount);

                                if(msg.equals(""))
                                    msg = mMyData.getUserNick() + "님이 " + heartCount + " 하트를 보냈습니다";
                                mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, msg, nowTime, heartCount);
                                mRef.push().setValue(chat_Data);
                            }
                        };
                        CommonFunc.getInstance().HeartGiftPopup(ChatRoomActivity.this, stTargetData.Idx, changeListener, sendListener);
                    }
                });


            }
        });
        btn_send = (Button)findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonFunc.getInstance().IsStringEmptyCheck(txt_msg.getText().toString()))
                {
                    CommonFunc.getInstance().ShowToast(mActivity ,"채팅에 내용이 없습니다.",false);
                    return;
                }
                else if(txt_msg.getText().toString().length() > CoomonValueData.TEXT_MAX_LENGTH_CHAT)
                {
                    CommonFunc.getInstance().ShowToast(mActivity ,"채팅에 내용이 너무 많습니다.",false);
                    return;
                }

                if(CommonFunc.getInstance().ShowBlockUser(mActivity, stTargetData.Idx) == false)
                {
                    String message = txt_msg.getText().toString();
                    int nLength = message.length();
                    long nowTime = CommonFunc.getInstance().GetCurrentTime();
                    if (txt_msg.getText().toString().replace(" ", "").equals("")) {
                        return;
                    }else{
                        ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), tempChatData.Nick, message, nowTime, "",0, 0);

                        mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, message, nowTime, 0);

                        mRef.push().setValue(chat_Data);
                        txt_msg.setText("");

                        mNotiFunc.SendChatToFCM(stTargetData, message);
                    }
                }
                else
                {
                    onBackPressed();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference table;
                    table = database.getReference("User/" + mMyData.getUserIdx()+ "/SendList/").child(tempChatData.ChatRoomName);
                    table.removeValue();

                    if(tempPosition == -1)
                    {
                        int RoomPos = 0;
                        for(int i =0; i <mMyData.arrChatNameList.size(); i++)
                        {
                            if(mMyData.arrChatNameList.get(i).contains(tempChatData.ChatRoomName))
                            {
                                RoomPos = i;
                                break;
                            }
                        }
                        mMyData.arrChatDataList.remove(mMyData.arrChatNameList.get(RoomPos));
                        mMyData.arrChatNameList.remove(RoomPos);
                    }
                    else
                    {
                        mMyData.arrChatDataList.remove(mMyData.arrChatNameList.get(tempPosition));
                        mMyData.arrChatNameList.remove(tempPosition);
                    }
                }
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_room,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){



            case R.id.btn_block:
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                final View BlockView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_block, null, false);
                final AlertDialog dialog1 = builder.setView(BlockView).create();
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog1.show();
                TextView textTitle = (TextView) BlockView.findViewById(R.id.textView4);

                textTitle.setText(stTargetData.NickName +  "님 차단하기");

                Button btn_ok = (Button) BlockView.findViewById(R.id.btn_ok);
                Button btn_no = (Button) BlockView.findViewById(R.id.btn_no);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference table;
                        table = database.getReference("User/" + mMyData.getUserIdx()+ "/SendList/");
                        table.child(tempChatData.ChatRoomName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().removeValue();

                                mMyData.makeBlockList(tempChatData);

                                mFireBaseData.DelChatData(tempChatData.ChatRoomName);
                                mFireBaseData.DelSendData(tempChatData.ChatRoomName);

                                mMyData.arrChatDataList.remove(tempChatData.ChatRoomName);
                                int findIndex = mMyData.arrChatNameList.indexOf(tempChatData.ChatRoomName);
                                mMyData.arrChatNameList.remove(findIndex);

                                onBackPressed();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }

                });

                break;

            case R.id.btn_report:
                final View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.report_popup, null, false);
                builder = new AlertDialog.Builder(mActivity);
                final AlertDialog dialog = builder.setView(v).create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
                final CheckBox cBox1 = (CheckBox) v.findViewById(R.id.checkBox2);
                final CheckBox cBox2 = (CheckBox) v.findViewById(R.id.checkBox3);
                final CheckBox cBox3 = (CheckBox) v.findViewById(R.id.checkBox4);

                Button btn_yes = v.findViewById(R.id.report);
                btn_yes.setText("신고");
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int nReportVal = 0;
                        if(cBox1.isChecked())
                        {
                            nReportVal +=1;
                        }
                        if(cBox2.isChecked())
                        {
                            nReportVal +=2;
                        }
                        if(cBox3.isChecked())
                        {
                            nReportVal +=3;
                        }

                        if(nReportVal == 0)
                        {
                            CommonFunc.getInstance().ShowToast(ChatRoomActivity.this,"신고유형을 선택해 주세요.", true);
                            return;
                        }


                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference table;
                        table = database.getReference("User/" + mMyData.getUserIdx()+ "/SendList/");
                        final int finalNReportVal = nReportVal;
                        table.child(tempChatData.ChatRoomName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().removeValue();

                                mMyData.makeBlockList(tempChatData);

                                mFireBaseData.DelChatData(tempChatData.ChatRoomName);
                                mFireBaseData.DelSendData(tempChatData.ChatRoomName);

                                mMyData.arrChatDataList.remove(tempChatData.ChatRoomName);
                                int findIndex = mMyData.arrChatNameList.indexOf(tempChatData.ChatRoomName);
                                mMyData.arrChatNameList.remove(findIndex);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference table = database.getReference("Reported").child(stTargetData.Idx);
                                final DatabaseReference user = table;

                                Map<String, Object> updateMap = new HashMap<>();
                                updateMap.put("ReportType", finalNReportVal);
                                user.push().setValue(updateMap);

                                CommonFunc.getInstance().ShowToast(ChatRoomActivity.this,"신고가 완료 되었습니다.", true);

                                onBackPressed();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        dialog.cancel();
                    }
                });

                btn_no = v.findViewById(R.id.cancel);
                btn_no.setText("취소");
                btn_no.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View view) {
                        dialog.dismiss();
                    }

                });
                break;
        }
        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1001 && resultCode == RESULT_OK && null != data) {
                Uri uri = data.getData();

                tempSaveUri = uri;
                UploadImage_Firebase(tempSaveUri);

            } else {
                // Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void UploadImage_Firebase(final Uri file) {

        StorageReference riversRef = storageRef.child("chatRoom/" + mMyData.getUserIdx() + "/" + tempSaveUri);//file.getLastPathSegment());

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = calculateInSampleSize(options, 100, 100 , 2);

        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
            bitmap = ExifUtils.rotateBitmap(file.getPath(),bitmap);
        } catch (Exception e) {
        }


        //bitmap = BitmapFactory.decodeFile(file.f, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();


        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

                CommonFunc.getInstance().DismissLoadingPage();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                CommonFunc.getInstance().ShowLoadingPage(mActivity, "로딩중");

/*                progressBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
                int currentprogress = (int) progress;
                progressBar.setProgress(currentprogress);*/

               /* if(bPrePare[0] == false)
                {


                    ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), tempChatData.Nick, "", nowTime, CoomonValueData.getInstance().ImgUrl, 0, 0);
                    mNotiFunc.SendMSGToFCM(stTargetData, "이미지를 보냈습니다");



                    DatabaseReference pushedPostRef = mRef.push();
                    postId[0] = pushedPostRef.getKey();
                    pushedPostRef.setValue(chat_Data);


                    bPrePare[0] = true;
                }*/


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //progressBar.setVisibility(View.INVISIBLE);

                CommonFunc.getInstance().DismissLoadingPage();

                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                long nowTime = CommonFunc.getInstance().GetCurrentTime();
                mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, "이미지를 보냈습니다", nowTime, 0);

                ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), tempChatData.Nick, "", nowTime, downloadUrl.toString(), 0, 0);

                DatabaseReference pushedPostRef = mRef.push();
                pushedPostRef.setValue(chat_Data);

      /*        *//*  ChatData chat_Data = new_img ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), tempChatData.Nick, "", formatStr, downloadUrl.toString(), 0, 0);

                mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, "이미지를 보냈습니다", formatStr, 0);
                mRef.push().setValue(chat_Data);*//*

                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("img", downloadUrl.toString());
                mRef.child( postId[0]).updateChildren(updateMap);*/



             /*   mRef.child( postId[0]).setValue(chat_Data);
                mRef.child( postId[0]).updateChildren()*/
                //mRef.push().setValue(chat_Data);

                tempSaveUri = downloadUrl;
                bPrePare[0] = false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}
