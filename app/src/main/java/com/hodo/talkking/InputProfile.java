package com.hodo.talkking;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hodo.talkking.Data.BoardData;
import com.hodo.talkking.Data.CoomonValueData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.AwsFunc;
import com.hodo.talkking.Util.CommonFunc;
import com.hodo.talkking.Util.LocationFunc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gun0912.tedbottompicker.TedBottomPicker;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.hodo.talkking.Data.CoomonValueData.FIRST_LOAD_MAIN_COUNT;
import static com.hodo.talkking.Data.CoomonValueData.GENDER_MAN;
import static com.hodo.talkking.Data.CoomonValueData.GENDER_WOMAN;
import static com.hodo.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;
import static com.hodo.talkking.MyProfileActivity.calculateInSampleSize;

public class InputProfile extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private LocationFunc mLocalFunc = LocationFunc.getInstance();
    private BoardData mBoardData = BoardData.getInstance();
    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl("gs://talkking-2aa18.appspot.com/");

    private ImageView mProfileImage;
    private EditText mNickName;

    private Spinner AgeSpinner;
    private Spinner GenderSpinner;
    private  int nAge1, nGender;

    private Button CheckBtn;

    private FusedLocationProviderClient mFusedLocationClient;

    LocationManager locationManager;
    String provider;

    private boolean bMySet, bMyThumb, bMyImg, bMyLoc = false;
    private boolean bSetNear, bSetNew, bSetRich, bSetRecv = false;

    private int nUserSet = 0;
    private static String TAG = "InputActivity Log!!";

    ProgressBar progressBar;

    private  String strIdx;

    public class PrePareHot extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            DatabaseReference ref;
            ref = FirebaseDatabase.getInstance().getReference().child("HotMember");
            Query query=ref.orderByChild("Point").limitToFirst(FIRST_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴;//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren())
                            {
                                SimpleUserData cTempData = new SimpleUserData();
                                cTempData = fileSnapshot.getValue(SimpleUserData.class);
                                if(cTempData != null) {

                                    if (!cTempData.Idx.equals(mMyData.getUserIdx()))
                                    {
                                        if(cTempData.Img == null)
                                            cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        double Dist = LocationFunc.getInstance().getDistance(mMyData.getUserLat(), mMyData.getUserLon(), cTempData.Lat, cTempData.Lon,"kilometer");
                                        if(Dist <= 10)
                                        {
                                            mMyData.arrUserAll_Recv.add(cTempData);

                                            if(mMyData.arrUserAll_Recv.get(i).Gender.equals("여자"))
                                            {
                                                mMyData.arrUserWoman_Recv.add(cTempData);
                                            }
                                            else {
                                                mMyData.arrUserMan_Recv.add(cTempData);
                                            }

                                            mMyData.arrUserAll_Recv_Age = mMyData.SortData_Age(mMyData.arrUserAll_Recv, mMyData.nStartAge, mMyData.nEndAge );
                                            mMyData.arrUserWoman_Recv_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Recv, mMyData.nStartAge, mMyData.nEndAge );
                                            mMyData.arrUserMan_Recv_Age = mMyData.SortData_Age(mMyData.arrUserMan_Recv, mMyData.nStartAge, mMyData.nEndAge );
                                            i++;
                                        }

                                    }
                                }
                            }

                            bSetRecv = true;
                            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true){
                                GoMainPage();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true){
                GoMainPage();
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }

    public class PrePareFan extends AsyncTask<Integer, Integer, Integer>  {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... voids) {
            DatabaseReference ref;
            ref = FirebaseDatabase.getInstance().getReference().child("SimpleData");
            Query query= ref.orderByChild("FanCount").limitToFirst(FIRST_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                SimpleUserData cTempData = new SimpleUserData();
                                cTempData = fileSnapshot.getValue(SimpleUserData.class);
                                if(cTempData != null) {
                                    if (!cTempData.Idx.equals(mMyData.getUserIdx()))  {
                                        if (cTempData.Img == null)
                                            cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_Send.add(cTempData);
                                        if (mMyData.arrUserAll_Send.get(i).Gender.equals("여자")) {
                                            mMyData.arrUserWoman_Send.add(mMyData.arrUserAll_Send.get(i));
                                        } else {
                                            mMyData.arrUserMan_Send.add(mMyData.arrUserAll_Send.get(i));
                                        }

                                        mMyData.arrUserAll_Send_Age = mMyData.SortData_Age(mMyData.arrUserAll_Send, mMyData.nStartAge, mMyData.nEndAge);
                                        mMyData.arrUserWoman_Send_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Send, mMyData.nStartAge, mMyData.nEndAge);
                                        mMyData.arrUserMan_Send_Age = mMyData.SortData_Age(mMyData.arrUserMan_Send, mMyData.nStartAge, mMyData.nEndAge);
                                        i++;
                                    }
                                }
                            }

                            bSetRich = true;

                            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true){
                                GoMainPage();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true){
                GoMainPage();
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public class PrePareNear extends AsyncTask<Integer, Integer, Integer>  {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }


        @Override
        protected Integer doInBackground(Integer... voids) {

            Double lStartLon = mMyData.getUserLon() - 0.1;
            Double lStartLat = mMyData.getUserLat() - 0.1;

            Double lEndLon = mMyData.getUserLon() + 0.1;
            Double IEndLat = mMyData.getUserLon() + 0.1;

            DatabaseReference ref;
            ref = FirebaseDatabase.getInstance().getReference().child("SimpleData");
            Query query=ref
                    .orderByChild("Lon")
                    .startAt(lStartLon).endAt(lEndLon).limitToFirst(FIRST_LOAD_MAIN_COUNT);
                    ;


            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0, j=0, k=0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                SimpleUserData stRecvData = new SimpleUserData ();
                                stRecvData = fileSnapshot.getValue(SimpleUserData.class);
                                if(stRecvData != null) {
                                    if (!stRecvData.Idx.equals(mMyData.getUserIdx()))  {
                                        if (stRecvData.Img == null)
                                            stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_Near.add(stRecvData);

                                        if (mMyData.arrUserAll_Near.get(i).Gender.equals("여자")) {
                                            mMyData.arrUserWoman_Near.add(mMyData.arrUserAll_Near.get(i));
                                        } else {
                                            mMyData.arrUserMan_Near.add(mMyData.arrUserAll_Near.get(i));
                                        }

                                        mMyData.arrUserAll_Near_Age = mMyData.SortData_Age(mMyData.arrUserAll_Near, mMyData.nStartAge, mMyData.nEndAge);
                                        mMyData.arrUserWoman_Near_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Near, mMyData.nStartAge, mMyData.nEndAge);
                                        mMyData.arrUserMan_Near_Age = mMyData.SortData_Age(mMyData.arrUserMan_Near, mMyData.nStartAge, mMyData.nEndAge);
                                        i++;
                                    }

                                }
                            }

                            bSetNear = true;

                            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true){
                                GoMainPage();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true){
                GoMainPage();
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public class PrePareNew extends AsyncTask<Integer, Integer, Integer>  {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }


        @Override
        protected Integer doInBackground(Integer... voids) {
            long time = CommonFunc.getInstance().GetCurrentTime();
            SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
            int nTodayDate =  Integer.parseInt(ctime.format(new Date(time)));
            int nStartDate = nTodayDate - 7;

            DatabaseReference ref;
            ref = FirebaseDatabase.getInstance().getReference().child("SimpleData");
            Query query=ref.orderByChild("Date").startAt(Integer.toString(nStartDate)).endAt(Integer.toString(nTodayDate)).limitToFirst(FIRST_LOAD_MAIN_COUNT);
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0, j=0, k=0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                SimpleUserData stRecvData = new SimpleUserData ();
                                stRecvData = fileSnapshot.getValue(SimpleUserData.class);
                                if(stRecvData != null) {
                                    if (!stRecvData.Idx.equals(mMyData.getUserIdx()))  {
                                        if (stRecvData.Img == null)
                                            stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_New.add(stRecvData);

                                        if (mMyData.arrUserAll_New.get(i).Gender.equals("여자")) {
                                            mMyData.arrUserWoman_New.add(mMyData.arrUserAll_New.get(i));
                                        } else {
                                            mMyData.arrUserMan_New.add(mMyData.arrUserAll_New.get(i));
                                        }

                                        mMyData.arrUserAll_New_Age = mMyData.SortData_Age(mMyData.arrUserAll_New, mMyData.nStartAge, mMyData.nEndAge);
                                        mMyData.arrUserWoman_New_Age = mMyData.SortData_Age(mMyData.arrUserWoman_New, mMyData.nStartAge, mMyData.nEndAge);
                                        mMyData.arrUserMan_New_Age = mMyData.SortData_Age(mMyData.arrUserMan_New, mMyData.nStartAge, mMyData.nEndAge);


                                        i++;
                                    }
                                }
                            }

                            bSetNew = true;
                            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true){
                                GoMainPage();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true){
                GoMainPage();
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_profile);

        final Bundle bundle = getIntent().getExtras();
        strIdx = (String) bundle.getSerializable("Idx");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("UserIdx");
        final DatabaseReference UserIdx = table.child(mMyData.ANDROID_ID);
        UserIdx.setValue(strIdx);
        mMyData.setUserIdx(strIdx);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        progressBar = (ProgressBar) findViewById(R.id.InputProfile_Progress) ;

  /*      PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("구글 로그인을 위해 연락처 접근 권한이 필요합니다")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_CONTACTS,  android.Manifest.permission.ACCESS_FINE_LOCATION)
                .check();*/




        mProfileImage = (ImageView) findViewById(R.id.InputProfile_SumImg);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(InputProfile.this)
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                // uri 활용
                                mMyData.setUserImg(uri.toString());

                                Glide.with(getApplicationContext())
                                        .load(mMyData.getUserImg())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .thumbnail(0.1f)
                                        .into(mProfileImage);

                                UploadThumbNailImage_Firebase(uri);
                                UploadImage_Firebase(uri);
                            }
                        })
                        .create();

                bottomSheetDialogFragment.show(getSupportFragmentManager());

            /*    Toast.makeText(InputProfile.this, "이미지 등록", Toast.LENGTH_SHORT).show();
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,1000);*/

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
                nAge1 += 20;
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
                String strGender = GENDER_WOMAN;

                if(position == 0)
                    strGender = GENDER_MAN;


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
                strNickName = CommonFunc.getInstance().RemoveEmptyString(strNickName);
                String strImg = mMyData.getUserImg();


                if(CommonFunc.getInstance().CheckTextMaxLength(strNickName, CoomonValueData.TEXT_MAX_LENGTH_NICKNAME, InputProfile.this ,"닉네임", true) == false)
                    return;

                if ("".equals(strNickName)) {
                    CommonFunc.getInstance().ShowToast(InputProfile.this, "이름을 입력 해주세요", true);
                }
                //if ("null".equals(strImg)) {
                if (strImg == null) {
                    CommonFunc.getInstance().ShowToast(InputProfile.this, "사진을 입력 해주세요", true);
                }
                else
                {
                    mMyData.setUserDate();
                    mMyData.nStartAge = (Integer.parseInt(mMyData.getUserAge()) / 10) * 10;
                    mMyData.nEndAge = mMyData.nStartAge + 19;
                    mMyData.setUserNick(strNickName);
                    mMyData.setUserHoney(50);


                    bMySet = true;

                    mMyData.getDownUrl();
                    mMyData.getImageLoading();

                    mMyData.getFanList();
                    mMyData.getReportedCnt();
                    mMyData.getSetting();

                    mMyData.getSendList();
                    mMyData.getSendHoneyList();
                    mMyData.getGiftHoneyList();
                    mMyData.getRecvHoneyList();
                    mMyData.getBlockList();
                    mMyData.getBlockedList();

                    PrePareHot initHot = new PrePareHot();
                    initHot.execute(0,0,0);

                    PrePareFan initFan = new PrePareFan();
                    initFan.execute(0,0,0);

                    PrePareNear initNear = new PrePareNear();
                    initNear.execute(0,0,0);

                    PrePareNew initNew = new PrePareNew();
                    initNew.execute(0,0,0);


              /*      InitData_Hot();
                    InitData_New();
                    InitData_FanCount();
                    InitData_Near();*/
                    /*Intent intent = new Intent(InputProfile.this, MainActivity.class);
                    startActivity(intent);*/
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

/*        if (mLocalFunc.checkLocationPermission(getApplicationContext(), this)) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }*/
    }


    public void getLocation() {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        Criteria criteria = new Criteria();
        provider = service.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        Location location = service.getLastKnownLocation(provider);
        bMyLoc = true;
        if(location != null)
        {
            mMyData.setUserLon(location.getLongitude());
            mMyData.setUserLat(location.getLatitude());
        }

        if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true){
            GoMainPage();
            finish();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == 1000 ){
                Uri uri = data.getData();

                mMyData.setUserImg(uri.toString());

                Glide.with(getApplicationContext())
                        .load(mMyData.getUserImg())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(mProfileImage);


                UploadThumbNailImage_Firebase(uri);
                UploadImage_Firebase(uri);

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            CommonFunc.getInstance().ShowToast(this, "Oops! 로딩에 오류가 있습니다.", false);
            e.printStackTrace();
        }

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

    private void UploadImage_Firebase(Uri file) {

        StorageReference riversRef = storageRef.child("images/"+ mMyData.getUserIdx() + "/" + 0 );//file.getLastPathSegment());

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
                progressBar.setVisibility(ProgressBar.GONE);
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Tr(downloadUrl);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setVisibility(ProgressBar.VISIBLE);
                progressBar.setProgress((int)progress) ;
                System.out.println("Upload is " + progress + "% done");
            }
        });
    }

    public void TrThumbNail(Uri uri)
    {
        mMyData.setUserImg(uri.toString());
        mMyData.setUserImgCnt(1);
        bMyThumb = true;
    }

    public void Tr(Uri uri)
    {
        mMyData.setUserProfileImg( mMyData.nSaveUri, uri.toString());
        mMyData.setUserImgCnt(1);
        bMyImg = true;

    }
    private void GoMainPage() {

        mFireBaseData.SaveFirstMyData(mMyData.getUserIdx());
        progressBar.setVisibility(ProgressBar.GONE);
        mFireBaseData.GetInitBoardData();
        mFireBaseData.GetInitMyBoardData();
        mCommon.refreshMainActivity(this, MAIN_ACTIVITY_HOME);
        finish();
        /*Intent intent = new Intent(InputProfile.this, MainActivity.class);
        intent.putExtra("StartFragment", 0);
        startActivity(intent);
        finish();*/
    }

    @Override
    public void onBackPressed(){


        String alertTitle = "종료";
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_exit_app,null,false);

        final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        final Button btn_exit;
        final Button btn_no;
        final TextView title;
        final AdView mAdView;

        title =  (TextView) v.findViewById(R.id.title);
        title.setVisibility(View.GONE);


        btn_exit = (Button) v.findViewById(R.id.btn_yes);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseData.getInstance().DelUser(mMyData.ANDROID_ID, mMyData.getUserIdx());

                FirebaseUser currentUser =  FirebaseAuth.getInstance().getCurrentUser();
                currentUser.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");
                                    int pid = android.os.Process.myPid(); android.os.Process.killProcess(pid);
                                }
                            }
                        });

            }
        });

        btn_no = (Button) v.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        mAdView = (AdView)v.findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_HOME){
            FirebaseData.getInstance().DelUser(mMyData.ANDROID_ID, mMyData.getUserIdx());

            FirebaseUser currentUser =  FirebaseAuth.getInstance().getCurrentUser();
            currentUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User account deleted.");
                                int pid = android.os.Process.myPid(); android.os.Process.killProcess(pid);
                            }
                        }
                    });

        }
        return true;
    }
}
