package com.example.petmileymain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShelterActivity extends AppCompatActivity {

    private static String IP_ADDRESS ="15.164.220.44";
    private ListView shelterListView;
    private ShelterListAdapter adapter;
    private List<Shelter> ShelterList;

    TextView selterText;
    TextView nameText;
    TextView dataText;
    Spinner siSp;
    private ArrayAdapter GunAdapter;
    Spinner GunSp;
    Spinner speciesSp;
    Button speciesBtn;
    Button spBtn;
    Button dateBtn;
    RoundedImageView image;
    private Button btnImageSearch;

    String local = "";
    String speciesData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter);
        shelterListView = (ListView)findViewById(R.id.shelterListView);
        //ShelterList = new ArrayList<Shelter>();

        siSp = (Spinner)findViewById(R.id.spSi);
        GunSp = (Spinner)findViewById(R.id.spGun);
        speciesSp =(Spinner)findViewById(R.id.spSpecies);
        new xmlParser().execute();
        btnImageSearch = (Button)findViewById(R.id.btnImgSearch);

        btnImageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageSearch.class);
                intent.putExtra("flag","shelter");
                //startActivityForResult(intent, 1);
                startActivity(intent);
            }
        });


        siSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.seoul,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 2:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gyeonggi,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 3:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.incheon,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 4:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sejong,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 5:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.daegu,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 6:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.daejeon,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 7:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.busan,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 8:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gwangju,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 9:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ulsan,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 10:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gangwon,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 11:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.chungbuk,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 12:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.chungnam,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 13:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.jeonbuk,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 14:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.jeonnam,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 15:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gyeongbuk,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 16:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gyeonnam,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;
                    case 17:
                        GunAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.jeju,R.layout.support_simple_spinner_dropdown_item);
                        GunSp.setAdapter(GunAdapter);
                        break;


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GunSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                spBtn = (Button)findViewById(R.id.btnSpSearch);
                spBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position == 0){ local = ""; }
                        else local = GunSp.getSelectedItem().toString();
                        new xmlParser().execute();
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        speciesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                speciesBtn = (Button)findViewById(R.id.btnSpecies);
                speciesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position == 0){ speciesData = "";}
                        else{ speciesData = speciesSp.getSelectedItem().toString(); }
                        new xmlParser().execute();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class ShelterListAdapter extends BaseAdapter{
        private Context context;
        private List<Shelter> ShelterList;
        Filter listFilter;

        public ShelterListAdapter(Context context, List<Shelter> selterList) {
            this.context = context;
            this.ShelterList = selterList;
        }

        @Override
        public int getCount() {

            return ShelterList.size();
        }

        @Override
        public Object getItem(int i) {
            return ShelterList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View View, ViewGroup viewGroup) {
            View v= View.inflate(context,R.layout.shelter,null);
            selterText=(TextView)v.findViewById(R.id.shelterText);
            nameText=(TextView)v.findViewById(R.id.nameText);
            dataText=(TextView)v.findViewById(R.id.dataText);
            image=(RoundedImageView)v.findViewById(R.id.shelterImage);


            selterText.setText((ShelterList.get(i).getKindCd()));
            nameText.setText((ShelterList.get(i).getCareAddr()));
            dataText.setText((ShelterList.get(i).getCareNm()));

            Picasso.get().load((ShelterList.get(i).getPopfile())).into(image);
            //image.setImageBitmap((ShelterList.get(i).getImg()));


            v.setTag(ShelterList.get(i));
            return v;
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
            target = "http://"+IP_ADDRESS+"/ShelterActivite.php?kindCd="+speciesData+"&careAddr="+local;
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

                adapter = new ShelterListAdapter(getApplicationContext(),arrayList);
                shelterListView.setAdapter(adapter);

                dateBtn = (Button) findViewById(R.id.btnDate);
                dateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Comparator<Shelter> DataAsc = new Comparator<Shelter>() {
                            @Override
                            public int compare(Shelter o1, Shelter o2) {
                                return o1.noticeEdt.compareTo(o2.noticeEdt);
                            }
                        };
                        Collections.sort(arrayList, DataAsc);
                        adapter.notifyDataSetChanged();
                    }
                });

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

            }catch (Exception e){

                e.printStackTrace();
            }



        }



    }

    /*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(ShelterActivity.this, data.getStringExtra("fileResult"), Toast.LENGTH_SHORT).show();
                //mArrayList.clear();
                //new xmlParser().execute();

            } else {   // RESULT_CANCEL
                Toast.makeText(ShelterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }

    }

     */
}


