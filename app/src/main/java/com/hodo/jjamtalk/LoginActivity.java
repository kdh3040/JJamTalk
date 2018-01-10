package com.hodo.jjamtalk;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.hodo.jjamtalk.Data.BoardLikeData;
import com.hodo.jjamtalk.Data.FanData;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.TempBoard_ReplyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;
import com.hodo.jjamtalk.Util.AwsFunc;
import com.hodo.jjamtalk.Util.LocationFunc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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
    //private BoardData mBoardData = BoardData.getInstance();

    //String strMyIdx = mAwsFunc.GetUserIdx(Auth.getCurrentUser().getEmail());
    String strMyIdx; // = mAwsFunc.GetUserIdx(Auth.getCurrentUser().getEmail());
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

    private long pressedTime;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

//                ActivityCompat.finishAffinity(this);
                String alertTitle = "종료";
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_exit_app,null,false);

                final AlertDialog dialog = new AlertDialog.Builder(this).setView(v).create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                final Button btn_exit;
                final Button btn_no;

                btn_exit = (Button) v.findViewById(R.id.btn_yes);
                btn_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pid = android.os.Process.myPid(); android.os.Process.killProcess(pid);
                    }
                });

                btn_no = (Button) v.findViewById(R.id.btn_no);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bSetNear = bSetNew = bSetRich = bSetRecv = false;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set up the login form.
        mAuth = FirebaseAuth.getInstance();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mTextView_SignUp = (TextView) findViewById(R.id.Login_SignUp);

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

        mTextView_NeedHelp = (TextView) findViewById(R.id.Login_NeedHelp);

        mNaverSignInButton = (Button) findViewById(R.id.Login_Naver);
        mGoogleSignInButton = (Button) findViewById(R.id.Login_Google);
        mToMain = (Button) findViewById(R.id.btn_tomain);

       if(mAuth.getCurrentUser() != null){
            showProgress(true);
            strMyIdx = mAwsFunc.GetUserIdx(mAuth.getCurrentUser().getEmail());
            Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
            InitData_Mine();
        }else      {

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
            mNaverSignInButton.setOnClickListener(new OnClickListener() {
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
            mToMain.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });

        }

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
        FirebaseData.getInstance().GetInitBoardData(10);
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

    // ValueEventListener

    private void SetBoardData() {

        DatabaseReference refBoard;
        refBoard = FirebaseDatabase.getInstance().getReference().child("Board");
        refBoard.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //mBoardData.AddBoardData(dataSnapshot);
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



        strMyIdx = mAwsFunc.GetUserIdx(email);



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
                                        //InitData_Fan();
                                     /*   InitData_Recv();
                                        InitData_Send();
                                        InitData_New();
                                        InitData_Near();*/
                                    }else {
                                        Toast.makeText(LoginActivity.this,
                                                "Log In Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
        }
    }

    static boolean bInit = false;

    private void InitData_Mine() {

        ref = FirebaseDatabase.getInstance().getReference().child("User").child(strMyIdx);
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


                            mMyData.setMyData(stRecvData.Idx, stRecvData.ImgCount, stRecvData.Img, stRecvData.ImgGroup0, stRecvData.ImgGroup1, stRecvData.ImgGroup2, stRecvData.ImgGroup3,
                                    stRecvData.NickName, stRecvData.Gender, stRecvData.Age, stRecvData.Lon, stRecvData.Lat, stRecvData.Honey, stRecvData.SendCount, stRecvData.RecvCount, stRecvData.Date,
                                    stRecvData.Memo, stRecvData.RecvMsg, stRecvData.PublicRoomStatus, stRecvData.PublicRoomName, stRecvData.PublicRoomLimit, stRecvData.PublicRoomTime,
                                    stRecvData.ItemCount, stRecvData.Item_1, stRecvData.Item_2, stRecvData.Item_3, stRecvData.Item_4, stRecvData.Item_5, stRecvData.Item_6, stRecvData.Item_7, stRecvData.Item_8);
                            bMySet = true;


                      /*      for(LinkedHashMap.Entry<String, String> entry : stRecvData.ChatTargetList.entrySet()) {
                                mMyData.arrMyChatTargetList.add(entry.getValue());
                            }*/

                         /*   for(LinkedHashMap.Entry<String, FanData> entry : stRecvData.FanList.entrySet()) {
                                    mMyData.arrMyFanList.add(entry.getValue());
                            }*/

                            //mMyData.getFanList();

                            // mMyData.nFanCount = mMyData.arrMyFanList.size();

                            for(LinkedHashMap.Entry<String, FanData> entry : stRecvData.StarList.entrySet()) {
                                mMyData.arrMyStarList.add(entry.getValue());
                                //mMyData.sortStarData();
                            }

                            for(LinkedHashMap.Entry<String, FanData> entry : stRecvData.CardList.entrySet()) {
                                mMyData.arrCardNameList.add(entry.getValue());
                            }

                            //mMyData.getCardList();

                            mMyData.getDownUrl();
                            mMyData.getMyfanData();

                            mMyData.getSetting();

                            mMyData.getSendList();
                            mMyData.getSendHoneyList();
                            mMyData.getGiftHoneyList();
                            mMyData.getRecvHoneyList();
                            mMyData.getBlockList();
                            mMyData.getBlockedList();
                            //mMyData.MonitorPublicRoomStatus();

                                //InitData_Fan();
                                InitData_Recv();
                                InitData_Send();
                                InitData_New();
                                InitData_Near();
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
/*
    private void InitData_Fan() {
        DatabaseReference refMan, refWoman;
        refMan = FirebaseDatabase.getInstance().getReference().child("User").child(mMyData.getUserIdx());
        Query query=refMan.orderByChild("Count");//키가 id와 같은걸 쿼리로 가져옴
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            FanData stRecvData = new FanData ();
                            stRecvData = fileSnapshot.getValue(FanData.class);
                            if(stRecvData != null) {
                                mMyData.arrUserAll_Fan.add(stRecvData);

                            }
                            i++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


    }*/

    private void InitData_Recv() {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        Query query=ref.orderByChild("RecvCount");//.limitToFirst(30);//키가 id와 같은걸 쿼리로 가져옴
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
                                for(LinkedHashMap.Entry<String, FanData> entry :  mMyData.arrUserAll_Recv.get(i).FanList.entrySet())
                                    mMyData.arrUserAll_Recv.get(i).arrFanList.add(entry.getValue());

                                for(LinkedHashMap.Entry<String, FanData> entry :  mMyData.arrUserAll_Recv.get(i).StarList.entrySet())
                                    mMyData.arrUserAll_Recv.get(i).arrStarList.add(entry.getValue());

                                if(mMyData.arrUserAll_Recv.get(i).Gender.equals("여자"))
                                {
                                    mMyData.arrUserWoman_Recv.add(stRecvData);
                            //        for(LinkedHashMap.Entry<String, FanData> entry :  mMyData.arrUserWoman_Recv.get(j).FanList.entrySet())
                             //           mMyData.arrUserWoman_Recv.get(j).arrFanList.add(entry.getValue());

                                    j++;
                                }
                                else {
                                    mMyData.arrUserMan_Recv.add(stRecvData);
                             //       for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserMan_Recv.get(k).FanList.entrySet())
                             //           mMyData.arrUserMan_Recv.get(k).arrFanList.add(entry.getValue());

                                    k++;
                                }


                            }
                            i++;

                        }

                        bSetRecv = true;

                        if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
                            showProgress(false);
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
        Query query= ref.orderByChild("FanCount");//.limitToFirst(3);//키가 id와 같은걸 쿼리로 가져옴
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
                               for(LinkedHashMap.Entry<String, FanData> entry :  mMyData.arrUserAll_Send.get(i).FanList.entrySet())
                                    mMyData.arrUserAll_Send.get(i).arrFanList.add(entry.getValue());

                                for(LinkedHashMap.Entry<String, FanData> entry :  mMyData.arrUserAll_Send.get(i).StarList.entrySet())
                                    mMyData.arrUserAll_Send.get(i).arrStarList.add(entry.getValue());

                                if(mMyData.arrUserAll_Send.get(i).Gender.equals("여자"))
                                {
                                    mMyData.arrUserWoman_Send.add(mMyData.arrUserAll_Send.get(i));
                                //   for(LinkedHashMap.Entry<String, FanData> entry :  mMyData.arrUserWoman_Send.get(j).FanList.entrySet())
                                //        mMyData.arrUserWoman_Send.get(j).arrFanList.add(entry.getValue());

                                    j++;
                                }
                                else {
                                    mMyData.arrUserMan_Send.add(mMyData.arrUserAll_Send.get(i));
                              //      for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserMan_Send.get(k).FanList.entrySet())
                              //          mMyData.arrUserMan_Send.get(k).arrFanList.add(entry.getValue());

                                    k++;
                                }

                            }
                            i++;
                        }

                        bSetRich = true;

                        if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
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

                                if(stRecvData.Img == null)
                                    stRecvData.Img = "http://cfile238.uf.daum.net/image/112DFD0B4BFB58A27C4B03";

                                mMyData.arrUserAll_New.add(stRecvData);
                                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserAll_New.get(i).FanList.entrySet())
                                    mMyData.arrUserAll_New.get(i).arrFanList.add(entry.getValue());

                                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserAll_New.get(i).StarList.entrySet())
                                    mMyData.arrUserAll_New.get(i).arrStarList.add(entry.getValue());

                                if(mMyData.arrUserAll_New.get(i).Gender.equals("여자"))
                                {
                                    mMyData.arrUserWoman_New.add(mMyData.arrUserAll_New.get(i));
                          //          for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserWoman_New.get(j).FanList.entrySet())
                           //             mMyData.arrUserWoman_New.get(j).arrFanList.add(entry.getValue());

                                    j++;
                                }
                                else {
                                    mMyData.arrUserMan_New.add(mMyData.arrUserAll_New.get(i));
                         //           for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserMan_New.get(k).FanList.entrySet())
                            //            mMyData.arrUserMan_New.get(k).arrFanList.add(entry.getValue());

                                    k++;
                                }

                            }
                            i++;
                        }

                        bSetNew = true;
                        if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
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

        Double lStartLon = mMyData.getUserLon() - 10;
        Double lStartLat = mMyData.getUserLat() - 10;

        Double lEndLon = mMyData.getUserLon() + 10;
        Double IEndLat = mMyData.getUserLon() + 10;

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        Query query=ref
                .orderByChild("Lon")
                .startAt(lStartLon).endAt(lEndLon)
                ;


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

                                mMyData.arrUserAll_Near.add(stRecvData);
                                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserAll_Near.get(i).FanList.entrySet())
                                   mMyData.arrUserAll_Near.get(i).arrFanList.add(entry.getValue());

                                for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserAll_Near.get(i).StarList.entrySet())
                                    mMyData.arrUserAll_Near.get(i).arrStarList.add(entry.getValue());

                                if(mMyData.arrUserAll_Near.get(i).Gender.equals("여자"))
                                {
                                    mMyData.arrUserWoman_Near.add(mMyData.arrUserAll_Near.get(i));
                                  //  for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserWoman_Near.get(j).FanList.entrySet())
                                  //     mMyData.arrUserWoman_Near.get(j).arrFanList.add(entry.getValue());

                                    j++;
                                }
                                else {
                                    mMyData.arrUserMan_Near.add(mMyData.arrUserAll_Near.get(i));
                                  //  for (LinkedHashMap.Entry<String, FanData> entry : mMyData.arrUserMan_Near.get(k).FanList.entrySet())
                                   //     mMyData.arrUserMan_Near.get(k).arrFanList.add(entry.getValue());

                                    k++;
                                }

                            }
                            i++;
                        }

                        bSetNear = true;

                        if(bSetNear == true && bSetNew == true && bSetRich == true && bSetRecv == true && bMySet == true){
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

