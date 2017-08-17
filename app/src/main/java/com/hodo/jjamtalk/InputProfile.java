package com.hodo.jjamtalk;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.TempBoardData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.LocationFunc;

import java.io.IOException;
import java.security.Permission;
import java.security.Permissions;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

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
    private ArrayAdapter<CharSequence> AgeSpin;
    private ArrayAdapter<CharSequence> GenderSpin;

    private Button CheckBtn;

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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mProfileImage = (ImageView) findViewById(R.id.InputProfile_Img);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InputProfile.this, "이미지 등록", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select"), 1);

            }
        });

        mNickName = (EditText) findViewById(R.id.InputProfile_Nick);

        AgeSpinner = (Spinner) findViewById(R.id.InputProfile_AgeSpinner);
        AgeSpinner.setPrompt("선택");
        AgeSpin = ArrayAdapter.createFromResource(this, R.array.InputProfile_Age, android.R.layout.simple_list_item_checked);
        AgeSpin.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        AgeSpinner.setAdapter(AgeSpin);
        AgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int Position, long Id) {
                Toast.makeText(InputProfile.this, "나이" + AgeSpin.getItem(Position), Toast.LENGTH_SHORT).show();
                mMyData.setUserAge(AgeSpin.getItem(Position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mMyData.setUserAge(AgeSpin.getItem(0).toString());
            }
        });

        GenderSpinner = (Spinner) findViewById(R.id.InputProfile_GenderSpinner);
        GenderSpinner.setPrompt("선택");
        GenderSpin = ArrayAdapter.createFromResource(this, R.array.InputProfile_Gender, android.R.layout.simple_list_item_checked);
        GenderSpin.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        GenderSpinner.setAdapter(GenderSpin);
        GenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int Position, long Id) {
                Toast.makeText(InputProfile.this, "성별" + GenderSpin.getItem(Position), Toast.LENGTH_SHORT).show();
                mMyData.setUserGender(GenderSpin.getItem(Position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mMyData.setUserGender(GenderSpin.getItem(0).toString());
            }
        });

        CheckBtn = (Button) findViewById(R.id.InputProfile_Check);
        CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strNickName = mNickName.getText().toString();
                if (strNickName != null) {
                    mMyData.setUserNick(strNickName);
                    mFireBaseData.SaveData(mMyData.getUserIdx());
                    bMySet = true;
                    InitData_Rank();
                    InitData_New();
                    InitData_Hot();
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


    private void InitData_Rank() {
        DatabaseReference refMan, refWoman;
        refMan = FirebaseDatabase.getInstance().getReference().child("Users").child("남자");
        Query query=refMan.orderByChild("Rank");//키가 id와 같은걸 쿼리로 가져옴
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.arrUserMan_Rank.add(stRecvData);
                                mMyData.arrUserAll_Rank.add(stRecvData);
                                Log.d("Login Man_Rank : ", mMyData.arrUserMan_Rank.get(i).NickName);
                            }
                            i++;
                        }

                        if(nUserSet != 4)
                            nUserSet+=1;

                        else if(nUserSet == 4 && bMySet == true){
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

        refWoman = FirebaseDatabase.getInstance().getReference().child("Users").child("여자");
        query=refWoman.orderByChild("Rank");//키가 id와 같은걸 쿼리로 가져옴
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.arrUserWoman_Rank.add(stRecvData);
                                mMyData.arrUserAll_Rank.add(stRecvData);
                                Log.d("Login Woman_Rank : ", mMyData.arrUserWoman_Rank.get(i).NickName);
                            }
                        }


                        if(nUserSet != 8)
                            nUserSet += 1;
                        else if(nUserSet == 7 && bMySet == true){
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

    private void InitData_Hot() {
        DatabaseReference refMan, refWoman;
        refMan = FirebaseDatabase.getInstance().getReference().child("Users").child("남자");
        Query query=refMan.orderByChild("Hot");//키가 id와 같은걸 쿼리로 가져옴
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.arrUserMan_Hot.add(stRecvData);
                                mMyData.arrUserAll_Hot.add(stRecvData);
                                Log.d("Login arrUserMan_Hot : ", mMyData.arrUserMan_Hot.get(i).NickName);
                            }
                            i++;
                        }

                        if(nUserSet != 8)
                            nUserSet += 1;
                        if(nUserSet == 8 && bMySet == true){
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

        refWoman = FirebaseDatabase.getInstance().getReference().child("Users").child("여자");
        query=refWoman.orderByChild("Hot");//키가 id와 같은걸 쿼리로 가져옴
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.arrUserWoman_Hot.add(stRecvData);
                                mMyData.arrUserAll_Hot.add(stRecvData);
                                Log.d("Login Woman_Hot : ", mMyData.arrUserWoman_Hot.get(i).NickName);
                            }
                        }


                        if(nUserSet != 8)
                            nUserSet += 1;
                        if(nUserSet == 8 && bMySet == true){
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

        DatabaseReference refMan, refWoman;
        refMan = FirebaseDatabase.getInstance().getReference().child("Users").child("남자");
        Query query=refMan.orderByChild("Date").startAt(Integer.toString(nStartDate)).endAt(Integer.toString(nTodayDate));
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.arrUserMan_New.add(stRecvData);
                                mMyData.arrUserAll_New.add(stRecvData);
                                Log.d("Login arrUserMan_New : ", mMyData.arrUserMan_New.get(i).NickName);
                            }
                            i++;
                        }


                        if(nUserSet != 8)
                            nUserSet += 1;
                        if(nUserSet == 8 && bMySet == true){
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

        refWoman = FirebaseDatabase.getInstance().getReference().child("Users").child("여자");
        query=refWoman.orderByChild("Date").startAt(Integer.toString(nStartDate)).endAt(Integer.toString(nTodayDate));
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.arrUserWoman_New.add(stRecvData);
                                mMyData.arrUserAll_New.add(stRecvData);
                                Log.d("Login Woman_New : ", mMyData.arrUserWoman_New.get(i).NickName);
                            }
                        }


                        if(nUserSet != 8)
                            nUserSet += 1;
                        if(nUserSet == 8 && bMySet == true){
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

        DatabaseReference refMan, refWoman;
        refMan = FirebaseDatabase.getInstance().getReference().child("Users").child("남자");
        Query query=refMan
                .orderByChild("Lon")
                .startAt(lStartLon).endAt(lEndLon);


        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.arrUserMan_Near.add(stRecvData);
                                mMyData.arrUserAll_Near.add(stRecvData);
                                Log.d("Login Man_Near : ", mMyData.arrUserMan_Near.get(i).NickName);
                            }
                            i++;
                        }


                        if(nUserSet != 8)
                            nUserSet += 1;
                        if(nUserSet == 8 && bMySet == true){
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

        refWoman = FirebaseDatabase.getInstance().getReference().child("Users").child("여자");
        query=refWoman
                .orderByChild("Lon")
                .startAt(lStartLon).endAt(lEndLon);
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            UserData stRecvData = new UserData ();
                            stRecvData = fileSnapshot.getValue(UserData.class);
                            if(stRecvData != null) {
                                mMyData.arrUserWoman_Near.add(stRecvData);
                                mMyData.arrUserAll_Near.add(stRecvData);
                                Log.d("Login Woman_Near : ", mMyData.arrUserWoman_Near.get(i).NickName);
                            }
                        }


                        if(nUserSet != 8)
                            nUserSet += 1;
                        if(nUserSet == 8 && bMySet == true){
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

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                mProfileImage.setImageBitmap(scaled);

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
        mMyData.setUserImg(uri.toString());
    }

    private void SetBoardMyData() {
        DatabaseReference refMyBoard;
        refMyBoard = FirebaseDatabase.getInstance().getReference().child("Board");

        refMyBoard.orderByChild("Idx").equalTo(mMyData.getUserIdx()).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TempBoardData stRecvData = new TempBoardData();
                stRecvData = dataSnapshot.getValue(TempBoardData.class);
                if (stRecvData != null) {
                    if (stRecvData != null) {
                        mBoardData.arrBoardMyList.add(stRecvData);
                    }
                }
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
                TempBoardData stRecvData = new TempBoardData();
                stRecvData = dataSnapshot.getValue(TempBoardData.class);
                if (stRecvData != null) {
                    mBoardData.arrBoardList.add(stRecvData);
                }
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
