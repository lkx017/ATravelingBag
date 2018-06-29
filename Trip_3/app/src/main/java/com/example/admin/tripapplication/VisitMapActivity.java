package com.example.admin.tripapplication;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class VisitMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    String NAME;
    String title;
    double lat;
    double lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_map);
        Intent intent=getIntent();
        NAME=intent.getStringExtra("name");
        title=intent.getStringExtra("title");
        lat=intent.getDoubleExtra("lat", 0);
        lng=intent.getDoubleExtra("lng", 0);
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(VisitMapActivity.this);
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
            Intent intent1 = new Intent(Intent.ACTION_WEB_SEARCH);
            intent1.putExtra(SearchManager.QUERY, marker.getTitle().toString());
            startActivity(intent1);
        }
    };
}
