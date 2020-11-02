package com.example.petmileymain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ReviewActivity extends AppCompatActivity {

    private static String IP_ADDRESS ="15.164.220.44";

    private static final String TAG = "review";
    private static final String board = "review";

    private FloatingActionButton btnWrite;
    private Button btnCategorize;
    private Toolbar toolbar;
    private TextView titleName;

    int categorizeItem = 0;
    String select_categorize="";

    private ArrayList<BoardData> mArrayList;
    private BoardAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageView imgview;

    List categorizeSelectedItems  = new ArrayList();

    public static Activity reviewActivity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        toolbar = findViewById(R.id.review_main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        reviewActivity=ReviewActivity.this;
        new BackgroundTask().execute(select_categorize);
        //버튼
        btnWrite = (FloatingActionButton)findViewById(R.id.btnWrite);
        btnCategorize = (Button)findViewById(R.id.btnCategorize);
        btnCategorize.setText("전체 글");

        titleName=(TextView)findViewById(R.id.titleName) ;
        titleName.setText("후기");


        imgview = findViewById(R.id.imgview);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();
        mAdapter = new BoardAdapter(this, board, mArrayList);
        mRecyclerView.setAdapter(mAdapter);


        btnCategorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorizeShow();

            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewWriteActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BoardData reviewData = mArrayList.get(position);
                Intent intent = new Intent(getBaseContext(), ReviewPostActivity.class);

                intent.putExtra("id", reviewData.getPost_id());
                intent.putExtra("note_title", reviewData.getPost_note_title());
                intent.putExtra("note_memo",reviewData.getPost_note_memo());
                intent.putExtra("nickname", reviewData.getUser_nickname());
                intent.putExtra("picture", reviewData.getImg());
                intent.putExtra("review_email",reviewData.getEmail());
                intent.putExtra("categorize",reviewData.getReview_categorize());
                intent.putExtra("userimg",reviewData.getUserimg());
                intent.putExtra("file_name",reviewData.getFile_name());
                startActivity(intent);


            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
    void categorizeShow()
    {
        final List<String> categorizeListItems = new ArrayList<>();
        categorizeListItems.add("전체 글");
        categorizeListItems.add("홍보");
        categorizeListItems.add("목격/실종");
        categorizeListItems.add("보호소");

        CharSequence[] typeitems =  categorizeListItems.toArray(new String[ categorizeListItems.size()]);
        categorizeSelectedItems.add(categorizeItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("구분");
        builder.setSingleChoiceItems(typeitems, categorizeItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        categorizeSelectedItems.clear();
                        categorizeSelectedItems.add(which);
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg="";
                        if (!categorizeSelectedItems.isEmpty()) {
                            int index = (int) categorizeSelectedItems.get(0);
                            msg = categorizeListItems.get(index);
                            categorizeItem=index;
                        }

                        if(msg.equals("전체 글")){
                            select_categorize="";
                        }
                        else{
                            select_categorize=msg;
                        }
                        mArrayList.clear();
                        new BackgroundTask().execute(select_categorize);
                        btnCategorize.setText(msg);
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }





    class BackgroundTask extends AsyncTask<String,Void,String> {
        String target;

        protected void onPreExecute(){
            target = "http://"+IP_ADDRESS+"/reviewView.php";

        }

        @Override
        protected String doInBackground(String... params) {

            String select_categorize= (String)params[0];
            String postParameters ="select_categorize="+select_categorize;


            try{
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
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                String nickname,note_title,id,note_memo,review_img,email,categorize,user,file_name;
                Bitmap picture;

                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    nickname = object.getString("nickname");
                    note_title = object.getString("note_title");
                    note_memo = object.getString("note_memo");
                    categorize = object.getString("categorize");
                    id = object.getString("id");
                    review_img=object.getString("picture");
                    email=object.getString("email");
                    user = object.getString("image");
                    file_name = object.getString("file_name");
                    picture = StringToBitMap(review_img);
                    BoardData reviewData = new BoardData();

                    reviewData.setPost_id(id);
                    reviewData.setUserNickname(nickname);
                    reviewData.setPost_note_title(note_title);
                    reviewData.setPicture(picture);
                    reviewData.setPost_note_memo(note_memo);
                    reviewData.setImg(review_img);
                    reviewData.setEmail(email);
                    reviewData.setReview_categorize(categorize);
                    reviewData.setUserimg(user);
                    reviewData.setFile_name(file_name);
                    mArrayList.add(reviewData);
                    mAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if(mArrayList.isEmpty()){
                mAdapter.clearItem();
                mAdapter.notifyDataSetChanged();
            }

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
            e.getMessage();
            return null;

        }

    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ReviewActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ReviewActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}
