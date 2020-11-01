package com.example.petmileymain;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
public class PromoteReviseActivity  extends AppCompatActivity {

    private static String IP_ADDRESS = "15.164.220.44";

    private static final String TAG = "test";
    private static final int REQUEST_CODE = 0;

    private  EditText editTextTitle;
    private  EditText editTextMemo;
    private  ImageView imgview;

    private File tempFile;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    private static final int PICK_FROM_ALBUM = 1;

    private String id="";
    private String note_memo = "";
    private String note_title = "";
    private String local = "";
    private String type = "";
    private String promote_picture = "";
    private String adoption=""; //수정 추가
    private String[] localArray;
    private int sigungu=R.array.nullarray;

    private String siText="해당없음";
    private String sigunguText="해당없음";

    private ArrayAdapter<CharSequence> typesAdapter;
    private ArrayAdapter<CharSequence> adoptionAdapter; //수정 추가
    private ArrayAdapter<CharSequence> sigunguAdapter;
    private ArrayAdapter<CharSequence> siAdapter;

    private Spinner sigunguSpinner;
    private Spinner typeSpinner;
    private Spinner siSpinner;
    private Spinner adoptionSpinner;
    private Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_write);


        toolbar = findViewById(R.id.promote_write_toolbar);
        setSupportActionBar(toolbar);


        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMemo = (EditText) findViewById(R.id.editTextMemo);



        adoptionSpinner = (Spinner) findViewById(R.id.adoption);
        adoptionAdapter = ArrayAdapter.createFromResource(this, R.array.adoption, android.R.layout.simple_spinner_dropdown_item);
        adoptionSpinner.setAdapter(adoptionAdapter);
        adoptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                Toast.makeText(PromoteReviseActivity.this, adoptionAdapter.getItem(position), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?>  parent) {

            }
        });


        //스피너 배열 설정해주기
        typeSpinner = (Spinner) findViewById(R.id.type);
        typesAdapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_dropdown_item);

        siSpinner = (Spinner) findViewById(R.id.si);
        siAdapter = ArrayAdapter.createFromResource(this, R.array.si, android.R.layout.simple_spinner_dropdown_item);

        sigunguSpinner = (Spinner) findViewById(R.id.sigungu);
        sigunguAdapter = ArrayAdapter.createFromResource(this, sigungu, android.R.layout.simple_spinner_dropdown_item);

        //스피너들 아이템 선택시
        typeSpinner.setAdapter(typesAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        siSpinner.setAdapter(siAdapter);
        siSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                String itemName = (String) siAdapter.getItem(position);
                Toast.makeText(PromoteReviseActivity.this, siAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                select(itemName);
                sigunguAdapter = ArrayAdapter.createFromResource(PromoteReviseActivity.this, sigungu, android.R.layout.simple_spinner_dropdown_item);
                sigunguSpinner.setAdapter(sigunguAdapter);
                sigunguSpinner.setSelection(sigunguAdapter.getPosition(sigunguText));
            }
            @Override
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        sigunguSpinner.setAdapter(sigunguAdapter);
        sigunguSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                //.makeText(PromoteReviseActivity.this, sigunguAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        imgview = (ImageView) findViewById(R.id.image);
        Button btnRevise = (Button) findViewById(R.id.btnInsert);
        btnRevise.setText("수정");
        Button btnBack = (Button) findViewById(R.id.btnBack);


        Bundle extras = getIntent().getExtras();

        note_title = extras.getString("note_title");
        note_memo = extras.getString("note_memo");
        promote_picture = extras.getString("promote_picture");
        local = extras.getString("local");
        id = extras.getString("id");
        type=extras.getString("type");
        adoption =extras.getString("adoption"); //수정 추가
        editTextTitle.setText(note_title);
        editTextMemo.setText(note_memo);

        //받은 지역 나눠서 스피너에 넣어주기
        localArray = local.split(" ");



        if(localArray.length==1){  //시도만 입력받았을 때
            siText=localArray[0];
            siSpinner.setSelection(siAdapter.getPosition(siText));
        }

        else if(localArray.length==2){ //시군구까지 입력받았을 때
            siText=localArray[0];
            sigunguText=localArray[1];
            select(siText);
            siSpinner.setSelection(siAdapter.getPosition(siText));
        }

        int index = typesAdapter.getPosition(type);
        typeSpinner.setSelection(index);
        Log.d(TAG,"index"+index);
        int adoptionindex = adoptionAdapter.getPosition(adoption); //수정 추가
        adoptionSpinner.setSelection(adoptionindex); //수정 추가
        Log.d(TAG,"adoption"+adoptionindex+"   "+adoption);

        imgview.setImageBitmap(StringToBitMap(promote_picture));

        imgview.setOnClickListener(new View.OnClickListener(){
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

        btnRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!siSpinner.getSelectedItem().toString().equals("선택안함")){
                    BitmapDrawable drawable = (BitmapDrawable) imgview.getDrawable();
                    Bitmap img = drawable.getBitmap();

                    String note_title = editTextTitle.getText().toString();
                    String note_memo = editTextMemo.getText().toString();
                    String local = siSpinner.getSelectedItem().toString();
                    String type = typeSpinner.getSelectedItem().toString();
                    String picture = BitmapToString(resize(img));
                    String adoption = adoptionSpinner.getSelectedItem().toString(); //수정 추가
                    if(!sigunguSpinner.getSelectedItem().toString().equals("선택안함")){
                        local = local +" "+sigunguSpinner.getSelectedItem().toString();
                    }
                    Log.d(TAG,"local:"+local);

                    PromoteRevise task = new PromoteRevise();
                    task.execute(note_title,note_memo,local,picture,type,adoption);

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
                else{
                    Toast.makeText(PromoteReviseActivity.this, "시/도를 선택해주세요.", Toast.LENGTH_SHORT).show();
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


    void select(String itemName){
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

                            //Toast.makeText(PromoteWriteActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();

                        }

                    });

                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                //dialog.dismiss();
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(PromoteReviseActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {
                //dialog.dismiss();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(PromoteReviseActivity.this, "Got Exception : see logcat ",Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Exception!", "Exception : " + e.getMessage(), e);

            }
            //dialog.dismiss();
            return serverResponseCode;
        }

    }

    class PromoteRevise extends AsyncTask<String,Void,String> {
        String target;

        protected void onPreExecute(){
            target = "http://"+IP_ADDRESS+"/promoteRevise.php?id="+ id;
        }

        @Override
        protected String doInBackground(String... params) {

            String note_title = (String)params[0];
            String note_memo = (String)params[1];
            String local = (String)params[2];
            String picture = (String)params[3];
            String type = (String)params[4];
            String adoption = (String)params[5];
            String fileName = tempFile.getName();
            String postParameters ="note_title=" + note_title + "&note_memo=" + note_memo+ "&local=" + local + "&picture=" + picture+"&type="+type+"&adoption="+adoption + "&fileName=" + fileName; //수정

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


        public  void onProgressUpdata(Void... values){
            super.onProgressUpdate();
        }

        public void onPostExecute(String result) {
            String note_title=null,id=null,note_memo=null,local=null ,promote_img=null,promote_email=null,nickname=null,type=null,adoption=null; //수정
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    note_title = object.getString("note_title");
                    note_memo = object.getString("note_memo");
                    local = object.getString("local");
                    id = object.getString("id");
                    type = object.getString("type");
                    promote_img=object.getString("picture");
                    promote_email = object.getString("email");
                    nickname = object.getString("nickname");
                    adoption = object.getString("adoption"); //수정 추가
                    Log.d(TAG,"후..."+promote_img);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if (result.contains("수정 완료")) {
                Toast.makeText(PromoteReviseActivity.this, "게시글이 수정되었습니다.", Toast.LENGTH_LONG).show();
                finish();
                PromotePostActivity postActivity=(PromotePostActivity)PromotePostActivity.promotePostActivity;
                postActivity.finish();
                Intent intent = new Intent(getApplicationContext(), PromotePostActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("local",local);
                intent.putExtra("type",type);
                intent.putExtra("note_title", note_title);
                intent.putExtra("note_memo",note_memo);
                intent.putExtra("promote_picture", promote_img);
                intent.putExtra("promote_email",promote_email);
                intent.putExtra("nickname",nickname);
                intent.putExtra("adoption",adoption); //수정 추가
                startActivity(intent);

            }




        }


    }
    public String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
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

        imgview.setImageBitmap(originalBm);

    }
}
