package com.example.petmileymain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UserWrotePost extends AppCompatActivity {

    private static String IP_ADDRESS = "15.164.220.44";

    TextView titleText;
    TextView nameText;
    ImageView image;

    TextView m_f;
    TextView missing_date;
    TextView place;
    ImageView imgview;

    private ListView listView;
    private UserPostPromoteAdapter pAdapter;
    private UserPostReviewAdapter rAdapter;
    private UserPostMissingFindAdapter mfAdapter;
    private ArrayList<UserPostPro> arrayList;
    String btn;
    Spinner spPostList;
    String useremail;
    Button search;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wrote_post);

        listView = (ListView) findViewById(R.id.userPostList);
        spPostList = (Spinner) findViewById(R.id.spList);
        search = (Button)findViewById(R.id.btnSearch);

        Intent intent = getIntent();
        useremail = intent.getStringExtra("email");

        spPostList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        btn ="notList";
                        break;
                    case 1:
                        btn = "review";

                        break;
                    case 2:
                        btn = "missing_find";

                        break;
                    case 3:
                        btn = "promote";

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn == "review"){new UserPostReview().execute();}
                else if(btn == "missing_find"){ new UserPostMissingFind().execute(); }
                else if(btn == "promote"){ new UserPostPromote().execute(); }

            }
        });



    }

    class UserPostPro {
        String id, note_title, note_memo, nickname, local, email,picture,adoption,type,userimg;
        Bitmap img;

        public String getUserimg() {
            return userimg;
        }

        public void setUserimg(String userimg) {
            this.userimg = userimg;
        }
        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getNote_memo() {
            return note_memo;
        }

        public void setNote_memo(String note_memo) {
            this.note_memo = note_memo;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLocal() {
            return local;
        }
        public String getType() {
            return type;
        }
        public void setLocal(String local) {
            this.local = local;
        }
        public void setType(String type) {
            this.type =type;
        }
        public String getEmail() {
            return email;
        }
        public String getAdoption() {
            return adoption;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setAdoption(String adoption) {
            this.adoption = adoption;
        }
        public UserPostPro(String id, String note_title, Bitmap img, String note_memo, String local, String nickname, String email,String picture,String type,String adoption,String userimg) {
            this.id = id;
            this.note_title = note_title;
            this.note_memo = note_memo;
            this.local = local;
            this.nickname = nickname;
            this.email = email;
            this.img = img;
            this.picture = picture;
            this.adoption=adoption;
            this.type=type;
            this.userimg=userimg;

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNote_title() {
            return note_title;
        }

        public void setNote_title(String note_title) {
            this.note_title = note_title;
        }

        public Bitmap getImg() {
            return img;
        }

        public void setImg(Bitmap picture) {
            this.img = img;
        }
    }

    class UserPostMf {
        String email, id, sex, tnr, color, m_f, age, kg, missing_data, place, feature, etc, type,picture;
        Bitmap img;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public UserPostMf(String email, String id, String sex, String tnr, String color, String m_f, String age, String kg, String missing_data, String place, String feature, String etc, String type, Bitmap img, String picture) {
            this.email = email;
            this.id = id;
            this.sex = sex;
            this.tnr = tnr;
            this.color = color;
            this.m_f = m_f;
            this.age = age;
            this.kg = kg;
            this.missing_data = missing_data;
            this.place = place;
            this.feature = feature;
            this.etc = etc;
            this.type = type;
            this.img = img;
            this.picture = picture;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTnr() {
            return tnr;
        }

        public void setTnr(String tnr) {
            this.tnr = tnr;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getM_f() {
            return m_f;
        }

        public void setM_f(String m_f) {
            this.m_f = m_f;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getKg() {
            return kg;
        }

        public void setKg(String kg) {
            this.kg = kg;
        }

        public String getMissing_data() {
            return missing_data;
        }

        public void setMissing_data(String missing_data) {
            this.missing_data = missing_data;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getEtc() {
            return etc;
        }

        public void setEtc(String etc) {
            this.etc = etc;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Bitmap getImg() {
            return img;
        }

        public void setImg(Bitmap img) {
            this.img = img;
        }
    }

    class UserPostRe {
        String id, note_title, note_memo, nickname, email,picture,categorize,userimg;
        Bitmap img;

        public UserPostRe(String id, String note_title, String note_memo, String nickname, String email, String picture, String categorize,Bitmap img,String userimg) {
            this.id = id;
            this.note_title = note_title;
            this.note_memo = note_memo;
            this.nickname = nickname;
            this.email = email;
            this.picture = picture;
            this.categorize = categorize;
            this.img = img;
            this.userimg=userimg;
        }

        public String getUserimg() {
            return userimg;
        }

        public void setUserimg(String userimg) {
            this.userimg = userimg;
        }
        public Bitmap getImg() {
            return img;
        }

        public void setImg(Bitmap img) {
            this.img = img;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNote_title() {
            return note_title;
        }

        public void setNote_title(String note_title) {
            this.note_title = note_title;
        }

        public String getNote_memo() {
            return note_memo;
        }

        public void setNote_memo(String note_memo) {
            this.note_memo = note_memo;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getCategorize() {
            return categorize;
        }

        public void setCategorize(String categorize) {
            this.categorize = categorize;
        }
    }

    class UserPostPromoteAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<UserPostPro> arrayList;

        public UserPostPromoteAdapter(Context context, ArrayList<UserPostPro> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(context, R.layout.post_list, null);
            titleText = (TextView) v.findViewById(R.id.txtPostList);
            nameText = (TextView)v.findViewById(R.id.nameText);
            image = (ImageView) v.findViewById(R.id.imgPost);

            titleText.setText((arrayList.get(position).getNote_title()));
            nameText.setText((arrayList.get(position).getNickname()));
            image.setImageBitmap((arrayList.get(position).getImg()));
            v.setTag(arrayList.get(position));
            return v;
        }
    }


    class UserPostReviewAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<UserPostRe> arrayList;

        public UserPostReviewAdapter(Context context, ArrayList<UserPostRe> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(context, R.layout.post_list, null);
            titleText = (TextView) v.findViewById(R.id.txtPostList);
            nameText = (TextView)v.findViewById(R.id.nameText);
            image = (ImageView) v.findViewById(R.id.imgPost);

            titleText.setText((arrayList.get(position).getNote_title()));
            nameText.setText((arrayList.get(position).getNickname()));
            image.setImageBitmap((arrayList.get(position).getImg()));
            v.setTag(arrayList.get(position));
            return v;
        }
    }


    class UserPostMissingFindAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<UserPostMf> arrayList;

        public UserPostMissingFindAdapter(Context context, ArrayList<UserPostMf> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(context, R.layout.lostlist, null);
            m_f = (TextView) v.findViewById(R.id.textView_list_m_f);
            missing_date = (TextView) v.findViewById(R.id.textView_list_missingdate);
            place = (TextView) v.findViewById(R.id.textView_list_place);
            imgview = (ImageView) v.findViewById(R.id.imgview);

            m_f.setText((arrayList.get(position).getM_f()));
            missing_date.setText((arrayList.get(position).getMissing_data()));
            place.setText((arrayList.get(position).getPlace()));
            imgview.setImageBitmap((arrayList.get(position).getImg()));
            v.setTag(arrayList.get(position));
            return v;
        }
    }


    class UserPostPromote extends AsyncTask<String, Void, String> {
        ArrayList<UserPostPro> arrayList = new ArrayList<>();
        ArrayList<UserPostMf> arrayList2 = new ArrayList<>();
        String target;

        @Override
        protected void onPreExecute() {
            try {
                Log.e("확인", btn);
                target = "http://" + IP_ADDRESS + "/UserPostListSearch.php?email=" + URLEncoder.encode(useremail, "UTF-8") + "&listName=" + btn;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;

                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");


                String note_title, note_memo, nickname, local, id, email,picture,adoption,type,userimg;
                Bitmap img;


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    nickname = object.getString("nickname");
                    note_title = object.getString("note_title");
                    note_memo = object.getString("note_memo");
                    local = object.getString("local");
                    id = object.getString("id");
                    picture = object.getString("picture");
                    email = object.getString("email");
                    img = StringToBitMap(picture);
                    adoption = object.getString("adoption");
                    type=object.getString("type");
                    userimg = object.getString("image");
                    Log.e("확인", note_title);
                    UserPostPro userPost = new UserPostPro(id, note_title, img, note_memo, local, nickname, email,picture,type,adoption,userimg);
                    arrayList.add(userPost);
                }
                pAdapter = new UserPostPromoteAdapter(getApplicationContext(), arrayList);
                listView.setAdapter(pAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (btn == "promote") {
                            Intent intent = new Intent(getApplicationContext(), PromotePostActivity.class);
                            intent.putExtra("id", arrayList.get(position).getId());
                            intent.putExtra("note_title", arrayList.get(position).getNote_title());
                            intent.putExtra("note_memo", arrayList.get(position).getNote_memo());
                            intent.putExtra("nickname", arrayList.get(position).getNickname());
                            intent.putExtra("local", arrayList.get(position).getLocal());
                            intent.putExtra("promote_picture", arrayList.get(position).getPicture());
                            intent.putExtra("promote_email",arrayList.get(position).getEmail());
                            intent.putExtra("adoption",arrayList.get(position).getAdoption());
                            intent.putExtra("type",arrayList.get(position).getType());
                            intent.putExtra("userimg",arrayList.get(position).getUserimg());
                            startActivity(intent);

                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Bitmap StringToBitMap(String encodedString) {
            try {
                encodedString = encodedString.replace(" ", "+");
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                return bitmap;

            } catch (Exception e) {
                Log.e("exception", e.getMessage());
                //e.getMessage();
                return null;

            }

        }
    }

    class UserPostReview extends AsyncTask<String, Void, String> {
        ArrayList<UserPostRe> arrayList = new ArrayList<>();
        String target;

        @Override
        protected void onPreExecute() {
            try {
                Log.e("확인", btn);
                target = "http://" + IP_ADDRESS + "/UserPostListSearch.php?email=" + URLEncoder.encode(useremail, "UTF-8") + "&listName=" + btn;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;

                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String resulte) {
            try {
                JSONObject jsonObject = new JSONObject(resulte);
                JSONArray jsonArray = jsonObject.getJSONArray("response");


                String id, note_title, note_memo, nickname, email,picture,categorize,userimg;
                Bitmap img;


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    note_title = object.getString("note_title");
                    note_memo = object.getString("note_memo");
                    nickname = object.getString("nickname");
                    email = object.getString("email");
                    picture = object.getString("picture");
                    categorize = object.getString("categorize");
                    userimg = object.getString("image");
                    img = StringToBitMap(picture);


                    id = object.getString("id");

                    img = StringToBitMap(object.getString("picture"));

                    Log.e("확인", note_title);
                    UserPostRe userPost = new UserPostRe(id, note_title, note_memo, nickname, email,picture,categorize,img,userimg);
                    ;
                    arrayList.add(userPost);
                }
                rAdapter = new UserPostReviewAdapter(getApplicationContext(), arrayList);
                listView.setAdapter(rAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (btn == "review") {
                            Intent intent = new Intent(getApplicationContext(), ReviewPostActivity.class);
                            intent.putExtra("id", arrayList.get(position).getId());
                            intent.putExtra("note_title", arrayList.get(position).getNote_title());
                            intent.putExtra("note_memo", arrayList.get(position).getNote_memo());
                            intent.putExtra("nickname", arrayList.get(position).getNickname());
                            intent.putExtra("picture", arrayList.get(position).getPicture());
                            intent.putExtra("review_email",arrayList.get(position).getEmail());
                            intent.putExtra("categorize",arrayList.get(position).getCategorize());
                            intent.putExtra("userimg",arrayList.get(position).getUserimg());
                            startActivity(intent);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Bitmap StringToBitMap(String encodedString) {
            try {
                encodedString = encodedString.replace(" ", "+");
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                return bitmap;

            } catch (Exception e) {
                Log.e("exception", e.getMessage());
                //e.getMessage();
                return null;

            }

        }

    }

    class UserPostMissingFind extends AsyncTask<String, Void, String> {
        ArrayList<UserPostMf> arrayList = new ArrayList<>();
        String target;

        @Override
        protected void onPreExecute() {
            try {
                Log.e("확인", btn);
                target = "http://" + IP_ADDRESS + "/UserPostListSearch.php?email=" + URLEncoder.encode(useremail, "UTF-8") + "&listName=" + btn;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;

                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String resulte) {
            try {
                JSONObject jsonObject = new JSONObject(resulte);
                JSONArray jsonArray = jsonObject.getJSONArray("response");


                String email, id, sex, tnr, color, m_f, age, kg, missing_data, place, feature, etc, type,picture;
                Bitmap img;


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    m_f = object.getString("m_f");
                    missing_data = object.getString("missing_date");
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
                    id = object.getString("id");
                    picture = object.getString("picture");
                    img = StringToBitMap(picture);


                    UserPostMf userPost = new UserPostMf(email, id, sex, tnr, color, m_f, age, kg, missing_data, place, feature, etc, type,img,picture);
                    arrayList.add(userPost);
                }
                mfAdapter = new UserPostMissingFindAdapter(getApplicationContext(), arrayList);
                listView.setAdapter(mfAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (btn == "missing_find") {
                            Intent intent = new Intent(getApplicationContext(), LostAndFoundPost.class);
                            intent.putExtra("lostandfound_id", arrayList.get(position).getId());
                            intent.putExtra("m_f", arrayList.get(position).getM_f());
                            intent.putExtra("missing_date", arrayList.get(position).getMissing_data());
                            intent.putExtra("place", arrayList.get(position).getPlace());
                            intent.putExtra("sex", arrayList.get(position).getSex());
                            intent.putExtra("type", arrayList.get(position).getType());
                            intent.putExtra("tnr", arrayList.get(position).getTnr());
                            intent.putExtra("kg", arrayList.get(position).getKg());
                            intent.putExtra("age", arrayList.get(position).getAge());
                            intent.putExtra("color", arrayList.get(position).getColor());
                            intent.putExtra("feature", arrayList.get(position).getFeature());
                            intent.putExtra("etc", arrayList.get(position).getEtc());
                            intent.putExtra("email", arrayList.get(position).getEmail());
                            intent.putExtra("lostandfound_img", arrayList.get(position).getPicture());
                            startActivity(intent);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Bitmap StringToBitMap(String encodedString) {
            try {
                encodedString = encodedString.replace(" ", "+");
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                return bitmap;

            } catch (Exception e) {
                Log.e("exception", e.getMessage());
                //e.getMessage();
                return null;

            }

        }
    }
}