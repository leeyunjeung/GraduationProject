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
//import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;
import java.util.List;


public class PromoteWriteActivity extends AppCompatActivity {
    private static String TAG = "petmily";

    private static String IP_ADDRESS = "15.164.220.44";

    private int enter = 0;
    private static final int REQUEST_CODE = 0;
    private String saveEmail;
    private ImageView imageView;

    private File tempFile;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    private static final int PICK_FROM_ALBUM = 1;

    private Button btnInsert;
    private Button btnBack;
    private Button btnLocal;
    private EditText editTextTitle;
    private EditText editTextMemo;
    private int sigungu=R.array.nullarray;
    ArrayAdapter<CharSequence> sigunguAdapter;

    private SharedPreferences appData;
    private Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_write );


        toolbar = findViewById(R.id.promote_write_toolbar);
        setSupportActionBar(toolbar);


        imageView = findViewById(R.id.image);
        btnInsert = findViewById(R.id.btnInsert);
        btnBack = findViewById(R.id.btnBack);
        btnLocal = findViewById(R.id.btnLocal);
        editTextTitle=findViewById(R.id.editTextTitle);
        editTextMemo=findViewById(R.id.editTextMemo);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        saveEmail = appData.getString("saveEmail", "");

        //스피너 3개
        final Spinner typeSpinner = (Spinner) findViewById(R.id.type);
        final ArrayAdapter<CharSequence> typesAdapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_dropdown_item);

        final Spinner siSpinner = (Spinner) findViewById(R.id.si); // 시/도
        final ArrayAdapter<CharSequence> siAdapter = ArrayAdapter.createFromResource(this, R.array.si, android.R.layout.simple_spinner_dropdown_item);

        final Spinner sigunguSpinner = (Spinner) findViewById(R.id.sigungu); //시/군/구
        sigunguAdapter = ArrayAdapter.createFromResource(this, sigungu, android.R.layout.simple_spinner_dropdown_item);

        final Spinner adoptionSpinner = (Spinner) findViewById(R.id.adoption);
        final ArrayAdapter<CharSequence> adoptionAdapter = ArrayAdapter.createFromResource(this, R.array.adoption, android.R.layout.simple_spinner_dropdown_item);
        adoptionSpinner.setAdapter(adoptionAdapter);
        adoptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                Toast.makeText(PromoteWriteActivity.this, adoptionAdapter.getItem(position), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?>  parent) {

            }
        });

        //시/도를 설정하지 않으면 사용할수없게끔
        sigunguSpinner.setEnabled(false);

        //스피너 클릭이벤트
        typeSpinner.setAdapter(typesAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                Toast.makeText(PromoteWriteActivity.this, typesAdapter.getItem(position), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?>  parent) {

            }
        });

        //시/도를 설정하지 않으면 사용할수없게끔
        sigunguSpinner.setEnabled(false);

        //스피너 클릭이벤트
        siSpinner.setAdapter(siAdapter);
        siSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                String itemName = (String) siAdapter.getItem(position);
                if(!itemName.equals("선택안함")){

                    sigunguSpinner.setEnabled(true);

                }
                Toast.makeText(PromoteWriteActivity.this, itemName, Toast.LENGTH_SHORT).show();
                switch (itemName) {
                    case "서울":
                        sigungu = R.array.seoul;
                        break;
                    case "경기":
                        sigungu = R.array.gyeonggi;
                        break;
                    case "인천":
                        sigungu = R.array.incheon;
                        break;
                    case "세종":
                        sigungu = R.array.sejong;
                        break;
                    case "대구":
                        sigungu = R.array.daegu;
                        break;
                    case "대전":
                        sigungu = R.array.daejeon;
                        break;
                    case "부산":
                        sigungu = R.array.busan;
                        break;
                    case "광주":
                        sigungu = R.array.gwangju;
                        break;
                    case "울산":
                        sigungu = R.array.ulsan;
                        break;
                    case "강원":
                        sigungu = R.array.gangwon;
                        break;
                    case "충북":
                        sigungu = R.array.chungbuk;
                        break;
                    case "충남":
                        sigungu = R.array.chungnam;
                        break;
                    case "전북":
                        sigungu = R.array.jeonbuk;
                        break;
                    case "전남":
                        sigungu = R.array.jeonnam;
                        break;
                    case "경북":
                        sigungu = R.array.gyeongbuk;
                        break;
                    case "경남":
                        sigungu = R.array.gyeonnam;
                        break;
                    case "제주":
                        sigungu = R.array.jeju;
                        break;
                    default:
                        sigungu = R.array.nullarray;
                        break;

                }
                sigunguAdapter = ArrayAdapter.createFromResource(PromoteWriteActivity.this, sigungu, android.R.layout.simple_spinner_dropdown_item);
                sigunguSpinner.setAdapter(sigunguAdapter);

            }
            @Override
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        sigunguSpinner.setAdapter(sigunguAdapter);
        sigunguSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                String  itemName= (String) sigunguAdapter.getItem(position);
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
                finish();
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!siSpinner.getSelectedItem().toString().equals("선택안함")){
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    String note_title = editTextTitle.getText().toString();
                    String note_memo = editTextMemo.getText().toString();
                    String type = typeSpinner.getSelectedItem().toString();
                    String email = saveEmail;
                    String picture =  BitmapToString(resize(bitmap));
                    String local = siSpinner.getSelectedItem().toString();
                    String adoption = adoptionSpinner.getSelectedItem().toString(); //수정 추가
                    if(!sigunguSpinner.getSelectedItem().toString().equals("선택안함")){
                        local =local +" "+sigunguSpinner.getSelectedItem().toString();
                    }
                    Log.d(TAG,"local:"+local);
                    InsertData task = new InsertData();

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

                    task.execute("http://" + IP_ADDRESS + "/promoteInsert.php", email,note_title,note_memo,local,picture,type,adoption);  //수정
                }
                else{
                    Toast.makeText(PromoteWriteActivity.this, "시/도를 선택해주세요.", Toast.LENGTH_SHORT).show();
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
        String serverURL = "http://" + IP_ADDRESS + "/promoteFile.php";
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

                            Toast.makeText(PromoteWriteActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(PromoteWriteActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(PromoteWriteActivity.this, "Got Exception : see logcat ",Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Exception!", "Exception : " + e.getMessage(), e);

            }
            //dialog.dismiss();
            return serverResponseCode;
        }

    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(PromoteWriteActivity.this,"Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(PromoteWriteActivity.this, result,Toast.LENGTH_LONG).show();
            if(result.equals("새로운 글을 추가했습니다.")){
                finish();
                PromoteActivity postActivity= (PromoteActivity) PromoteActivity.promoteActivity;
                postActivity.finish();
                Intent intent = new Intent(getApplicationContext(), PromoteActivity.class);
                startActivity(intent);

            }
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            String note_title = (String)params[2];
            String note_memo= (String)params[3];
            String local = (String)params[4];
            String picture = (String)params[5];
            String type = (String)params[6];
            String adoption = (String)params[7]; //수정 추가
            String fileName = tempFile.getName();

            String serverURL = (String)params[0];
            String postParameters = "email="+ email +"&note_title=" + note_title + "&note_memo=" + note_memo + "&local=" + local+"&picture="+ picture+"&type="+type+"&adoption="+adoption+"&fileName="+fileName;  //수정


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
