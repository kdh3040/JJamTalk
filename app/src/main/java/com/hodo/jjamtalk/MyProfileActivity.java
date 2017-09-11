package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Firebase.FirebaseData;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class MyProfileActivity extends AppCompatActivity {
    private EditText et_Memo, et_NickName;
    private ImageView Img_Sum;
    private ImageView[] Img_Profiles = new ImageView[4];
    private int nImgNumber;
    private MyData mMyData = MyData.getInstance();
    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://jamtalk-cf526.appspot.com/");
    Activity activity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Img_Sum = (ImageView)findViewById(R.id.MyProfile_SumImg);


        Img_Profiles[0] = (ImageView)findViewById(R.id.MyProfile_Img1);
        Img_Profiles[1] = (ImageView)findViewById(R.id.MyProfile_Img2);
        Img_Profiles[2] = (ImageView)findViewById(R.id.MyProfile_Img3);
        Img_Profiles[3] = (ImageView)findViewById(R.id.MyProfile_Img4);
        //Img_Profiles[4] = (ImageView)findViewById(R.id.MyProfile_Img5);

        et_Memo = (EditText)findViewById(R.id.MyProfile_Memo);
        et_Memo.setText("같이 놀아요");

        et_NickName = (EditText)findViewById(R.id.MyProfile_NickName);
        et_NickName.setText(mMyData.getUserNick());

        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {

                    case R.id.MyProfile_SumImg:
                        startActivity(new Intent(getApplicationContext(),ImageViewPager.class));

                        //LoadImage(view, 5);
                        break;
                    case R.id.MyProfile_Img1:
                        popUp();
                    
                        //LoadImage(view, 1);
                        break;
                    case R.id.MyProfile_Img2:
                        popUp();
                        //LoadImage(view, 2);
                        break;
                    case R.id.MyProfile_Img3:
                        popUp();
                        //LoadImage(view, 3);
                        break;
                    case R.id.MyProfile_Img4:
                        popUp();
                        //LoadImage(view, 4);
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
                .thumbnail(0.1f)
                .into(Img_Sum);

/*        Glide.with(getApplicationContext())
                .load(mMyData.arrImgList.get(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Img_Profiles[0]);*/


        for(int i = 0; i< 4; i++) {
            if(mMyData.arrImgList.get(i) == null)
            {
                Glide.with(getApplicationContext())
                        .load("http://imagescdn.gettyimagesbank.com/500/14/730/414/0/512600801.jpg")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(Img_Profiles[i]);

            }
            else
            {
                Glide.with(getApplicationContext())
                        .load(mMyData.arrImgList.get(i))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(Img_Profiles[i]);
            }

        }
    }

    private void popUp() {
        ViewClickDialog dialog = new ViewClickDialog(activity);
        dialog.show();
    }
/*
    @Override
    public void onBackPressed(){

        mMyData.setProfileData(txt_Memo.getText(), txt_School.getText(), txt_Company.getText(), txt_Title.getText());

        mFireBaseData.SaveData(mMyData.getUserIdx());
        Intent intent = new Intent(this, MyPageActivity.class);
        startActivity(intent);
        finish();
    }*/


    private void LoadImage(View view, int i) {
        
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

               /* Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                ImgProfile.setImageBitmap(scaled);*/

                UploadImage_Firebase(uri);

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void UploadImage_Firebase(Uri file) {

        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
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
    }

    public void Tr(Uri uri)
    {
        if(mMyData.arrImgList.size() < nImgNumber)
            mMyData.arrImgList.add(uri.toString());
        else
            mMyData.arrImgList.set(nImgNumber-1, uri.toString());
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

            mMyData.setUserNick(et_NickName.getText().toString());
            mMyData.setProfileData(et_Memo.getText());
            mFireBaseData.SaveData(mMyData.getUserIdx());

        }
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }




}
