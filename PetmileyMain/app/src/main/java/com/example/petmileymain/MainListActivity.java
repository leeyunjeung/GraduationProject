package com.example.petmileymain;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;




public class MainListActivity extends AppCompatActivity {

    private static String TAG = "petmiley";
    private static String IP_ADDRESS ="15.164.220.44";
    public static String email;

    private Button btnSelter;
    private Button btnMissingFind;
    private Button btnPromote;
    private Button btnReview;
    private Button btnUserInformation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = getIntent().getStringExtra("email");
        btnSelter = (Button)findViewById(R.id.btnShelter);
        btnMissingFind = (Button)findViewById(R.id.btnMissingFind);
        btnPromote = (Button)findViewById(R.id.btnPromote);
        btnReview =(Button)findViewById(R.id.btnReview);
        btnUserInformation =(Button)findViewById(R.id.btnUserInformation);




        this.ButtonListener();

    }


    public void ButtonListener(){

        btnSelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShelterActivity.class);
                startActivity(intent);
            }
        });

        btnMissingFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LostAndFoundMain.class);
                startActivity(intent);

            }
        });

        btnPromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PromoteActivity.class);
                startActivity(intent);

            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                startActivity(intent);

            }
        });

        btnUserInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserInformation.class);
                startActivity(intent);
            }

        });
    }


}
