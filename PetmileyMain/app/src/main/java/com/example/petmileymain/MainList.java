package com.example.petmileymain;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainList extends AppCompatActivity {

    private static String TAG = "petmiley";
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
