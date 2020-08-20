package com.example.petmileymain;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView txtFeature;
    TextView txtPost;
    ImageView pet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_post_activite);

        txtFeature = (TextView)findViewById(R.id.txtFeature);
        txtPost =(TextView)findViewById(R.id.txtPost);
        pet = (ImageView)findViewById(R.id.imageView2);
        Button back = (Button)findViewById(R.id.btnBack);

        Intent intent = getIntent();
        txtFeature.setText("시군명: " + intent.getStringExtra("orgNm") + "\n접수일자: " + intent.getStringExtra("happenDt") + "\n공고날짜: " + intent.getStringExtra("noticeSdt") + "~"
                + intent.getStringExtra("noticeEdt") + "\n보호소명: " + intent.getStringExtra("careNm") + "\n보호소 전화번호: " + intent.getStringExtra("careTel") + "\n보호조 주소: " + intent.getStringExtra("careAddr"));
        txtPost.setText("상태: " + intent.getStringExtra("specialMark") + "\n품종: " + intent.getStringExtra("kindCd") + "\n색상: " + intent.getStringExtra("colorCd") + "\n나이: "
                + intent.getStringExtra("age") + "\n체중: " + intent.getStringExtra("weight") + "\n성별: " + intent.getStringExtra("sexCd") + "\n중성화: " + intent.getStringExtra("neuterYn") + "\n특징: " + intent.getStringExtra("noticeComment"));
        //pet.setImageBitmap((Bitmap)intent.getParcelableExtra("img"));
        Picasso.get().load(intent.getStringExtra("popfile")).into(pet);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShelterActivity.class);
                startActivity(intent);
            }
        });
    }
}
