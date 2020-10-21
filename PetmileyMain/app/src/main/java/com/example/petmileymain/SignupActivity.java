package com.example.petmileymain;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;

    private static String IP_ADDRESS = "15.164.220.44";

    private static String TAG = "petmily";

    private EditText mEditTextNickname;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextTelephone;
    private EditText mEditTextPasswordCheck;
    private TextView mTextViewResult;
    private RoundedImageView user;
    Button buttonInsert;
    TextView checkemail;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEditTextNickname = (EditText)findViewById(R.id.editText_main_nickname);
        mEditTextEmail = (EditText)findViewById(R.id.editText_main_email);
        mEditTextTelephone = (EditText)findViewById(R.id.editText_main_telephone);
        mEditTextPassword = (EditText)findViewById(R.id.editText_main_password);
        mEditTextPasswordCheck = (EditText)findViewById(R.id.editText_main_passwordcheck);
        checkemail = findViewById(R.id.signupCheck);
        user = (RoundedImageView)findViewById(R.id.signupImg);
        toolbar = findViewById(R.id.singupToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        buttonInsert = (Button)findViewById(R.id.button_main_insert);
        user.setImageResource(R.drawable.blank);


        mEditTextEmail.addTextChangedListener(new TextWatcher() {
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
                    buttonInsert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"이메일을 확인해주세요",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    checkemail.setTextColor(Color.parseColor("#A9D0F5"));
                    checkemail.setText("올바른 이메일입니다.");

                    buttonInsert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            RoundedDrawable drawable = (RoundedDrawable) user.getDrawable();
                            Bitmap bitmap = drawable.getSourceBitmap();

                            String nickname = mEditTextNickname.getText().toString();
                            String email = mEditTextEmail.getText().toString();
                            String password = mEditTextPassword.getText().toString();
                            String passwordCheck = mEditTextPasswordCheck.getText().toString();
                            String telephone = mEditTextTelephone.getText().toString();
                            String image = BitMapToString(resize(bitmap));

                            if (!password.equals(passwordCheck)){
                                Toast.makeText(SignupActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                            }
                            else{
                                InsertData task = new InsertData();
                                task.execute("http://" + IP_ADDRESS + "/insert.php", email,password,telephone,nickname,image);

                            }


                        }
                    });

                }
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);

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

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

    private Bitmap resize(Bitmap bm){
        Configuration config=getResources().getConfiguration();
        if(config.smallestScreenWidthDp>=800)
            bm = Bitmap.createScaledBitmap(bm, 400, 240, true);
        else if(config.smallestScreenWidthDp>=600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if(config.smallestScreenWidthDp>=400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if(config.smallestScreenWidthDp>=360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        return bm;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    user.setImageBitmap(img);
                }catch(Exception e)
                {

                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(), "선택 사진 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignupActivity.this,
                    "Please Wait", null, true, true);
        }




        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Toast.makeText(SignupActivity.this, result,Toast.LENGTH_LONG).show();
            if(result.equals("새로운 사용자를 추가했습니다.")){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            String password = (String)params[2];
            String telephone = (String)params[3];
            String nickname = (String)params[4];
            String image = (String)params[5];

            String serverURL = (String)params[0];
            String postParameters = "email="+ email +"&password=" + password + "&telephone=" + telephone+ "&nickname=" + nickname + "&image=" + image;


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

}
