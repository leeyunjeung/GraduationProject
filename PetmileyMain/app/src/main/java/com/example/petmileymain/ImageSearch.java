package com.example.petmileymain;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageSearch extends AppCompatActivity {

    private static String TAG = "petmily";

    private static String IP_ADDRESS = "3.34.44.142";
    private String saveEmail;
    private static final int REQUEST_CODE = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private String lostandfound,promote;
    private File tempFile;
    private ImageView imageView;

    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        imageView = findViewById(R.id.image);
        btnSearch = findViewById(R.id.btnSearch);

        lostandfound = "";


        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(ImageSearch.this, "", "Uploading file...", true);

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

    public int uploadFile() {
        String fileName = tempFile.getName();

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String serverURL = "http://" + IP_ADDRESS + "/imagesearch.php";
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

                            Toast.makeText(ImageSearch.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(ImageSearch.this, "MalformedURLException", Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(ImageSearch.this, "Got Exception : see logcat ",Toast.LENGTH_SHORT).show();

                    }

                });
                Log.e("Exception!", "Exception : " + e.getMessage(), e);

            }
            dialog.dismiss();

            return serverResponseCode;
        }

    }

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