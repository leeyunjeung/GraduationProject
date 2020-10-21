package com.example.petmileymain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class UserRevise extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1;
    private static final int REQUEST_CODE = 0;

    private static String IP_ADDRESS = "15.164.220.44";

    private SharedPreferences appData;
    private String saveEmail;

    EditText Editpassword;
    EditText Editnickname;
    EditText Edittelephone;
    ImageView imageView;
    ImageView user;
    Button btnOk;
    Button btnBack;

    private String nickname;
    private String telephone;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_revise);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        saveEmail = appData.getString("saveEmail", "");

        Editpassword = (EditText)findViewById(R.id.etPw);
        Editnickname = (EditText)findViewById(R.id.etNick);
        Edittelephone = (EditText)findViewById(R.id.etPhone);
        imageView=(ImageView)findViewById(R.id.imageView);
        btnOk = (Button)findViewById(R.id.btnOk);
        btnBack = (Button)findViewById(R.id.btnBack);
        user = findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();

        nickname=extras.getString("nickname");
        telephone=extras.getString("telephone");
        img=extras.getString("img");
        Log.d("img",img);

        Editnickname.setText(nickname);
        Edittelephone.setText(telephone);
        imageView.setImageBitmap(StringToBitMap(img));

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) user.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                String password = Editpassword.getText().toString();
                String nickname = Editnickname.getText().toString();
                String telephone = Edittelephone.getText().toString();
                String temp = BitMapToString(resize(bitmap));

                InformainRevise task = new InformainRevise();
                task.execute(password,nickname,telephone,temp);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    public Bitmap StringToBitMap(String encodedString) {
        try {
            encodedString = encodedString.replace(" ","+");
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bitmap;

        } catch (Exception e) {
            Log.e("exception",e.getMessage());
            //e.getMessage();
            return null;

        }

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
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }





    /*private void setImage(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(),options);

        user.setImageBitmap(originalBm);

    }*/






    class InformainRevise extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String target;

        protected void onPreExecute(){
            try {
                target = "http://"+IP_ADDRESS+"/InformationRevise.php?email="+ URLEncoder.encode(saveEmail,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            String password = (String)params[0];
            String nickname = (String)params[1];
            String telephone = (String)params[2];
            String temp = (String)params[3];

            String postParameters ="password=" + password + "&nickname=" + nickname+ "&telephone=" + telephone + "&temp=" + temp;


            try {

                URL url = new URL(target);
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

                return new String("Error: " + e.getMessage());
            }
        }

        public void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(UserRevise.this, result,Toast.LENGTH_LONG).show();
            if(result.equals("정보를 저장했습니다.")) {
                Intent intent = new Intent(getApplicationContext(), UserInformation.class);
                startActivity(intent);
            }
        }


    }
}
