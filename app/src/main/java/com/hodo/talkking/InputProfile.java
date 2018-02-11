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
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Firebase.FirebaseData;
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

import static com.hodo.talkking.Data.CoomonValueData.FIRST_LOAD_MAIN_COUNT;
import static com.hodo.talkking.Data.CoomonValueData.GENDER_MAN;
import static com.hodo.talkking.Data.CoomonValueData.GENDER_WOMAN;
import static com.hodo.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;

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

    private ProgressBar progressBar;
    private FusedLocationProviderClient mFusedLocationClient;

    LocationManager locationManager;
    String provider;

    private boolean bMySet = false;
    private boolean bSetNear, bSetNew, bSetRich, bSetRecv = false;

    private int nUserSet = 0;
    private static String TAG = "InputActivity Log!!";

    private View mProgressView;

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);





            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);


        }
    }


    public class PrePareHot extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

                            bSetRecv = true;
                            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
                                showProgress(false);
                                Log.d(TAG, "Account Log in  Complete");
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
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
                showProgress(false);
                Log.d(TAG, "Account Log in  Complete");
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

                            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
                                showProgress(false);
                                Log.d(TAG, "Account Log in  Complete");
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
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
                showProgress(false);
                Log.d(TAG, "Account Log in  Complete");
                GoMainPage();

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
        }

        @Override
        protected Integer doInBackground(Integer... voids) {

            Double lStartLon = mMyData.getUserLon() - 10;
            Double lStartLat = mMyData.getUserLat() - 10;

            Double lEndLon = mMyData.getUserLon() + 10;
            Double IEndLat = mMyData.getUserLon() + 10;

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

                            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
                                showProgress(false);
                                Log.d(TAG, "Account Log in  Complete");
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
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
                showProgress(false);
                Log.d(TAG, "Account Log in  Complete");
                GoMainPage();
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
                            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
                                showProgress(false);
                                Log.d(TAG, "Account Log in  Complete");
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
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
                showProgress(false);
                Log.d(TAG, "Account Log in  Complete");
                GoMainPage();
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

        PermissionListener permissionlistener = new PermissionListener() {
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
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissions(android.Manifest.permission.READ_CONTACTS)
                .check();

        //mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.InputProfile_Progress);

        progressBar = (ProgressBar)findViewById(R.id.InputProfile_Progress);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mProfileImage = (ImageView) findViewById(R.id.InputProfile_SumImg);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InputProfile.this, "이미지 등록", Toast.LENGTH_SHORT).show();
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,1000);

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
                    mMyData.nStartAge = (Integer.parseInt(mMyData.getUserAge()) / 10) * 10;
                    mMyData.nEndAge = mMyData.nStartAge + 19;
                    mMyData.setUserNick(strNickName);
                    mFireBaseData.SaveData(mMyData.getUserIdx());
                    bMySet = true;

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
            if (resultCode == RESULT_OK && requestCode == 1000 ){
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

        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(file, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        bitmap = ExifUtils.rotateBitmap(imagePath,bitmap);
        cursor.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.createScaledBitmap(bitmap, 350, 350, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);
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
    }

    public void Tr(Uri uri)
    {
        mMyData.setUserImg(uri.toString());
        mMyData.setUserProfileImg(0, uri.toString());
        mMyData.setUserImgCnt(1);

        Toast.makeText(this," 사진이 저장되었습니다",Toast.LENGTH_LONG).show();
    }

    private void GoMainPage() {
        mFireBaseData.GetInitBoardData();
        mFireBaseData.GetInitMyBoardData();
        mCommon.refreshMainActivity(this, MAIN_ACTIVITY_HOME);
        finish();
        /*Intent intent = new Intent(InputProfile.this, MainActivity.class);
        intent.putExtra("StartFragment", 0);
        startActivity(intent);
        finish();*/
    }

}
