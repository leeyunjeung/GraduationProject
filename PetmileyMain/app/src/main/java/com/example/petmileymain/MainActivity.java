package com.example.petmileymain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean saveLoginData; //로그인한적이 있는지 체크
    private String saveEmail;
    private SharedPreferences appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        if(saveLoginData){  //자동 로그인한 적 있으면
            Intent intent = new Intent(getApplicationContext(), MainListActivity.class);
            intent.putExtra("email",saveEmail);
            startActivity(intent);
        }

        Button Join = (Button)findViewById(R.id.btn_signup);
        Button Login = (Button)findViewById(R.id.btn_signin);

        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    // 설정값을 불러오는 함수
    private void load() {
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        saveEmail = appData.getString("saveEmail", "");
    }

}
