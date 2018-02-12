package com.hodo.talkking;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hodo.talkking.Data.MyData;
import com.hodo.talkking.Util.AwsFunc;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AwsFunc mAwsFunc = AwsFunc.getInstance();
    private MyData mMyData = MyData.getInstance();

    private TextView mTextInfo;
    private EditText mEditEmail;
    private EditText mEditPwd;
    private Button mBtnSignUp;

    private String mStrEmail;
    private String mStrPwd;
    private String mUserIdx;

    private boolean bConfEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        mTextInfo = (TextView) findViewById(R.id.signup_Info);

        mEditEmail = (EditText)findViewById(R.id.signup_EditEmail);
        mEditPwd = (EditText)findViewById(R.id.signup_EditPwd);

        mBtnSignUp = (Button)findViewById(R.id.signup_BtnSignUp);

        bConfEmail = false;

        mTextInfo.setText("이메일과 비밀 번호를 6자리 이상 입력해주세요");

      /*  mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mStrEmail = mEditEmail.getText().toString().trim();
                mStrPwd = mEditPwd.getText().toString().trim();

                if(mStrEmail != null && mStrPwd != null) {
                    mAuth.createUserWithEmailAndPassword(mStrEmail,mStrPwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mUserIdx= mAwsFunc.CreateUserIdx(mStrEmail);
                                        mMyData.setUserIdx(mUserIdx);
                                        Intent intent = new Intent(SignUpActivity.this, InputProfile.class);
                                        startActivity(intent);

                                    } else {
                                       // Toast.makeText(SignUpActivity.this,"이메일 입력이 잘못되었습니다",Toast.LENGTH_LONG).show();
                                        //보통 이메일이 이미 존재하거나, 이메일 형식이아니거나, 비밀번호가 6자리 이상이 아닐 때 발생 
                                    }
                                }
                            });
                }
            }
        });*/

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private boolean isValidEmail(String str){
        if(str == null || TextUtils.isEmpty(str)){
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(str).matches();
        }
    }

}
