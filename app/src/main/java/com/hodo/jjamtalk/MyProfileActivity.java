package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.kakao.usermgmt.response.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class MyProfileActivity extends AppCompatActivity {
    private EditText et_Memo, et_NickName;
    private ImageView Img_Sum;
    private ImageView[] Img_Profiles = new ImageView[4];
    private int nImgNumber;
    private Spinner Spinner_Age;
    private  int nAge1, nAge2;
    private MyData mMyData = MyData.getInstance();
    private UserData stTargetData = new UserData();

    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://jamtalk-cf526.appspot.com/");
    Activity activity = this;

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

        nUserAge -= 17;
        Spinner_Age.setSelection(nUserAge);
        Spinner_Age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                nAge1 = position;
                nAge1 += 17;
                String strAge = Integer.toString(nAge1);
                mMyData.setUserAge(strAge);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Img_Sum = (ImageView) findViewById(R.id.MyProfile_SumImg);

        Img_Profiles[0] = (ImageView) findViewById(R.id.MyProfile_Img1);
        Img_Profiles[1] = (ImageView) findViewById(R.id.MyProfile_Img2);
        Img_Profiles[2] = (ImageView) findViewById(R.id.MyProfile_Img3);
        Img_Profiles[3] = (ImageView) findViewById(R.id.MyProfile_Img4);
        //Img_Profiles[4] = (ImageView)findViewById(R.id.MyProfile_Img5);

        et_Memo = (EditText) findViewById(R.id.MyProfile_Memo);
        et_Memo.setText(mMyData.getUserMemo());

        et_NickName = (EditText) findViewById(R.id.MyProfile_NickName);
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

                }
            }
        };

        Img_Sum.setOnClickListener(listener);
        Img_Profiles[0].setOnClickListener(listener);
        Img_Profiles[1].setOnClickListener(listener);
        Img_Profiles[2].setOnClickListener(listener);
        Img_Profiles[3].setOnClickListener(listener);
        //Img_Profiles[4].setOnClickListener(listener);*/

        Glide.with(getApplicationContext())
                .load(mMyData.getUserImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //   .bitmapTransform(new RoundedCornersTransformation(getApplicationContext()))
                .thumbnail(0.1f)
                .into(Img_Sum);

        Glide.with(getApplicationContext())
                .load(mMyData.getUserImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .thumbnail(0.1f)
                .into(Img_Profiles[0]);

/*        Glide.with(getApplicationContext())
                .load(mMyData.arrImgList.get(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Img_Profiles[0]);*/

        for (int i = 1; i < 4; i++) {
            if (mMyData.strProfileImg[i] == null) {
                Glide.with(getApplicationContext())
                        .load("http://imagescdn.gettyimagesbank.com/500/14/730/414/0/512600801.jpg")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.1f)
                        .into(Img_Profiles[i]);

            } else {
                Glide.with(getApplicationContext())
                        .load(mMyData.strProfileImg[i])
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .thumbnail(0.1f)
                        .into(Img_Profiles[i]);
            }
        }

    }

    private void popUp(final int index) {
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

        dialog.tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카메라로 찍기

            }
        });
        dialog.tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 앨범 선택
                LoadImage(view, index);
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

    private void LoadImage(View view, int i) {

        bChangeImg = true;
        nImgNumber = i;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/"+mMyData.getUserIdx() + "/*");
        startActivityForResult(Intent.createChooser(intent, "Select"), 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Uri uri = data.getData();

      /*          Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeFile(uri.toString(), options);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);*/


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
                    Toast.makeText(this, "사진 되었습니다.", Toast.LENGTH_LONG).show();
                }
                else
                    mMyData.setUserImgCnt(mMyData.getUserImgCnt()+1);

                mMyData.urSaveUri = uri;
                mMyData.nSaveUri = nImgNumber;
                UploadImage_Firebase(mMyData.urSaveUri);

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void UploadImage_Firebase(Uri file) {

        StorageReference riversRef = storageRef.child("images/"+ mMyData.getUserIdx() + "/" +  mMyData.nSaveUri );//file.getLastPathSegment());

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.createScaledBitmap(bitmap, 350, 350, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
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
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Tr(Uri uri)
    {
        if( mMyData.nSaveUri == 0)
        {
            mMyData.setUserImg(uri.toString());
        }

        mMyData.setUserProfileImg( mMyData.nSaveUri, uri.toString());
        mFireBaseData.SaveData(mMyData.getUserIdx());
        Toast.makeText(this," 사진이 저장되었습니다",Toast.LENGTH_LONG).show();

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
            Toast.makeText(this,"프로필이 저장되었습니다",Toast.LENGTH_LONG).show();

          /*  if(bChangeImg)
                UploadImage_Firebase(mMyData.urSaveUri);*/

            mMyData.setUserNick(et_NickName.getText().toString());
            mMyData.setProfileData(et_Memo.getText());
            mFireBaseData.SaveData(mMyData.getUserIdx());
            bChangeImg = false;

            Intent intent = new Intent(this, MyPageActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId() == android.R.id.home)
        {
            Toast.makeText(this,"프로필이 저장되었습니다",Toast.LENGTH_LONG).show();

         /*   if(bChangeImg)
                UploadImage_Firebase(mMyData.urSaveUri);*/

            mMyData.setUserNick(et_NickName.getText().toString());
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

    public  void DeleteData(final int Index)
    {
        mMyData.delUserProfileImg(Index, "http://imagescdn.gettyimagesbank.com/500/14/730/414/0/512600801.jpg");
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
        Toast.makeText(this,"프로필이 저장되었습니다",Toast.LENGTH_LONG).show();

         /*   if(bChangeImg)
                UploadImage_Firebase(mMyData.urSaveUri);*/

        mMyData.setUserNick(et_NickName.getText().toString());
        mMyData.setProfileData(et_Memo.getText());
        mFireBaseData.SaveData(mMyData.getUserIdx());
        bChangeImg = false;

        Intent intent = new Intent(this, MyPageActivity.class);
        startActivity(intent);
        finish();
    }

}
