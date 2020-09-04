package com.example.petmileymain;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LostAndFoundPost extends AppCompatActivity {

    private Button btnBack;
    private Button btnDelete;
    private Button btnRevise;
    public String lostandfound_id,m_f,missing_date,place,sex,type,tnr,kg,age,color,feature,etc,email,lostandfound_img;

    private static String IP_ADDRESS = "40.40.40.45";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_post);

        btnBack = (Button)findViewById(R.id.btnBack);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnRevise = (Button)findViewById(R.id.btnRevise);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView textViewM_f =(TextView)findViewById(R.id.textViewM_f);
        TextView textViewType =(TextView)findViewById(R.id.textViewType);
        TextView textViewSex =(TextView)findViewById(R.id.textViewSex);
        TextView textViewTnr =(TextView)findViewById(R.id.textViewTnr);
        TextView textViewAge =(TextView)findViewById(R.id.textViewAge);
        TextView textViewKg =(TextView)findViewById(R.id.textViewKg);
        TextView textViewColor =(TextView)findViewById(R.id.textViewColor);
        TextView textViewMissingdate =(TextView)findViewById(R.id.textViewMissingdate);
        TextView textViewPlace =(TextView)findViewById(R.id.textViewPlace);
        TextView textViewFeature =(TextView)findViewById(R.id.textViewFeature);
        TextView textViewEtc =(TextView)findViewById(R.id.textVieweEtc);
        TextView textViewEmail =(TextView)findViewById(R.id.textViewEmail);
        ImageView imgview = (ImageView) findViewById(R.id.imageView2);


        lostandfound_id = "";
        m_f = "";
        missing_date = "";
        place = "";
        sex = "";
        type = "";
        tnr = "";
        kg = "";
        age = "";
        color = "";
        feature = "";
        etc = "";
        email = "";
        lostandfound_img = "";

        Bundle extras = getIntent().getExtras();

        lostandfound_id = extras.getString("lostandfound_id");
        m_f = extras.getString("m_f");
        missing_date = extras.getString("missing_date");
        place = extras.getString("place");
        sex = extras.getString("sex");
        type = extras.getString("type");
        kg = extras.getString("kg");
        age = extras.getString("age");
        color = extras.getString("color");
        feature = extras.getString("feature");
        etc = extras.getString("etc");
        email = extras.getString("email");
        tnr = extras.getString("tnr");
        lostandfound_img = extras.getString("lostandfound_img");
        String why = "";

        textViewM_f.setText(m_f);

        if(email.equals(MainList.email)){
            btnRevise.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LostAndFoundPost.this);
                dialog.setTitle("게시글 삭제");
                dialog.setMessage("게시글을 삭제하시겠습니까?");
                dialog.setPositiveButton("삭제",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteData task = new DeleteData();
                        task.execute("http://" + IP_ADDRESS + "/LostAndFoundDelete.php", lostandfound_id);
                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.create();
                dialog.show();

            }
        });

        btnRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LostAndFoundRevise.class);


                intent.putExtra("lostandfound_id", lostandfound_id);
                intent.putExtra("m_f", m_f);
                intent.putExtra("missing_date", missing_date);
                intent.putExtra("place", place);
                intent.putExtra("sex", sex);
                intent.putExtra("type", type);
                intent.putExtra("tnr", tnr);
                intent.putExtra("kg", kg);
                intent.putExtra("age", age);
                intent.putExtra("color",color);
                intent.putExtra("feature", feature);
                intent.putExtra("etc", etc);
                intent.putExtra("email",email);
                intent.putExtra("lostandfound_img", lostandfound_img);
                startActivity(intent);
            }
        });

        textViewType.setText(type);
        textViewSex.setText(sex);
        textViewTnr.setText(tnr);
        textViewAge.setText(age);
        textViewKg.setText(kg);
        textViewColor.setText(color);
        textViewMissingdate.setText(missing_date);
        textViewPlace.setText(place);
        textViewFeature.setText(feature);
        textViewEtc.setText(etc);
        textViewEmail.setText(email);
        imgview.setImageBitmap(StringToBitMap(lostandfound_img));
        Log.d(type,"룰루랄라~~:" + etc );
        why = (String) textViewEtc.getText();
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

    class DeleteData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LostAndFoundPost.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Toast.makeText(LostAndFoundPost.this, result,Toast.LENGTH_LONG).show();

            if(result.equals("삭제되었습니다.")){
                Intent intent = new Intent(getBaseContext(), LostAndFoundMain.class);
                startActivity(intent);
            }

            Log.d("우횻", "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id ;


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
                Log.d("우웃", "POST response code - " + responseStatusCode);

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

                Log.d("", "DeleteData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

    }


}
