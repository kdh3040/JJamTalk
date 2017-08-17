package com.hodo.jjamtalk;

import android.*;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.BoardData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.TempBoardData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Util.AwsFunc;

import com.hodo.jjamtalk.Kakao.KakaoSignupActivity;
import com.hodo.jjamtalk.Util.LocationFunc;
import com.kakao.auth.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
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
    private View mProgressView;
    private View mLoginFormView;

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private TextView mTextView_SignUp;
    private TextView mTextView_NeedHelp;
    private Button mEmailSignInButton;
    private Button mGoogleSignInButton;
    private Button mKakaoSignInButton;
    private Button mNaverSignInButton;
    private Button mToMain;

    private FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    private LocationFunc mLocalFunc = LocationFunc.getInstance();
    private BoardData mBoardData = BoardData.getInstance();

    //String strMyIdx = mAwsFunc.GetUserIdx(Auth.getCurrentUser().getEmail());
    String strMyIdx; // = mAwsFunc.GetUserIdx(Auth.getCurrentUser().getEmail());
    //String strMyIdx;
    DatabaseReference ref;


    private Boolean bMySet = false;
    private int nUserSet = 0;
    private static final int RC_SIGN_IN = 9001;
    private static String TAG = "LoginActivity Log!!";

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mAuth = FirebaseAuth.getInstance();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mTextView_SignUp = (TextView)findViewById(R.id.Login_SignUp);
        mTextView_SignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mTextView_NeedHelp = (TextView)findViewById(R.id.Login_NeedHelp);
        mTextView_NeedHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LoginActivity", "mTextView_NeedHelp Clicked!");
            }
        });
        //mKakaoSignInButton = (Button)findViewById(R.id.Login_Kakao);

    /*    mKakaoSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        mNaverSignInButton  = (Button)findViewById(R.id.Login_Naver);
        mNaverSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LoginActivity", "mTextView_NeedHelp Clicked!");
            }
        });

        mGoogleSignInButton = (Button)findViewById(R.id.Login_Google);
        mGoogleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


        if(mAuth.getCurrentUser() != null){
            Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
            // 만약 회원이라면 메인으로 이동한다.
            //GoMainPage();
        } else {
            Log.d(TAG, "Log out State");
        }


        mToMain =(Button)findViewById(R.id.btn_tomain);
        mToMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.d("nUserSet !!!", "nUserSet : " + nUserSet);
        if (mLocalFunc.checkLocationPermission(getApplicationContext(), this)) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
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
        SetBoardData();
        SetBoardMyData();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {

                // 로그인 성공 했을때
                GoogleSignInAccount acct = result.getSignInAccount();
                Log.d(TAG, "표시되는 전체 이름 =" + acct.getDisplayName());
                Log.d(TAG, "표시되는 이름=" + acct.getGivenName());
                Log.d(TAG, "이메일=" + acct.getEmail());
                Log.d(TAG, "표시되는 성=" + acct.getFamilyName());

                firebaseAuthWithGoogle(acct);

            } else {

                // 로그인 실패 했을때
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        mMyData.setUserIdx(mAwsFunc.GetUserIdx(acct.getEmail()));
                        GoProfilePage();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        strMyIdx = mAwsFunc.GetUserIdx(email);
       // String email;// = "tt@naver.com";
       // String password;// = "111111";


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
            showProgress(true);

            //strMyIdx = "87";
            //strMyIdx = "81";

            InitData_Mine();
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
                                        InitData_Rank();
                                        InitData_New();
                                        InitData_Hot();
                                        InitData_Near();
                                    }else {
                                        Toast.makeText(LoginActivity.this,
                                                "Log In Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }

    private void InitData_Mine() {


        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("여자").child(strMyIdx);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        UserData stRecvData = new UserData ();
                        stRecvData = dataSnapshot.getValue(UserData.class);
                        if(stRecvData != null) {
                            mMyData.setMyData(stRecvData.Idx, stRecvData.Img, stRecvData.ImgGroup0, stRecvData.ImgGroup1, stRecvData.ImgGroup2, stRecvData.ImgGroup3, stRecvData.ImgGroup4,
                                    stRecvData.NickName, stRecvData.Gender, stRecvData.Age, stRecvData.Lon, stRecvData.Lat, stRecvData.Heart, stRecvData.Honey, stRecvData.Rank, stRecvData.Date,
                                    stRecvData.Memo, stRecvData.School, stRecvData.Company, stRecvData.Title);
                            bMySet = true;

                            mMyData.getCardList();
                            mMyData.getSendList();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });

        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("남자").child(strMyIdx);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        UserData stRecvData = new UserData ();
                        stRecvData = dataSnapshot.getValue(UserData.class);
                        if(stRecvData != null) {
                            mMyData.setMyData(stRecvData.Idx, stRecvData.Img, stRecvData.ImgGroup0, stRecvData.ImgGroup1, stRecvData.ImgGroup2, stRecvData.ImgGroup3, stRecvData.ImgGroup4,
                                    stRecvData.NickName, stRecvData.Gender, stRecvData.Age, stRecvData.Lon, stRecvData.Lat, stRecvData.Heart, stRecvData.Honey, stRecvData.Rank, stRecvData.Date,
                                    stRecvData.Memo, stRecvData.School, stRecvData.Company, stRecvData.Title);
                            bMySet = true;

                            mMyData.getCardList();
                            mMyData.getSendList();
                            //mMyData.getSendData();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        //Toast toast = Toast.makeText(getApplicationContext(), "유져 데이터 cancelled", Toast.LENGTH_SHORT);
                    }
                });
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
                            showProgress(false);
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
                            showProgress(false);
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
        Query query=refMan.orderByChild("Heart");//키가 id와 같은걸 쿼리로 가져옴
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
                            showProgress(false);
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
        query=refWoman.orderByChild("Heart");//키가 id와 같은걸 쿼리로 가져옴
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
                            showProgress(false);
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
                            showProgress(false);
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
                            showProgress(false);
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
                            showProgress(false);
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
                            showProgress(false);
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
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
            showProgress(false);

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
            showProgress(false);
        }
    }

}

