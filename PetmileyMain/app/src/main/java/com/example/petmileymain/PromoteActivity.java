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
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class PromoteActivity extends AppCompatActivity {
    private static final  String board = "promote";

    private static String IP_ADDRESS ="15.164.220.44";

    private static final String TAG = "test";

    private FloatingActionButton btnWrite;
    private Button btnBack;
    private Button btnLocal;
    private Button btnType;

    private Button btnAdoption;
    private Button btnInit;
    private Button btnImageSearch;

    String select_local="";
    String select_type="";
    String select_adoption="";
    int typeItem = 0;
    int localItem = 0;
    int adoptionItem = 0;
    private ArrayList<BoardData> mArrayList;
    private BoardAdapter mAdapter;
    private RecyclerView mRecyclerView;

    List typeSelectedItems  = new ArrayList();
    List localSelectedItems  = new ArrayList();
    List adoptionSelectedItems  = new ArrayList();

    String[] si;
    public static Activity promoteActivity;

    Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);

        toolbar = findViewById(R.id.promote_main_toolbar);
        setSupportActionBar(toolbar);

        promoteActivity=PromoteActivity.this;
        new BackgroundTask().execute(select_local,select_type,select_adoption,null);
        btnWrite = (FloatingActionButton)findViewById(R.id.btnWrite);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnLocal = (Button)findViewById(R.id.btnLocal);
        btnType = (Button)findViewById(R.id.btnType);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();
        mAdapter = new BoardAdapter(this, board, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localShow();

            }
        });
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeShow();

            }
        });

        btnAdoption = (Button)findViewById(R.id.btnAdoption);
        btnInit=(Button)findViewById(R.id.btnInit);

        btnAdoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adoptionShow();
            }
        });
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_adoption="";
                select_local="";
                select_type="";
                mArrayList.clear();

                btnLocal.setText("모든 지역");
                btnType.setText("모든 품종");
                btnAdoption.setText("모든 입양");
                typeItem = 0;
                localItem = 0;
                adoptionItem = 0;


                new BackgroundTask().execute(select_local,select_type,select_adoption,null);
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PromoteWriteActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BoardData promoteData = mArrayList.get(position);
                Intent intent = new Intent(getBaseContext(), PromotePostActivity.class);
                intent.putExtra("id", promoteData.getPost_id());
                intent.putExtra("note_title", promoteData.getPost_note_title());
                intent.putExtra("note_memo", promoteData.getPost_note_memo());
                intent.putExtra("nickname", promoteData.getUser_nickname());
                intent.putExtra("local", promoteData.getPromote_local());
                intent.putExtra("type", promoteData.getPromote_type());
                intent.putExtra("promote_picture", promoteData.getImg());
                intent.putExtra("promote_email",promoteData.getEmail());
                intent.putExtra("userimg",promoteData.getUserimg());
                intent.putExtra("adoption",promoteData.getAdoption());
                intent.putExtra("file_name",promoteData.getFile_name());//수정 추가
                Log.d("adoption",promoteData.getAdoption());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnImageSearch:
                Intent intent = new Intent(getApplicationContext(), ImageSearch.class);
                intent.putExtra("flag","promote");
                startActivityForResult(intent, 1);
                //startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public  boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.image_btn, menu);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        return true;
    }

    void typeShow()
    {
        final List<String> typeListItems = new ArrayList<>();
        typeListItems.add("모든 동물");
        typeListItems.add("개");
        typeListItems.add("고양이");
        typeListItems.add("기타 동물");

        CharSequence[] typeitems =  typeListItems.toArray(new String[ typeListItems.size()]);
        typeSelectedItems.add(typeItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("품종 선택");
        builder.setSingleChoiceItems(typeitems, typeItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeSelectedItems.clear();
                        typeSelectedItems.add(which);
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg="";
                        if (!typeSelectedItems.isEmpty()) {
                            int index = (int) typeSelectedItems.get(0);
                            msg = typeListItems.get(index);
                            typeItem=index;
                        }
                        if(msg.equals("모든 동물")){
                            select_type="";
                        }
                        else{
                            select_type=msg;
                        }
                        mArrayList.clear();
                        new BackgroundTask().execute(select_local,select_type,select_adoption,null);
                        btnType.setText(msg);
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }


    void localShow()
    {
        localSelectedItems.add(localItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("지역 선택");

        si = getResources().getStringArray(R.array.si);
        si[0]="모든 지역";
        builder.setSingleChoiceItems(si, localItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        localSelectedItems.clear();
                        localSelectedItems.add(which);

                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg="";
                        if (!localSelectedItems.isEmpty()) {
                            int index = (int) localSelectedItems.get(0);
                            msg = si[index];
                            Log.d(TAG,"msg"+msg);
                            localItem=index;
                        }
                        if(msg.equals("모든 지역")){
                            select_local="";
                        }
                        else{
                            select_local=msg;
                        }
                        mArrayList.clear();
                        new BackgroundTask().execute(select_local,select_type,select_adoption,null);
                        btnLocal.setText(msg);
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }

    void adoptionShow()
    {
        final List<String> adoptionListItems = new ArrayList<>();
        adoptionListItems.add("모든 입양");
        adoptionListItems.add("완료");
        adoptionListItems.add("미완료");

        CharSequence[] adoptionitems =   adoptionListItems.toArray(new String[ adoptionListItems.size()]);
        adoptionSelectedItems.add(adoptionItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("입양 여부 선택");
        builder.setSingleChoiceItems(adoptionitems, adoptionItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adoptionSelectedItems.clear();
                        adoptionSelectedItems.add(which);
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg="";
                        if (!adoptionSelectedItems.isEmpty()) {
                            int index = (int) adoptionSelectedItems.get(0);
                            msg =  adoptionListItems.get(index);
                            adoptionItem=index;
                        }
                        if(msg.equals("모든 입양")){
                            select_adoption="";
                        }
                        else{
                            select_adoption=msg;
                        }
                        mArrayList.clear();
                        new BackgroundTask().execute(select_local,select_type,select_adoption,null);
                        btnAdoption.setText(msg);
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }
    class BackgroundTask extends AsyncTask<String,Void,String> {
        String target;

        protected void onPreExecute(){
            target = "http://"+IP_ADDRESS+"/promoteView.php";

        }

        @Override
        protected String doInBackground(String... params) {

            String select_local= (String)params[0];
            String select_type= (String)params[1];
            String select_adoption = (String)params[2];
            String search_file = (String)params[3];
            String postParameters ="select_local="+select_local +"&select_type="+ select_type+"&select_adoption="+select_adoption+"&search_file="+search_file;


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

                String nickname,note_title,id,note_memo,local,promote_img,email,type,userimg, adoption, file_name; //수정
                Bitmap picture;

                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    nickname = object.getString("nickname");
                    note_title = object.getString("note_title");
                    note_memo = object.getString("note_memo");
                    local = object.getString("local");
                    type = object.getString("type");
                    userimg = object.getString("image");
                    adoption = object.getString("adoption"); //수정추가
                    id = object.getString("id");
                    promote_img=object.getString("picture");
                    email=object.getString("email");
                    file_name = object.getString("file_name");
                    picture = StringToBitMap(promote_img);
                    BoardData promoteData = new BoardData();

                    promoteData.setPost_id(id);
                    promoteData.setUserNickname(nickname);
                    promoteData.setPost_note_title(note_title);
                    promoteData.setPicture(picture);
                    promoteData.setPromote_local(local);
                    promoteData.setPromote_type(type);
                    promoteData.setAdoption(adoption); //수정 추가
                    promoteData.setPost_note_memo(note_memo);
                    promoteData.setImg(promote_img);
                    promoteData.setEmail(email);
                    promoteData.setUserimg(userimg);
                    promoteData.setFile_name(file_name);
                    mArrayList.add(promoteData);
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
            //e.getMessage();
            return null;

        }

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private PromoteActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final PromoteActivity.ClickListener clickListener) {
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
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
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

    //추가된 내용 이미지검색
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(PromoteActivity.this, data.getStringExtra("fileResult"), Toast.LENGTH_SHORT).show();
                mArrayList.clear();
                new BackgroundTask().execute(select_local, select_type, select_adoption, data.getStringExtra("fileResult"));

            } else {   // RESULT_CANCEL
                Toast.makeText(PromoteActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
