package com.example.petmileymain;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

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
    private static String IP_ADDRESS = "40.40.40.62";
    private String id="";
    private String note_memo = "";
    private String note_title = "";
    private String local = "";
    private String nickname = "";
    private String promote_picture = "";
    private String promote_email = "";
    private String type = "";
    private String adoption=""; //추가
    private String user = "";
    public static Activity promotePostActivity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_post);
        promotePostActivity=PromotePostActivity.this;
        TextView TextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView TextViewMemo = (TextView) findViewById(R.id.textViewMemo);
        TextView TextViewNickname = (TextView) findViewById(R.id.textViewNickname);
        TextView TextViewLocal = (TextView) findViewById(R.id.textViewLocal);
        TextView TextViewType = (TextView) findViewById(R.id.textViewType);
        ImageView imgview = (ImageView) findViewById(R.id.imageView2);
        ImageView imguser = (ImageView)findViewById(R.id.promoteUserImg);

        TextView TextViewAdoption = (TextView) findViewById(R.id.textViewAdoption); //추가

        Button btnRevise = (Button) findViewById(R.id.btnRevise);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        Button btnBack = (Button) findViewById(R.id.btnBack);

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

        if (!MainList.email.equals(promote_email) ) {
            btnDelete.setVisibility(View.GONE);
            btnRevise.setVisibility(View.GONE);
        }
        else {
            btnDelete.setVisibility(View.VISIBLE);
            btnRevise.setVisibility(View.VISIBLE);
            Log.d(TAG,"후..."+MainList.email+"하..."+promote_email);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData task = new DeleteData();
                task.execute("http://" + IP_ADDRESS + "/promoteDelete.php",id);


            }
        });

        btnRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        TextViewNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserInformation.class);
                intent.putExtra("email",promote_email);
                startActivity(intent);
            }
        });

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
            target = "http://"+IP_ADDRESS+"/promoteDelete.php?id="+ id;
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