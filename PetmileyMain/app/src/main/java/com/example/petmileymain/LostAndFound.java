package com.example.petmileymain;


import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LostAndFound extends AppCompatActivity {

    private static String TAG = "petmily";

    private static String IP_ADDRESS = "15.164.220.44";
    private String saveEmail;
    private static final int REQUEST_CODE = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;
    private ImageView imageView;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    private SharedPreferences appData;

    private Button btnInsert;


    private EditText editDate;
    private EditText editColor,editFeature,editEtc;
    private TextView tvtest;

    final Calendar myCalendar = Calendar.getInstance();




    final DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);



        imageView = findViewById(R.id.image);
        btnInsert = findViewById(R.id.btnInsert);

        editDate=findViewById(R.id.etDate);
        editColor=findViewById(R.id.etColor);
        editFeature=findViewById(R.id.etFeature);
        editEtc=findViewById(R.id.etEtc);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        saveEmail = appData.getString("saveEmail", "");


        final Spinner spinnerLocal = (Spinner)findViewById(R.id.spinnerLocal);
        final Spinner spinnerMF =(Spinner)findViewById(R.id.spinnerMF);
        final Spinner spinnerSex =(Spinner)findViewById(R.id.spinnerSex);
        final Spinner spinnerAge =(Spinner)findViewById(R.id.spinnerAge);
        final Spinner spinnerKg =(Spinner)findViewById(R.id.spinnerKg);
        final Spinner spinnerTnr =(Spinner)findViewById(R.id.spinnerTnr);
        final Spinner spinnerType =(Spinner)findViewById(R.id.spinnerType);

        editDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(LostAndFound.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });



        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                String m_f = spinnerMF.getSelectedItem().toString();
                String sex = spinnerSex.getSelectedItem().toString();
                String age = spinnerAge.getSelectedItem().toString();
                String kg = spinnerKg.getSelectedItem().toString();
                String type = spinnerType.getSelectedItem().toString();
                String tnr = spinnerTnr.getSelectedItem().toString();
                String missing_date = editDate.getText().toString();
                String place = spinnerLocal.getSelectedItem().toString();
                String color = editColor.getText().toString();
                String etc = editEtc.getText().toString();
                String feature = editFeature.getText().toString();
                String email = saveEmail;
                String picture =  BitmapToString(bitmap);

                Log.d("HEY!!!!::",editDate.getText().toString());
                LostAndFound.InsertData task = new LostAndFound.InsertData();
                task.execute("http://" + IP_ADDRESS + "/lostandfound.php", email,sex,missing_date,place,m_f,age,kg,type,tnr,color,etc,feature,picture);

                dialog = ProgressDialog.show(LostAndFound.this, "", "Uploading file...", true);

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d("업로드","uploading started.....");
                            }
                        });

                        uploadFile();

                    }
                }).start();

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


    // 사진
    public int uploadFile() {
        String fileName = tempFile.getName();

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String serverURL = "http://" + IP_ADDRESS + "/lostandfoundfile.php";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;


        if (!tempFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :" + fileName);

            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            return 0;

        }
        else

        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(tempFile);
                URL url = new URL(serverURL);

                // HttpURLConnection 통신
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                //write data
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                Log.d("Test","image byte is" + bytesRead);

                // read image
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                Log.e("Test","File is written");
                // Responses from the server (code and message)

                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(LostAndFound.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();

                        }

                    });

                }


                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

                InputStream is = conn.getInputStream();

                StringBuffer b = new StringBuffer();
                for(int ch = 0; (ch = is.read()) != -1;) {
                    b.append((char) ch);
                }
                is.close();
                Log.e("Test", b.toString());

            } catch (MalformedURLException ex) {
                dialog.dismiss();
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LostAndFound.this, "MalformedURLException", Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(LostAndFound.this, "Got Exception : see logcat ",Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Exception!", "Exception : " + e.getMessage(), e);

            }
            dialog.dismiss();

            return serverResponseCode;
        }

    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LostAndFound.this,"Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Toast.makeText(LostAndFound.this, result,Toast.LENGTH_LONG).show();
            if(result.equals("새로운 글을 추가했습니다.")){
                Intent intent = new Intent(getApplicationContext(), LostAndFoundMain.class);
                startActivity(intent);

            }
            Log.d(TAG, "POST response  - " + result);
        }



        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            String sex = (String)params[2];
            String missing_date= (String)params[3];
            String place = (String)params[4];
            String m_f = (String)params[5];
            String age = (String)params[6];
            String kg = (String)params[7];
            String type = (String)params[8];
            String tnr = (String)params[9];
            String color =(String)params[10];
            String etc = (String)params[11];
            String feature = (String)params[12];
            String picture = (String)params[13];
            String fileName = tempFile.getName();



            String serverURL = (String)params[0];
            String postParameters = "&email="+ email +"&sex=" + sex + "&missing_date=" + missing_date + "&place=" + place +  "&m_f=" + m_f +  "&age=" + age +  "&kg=" + kg +  "&type=" + type +  "&tnr=" + tnr +  "&color=" + color +  "&etc=" + etc +  "&feature=" + feature + "&picture=" + picture +"&fileName="+fileName;


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
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FROM_ALBUM){
            Uri photouri = data.getData();
            Cursor cursor = null;
            try{

                String[] proj = {MediaStore.Images.Media.DATA};
                assert photouri != null;
                cursor = getContentResolver().query(photouri,proj,null,null,null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            }finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = (EditText) findViewById(R.id.etDate);
        et_date.setText(sdf.format(myCalendar.getTime()));

    }

    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imageView.setImageBitmap(originalBm);

    }
}
