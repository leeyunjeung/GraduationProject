package com.example.petmileymain;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReviewReviseActivity  extends AppCompatActivity {

    private static String IP_ADDRESS = "15.164.220.44";

    private static final String TAG = "test";
    private static final int REQUEST_CODE = 0;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;

    private  EditText editTextTitle;
    private  EditText editTextMemo;
    private  ImageView imgview;

    private String id="";
    private String note_memo = "";
    private String note_title = "";
    private String categorize = "";
    private String review_picture = "";
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);


        toolbar = findViewById(R.id.review_write_toolbar);
        setSupportActionBar(toolbar);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextMemo = (EditText) findViewById(R.id.editTextMemo);



        final Spinner spinner = (Spinner) findViewById(R.id.categorize);
        final ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(this, R.array.categorize, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(sAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                Toast.makeText(ReviewReviseActivity.this, sAdapter.getItem(position), Toast.LENGTH_SHORT).show();
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
        review_picture = extras.getString("picture");
        id = extras.getString("id");
        categorize=extras.getString("categorize");

        editTextTitle.setText(note_title);
        editTextMemo.setText(note_memo);
        int index = sAdapter.getPosition(categorize);
        spinner.setSelection(index);
        imgview.setImageBitmap(StringToBitMap(review_picture));

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
                BitmapDrawable drawable = (BitmapDrawable) imgview.getDrawable();
                Bitmap img = drawable.getBitmap();

                String note_title = editTextTitle.getText().toString();
                String note_memo = editTextMemo.getText().toString();
                String categorize = spinner.getSelectedItem().toString();
                String picture = BitmapToString(resize(img));

                ReviewRevise task = new ReviewRevise();
                task.execute(note_title,note_memo,picture,categorize);

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

    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imgview.setImageBitmap(originalBm);

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

                            Toast.makeText(ReviewReviseActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(ReviewReviseActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(ReviewReviseActivity.this, "Got Exception : see logcat ",Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Exception!", "Exception : " + e.getMessage(), e);

            }
            //dialog.dismiss();
            return serverResponseCode;
        }

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



    class ReviewRevise extends AsyncTask<String,Void,String> {
        String target;

        protected void onPreExecute(){
            target = "http://"+IP_ADDRESS+"/reviewRevise.php?id="+ id;
        }

        @Override
        protected String doInBackground(String... params) {

            String note_title = (String)params[0];
            String note_memo = (String)params[1];
            String picture = (String)params[2];
            String categorize = (String)params[3];
            String fileName = tempFile.getName();

            String postParameters ="note_title=" + note_title + "&note_memo=" + note_memo+ "&picture=" + picture + "&categorize="+categorize + "&fileName=" + fileName;


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
            String note_title=null, id=null, note_memo=null, categorize=null ,review_img=null, review_email=null,nickname=null;
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    note_title = object.getString("note_title");
                    note_memo = object.getString("note_memo");
                    categorize = object.getString("categorize");
                    id = object.getString("id");
                    nickname = object.getString("nickname");
                    review_email=object.getString("email");
                    review_img=object.getString("picture");

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if (result.contains("수정 완료")) {
                Toast.makeText(ReviewReviseActivity.this, "게시글이 수정되었습니다.", Toast.LENGTH_LONG).show();
                finish();
                ReviewPostActivity activity=(ReviewPostActivity)ReviewPostActivity.reviewPostActivity;
                activity.finish();
                Intent intent = new Intent(getApplicationContext(), ReviewPostActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("categorize",categorize);
                intent.putExtra("note_title", note_title);
                intent.putExtra("note_memo",note_memo);
                intent.putExtra("picture", review_img);
                intent.putExtra("review_email",review_email);
                intent.putExtra("nickname",nickname);
                startActivity(intent);

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
}
