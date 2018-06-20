package com.example.admin.tripapplication.cameraAction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.tripapplication.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService extends Activity implements LocationListener {

    LocationManager locManager;
    Geocoder geo;
    Location myLocation = null;
    double lat, lon;
    static String city = "";
    Button btn1, btn2;
    TextView[] tvs = new TextView[3];

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // GPS로 부터 위치정보를 업데이트 요청, 1초마다 5km 이동시
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
        // 기지국으로 부터 위치정보를 업데이트 요청
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
        geo = new Geocoder(this, Locale.KOREA);

        for (int i = 0; i < 3; i++) {
            int id = getResources().getIdentifier("tv" + (i + 1), "id", "com.example.admin.tripapplication");
            tvs[i] = findViewById(id);
        }

        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                GetLocations();
            }
        });
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    void GetLocations() {
        if (myLocation != null) {
            lat = myLocation.getLatitude();
            lon = myLocation.getLongitude();
            try {
                // 위도,경도를 이용하여 현재 위치의 주소를 가져옴
                List<Address> addresses;
                addresses = geo.getFromLocation(lat, lon, 1);
                Address address = addresses.get(0);
                city = address.getLocality();
                tvs[0].setText(lat + "");
                tvs[1].setText(lon + "");
                tvs[2].setText(city + "");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Log.d("tiger", "GetLocations: aa");
        }
    }

    public void onLocationChanged(Location location) {
        myLocation = location;
    }

    public void onProviderDisabled(String s) {
    }

    public void onProviderEnabled(String s) {
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
}