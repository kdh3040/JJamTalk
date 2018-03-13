package com.hodo.talkking;

import android.app.Activity;
import android.content.Context;
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
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.CommonFunc;

import java.io.ByteArrayOutputStream;

import gun0912.tedbottompicker.TedBottomPicker;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class MyProfileActivity extends AppCompatActivity {
    private EditText et_Memo;
    private ImageView Img_Sum;
    private Button change_nick;

    private ImageView[] Img_Profiles = new ImageView[4];
    private int nImgNumber;
    private Spinner Spinner_Age;
    private RadioButton male, female;
    private  int nAge1, nAge2;
    private MyData mMyData = MyData.getInstance();
    private UserData stTargetData = new UserData();

    private TextView et_NickName;
    private Button Btn_Insta;
    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://talkking-2aa18.appspot.com/");
    Activity activity = this;

    DatabaseReference ref;

    private String tempNickName;
    private String tempAge;
    private String tempGender;
    private String tempMemo;
    private String tempImgSum;
    private Uri tempImg[] = new Uri[4];
    private int tempImgCnt;

    private int tempImgChange[] = new int[4];
    private int tempImgChangeCnt;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner_Age = (Spinner) findViewById(R.id.MyProfile_Age_1);
        String strUserAge = mMyData.getUserAge();
        int nUserAge = Integer.parseInt(strUserAge);


        tempNickName = mMyData.getUserNick();
        tempAge = mMyData.getUserAge();
        tempGender = mMyData.getUserGender();
        tempMemo = mMyData.getUserMemo();
        tempImgSum = mMyData.getUserImg();

        for(int i=0; i<4; i++) {
            tempImg[i] = Uri.parse(mMyData.getUserProfileImg(i));
            tempImgChange[i] = 0;
        }

        tempImgChangeCnt = 0;

        nUserAge -= 20;
        Spinner_Age.setSelection(nUserAge);
        Spinner_Age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                nAge1 = position;
                nAge1 += 20;
                String strAge = Integer.toString(nAge1);
                tempAge = strAge;
               // mMyData.setUserAge(strAge);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        male = (RadioButton)findViewById(R.id.male);
        male.setChecked(false);
        female = (RadioButton)findViewById(R.id.female);
        female.setChecked(false);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(female.isChecked()) {
                    male.setChecked(true);
                    female.setChecked(false);
                    tempGender = "남자";
                   // mMyData.setUserGender("남자");
                }
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(male.isChecked()) {
                    female.setChecked(true);
                    male.setChecked(false);
                    tempGender = "여자";
                    //mMyData.setUserGender("여자");
                }
            }
        });


        if(mMyData.getUserGender().equals("남자"))
            male.setChecked(true);
        else
            female.setChecked(true);


        Img_Sum = (ImageView) findViewById(R.id.MyProfile_SumImg);

        Img_Profiles[0] = (ImageView) findViewById(R.id.MyProfile_Img1);
        Img_Profiles[1] = (ImageView) findViewById(R.id.MyProfile_Img2);
        Img_Profiles[2] = (ImageView) findViewById(R.id.MyProfile_Img3);
        Img_Profiles[3] = (ImageView) findViewById(R.id.MyProfile_Img4);
        //Img_Profiles[4] = (ImageView)findViewById(R.id.MyProfile_Img5);

        //Btn_Insta = (Button)findViewById(R.id.MyProfile_Btn_Insta);
        // TODO : 1차에서 인스타 연동 기능 필요
       // Btn_Insta.setVisibility(View.GONE);

        et_Memo = (EditText) findViewById(R.id.MyProfile_Memo);
        et_Memo.setText(mMyData.getUserMemo());

        et_NickName = (TextView) findViewById(R.id.MyProfile_NickName);
        et_NickName.setText(mMyData.getUserNick());

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        ConstraintLayout ll = (ConstraintLayout)findViewById(R.id.constraintLayout);
        ll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imm.hideSoftInputFromWindow(et_Memo.getWindowToken(), 0);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {

                    case R.id.MyProfile_SumImg:
                      //  startActivity(new Intent(getApplicationContext(), ImageViewPager.class));

                        stTargetData.ImgGroup0 =  tempImg[0].toString();
                        stTargetData.ImgGroup1 = tempImg[1].toString();
                        stTargetData.ImgGroup2 = tempImg[2].toString();
                        stTargetData.ImgGroup3 = tempImg[3].toString();

                        stTargetData.ImgCount = mMyData.getUserImgCnt();

                        Intent intent = new Intent(getApplicationContext(), ImageViewPager.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Target", stTargetData);
                        bundle.putSerializable("Index", 0);
                        intent.putExtras(bundle);
                        startActivity(intent);

                        //LoadImage(view, 5);
                        break;
                    case R.id.MyProfile_Img1:
                        popUp(0);
                        break;
                    case R.id.MyProfile_Img2:
                        popUp(1);
                        break;
                    case R.id.MyProfile_Img3:
                        popUp(2);
                        break;
                    case R.id.MyProfile_Img4:
                        popUp(3);
                        break;
                    //case R.id.MyProfile_Btn_Insta:
                    case R.id.change_nick:
                        LayoutInflater inflater = LayoutInflater.from(activity);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        View view1 = inflater.inflate(R.layout.dialog_change_nick, null);
                        Button btn_cancel = view1.findViewById(R.id.btn_cancel);
                        final EditText et_msg = view1.findViewById(R.id.et_nick);

                        et_msg.setHint("(8글자 이내)");
                        int maxLengthofEditText = 8;
                        et_msg.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthofEditText)});

                        TextView title = (TextView)view1.findViewById(R.id.textView);
                        title.setText("닉네임 변경");

                        TextView body = (TextView)view1.findViewById(R.id.tv_change_nick);
                        if(mMyData.NickChangeCnt != 0)
                            body.setText("닉네임 변경 시 50코인이 소모됩니다");

                        else
                            body.setText("닉네임 최초 변경은 무료입니다 \n 이후에는 50코인이 소모됩니다");

                        builder.setView(view1);

                        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                        ConstraintLayout ll = (ConstraintLayout)view1.findViewById(R.id.constraintLayout);
                        ll.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                imm.hideSoftInputFromWindow(et_msg.getWindowToken(), 0);
                            }
                        });


                        final AlertDialog msgDialog = builder.create();
                        msgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        msgDialog.show();
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                msgDialog.dismiss();
                            }
                        });
                        Button btn_send = view1.findViewById(R.id.btn_send);
/*



                           btn_send.setText("코인 구매");
                           btn_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(activity, BuyGoldActivity.class));
                                    msgDialog.dismiss();
                                }
                            });
                        }
                        else
                        {*/
                        btn_send.setText("변경");

                        btn_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(mMyData.getUserHoney() < 50)
                                    CommonFunc.getInstance().ShowToast(getApplicationContext(), "코인이 부족합니다.", true);
                                else
                                    {

                                    String strMemo = et_msg.getText().toString();
                                    strMemo = CommonFunc.getInstance().RemoveEmptyString(strMemo);

                                    if(CommonFunc.getInstance().CheckTextMaxLength(strMemo, CoomonValueData.TEXT_MAX_LENGTH_NICKNAME, MyProfileActivity.this ,"닉네임", true) == false)
                                        return;


                                    if(mMyData.NickChangeCnt != 0)
                                    {
                                        mMyData.setUserHoney(mMyData.getUserHoney() - 50);
                                    }

                                    mMyData.NickChangeCnt++;
                                    mMyData.setUserNick(strMemo);
                                    et_NickName.setText(strMemo);
                                    CommonFunc.getInstance().ShowToast(getApplicationContext(), "닉네임 변경 완료!", true);

                                    msgDialog.dismiss();

                                    }
                                }
                            });
                        break;
                        }



                        //break;

                }
            };


        Img_Sum.setOnClickListener(listener);
        Img_Profiles[0].setOnClickListener(listener);
        Img_Profiles[1].setOnClickListener(listener);
        Img_Profiles[2].setOnClickListener(listener);
        Img_Profiles[3].setOnClickListener(listener);

        //et_NickName.setOnClickListener(listener);
        change_nick = findViewById(R.id.change_nick);
        change_nick.setOnClickListener(listener);
        //Btn_Insta.setOnClickListener(listener);

        //Img_Profiles[4].setOnClickListener(listener);*/

        Glide.with(getApplicationContext())
                .load(mMyData.strProfileImg[0])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //   .bitmapTransform(new RoundedCornersTransformation(getApplicationContext()))
                .thumbnail(0.1f)
                .into(Img_Sum);


        for (int i = 0; i < 4; i++) {
            if(mMyData.strProfileImg[i].equals("1"))
            {
                Glide.with(getApplicationContext())
                        .load(R.drawable.picture)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                       // .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.1f)
                        .into(Img_Profiles[i]);
            }
            else
            {
                Glide.with(getApplicationContext())
                        .load(tempImg[i])
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        //.bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.1f)
                        .into(Img_Profiles[i]);
            }
        }

    }

    private void popUp(final int index) {

        if(tempImg[index].equals(Uri.parse("1")))
        {
            int tempIdx = 0;
            LoadImage(index);
       /*     for (int i = 0; i<4;i++)
            {
                if(tempImg[i].equals(Uri.parse("1")))
                {
                    tempIdx = i;
                    LoadImage(tempIdx);
                    break;
                }
            }*/
        }
        else
        {
            final ViewClickDialog dialog = new ViewClickDialog(activity, index);
            dialog.show();

            if(index == 0)
            {
                //dialog.tv_delete.setVisibility(View.GONE);
            }
            dialog.tv_see.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 사진 보기
                    stTargetData.ImgGroup0 = tempImg[index].toString();
                    stTargetData.ImgGroup1 = "1";
                    stTargetData.ImgGroup2 = "1";
                    stTargetData.ImgGroup3 = "1";

                    stTargetData.ImgCount = 1;//mMyData.getUserImgCnt();

                    Intent intent = new Intent(getApplicationContext(), ImageViewPager.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Target", stTargetData);
                    bundle.putSerializable("Index", index);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    //startActivity(new Intent(getApplicationContext(),ImageViewPager.class));
                    dialog.dismiss();
                }
            });

            dialog.tv_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 앨범 선택
                    LoadImage(index);
                    dialog.dismiss();
                }
            });
            /*dialog.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 사진 삭제
                    DeleteData(index);
                    //DeleteFireBaseData(index);
                    dialog.dismiss();
                }
            });*/
        }


    }

    private void LoadImage(int i) {

        nImgNumber = i;

        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(MyProfileActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // uri 활용
                        tempImg[nImgNumber] = uri;

                        if(nImgNumber == 0)
                        {
                            mMyData.setUserImg(tempImg[nImgNumber].toString());
                            Glide.with(getApplicationContext())
                                    .load(mMyData.getUserImg())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.1f)
                                    .into(Img_Sum);
                        }

                       // mMyData.setUserProfileImg(nImgNumber, uri.toString());

                        Glide.with(getApplicationContext())
                                .load(tempImg[nImgNumber].toString())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                //.bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                .thumbnail(0.1f)
                                .into(Img_Profiles[nImgNumber]);

                        if(tempImgChange[nImgNumber] == 0) {
                            tempImgChangeCnt++;
                        }

                        tempImgChange[nImgNumber] = 1;


               /*         if(nImgNumber <= mMyData.getUserImgCnt() - 1)
                        {
                            // Toast.makeText(this, "사진 되었습니다.", Toast.LENGTH_LONG).show();
                        }
                        else
                            tempImgCnt +=1;*/
                            //mMyData.setUserImgCnt(mMyData.getUserImgCnt()+1);

           /*             mMyData.urSaveUri = uri;
                        mMyData.nSaveUri = nImgNumber;

                        if(mMyData.nSaveUri == 0)
                            UploadThumbNailImage_Firebase(mMyData.urSaveUri);

                        UploadImage_Firebase(mMyData.urSaveUri);*/
                    }
                })
                .create();

        bottomSheetDialogFragment.show(getSupportFragmentManager());
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight, int SampleSize) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = SampleSize;


        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void UploadThumbNailImage_Firebase(Uri file) {

        StorageReference riversRef = storageRef.child("images/"+ mMyData.getUserIdx() + "/" +  "ThumbNail" );//file.getLastPathSegment());

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
       /* String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(file, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        cursor.close();*/


        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
        } catch (Exception e) {
        }

        if(bitmap.getWidth() * bitmap.getHeight() * 4 / 1024 >= 60)
        {
            options.inSampleSize = calculateInSampleSize(options, 100, 100 , 8);
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
            } catch (Exception e) {
            }
        }

        else
        {
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
            } catch (Exception e) {
            }
        }

        bitmap = ExifUtils.rotateBitmap(file.getPath(),bitmap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.createScaledBitmap(bitmap, 50, 50, true);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                TrThumbNail(downloadUrl);
            }
        });
    }


    private void UploadImage_Firebase(final int Idx, Uri file) {

        StorageReference riversRef = storageRef.child("images/"+ mMyData.getUserIdx() + "/" +  Idx );//file.getLastPathSegment());
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = calculateInSampleSize(options, 100, 100 , 2);


        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
            bitmap = ExifUtils.rotateBitmap(file.getPath(),bitmap);
        } catch (Exception e) {
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Tr(Idx, downloadUrl);

                tempImgChangeCnt --;
                if(tempImgChangeCnt == 0)
                {
                    SaveData();
                }
                //strImg = downloadUrl.toString();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                   System.out.println("Upload is " + progress + "% done");

                CommonFunc.getInstance().ShowToast(getApplicationContext(), "업로드 중 GIF 이미지 출력 부분", true);

            }
        });
    }

    public void TrThumbNail(Uri uri)
    {
        mMyData.setUserImg(uri.toString());
      //  Toast.makeText(this," 사진이 저장되었습니다",Toast.LENGTH_LONG).show();
    }

    public void Tr(int Idx, Uri uri)
    {
        mMyData.setUserProfileImg( Idx, uri.toString());
       // Toast.makeText(this," 사진이 저장되었습니다",Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_save){
            MyDataSave();
        }
        if(item.getItemId() == android.R.id.home)
        {
            MyDataSave();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        MyDataSave();
    }

    public void onRealBackPressed()
    {
        super.onBackPressed();
    }

    private void MyDataSave()
    {
        boolean bChangeImg = false;
        for(int i=0; i<4; i++)
        {
            if(tempImgChange[i] == 1)
            {
                bChangeImg = true;
            }
        }

        if(!mMyData.getUserNick().equals(tempNickName) ||
                !mMyData.getUserAge().equals(tempAge) ||
                !mMyData.getUserGender().equals(tempGender) ||
                !et_Memo.getText().toString().equals(mMyData.getUserMemo()) ||
                bChangeImg)
        {
            CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
                public void YesListener() {
                    if(!mMyData.getUserNick().equals(tempNickName))
                        mMyData.setUserNick(tempNickName);

                    if(!mMyData.getUserAge().equals(tempAge))
                        mMyData.setUserAge(tempAge);

                    if(!mMyData.getUserGender().equals(tempGender))
                        mMyData.setUserGender(tempGender);

                    if(!et_Memo.getText().toString().equals(mMyData.getUserMemo()))
                        mMyData.setMemo(et_Memo.getText());

                    boolean bChangeImg = false;
                    for(int i=0; i<4; i++)
                    {
                        if(tempImgChange[i] == 1)
                        {
                            if(i == 0)
                                UploadThumbNailImage_Firebase(tempImg[i]);

                            UploadImage_Firebase(i, tempImg[i]);

                            bChangeImg = true;
                        }

                        if(!tempImg[i].equals(Uri.parse("1")))
                        {
                            tempImgCnt++;
                        }

                        mMyData.setUserProfileImg( i, tempImg[i].toString());
                    }

                    mMyData.setUserImgCnt(tempImgCnt);

                    if(bChangeImg == false)
                        SaveData();
                }
            };

            CommonFunc.ShowDefaultPopup_YesListener Nolistener = new CommonFunc.ShowDefaultPopup_YesListener() {
                public void YesListener() {
                    onRealBackPressed();
                }
            };
            CommonFunc.getInstance().ShowDefaultPopup(MyProfileActivity.this, listener, Nolistener, "프로필","변경된 프로필을 저장 하시겠습니까?", "네", "아니요" );
        }
        else
            onRealBackPressed();
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

    public  void DeleteData(final int Index)
    {
        tempImg[Index] = Uri.parse("1");
        //mMyData.delUserProfileImg(Index, "1");
        //mMyData.setUserImgCnt(mMyData.getUserImgCnt()-1);

        Glide.with(getApplicationContext())
                .load(mMyData.getUserImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //   .bitmapTransform(new RoundedCornersTransformation(getApplicationContext()))
                .thumbnail(0.1f)
                .into(Img_Sum);

        for (int i = 0; i < 4; i++) {

            if(tempImg[i].toString().equals("1"))
            {
                Glide.with(getApplicationContext())
                        .load(R.drawable.picture)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                      //  .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.1f)
                        .into(Img_Profiles[i]);
            }
            else
            {
                Glide.with(getApplicationContext())
                        .load(tempImg[i])
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        //.bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.1f)
                        .into(Img_Profiles[i]);
            }

        }
    }

    public  void DeleteFireBaseData(final int Index)
    {
        mFireBaseData.SaveData(mMyData.getUserIdx());
    }

    public  void SaveData()
    {
        mFireBaseData.SaveData(mMyData.getUserIdx());
        onRealBackPressed();
    }

}
