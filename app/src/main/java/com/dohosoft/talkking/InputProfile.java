package com.dohosoft.talkking;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dohosoft.talkking.Data.SettingData;
import com.dohosoft.talkking.Data.UserData;
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
import com.dohosoft.talkking.Data.BoardData;
import com.dohosoft.talkking.Data.CoomonValueData;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.LocationFunc;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import gun0912.tedbottompicker.TedBottomPicker;

import static com.dohosoft.talkking.Data.CoomonValueData.DEF_LAT;
import static com.dohosoft.talkking.Data.CoomonValueData.DEF_LON;
import static com.dohosoft.talkking.Data.CoomonValueData.FIRST_LOAD_MAIN_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.GENDER_MAN;
import static com.dohosoft.talkking.Data.CoomonValueData.GENDER_WOMAN;
import static com.dohosoft.talkking.Data.CoomonValueData.HOTMEMBER_LOAD_MAIN_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.LOAD_MAIN_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;
import static com.dohosoft.talkking.Data.CoomonValueData.REF_LAT;
import static com.dohosoft.talkking.Data.CoomonValueData.REF_LON;
import static com.dohosoft.talkking.Data.CoomonValueData.REWARD_NEW;
import static com.dohosoft.talkking.Data.CoomonValueData.bMyLoc;
import static com.dohosoft.talkking.Data.CoomonValueData.bMySet;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetHot;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetNear;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetNew;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetRecv;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetRich;
import static com.dohosoft.talkking.MyProfileActivity.calculateInSampleSize;

public class InputProfile extends AppCompatActivity {

    private MyData mMyData = MyData.getInstance();
    private LocationFunc mLocalFunc = LocationFunc.getInstance();
    private BoardData mBoardData = BoardData.getInstance();
    private FirebaseData mFireBaseData = FirebaseData.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl("gs://talkking-25dd8.appspot.com/");

    private Activity mActivity;

    private ImageView mProfileImage;
    private EditText mNickName, mMemo;

    private Spinner AgeSpinner;
    private Spinner GenderSpinner;
    private int nAge1, nGender;

    private Button CheckBtn;

    private CheckBox CbAccess, CbLoc;
    private Button BtnAccess;
    private Button BtnLoc;
    private Button BtnPrivacy;

    private FusedLocationProviderClient mFusedLocationClient;

    LocationManager locationManager;
    String provider;

    private boolean bMySet, bMyThumb, bMyImg, bMyLoc = false;

    private int nUserSet = 0;
    private static String TAG = "InputActivity Log!!";
    private String strIdx;

    private Uri tempImgUri;
    private boolean bClickSave = false;

    private int bRejoin = 0;

    public class PrePareHot extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            DatabaseReference ref;


            if (mMyData.getUserGender().equals("여자")) {
                ref = FirebaseDatabase.getInstance().getReference().child("HotMemberIdx").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("HotMemberIdx").child("Woman");
            }

            Query query = ref.limitToLast(HOTMEMBER_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                String tempValue = fileSnapshot.getValue(String.class);

                                if (tempValue != null || !tempValue.equals("")) {
                                    i++;
                                    final DatabaseReference Hotref;
                                    if (mMyData.getUserGender().equals("여자")) {
                                        Hotref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man").child(tempValue);
                                    } else {
                                        Hotref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman").child(tempValue);
                                    }


                                    final int finalI = i;
                                    Hotref.addListenerForSingleValueEvent(
                                            new ValueEventListener() {

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    UserData stRecvData = new UserData();
                                                    stRecvData = dataSnapshot.getValue(UserData.class);
                                                    if (stRecvData != null) {
                                                        if (stRecvData != null && stRecvData.Idx != null) {
                                                            if (CommonFunc.getInstance().CheckUserData(stRecvData, dataSnapshot.getKey())) {
                                                                if (stRecvData.Img == null)
                                                                    stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                                                mMyData.arrUserAll_Hot.add(stRecvData);

                                                                mMyData.mapGenderData.put(stRecvData.Idx, stRecvData.Gender);

                                                                if (mMyData.arrUserAll_Hot.size() == HOTMEMBER_LOAD_MAIN_COUNT) {


                                                                    CommonFunc.AscendingObj ascending = new CommonFunc.AscendingObj();

                                                                    Collections.sort(mMyData.arrUserAll_Hot, ascending);

                                                                    mMyData.arrUserAll_Hot_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Hot, mMyData.nStartAge, mMyData.nEndAge);

                                                                    if (mMyData.arrUserAll_Hot.size() > 0)
                                                                        mMyData.HotIndexRef = mMyData.arrUserAll_Hot.get(mMyData.arrUserAll_Hot.size() - 1).Date;

                                                                    bSetHot = true;
                                                                    if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
                                                                        GoMainPage();
                                                                        finish();
                                                                    }

                                                                }

                                                            }
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }
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
            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
                GoMainPage();
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }


    public class PrePareRecv extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            DatabaseReference ref;

            if (mMyData.getUserGender().equals("여자")) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            Query query = ref.orderByChild("RecvGold").limitToFirst(FIRST_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData cTempData = new UserData();
                                cTempData = fileSnapshot.getValue(UserData.class);
                                if (cTempData != null && cTempData.Idx != null) {

                                    if (CommonFunc.getInstance().CheckUserData(cTempData, fileSnapshot.getKey())) {
                                        if (cTempData.Img == null)
                                            cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_Recv.add(cTempData);
                                        mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);
                           /*             if(mMyData.arrUserAll_Recv.get(i).Gender.equals("여자"))
                                        {
                                            mMyData.arrUserWoman_Recv.add(cTempData);
                                        }
                                        else {
                                            mMyData.arrUserMan_Recv.add(cTempData);
                                        }*/

                                        i++;
                                    }


                                }
                            }


                            mMyData.arrUserAll_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Recv, mMyData.nStartAge, mMyData.nEndAge);
                  /*          mMyData.arrUserWoman_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserWoman_Recv, mMyData.nStartAge, mMyData.nEndAge );
                            mMyData.arrUserMan_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserMan_Recv, mMyData.nStartAge, mMyData.nEndAge );
*/
                            if (mMyData.arrUserAll_Recv.size() > 0)
                                mMyData.RecvIndexRef = mMyData.arrUserAll_Recv.get(mMyData.arrUserAll_Recv.size() - 1).RecvGold;

                            bSetRecv = true;
                            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
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
            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
                GoMainPage();
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }

    public class PrePareFan extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... voids) {
            DatabaseReference ref;
            if (mMyData.getUserGender().equals("여자")) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }
            Query query = ref.orderByChild("FanCount").limitToFirst(FIRST_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData cTempData = new UserData();
                                cTempData = fileSnapshot.getValue(UserData.class);
                                if (cTempData != null && cTempData.Idx != null) {
                                    if (CommonFunc.getInstance().CheckUserData(cTempData, fileSnapshot.getKey())) {
                                        if (cTempData.Img == null)
                                            cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_Send.add(cTempData);
                                        mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);

                               /*         if (mMyData.arrUserAll_Send.get(i).Gender.equals("여자")) {
                                            mMyData.arrUserWoman_Send.add(mMyData.arrUserAll_Send.get(i));
                                        } else {
                                            mMyData.arrUserMan_Send.add(mMyData.arrUserAll_Send.get(i));
                                        }*/


                                        i++;
                                    }


                                }
                            }

                            mMyData.arrUserAll_Send_Age = mMyData.SortData_Age(mMyData.arrUserAll_Send, mMyData.nStartAge, mMyData.nEndAge);
                /*            mMyData.arrUserWoman_Send_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Send, mMyData.nStartAge, mMyData.nEndAge);
                            mMyData.arrUserMan_Send_Age = mMyData.SortData_Age(mMyData.arrUserMan_Send, mMyData.nStartAge, mMyData.nEndAge);
*/
                            bSetRich = true;

                            if (mMyData.arrUserAll_Send.size() > 0)
                                mMyData.FanCountRef = mMyData.arrUserAll_Send.get(mMyData.arrUserAll_Send.size() - 1).FanCount;

                            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
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
            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
                GoMainPage();
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public class PrePareNear extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Integer doInBackground(Integer... voids) {

            Double lStartLon = mMyData.getUserLon() - 0.1;
            Double lStartLat = mMyData.getUserLat() - 0.1;

            Double lEndLon = mMyData.getUserLon() + 0.1;
            Double IEndLat = mMyData.getUserLon() + 0.1;

            DatabaseReference ref;
            if (mMyData.getUserGender().equals("여자")) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            Query query = ref
                    .orderByChild("Dist").startAt(0).limitToFirst(FIRST_LOAD_MAIN_COUNT);

            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0, j = 0, k = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData stRecvData = new UserData();
                                stRecvData = fileSnapshot.getValue(UserData.class);
                                if (stRecvData != null) {

                                    if (stRecvData.Lat == 0 || stRecvData.Lon == 0) {
                                        // 위치 못받아오는 애들
                                    } else {
                                        if (CommonFunc.getInstance().CheckUserData(stRecvData, fileSnapshot.getKey())) {
                                            if (stRecvData.Img == null)
                                                stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                            double Dist = LocationFunc.getInstance().getDistance(mMyData.getUserLat(), mMyData.getUserLon(), stRecvData.Lat, stRecvData.Lon, "meter");

                                            stRecvData.Dist = Dist;
                                            mMyData.arrUserAll_Near.add(stRecvData);
                                            mMyData.mapGenderData.put(stRecvData.Idx, stRecvData.Gender);
                                /*            if (mMyData.arrUserAll_Near.get(i).Gender.equals("여자")) {
                                                mMyData.arrUserWoman_Near.add(mMyData.arrUserAll_Near.get(i));
                                            } else {
                                                mMyData.arrUserMan_Near.add(mMyData.arrUserAll_Near.get(i));
                                            }*/
                                            i++;
                                        }
                                    }
                                }
                            }


                            CommonFunc.NearSort cNearSort = new CommonFunc.NearSort();
                            Collections.sort(mMyData.arrUserAll_Near, cNearSort);
/*                            Collections.sort(mMyData.arrUserWoman_Near, cNearSort);
                            Collections.sort(mMyData.arrUserMan_Near, cNearSort);*/

                            mMyData.arrUserAll_Near_Age = mMyData.SortData_Age(mMyData.arrUserAll_Near, mMyData.nStartAge, mMyData.nEndAge);
  /*                          mMyData.arrUserWoman_Near_Age = mMyData.SortData_Age(mMyData.arrUserWoman_Near, mMyData.nStartAge, mMyData.nEndAge);
                            mMyData.arrUserMan_Near_Age = mMyData.SortData_Age(mMyData.arrUserMan_Near, mMyData.nStartAge, mMyData.nEndAge);*/

                            bSetNear = true;

                            if (mMyData.arrUserAll_Near.size() > 0)
                                mMyData.NearDistanceRef = mMyData.arrUserAll_Near.get(mMyData.arrUserAll_Near.size() - 1).Dist;

                            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
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
            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
                GoMainPage();
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public class PrePareNew extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Integer doInBackground(Integer... voids) {
            long time = CommonFunc.getInstance().GetCurrentTime();
            SimpleDateFormat ctime = new SimpleDateFormat("yyyyMMdd");
            int nTodayDate = Integer.parseInt(ctime.format(new Date(time)));
            int nStartDate = nTodayDate - 7;

            DatabaseReference ref;
            if (mMyData.getUserGender().equals("여자")) {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            } else {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }
            Query query = ref.orderByChild("Date").limitToFirst(FIRST_LOAD_MAIN_COUNT);

            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0, j = 0, k = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData stRecvData = new UserData();
                                stRecvData = fileSnapshot.getValue(UserData.class);
                                if (stRecvData != null) {
                                    if (CommonFunc.getInstance().CheckUserData(stRecvData, fileSnapshot.getKey())) {
                                        if (stRecvData.Img == null)
                                            stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_New.add(stRecvData);
                                        mMyData.mapGenderData.put(stRecvData.Idx, stRecvData.Gender);
                             /*           if (mMyData.arrUserAll_New.get(i).Gender.equals("여자")) {
                                            mMyData.arrUserWoman_New.add(mMyData.arrUserAll_New.get(i));
                                        } else {
                                            mMyData.arrUserMan_New.add(mMyData.arrUserAll_New.get(i));
                                        }*/
                                        i++;
                                    }
                                }
                            }

                            mMyData.arrUserAll_New_Age = mMyData.SortData_Age(mMyData.arrUserAll_New, mMyData.nStartAge, mMyData.nEndAge);
/*                            mMyData.arrUserWoman_New_Age = mMyData.SortData_Age(mMyData.arrUserWoman_New, mMyData.nStartAge, mMyData.nEndAge);
                            mMyData.arrUserMan_New_Age = mMyData.SortData_Age(mMyData.arrUserMan_New, mMyData.nStartAge, mMyData.nEndAge);*/

                            if (mMyData.arrUserAll_New.size() > 0)
                                mMyData.NewDateRef = mMyData.arrUserAll_New.get(mMyData.arrUserAll_New.size() - 1).Date;

                            bSetNew = true;
                            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
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
            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
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


        mActivity = this;

        final Bundle bundle = getIntent().getExtras();
        strIdx = (String) bundle.getSerializable("Idx");
        bRejoin = (int) bundle.getSerializable("ReJoin");


        mMyData.setUserIdx(strIdx);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

  /*      PermissionListener permissionlistener = new_img PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        new_img TedPermission(this)
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

                                tempImgUri = uri;

                            }
                        })
                        .create();

                bottomSheetDialogFragment.show(getSupportFragmentManager());

            }
        });

        mNickName = (EditText) findViewById(R.id.InputProfile_NickName);
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //mMemo = (EditText) findViewById(R.id.InputProfile_Memo);
        ConstraintLayout ll = (ConstraintLayout) findViewById(R.id.linearLayout);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(mMemo.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mNickName.getWindowToken(), 0);
            }
        });


        AgeSpinner = (Spinner) findViewById(R.id.InputProfile_Age_1);
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
        GenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String strGender = GENDER_WOMAN;

                if (position == 0)
                    strGender = GENDER_MAN;


                mMyData.setUserGender(strGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        BtnAccess = (Button) findViewById(R.id.btn_access);
        BtnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccessActivity.class);
                startActivity(intent);
            }

        });

        BtnLoc = (Button) findViewById(R.id.btn_loc);
        BtnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LocActivity.class);
                startActivity(intent);
            }

        });

        BtnPrivacy = (Button) findViewById(R.id.btn_privacy);
        BtnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), privacyActivity.class);
                startActivity(intent);
            }

        });

        CbAccess = (CheckBox) findViewById(R.id.cb_access);
        CbLoc = (CheckBox) findViewById(R.id.cb_loc);


        CheckBtn = (Button) findViewById(R.id.InputProfile_Check);
        CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bClickSave == true)
                    return;

                String strNickName = mNickName.getText().toString();
                strNickName = CommonFunc.getInstance().RemoveEmptyString(strNickName);

                //String strMemo = mMemo.getText().toString();


                String strImg = mMyData.getUserImg();


                if (CommonFunc.getInstance().CheckTextMaxLength(strNickName, CoomonValueData.TEXT_MAX_LENGTH_NICKNAME, InputProfile.this, "닉네임", true) == false)
                    return;

                if ("".equals(strNickName)) {
                    CommonFunc.getInstance().ShowToast(InputProfile.this, "이름을 입력 해주세요", true);
                    return;
                }
                //if ("null".equals(strImg)) {
                if (strImg == null) {
                    CommonFunc.getInstance().ShowToast(InputProfile.this, "사진을 입력 해주세요", true);
                    return;
                }

                if (!CbAccess.isChecked()) {
                    CommonFunc.getInstance().ShowToast(InputProfile.this, "이용약관에 동의해주세요", true);
                    return;
                }
                if (!CbLoc.isChecked()) {
                    CommonFunc.getInstance().ShowToast(InputProfile.this, "위치정보 이용에 동의해주세요", true);
                    return;
                } else {

                    bClickSave = true;
                    CommonFunc.getInstance().ShowLoadingPage(InputProfile.this, "환영합니다");

                    UploadThumbNailImage_Firebase(tempImgUri);
                    UploadImage_Firebase(tempImgUri);


                    mMyData.setUserDate();
                    mMyData.nStartAge = 20;//(Integer.parseInt(mMyData.getUserAge()) / 10) * 10;
                    mMyData.nEndAge = (Integer.parseInt(mMyData.getUserAge()) + 19);
                    mMyData.setUserNick(strNickName);
                    //mMyData.setUserMemo(strMemo);

                    mMyData.getNotice();
                    mMyData.getLogin();
                    mMyData.getSignUp();

                    mMyData.getDownUrl(mActivity);
                    mMyData.getImageLoading(mActivity);
                    mMyData.getBoardLoadingDate(mActivity);

                    //mMyData.getCardList(mActivity);
                    mMyData.getFanList(mActivity);
                    mMyData.getVersion();
                    mMyData.getNotification();

                    mMyData.getReportedCnt();
                    mMyData.getSetting();

                    mMyData.getSendList(mActivity);
                    mMyData.getSendHoneyList();
                    mMyData.getGiftHoneyList();
                    mMyData.getRecvHoneyList();
                    mMyData.getBlockList();
                    mMyData.getBlockedList();

                    bMySet = true;

                    PrePareHot initHot = new PrePareHot();
                    initHot.execute(0, 0, 0);

                    PrePareRecv initRecv = new PrePareRecv();
                    initRecv.execute(0, 0, 0);


              /*      PrePareFan initFan = new PrePareFan();
                    initFan.execute(0, 0, 0);*/

                    PrePareNear initNear = new PrePareNear();
                    initNear.execute(0, 0, 0);

                    PrePareNew initNew = new PrePareNew();
                    initNew.execute(0, 0, 0);


              /*      InitData_Hot();
                    InitData_New();
                    InitData_FanCount();
                    InitData_Near();*/
                    /*Intent intent = new_img Intent(InputProfile.this, MainActivity.class);
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
        double tempLon, tempLat;

        if (location != null) {

            if (location.getLongitude() == 0) {
                tempLon = Math.random() * (1.072271) + 126.611512;
                ;
            } else {
                tempLon = location.getLongitude();
            }
            if (location.getLatitude() == 0) {
                tempLat = Math.random() * (0.836468) + 37.077091;
            } else {
                tempLat = location.getLatitude();
            }

            mMyData.setUserLon(tempLon);
            mMyData.setUserLat(tempLat);
            mMyData.setUserDist(LocationFunc.getInstance().getDistance(mMyData.getUserLat(), mMyData.getUserLon(), REF_LAT, REF_LON, "meter"));
        } else {
            tempLon = Math.random() * (1.072271) + 126.611512;
            tempLat = Math.random() * (0.836468) + 37.077091;


            mMyData.setUserLon(tempLon);
            mMyData.setUserLat(tempLat);
            mMyData.setUserDist(LocationFunc.getInstance().getDistance(mMyData.getUserLat(), mMyData.getUserLon(), REF_LAT, REF_LON, "meter"));
        }

        if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
            GoMainPage();
            finish();
        }
    }


    private void UploadThumbNailImage_Firebase(Uri file) {

        StorageReference riversRef = storageRef.child("images/" + mMyData.getUserIdx() + "/" + "ThumbNail");//file.getLastPathSegment());

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
            bitmap = ExifUtils.rotateBitmap(file.getPath(), bitmap);
        } catch (Exception e) {
            FirebaseData.getInstance().DelUser(mMyData.ANDROID_ID, mMyData.getUserIdx());

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            currentUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast toast = Toast.makeText(getApplicationContext(), "잘못된 이미지 입니다", Toast.LENGTH_SHORT);
                                mMyData.Clear();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        }
                    });
        }

        if (bitmap == null) {

            FirebaseData.getInstance().DelUser(mMyData.ANDROID_ID, mMyData.getUserIdx());

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            currentUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast toast = Toast.makeText(getApplicationContext(), "잘못된 이미지 입니다", Toast.LENGTH_SHORT);
                                mMyData.Clear();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        }
                    });
        } else {
            if (bitmap.getWidth() * bitmap.getHeight() * 4 / 1024 >= 100) {
                options.inSampleSize = calculateInSampleSize(options, 512, 512, 2);
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
                    bitmap = ExifUtils.rotateBitmap(file.getPath(), bitmap);
                } catch (Exception e) {
                }
            } else {
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
                    bitmap = ExifUtils.rotateBitmap(file.getPath(), bitmap);
                } catch (Exception e) {
                }
            }

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


    }

    private void UploadImage_Firebase(Uri file) {

        StorageReference riversRef = storageRef.child("images/" + mMyData.getUserIdx() + "/" + 0);//file.getLastPathSegment());

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
            bitmap = ExifUtils.rotateBitmap(file.getPath(), bitmap);
        } catch (Exception e) {
        }

        if (bitmap.getWidth() * bitmap.getHeight() * 4 / 1024 >= 10000) {
            options.inSampleSize = calculateInSampleSize(options, 1024, 1024, 2);
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
                bitmap = ExifUtils.rotateBitmap(file.getPath(), bitmap);
            } catch (Exception e) {
            }
        } else {
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(file), null, options);
                bitmap = ExifUtils.rotateBitmap(file.getPath(), bitmap);
            } catch (Exception e) {
            }
        }

        if (bitmap == null) {

            FirebaseData.getInstance().DelUser(mMyData.ANDROID_ID, mMyData.getUserIdx());

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            currentUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast toast = Toast.makeText(getApplicationContext(), "잘못된 이미지 입니다", Toast.LENGTH_SHORT);
                                mMyData.Clear();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        }
                    });
        } else {
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
                    CommonFunc.getInstance().DismissLoadingPage();
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Tr(downloadUrl);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    CommonFunc.getInstance().ShowLoadingPage(InputProfile.this, "로딩중");
                    System.out.println("Upload is " + progress + "% done");
                }
            });
        }

    }

    public void TrThumbNail(Uri uri) {
        mMyData.setUserImg(uri.toString());
        mMyData.setUserImgCnt(1);
        bMyThumb = true;

        if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
            GoMainPage();
            finish();
        }

    }

    public void Tr(Uri uri) {
        mMyData.setUserProfileImg(mMyData.nSaveUri, uri.toString());
        mMyData.setUserImgCnt(1);
        bMyImg = true;

        if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyImg == true && bMyThumb == true && bMyLoc == true) {
            GoMainPage();
            finish();
        }

    }

    private void GoMainPage() {


        bSetHot = false;
        bSetNear = false;
        bSetNew = false;
        //bSetRich = false;
        bSetRecv = false;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("UserIdx");
        final DatabaseReference UserIdx = table.child(mMyData.ANDROID_ID);
        UserIdx.setValue(strIdx);

        if (bRejoin == 0) {
            mMyData.setUserHoney(REWARD_NEW);
            mMyData.setPoint(REWARD_NEW);
            mMyData.Rejoin = 0;
        }


        mFireBaseData.SaveFirstMyData(mMyData.getUserIdx());

        //mFireBaseData.SaveUsersMyData(mMyData.getUserIdx());
        mMyData.mapGenderData.put(mMyData.getUserIdx(), mMyData.getUserGender());

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyData", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Gender", mMyData.getUserGender());
        editor.commit();


        mMyData.getRecvGold();
        mFireBaseData.GetInitBoardData();
        mFireBaseData.GetInitMyBoardData();
        mCommon.refreshMainActivity(this, MAIN_ACTIVITY_HOME, 0, 1);
        finish();
        /*Intent intent = new_img Intent(InputProfile.this, MainActivity.class);
        intent.putExtra("StartFragment", 0);
        startActivity(intent);
        finish();*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            String alertTitle = "종료";
            View v = LayoutInflater.from(this).inflate(R.layout.dialog_exit_app, null, false);

            final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

            final Button btn_exit;
            final Button btn_no;
            final TextView title;
            final AdView mAdView;

            title = (TextView) v.findViewById(R.id.title);
            title.setVisibility(View.GONE);


            btn_exit = (Button) v.findViewById(R.id.btn_yes);
            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  FirebaseData.getInstance().DelUser(mMyData.ANDROID_ID, mMyData.getUserIdx());

                    // moveTaskToBack(true);
                    finish();
                    System.exit(0);
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);

/*                    FirebaseUser currentUser =  FirebaseAuth.getInstance().getCurrentUser();
                    currentUser.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User account deleted.");
                                        int pid = android.os.Process.myPid(); android.os.Process.killProcess(pid);
                                    }
                                }
                            });*/

                }
            });

            btn_no = (Button) v.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            mAdView = (AdView) v.findViewById(R.id.adView);
            mAdView.setVisibility(View.GONE);
            //CommonFunc.getInstance().ViewAdsBanner(mAdView);

        }
        return true;
    }
}
