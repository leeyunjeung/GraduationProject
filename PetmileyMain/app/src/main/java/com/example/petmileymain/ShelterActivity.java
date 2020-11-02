package com.example.petmileymain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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


    TextView selterText;
    TextView nameText;
    TextView dataText;

    Button btnLocal;
    Button btnType;
    Button btnGongo;
    Button btnSigun;
    ImageView image;
    private Button btnImageSearch;
    private Toolbar toolbar;
    String local = "";
    String speciesData = "";
    String sigun = "";
    String searchFile=null;

    int typeItem = 0;
    int localItem = 0;
    int sigunItem = 0;

    List typeSelectedItems  = new ArrayList();
    List localSelectedItems  = new ArrayList();
    List sigunSelectedItems = new ArrayList();

    String[] si;
    String[] gun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter);
        shelterListView = (ListView)findViewById(R.id.shelterListView);
        //ShelterList = new ArrayList<Shelter>();


        toolbar = findViewById(R.id.shelter_toolbar);
        setSupportActionBar(toolbar);


        btnLocal = (Button)findViewById(R.id.btnLocal);
        btnType = (Button)findViewById(R.id.btnType);
        btnSigun = (Button)findViewById(R.id.btnSigun);




        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localShow();

            }
        });
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speciesShow();

            }
        });
        btnSigun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigunShow();

            }
        });

        new xmlParser().execute();




    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnImageSearch:
                Intent intent = new Intent(getApplicationContext(), ImageSearch.class);
                intent.putExtra("flag","shelter");
                startActivityForResult(intent, 1);
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


    void localShow()
    {
        localSelectedItems.add(localItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("지역 선택");

        si = getResources().getStringArray(R.array.localArray);
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

                            localItem=index;
                        }
                        if(msg.equals("모든 지역")){
                            local="";
                        }
                        else{
                            local=msg;
                            switch (msg){
                                case "서울특별시" :
                                    gun = getResources().getStringArray(R.array.seoul);
                                    break;
                                case "경기도" :
                                    gun = getResources().getStringArray(R.array.gyeonggi);
                                    break;
                                case "인천광역시" :
                                    gun = getResources().getStringArray(R.array.incheon);
                                    break;
                                case "세종특별자치시" :
                                    gun = getResources().getStringArray(R.array.sejong);
                                    break;
                                case "대구광역시" :
                                    gun = getResources().getStringArray(R.array.daegu);
                                    break;
                                case "대전광역시" :
                                    gun = getResources().getStringArray(R.array.daejeon);
                                    break;
                                case "부산광역시" :
                                    gun = getResources().getStringArray(R.array.busan);
                                    break;
                                case "광주광역시" :
                                    gun = getResources().getStringArray(R.array.gwangju);
                                    break;
                                case "울산광역시" :
                                    gun = getResources().getStringArray(R.array.ulsan);
                                    break;
                                case "강원도" :
                                    gun = getResources().getStringArray(R.array.gangwon);
                                    break;
                                case "충청북도" :
                                    gun = getResources().getStringArray(R.array.chungbuk);
                                    break;
                                case "충청남도" :
                                    gun = getResources().getStringArray(R.array.chungnam);
                                    break;
                                case "전라북도" :
                                    gun = getResources().getStringArray(R.array.jeonbuk);
                                    break;
                                case "전라남도" :
                                    gun = getResources().getStringArray(R.array.jeonnam);
                                    break;
                                case "경상북도" :
                                    gun = getResources().getStringArray(R.array.gyeongbuk);
                                    break;
                                case "경상남도" :
                                    gun = getResources().getStringArray(R.array.gyeonnam);
                                    break;
                                case "제주특별자치도" :
                                    gun = getResources().getStringArray(R.array.jeju);
                                    break;
                            }
                            Log.d("shelter local",local);
                        }
                        new xmlParser().execute();
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

    void sigunShow()
    {
        sigunSelectedItems.add(sigunItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("시/군 선택");


        builder.setSingleChoiceItems(gun, sigunItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sigunSelectedItems.clear();
                        sigunSelectedItems.add(which);

                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg="";
                        if (!sigunSelectedItems.isEmpty()) {
                            int index = (int) sigunSelectedItems.get(0);
                            msg = gun[index];

                            sigunItem=index;
                        }
                        if(msg.equals("선택안함")){
                            local="";
                        }
                        else{
                            local=msg;
                            Log.d("shelter local",local);
                        }
                        new xmlParser().execute();
                        btnSigun.setText(msg);
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }

    void speciesShow()
    {
        final List<String> typeListItems = new ArrayList<>();
        typeListItems.add("모든 동물");
        typeListItems.add("개");
        typeListItems.add("고양이");
        typeListItems.add("기타");

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
                            speciesData="";
                        }
                        else{
                            speciesData=msg;
                        }

                        new xmlParser().execute();
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
            image=(ImageView)v.findViewById(R.id.shelterImage);



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
            target = "http://"+IP_ADDRESS+"/ShelterActivite.php?kindCd="+speciesData+"&careAddr="+local+"&searchFile="+searchFile;
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


                btnGongo = (Button)findViewById(R.id.btnGongo);
                btnGongo.setOnClickListener(new View.OnClickListener() {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                searchFile=data.getStringExtra("fileResult").replace(",","|").replace("\"" ,"");
                Toast.makeText(ShelterActivity.this, searchFile, Toast.LENGTH_SHORT).show();
                Log.d("searchfile",searchFile);
                new xmlParser().execute();

            } else {   // RESULT_CANCEL
                Toast.makeText(ShelterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }

    }
}


