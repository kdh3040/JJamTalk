package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Util.AwsFunc;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AwsFunc mAwsFunc = AwsFunc.getInstance();
    private MyData mMyData = MyData.getInstance();

    private TextView mTextInfo;
    private EditText mEditText;
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

        mEditText = (EditText)findViewById(R.id.signup_EditEmail);
        mBtnSignUp = (Button)findViewById(R.id.signup_BtnSignUp);
        mEditText.setHint("이메일");

        bConfEmail = false;

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bConfEmail == false)
                {
                    mStrEmail = mEditText.getText().toString().trim();
                    mUserIdx = mAwsFunc.CreateUserIdx(mStrEmail);

                    mTextInfo.setText("입력하신 이메일 " + "\n" + mStrEmail + "\n\n" + "비밀 번호를 6자리 이상 입력해주세요");
                    mEditText.getEditableText().clear();

                    mEditText.setHint("비밀번호");

                    mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    mBtnSignUp.setText("회원가입");

                    bConfEmail = true;
                }

                else
                {
                    mStrPwd = mEditText.getText().toString().trim();

                    if(mStrEmail != null && mStrPwd != null) {

                        mAuth.createUserWithEmailAndPassword(mStrEmail,mStrPwd)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mMyData.setUserIdx(mUserIdx);
                                            Intent intent = new Intent(SignUpActivity.this, InputProfile.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(SignUpActivity.this,"이메일 형식이 아닙니다",Toast.LENGTH_LONG).show();
                                            int asdasd =0;
                                            //보통 이메일이 이미 존재하거나, 이메일 형식이아니거나, 비밀번호가 6자리 이상이 아닐 때 발생 
                                        }
                                    }
                                });
                    }
                }

            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }
}
