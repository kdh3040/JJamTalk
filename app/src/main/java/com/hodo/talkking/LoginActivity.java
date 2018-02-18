package com.hodo.talkking;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
import com.hodo.talkking.Data.FanData;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Data.PublicRoomChatData;
import com.hodo.talkking.Data.SimpleUserData;
import com.hodo.talkking.Data.UserData;
import com.hodo.talkking.Firebase.FirebaseData;
import com.hodo.talkking.Util.AppStatus;
import com.hodo.talkking.Util.AwsFunc;
import com.hodo.talkking.Util.CommonFunc;
import com.hodo.talkking.Util.LocationFunc;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static com.hodo.talkking.Data.CoomonValueData.FIRST_LOAD_MAIN_COUNT;
import static com.hodo.talkking.Data.CoomonValueData.MAIN_ACTIVITY_HOME;

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

    //private BoardData mBoardData = BoardData.getInstance();

    //String strMyIdx = mAwsFunc.GetUserIdx(Auth.getCurrentUser().getEmail());
    //String strMyIdx; // = mAwsFunc.GetUserIdx(Auth.getCurrentUser().getEmail());
    //String strMyIdx;
    DatabaseReference ref;


    private boolean bMySet = false;
    private boolean bSetNear, bSetNew, bSetRich, bSetRecv = false;

    private int nUserSet = 0;
    private static final int RC_SIGN_IN = 9001;
    private static String TAG = "LoginActivity Log!!";

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;
    String provider;

    private Activity mActivity;
    private long pressedTime;

    ProgressBar progressBar;



    @Override
    public void onBackPressed() {

        CommonFunc.ShowDefaultPopup_YesListener listener = new CommonFunc.ShowDefaultPopup_YesListener() {
            public void YesListener() {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        };

        CommonFunc.getInstance().ShowDefaultPopup(this, listener, "종료 확인", "킹톡을 종료하시겠습니까?", "종료", "계속");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;

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
                }
                else
                {
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


        bSetNear = bSetNew = bSetRich = bSetRecv = false;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set up the login form.
        mAuth = FirebaseAuth.getInstance();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        //mLoginFormView = findViewById(R.id.login_form);
        progressBar = findViewById(R.id.login_progress);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        final long[] tempVal = {0};
        final String[] rtStr = new String[1];

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            FirebaseDatabase fierBaseDataInstance = FirebaseDatabase.getInstance();
            Query data = FirebaseDatabase.getInstance().getReference().child("UserIdx").child(mMyData.ANDROID_ID);

            data.addListenerForSingleValueEvent(new ValueEventListener() {
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
        else
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

                                if(rtStr[0] == null || rtStr[0].equals("") )
                                    go();
                                else
                                {
                                    mMyData.setUserIdx(rtStr[0]);
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


            new TedPermission(LoginActivity.this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                    .check();

    /*
           */


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
        LoginTask login = new LoginTask();
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

        *//*LoginTask login = new LoginTask();
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


            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin(null, null);
                }
            });
            mTextView_SignUp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            mTextView_NeedHelp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("LoginActivity", "mTextView_NeedHelp Clicked!");
                }
            });
            mGoogleSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });

        }*/

    }

    private void go()
    {
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
                                    final long[] tempVal = {0};
                                    final String[] rtStr = new String[1];

                                    tempVal[0] = dataSnapshot.getValue(long.class);
                                    rtStr[0] = Long.toString(tempVal[0]);


                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference table = database.getReference("UserIdx");
                                    final DatabaseReference UserIdx = table.child(mMyData.ANDROID_ID);
                                    UserIdx.setValue(rtStr[0]);

                                    mMyData.setUserIdx(rtStr[0]);
                                    GoProfilePage();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
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
        OnCompleteListener<Location> mCompleteListener = new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location tempLoc = task.getResult();
                    mMyData.setUserLon(tempLoc.getLongitude());
                    mMyData.setUserLat(tempLoc.getLatitude());
                    //InitData_Near();
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(this, mCompleteListener);
    }

    private void GoProfilePage() {
        Intent intent = new Intent(LoginActivity.this, InputProfile.class);
        startActivity(intent);
        finish();
    }
    private void GoMainPage() {

        progressBar.setVisibility(ProgressBar.GONE);
        FirebaseData.getInstance().GetInitBoardData();
        FirebaseData.getInstance().GetInitMyBoardData();
        FirebaseData.getInstance().SaveData(mMyData.getUserIdx());
        mCommon.refreshMainActivity(mActivity, MAIN_ACTIVITY_HOME);
        finish();
        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("StartFragment", 0);
        startActivity(intent);
        finish();*/
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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

        if(Email == null)
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
                                    if(task.isSuccessful()){
                                        InitData_Mine();
                                    }else {
                                        //Toast.makeText(LoginActivity.this,"Log In Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }

    static boolean bInit = false;

    private void InitData_Mine() {

        ref = FirebaseDatabase.getInstance().getReference().child("User").child(mMyData.getUserIdx());
        //ref.addValueEventListener(
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        UserData stRecvData = new UserData ();
                        stRecvData = dataSnapshot.getValue(UserData.class);
                        if(stRecvData != null) {

                            if(stRecvData.Img == null)
                                stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";


                            mMyData.setMyData(stRecvData.Uid, stRecvData.Idx, stRecvData.ImgCount, stRecvData.Img, stRecvData.ImgGroup0, stRecvData.ImgGroup1, stRecvData.ImgGroup2, stRecvData.ImgGroup3,
                                    stRecvData.NickName, stRecvData.Gender, stRecvData.Age, stRecvData.Lon, stRecvData.Lat, stRecvData.Honey, stRecvData.SendCount, stRecvData.RecvCount, stRecvData.Date,
                                    stRecvData.Memo, stRecvData.RecvMsgReject, stRecvData.PublicRoomStatus, stRecvData.PublicRoomName, stRecvData.PublicRoomLimit, stRecvData.PublicRoomTime,
                                    stRecvData.ItemCount, stRecvData.Item_1, stRecvData.Item_2, stRecvData.Item_3, stRecvData.Item_4, stRecvData.Item_5, stRecvData.Item_6, stRecvData.Item_7, stRecvData.Item_8, stRecvData.BestItem,
                                    stRecvData.Point, stRecvData.Grade, stRecvData.ConnectDate, stRecvData.LastBoardWriteTime, stRecvData.LastAdsTime);
                            bMySet = true;


                      /*      for(LinkedHashMap.Entry<String, String> entry : stRecvData.ChatTargetList.entrySet()) {
                                mMyData.arrMyChatTargetList.add(entry.getValue());
                            }*/

                         /*   for(LinkedHashMap.Entry<String, FanData> entry : stRecvData.FanList.entrySet()) {
                                    mMyData.arrMyFanList.add(entry.getValue());
                            }*/

                            //mMyData.getFanList();

                            // mMyData.nFanCount = mMyData.arrMyFanList.size();

                            //mMyData.arrMyStarList = stRecvData.StarList;
                        /*    for(LinkedHashMap.Entry<String, SimpleUserData> entry : stRecvData.StarList.entrySet()) {
                                StarData tempStarData = new StarData();
                                tempStarData.Idx = entry.getValue().Idx;
                                tempStarData.SendGold = entry.getValue().SendGold;
                                mMyData.arrMyStarList.add(tempStarData);
                                //mMyData.sortStarData();
                            }*/

               /*             for(LinkedHashMap.Entry<String, SimpleUserData> entry : stRecvData.FanList.entrySet()) {
                                FanData tempFanData = new FanData();
                                tempFanData.Idx = entry.getValue().Idx;
                                tempFanData.RecvGold = entry.getValue().RecvGold;
                                mMyData.arrMyFanList.add(tempFanData);
                                //mMyData.sortStarData();
                            }
*/

                            for(LinkedHashMap.Entry<String, String> entry : stRecvData.CardList.entrySet()) {
                                mMyData.arrCardNameList.add(entry.getValue());
                                //mMyData.sortStarData();
                            }
                            /*for(LinkedHashMap.Entry<String, SimpleUserData> entry : stRecvData.CardList.entrySet()) {
                                mMyData.arrCardNameList.add(entry.getValue());
                            }*/

                            //mMyData.getCardList();

                            getLocation();
                            mMyData.getDownUrl();
                            //mMyData.getAdBannerID();
                            mMyData.getFanList();

                            mMyData.getReportedCnt();

                            mMyData.getSetting();

                            mMyData.getSendList();
                            mMyData.getSendHoneyList();
                            mMyData.getGiftHoneyList();
                            mMyData.getRecvHoneyList();
                            mMyData.getBlockList();
                            mMyData.getBlockedList();
                            //mMyData.MonitorPublicRoomStatus();

                            PrePareHot initHot = new PrePareHot();
                            initHot.execute(0,0,0);

                            PrePareFan initFan = new PrePareFan();
                            initFan.execute(0,0,0);

                            PrePareNear initNear = new PrePareNear();
                            initNear.execute(0,0,0);

                            PrePareNew initNew = new PrePareNew();
                            initNew.execute(0,0,0);

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
                // Retrieve data rows for the device user's 'profile' contact.
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

            // TODO: register the new account here.
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
            Query query=ref.orderByChild("Point").limitToFirst(FIRST_LOAD_MAIN_COUNT);//키가 id와 같은걸 쿼리로 가져옴
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

                                Log.d(TAG, "Account Log in  Complete");
                                GoMainPage();
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

                Log.d(TAG, "Account Log in  Complete");
                GoMainPage();
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
                                    if (!cTempData.Idx.equals(mMyData.getUserIdx())) {
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
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){

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
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... voids) {

            Double lStartLon = mMyData.getUserLon() - 0.05;
            Double lStartLat = mMyData.getUserLat() - 0.05;

            Double lEndLon = mMyData.getUserLon() + 0.05;
            Double IEndLat = mMyData.getUserLon() + 0.05;

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
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){

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
            Query query=ref.orderByChild("Date").startAt(Integer.toString(nStartDate)).endAt(Integer.toString(nTodayDate)).limitToFirst(FIRST_LOAD_MAIN_COUNT);;
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
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){

                Log.d(TAG, "Account Log in  Complete");
                GoMainPage();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public class LoginTask extends AsyncTask<Integer, Integer, Integer>  {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
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
