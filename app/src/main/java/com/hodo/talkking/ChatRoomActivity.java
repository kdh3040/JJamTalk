package com.hodo.talkking;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SimpleChatData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.CommonFunc;
import com.hodo.talkking.Util.NotiFunc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.hodo.talkking.MainActivity.mFragmentMng;

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

    int     tempPosition;
    SimpleChatData tempChatData;
    String  tempChatIdx;

    private ProgressBar progressBar;

    private android.app.FragmentManager mFragmentManager;

    static  Uri tempSaveUri;

    static int a = 0;

    private Activity mActivity;

    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layout;
        LinearLayout Msg_layout;
        LinearLayout Msg_detail_layout;

        ImageView image_profile,send_Img;
        TextView message;

        TextView targetName;

        TextView send_new,recv_new;

        ImageView Sender_image_profile,Sender_image_sent;
        TextView Sender_message;

        TextView Sender_sender;
        TextView Sender_time;

        //회색글자 처리 뜸 원인불명
        public ChatViewHolder(View itemView) {
            super(itemView);
            image_profile = (ImageView)itemView.findViewById(R.id.ChatRoom_Img);
           // image_sent = (ImageView)itemView.findViewById(R.id.iv_sent);
            targetName = (TextView)itemView.findViewById(R.id.ChatRoom_name);
            message =(TextView)itemView.findViewById(R.id.message);
            layout = (LinearLayout)itemView.findViewById(R.id.ChatRoom_layout);
            Msg_layout= (LinearLayout)itemView.findViewById(R.id.ChatRoom_msg_layout);
            Msg_detail_layout= (LinearLayout)itemView.findViewById(R.id.ChatRoom_msg_detail_layout);
            send_Img = (ImageView)itemView.findViewById(R.id.send_img);

            send_new = (TextView)itemView.findViewById(R.id.Send_new);
            recv_new  = (TextView)itemView.findViewById(R.id.Recv_new);
          //  Sender_image_profile = (ImageView)itemView.findViewById(R.id.Sender_Img);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mMyData.SetCurFrag(2);
        ChatListFragment frg = null;
        frg = (ChatListFragment)mFragmentMng.findFragmentByTag("ChatListFragment");
        frg.refresh();


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
        tempChatData = mMyData.arrChatDataList.get(mMyData.arrChatNameList.get(tempPosition));

        //stTargetData.NickName = tempChatData.strTargetNick;
        //stTargetData.Img= tempChatData.strTargetImg;

        //stTargetData.NickName = mMyData.arrChatTargetData.get(tempChatIdx).NickName;
        //stTargetData.Img= mMyData.arrChatTargetData.get(tempChatIdx).Img;

        mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(tempChatData.ChatRoomName);

        txt_msg = (EditText)findViewById(R.id.et_msg);


/*        ChatData chat_Data = new ChatData(mMyData.getUserNick(), tempChatData.Nick, tempChatData.Msg, tempChatData.Date, "");
        mRef.push().setValue(chat_Data);*/



        btn_plus = (Button)findViewById(R.id.btn_plus);

        recyclerView = (RecyclerView) findViewById(R.id.chat_list);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatData, ChatViewHolder>(
                ChatData.class,
                R.layout.content_chat_data,
                ChatViewHolder.class,
                mRef){


            @Override
            protected ChatData parseSnapshot(DataSnapshot snapshot) {

                ChatData chat_message = super.parseSnapshot(snapshot);
                if(chat_message != null){
                    chat_message.setId(snapshot.getKey());
                    if(chat_message.getFrom().equals(tempChatData.Nick))
                    {
                        chat_message.Check = 1;
                        mRef.child(chat_message.strId).child("Check").setValue(chat_message.Check);
                    }

                }
                return chat_message;
            }

            int a= 0;

            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, ChatData chat_message, int position) {



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


                 if( !chat_message.getMsg().equals("")){

                    viewHolder.message.setVisibility(TextView.VISIBLE);
                    viewHolder.send_Img.setVisibility(TextView.GONE);
                    viewHolder.targetName.setVisibility(TextView.VISIBLE);

                    viewHolder.message.setText(chat_message.getMsg());
                    viewHolder.targetName.setText(stTargetData.NickName);

                }
                else if( !chat_message.getImg().equals("")){

                     viewHolder.send_Img.setVisibility(TextView.VISIBLE);
                     viewHolder.message.setVisibility(TextView.GONE);
                     viewHolder.targetName.setVisibility(TextView.VISIBLE);

                    Glide.with(getApplicationContext())
                            .load(chat_message.getImg())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.send_Img);

                     viewHolder.targetName.setText( stTargetData.NickName);

                }
                else{
                 //   viewHolder.image_sent.setVisibility(ImageView.VISIBLE);

                    viewHolder.send_Img.setVisibility(TextView.GONE);
                    viewHolder.message.setVisibility(TextView.GONE);
                    viewHolder.targetName.setVisibility(TextView.GONE);
                }

                //viewHolder.sender.setText(chat_message.getFrom());

                Log.d("!@#$%", chat_message.getMsg() + "    " + position +"     " + chat_message.from);

                if(chat_message.from.equals(mMyData.getUserNick()))
              //  if(a % 2 == 0)
                {
                    Log.d("!@#$%", "11111");

                    if (chat_message.Check == 0)
                        viewHolder.send_new.setVisibility(View.VISIBLE);
                    else
                        viewHolder.send_new.setVisibility(View.GONE);

                    viewHolder.recv_new.setVisibility(View.GONE);

                    //viewHolder.send_new.setVisibility(TextView.VISIBLE);
/*                    viewHolder.recv_new.setVisibility(TextView.GONE);

                    if(tempChatData.Check == 0)
                        viewHolder.send_new.setVisibility(TextView.VISIBLE);
                    else
                        viewHolder.send_new.setVisibility(TextView.GONE);*/

                    viewHolder.targetName.setVisibility(TextView.GONE);
                    viewHolder.image_profile.setVisibility(View.GONE);
                    //viewHolder.message.setBackgroundResource(R.drawable.outbox2);
                    viewHolder.message.setBackgroundResource(R.drawable.bg_chat_mine);

                    viewHolder.Msg_layout.setGravity(Gravity.RIGHT);
                    viewHolder.Msg_detail_layout.setGravity(Gravity.RIGHT);
                    a = 0;

                  //  viewHolder.Sender_sender.setText(chat_message.getFrom());
                }
                else
                {
                    Log.d("!@#$%", "22222");

                    viewHolder.send_new.setVisibility(View.GONE);
                    viewHolder.recv_new.setVisibility(View.GONE);

    /*                viewHolder.send_new.setVisibility(TextView.GONE);

                    if(tempChatData.Check == 0)
                        viewHolder.recv_new.setVisibility(TextView.VISIBLE);
                    else
                        viewHolder.recv_new.setVisibility(TextView.GONE);
    */

                    viewHolder.image_profile.setVisibility(View.VISIBLE);
                    viewHolder.targetName.setVisibility(TextView.VISIBLE);
                    viewHolder.targetName.setText(stTargetData.NickName);
                    viewHolder.message.setBackgroundResource(R.drawable.bg_chat_yours);

                   Glide.with(getApplicationContext())
                            .load( stTargetData.Img)
                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.1f)
                            .into(viewHolder.image_profile);

                    viewHolder.Msg_layout.setGravity(Gravity.LEFT);
                    viewHolder.Msg_detail_layout.setGravity(Gravity.LEFT);
                    a = 1;
                }
                a++;
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
                // of the list to show the newly added message.
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
                final AlertDialog dialog = builder.create();
                dialog.show();
                Button btn_gal = popup.findViewById(R.id.btn_gallery);
                btn_gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_IMAGE);

                    }
                });

                Button btn_cam = popup.findViewById(R.id.btn_camera);
                btn_cam.setVisibility(View.GONE);
                btn_cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                Button btn_gift = popup.findViewById(R.id.btn_gift);
                btn_gift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
                        Button btnHeart100 = v.findViewById(R.id.HeartPop_100);
                        Button btnHeart200 = v.findViewById(R.id.HeartPop_200);
                        Button btnHeart300 = v.findViewById(R.id.HeartPop_300);
                        Button btnHeart500 = v.findViewById(R.id.HeartPop_500);
                        Button btnHeart1000 = v.findViewById(R.id.HeartPop_1000);
                        Button btnHeart5000 = v.findViewById(R.id.HeartPop_5000);
                        final TextView Msg = v.findViewById(R.id.HeartPop_text);

                        //tvHeartCnt.setText("꿀 : " + Integer.toString(mMyData.getUserHoney()) + " 개");
                        Msg.setText("100개의 꿀을 보내시겠습니까?");

                        final int[] nSendHoneyCnt = new int[1];
                        nSendHoneyCnt[0] = 10;

                        btnHeart100.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 10;
                                Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                            }
                        });

                        btnHeart200.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 20;
                                Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                            }
                        });

                        btnHeart300.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 30;
                                Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                            }
                        });

                        btnHeart500.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 50;
                                Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                            }
                        });

                        btnHeart1000.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 100;
                                Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                            }
                        });

                        btnHeart5000.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nSendHoneyCnt[0] = 500;
                                Msg.setText(nSendHoneyCnt[0] + "개의 꿀을 보내시겠습니까?");
                            }
                        });

                        final EditText SendMsg = v.findViewById(R.id.HeartPop_Msg);

                        Button btn_gift_send = v.findViewById(R.id.btn_gift_send);
                        btn_gift_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (mMyData.getUserHoney() < nSendHoneyCnt[0]) {
                                    Toast.makeText(getApplicationContext(), "골드가 없습니다. 표시 기능 추가 예정", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String strSendMsg = SendMsg.getText().toString();
                                    if (strSendMsg.equals(""))
                                        strSendMsg = mMyData.getUserNick() + "님이 " + nSendHoneyCnt[0] + "골드를 보내셨습니다!!";

                                    boolean rtValuew = mMyData.makeSendHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);
                                    rtValuew = mMyData.makeRecvHoneyList(stTargetData, nSendHoneyCnt[0], strSendMsg);

                                    if (rtValuew == true) {
                                        //mNotiFunc.SendHoneyToFCM(stTargetData, nSendHoneyCnt[0]);
                                        mMyData.setUserHoney(mMyData.getUserHoney() - nSendHoneyCnt[0]);
                                        mMyData.setSendHoneyCnt(nSendHoneyCnt[0]);
                                        Toast.makeText(getApplicationContext(), rtValuew + "", Toast.LENGTH_SHORT).show();

                                        String message;
                                        if (SendMsg.getText().toString().equals(""))
                                            message = mMyData.getUserNick() + "님이 " + nSendHoneyCnt[0] + "골드를 보내셨습니다!!";
                                        else
                                            message = strSendMsg;

                                        Calendar cal = Calendar.getInstance();
                                        Date date = cal.getTime();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
                                        String formatStr = sdf.format(date);

                                        mNotiFunc.SendHoneyToFCM(stTargetData, nSendHoneyCnt[0]);

                                        ChatData chat_Data = new ChatData(mMyData.getUserNick(),  stTargetData.NickName, message, formatStr, "", 0);
                                        mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, message, formatStr);
                                        mRef.push().setValue(chat_Data);
                                        dialog.dismiss();

                                    }
                                }

                                dialog.dismiss();


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
                String message = txt_msg.getText().toString();
                long nowTime = CommonFunc.getInstance().GetCurrentTime();
                if(txt_msg.getText() == null || txt_msg.getText().equals("")){
                    return;
                }else{
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
                    String formatStr = sdf.format(date);

                    //mNotiFunc.SendMsgToFCM(stTargetData);

                    ChatData chat_Data = new ChatData(mMyData.getUserNick(), tempChatData.Nick, message, formatStr, "",0);

                    mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, message, formatStr);

                    mRef.push().setValue(chat_Data);
                    txt_msg.setText("");

                    mNotiFunc.SendChatToFCM(message, stTargetData.Token);

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
            case R.id.btn_report:
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

                                mMyData.arrChatDataList.remove(mMyData.arrChatNameList.get(tempPosition));
                                mMyData.arrChatNameList.remove(tempPosition);

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
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void UploadImage_Firebase(Uri file) {

        StorageReference riversRef = storageRef.child("chatRoom/" + mMyData.getUserIdx() + "/" + tempSaveUri);//file.getLastPathSegment());

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        //bitmap = BitmapFactory.decodeFile("/sdcard/image.jpg", options);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);

            //bitmap = BitmapFactory.decodeFile(file.f, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getWidth(), true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
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
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //progressBar.setVisibility(View.INVISIBLE);
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    ChatData chat_Data = new ChatData(mMyData.getUserNick(), tempChatData.Nick, "", null, downloadUrl.toString(), 0);

                    mMyData.makeLastMSG(stTargetData, tempChatData.ChatRoomName, "이미지를 보냈습니다", null);
                    mRef.push().setValue(chat_Data);

                    tempSaveUri = downloadUrl;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}
