package com.example.admin.tripapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.tripapplication.logger.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



class LVisit {
    String name;
    String add;
    String image;
    int id;
    double lat;
    double lng;

    public LVisit(String name, String add, String image, int id, double lat, double lng) {
        this.name = name;
        this.add = add;
        this.image = image;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}

class LVisitAdapter extends ArrayAdapter<LVisit> {
    Context context;
    int resource;
    private ProgressDialog progressDialog;
    Bitmap bitmap;
    public LVisitAdapter(@NonNull Context context, int resource, @NonNull List<LVisit> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resource, null);
        TextView name = convertView.findViewById(R.id.visitname);
        TextView addr = convertView.findViewById(R.id.visitaddr);
        ImageView imageView=convertView.findViewById(R.id.visitimage);
        name.setText(getItem(position).getName());
        addr.setText(getItem(position).getAdd());
        if(!getItem(position).getImage().equals("")) {
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(getItem(position).getImage());
                        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is =conn.getInputStream();
                        bitmap= BitmapFactory.decodeStream(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start();
            try{
                mThread.join();
                imageView.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }
}
public class LVisitActivity extends AppCompatActivity {
    ArrayAdapter adapter;
    int radius;
    ArrayList<LVisit> arrayList;
    String urlAddress;
    double lat;
    double lng;
    JSONArray jsonArray;
    Bitmap bitmap;
    Handler handler = new Handler();
    ImageView imageVisit;
    LVisitAdapter visitAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvisit);
        Intent intent=getIntent();
        lat=intent.getDoubleExtra("lat", 0);
        lng=intent.getDoubleExtra("lng", 0);
        radius=500;
        urlAddress="http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=R6v0pzqdeYAmMRisF2HXcUwLPPSZG%2Fwya3znAd%2BSphY1ekm849EACM%2BNKzDtSLddvQeU7uUg7j6twz%2FgKgtuPg%3D%3D&contentTypeId=12&mapX="+lng+"&mapY="+lat+"&radius="+radius+"&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=1000&pageNo=1&_type=json";
        listView=findViewById(R.id.LlistView);
        Spinner s =findViewById(R.id.spinner1);
        String json = null;
        adapter = ArrayAdapter.createFromResource(LVisitActivity.this, R.array.radius, android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sr=(String)parent.getItemAtPosition(position);
                radius= Integer.parseInt(sr);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button reButton=findViewById(R.id.reButton);
        reButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                urlAddress="http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=R6v0pzqdeYAmMRisF2HXcUwLPPSZG%2Fwya3znAd%2BSphY1ekm849EACM%2BNKzDtSLddvQeU7uUg7j6twz%2FgKgtuPg%3D%3D&contentTypeId=12&mapX="+lng+"&mapY="+lat+"&radius="+radius+"&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=1000&pageNo=1&_type=json";
                loadHtml();
            }
        });

        arrayList = new ArrayList<>();
        loadHtml();

        visitAdapter= new LVisitAdapter(LVisitActivity.this, R.layout.visitlist, arrayList);
        listView.setAdapter(visitAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(LVisitActivity.this, VisitItemActivity.class);
                intent.putExtra("id", arrayList.get(position).getId());
                intent.putExtra("name", arrayList.get(position).getName());
                intent.putExtra("lat", arrayList.get(position).getLat());
                intent.putExtra("lng", arrayList.get(position).getLng());
                startActivity(intent);
            }
        });
        Toast.makeText(this, urlAddress, Toast.LENGTH_SHORT).show();
    }
    void loadHtml() { // 웹에서 html 읽어오기
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuffer sb = new StringBuffer();
                try {
                    URL url = new URL(urlAddress);
                    HttpURLConnection conn =
                            (HttpURLConnection)url.openConnection();// 접속
                    if (conn != null) {
                        conn.setConnectTimeout(2000);
                        conn.setUseCaches(false);
                        String line="";
                        String input="";
                        if (conn.getResponseCode()
                                ==HttpURLConnection.HTTP_OK){
                            //    데이터 읽기
                            BufferedReader br
                                    = new BufferedReader(new InputStreamReader
                                    (conn.getInputStream(),"utf-8"));//"utf-8"
                            while(true) {
                                line = br.readLine();
                                if (line == null) break;
                                // sb.append(line+"\n");
                                input+=line;
                            }
                            JSONObject jsonObject=new JSONObject(input);
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            JSONObject jsonObject2=jsonObject1.getJSONObject("body");
                            JSONObject jsonObject3=jsonObject2.getJSONObject("items");
                            jsonArray=jsonObject3.getJSONArray("item");
                            Log.d("test", urlAddress);
//                            sb.append(jsonObject4.getString("firstimage"));
                            br.close(); // 스트림 해제
                        }

                        conn.disconnect(); // 연결 끊기
                    }
                    // 값을 출력하기
                    Log.d("test", sb.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            textView.setText(sb.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject order = null;
                                try {
                                    order = jsonArray.getJSONObject(i);
                                    try {
                                        arrayList.add(new LVisit(order.getString("title"), order.getString("addr1"),
                                                order.getString("firstimage"), order.getInt("contentid"), order.getDouble("mapy"), order.getDouble("mapx")));
                                    } catch (Exception e) {
                                        arrayList.add(new LVisit(order.getString("title"), order.getString("addr1"),
                                                "", order.getInt("contentid"), order.getDouble("mapy"), order.getDouble("mapx")));
                                    }
                                    visitAdapter= new LVisitAdapter(LVisitActivity.this, R.layout.visitlist, arrayList);
                                    listView.setAdapter(visitAdapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작
    }
}