package com.example.admin.tripapplication.BLU;


import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.tripapplication.R;

import java.util.ArrayList;

public class Step03 extends Fragment {

    public static Context mContext;

    ImageView arrow1;
    ImageView arrow2;
    TextView t1;
    TextView t2;
    TextView t3;

    Button start;


    ArrayList<SensorList> sensorLists;

    private RecyclerView sRecyclerView;
    private RecyclerView.Adapter sAdapter;

    // use a linear layout manager
    private RecyclerView.LayoutManager sLayoutManager;

    //블루투스
    BluetoothManager btManager;
    BluetoothLeScanner btScanner;
    BluetoothAdapter btAdapter;

    private Handler mHandler = new Handler();
    private static final long SCAN_PERIOD = 3000;


    Boolean btScanning = false;
    ArrayList<BluetoothDevice> devicesDiscovered = new ArrayList<BluetoothDevice>();

    ViewGroup rootView;
    Step activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.step_03, container, false);
        activity = (Step)getActivity();
        mContext = getActivity();


        arrow1 = (ImageView)rootView.findViewById(R.id.arrow1);
        arrow1.setOnClickListener(event);

        arrow2=(ImageView)rootView.findViewById(R.id.arrow2);
        arrow2.setOnClickListener(event);

        start =(Button)rootView.findViewById(R.id.start_btn);
        start.setOnClickListener(event);
        t1=(TextView)rootView.findViewById(R.id.t1);
        t2=(TextView)rootView.findViewById(R.id.t2);
        t3=(TextView)rootView.findViewById(R.id.t3);

        btManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();



        sRecyclerView = rootView.findViewById(R.id.sensorlist);

        sRecyclerView.setHasFixedSize(true);

        sLayoutManager = new LinearLayoutManager(getActivity());

        sRecyclerView.setLayoutManager(sLayoutManager);

        // specify an adapter (see also next example)
        sensorLists= new ArrayList<>();

        sRecyclerView.setOnClickListener(event);

        return rootView;
    }


    View.OnClickListener event =new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()){
                case R.id.arrow1:
                    activity.onFragmentChanged(2);
                    break;
                case R.id.start_btn:
                    t2.setText("신호가 가장 강한것을 선택하세요");
                    t3.setVisibility(View.VISIBLE);
                    startScanning();
                    break;
                case R.id.arrow2:

                     activity.onFragmentChanged(4);
                 break;
            }
        }
    };



  public void startScanning() {
        //System.out.println("start scanning");
        btScanner = btAdapter.getBluetoothLeScanner();
        btScanning = true;

        devicesDiscovered.clear();

        Toast.makeText(mContext,"Started Scanning",Toast.LENGTH_SHORT).show();

        sensorLists.clear();

        sAdapter = new SAdapter(sensorLists,mContext,(Step) getActivity(), rootView);

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                btScanner.startScan(leScanCallback);

            }

        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScanning();
            }
        }, SCAN_PERIOD);
    }


    double rssi;
    private ScanCallback leScanCallback = new ScanCallback() {



        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

         /*  if(result.getDevice().getName().equals("HMSoft")){

            }*/

        rssi = ((-55)/(double)result.getRssi())*100;
     if(rssi>100){
            rssi=100;
        }
           Log.d( "Tiger", ""+(int)rssi + " "+result.getRssi());



             sensorLists.add(new SensorList(result.getDevice().getName(), result.getDevice().getAddress(), (int)rssi));
             //devicesDiscovered.add(result.getDevice());
            //hashSet = new HashSet<>(sensorLists);
           // sensorLists2 = new ArrayList<>(hashSet);
             sRecyclerView.setAdapter(sAdapter);

        }
    };




    public void stopScanning() {
        Toast.makeText(mContext,"stoppig scanning",Toast.LENGTH_SHORT).show();


        //devicesDiscovered.add(result.getDevice());

        btScanning = false;


        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });
    }
}
