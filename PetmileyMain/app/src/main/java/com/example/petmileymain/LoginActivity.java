package com.example.petmileymain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {
    private String saveEmail;
    private SharedPreferences appData;
    private boolean saveLoginData; //로그인한적이 있는지 체크
    private CheckBox checkBox;
    Toolbar toolbar;
    private static String IP_ADDRESS = "15.164.220.44";
    private static String TAG = "phptest";

    private EditText editEmail;
    private EditText editPassword;
    Button buttonSignUp;
    TextView checkemail;
    //private TextView mTextViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        editEmail = (EditText)findViewById(R.id.email);
        editPassword = (EditText)findViewById(R.id.password);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkemail = findViewById(R.id.txtEmail);
        //mTextViewResult = (TextView)findViewById(R.id.result);
        toolbar = findViewById(R.id.loginToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        buttonSignUp = (Button)findViewById(R.id.signup);

        //mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
                    checkemail.setTextColor(Color.parseColor("#FA5858"));
                    checkemail.setText("올바른 이메일 형식이 아닙니다.");
                    buttonSignUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"이메일을 확인해주세요",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    checkemail.setTextColor(Color.parseColor("#A9D0F5"));
                    checkemail.setText("올바른 이메일입니다.");
                    buttonSignUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String email = editEmail.getText().toString();
                            String password = editPassword.getText().toString();

                            InsertData task = new InsertData();
                            task.execute("http://" + IP_ADDRESS + "/login.php", email,password);


                        }
                    });
                }
            }
        });





    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN= Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );



    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            final String email = editEmail.getText().toString();

            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, result,Toast.LENGTH_LONG).show();

            if(result.equals("로그인 완료.")){
                Intent intent = new Intent(getApplicationContext(), MainListActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
                save();
            }

            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            String password = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "email=" + email + "&password=" + password;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

    }
    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_LOGIN_DATA",checkBox.isChecked());
        editor.putString("saveEmail", editEmail.getText().toString().trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }





}