package com.example.admin.tripapplication.json;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.logger.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        textView=findViewById(R.id.tv1);
        jsonParsing();
    }
    private void jsonParsing() {
        String json = null;
        Log.d("json", "!");
        try {
            String result="";
            InputStream is = getAssets().open("areaBasedList1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray ja= new JSONArray(json);
            for(int i=0; i<ja.length(); i++){
                JSONObject order=ja.getJSONObject(i);
                result+=order.getString("addr1");
            }
            textView.setText(result);
/*
            JSONObject root = new JSONObject(json);
            textView.setText(root.getString("addr1"));
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
