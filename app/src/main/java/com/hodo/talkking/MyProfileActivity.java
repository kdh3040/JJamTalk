package com.hodo.talkking;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hodo.talkking.Data.ChatData;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.CommonFunc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class MyProfileActivity extends AppCompatActivity {
    private EditText et_Memo;
    private ImageView Img_Sum;
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

    private String strThumbNail, strImg;

    private boolean bChangeImg = false;


    DatabaseReference ref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner_Age = (Spinner) findViewById(R.id.MyProfile_Age_1);
        String strUserAge = mMyData.getUserAge();
        int nUserAge = Integer.parseInt(strUserAge);

        nUserAge -= 20;
        Spinner_Age.setSelection(nUserAge);
        Spinner_Age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                nAge1 = position;
                nAge1 += 20;
                String strAge = Integer.toString(nAge1);
                mMyData.setUserAge(strAge);
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
                    mMyData.setUserGender("남자");
                }
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(male.isChecked()) {
                    female.setChecked(true);
                    male.setChecked(false);
                    mMyData.setUserGender("여자");
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

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {

                    case R.id.MyProfile_SumImg:
                      //  startActivity(new Intent(getApplicationContext(), ImageViewPager.class));

                        stTargetData.ImgGroup0 = mMyData.getUserImg();
                        stTargetData.ImgGroup1 = mMyData.strProfileImg[0];
                        stTargetData.ImgGroup2 = mMyData.strProfileImg[1];
                        stTargetData.ImgGroup3 = mMyData.strProfileImg[2];
                        stTargetData.ImgGroup4 = mMyData.strProfileImg[3];

                        stTargetData.ImgCount = mMyData.getUserImgCnt();

                        Intent intent = new Intent(getApplicationContext(), ImageViewPager.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Target", stTargetData);
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
                    case R.id.MyProfile_NickName:
                        LayoutInflater inflater = LayoutInflater.from(activity);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        View view1 = inflater.inflate(R.layout.alert_send_msg, null);
                        Button btn_cancel = view1.findViewById(R.id.btn_cancel);
                        final EditText et_msg = view1.findViewById(R.id.et_msg);

                        et_msg.setHint("(8글자 이내)");
                        int maxLengthofEditText = 8;
                        et_msg.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthofEditText)});

                        TextView title = (TextView)view1.findViewById(R.id.textView);
                        title.setText("닉네임 변경");

                        TextView body = (TextView)view1.findViewById(R.id.textView4);
                        body.setText("닉네임 변경은 50골드가 필요합니다");

                        builder.setView(view1);


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

                        if(mMyData.getUserHoney() < 50)
                        {
                            btn_send.setText("골드 구매");
                            btn_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(activity, BuyGoldActivity.class));
                                    msgDialog.dismiss();
                                }
                            });
                        }
                        else
                        {
                            btn_send.setText("변경");

                            btn_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String strMemo = et_msg.getText().toString();
                                    strMemo = CommonFunc.getInstance().RemoveEmptyString(strMemo);

                                    if(CommonFunc.getInstance().CheckTextMaxLength(strMemo, CoomonValueData.TEXT_MAX_LENGTH_NICKNAME, MyProfileActivity.this ,"닉네임", true) == false)
                                        return;

                                    mMyData.setUserHoney(mMyData.getUserHoney() - 50);
                                    mMyData.setUserNick(strMemo);
                                    et_NickName.setText(strMemo);

                                    msgDialog.dismiss();
                                }
                            });
                        }

                        break;
                        //break;

                }
            }
        };

        Img_Sum.setOnClickListener(listener);
        Img_Profiles[0].setOnClickListener(listener);
        Img_Profiles[1].setOnClickListener(listener);
        Img_Profiles[2].setOnClickListener(listener);
        Img_Profiles[3].setOnClickListener(listener);

        et_NickName.setOnClickListener(listener);

        //Btn_Insta.setOnClickListener(listener);

        //Img_Profiles[4].setOnClickListener(listener);*/

        Glide.with(getApplicationContext())
                .load(mMyData.strProfileImg[0])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //   .bitmapTransform(new RoundedCornersTransformation(getApplicationContext()))
                .thumbnail(0.1f)
                .into(Img_Sum);


        for (int i = 0; i < mMyData.getUserImgCnt(); i++) {
                Glide.with(getApplicationContext())
                        .load(mMyData.strProfileImg[i])
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.1f)
                        .into(Img_Profiles[i]);
        }

    }

    private void popUp(final int index) {

        if(mMyData.strProfileImg[index].equals("1"))
        {
            LoadImage(index);
        }
        else
        {
            final ViewClickDialog dialog = new ViewClickDialog(activity, index);
            dialog.show();

            dialog.tv_see.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 사진 보기
                    stTargetData.ImgGroup0 = mMyData.getUserImg();
                    stTargetData.ImgGroup1 = mMyData.strProfileImg[0];
                    stTargetData.ImgGroup2 = mMyData.strProfileImg[1];
                    stTargetData.ImgGroup3 = mMyData.strProfileImg[2];
                    stTargetData.ImgGroup4 = mMyData.strProfileImg[3];

                    stTargetData.ImgCount = mMyData.getUserImgCnt();

                    Intent intent = new Intent(getApplicationContext(), ImageViewPager.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Target", stTargetData);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    //startActivity(new Intent(getApplicationContext(),ImageViewPager.class));
                    dialog.dismiss();
                }
            });

        /*dialog.tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카메라로 찍기

            }
        });
        });*/

            dialog.tv_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 앨범 선택
                    LoadImage(index);
                    dialog.dismiss();
                }
            });
            dialog.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 사진 삭제
                    DeleteData(index);
                    DeleteFireBaseData(index);
                    dialog.dismiss();
                }
            });
        }


    }

    private void LoadImage(int i) {


        nImgNumber = i;
/*        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/"+mMyData.getUserIdx() + "*//*");
        startActivityForResult(Intent.createChooser(intent, "Select"), 1);*/

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery,1000);

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
          //  if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            if (resultCode == RESULT_OK && requestCode == 1000 ){
                Uri uri = data.getData();

                if(nImgNumber == 0)
                {
                    mMyData.setUserImg(uri.toString());
                    Glide.with(getApplicationContext())
                            .load(mMyData.getUserImg())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(0.1f)
                            .into(Img_Sum);
                }

                mMyData.setUserProfileImg(nImgNumber, uri.toString());
                Glide.with(getApplicationContext())
                        .load(mMyData.getUserProfileImg(nImgNumber))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.1f)
                        .into(Img_Profiles[nImgNumber]);

                if(nImgNumber <= mMyData.getUserImgCnt() - 1)
                {
                   // Toast.makeText(this, "사진 되었습니다.", Toast.LENGTH_LONG).show();
                }
                else
                    mMyData.setUserImgCnt(mMyData.getUserImgCnt()+1);

                mMyData.urSaveUri = uri;
                mMyData.nSaveUri = nImgNumber;

                if(mMyData.nSaveUri == 0)
                    UploadThumbNailImage_Firebase(mMyData.urSaveUri);

                UploadImage_Firebase(mMyData.urSaveUri);

            } else {
              //  Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
          //  Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight, boolean thumbnail) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if(thumbnail == true)
            inSampleSize = 8;


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

        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(file, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

        bitmap = BitmapFactory.decodeFile(imagePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        if(bitmap.getWidth() * bitmap.getHeight() * 4 / 1024 >= 60)
        {
            options.inSampleSize = calculateInSampleSize(options, 100, 100 , true);
            bitmap = BitmapFactory.decodeFile(imagePath, options);
        }

        else
        {
            bitmap = BitmapFactory.decodeFile(imagePath, options);
        }

        bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);
        cursor.close();

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
                strThumbNail = downloadUrl.toString();
                bChangeImg = true;
                TrThumbNail(downloadUrl);
            }
        });
    }


    private void UploadImage_Firebase(Uri file) {

        StorageReference riversRef = storageRef.child("images/"+ mMyData.getUserIdx() + "/" +  mMyData.nSaveUri );//file.getLastPathSegment());

        Bitmap bitmap = null;

        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(file, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        options.inSampleSize = calculateInSampleSize(options, 100, 100 , false);

        bitmap = BitmapFactory.decodeFile(imagePath, options);
        bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);
        cursor.close();

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
                Tr(downloadUrl);

                //strImg = downloadUrl.toString();
            }
        });
    }

    public void TrThumbNail(Uri uri)
    {
        mMyData.setUserImg(uri.toString());
        mFireBaseData.SaveData(mMyData.getUserIdx());
      //  Toast.makeText(this," 사진이 저장되었습니다",Toast.LENGTH_LONG).show();
    }

    public void Tr(Uri uri)
    {
        mMyData.setUserProfileImg( mMyData.nSaveUri, uri.toString());
        mFireBaseData.SaveData(mMyData.getUserIdx());
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
            //프로필 저장 구현
            //Toast.makeText(this,"프로필이 저장되었습니다",Toast.LENGTH_LONG).show();

          /*  if(bChangeImg)
                UploadImage_Firebase(mMyData.urSaveUri);*/
         /*   if(bChangeImg == true)
                mMyData.setUserImg(strThumbNail);

            mMyData.setUserProfileImg( mMyData.nSaveUri, strImg);*/

            mMyData.setProfileData(et_Memo.getText());
            mFireBaseData.SaveData(mMyData.getUserIdx());
            bChangeImg = false;

            Intent intent = new Intent(this, MyPageActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId() == android.R.id.home)
        {
          //  Toast.makeText(this,"프로필이 저장되었습니다",Toast.LENGTH_LONG).show();

         /*   if(bChangeImg)
                UploadImage_Firebase(mMyData.urSaveUri);*/

            mMyData.setProfileData(et_Memo.getText());
            mFireBaseData.SaveData(mMyData.getUserIdx());
            bChangeImg = false;

            Intent intent = new Intent(this, MyPageActivity.class);
            startActivity(intent);
            finish();
            //onBackPressed();
        }

        return super.onOptionsItemSelected(item);
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
        mMyData.delUserProfileImg(Index, "1");
        mMyData.setUserImgCnt(mMyData.getUserImgCnt()-1);

        Glide.with(getApplicationContext())
                .load(mMyData.getUserImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //   .bitmapTransform(new RoundedCornersTransformation(getApplicationContext()))
                .thumbnail(0.1f)
                .into(Img_Sum);

        for(int i=0;i<4;i++)
        {
            Glide.with(getApplicationContext())
                .load(mMyData.getUserProfileImg(i))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .thumbnail(0.1f)
                .into(Img_Profiles[i]);
        }

    }

    public  void DeleteFireBaseData(final int Index)
    {
        mFireBaseData.SaveData(mMyData.getUserIdx());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Toast.makeText(this,"프로필이 저장되었습니다",Toast.LENGTH_LONG).show();

         /*   if(bChangeImg)
                UploadImage_Firebase(mMyData.urSaveUri);*/

      /* mMyData.setProfileData(et_Memo.getText());
        mFireBaseData.SaveData(mMyData.getUserIdx());
        bChangeImg = false;
*/
        Intent intent = new Intent(this, MyPageActivity.class);
        startActivity(intent);
        finish();
    }

}