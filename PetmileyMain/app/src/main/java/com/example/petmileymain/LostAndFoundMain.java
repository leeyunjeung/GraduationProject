package com.example.petmileymain;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LostAndFoundMain extends AppCompatActivity implements LostAndFoundDialog.OnCompleteListener {

    private Button btnWrite;
    private Button btnSearch;
    private static String IP_ADDRESS = "3.34.44.142";
    private static final String TAG = "petmily";
    public String select_local, select_type,select_mf,start_date,end_date;
    private Button btnBack;
    private ArrayList<LostAndFoundData> mArrayList;
    private LostAndFoundAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageView imgview;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_main);

        select_local = "";
        select_type = "";
        select_mf = "";

        start_date = "1990-01-01";
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        end_date = mFormat.format(date);
        Log.d("시작:",start_date);
        Log.d("끝:",end_date);

        new BackgroundTask().execute(select_local,select_type,select_mf,start_date,end_date);
        btnBack = findViewById(R.id.btnLost_foundBack);
        btnWrite = (Button)findViewById(R.id.btnWrite);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        imgview = findViewById(R.id.imgview);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();
        mAdapter = new LostAndFoundAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainList.class);
                startActivity(intent);
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LostAndFound.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LostAndFoundData lostandfoundData = mArrayList.get(position);

                Intent intent = new Intent(getBaseContext(), LostAndFoundPost.class);


                intent.putExtra("lostandfound_id", lostandfoundData.getLostandfound_id());
                intent.putExtra("m_f", lostandfoundData.getM_f());
                intent.putExtra("missing_date", lostandfoundData.getMissing_date());
                intent.putExtra("place", lostandfoundData.getPlace());
                intent.putExtra("sex", lostandfoundData.getSex());
                intent.putExtra("type", lostandfoundData.getType());
                intent.putExtra("tnr", lostandfoundData.getTnr());
                intent.putExtra("kg", lostandfoundData.getKg());
                intent.putExtra("age", lostandfoundData.getAge());
                intent.putExtra("color", lostandfoundData.getColor());
                intent.putExtra("feature", lostandfoundData.getFeature());
                intent.putExtra("etc", lostandfoundData.getEtc());
                intent.putExtra("email", lostandfoundData.getEmail());
                intent.putExtra("lostandfound_img", lostandfoundData.getLostandfound_img());
                intent.putExtra("file_name",lostandfoundData.getFile_name());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));



    }



    void show()
    {
        DialogFragment newFragment = new LostAndFoundDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void onInputedData(String local, String type, String mf,String start,String end) {
        select_local = local;
        select_type = type;
        select_mf = mf;
        start_date = start;
        end_date = end;


        if (local.equals("모든지역")){
            select_local = "";
        }
        if (type.equals("모든 동물")){
            select_type = "";
        }
        if (mf.equals("전체")){
            select_mf = "";
        }
        mArrayList.clear();
        new BackgroundTask().execute(select_local,select_type,select_mf,start,end);
    }


    class BackgroundTask extends AsyncTask<String,Void,String> {
        String target;

        protected void onPreExecute(){
            target = "http://"+IP_ADDRESS+"/LostAndFoundBoard.php";
        }



        @Override
        protected String doInBackground(String... params) {

            String select_local= (String)params[0];
            String select_type= (String)params[1];
            String select_mf= (String)params[2];
            String start= (String)params[3];
            String end= (String)params[4];

            String postParameters ="select_local="+select_local +"&select_type="+ select_type +"&select_mf="+ select_mf+"&start_date="+start+"&end_date="+end;

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

            if(result.contains("false")){
                Toast.makeText(LostAndFoundMain.this, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }


            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                String m_f,missing_date,place,sex,lostandfound_id,lostandfound_img,type,tnr,kg,age,color,feature,etc,email,file_name;
                Bitmap picture;

                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    m_f = object.getString("m_f");
                    missing_date = object.getString("missing_date");
                    place = object.getString("place");
                    sex = object.getString("sex");
                    type = object.getString("type");
                    tnr = object.getString("tnr");
                    kg = object.getString("kg");
                    age = object.getString("age");
                    color = object.getString("color");
                    feature = object.getString("feature");
                    etc = object.getString("etc");
                    email = object.getString("email");
                    lostandfound_id = object.getString("id");
                    lostandfound_img=object.getString("picture");
                    file_name = object.getString("file_name");
                    picture = StringToBitMap(lostandfound_img);
                    LostAndFoundData lostandfoundData = new LostAndFoundData();

                    lostandfoundData.setLostandfound_id(lostandfound_id);
                    lostandfoundData.setM_f(m_f);
                    lostandfoundData.setMissing_date(missing_date);
                    lostandfoundData.setPicture(picture);
                    lostandfoundData.setPlace(place);
                    lostandfoundData.setSex(sex);
                    lostandfoundData.setAge(age);
                    lostandfoundData.setColor(color);
                    lostandfoundData.setType(type);
                    lostandfoundData.setTnr(tnr);
                    lostandfoundData.setKg(kg);
                    lostandfoundData.setFeature(feature);
                    lostandfoundData.setEtc(etc);
                    lostandfoundData.setEmail(email);
                    lostandfoundData.setLostandfound_img(lostandfound_img);
                    lostandfoundData.setFile_name(file_name);

                    mArrayList.add(lostandfoundData);
                    mAdapter.notifyDataSetChanged();


                }
            }catch (Exception e){

                e.printStackTrace();
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
        private LostAndFoundMain.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final LostAndFoundMain.ClickListener clickListener) {
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

        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
