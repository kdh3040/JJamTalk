package com.hodo.jjamtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hodo.jjamtalk.Data.MyData;
import com.hodo.jjamtalk.Data.UserData;
import com.hodo.jjamtalk.Firebase.FirebaseData;

import org.json.JSONException;
import org.json.JSONObject;




import java.util.ArrayList;

import com.google.common.collect.Lists;

import java.util.List;



import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class MainActivity extends AppCompatActivity {
    SpringIndicator springIndicator;
    ScrollerViewPager viewPager;
    private FirebaseData mFireBaseData = FirebaseData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        viewPager = (ScrollerViewPager)findViewById(R.id.view_pager);
        springIndicator = (SpringIndicator)findViewById(R.id.indicator);

        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(GuideFragment.class, getBgRes(),getTitles());

        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);

        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();
        springIndicator.setViewPager(viewPager);


    }
    private List<String> getTitles(){
        return Lists.newArrayList("Near", "Hot", "Rank", "New");
    }

    private List<Integer> getBgRes(){
        return Lists.newArrayList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View menu_Main  = inflater.inflate(R.layout.menu_main,null);
        actionBar.setCustomView(menu_Main);
        Toolbar parent = (Toolbar)menu_Main.getParent();
        parent.setContentInsetsAbsolute(0,0);

        Button button = (Button)findViewById(R.id.btn_profile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyPageActivity.class));
            }
        });


        final ArrayList<UserData> arrTemp = new ArrayList<UserData>();

        final DatabaseReference table = FirebaseDatabase.getInstance().getReference("Users");
        Query query=table.orderByChild("Heart").limitToLast(10);//키가 id와 같은걸 쿼리로 가져옴

        query.addListenerForSingleValueEvent(new ValueEventListener() {//그걸 처리해줘야겠지
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                JSONObject json=null;
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    UserData tempDB = new UserData();
                    //   json=new JSONObject(dataSnapshot.getValue());
                    tempDB = fileSnapshot.getValue(UserData.class);

                    arrTemp.add(tempDB);
                    Log.d("MainAc!!", "!!!! " + tempDB.Heart + " @@@@@ " + tempDB.NickName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }
}
