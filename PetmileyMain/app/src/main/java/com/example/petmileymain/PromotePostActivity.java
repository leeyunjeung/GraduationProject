package com.example.petmileymain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

//import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class PromotePostActivity extends AppCompatActivity {
    private static final String TAG = "PromoePostAcitivity";
    Toolbar toolbar;
    private static String IP_ADDRESS = "15.164.220.44";

    private String id="";
    private String note_memo = "";
    private String note_title = "";
    private String local = "";
    private String nickname = "";
    private String promote_picture = "";
    private String promote_email = "";
    private String type = "";
    private String adoption="";
    private String user = "";
    private String file_name = "";
    private String saveEmail; //현재 로그인한 이메일
    private SharedPreferences appData;
    public static Activity promotePostActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_post);


        appData = getSharedPreferences("appData", MODE_PRIVATE);
        saveEmail = appData.getString("saveEmail", "");


        promotePostActivity=PromotePostActivity.this;
        TextView TextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView TextViewMemo = (TextView) findViewById(R.id.textViewMemo);
        TextView TextViewNickname = (TextView) findViewById(R.id.textViewNickname);
        TextView TextViewLocal = (TextView) findViewById(R.id.textViewLocal);
        TextView TextViewType = (TextView) findViewById(R.id.textViewType);
        ImageView imgview = (ImageView) findViewById(R.id.imageView2);
        RoundedImageView imguser = (RoundedImageView)findViewById(R.id.promoteUserImg);

        toolbar = findViewById(R.id.promotePostToolbar);
        setSupportActionBar(toolbar);

        TextView TextViewAdoption = (TextView) findViewById(R.id.textViewAdoption); //추가

        Bitmap img,userImg;
        Bundle extras = getIntent().getExtras();

        note_title = extras.getString("note_title");
        note_memo = extras.getString("note_memo");
        nickname = extras.getString("nickname");
        promote_picture = extras.getString("promote_picture");
        local = extras.getString("local");
        promote_email = extras.getString("promote_email");
        id = extras.getString("id");
        type = extras.getString("type");
        adoption=extras.getString("adoption"); //추가
        user = extras.getString("userimg");
        file_name = extras.getString("file_name");

        img=StringToBitmap(promote_picture);
        userImg = StringToBitmap(user);

        TextViewTitle.setText(note_title);
        TextViewMemo.setText(note_memo);
        TextViewLocal.setText(local);
        TextViewNickname.setText(nickname);
        imgview.setImageBitmap(img);
        TextViewType.setText(type);
        TextViewAdoption.setText(adoption);
        imguser.setImageBitmap(userImg);



        TextViewNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserInformation.class);
                intent.putExtra("email",promote_email);
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
                Intent intent = new Intent(getApplicationContext(), PromoteReviseActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("local",local);
                intent.putExtra("type",type);
                intent.putExtra("note_title", note_title);
                intent.putExtra("note_memo",note_memo);
                intent.putExtra("promote_picture", promote_picture);
                intent.putExtra("adoption",adoption); //추가
                Log.d("adoption",adoption);
                startActivity(intent);
                break;


            case R.id.btnDelete:
                DeleteData task = new DeleteData();
                task.execute("http://" + IP_ADDRESS + "/promoteDelete.php",id);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public  boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.lost,menu);
        ActionBar actionBar = getSupportActionBar();

        if(!saveEmail.equals(promote_email)){
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
        Intent intent = new Intent(getApplicationContext(), PromoteActivity.class);
        startActivity(intent);
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
            target = "http://"+IP_ADDRESS+"/promoteDelete.php?id="+ id +"&file_name=" +file_name;
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
            Toast.makeText(PromotePostActivity.this, result,Toast.LENGTH_LONG).show();
            if(result.equals("게시글이 삭제되었습니다.")) {
                Intent intent = new Intent(getBaseContext(), PromoteActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }

}
