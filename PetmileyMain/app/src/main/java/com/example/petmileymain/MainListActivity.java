package com.example.petmileymain;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class MainListActivity extends AppCompatActivity {

    private static String TAG = "petmiley";
    private static String IP_ADDRESS ="15.164.220.44";
    public static String email;

    private Button btnSelter;
    private Button btnMissingFind;
    private Button btnPromote;
    private Button btnReview;
    private Button btnUserInformation;
    AutoScrollViewPager autoScrollViewPager;

    AutoScrollAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = getIntent().getStringExtra("email");
        btnSelter = (Button)findViewById(R.id.btnShelter);
        btnMissingFind = (Button)findViewById(R.id.btnMissingFind);
        btnPromote = (Button)findViewById(R.id.btnPromote);
        btnReview =(Button)findViewById(R.id.btnReview);
        btnUserInformation =(Button)findViewById(R.id.btnUserInformation);
        autoScrollViewPager = findViewById(R.id.mainAutoScroll);



        this.ButtonListener();
        new xmlParser().execute();
    }


    public void ButtonListener(){

        btnSelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShelterActivity.class);
                startActivity(intent);
            }
        });

        btnMissingFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LostAndFoundMain.class);
                startActivity(intent);

            }
        });

        btnPromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PromoteActivity.class);
                startActivity(intent);

            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                startActivity(intent);

            }
        });

        btnUserInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserInformation.class);
                startActivity(intent);
            }

        });
    }

    public class AutoScrollAdapter extends PagerAdapter {
        Context context;
        ArrayList<Shelter> data;

        public AutoScrollAdapter(Context context, ArrayList<Shelter> arrayList) {
            this.context = context;
            this.data = arrayList;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.mainauto,null);
            ImageView animalImg = v.findViewById(R.id.animalImg);
            TextView animalName = v.findViewById(R.id.autoAnimalName);


            animalName.setText(data.get(position).getSpecialMark());
            Picasso.get().load((data.get(position).getPopfile())).into(animalImg);
            container.addView(v);

            return v;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }



    static class Shelter {

        String desertionNo;
        String filename;
        String happenDt;
        String happenPlace;
        String kindCd;
        String colorCd;
        String age;
        String weight;
        String noticeNo;
        String noticeSdt;
        String noticeEdt;
        String popfile;
        String processState;
        String sexCd;
        String neuterYn;
        String specialMark;
        String careNm;
        String careTel;
        String careAddr;
        String orgNm;
        String officetel;
        Bitmap img;

        public Shelter(String desertionNo, String filename, String happenDt, String happenPlace, String kindCd, String colorCd, String age, String weight, String noticeNo, String noticeSdt, String noticeEdt, String popfile, String processState, String sexCd, String neuterYn, String specialMark, String careNm, String careTel, String careAddr, String orgNm, String officetel) {
            this.desertionNo = desertionNo;
            this.filename = filename;
            this.happenDt = happenDt;
            this.happenPlace = happenPlace;
            this.kindCd = kindCd;
            this.colorCd = colorCd;
            this.age = age;
            this.weight = weight;
            this.noticeNo = noticeNo;
            this.noticeSdt = noticeSdt;
            this.noticeEdt = noticeEdt;
            this.popfile = popfile;
            this.processState = processState;
            this.sexCd = sexCd;
            this.neuterYn = neuterYn;
            this.specialMark = specialMark;
            this.careNm = careNm;
            this.careTel = careTel;
            this.careAddr = careAddr;
            this.orgNm = orgNm;
            this.officetel = officetel;
            //this.img = img;
        }

        public Shelter(Bitmap img) {
            this.img = img;
        }

        public String getDesertionNo() {
            return desertionNo;
        }

        public void setDesertionNo(String desertionNo) {
            this.desertionNo = desertionNo;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getHappenDt() {
            return happenDt;
        }

        public void setHappenDt(String happenDt) {
            this.happenDt = happenDt;
        }

        public String getHappenPlace() {
            return happenPlace;
        }

        public void setHappenPlace(String happenPlace) {
            this.happenPlace = happenPlace;
        }

        public String getKindCd() {
            return kindCd;
        }

        public void setKindCd(String kindCd) {
            this.kindCd = kindCd;
        }

        public String getColorCd() {
            return colorCd;
        }

        public void setColorCd(String colorCd) {
            this.colorCd = colorCd;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getNoticeNo() {
            return noticeNo;
        }

        public void setNoticeNo(String noticeNo) {
            this.noticeNo = noticeNo;
        }

        public String getNoticeSdt() {
            return noticeSdt;
        }

        public void setNoticeSdt(String noticeSdt) {
            this.noticeSdt = noticeSdt;
        }

        public String getNoticeEdt() {
            return noticeEdt;
        }

        public void setNoticeEdt(String noticeEdt) {
            this.noticeEdt = noticeEdt;
        }

        public String getPopfile() {
            return popfile;
        }

        public void setPopfile(String popfile) {
            this.popfile = popfile;
        }

        public String getProcessState() {
            return processState;
        }

        public void setProcessState(String processState) {
            this.processState = processState;
        }

        public String getSexCd() {
            return sexCd;
        }

        public void setSexCd(String sexCd) {
            this.sexCd = sexCd;
        }

        public String getNeuterYn() {
            return neuterYn;
        }

        public void setNeuterYn(String neuterYn) {
            this.neuterYn = neuterYn;
        }

        public String getSpecialMark() {
            return specialMark;
        }

        public void setSpecialMark(String specialMark) {
            this.specialMark = specialMark;
        }

        public String getCareNm() {
            return careNm;
        }

        public void setCareNm(String careNm) {
            this.careNm = careNm;
        }

        public String getCareTel() {
            return careTel;
        }

        public void setCareTel(String careTel) {
            this.careTel = careTel;
        }

        public String getCareAddr() {
            return careAddr;
        }

        public void setCareAddr(String careAddr) {
            this.careAddr = careAddr;
        }

        public String getOrgNm() {
            return orgNm;
        }

        public void setOrgNm(String orgNm) {
            this.orgNm = orgNm;
        }

        public String getOfficetel() {
            return officetel;
        }

        public void setOfficetel(String officetel) {
            this.officetel = officetel;
        }

        public Bitmap getImg() {
            return img;
        }

        public void setImg(Bitmap img) {
            this.img = img;
        }
    }

    private class xmlParser extends AsyncTask<String, Void, String> {
        ArrayList<Shelter> arrayList = new ArrayList<Shelter>();
        String target;

        protected void onPreExecute() {
            target = "http://"+IP_ADDRESS+"/ShelterActivite.php";
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

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

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                String desertionNo,filename,happenDt,happenPlace,kindCd, colorCd,age,weight,noticeNo,noticeSdt,noticeEdt,popfile,
                        processState,sexCd,neuterYn,specialMark,careNm,careTel,careAddr, orgNm,officetel;
                Bitmap img;

                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    desertionNo = object.getString("desertionNo");
                    filename = object.getString("filename");
                    happenDt = object.getString("happenDt");
                    happenPlace = object.getString("happenPlace");
                    kindCd = object.getString("kindCd");
                    colorCd=object.getString("colorCd");
                    age=object.getString("age");
                    weight = object.getString("weight");
                    noticeNo = object.getString("noticeNo");
                    noticeSdt = object.getString("noticeSdt");
                    noticeEdt = object.getString("noticeEdt");
                    popfile = object.getString("popfile");
                    popfile = popfile.replace("\\", "");
                    processState=object.getString("processState");
                    sexCd=object.getString("sexCd");
                    neuterYn = object.getString("neuterYn");
                    specialMark = object.getString("specialMark");
                    careNm = object.getString("careNm");
                    careTel = object.getString("careTel");
                    careAddr = object.getString("careAddr");
                    orgNm=object.getString("orgNm");
                    officetel=object.getString("officetel");
                    //img = new StringToBitMap().execute(popfile).get();



                    //Log.e("확인", desertionNo);
                    Shelter shelter = new Shelter(desertionNo,filename,happenDt,happenPlace,kindCd, colorCd,age,weight,noticeNo,noticeSdt,noticeEdt,popfile,
                            processState,sexCd,neuterYn,specialMark,careNm,careTel,careAddr, orgNm,officetel);



                    arrayList.add(shelter);


                }

                adapter = new AutoScrollAdapter(getApplicationContext(),arrayList);




/*

                shelterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), ShelterPostActivite.class);
                        intent.putExtra("desertionNo", arrayList.get(position).getDesertionNo());
                        intent.putExtra("filename", arrayList.get(position).getFilename());
                        intent.putExtra("happenDt", arrayList.get(position).getHappenDt());
                        intent.putExtra("happenPlace", arrayList.get(position).getHappenPlace());
                        intent.putExtra("kindCd", arrayList.get(position).getKindCd());
                        intent.putExtra("colorCd",arrayList.get(position).getColorCd());
                        intent.putExtra("age",arrayList.get(position).getAge());
                        intent.putExtra("weight", arrayList.get(position).getWeight());
                        intent.putExtra("noticeNo", arrayList.get(position).getNoticeNo());
                        intent.putExtra("noticeSdt", arrayList.get(position).getNoticeSdt());
                        intent.putExtra("noticeEdt", arrayList.get(position).getNoticeEdt());
                        intent.putExtra("popfile", arrayList.get(position).getPopfile());
                        intent.putExtra("processState",arrayList.get(position).getProcessState());
                        intent.putExtra("sexCd",arrayList.get(position).getSexCd());
                        intent.putExtra("neuterYn", arrayList.get(position).getNeuterYn());
                        intent.putExtra("specialMark", arrayList.get(position).getSpecialMark());
                        intent.putExtra("careNm", arrayList.get(position).getCareNm());
                        intent.putExtra("careTel", arrayList.get(position).getCareTel());
                        intent.putExtra("careAddr", arrayList.get(position).getCareAddr());
                        intent.putExtra("orgNm",arrayList.get(position).getOrgNm());
                        intent.putExtra("officetel",arrayList.get(position).getOfficetel());
                        //intent.putExtra("img",arrayList.get(position).getImg());
                        startActivity(intent);

                    }
                });


 */
            }catch (Exception e){

                e.printStackTrace();
            }



        }



    }


}
