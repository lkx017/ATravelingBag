package com.example.admin.tripapplication.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.tripapplication.LVisitActivity;
import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.VisitActivity;
import com.example.admin.tripapplication.googlemap.MapsActivity;
import com.example.admin.tripapplication.json.JsonActivity;
import com.example.admin.tripapplication.logger.Log;
import com.example.admin.tripapplication.storage.StorageActivity;
import com.github.florent37.materialviewpager.MaterialViewPagerHeaderView;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

class TravelAdapter extends ArrayAdapter<Travel> {
    Context context;
    int resource;

    public TravelAdapter(@NonNull Context context, int resource, @NonNull List<Travel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resource, null);
        CircleImageView circleImageView = convertView.findViewById(R.id.circleView);
        TextView textView = convertView.findViewById(R.id.travelTV1);
        Drawable drawable = getContext().getResources().getDrawable(getItem(position).getImage());
        circleImageView.setImageDrawable(drawable);
        textView.setText(getItem(position).getName());


        return convertView;
    }
}

class Travel {
    int image;
    String name;

    public Travel(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

public class FirstFragment extends Fragment {
    double lat;
    double lng;
    private static final boolean GRID_LAYOUT = false;
//    private static final int ITEM_COUNT = 100;

    @BindView(R.id.ll1)
    LinearLayout linearLayout;
    LocationManager manager;

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Intent intent;
    String s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        startLocationService();
        ListView listView = rootView.findViewById(R.id.flv);
        ArrayList<Travel> arrayList = new ArrayList<>();
        arrayList.add(new Travel(R.drawable.travel1, "전주"));
        arrayList.add(new Travel(R.drawable.travel2, "경주"));
        arrayList.add(new Travel(R.drawable.travel3, "제주"));
        arrayList.add(new Travel(R.drawable.travel7, "부산"));
        arrayList.add(new Travel(R.drawable.travel5, "대구"));
        arrayList.add(new Travel(R.drawable.travel6, "대전"));
        arrayList.add(new Travel(R.drawable.travel4, "내 주변"));
        Log.d("tiger", "1");
        TravelAdapter travelAdapter = new TravelAdapter(getContext(), R.layout.travellist, arrayList);
        listView.setAdapter(travelAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        intent = new Intent(getContext(), VisitActivity.class);
                        s = "areaBasedList1.json";
                        intent.putExtra("json", s);
                        startActivity(intent);

                        Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        intent = new Intent(getContext(), VisitActivity.class);
                        s = "areaBasedList2.json";
                        intent.putExtra("json", s);
                        startActivity(intent);
                        Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        intent = new Intent(getContext(), VisitActivity.class);
                        s = "areaBasedList3.json";
                        intent.putExtra("json", s);
                        startActivity(intent);
                        Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        intent = new Intent(getContext(), VisitActivity.class);
                        s = "areaBasedList4.json";
                        intent.putExtra("json", s);
                        startActivity(intent);
                        Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        intent = new Intent(getContext(), VisitActivity.class);
                        s = "areaBasedList5.json";
                        intent.putExtra("json", s);
                        startActivity(intent);
                        Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        intent = new Intent(getContext(), VisitActivity.class);
                        s = "areaBasedList6.json";
                        intent.putExtra("json", s);
                        startActivity(intent);
                        Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
                      /*  intent = new Intent(getContext(), StorageActivity.class);
                        startActivity(intent);*/
                        intent = new Intent(getContext(), LVisitActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        startActivity(intent);
                        break;
                }
            }
        });
        return rootView;
    }

    public void startLocationService() {
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        long minTime = 1000;
        float minDistance = 1;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Don't have permissions", Toast.LENGTH_SHORT).show();
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener);
    }

    public void stopLocationService() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Don't have permissions", Toast.LENGTH_SHORT).show();
            return;
        }
        manager.removeUpdates(mLocationListener);
    }

    public final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            stopLocationService();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
