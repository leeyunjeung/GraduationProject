package com.example.petmileymain;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReviewWriteActivity extends AppCompatActivity {
    private static String TAG = "petmily";
    private static String IP_ADDRESS = "15.164.220.44";

    private int enter = 0;
    private static final int REQUEST_CODE = 0;
    private static final int PICK_FROM_ALBUM = 1;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    private File tempFile;
    private ImageView imageView;

    private Button btnInsert;
    private Button btnBack;
    private EditText editTextTitle;
    private EditText editTextMemo;
    private int itemIndex;

    private SharedPreferences appData;
    private String saveEmail;
    private Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write );


        toolbar = findViewById(R.id.review_write_toolbar);
        setSupportActionBar(toolbar);


        appData = getSharedPreferences("appData", MODE_PRIVATE);
        saveEmail = appData.getString("saveEmail", "");

        imageView = findViewById(R.id.image);
        btnInsert = findViewById(R.id.btnInsert);
        btnBack = findViewById(R.id.btnBack);
        editTextTitle=findViewById(R.id.editTextTitle);
        editTextMemo=findViewById(R.id.editTextMemo);

        final Spinner spinner = (Spinner) findViewById(R.id.categorize);

        final ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(this, R.array.categorize, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(sAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                Toast.makeText(ReviewWriteActivity.this, sAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                itemIndex = position;
                Log.d(TAG,"인덱스:"+itemIndex);
            }
            @Override
            public void onNothingSelected(AdapterView<?>  parent) {
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ReviewPostActivity.class);
                startActivity(intent);
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                String note_title = editTextTitle.getText().toString();
                String note_memo = editTextMemo.getText().toString();
                String categorize = spinner.getSelectedItem().toString();
                String email = saveEmail;
                String picture =  BitmapToString(resize(bitmap));
                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/reviewInsert.php", email,note_title,note_memo,categorize,picture);


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


    public  boolean onCreateOptionsMenu(Menu menu){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        return true;
    }

    public int uploadFile() {
        String fileName = tempFile.getName();

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String serverURL = "http://" + IP_ADDRESS + "/reviewFile.php";
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

                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

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

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)

                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(ReviewWriteActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();

                        }

                    });

                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                dialog.dismiss();
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ReviewWriteActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(ReviewWriteActivity.this, "Got Exception : see logcat ",Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Exception!", "Exception : " + e.getMessage(), e);

            }
            //dialog.dismiss();
            return serverResponseCode;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(getBaseContext(), ReviewPostActivity.class);
        startActivity(intent);

    }
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ReviewWriteActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(ReviewWriteActivity.this, result,Toast.LENGTH_LONG).show();
            if(result.equals("새로운 글을 추가했습니다.")){
                finish();
                ReviewActivity activity=(ReviewActivity)ReviewActivity.reviewActivity;
                activity.finish();
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                startActivity(intent);

            }
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            String note_title = (String)params[2];
            String note_memo= (String)params[3];
            String categorize = (String)params[4];
            String picture = (String)params[5];
            String fileName = tempFile.getName();

            String serverURL = (String)params[0];
            String postParameters = "email="+ email +"&note_title=" + note_title + "&note_memo=" + note_memo + "&categorize=" + categorize+"&picture="+ picture + "&fileName=" + fileName ;


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



    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imageView.setImageBitmap(originalBm);

    }
}