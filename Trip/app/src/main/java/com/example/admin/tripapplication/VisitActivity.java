package com.example.admin.tripapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.tripapplication.json.Json1Activity;
import com.example.admin.tripapplication.json.Json2Activity;
import com.example.admin.tripapplication.json.JsonActivity;
import com.example.admin.tripapplication.logger.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class Visit {
    String name;
    String add;
    String image;
    int id;
    double lat;
    double lng;

    public Visit(String name, String add, String image, int id, double lat, double lng) {
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

class VisitAdapter extends ArrayAdapter<Visit> {
    Context context;
    int resource;
    private ProgressDialog progressDialog;
    Bitmap bitmap;
    public VisitAdapter(@NonNull Context context, int resource, @NonNull List<Visit> objects) {
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

public class VisitActivity extends AppCompatActivity {
    ArrayList<Visit> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        String json = null;
        ListView listView = findViewById(R.id.visitlist);
        arrayList = new ArrayList<>();
        try {
            Intent intent = getIntent();
            String jsonFile=intent.getStringExtra("json");
            InputStream is = getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject order = ja.getJSONObject(i);
                try {
                    arrayList.add(new Visit(order.getString("title"), order.getString("addr1"),
                            order.getString("firstimage"), order.getInt("contentid"), order.getDouble("mapy"), order.getDouble("mapx")));
                } catch (Exception e) {
                   /* arrayList.add(new Visit(order.getString("title"), order.getString("addr1"),
                            "", order.getInt("contentid"), order.getDouble("mapy"), order.getDouble("mapx")));*/
                   e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        VisitAdapter visitAdapter = new VisitAdapter(this, R.layout.visitlist, arrayList);
        listView.setAdapter(visitAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(VisitActivity.this, VisitItemActivity.class);
                intent.putExtra("id", arrayList.get(position).getId());
                intent.putExtra("name", arrayList.get(position).getName());
                intent.putExtra("lat", arrayList.get(position).getLat());
                intent.putExtra("lng", arrayList.get(position).getLng());
                startActivity(intent);
            }
        });
    }

}
