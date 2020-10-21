package com.example.petmileymain;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ReviewPostActivity extends AppCompatActivity {
    private static final String TAG = "ReviewPostAcitivity";

    private static String IP_ADDRESS = "15.164.220.44";
    Toolbar toolbar;
    private String id="";
    private String note_memo = "";
    private String note_title = "";
    private String nickname = "";
    private String picture = "";
    private String review_email = "";
    private String categorize = "";
    private String file_name = "";
    private String userimg;
    public static Activity reviewPostActivity;
    private SharedPreferences appData;
    private String saveEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_post);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        saveEmail = appData.getString("saveEmail", "");

        reviewPostActivity=ReviewPostActivity.this;
        TextView TextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView TextViewMemo = (TextView) findViewById(R.id.textViewMemo);
        TextView TextViewNickname = (TextView) findViewById(R.id.textViewNickname);
        TextView TextViewCategorize = (TextView) findViewById(R.id.textViewCategorize);
        toolbar = findViewById(R.id.reviewPostToobar);
        setSupportActionBar(toolbar);

        ImageView imgview = (ImageView) findViewById(R.id.imageView2);
        ImageView imauser = (ImageView) findViewById(R.id.reviewUserImg);


        Bundle extras = getIntent().getExtras();


        id = extras.getString("id");
        note_title = extras.getString("note_title");
        note_memo = extras.getString("note_memo");
        nickname = extras.getString("nickname");
        picture = extras.getString("picture");
        review_email = extras.getString("review_email");
        categorize = extras.getString("categorize");
        userimg = extras.getString("userimg");
        file_name = extras.getString("file_name");


        TextViewTitle.setText(note_title);
        TextViewMemo.setText(note_memo);
        TextViewNickname.setText(nickname);
        imgview.setImageBitmap(StringToBitmap(picture));
        imauser.setImageBitmap(StringToBitmap(userimg));
        TextViewCategorize.setText(categorize);


        TextViewNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserInformation.class);
                intent.putExtra("email",review_email);
                startActivity(intent);
            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;


            case R.id.btnRevise:
                Intent intent = new Intent(getApplicationContext(), ReviewReviseActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("note_title", note_title);
                intent.putExtra("note_memo",note_memo);
                intent.putExtra("picture", picture);
                intent.putExtra("categorize", categorize);
                startActivity(intent);
                break;


            case R.id.btnDelete:
                DeleteData task = new DeleteData();
                task.execute(id);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public  boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.lost,menu);
        ActionBar actionBar = getSupportActionBar();

        if(!saveEmail.equals(review_email)){
            menu.findItem(R.id.btnRevise).setVisible(false);
            menu.findItem(R.id.btnDelete).setVisible(false);
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public Bitmap StringToBitmap(String encodedString) {
        try {
            encodedString = encodedString.replace(" ","+");
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    class DeleteData extends AsyncTask<String, Void, String> {
        String target;

        protected void onPreExecute(){
            target = "http://"+IP_ADDRESS+"/reviewDelete.php?id="+ id + "&file_name=" + file_name;
        }
        @Override
        protected String doInBackground(String... params) {


            try {

                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


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
            Toast.makeText(ReviewPostActivity.this, result,Toast.LENGTH_LONG).show();
            if(result.equals("게시글이 삭제되었습니다.")) {
                Intent intent = new Intent(getBaseContext(), ReviewActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }

}

