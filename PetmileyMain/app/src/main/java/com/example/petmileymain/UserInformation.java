package com.example.petmileymain;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;

public class UserInformation extends AppCompatActivity {

    private static String IP_ADDRESS = "3.34.44.142";

    private String img;
    private TextView name;
    private TextView phone;
    private ImageView userimg;
    private SharedPreferences appData;

    private String useremail;
    private String intenemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        name = (TextView) findViewById(R.id.txtUserNickname);
        phone = (TextView) findViewById(R.id.txtPhone);
        userimg = (ImageView) findViewById(R.id.imgUser);
        Button revise = (Button) findViewById(R.id.btnRevise);
        Button back = (Button)findViewById(R.id.btnBack);
        Button logout = (Button)findViewById(R.id.btnLogout);
        Button userPost = (Button)findViewById(R.id.btnUserPostView);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        useremail = appData.getString("saveEmail", "");

        Intent intent = getIntent();
        if((intenemail =intent.getStringExtra("email"))!=null) {
            if (!useremail.equals(intenemail)) {
                useremail = intenemail;
                revise.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
            } else {
                revise.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
            }
        }


        new BackgroundTask().execute();
        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRevise.class);
                intent.putExtra("nickname",name.getText());
                intent.putExtra("telephone",phone.getText());
                intent.putExtra("img",img);
                Log.d("img",img);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainList.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = appData.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        userPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserWrotePost.class);
                intent.putExtra("email",useremail);
                startActivity(intent);
            }
        });






    }




    class BackgroundTask extends AsyncTask<String,Void,String>{
        String target;

        protected void onPreExecute(){
            try {
                target = "http://"+IP_ADDRESS+"/UserInformation.php?email="+URLEncoder.encode(useremail,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {



            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

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
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }



        public  void onProgressUpdata(Void... values){
            super.onProgressUpdate();
        }

        public void onPostExecute(String result){

            if(result.contains("false")){
                Toast.makeText(UserInformation.this, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }

            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                String nickname,telephone;
                Bitmap bitmap;


                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    nickname = object.getString("nickname");
                    telephone = object.getString("telephone");
                    bitmap = StringToBitMap(object.getString("image"));
                    img=object.getString("image");

                    name.setText(nickname);
                    phone.setText(0+telephone);
                    userimg.setImageBitmap(bitmap);


                }
            }catch (Exception e){
                e.printStackTrace();
            }

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

    }

}
