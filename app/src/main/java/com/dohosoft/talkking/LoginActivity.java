package com.dohosoft.talkking;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dohosoft.talkking.Data.FanData;
import com.dohosoft.talkking.Data.SettingData;
import com.dohosoft.talkking.Data.SimpleChatData;
import com.fpang.lib.FpangSession;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.dohosoft.talkking.Data.MyData;
import com.dohosoft.talkking.Data.SimpleUserData;
import com.dohosoft.talkking.Data.UserData;
import com.dohosoft.talkking.Firebase.FirebaseData;
import com.dohosoft.talkking.Util.AwsFunc;
import com.dohosoft.talkking.Util.CommonFunc;
import com.dohosoft.talkking.Util.LocationFunc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static com.dohosoft.talkking.Data.CoomonValueData.DEF_LAT;
import static com.dohosoft.talkking.Data.CoomonValueData.DEF_LON;
import static com.dohosoft.talkking.Data.CoomonValueData.FIRST_LOAD_MAIN_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.HOTMEMBER_LOAD_MAIN_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.LOAD_MAIN_COUNT;
import static com.dohosoft.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;
import static com.dohosoft.talkking.Data.CoomonValueData.REF_LAT;
import static com.dohosoft.talkking.Data.CoomonValueData.REF_LON;
import static com.dohosoft.talkking.Data.CoomonValueData.bMyLoc;
import static com.dohosoft.talkking.Data.CoomonValueData.bMySet;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetNear;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetNew;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetRecv;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetRich;
import static com.dohosoft.talkking.Data.CoomonValueData.bSetHot;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, GoogleApiClient.OnConnectionFailedListener {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AwsFunc mAwsFunc = AwsFunc.getInstance();
    private MyData mMyData = MyData.getInstance();

    private UserLoginTask mAuthTask = null;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private View mLoginFormView;

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private TextView mTextView_SignUp;
    private TextView mTextView_NeedHelp;
    private Button mEmailSignInButton;
    private SignInButton mGoogleSignInButton;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private LocationFunc mLocalFunc = LocationFunc.getInstance();
    private CommonFunc mCommon = CommonFunc.getInstance();
    private LocationFunc mLocFunc = LocationFunc.getInstance();

    //private BoardData mBoardData = BoardData.getInstance();

    //String strMyIdx = mAwsFunc.GetUserIdx(Auth.getCurrentUser().getEmail());
    //String strMyIdx; // = mAwsFunc.GetUserIdx(Auth.getCurrentUser().getEmail());
    //String strMyIdx;
    DatabaseReference ref;


    private boolean bUpdate = true;

    private int nUserSet = 0;
    private static final int RC_SIGN_IN = 9001;
    private static String TAG = "123123123";

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;
    String provider;

    private Activity mActivity;
    private long pressedTime;

    //ProgressBar progressBar;

    private ImageView ImgLoading;

    final long[] tempVal = {0};
    String rtStr = null;
    String version = null;


    AlertDialog.Builder mDialog;

    @Override
    public void onBackPressed() {

        CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
            public void YesListener() {
                finish();
                System.exit(0);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        };

        CommonFunc.getInstance().ShowDefaultPopup(this, listener, null, "종료 확인", "톡킹을 종료하시겠습니까?", "종료", "계속");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }



    public void getVersion() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = database.getReference("CommonValue").child("Version");

        table.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMyData.marketVersion = dataSnapshot.getValue(String.class);

                if (!mMyData.verSion.equals(mMyData.marketVersion)) {
                    bUpdate = false;
                    mDialog.setMessage("업데이트 후 사용해주세요.")
                            .setCancelable(false)
                            .setPositiveButton("업데이트 바로가기",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            Intent marketLaunch = new Intent(
                                                    Intent.ACTION_VIEW);
                                            marketLaunch.setData(Uri
                                                    //.parse("https://play.google.com/store/apps/details?id=패키지명 적으세요"));
                                                    .parse("https://play.google.com/store/apps/details?id=com.dohosoft.talkking"));

                                            startActivity(marketLaunch);
                                            finish();
                                        }
                                    });
                    AlertDialog alert = mDialog.create();
                    alert.setTitle("안 내");
                    alert.show();
                } else
                    bUpdate = true;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;



          /*  mMyData.mRewardedVideoAd.loadAd("ca-app-pub-4020702622451243/5818242350",
                    new AdRequest.Builder().build());*/



        //FpangSession.setDebug(true);
      //  FpangSession.setUserId("Talkking");

       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mMyData.SetCurFrag(0);

        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mMyData.verSion = pi.versionName;

        mDialog = new AlertDialog.Builder(this);
        getVersion();

  /*     PrepareMan initHot = new PrepareMan();
        initHot.execute(0, 0, 0);*/
/*


        new getMarketVersion().execute();
*/

      /*  try {
            PackageInfo i = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            version = i.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }*/

        Log.d(TAG, "!!!!!!!!!!!!!!!!!!!");

        getApplication().registerActivityLifecycleCallbacks(new CommonFunc.MyActivityLifecycleCallbacks());

        mMyData.ANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            if (!"9774d56d682e549c".equals(mMyData.ANDROID_ID)) {
                mMyData.uuid = UUID.nameUUIDFromBytes(mMyData.ANDROID_ID
                        .getBytes("utf8"));
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                } else {
                    final String deviceId = ((TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    mMyData.uuid = deviceId != null ? UUID
                            .nameUUIDFromBytes(deviceId
                                    .getBytes("utf8")) : UUID
                            .randomUUID();
                }

            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        /*ImgLoading = (ImageView)findViewById(R.id.loading);

        Glide.with(getApplicationContext())
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.logo_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ImgLoading);*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set up the login form.
        mAuth = FirebaseAuth.getInstance();


        final long[] tempVal = {0};
        final String[] rtStr = new String[1];

  /*      Handler handler = new_img Handler();
        handler.postDelayed(new_img Runnable() {
            public void run() {*/


        FirebaseUser currentUser = mAuth.getCurrentUser();
    /*    if(currentUser != null)
        {
            FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
            Query data = FirebaseDatabase.getInstance().getReference().child("UserIdx").child(mMyData.ANDROID_ID);

            data.addListenerForSingleValueEvent(new_img ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rtStr[0] = dataSnapshot.getValue(String.class);

                    mMyData.setUserIdx(rtStr[0]);
                    InitData_Mine();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else*/
        {

            final boolean[] bCheckedMyIdx = {false};

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {

                    FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
                    Query data = FirebaseDatabase.getInstance().getReference().child("UserIdx").child(mMyData.ANDROID_ID);

                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            bCheckedMyIdx[0] = true;
                            rtStr[0] = dataSnapshot.getValue(String.class);

                            if (rtStr[0] == null || rtStr[0].equals(""))
                                go();
                            else {
                                mMyData.setUserIdx(rtStr[0]);
                                getLocation();
                                InitData_Mine();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    int aaa = 0;
                }
            };

            TedPermission.with(LoginActivity.this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                    .check();

        }

/*
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
*/


       /* OptionalPendingResult<GoogleSignInResult> pendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(pendingResult != null)
        {
            if(pendingResult.isDone())
            {
                GoogleSignInResult signInResult = pendingResult.get();
                onSilentLoginFinished(signInResult);
            }
        }
*/

/*
        LoginTask login = new_img LoginTask();
        login.execute(0,0,0);
*/

        // Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        /*Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);*/







/*
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mTextView_NeedHelp = (TextView) findViewById(R.id.Login_NeedHelp);
        mGoogleSignInButton = (SignInButton) findViewById(R.id.Login_Google);
        mTextView_SignUp = (TextView) findViewById(R.id.Login_SignUp);

        *//*LoginTask login = new_img LoginTask();
        login.execute(0,0,0);*//*


       if(mAuth.getCurrentUser() != null){
            strMyIdx = mAwsFunc.GetUserIdx(mAuth.getCurrentUser().getEmail());
            Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
            InitData_Mine();
        }
        else
            {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);


            mEmailSignInButton.setOnClickListener(new_img OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin(null, null);
                }
            });
            mTextView_SignUp.setOnClickListener(new_img OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new_img Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            mTextView_NeedHelp.setOnClickListener(new_img OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("LoginActivity", "mTextView_NeedHelp Clicked!");
                }
            });
            mGoogleSignInButton.setOnClickListener(new_img OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });

        }*/
        /*    }
        }, 5000);*/
    }

    private void go() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");

                            final FirebaseUser user = mAuth.getCurrentUser();


                            FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
                            DatabaseReference data = fierBaseDataInstance.getReference("UserCount");
                            data.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    Long index = mutableData.getValue(Long.class);
                                    if (index == null)
                                        return Transaction.success(mutableData);

                                    index++;

                                    mutableData.setValue(index);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {


                                    tempVal[0] = dataSnapshot.getValue(Long.class);
                                    rtStr = Long.toString(tempVal[0]);


                                    GoProfilePage();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            CommonFunc.getInstance().ShowToast(LoginActivity.this, "Authentication failed.", true);
                        }

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "@@@@@@@@@@@@@@@@@@@");

/*        Log.d("nUserSet !!!", "nUserSet : " + nUserSet);
        if (mLocalFunc.checkLocationPermission(getApplicationContext(), this)) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
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

        if (bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

            Log.d(TAG, "Account Log in  Complete");
            CommonFunc.getInstance().GoMainPage(mActivity);
        }

        /*
        OnCompleteListener<Location> mCompleteListener = new_img OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location tempLoc = task.getResult();
                    mMyData.setUserLon(tempLoc.getLongitude());
                    mMyData.setUserLat(tempLoc.getLatitude());

                    bMyLoc = true;
                          if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true){

                        Log.d(TAG, "Account Log in  Complete");
                        GoMainPage();
                    }


                    //InitData_Near();
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(this, mCompleteListener);*/
    }

    private void GoProfilePage() {

        CommonFunc.getInstance().ShowLoadingPage(LoginActivity.this, "로딩중");
        FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
        Query data;
        data = FirebaseDatabase.getInstance().getReference().child("UserIdx_History").child(mMyData.ANDROID_ID);


        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CommonFunc.getInstance().DismissLoadingPage();

              String tempStr = dataSnapshot.getValue(String.class);

                if (tempStr == null || tempStr.equals("")) {
                    Intent intent = new Intent(LoginActivity.this, InputProfile.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Idx", rtStr);
                    bundle.putSerializable("ReJoin", false);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(LoginActivity.this, InputProfile.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Idx", rtStr);
                    bundle.putSerializable("ReJoin", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void SetBoardMyData() {
        DatabaseReference refMyBoard;
        refMyBoard = FirebaseDatabase.getInstance().getReference().child("Board");

        refMyBoard.orderByChild("Idx").equalTo(mMyData.getUserIdx()).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //mBoardData.AddMyBoardData(dataSnapshot);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    /*    if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {

                GoogleSignInAccount acct = result.getSignInAccount();

                mMyData.setUserIdx(mAwsFunc.GetUserIdx(acct.getEmail()));

                if(mMyData.getUserIdx() == null || mMyData.getUserIdx().equals("") ){

                    Log.d(TAG, "표시되는 전체 이름 =" + acct.getDisplayName());
                    Log.d(TAG, "표시되는 이름=" + acct.getGivenName());
                    Log.d(TAG, "이메일=" + acct.getEmail());
                    Log.d(TAG, "표시되는 성=" + acct.getFamilyName());

                    firebaseAuthWithGoogle(acct);
                }

                else {
                    // Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
                    InitData_Mine();

                    // 로그인 성공 했을때

                }

            } else {

                // 로그인 실패 했을때
            }
        }*/
    }

/*
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new_img OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        mMyData.setUserIdx(mAwsFunc.CreateUserIdx((acct.getEmail())));
                        GoProfilePage();

                        // If sign in fails, display a message1 to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "signInWithCredential", task.getException());
                            // Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
*/

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(String Email, String PW) {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = null;
        String password = null;

        if (Email == null)
            email = mEmailView.getText().toString();
        else
            email = Email;


        if (PW == null)
            password = mPasswordView.getText().toString();
        else
            password = PW;


        // mMyData.setUserIdx(mAwsFunc.GetUserIdx(email));


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.


            //strMyIdx = "87";
            //strMyIdx = "81";


            //          InitData_Near();
            //           InitData_New();
//            InitData_Hot();

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "Sing in Account:" + task.isSuccessful());
                                    if (task.isSuccessful()) {
                                        InitData_Mine();
                                    } else {
                                        //Toast.makeText(LoginActivity.this,"Log In Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }

    static boolean bInit = false;

    private void InitData_Mine() {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table = null;
        table = database.getReference("GenderList");

        table.child(mMyData.getUserIdx()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tempValue = dataSnapshot.getValue(String.class);

                if(tempValue.equals("여자"))
                {
                    ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman").child(mMyData.getUserIdx());
                }
                else  if(tempValue.equals("남자"))
                {
                    ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man").child(mMyData.getUserIdx());
                }

                //ref.addValueEventListener(
                ref.addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i = 0;
                                UserData stRecvData = new UserData();
                                stRecvData = dataSnapshot.getValue(UserData.class);
                                if (stRecvData != null) {

                                    if (stRecvData.Img == null)
                                        stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                    mMyData.setMyData(stRecvData.Uid, stRecvData.Idx, stRecvData.ImgCount, stRecvData.Img, stRecvData.ImgGroup0, stRecvData.ImgGroup1, stRecvData.ImgGroup2, stRecvData.ImgGroup3,
                                            stRecvData.NickName, stRecvData.Gender, stRecvData.Age, stRecvData.Honey, stRecvData.SendCount, stRecvData.RecvGold, stRecvData.Date,
                                            stRecvData.Memo, stRecvData.RecvMsgReject, stRecvData.PublicRoomStatus, stRecvData.PublicRoomName, stRecvData.PublicRoomLimit, stRecvData.PublicRoomTime,
                                            stRecvData.ItemCount, stRecvData.Item_1, stRecvData.Item_2, stRecvData.Item_3, stRecvData.Item_4, stRecvData.Item_5, stRecvData.Item_6, stRecvData.Item_7, stRecvData.Item_8, stRecvData.BestItem,
                                            stRecvData.Point, stRecvData.Grade, stRecvData.ConnectDate, stRecvData.LastBoardWriteTime, stRecvData.LastAdsTime, stRecvData.NickChangeCnt);


                                    mMyData.mapGenderData.put(mMyData.getUserIdx(), mMyData.getUserGender());

                                    for (LinkedHashMap.Entry<String, String> entry : stRecvData.CardList.entrySet()) {
                                        mMyData.arrCardNameList.add(entry.getValue());
                                        //mMyData.sortStarData();
                                    }
                            /*for(LinkedHashMap.Entry<String, SimpleUserData> entry : stRecvData.CardList.entrySet()) {
                                mMyData.arrCardNameList.add(entry.getValue());
                            }*/

                                    //mMyData.getCardList();


                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Setting", getApplicationContext().MODE_PRIVATE);
                                    mMyData.nSearchMode = pref.getInt("nSearchMode", 0);


                                    mMyData.getSetting();

                                    mMyData.getNotice();
                                    mMyData.getLogin();
                                    mMyData.getSignUp();

                                    mMyData.getDownUrl(mActivity);
                                    mMyData.getImageLoading(mActivity);
                                    mMyData.getBoardLoadingDate(mActivity);
                                    //mMyData.getAdBannerID();
//                            mMyData.getCardList(mActivity);
                                    mMyData.getFanList(mActivity);
                                    mMyData.getReportedCnt();
                                    mMyData.getVersion();
                                    mMyData.getNotification();



                                    mMyData.getSendList(mActivity);
                                    mMyData.getSendHoneyList();
                                    mMyData.getGiftHoneyList();
                                    mMyData.getRecvHoneyList();
                                    mMyData.getBlockList();
                                    mMyData.getBlockedList();

                                    FirebaseData.getInstance().GetInitBoardData();
                                    FirebaseData.getInstance().GetInitMyBoardData();

                                    //mMyData.MonitorPublicRoomStatus();

                            /*PrepareMan initHot = new PrepareMan();
                            initHot.execute(0, 0, 0);*/

                            /*PrepareHotmember init = new PrepareHotmember();
                            init.execute(0, 0, 0);*/


                                    PrePareHot initHot = new PrePareHot();
                                    initHot.execute(0, 0, 0);

                                    PrePareRecv initRecv = new PrePareRecv();
                                    initRecv.execute(0, 0, 0);

                                  /*  PrePareFan initFan = new PrePareFan();
                                    initFan.execute(0, 0, 0);*/

                                    PrePareNear initNear = new PrePareNear();
                                    initNear.execute(0, 0, 0);

                                    PrePareNew initNew = new PrePareNew();
                                    initNew.execute(0, 0, 0);




                                    bInit = true;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                                //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'image' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        //Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

        /*    for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/

            // TODO: register the new_img account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public class PrepareMan extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            DatabaseReference ref;
            ref = FirebaseDatabase.getInstance().getReference().child("User");
            Query query = ref.orderByChild("Idx").limitToFirst(FIRST_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData cTempData = new UserData();
                                cTempData = fileSnapshot.getValue(UserData.class);
                                if (cTempData != null && cTempData.Idx != null) {
                                    // if (!cTempData.Idx.equals(mMyData.getUserIdx()))
                                    {
                                        if (cTempData.Img == null)
                                            cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_Recv.add(cTempData);

                                        for (LinkedHashMap.Entry<String, String> entry : cTempData.CardList.entrySet()) {
                                            mMyData.arrUserAll_Recv.get(i).arrCardList.add(entry.getValue());
                                        }

                                        for (LinkedHashMap.Entry<String, FanData> entry : cTempData.FanList.entrySet()) {
                                            mMyData.arrUserAll_Recv.get(i).arrFanList.add(entry.getValue());
                                        }



                                        for (LinkedHashMap.Entry<String, SimpleChatData> entry : cTempData.SendList.entrySet()) {
                                            mMyData.arrUserAll_Recv.get(i).tempChatData.add(entry.getValue());
                                        }

                                        if (mMyData.arrUserAll_Recv.get(i).Gender.equals("여자")) {
                                            mMyData.arrUserWoman_Recv.add(mMyData.arrUserAll_Recv.get(i));
                                        } else {
                                            mMyData.arrUserMan_Recv.add(mMyData.arrUserAll_Recv.get(i));
                                        }


                                        i++;
                                    }

                                }

                            }

                            mMyData.arrUserAll_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Recv, mMyData.nStartAge, mMyData.nEndAge);
                            mMyData.arrUserWoman_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserWoman_Recv, mMyData.nStartAge, mMyData.nEndAge);
                            mMyData.arrUserMan_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserMan_Recv, mMyData.nStartAge, mMyData.nEndAge);



                            for(int a = 0; a<mMyData.arrUserWoman_Recv.size(); a++)
                            {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference table = database.getReference("Users");//.child(mMyData.getUserIdx());

                                // DatabaseReference user = table.child( userIdx);
                                DatabaseReference user = table.child("Woman").child(mMyData.arrUserWoman_Recv.get(a).Idx);
                                user.child("Age").setValue(mMyData.arrUserWoman_Recv.get(a).Age);
                                user.child("BestItem").setValue(mMyData.arrUserWoman_Recv.get(a).BestItem);
                                user.child("ConnectDate").setValue(mMyData.arrUserWoman_Recv.get(a).ConnectDate);
                                user.child("Date").setValue(mMyData.arrUserWoman_Recv.get(a).Date);
                                user.child("Dist").setValue(mMyData.arrUserWoman_Recv.get(a).Dist);
                                user.child("FanCount").setValue(mMyData.arrUserWoman_Recv.get(a).FanCount);

                                user.child("FanList").setValue(mMyData.arrUserWoman_Recv.get(a).FanList);
                                user.child("SendList").setValue(mMyData.arrUserWoman_Recv.get(a).SendList);


                                user.child("Gender").setValue(mMyData.arrUserWoman_Recv.get(a).Gender);
                                user.child("Grade").setValue(mMyData.arrUserWoman_Recv.get(a).Grade);
                                user.child("Honey").setValue(mMyData.arrUserWoman_Recv.get(a).Honey);
                                user.child("Idx").setValue(mMyData.arrUserWoman_Recv.get(a).Idx);
                                user.child("Img").setValue(mMyData.arrUserWoman_Recv.get(a).Img);
                                user.child("ImgCount").setValue(mMyData.arrUserWoman_Recv.get(a).ImgCount);
                                user.child("ImgGroup0").setValue(mMyData.arrUserWoman_Recv.get(a).ImgGroup0);
                                user.child("ImgGroup1").setValue(mMyData.arrUserWoman_Recv.get(a).ImgGroup1);
                                user.child("ImgGroup2").setValue(mMyData.arrUserWoman_Recv.get(a).ImgGroup2);
                                user.child("ImgGroup3").setValue(mMyData.arrUserWoman_Recv.get(a).ImgGroup3);

                                user.child("Lat").setValue(mMyData.arrUserWoman_Recv.get(a).Lat);
                                user.child("Lon").setValue(mMyData.arrUserWoman_Recv.get(a).Lon);

                                user.child("NickChangeCnt").setValue(mMyData.arrUserWoman_Recv.get(a).NickChangeCnt);
                                user.child("NickName").setValue(mMyData.arrUserWoman_Recv.get(a).NickName);
                                user.child("Point").setValue(mMyData.arrUserWoman_Recv.get(a).Point);
                                user.child("RecvGold").setValue(mMyData.arrUserWoman_Recv.get(a).RecvGold);
                                user.child("RecvMsgReject").setValue(mMyData.arrUserWoman_Recv.get(a).RecvMsgReject);
                                user.child("SendCount").setValue(mMyData.arrUserWoman_Recv.get(a).SendCount);
                                user.child("Token").setValue(mMyData.arrUserWoman_Recv.get(a).Token);

                                user.child("CardList").setValue(mMyData.arrUserWoman_Recv.get(a).CardList);

                                user.child("ItemCount").setValue(mMyData.arrUserWoman_Recv.get(a).ItemCount);
                                user.child("Item_1").setValue(mMyData.arrUserWoman_Recv.get(a).Item_1);
                                user.child("Item_2").setValue(mMyData.arrUserWoman_Recv.get(a).Item_2);
                                user.child("Item_3").setValue(mMyData.arrUserWoman_Recv.get(a).Item_3);
                                user.child("Item_4").setValue(mMyData.arrUserWoman_Recv.get(a).Item_4);
                                user.child("Item_5").setValue(mMyData.arrUserWoman_Recv.get(a).Item_5);
                                user.child("Item_6").setValue(mMyData.arrUserWoman_Recv.get(a).Item_6);
                                user.child("Item_7").setValue(mMyData.arrUserWoman_Recv.get(a).Item_7);
                                user.child("Item_8").setValue(mMyData.arrUserWoman_Recv.get(a).Item_8);
                            }


                            for(int a = 0; a<mMyData.arrUserMan_Recv.size(); a++)
                            {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference table = database.getReference("Users");//.child(mMyData.getUserIdx());

                                // DatabaseReference user = table.child( userIdx);
                                DatabaseReference user = table.child("Man").child(mMyData.arrUserMan_Recv.get(a).Idx);
                                user.child("Age").setValue(mMyData.arrUserMan_Recv.get(a).Age);
                                user.child("BestItem").setValue(mMyData.arrUserMan_Recv.get(a).BestItem);
                                user.child("ConnectDate").setValue(mMyData.arrUserMan_Recv.get(a).ConnectDate);
                                user.child("Date").setValue(mMyData.arrUserMan_Recv.get(a).Date);
                                user.child("Dist").setValue(mMyData.arrUserMan_Recv.get(a).Dist);
                                user.child("FanCount").setValue(mMyData.arrUserMan_Recv.get(a).FanCount);

                                user.child("FanList").setValue(mMyData.arrUserMan_Recv.get(a).FanList);
                                user.child("SendList").setValue(mMyData.arrUserMan_Recv.get(a).SendList);


                                user.child("Gender").setValue(mMyData.arrUserMan_Recv.get(a).Gender);
                                user.child("Grade").setValue(mMyData.arrUserMan_Recv.get(a).Grade);
                                user.child("Honey").setValue(mMyData.arrUserMan_Recv.get(a).Honey);
                                user.child("Idx").setValue(mMyData.arrUserMan_Recv.get(a).Idx);
                                user.child("Img").setValue(mMyData.arrUserMan_Recv.get(a).Img);
                                user.child("ImgCount").setValue(mMyData.arrUserMan_Recv.get(a).ImgCount);
                                user.child("ImgGroup0").setValue(mMyData.arrUserMan_Recv.get(a).ImgGroup0);
                                user.child("ImgGroup1").setValue(mMyData.arrUserMan_Recv.get(a).ImgGroup1);
                                user.child("ImgGroup2").setValue(mMyData.arrUserMan_Recv.get(a).ImgGroup2);
                                user.child("ImgGroup3").setValue(mMyData.arrUserMan_Recv.get(a).ImgGroup3);

                                user.child("Lat").setValue(mMyData.arrUserMan_Recv.get(a).Lat);
                                user.child("Lon").setValue(mMyData.arrUserMan_Recv.get(a).Lon);

                                user.child("NickChangeCnt").setValue(mMyData.arrUserMan_Recv.get(a).NickChangeCnt);
                                user.child("NickName").setValue(mMyData.arrUserMan_Recv.get(a).NickName);
                                user.child("Point").setValue(mMyData.arrUserMan_Recv.get(a).Point);
                                user.child("RecvGold").setValue(mMyData.arrUserMan_Recv.get(a).RecvGold);
                                user.child("RecvMsgReject").setValue(mMyData.arrUserMan_Recv.get(a).RecvMsgReject);
                                user.child("SendCount").setValue(mMyData.arrUserMan_Recv.get(a).SendCount);
                                user.child("Token").setValue(mMyData.arrUserMan_Recv.get(a).Token);

                                user.child("CardList").setValue(mMyData.arrUserMan_Recv.get(a).CardList);

                                user.child("ItemCount").setValue(mMyData.arrUserMan_Recv.get(a).ItemCount);
                                user.child("Item_1").setValue(mMyData.arrUserMan_Recv.get(a).Item_1);
                                user.child("Item_2").setValue(mMyData.arrUserMan_Recv.get(a).Item_2);
                                user.child("Item_3").setValue(mMyData.arrUserMan_Recv.get(a).Item_3);
                                user.child("Item_4").setValue(mMyData.arrUserMan_Recv.get(a).Item_4);
                                user.child("Item_5").setValue(mMyData.arrUserMan_Recv.get(a).Item_5);
                                user.child("Item_6").setValue(mMyData.arrUserMan_Recv.get(a).Item_6);
                                user.child("Item_7").setValue(mMyData.arrUserMan_Recv.get(a).Item_7);
                                user.child("Item_8").setValue(mMyData.arrUserMan_Recv.get(a).Item_8);
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
            if (bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                Log.d(TAG, "Account Log in  Complete");
                CommonFunc.getInstance().GoMainPage(mActivity);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }



    public class PrepareHotmember extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            DatabaseReference ref;
            ref = FirebaseDatabase.getInstance().getReference().child("HotMember");
            Query query = ref.child("Woman").orderByChild("Idx").limitToFirst(100);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData cTempData = new UserData();
                                cTempData = fileSnapshot.getValue(UserData.class);
                                if (cTempData != null && cTempData.Idx != null) {
                                    // if (!cTempData.Idx.equals(mMyData.getUserIdx()))
                                    {
                                        String tempIdx = cTempData.Idx;

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference table = database.getReference("HotMemberIdx");//.child(mMyData.getUserIdx());
                                        DatabaseReference user = table.child("Woman");
                                        user.child(tempIdx).setValue(tempIdx);
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


            ref = FirebaseDatabase.getInstance().getReference().child("HotMember");
            query = ref.child("Man").orderByChild("Idx").limitToFirst(100);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData cTempData = new UserData();
                                cTempData = fileSnapshot.getValue(UserData.class);
                                if (cTempData != null && cTempData.Idx != null) {
                                    // if (!cTempData.Idx.equals(mMyData.getUserIdx()))
                                    {
                                        String tempIdx = cTempData.Idx;

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference table = database.getReference("HotMemberIdx");//.child(mMyData.getUserIdx());
                                        DatabaseReference user = table.child("Man");
                                        user.child(tempIdx).setValue(tempIdx);
                                    }
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
            if (bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                Log.d(TAG, "Account Log in  Complete");
                CommonFunc.getInstance().GoMainPage(mActivity);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            super.onProgressUpdate(params);
        }
    }


    public class PrePareHot extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {

            if(SettingData.getInstance().getnSearchSetting() == 1)
            {
                ref = FirebaseDatabase.getInstance().getReference().child("HotMemberIdx").child("Man");
            }
            else
            {
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

                                if(tempValue != null || !tempValue.equals(""))
                                {
                                    i ++;
                                    final DatabaseReference Hotref;
                                    if(SettingData.getInstance().getnSearchSetting() == 1)
                                    {
                                        Hotref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man").child(tempValue);
                                    }
                                    else
                                    {
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
                                                            if (CommonFunc.getInstance().CheckUserData(stRecvData,dataSnapshot.getKey()))
                                                            {
                                                                if (stRecvData.Img == null)
                                                                    stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                                                mMyData.arrUserAll_Hot.add(stRecvData);

                                                                mMyData.mapGenderData.put(stRecvData.Idx, stRecvData.Gender);

                                                                if(mMyData.arrUserAll_Hot.size() == HOTMEMBER_LOAD_MAIN_COUNT)
                                                                {

                                                                    CommonFunc.AscendingObj ascending = new CommonFunc.AscendingObj();

                                                                    Collections.sort(mMyData.arrUserAll_Hot, ascending);

                                                                    mMyData.arrUserAll_Hot_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Hot, mMyData.nStartAge, mMyData.nEndAge);

                                                                    bSetHot = true;
                                                                    if (mMyData.arrUserAll_Hot.size() > 0)
                                                                        mMyData.HotIndexRef = mMyData.arrUserAll_Hot.get(mMyData.arrUserAll_Hot.size() - 1).Date;

                                                                    if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                                                                        Log.d(TAG, "Account Log in  Complete");
                                                                        CommonFunc.getInstance().GoMainPage(mActivity);
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
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            DatabaseReference ref;

            if(SettingData.getInstance().getnSearchSetting() == 1)
            {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            }
            else
            {
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
                                    if (CommonFunc.getInstance().CheckUserData(cTempData,fileSnapshot.getKey()))
                                    {
                                        if (cTempData.Img == null)
                                            cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_Recv.add(cTempData);
                                        mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);


                                        i++;
                                    }

                                }

                            }

                            mMyData.arrUserAll_Recv_Age = mMyData.SortData_UAge(mMyData.arrUserAll_Recv, mMyData.nStartAge, mMyData.nEndAge);

                            bSetRecv = true;
                            if (mMyData.arrUserAll_Recv.size() > 0)
                                mMyData.RecvIndexRef = mMyData.arrUserAll_Recv.get(mMyData.arrUserAll_Recv.size() - 1).RecvGold;

                            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                                Log.d(TAG, "Account Log in  Complete");
                                CommonFunc.getInstance().GoMainPage(mActivity);
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
            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                Log.d(TAG, "Account Log in  Complete");
                CommonFunc.getInstance().GoMainPage(mActivity);
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
            //progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... voids) {
            DatabaseReference ref;

            if(SettingData.getInstance().getnSearchSetting() == 1)
            {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            }
            else
            {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            //ref = FirebaseDatabase.getInstance().getReference().child("TestHotMember").child("Man");

            Query query = ref.orderByChild("FanCount").limitToFirst(FIRST_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                UserData cTempData = new UserData();
                                cTempData = fileSnapshot.getValue(UserData.class);

                                if (cTempData != null) {
                                    if (CommonFunc.getInstance().CheckUserData(cTempData,fileSnapshot.getKey()))
                                    {
                                        if (cTempData.Img == null)
                                            cTempData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                        mMyData.arrUserAll_Send.add(cTempData);
                                        mMyData.mapGenderData.put(cTempData.Idx, cTempData.Gender);

                                        i++;
                                    }


                                }
                            }

                            mMyData.arrUserAll_Send_Age = mMyData.SortData_Age(mMyData.arrUserAll_Send, mMyData.nStartAge, mMyData.nEndAge);

                            bSetRich = true;

                            if (mMyData.arrUserAll_Send.size() > 0)
                                mMyData.FanCountRef = mMyData.arrUserAll_Send.get(mMyData.arrUserAll_Send.size() - 1).FanCount;

                            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {


                                Log.d(TAG, "Account Log in  Complete");
                                CommonFunc.getInstance().GoMainPage(mActivity);
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
            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                Log.d(TAG, "Account Log in  Complete");
                CommonFunc.getInstance().GoMainPage(mActivity);
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
            // progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... voids) {

            DatabaseReference ref;

            if(SettingData.getInstance().getnSearchSetting() == 1)
            {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            }
            else
            {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Woman");
            }

            Query query = ref
                    .orderByChild("Dist").limitToFirst(FIRST_LOAD_MAIN_COUNT);

            final double[] tempDist = {0};
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
                                        if (CommonFunc.getInstance().CheckUserData(stRecvData,fileSnapshot.getKey()))
                                        {
                                            if (stRecvData.Img == null)
                                                stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                            tempDist[0] = stRecvData.Dist;
                                            double Dist = mLocFunc.getDistance(mMyData.getUserLat(), mMyData.getUserLon(), stRecvData.Lat, stRecvData.Lon, "meter");
                                            stRecvData.Dist = Dist;

                                            mMyData.arrUserAll_Near.add(stRecvData);
                                            mMyData.mapGenderData.put(stRecvData.Idx, stRecvData.Gender);

                                            i++;
                                        }
                                    }
                                }
                            }

                            if (mMyData.arrUserAll_Near.size() > 0)
                                mMyData.NearDistanceRef = tempDist[0]; //mMyData.arrUserAll_Near.get(mMyData.arrUserAll_Near.size()-1).Dist;

                            CommonFunc.NearSort cNearSort = new CommonFunc.NearSort();
                            Collections.sort(mMyData.arrUserAll_Near, cNearSort);

                            mMyData.arrUserAll_Near_Age = mMyData.SortData_Age(mMyData.arrUserAll_Near, mMyData.nStartAge, mMyData.nEndAge);

                            bSetNear = true;


                            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                                Log.d(TAG, "Account Log in  Complete");
                                CommonFunc.getInstance().GoMainPage(mActivity);
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
            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                Log.d(TAG, "Account Log in  Complete");
                CommonFunc.getInstance().GoMainPage(mActivity);
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
            //    progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... voids) {

            DatabaseReference ref;

            if(SettingData.getInstance().getnSearchSetting() == 1)
            {
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Man");
            }
            else
            {
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

                                if(stRecvData != null)
                                {
                                    if (CommonFunc.getInstance().CheckUserData(stRecvData, fileSnapshot.getKey())) {
                                        //if (!stRecvData.Idx.equals(mMyData.getUserIdx()))
                                        {
                                            if (stRecvData.Img == null)
                                                stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                            mMyData.arrUserAll_New.add(stRecvData);
                                            mMyData.mapGenderData.put(stRecvData.Idx, stRecvData.Gender);

                                            i++;
                                        }
                                    }
                                }
                            }

                            mMyData.arrUserAll_New_Age = mMyData.SortData_Age(mMyData.arrUserAll_New, mMyData.nStartAge, mMyData.nEndAge);

                            bSetNew = true;

                            if (mMyData.arrUserAll_New.size() > 0)
                                mMyData.NewDateRef = mMyData.arrUserAll_New.get(mMyData.arrUserAll_New.size() - 1).Date;

                            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                                Log.d(TAG, "Account Log in  Complete");
                                CommonFunc.getInstance().GoMainPage(mActivity);
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
            if (bSetHot == true && bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true && bMyLoc == true && bUpdate == true) {

                Log.d(TAG, "Account Log in  Complete");
                CommonFunc.getInstance().GoMainPage(mActivity);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public class LoginTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }


}

