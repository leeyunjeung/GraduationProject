package com.example.petmileymain;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class ShelterPostActivite extends AppCompatActivity {


    TextView mName, mAddr, mDt, mAge, mSex, mWeight, mNeuter, mState;
    TextView mHappenDt, mHappenAddr;
    Button mCall, mMap;
    Toolbar mToolber;
    ImageView pet;

    String name, addr, dt, age, sex,state, weight, neuter,call,map, happenDt, happenAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_post_activite);

        mName = findViewById(R.id.name);
        mAddr = findViewById(R.id.addr);
        mDt = findViewById(R.id.dt);
        mAge = findViewById(R.id.age);
        mSex = findViewById(R.id.sex);
        mWeight = findViewById(R.id.weight);
        mNeuter = findViewById(R.id.neuter);
        mState = findViewById(R.id.state);
        mHappenDt = findViewById(R.id.happenDt);
        mHappenAddr = findViewById(R.id.happenAddr);

        mCall = findViewById(R.id.call);
        mMap = findViewById(R.id.map);

        mToolber = findViewById(R.id.shelterPostToolbar);
        setSupportActionBar(mToolber);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        pet = (ImageView)findViewById(R.id.imageView2);

        Intent intent = getIntent();
        name = intent.getStringExtra("kindCd");
        addr = intent.getStringExtra("careAddr");
        dt = intent.getStringExtra("noticeSdt") + "~" +intent.getStringExtra("noticeEdt");
        age = intent.getStringExtra("age");
        sex = intent.getStringExtra("sexCd");
        weight = intent.getStringExtra("weight");
        neuter = intent.getStringExtra("neuterYn");
        call = intent.getStringExtra("careTel");
        map = intent.getStringExtra("careAddr");
        state = intent.getStringExtra("processState");
        happenAddr = intent.getStringExtra("happenPlace");
        happenDt = intent.getStringExtra("happenDt");


        mName.setText(name);
        mAddr.setText(addr);
        mDt.setText(dt);
        mAge.setText(age);
        mSex.setText(sex);
        mWeight.setText(weight);
        mNeuter.setText(neuter);
        mState.setText(state);
        mHappenAddr.setText(happenAddr);
        mHappenDt.setText(happenDt);

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+call));
                startActivity(intent1);
            }
        });

        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0.0?q="+map));
                startActivity(intent2);
            }
        });

        Picasso.get().load(intent.getStringExtra("popfile")).into(pet);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
