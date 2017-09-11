package com.hodo.jjamtalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
    private Spinner Spinner_Age1, Spinner_Age2;
    private  int nAge1, nAge2;
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
        Spinner_Age1 = (Spinner)findViewById(R.id.MyProfile_Age_1);
        String strUserAge = mMyData.getUserAge();
        int nUserAge = Integer.parseInt(strUserAge);

        Spinner_Age1.setSelection(nUserAge / 10);
        Spinner_Age1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                nAge1 = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        nUserAge = nUserAge - ((nUserAge / 10) *10);
        Spinner_Age2 = (Spinner)findViewById(R.id.MyProfile_Age_2);
        Spinner_Age2.setSelection(nUserAge);
        Spinner_Age2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                nAge2 = position;
                String strAge = nAge1 +""+ nAge2;

                mMyData.setUserAge(strAge);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        Img_Sum = (ImageView)findViewById(R.id.MyProfile_SumImg);

        Img_Profiles[0] = (ImageView)findViewById(R.id.MyProfile_Img1);
        Img_Profiles[1] = (ImageView)findViewById(R.id.MyProfile_Img2);
        Img_Profiles[2] = (ImageView)findViewById(R.id.MyProfile_Img3);
        Img_Profiles[3] = (ImageView)findViewById(R.id.MyProfile_Img4);
        //Img_Profiles[4] = (ImageView)findViewById(R.id.MyProfile_Img5);

        et_Memo = (EditText)findViewById(R.id.MyProfile_Memo);
        et_Memo.setText(mMyData.getUserMemo());

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
                        popUp(1);
                    
                        //LoadImage(view, 1);
                        break;
                    case R.id.MyProfile_Img2:
                        popUp(2);
                        //LoadImage(view, 2);
                        break;
                    case R.id.MyProfile_Img3:
                        popUp(3);
                        //LoadImage(view, 3);
                        break;
                    case R.id.MyProfile_Img4:
                        popUp(4);
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

        Glide.with(getApplicationContext())
                .load(mMyData.getUserImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(Img_Profiles[0]);

/*        Glide.with(getApplicationContext())
                .load(mMyData.arrImgList.get(0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Img_Profiles[0]);*/


        for(int i = 0; i< mMyData.arrImgList.size(); i++) {
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

    private void popUp(final int index) {
        final ViewClickDialog dialog = new ViewClickDialog(activity, index);
        dialog.show();

        dialog.tv_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사진 보기
                startActivity(new Intent(getApplicationContext(),ImageViewPager.class));
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
                dialog.dismiss();
            }
        });

    }

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

         /*       Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                Img_Profiles[nImgNumber-1].setImageBitmap(scaled);*/

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


        StorageReference riversRef = storageRef.child("images/"+ mMyData.getUserIdx() + "/" + nImgNumber );//file.getLastPathSegment());
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

        Glide.with(getApplicationContext())
                .load(mMyData.arrImgList.get(nImgNumber-1))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(Img_Profiles[nImgNumber-1]);

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
            mMyData.setUserNick(et_NickName.getText().toString());
            mMyData.setProfileData(et_Memo.getText());
            mFireBaseData.SaveData(mMyData.getUserIdx());
            //onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    public  void DeleteData(final int Index)
    {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://");
        StorageReference desertRef = storageRef.child("images/"+ mMyData.getUserIdx() + "/" + Index );//file.getLastPathSegment());

// Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                mMyData.arrImgList.remove(Index);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

        Glide.with(getApplicationContext())
                .load("http://imagescdn.gettyimagesbank.com/500/14/730/414/0/512600801.jpg")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(Img_Profiles[Index]);

    }


}
