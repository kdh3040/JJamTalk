package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Firebase.FirebaseData;

/**
 * Created by mjk on 2017. 8. 5..
 */

public class MyProfileActivity extends AppCompatActivity {
    private EditText txt_Memo, txt_School, txt_Company, txt_Title;

    private MyData mMyData = MyData.getInstance();
    private FirebaseData mFireBaseData = FirebaseData.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_Memo = (EditText)findViewById(R.id.MyProfile_Memo);
        txt_School = (EditText)findViewById(R.id.MyProfile_School);
        txt_Company = (EditText)findViewById(R.id.MyProfile_Company);
        txt_Title = (EditText)findViewById(R.id.MyProfile_Title);

        txt_Memo.setText(mMyData.getUserMemo());
        txt_School.setText(mMyData.getUserSchool());
        txt_Company.setText(mMyData.getUserCompany());
        txt_Title.setText(mMyData.getUserTitle());


    }

    @Override
    public void onBackPressed(){

        mMyData.setProfileData(txt_Memo.getText(), txt_School.getText(), txt_Company.getText(), txt_Title.getText());

        mFireBaseData.SaveData(mMyData.getUserIdx());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
