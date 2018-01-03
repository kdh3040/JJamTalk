package com.hodo.jjamtalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hodo.jjamtalk.Data.ChatData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.PublicRoomChatData;
import com.hodo.jjamtalk.Data.SendData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.NotiFunc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 10..
 */

public class ChatRoomActivity extends AppCompatActivity {
    private NotiFunc mNotiFunc = NotiFunc.getInstance();
    private MyData mMyData = MyData.getInstance();
    private UserData stTargetData = new UserData();

    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://jamtalk-cf526.appspot.com/");

    private static final int REQUEST_IMAGE = 1001;
    Button btn_send,btn_plus;
    EditText txt_msg;

    Context context=this;
    DatabaseReference mRef;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<ChatData, ChatViewHolder> firebaseRecyclerAdapter;
    LinearLayoutManager mLinearLayoutManager;
    SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm");

    SendData tempChatData;
    int  tempChatIdx;

    private ProgressBar progressBar;

    static LinearLayout layout;
    static LinearLayout Msg_layout;

    static  Uri tempSaveUri;

    static int a = 0;


    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        ImageView image_profile,send_Img;
        TextView message;

        TextView targetName;
        TextView time;

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
            time = (TextView)itemView.findViewById(R.id.time);
            layout = (LinearLayout)itemView.findViewById(R.id.ChatRoom_layout);
            Msg_layout= (LinearLayout)itemView.findViewById(R.id.ChatRoom_msg_layout);
            send_Img = (ImageView)itemView.findViewById(R.id.send_img);

          //  Sender_image_profile = (ImageView)itemView.findViewById(R.id.Sender_Img);

            time = (TextView)itemView.findViewById(R.id.time);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent intent = getIntent();
      //  progressBar = (ProgressBar)findViewById(R.id.chat_Progress);

        tempChatData = (SendData) intent.getExtras().getSerializable("ChatData");
        tempChatIdx = (int) intent.getExtras().getSerializable("ChatIdx");

        //stTargetData.NickName = tempChatData.strTargetNick;
        //stTargetData.Img= tempChatData.strTargetImg;

        stTargetData.NickName = mMyData.arrChatTargetData.get(tempChatIdx).NickName;
        stTargetData.Img= mMyData.arrChatTargetData.get(tempChatIdx).Img;

        mRef = FirebaseDatabase.getInstance().getReference().child("ChatData").child(tempChatData.strSendName);

        txt_msg = (EditText)findViewById(R.id.et_msg);


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
                }
                return chat_message;
            }


            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, ChatData chat_message, int position) {

                viewHolder.image_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getApplicationContext(), UserPageActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("Target", mMyData.arrChatTargetData.get(tempChatIdx));
                        intent.putExtras(bundle);

                        view.getContext().startActivity(intent);

                    }
                });


                 if( !chat_message.getMsg().equals("")){

                    viewHolder.message.setVisibility(TextView.VISIBLE);
                    viewHolder.send_Img.setVisibility(TextView.GONE);
                    viewHolder.targetName.setVisibility(TextView.VISIBLE);

                    viewHolder.message.setText(chat_message.getMsg());
                    viewHolder.targetName.setText( mMyData.arrChatTargetData.get(tempChatIdx).NickName);


                }
                else if( !chat_message.getImg().equals("")){

                     viewHolder.send_Img.setVisibility(TextView.VISIBLE);
                     viewHolder.message.setVisibility(TextView.GONE);
                     viewHolder.targetName.setVisibility(TextView.VISIBLE);

                    Glide.with(getApplicationContext())
                            .load(chat_message.getImg())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.send_Img);

                     viewHolder.targetName.setText( mMyData.arrChatTargetData.get(tempChatIdx).NickName);

                }
                else{
                 //   viewHolder.image_sent.setVisibility(ImageView.VISIBLE);

                    viewHolder.send_Img.setVisibility(TextView.GONE);
                    viewHolder.message.setVisibility(TextView.GONE);
                    viewHolder.targetName.setVisibility(TextView.GONE);
                }

                //viewHolder.sender.setText(chat_message.getFrom());


                //if(chat_message.strFrom.equals(mMyData.getUserNick()))
                if(a % 2 == 0)
                {
                    viewHolder.message.setBackgroundResource(R.drawable.outbox2);
                    viewHolder.send_Img.setBackgroundResource(R.drawable.outbox2);

                    Msg_layout.setGravity(Gravity.RIGHT);

                    viewHolder.targetName.setVisibility(TextView.GONE);
                    viewHolder.image_profile.setVisibility(View.GONE);


                  //  viewHolder.Sender_sender.setText(chat_message.getFrom());
                }
                else
                {
                    viewHolder.targetName.setVisibility(TextView.VISIBLE);
                    viewHolder.send_Img.setBackgroundResource(R.drawable.inbox2);
                    viewHolder.message.setBackgroundResource(R.drawable.inbox2);
                    Msg_layout.setGravity(Gravity.LEFT);
                 //   viewHolder.Sender_image_profile.setVisibility(View.GONE);
                    //viewHolder.image_profile.setVisibility(View.GONE);
    /*                viewHolder.Sender_image_profile.setVisibility(View.INVISIBLE);
                    viewHolder.image_profile.setVisibility(View.VISIBLE);*/

                    Glide.with(getApplicationContext())
                            .load( mMyData.arrChatTargetData.get(tempChatIdx).Img)
                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.1f)
                            .into(viewHolder.image_profile);
                }
                a ++;
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


                        TextView tvHeartCnt = v.findViewById(R.id.HeartPop_MyHeart);
                        Button btnHeart100 = v.findViewById(R.id.HeartPop_100);
                        Button btnHeart200 = v.findViewById(R.id.HeartPop_200);
                        Button btnHeart300 = v.findViewById(R.id.HeartPop_300);
                        Button btnHeart500 = v.findViewById(R.id.HeartPop_500);
                        Button btnHeart1000 = v.findViewById(R.id.HeartPop_1000);
                        Button btnHeart5000 = v.findViewById(R.id.HeartPop_5000);
                        final TextView Msg = v.findViewById(R.id.HeartPop_text);

                        tvHeartCnt.setText("꿀 : " + Integer.toString(mMyData.getUserHoney()) + " 개");
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

                                    boolean rtValuew = mMyData.makeSendHoneyList(mMyData.arrChatTargetData.get(tempChatIdx), nSendHoneyCnt[0], strSendMsg);
                                    rtValuew = mMyData.makeRecvHoneyList(mMyData.arrChatTargetData.get(tempChatIdx), nSendHoneyCnt[0], strSendMsg);

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
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                        String formatStr = sdf.format(date);

                                        ChatData chat_Data = new ChatData(mMyData.getUserNick(),  mMyData.arrChatTargetData.get(tempChatIdx).NickName, message, formatStr, "");
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
                long nowTime =System.currentTimeMillis();
                if(txt_msg.getText() == null){
                    return;
                }else{
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    String formatStr = sdf.format(date);

                    ChatData chat_Data = new ChatData(mMyData.getUserNick(), tempChatData.strTargetNick, message, formatStr, "");
                    mRef.push().setValue(chat_Data);
                    txt_msg.setText("");

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
                        mMyData.makeBlockList(tempChatData);

                        mFireBaseData.DelChatData(tempChatData.strSendName);
                        mFireBaseData.DelSendData(tempChatData.strSendName);

                        //Intent intent = new Intent(getApplicationContext(),ChatListActivity.class);
                        //startActivity(intent);
                        finish();
                    }
                }).
                        setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).setMessage("이 사람을 차단하시겠습니까? \n(차단과 함께 대화방이 삭제됩니다. 앞으로 이 사람으로부터 쪽지 및 선물을 받지 않습니다.)")
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
                    ChatData chat_Data = new ChatData(mMyData.getUserNick(), tempChatData.strTargetNick, "", null, downloadUrl.toString());
                    mRef.push().setValue(chat_Data);

                    tempSaveUri = downloadUrl;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
