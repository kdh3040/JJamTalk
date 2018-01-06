package com.hodo.jjamtalk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.LocationFunc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

public class InputProfile extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private LocationFunc mLocalFunc = LocationFunc.getInstance();
    private BoardData mBoardData = BoardData.getInstance();
    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://jamtalk-cf526.appspot.com/");

    private ImageView mProfileImage;
    private EditText mNickName;

    private Spinner AgeSpinner;
    private Spinner GenderSpinner;
    private  int nAge1, nGender;

    private Button CheckBtn;

    private ProgressBar progressBar;
    private FusedLocationProviderClient mFusedLocationClient;

    LocationManager locationManager;
    String provider;

    private Boolean bMySet = false;
    private int nUserSet = 0;
    private static String TAG = "InputActivity Log!!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_profile);

        progressBar = (ProgressBar)findViewById(R.id.InputProfile_Progress);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mProfileImage = (ImageView) findViewById(R.id.InputProfile_SumImg);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InputProfile.this, "이미지 등록", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select"), 1);

            }
        });

        mNickName = (EditText) findViewById(R.id.InputProfile_NickName);

        AgeSpinner = (Spinner) findViewById(R.id.InputProfile_Age_1);
        AgeSpinner.setPrompt("선택");
        AgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        GenderSpinner = (Spinner) findViewById(R.id.InputProfile_Gender_1);
        GenderSpinner.setPrompt("선택");
        GenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String strGender = "여자";

                if(position == 0)
                    strGender = "남자";


                mMyData.setUserGender(strGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        CheckBtn = (Button) findViewById(R.id.InputProfile_Check);
        CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strNickName = mNickName.getText().toString();
                String strImg = mMyData.getUserImg();

                if ("".equals(strNickName)) {
                    Toast.makeText(InputProfile.this, "이름을 입력 해주세요", Toast.LENGTH_SHORT).show();
                }
                //if ("null".equals(strImg)) {
                if (strImg == null) {
                    Toast.makeText(InputProfile.this, "사진을 입력 해주세요", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mMyData.setUserNick(strNickName);
                    mFireBaseData.SaveData(mMyData.getUserIdx());
                    bMySet = true;
                    InitData_Recv();
                    InitData_New();
                    InitData_Send();
                    InitData_Near();
                    /*Intent intent = new Intent(InputProfile.this, MainActivity.class);
                    startActivity(intent);*/
                }
            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mLocalFunc.checkLocationPermission(getApplicationContext(), this)) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    private void InitData_Recv() {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        Query query=ref.orderByChild("RecvCount");//키가 id와 같은걸 쿼리로 가져옴
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0, j=0, k=0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {

                                if(stRecvData.Img == null)
                                    stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                mMyData.arrUserAll_Recv.add(stRecvData);
                                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserAll_Recv.get(i).FanList.entrySet())
                                    mMyData.arrUserAll_Recv.get(i).arrFanList.add(entry.getValue());

                                if(mMyData.arrUserAll_Recv.get(i).Gender.equals("여자"))
                                {
                                    mMyData.arrUserWoman_Recv.add(stRecvData);
                                    for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserWoman_Recv.get(j).FanList.entrySet())
                                        mMyData.arrUserWoman_Recv.get(j).arrFanList.add(entry.getValue());

                                    j++;
                                }
                                else {
                                    mMyData.arrUserMan_Recv.add(stRecvData);
                                    for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserMan_Recv.get(k).FanList.entrySet())
                                        mMyData.arrUserMan_Recv.get(k).arrFanList.add(entry.getValue());

                                    k++;
                                }
                            }
                            i++;
                        }

                        if(nUserSet != 4)
                            nUserSet += 1;
                        if(nUserSet == 4 && bMySet == true){
                            Log.d(TAG, "Account Log in  Complete");
                            GoMainPage();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void InitData_Send() {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        Query query=ref.orderByChild("SendCount");//키가 id와 같은걸 쿼리로 가져옴
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0, j=0, k=0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {

                                if(stRecvData.Img == null)
                                    stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                mMyData.arrUserAll_Send.add(stRecvData);
                                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserAll_Send.get(i).FanList.entrySet())
                                    mMyData.arrUserAll_Send.get(i).arrFanList.add(entry.getValue());

                                if(mMyData.arrUserAll_Send.get(i).Gender.equals("여자"))
                                {
                                    mMyData.arrUserWoman_Send.add(stRecvData);
                                    for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserWoman_Send.get(j).FanList.entrySet())
                                        mMyData.arrUserWoman_Send.get(j).arrFanList.add(entry.getValue());

                                    j++;
                                }
                                else
                                {
                                    mMyData.arrUserMan_Send.add(stRecvData);
                                    for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserMan_Send.get(k).FanList.entrySet())
                                        mMyData.arrUserMan_Send.get(k).arrFanList.add(entry.getValue());

                                    k++;
                                }
                            }
                            i++;
                        }

                        if(nUserSet != 4)
                            nUserSet += 1;
                        if(nUserSet == 4 && bMySet == true){
                            Log.d(TAG, "Account Log in  Complete");
                            GoMainPage();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });

    }

    // 일주일간 NEW 멤버
    private void InitData_New() {
        long time = System.currentTimeMillis();
        SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
        int nTodayDate =  Integer.parseInt(ctime.format(new Date(time)));
        int nStartDate = nTodayDate - 7;

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        Query query=ref.orderByChild("Date").startAt(Integer.toString(nStartDate)).endAt(Integer.toString(nTodayDate));
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0, j=0, k=0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {

                                mMyData.arrUserAll_New.add(stRecvData);
                                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserAll_New.get(i).FanList.entrySet())
                                    mMyData.arrUserAll_New.get(i).arrFanList.add(entry.getValue());

                                if(mMyData.arrUserAll_New.get(i).Gender.equals("여자"))
                                {
                                    mMyData.arrUserWoman_New.add(stRecvData);
                                    for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserWoman_New.get(j).FanList.entrySet())
                                        mMyData.arrUserWoman_New.get(j).arrFanList.add(entry.getValue());

                                    j++;
                                }
                                else
                                {
                                    mMyData.arrUserMan_New.add(stRecvData);
                                    for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserMan_New.get(k).FanList.entrySet())
                                        mMyData.arrUserMan_New.get(k).arrFanList.add(entry.getValue());

                                    k++;
                                }
                            }
                            i++;
                        }

                        if(nUserSet != 4)
                            nUserSet += 1;
                        if(nUserSet == 4 && bMySet == true){
                            Log.d(TAG, "Account Log in  Complete");
                            GoMainPage();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });
    }

    // 경위도 +- 1 씩
    private void InitData_Near() {

        Double lStartLon = mMyData.getUserLon() - 1;
        Double lStartLat = mMyData.getUserLat() - 1;

        Double lEndLon = mMyData.getUserLon() + 1;
        Double IEndLat = mMyData.getUserLon() + 1;

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        Query query=ref
                .orderByChild("Lon")
                .startAt(lStartLon).endAt(lEndLon);


        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0, j=0, k=0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {

                                mMyData.arrUserAll_Near.add(stRecvData);
                                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserAll_Near.get(i).FanList.entrySet())
                                    mMyData.arrUserAll_Near.get(i).arrFanList.add(entry.getValue());

                                if(mMyData.arrUserAll_Near.get(i).Gender.equals("여자"))
                                {
                                    mMyData.arrUserWoman_Near.add(stRecvData);
                                    for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserWoman_Near.get(j).FanList.entrySet())
                                        mMyData.arrUserWoman_Near.get(j).arrFanList.add(entry.getValue());

                                    j++;
                                }
                                else
                                {
                                    mMyData.arrUserMan_Near.add(stRecvData);
                                    for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserMan_Near.get(k).FanList.entrySet())
                                        mMyData.arrUserMan_Near.get(k).arrFanList.add(entry.getValue());

                                    k++;
                                }
                            }
                            i++;
                        }


                        if(nUserSet != 4)
                            nUserSet += 1;
                        if(nUserSet == 4 && bMySet == true){
                            Log.d(TAG, "Account Log in  Complete");
                            GoMainPage();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });
    }

    public void getLocation() {
        OnCompleteListener<Location> mCompleteListener = new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location tempLoc = task.getResult();
                    mMyData.setUserLon(tempLoc.getLongitude());
                    mMyData.setUserLat(tempLoc.getLatitude());
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(this, mCompleteListener);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Uri uri = data.getData();

                mMyData.setUserImg(uri.toString());
                Glide.with(getApplicationContext())
                        .load(mMyData.getUserImg())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(mProfileImage);

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

        StorageReference riversRef = storageRef.child("images/"+ mMyData.getUserIdx() + "/" + 0 );//file.getLastPathSegment());

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
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        System.out.println("Upload is " + progress + "% done");
                        int currentprogress = (int) progress;
                        progressBar.setProgress(currentprogress);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    progressBar.setVisibility(View.INVISIBLE);
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
        mMyData.setUserImg(uri.toString());
        mMyData.setUserProfileImg(0, uri.toString());
        mMyData.setUserImgCnt(1);

        Toast.makeText(this," 사진이 저장되었습니다",Toast.LENGTH_LONG).show();
    }

    private void SetBoardMyData() {
        DatabaseReference refMyBoard;
        refMyBoard = FirebaseDatabase.getInstance().getReference().child("Board");

        refMyBoard.orderByChild("Idx").equalTo(mMyData.getUserIdx()).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // TODO 환웅 내가 쓴 게시물 받아오기
                /*BoardMsgData stRecvData = new BoardMsgData();
                stRecvData = dataSnapshot.getValue(BoardMsgData.class);
                if (stRecvData != null) {
                    if (stRecvData != null) {
                        mBoardData.arrBoardMyList.add(stRecvData);
                    }
                }*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SetBoardData() {

        DatabaseReference refBoard;
        refBoard = FirebaseDatabase.getInstance().getReference().child("Board");
        refBoard.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mBoardData.AddBoardData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(getApplicationContext(), "마이 데이터 cancelled", Toast.LENGTH_SHORT);
            }
        });
    }


    private void GoMainPage() {
        SetBoardData();
        SetBoardMyData();
        Intent intent = new Intent(InputProfile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
