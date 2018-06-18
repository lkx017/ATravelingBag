package com.example.admin.tripapplication.json;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.logger.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Json2Activity extends AppCompatActivity {
    TextView textView;
    String urlAddress = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey=R6v0pzqdeYAmMRisF2HXcUwLPPSZG%2Fwya3znAd%2BSphY1ekm849EACM%2BNKzDtSLddvQeU7uUg7j6twz%2FgKgtuPg%3D%3D&contentTypeId=12&contentId=2518764&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y&_type=json";
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        textView = findViewById(R.id.tv1);
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
                            JSONObject jsonObject4=jsonObject3.getJSONObject("item");
                            sb.append(jsonObject4.getString("firstimage"));
                            br.close(); // 스트림 해제
                        }
                        conn.disconnect(); // 연결 끊기
                    }
                    // 값을 출력하기
                    Log.d("test", sb.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(sb.toString());
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


