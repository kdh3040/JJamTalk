package com.hodo.talkking;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

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
import com.hodo.talkking.Data.ChatData;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SimpleChatData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.CommonFunc;
import com.hodo.talkking.Util.NotiFunc;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.hodo.talkking.MainActivity.mFragmentMng;
import static com.hodo.talkking.MyProfileActivity.calculateInSampleSize;

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
    StorageReference storageRef = storage.getReferenceFromUrl("gs://talkking-2aa18.appspot.com/");

    private static final int REQUEST_IMAGE = 1001;
    Button btn_send,btn_plus;
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


        TextView gift_check;
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
            nickname1 = itemView.findViewById(R.id.from_nickname);
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
            gift_from = (TextView)itemView.findViewById(R.id.from);
            gift_from_Nickname = (TextView)itemView.findViewById(R.id.from_nickname);
            gift_to = (TextView)itemView.findViewById(R.id.to);
            gift_to_Nickname = (TextView)itemView.findViewById(R.id.to_nickname);
            gift_Msg = (TextView) itemView.findViewById(R.id.giftmessage);
            gift_Heart_img = (ImageView)itemView.findViewById(R.id.heart);
            gift_Heart_Cnt = (TextView)itemView.findViewById(R.id.heart_count);
            gift_check= (TextView)itemView.findViewById(R.id.gift_check);
            gift_date = (TextView)itemView.findViewById(R.id.gift_date);

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
            tempChatData.Date = "";
            tempChatData.Check = 0;
        }
        else
            tempChatData = mMyData.arrChatDataList.get(mMyData.arrChatNameList.get(tempPosition));

        GiftLayout = (ConstraintLayout)findViewById(R.id.ChatGiftLayout);
        //stTargetData.NickName = tempChatData.strTargetNick;
        //stTargetData.Img= tempChatData.strTargetImg;

        //stTargetData.NickName = mMyData.arrChatTargetData.get(tempChatIdx).NickName;
        //stTargetData.Img= mMyData.arrChatTargetData.get(tempChatIdx).Img;

        mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(tempChatData.ChatRoomName);
        setTitle(stTargetData.NickName);

        boolean btnSendEnable = true;
        txt_msg = (EditText)findViewById(R.id.et_nick);


/*        ChatData chat_Data = new ChatData(mMyData.getUserNick(), tempChatData.Nick, tempChatData.Msg, tempChatData.Date, "");
        mRef.push().setValue(chat_Data);*/



        btn_plus = (Button)findViewById(R.id.btn_plus);

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

                        Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("Target", stTargetData);
                        intent.putExtras(bundle);

                        view.getContext().startActivity(intent);

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
                        viewHolder.gift_check.setVisibility(View.VISIBLE);
                    }

                    else
                    {
                        viewHolder.check.setVisibility(View.GONE);
                        viewHolder.send_img2_check.setVisibility(View.GONE);
                        viewHolder.gift_check.setVisibility(View.GONE);
                    }

                    viewHolder.message1.setVisibility(TextView.GONE);
                    viewHolder.targetName.setVisibility(TextView.GONE);
                    viewHolder.image_profile.setVisibility(View.GONE);
                    viewHolder.send_Img1.setVisibility(TextView.GONE);
                    viewHolder.date1.setVisibility(TextView.GONE);
                    viewHolder.date2.setVisibility(TextView.VISIBLE);

                    long time = CommonFunc.getInstance().GetCurrentTime();
                    Date writeDate = CommonFunc.getInstance().GetStringToDate(chat_message.time, CoomonValueData.DATE_FORMAT);
                    Date todayDate = new Date(time);

                    if(CommonFunc.getInstance().IsTodayDate(todayDate, writeDate))
                    {
                        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_TODAY_DATE_FORMAT);
                        viewHolder.date2.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.send_Img2_date.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.send_Img2_date.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.gift_date.setText(ctime.format(new Date(writeDate.getTime())));
                    }
                    else
                    {
                        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_DATE_FORMAT);
                        viewHolder.date2.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.send_Img2_date.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.send_Img2_date.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.gift_date.setText(ctime.format(new Date(writeDate.getTime())));
                    }

                    if(chat_message.Heart == 0)
                    {
                        viewHolder.gift_img.setVisibility(ImageView.GONE);
                        viewHolder.gift_from.setVisibility(TextView.GONE);
                        viewHolder.gift_from_Nickname.setVisibility(TextView.GONE);
                        viewHolder.gift_to.setVisibility(TextView.GONE);
                        viewHolder.gift_to_Nickname.setVisibility(TextView.GONE);
                        viewHolder.gift_Msg.setVisibility(TextView.GONE);
                        viewHolder.gift_Heart_img.setVisibility(ImageView.GONE);
                        viewHolder.gift_Heart_Cnt.setVisibility(TextView.GONE);
                        viewHolder.gift_date.setVisibility(TextView.GONE);
                        viewHolder.gift_check.setVisibility(TextView.GONE);

                        viewHolder.send_Img2.setVisibility(View.GONE);
                        viewHolder.send_Img2_date.setVisibility(TextView.GONE);

                        viewHolder.send_Img1_date.setVisibility(View.GONE);

                        viewHolder.date2.setVisibility(TextView.GONE);
                        viewHolder.message2.setVisibility(View.GONE);



                        if( !chat_message.getMsg().equals("")){

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

                        viewHolder.gift_img.setVisibility(ImageView.VISIBLE);
                        viewHolder.gift_from.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_from_Nickname.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_from_Nickname.setText(mMyData.getUserNick());
                        viewHolder.gift_to.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_to_Nickname.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_to_Nickname.setText(stTargetData.NickName);
                        viewHolder.gift_Msg.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_Msg.setText(chat_message.msg);
                        viewHolder.gift_Heart_img.setVisibility(ImageView.VISIBLE);
                        viewHolder.gift_Heart_Cnt.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_Heart_Cnt.setText(Integer.toString(chat_message.Heart));
                        viewHolder.gift_date.setVisibility(View.VISIBLE);
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
                    viewHolder.gift_check.setVisibility(TextView.GONE);

                    long time = CommonFunc.getInstance().GetCurrentTime();
                    Date writeDate = CommonFunc.getInstance().GetStringToDate(chat_message.time, CoomonValueData.DATE_FORMAT);
                    Date todayDate = new Date(time);

                    if(CommonFunc.getInstance().IsTodayDate(todayDate, writeDate))
                    {
                        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_TODAY_DATE_FORMAT);
                        viewHolder.date1.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.gift_date.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.send_Img1_date.setText(ctime.format(new Date(writeDate.getTime())));
                    }
                    else
                    {
                        SimpleDateFormat ctime = new SimpleDateFormat(CoomonValueData.BOARD_DATE_FORMAT);
                        viewHolder.date1.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.gift_date.setText(ctime.format(new Date(writeDate.getTime())));
                        viewHolder.send_Img1_date.setText(ctime.format(new Date(writeDate.getTime())));
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
                        viewHolder.gift_from.setVisibility(TextView.GONE);
                        viewHolder.gift_from_Nickname.setVisibility(TextView.GONE);
                        viewHolder.gift_to.setVisibility(TextView.GONE);
                        viewHolder.gift_to_Nickname.setVisibility(TextView.GONE);
                        viewHolder.gift_Msg.setVisibility(TextView.GONE);
                        viewHolder.gift_Heart_img.setVisibility(ImageView.GONE);
                        viewHolder.gift_Heart_Cnt.setVisibility(TextView.GONE);

                        viewHolder.gift_date.setVisibility(TextView.GONE);

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
                                bundle.putSerializable("Target", tempUser);
                                bundle.putSerializable("Index", 0);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });


                    }
                    else
                    {


                        viewHolder.image_profile.setVisibility(TextView.GONE);
                        viewHolder.targetName.setVisibility(TextView.GONE);
                        viewHolder.date1.setVisibility(TextView.GONE);
                        viewHolder.send_Img1.setVisibility(TextView.GONE);
                        viewHolder.send_Img1_date.setVisibility(TextView.GONE);

                        viewHolder.message1.setVisibility(View.GONE);

                        viewHolder.gift_img.setVisibility(ImageView.VISIBLE);
                        viewHolder.gift_from.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_from_Nickname.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_from_Nickname.setText(stTargetData.NickName);
                        viewHolder.gift_to.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_to_Nickname.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_to_Nickname.setText(mMyData.getUserNick());
                        viewHolder.gift_Msg.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_Msg.setText(chat_message.msg);
                        viewHolder.gift_Heart_img.setVisibility(ImageView.VISIBLE);
                        viewHolder.gift_Heart_Cnt.setVisibility(TextView.VISIBLE);
                        viewHolder.gift_Heart_Cnt.setText(Integer.toString(chat_message.Heart));
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

                     /*   Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image*//*");
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_IMAGE);*/

                    }
                });

                /*Button btn_cam = popup.findViewById(R.id.btn_camera);
                btn_cam.setVisibility(View.GONE);
                btn_cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/

                ImageButton btn_gift = popup.findViewById(R.id.btn_gift);
                btn_gift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_gal_gift.dismiss();
                        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_send_gift,null);
                        builder.setView(v);
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        Button btn_HeartCharge = (Button)v.findViewById(R.id.HeartPop_Charge);
                        btn_HeartCharge.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getApplicationContext(), BuyGoldActivity.class));
                            }
                        });


                        //TextView tvHeartCnt = v.findViewById(R.id.HeartPop_MyHeart);
                        final int[] nSendHoneyCnt = new int[1];
                        nSendHoneyCnt[0] = 10;

                        Button btnHeart100 = v.findViewById(R.id.HeartPop_10);
                        Button btnHeart200 = v.findViewById(R.id.HeartPop_30);
                        Button btnHeart300 = v.findViewById(R.id.HeartPop_50);
                        Button btnHeart500 = v.findViewById(R.id.HeartPop_100);
                        Button btnHeart1000 = v.findViewById(R.id.HeartPop_300);
                        Button btnHeart5000 = v.findViewById(R.id.HeartPop_500);
                        final TextView Msg = v.findViewById(R.id.HeartPop_text);
                        Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+ nSendHoneyCnt[0]+"코인 소모)");
                        //tvHeartCnt.setText("꿀 : " + Integer.toString(mMyData.getUserHoney()) + " 개");
                        //Msg.setText("현재 보유 코인은 "+String.valueOf(mMyData.getUserHoney())+"코인 입니다." );
                        final Button btn_gift_send = v.findViewById(R.id.btn_gift_send);

                        btnHeart100.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 10;
                                if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                    int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                    btn_gift_send.setEnabled(false);
                                    Msg.setText(String.valueOf(nPrice)+"코인이 부족합니다" );
                                }
                                else {
                                    btn_gift_send.setEnabled(true);
                                    Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+ nSendHoneyCnt[0]+"코인 필요)");
                                }
                            }
                        });

                        btnHeart200.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 30;
                                if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                    int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                    btn_gift_send.setEnabled(false);
                                    Msg.setText(String.valueOf(nPrice)+"코인이 부족합니다" );
                                }
                                else {
                                    btn_gift_send.setEnabled(true);
                                    Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+ nSendHoneyCnt[0]+"코인 필요)");
                                }
                            }
                        });

                        btnHeart300.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 50;
                                if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                    int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                    btn_gift_send.setEnabled(false);
                                    Msg.setText(String.valueOf(nPrice)+"코인이 부족합니다" );
                                }
                                else {
                                    btn_gift_send.setEnabled(true);
                                    Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+ nSendHoneyCnt[0]+"코인 필요)");
                                }
                            }
                        });

                        btnHeart500.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 100;
                                if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                    int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                    btn_gift_send.setEnabled(false);
                                    Msg.setText(String.valueOf(nPrice)+"코인이 부족합니다" );
                                }
                                else {
                                    btn_gift_send.setEnabled(true);
                                    Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+ nSendHoneyCnt[0]+"코인 필요)");
                                }
                            }
                        });

                        btnHeart1000.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 300;
                                if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                    int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                    btn_gift_send.setEnabled(false);
                                    Msg.setText(String.valueOf(nPrice)+"코인이 부족합니다");
                                }
                                else {
                                    btn_gift_send.setEnabled(true);
                                    Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+ nSendHoneyCnt[0]+"코인 필요)");
                                }
                            }
                        });

                        btnHeart5000.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 500;
                                if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                    int nPrice = nSendHoneyCnt[0] - mMyData.getUserHoney();
                                    btn_gift_send.setEnabled(false);
                                    Msg.setText(String.valueOf(nPrice)+"코인이 부족합니다" );
                                }
                                else {
                                    btn_gift_send.setEnabled(true);
                                    Msg.setText(nSendHoneyCnt[0] + "하트를 날리시겠습니까?("+ nSendHoneyCnt[0]+"코인 필요)");
                                }
                            }
                        });

                        final EditText SendMsg = v.findViewById(R.id.HeartPop_Msg);

                        btn_gift_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                    btn_gift_send.setEnabled(false);
                                }
                                else
                                {
                                    btn_gift_send.setEnabled(true);
                                    String strSendMsg = SendMsg.getText().toString();
                                    boolean rtValuew = mMyData.makeSendHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);
                                    rtValuew = mMyData.makeRecvHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);
                                    rtValuew = mMyData.makeCardList(stTargetData);

                                        //mNotiFunc.SendHoneyToFCM(stTargetData, nSendHoneyCnt[0]);
                                        mMyData.setUserHoney(mMyData.getUserHoney() - nSendHoneyCnt[0]);
                                        mNotiFunc.SendHoneyToFCM(stTargetData, nSendHoneyCnt[0],strSendMsg );
                                        mMyData.setSendHoneyCnt(nSendHoneyCnt[0]);
                                        mMyData.makeFanList(stTargetData, nSendHoneyCnt[0]);
                                        CommonFunc.getInstance().ShowToast(getApplicationContext(), rtValuew ? "성공" : "실패", true);

                                        Calendar cal = Calendar.getInstance();
                                        Date date = cal.getTime();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                                        String formatStr = sdf.format(date);

                                        ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(),  stTargetData.NickName, strSendMsg, formatStr, "", 0, nSendHoneyCnt[0]);

                                        if(strSendMsg.equals(""))
                                            strSendMsg = mMyData.getUserNick() + "님이 " + nSendHoneyCnt[0] + " 하트를 보냈습니다";
                                        mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, strSendMsg, formatStr, nSendHoneyCnt[0]);
                                        mRef.push().setValue(chat_Data);
                                        dialog.dismiss();

                                }

                                dialog.dismiss();
                                CommonFunc.getInstance().ShowToast(ChatRoomActivity.this, nSendHoneyCnt[0]  + " 하트를 보냈습니다.", true);
                            }
                        });
                        Button btn_gift_cancel = v.findViewById(R.id.btn_gift_cancel);
                        btn_gift_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });


            }
        });
        btn_send = (Button)findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CommonFunc.getInstance().CheckTextMaxLength(txt_msg.getText().toString(), CoomonValueData.TEXT_MAX_LENGTH_CHAT, mActivity ,"채팅", true) == false)
                    return;

                int nSize = mMyData.arrBlockedDataList.size();

                boolean bBlocked = false;

                for (int i = 0; i < nSize; i++) {
                    if (mMyData.arrBlockedDataList.get(i).Idx.equals(stTargetData.Idx)) {
                        bBlocked = true;
                        break;
                    }
                }

                if(bBlocked == true)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    final int[] nSendHoneyCnt = new int[1];
                    nSendHoneyCnt[0] = 0;
                    View giftView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_send_msg, null);
                    builder.setView(giftView);
                    final AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    final TextView Msg = giftView.findViewById(R.id.textView);
                    Msg.setText("메세지 전송 실패");

                    final EditText Edit = giftView.findViewById(R.id.et_nick);
                    Edit.setVisibility(View.GONE);

                    final TextView Body = giftView.findViewById(R.id.tv_change_nick);
                    Body.setText("당신은 차단 되었습니다");

                    final Button OK = giftView.findViewById(R.id.btn_send);
                    OK.setText("확인");
                    OK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    });

                    final Button No = giftView.findViewById(R.id.btn_cancel);
                    No.setVisibility(View.GONE);

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

                else
                {
                    String message = txt_msg.getText().toString();
                    int nLength = message.length();
                    long nowTime = CommonFunc.getInstance().GetCurrentTime();
                    if (txt_msg.getText().toString().replace(" ", "").equals("")) {
                        return;
                    }else{
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                        String formatStr = sdf.format(date);

                        //mNotiFunc.SendMsgToFCM(stTargetData);

                        ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), tempChatData.Nick, message, formatStr, "",0, 0);

                        mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, message, formatStr, 0);

                        mRef.push().setValue(chat_Data);
                        txt_msg.setText("");

                        mNotiFunc.SendChatToFCM(stTargetData, message);
                    }

                }
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){


            case R.id.btn_block:
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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

                                //mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_CHAT);

                              /*  Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("StartFragment", 2);
                                getApplicationContext().startActivity(intent);
                                finish();*/
                                //mCommon.refreshFragMent(MainActivity.mFragmentMng, frg);


                          /*      final FragmentTransaction ft = frgMng.beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commit();*/

                              //  finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        //Intent intent = new Intent(getApplicationContext(),ChatListActivity.class);
                        //startActivity(intent);

                    }
                }).
                        setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).setMessage("이 사람을 차단하시겠습니까? \n(차단과 함께 대화방이 삭제됩니다. 앞으로 이 사람으로부터 쪽지 및 선물을 받지 않습니다. \n차단은 설정에서 다시 해제하실 수 있습니다.)")
                        .setTitle("차단하기");
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.btn_report:
                final View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.report_popup, null, false);
                builder = new AlertDialog.Builder(mActivity);
                final AlertDialog dialog1 = builder.setView(v).create();
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog1.show();
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

                                onBackPressed();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        dialog1.cancel();
                    }
                });

                Button btn_no = v.findViewById(R.id.cancel);
                btn_no.setText("취소");
                btn_no.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View view) {
                        dialog1.dismiss();
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
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
/*                progressBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
                int currentprogress = (int) progress;
                progressBar.setProgress(currentprogress);*/

                if(bPrePare[0] == false)
                {
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                    String formatStr = sdf.format(date);

                    ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), tempChatData.Nick, "", formatStr, mMyData.strImgLodingUri, 0, 0);

                    mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, "이미지를 보냈습니다", formatStr, 0);

                    DatabaseReference pushedPostRef = mRef.push();
                    postId[0] = pushedPostRef.getKey();
                    pushedPostRef.setValue(chat_Data);


                    bPrePare[0] = true;
                }


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //progressBar.setVisibility(View.INVISIBLE);
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                String formatStr = sdf.format(date);

                ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), tempChatData.Nick, "", formatStr, downloadUrl.toString(), 0, 0);

              /*  ChatData chat_Data = new ChatData(mMyData.getUserIdx(), mMyData.getUserNick(), tempChatData.Nick, "", formatStr, downloadUrl.toString(), 0, 0);

                mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, "이미지를 보냈습니다", formatStr, 0);
                mRef.push().setValue(chat_Data);*/

                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("img", downloadUrl.toString());
                mRef.child( postId[0]).updateChildren(updateMap);

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
