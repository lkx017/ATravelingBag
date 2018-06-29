package com.example.admin.tripapplication;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.tripapplication.logger.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VisitItemActivity extends AppCompatActivity implements OnMapReadyCallback {
    String urlAddress;
    Handler handler = new Handler();
    ImageView imageVisit;
    TextView tvVisit;
    TextView addrVisit;
    TextView viewVisit;
    Bitmap bitmap;
    String NAME;
    JSONObject jsonObject4;
    double lat;
    double lng;
    String title;
    TextView mapText;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    ImageView image5;
    ImageView image6;
    ImageView image7;
    ImageView image8;
    ImageView image9;
    ImageView image10;
    String[] imageArr=new String[10];
    ScrollView scrollView;
    private String clientId = "VYNQQKTxIhdTV95Uur6h"; //네이버에서 발급받은 애플리케이션 클라이언트 아이디값";
    private String clientSecret = "6h_GQb94tl"; //네이버에서 발급받은애플리케이션 클라이언트 시크릿값";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_item);
        Intent intent=getIntent();
        int ID=intent.getIntExtra("id", 0);
        NAME=intent.getStringExtra("name");
        lat=intent.getDoubleExtra("lat", 0);
        lng=intent.getDoubleExtra("lng", 0);
        Toast.makeText(this, ID+"", Toast.LENGTH_SHORT).show();
        urlAddress="http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey=R6v0pzqdeYAmMRisF2HXcUwLPPSZG%2Fwya3znAd%2BSphY1ekm849EACM%2BNKzDtSLddvQeU7uUg7j6twz%2FgKgtuPg%3D%3D&contentTypeId=12&contentId="+ID+"&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y &_type=json";
        imageVisit=findViewById(R.id.imageVisit);
        tvVisit=findViewById(R.id.tvVisit);
        addrVisit=findViewById(R.id.addrVisit);
        viewVisit=findViewById(R.id.viewVisit);
        mapText=findViewById(R.id.mapText);
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);
        image5=findViewById(R.id.image5);
        image6=findViewById(R.id.image6);
        image7=findViewById(R.id.image7);
        image8=findViewById(R.id.image8);
        image9=findViewById(R.id.image9);
        image10=findViewById(R.id.image10);
        scrollView=findViewById(R.id.scrollView);

        mapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(VisitItemActivity.this, VisitMapActivity.class);
                intent1.putExtra("lat", lat);
                intent1.putExtra("lng", lng);
                intent1.putExtra("name", NAME);
                intent1.putExtra("title", title);
                startActivity(intent1);
            }
        });
       /* FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(VisitItemActivity.this);
        LoadingTask loadingTask=new LoadingTask();
        loadingTask.execute();*/
        loadHtml();
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
                            jsonObject4=jsonObject3.getJSONObject("item");

                            br.close(); // 스트림 해제
                        }
                        conn.disconnect(); // 연결 끊기
                    }
                    // 값을 출력하기
                    Log.d("test", sb.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("ddd", "애혀");
                                tvVisit.setText(jsonObject4.getString("title"));
                                addrVisit.setText(jsonObject4.getString("addr1"));
                                String overview=jsonObject4.getString("overview");
                                overview=overview.replaceAll("<br>|<br \\/>|&lt;|&gt;|<strong>|<strong \\/>|<\\/strong>|&nbsp;", "");
                                viewVisit.setText(overview);
                                title=jsonObject4.getString("title");
                                requestWithSomeHttpHeaders();
                                final String urlName=jsonObject4.getString("firstimage");
                                if(!jsonObject4.getString("firstimage").equals("")){
                                    Picasso.get().load(urlName).into(imageVisit);
                                   /* Thread mThread = new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                URL url = new URL(urlName);
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
                                        imageVisit.setImageBitmap(bitmap);
                                        //imageVisit.setImageBitmap(bitmap);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }*/
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                           // textView.setText(sb.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng place = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place);
        markerOptions.title(NAME);
        markerOptions.snippet(title);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        googleMap.setOnInfoWindowClickListener(infoWindowClickListener);
    }
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, marker.getTitle().toString());
            startActivity(intent);
        }
    };
    class LoadingTask extends AsyncTask<Void, Void, Void>{
        ProgressDialog progressDialog=new ProgressDialog(VisitItemActivity.this);
        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중입니다..");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                for (int i = 0; i < 5; i++) {
                    progressDialog.setProgress(i * 30);
                    loadHtml();
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void requestWithSomeHttpHeaders() {
        RequestQueue queue = Volley.newRequestQueue(VisitItemActivity.this);
        String NAVERURL = "https://openapi.naver.com/v1/search/image?query=" + tvVisit.getText() + "&display=10";


        StringRequest postRequest = new StringRequest(Request.Method.GET, NAVERURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
// response
                        String result = "";
                        android.util.Log.d("DDD", response);
                        JSONObject jo = null;
                        try {
                            jo = new JSONObject(response);
                            JSONArray ja=jo.getJSONArray("items");
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject order = ja.getJSONObject(i);
                                imageArr[i]=order.getString("link");
                            }
                            Picasso.get().load(imageArr[0]).resize(400,400).into(image1);
                            Picasso.get().load(imageArr[1]).resize(400,400).into(image2);
                            Picasso.get().load(imageArr[2]).resize(400,400).into(image3);
                            Picasso.get().load(imageArr[3]).resize(400,400).into(image4);
                            Picasso.get().load(imageArr[4]).resize(400,400).into(image5);
                            Picasso.get().load(imageArr[5]).resize(400,400).into(image6);
                            Picasso.get().load(imageArr[6]).resize(400,400).into(image7);
                            Picasso.get().load(imageArr[7]).resize(400,400).into(image8);
                            Picasso.get().load(imageArr[8]).resize(400,400).into(image9);
                            Picasso.get().load(imageArr[9]).resize(400,400).into(image10);
                            android.util.Log.d("SSS", "그려~!");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
// TODO Auto-generated method stub
                        android.util.Log.d("s", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("X-Naver-Client-Id", clientId);
                params.put("X-Naver-Client-Secret", clientSecret);
                android.util.Log.d("getHedaer =>", "" + params);
                return params;
            }
        };
        queue.add(postRequest);
    }
}
