package com.example.admin.tripapplication.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.admin.tripapplication.BLU.AttributeList;
import com.example.admin.tripapplication.BLU.Details;
import com.example.admin.tripapplication.BLU.MAdapter;
import com.example.admin.tripapplication.BLU.MyBleList;
import com.example.admin.tripapplication.BLU.MyList;
import com.example.admin.tripapplication.MainActivity;
import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.login.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BleFragment extends Fragment {

    //sound----------
    SoundPool pool;

    int soundid=0;

    //----------------[

    MyBleList myBleList;
    static String key;

    public static Context mContext;

    public static BleFragment bleFragment;

    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    static String userID;
    ArrayList<User> userItem=new ArrayList<>();

    private final static int REQUEST_ENABLE_BT = 2000;

    static Switch bleable;
    Button addbtn;

    static ArrayList<MyList> myList;

    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String TAG = "Tiger";

    int position ;

    BluetoothLeScanner btScanner;
    static ArrayList<BluetoothGatt> bluetoothGatts = new ArrayList<>();
    BluetoothDevice devicesDiscovered;


    private Handler mHandler = new Handler();
    private final long SCAN_PERIOD = 3000;

    LinearLayout ble_main;


    ///---------연결되고 난 후
    static ArrayList<Integer> conflag = new ArrayList<>();

    int receveflag = 0;
    Handler recevehandler=null;

    ProgressDialog asyncDialog;

    double[] average_array = new double[10];
    double average =0;
    double distance;
    
    static ArrayList<Handler> handlers = new ArrayList<>();
    static ArrayList<Handler> handlers0 = new ArrayList<>();


    static ArrayList<Integer>  prevent_flag = new ArrayList<>() ;
    static ArrayList<Integer> con_rssi = new ArrayList<>();


    //static ArrayList<NotificationCompat.Builder> nb = new ArrayList<>();
   // static ArrayList<NotificationManager> nm = new ArrayList<>();


    static ArrayList<BluetoothGattCharacteristic> characs = new ArrayList<>();

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    //Vibrator vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);

    public static BleFragment newInstance() {
        return new BleFragment();
    }

    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = firebaseDatabase.getReference();

    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.ble_main_fragment, container,false);

        mContext =  getActivity();
        bleFragment = this;
       Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("tiger", "호랑이");
            userID = bundle.getString("id");
            userItem=(ArrayList<User>)bundle.getSerializable("users");
        }

        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        soundid= pool.load(mContext,R.raw.s1, 1);

        ble_main = rootview.findViewById(R.id.ble_main);


        bleable=rootview.findViewById(R.id.switch1);
        bleable.setChecked(false);
        addbtn=rootview.findViewById(R.id.addbtn);
        addbtn.setEnabled(false);

        mRecyclerView = rootview.findViewById(R.id.myblelist);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myList= new ArrayList<>();
        // myList.add(new MyList("블루투스","MAC"));

        mAdapter = new MAdapter(myList,mContext,bleFragment);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.INVISIBLE);




        //-------------------list 불러오기-----------------------

        myList.clear();
        databaseReference.child("blelist").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onDataChange: "+dataSnapshot.getValue(MyBleList.class));
                //Log.d(TAG, "onDataChange: "+ dataSnapshot.getValue().getClass());

                myBleList = dataSnapshot.getValue(MyBleList.class);
                //myBleLists.add(myBleList);


               // for(int i = 0; i<myBleLists.size(); i++){
                    Log.d(TAG, "onDataChange: "+myBleList.getUserid());
                //}

                if (myBleList.getUserid().equals(userID)) {
                  Log.d(TAG, "onDataChange: "+myBleList.getMac());

                    myList.add(new MyList(myBleList.getId() ,myBleList.getItem(),
                            myBleList.getMac(),Integer.parseInt(myBleList.getIcon()),
                            "연결 끊김",
                            R.drawable.signal_0,
                            R.drawable.siran_1,
                            R.drawable.serch_1,
                            R.drawable.set_1,
                            R.drawable.con_1));
                    mRecyclerView.setAdapter(mAdapter);
                    update2();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for LOLLIPOP and newer versions
            System.out.println("Hello Marshmallow (마시멜로우)");
            int permissionResult = getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                //요청한 권한( WRITE_EXTERNAL_STORAGE )이 없을 때..거부일때...
                        *//* 사용자가 WRITE_EXTERNAL_STORAGE 권한을 한번이라도 거부한 적이 있는 지 조사한다.
         * 거부한 이력이 한번이라도 있다면, true를 리턴한다.
         *//*
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("단말기의 파일쓰기 권한이 필요합니다.\\n계속하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {

                                        System.out.println("감사합니다. 권한을 허락했네요 (마시멜로우)");
                              *//*          requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);*//*
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            onRequestPermissionsResult(,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                                        }
                                    }

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),"권한 요청 취소", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();

                    //최초로 권한을 요청할 때.
                } else {
                    System.out.println("최초로 권한을 요청할 때. (마시멜로우)");
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    //        getThumbInfo();
                }
            }else{
                //권한이 있을 때.
                //       getThumbInfo();
            }

        } else {
            System.out.println("(마시멜로우 이하 버전입니다.)");
            //   getThumbInfo();
        }*/

       /* myblelist=findViewById(R.id.myblelist);
        myList= new ArrayList<>();
        View.OnClickListener event = new View.OnClickListener() {@Override public void onClick(View v) { }};
        myAdapter= new MyAdapter(this, R.layout.my_list,myList,event);

        myList.add(new MyList("블루투스","MAC"));

        myblelist.setAdapter(myAdapter);

        myblelist.setVisibility(View.INVISIBLE);*/

        //블루투스 활성화 확인, 위치권한
        bleable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    //BluetoothAdapter 얻기
                    btManager = (BluetoothManager)mContext.getSystemService(Context.BLUETOOTH_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        btAdapter = btManager.getAdapter();
                    }

                    //블루투스 활성화
                    //isEnabled()가 false면 비활성화 이다.
                    if (btAdapter != null && !btAdapter.isEnabled()) {

                        startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 마시멜로우 버전과 같거나 이상이라면
                        if (mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                                Toast.makeText(getActivity(), "퍼미션 권한 확인중", Toast.LENGTH_SHORT).show();
                            }

                            requestPermissions(new String[]
                                            {Manifest.permission.ACCESS_COARSE_LOCATION},
                                    1);  //마지막 인자는 체크해야될 권한 갯수

                        } else {
                            //Toast.makeText(this, "권한 승인되었음", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // Make sure we have access coarse location enabled, if not, prompt the user to enable it
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("This app needs location access");
                            builder.setMessage("Please grant location access so this app can detect peripherals.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                                    }


                                }
                            });
                            builder.show();
                        }
                    }

                    mRecyclerView.setVisibility(View.VISIBLE);
                    addbtn.setEnabled(true);
                }
                else{
                    btAdapter.disable();
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    addbtn.setEnabled(false);
                }
            }
        });



        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode!= Activity.RESULT_OK){
                    bleable.setChecked(false);
                }
                break;
        }
    }


    public static void update(AttributeList attribute){
        /*myList.add(new MyList(attribute.getId() ,attribute.getItem(),
                attribute.getMAC(),attribute.getIcon(),
                "연결 끊김",
                R.drawable.signal_0,
                R.drawable.siran_1,
                R.drawable.serch_1,
                R.drawable.set_1,
                R.drawable.con_1));
        mRecyclerView.setAdapter(mAdapter);*/
        pushlist(attribute,1);
        //update2();
    }

    public static void update2(){
        prevent_flag.add(0);
        con_rssi.add(0);
        handlers0.add(null);
        handlers.add(null);
        bluetoothGatts.add(null);
        characs.add(null);
        conflag.add(0);
       // nb.add(null);
       // nm.add(null);
    }

    public static void remove(int i){
        prevent_flag.remove(i);
        con_rssi.remove(i);
        handlers0.remove(i);
        handlers.remove(i);
        bluetoothGatts.remove(i);
        characs.remove(i);
        conflag.remove(i);
        //nb.remove(i);
       // nm.remove(i);
    }

    //-------------------------------------------------


    public  void deviceconnect(final int position){


        this.position = position;
        btScanner = btAdapter.getBluetoothLeScanner();
        Toast.makeText(getActivity(),"Started Connecting...",Toast.LENGTH_SHORT).show();


        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {

                btScanner.startScan(leScanCallback);

            }

        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                final int p = position;
                stopScanning();

                if(bluetoothGatts.get(p)==null) {
                    Toast.makeText(getActivity(),"센서를 찾을 수 없습니다.",Toast.LENGTH_SHORT).show();
                    asyncDialog.cancel();
                }

            }
        }, SCAN_PERIOD);
    }

    public void stopScanning() {

        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);

            }
        });
    }

    private ScanCallback leScanCallback = new ScanCallback() {



        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

         /*  if(result.getDevice().getName().equals("HMSoft")){

            }*/
            if(result.getDevice().getAddress().equals(myList.get(position).getMAC()) && conflag.get(position)==0) {
                Log.d(TAG, "onScanResult: ");
                devicesDiscovered = result.getDevice();
                bluetoothGatts.set(position,devicesDiscovered.connectGatt(getActivity(), false, btleGattCallback));
                conflag.set(position,1);
                return;
            }
        }
    };


    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // this will get called anytime you perform a read or write characteristic operation
            //속성(characteristic)에 read or write 수행 할때 마다 call됨
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(),"device read or wrote to",Toast.LENGTH_SHORT).show();
                    final byte[] data2 = characteristic.getValue();
                    String bytesasstring = new String(data2, StandardCharsets.UTF_8);

                    Log.d(TAG, "run: "+bytesasstring);

                        if (receveflag == 0 ) {
                            receveflag = 1;
                            if (recevehandler == null) {
                                recevehandler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {

                                        super.handleMessage(msg);
                                        recevehandler.sendEmptyMessageDelayed(999, 500);
                                        ((MainActivity) mContext).vibrator();
                                        pool.play(soundid, 1, 1, 0, 0, 1);
                                    }
                                };
                                recevehandler.sendEmptyMessage(999);
                            }

                        } else {
                            receveflag = 0;
                            if (recevehandler != null) {
                                recevehandler.removeMessages(999);
                                recevehandler = null;
                            }
                        }

                        Log.d(TAG, characteristic + " " + bytesasstring);

                }
            });
        }

        //연결 상태가 달라지면 call 됨
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            // this will get called when a device connects or disconnects
            System.out.println(newState);
            switch (newState) {
                case 0:
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),"device disconnected",Toast.LENGTH_SHORT).show();
                            asyncDialog.cancel();
                            if (recevehandler != null) {
                                recevehandler.removeMessages(999);
                                recevehandler = null;
                            }
                            //bluetoothflag-=1;
                           /* stopprevent();
                            signal_stop();
                            myList.get(i).setSignal_state(R.drawable.signal_0);
                            myList.get(i).setCon(R.drawable.con_1);
                            myList.get(i).setCon_state("연결 끊김");
                            myList.get(i).setSiran(R.drawable.siran_1);
                            mRecyclerView.setAdapter(mAdapter);*/

                           for(int i =0; i<myList.size(); i++) {
                                if(gatt.getDevice().getAddress().equals(myList.get(i).getMAC())){
                                    Log.d(TAG, "disconnect: "+i);
                                    stopprevent(i);
                                    signal_stop(i);

                                    conflag.set(i,0);
                                    myList.get(i).setSignal_state(R.drawable.signal_0);
                                    myList.get(i).setCon(R.drawable.con_1);
                                    myList.get(i).setCon_state("연결 끊김");
                                    myList.get(i).setSiran(R.drawable.siran_1);
                                    prevent_flag.set(i,0);
                                    bluetoothGatts.set(i,null);
                                    mRecyclerView.setAdapter(mAdapter);

                                    ((MainActivity)mContext).discon_notification(i,myList.get(i).getIcon()
                                            ,myList.get(i).getName()
                                            ,myList.get(i).getItem());
                                   // remove(i);
                                }

                            }
                        }
                    });

                    break;
                case 2:
                    getActivity().runOnUiThread(new Runnable() {
                        @SuppressLint("HandlerLeak")
                        public void run() {
                            Toast.makeText(getActivity(),"device connected",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "device connected");
                            //Toast.makeText(MainActivity.this,"device connected\n",Toast.LENGTH_SHORT).show();
                            conflag.set(position,1);
                            asyncDialog.cancel();
                            //----------------------------------------------
                           // bluetoothflag+=1;
                            //Collections.swap(myList, bluetoothflag, position);
                            //mRecyclerView.setAdapter(mAdapter);
                            myList.get(position).setCon(R.drawable.con_2);
                            myList.get(position).setCon_state("신호강함");
                            myList.get(position).setSignal_state(R.drawable.signal_3);
                            mRecyclerView.setAdapter(mAdapter);
                            //con_flag = 1;
                            Log.d(TAG, "device connected 1");
                            //Log.d(TAG, "bluetoothflag "+bluetoothflag);

                            gatt.discoverServices();
                            signal_state (gatt,position);

                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                            alert_confirm.setMessage("연결되었습니다. 도난방지를 시작하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                                startprevent(position);
                                                //prevent_flag.add(0);
                                                prevent_flag.set(position,1);
                                                myList.get(position).setSiran(R.drawable.siran_2);
                                                mRecyclerView.setAdapter(mAdapter);

                                        }
                                    }).setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 'No'
                                            return;
                                        }
                                    });
                            AlertDialog alert = alert_confirm.create();
                            alert.show();


                        }
                    });

                    // discover services and characteristics for this device


                    break;
                default:
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),"we encounterned an unkonwn state, uh oh",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            Log.d(TAG, "device services have been discovered:1 ");
            // this will get called after the client initiates a BluetoothGatt.discoverServices() call
            //BluetoothGatt.discoverServices()을 호출하면 call됨
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.d(TAG, "device services have been discovered:2 ");
                    Toast.makeText(getActivity(),"device services have been discovered",Toast.LENGTH_SHORT).show();
                    // textView.setText(textView.getText()+"\n"+"device services have been discovere");
                }
            });
            displayGattServices(gatt,gatt.getServices());
        }



        @Override
        // Result of a characteristic read operation
        //characteristic이 read 되면 call됨
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        int i=0;
        int a =0;
        int tx;

        boolean average_flag = true;
        int p;
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            //Toast.makeText(MainActivity.this,"rssi : "+rssi,Toast.LENGTH_SHORT).show();
            // textView.setText("");
            Log.d(TAG, "onReadRemoteRssi: ");
            for(int i=0; i<myList.size();i++){
                if(gatt.getDevice().getAddress().equals(myList.get(i).getMAC())) {
                   p=i;
                    Log.d(TAG, "onReadRemoteRssi: "+p);
                }
            }

            //Log.d(TAG, "rssi"+rssi);



            con_rssi.set(p, rssi);
           // Log.d(TAG, "2: "+con_rssi.get(p));

            if (prevent_flag.get(p) == 1) {

                Log.d(TAG, i+" "+rssi+" "+tx);
                i++;
                if (i == 1) {
                    tx = rssi;
                }
                //--------------------------------------------------------
                if (average_flag == true) {
                    average_array[a] = rssi;
                    a++;
                    if (a == 9) {
                        a = 0;
                        for (int i = 0; i < average_array.length; i++) {
                            average += average_array[i];
                        }
                        average = average / 10;
                        average_flag = false;
                    }
                }
                //-----------------------------------------------------
                else {
                    // if(average+1>rssi&&average-1<rssi){
                    average_array[a] = rssi;
                    a++;
                    // }
                    if (a == 9) {
                        average = 0;
                        a = 0;
                        for (int i = 0; i < average_array.length; i++) {
                            average += average_array[i];
                        }
                        average = average / 10;
                    }
                }
                Log.d(TAG, "" + getDistance(average, tx) + " " + average);
                //state.setText(average + " " + distance);

               if(-average>75){
                   ((MainActivity) mContext).vibrator();
                   pool.play(soundid, 1, 1, 0, 0, 1);
                   /*notification(p,myList.get(p).getIcon()
                           ,myList.get(p).getName()
                           ,myList.get(p).getItem());*/
                   ((MainActivity)mContext).notification(p,myList.get(p).getIcon()
                           ,myList.get(p).getName()
                           ,myList.get(p).getItem());
               }

               /*if(-average>75 && (noti_flag.get(p)==0)){
                   noti_flag.set(p,1);
                   ((MainActivity)mContext).notification(myList.get(p).getIcon()
                                                        ,myList.get(p).getName()
                                                        ,myList.get(p).getItem());

               }
*/
            }
        }


    };

    private void displayGattServices(final BluetoothGatt gatt,List<BluetoothGattService> gattServices) {
        Log.d(TAG, "displayGattServices: ");
        if (gattServices == null) return;
        Log.d(TAG, "displayGattServices 2: ");
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            final String uuid = gattService.getUuid().toString();
            System.out.println("Service discovered: " + uuid);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                  //  Toast.makeText(mContext,"service disovered\n"+uuid,Toast.LENGTH_SHORT).show();

                }
            });
            new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic :
                    gattCharacteristics) {

                Ischecked(gatt,gattCharacteristic);

                final String charUuid = gattCharacteristic.getUuid().toString();
                Log.d(TAG, "Characteristic discovered for service: " + charUuid);
                System.out.println("Characteristic discovered for service: " + charUuid);

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                       // Toast.makeText(mContext,"characteristic discovered for service\n"+charUuid,Toast.LENGTH_SHORT).show();
                        //textView.setText(textView.getText()+"\n"+"characteristic discovered for service "+charUuid);
                    }
                });

            }
        }


    }

    public void Ischecked(final BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {
        Log.d(TAG, "Ischecked: ");
        int properties = characteristic.getProperties();
        int permission = characteristic.getPermissions();

        if(properties == 22){
            //charac = characteristic;
            characs.set(position,characteristic);
            gatt.setCharacteristicNotification(characteristic,true);

            Log.d(TAG, characteristic + " " + properties + " " + permission);
            //알림을 받으려면 특성 알림을 true로 설정

        }

    }


    public void writeCharacteristic(String data,int position) {
        BluetoothGatt bluetoothGatt = bluetoothGatts.get(position);
        BluetoothGattCharacteristic c = characs.get(position);
        if (btAdapter == null || bluetoothGatt == null || c == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }


        Log.i(TAG, "characteristic " + characs.get(position).toString());

        try {
            Log.i(TAG, "data " + URLEncoder.encode(data, "utf-8"));

            characs.get(position).setValue(URLEncoder.encode(data, "utf-8"));

            // TODO
            bluetoothGatts.get(position).writeCharacteristic(characs.get(position));


        } catch (Exception e) {
            e.printStackTrace();
        }
        final byte[] data2 = characs.get(position).getValue();
        String bytesasstring = new String(data2, StandardCharsets.UTF_8);

        Log.d(TAG, "writeCharacteristic: "+ bytesasstring);

    }


    double getDistance(double rssi, int txPower){
        //RSSI = TxPower - 10 * n * lg(d)
        // n is usually 2 or 4 in free space
        //d = 10 ^ ((TxPower - RSSI) / (10 * n))
        distance = (Math.pow(10,((double) txPower-rssi)/(10*4)));
        return (Math.pow(10,((double) txPower-rssi)/(10*4)));
    }

    public void devicedisconnect(int position) {
        Log.d(TAG, "btn devicedisconnect: ");
        if(bluetoothGatts.get(position)!=null) {
            bluetoothGatts.get(position).disconnect();
            //bluetoothGatts.set(position,null);
            //bluetoothGatts.remove(position);
            //Log.d(TAG, "devicedisconnect: ");
            /*myList.get(position).setCon(R.drawable.con_1);
            myList.get(position).setSignal_state(R.drawable.signal_0);
            myList.get(position).setCon_state("연결 끊김");
            mRecyclerView.setAdapter(mAdapter);*/
        }
    }

    public void startprevent (final int i) {

        if (handlers.get(i) == null) {
            handlers.set(i,new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    super.handleMessage(msg);
                   
                    handlers.get(i).sendEmptyMessageDelayed(10+i, 500);
                    bluetoothGatts.get(i).readRemoteRssi();
                }
            });
            handlers.get(i).sendEmptyMessage(10+i);
        }



    }


    public void stopprevent(int i){
        Log.d(TAG, "stopprevent: ");
        if(handlers.get(i)!=null){
            handlers.get(i).removeMessages(10+i);
            handlers.set(i,null);
        }
        Log.d(TAG, "stop: ");

        //myList.get(c).setSiran(R.drawable.siran_1);
       // mRecyclerView.setAdapter(mAdapter);
    }

    public void con_rssi(int i){
        Log.d(TAG, "con_rssi: "+i);
        if(con_rssi.get(i)>-60){
            myList.get(i).setSignal_state(R.drawable.signal_3);
            myList.get(i).setCon_state("가까이 있음");
        }else if(con_rssi.get(i)>-70){
            myList.get(i).setSignal_state(R.drawable.signal_2);
            myList.get(i).setCon_state("멀어짐");
        }else{
            myList.get(i).setSignal_state(R.drawable.signal_1);
            myList.get(i).setCon_state("멀리 있음");
        }
        mRecyclerView.setAdapter(mAdapter);
        //Log.d(TAG, ""+con_rssi);

    }


   public void prevent_flag(int position,int flag){
        prevent_flag.set(position,flag);
    }


    public void signal_state (final BluetoothGatt gatt,final int i) {
        //signal_position = i;
       //Log.d(TAG, "signal_state: "+signal_position);
      // Handler h = null;
       // handlers0.add(h);
       // Log.d(TAG, "signal_state: 3"+handlers0.get(signal_position));
       // if (handlers0.get(signal_position) == null) {
        //con_rssi.add(0);
        Log.d(TAG, "signal_state 1: ");
           if(handlers0.get(i) == null){
            handlers0.set(i,new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    super.handleMessage(msg);
                    handlers0.get(i).sendEmptyMessageDelayed(i, 500);

                    gatt.readRemoteRssi();
                    //readRssiFlag = bluetoothGatts.get(signal_position).readRemoteRssi();
                    //Log.d(TAG, "handleMessage: "+con_rssi.get(signal_position));
                    con_rssi(i);
                }
            });

            handlers0.get(i).sendEmptyMessage(i);
       // }
           }

        Log.d(TAG, "signal_state 2: ");

    }

    public void signal_stop (int i) {
        if(handlers0.get(i) != null){
            handlers0.get(i).removeMessages(i);
            handlers0.set(i,null);
        }
    }

    public void progressdialog(){
        asyncDialog = new ProgressDialog(mContext);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //asyncDialog.setCancelable(false);
        asyncDialog.setMessage("로딩중입니다..");
        asyncDialog.show();
    }

    int deleteflag = 0;
    public void deleteitem(final int position, final int flag){
        //this.position = position;
        deleteflag = flag;

        databaseReference.child("blelist").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                myBleList = dataSnapshot.getValue(MyBleList.class);

//                Log.d("WWW", key);
               //Log.d(TAG, "onChildAdded: "+myList.get(deleteposition)+" "+deleteposition);
             if (myList.get(position).getName().equals(myBleList.getId()) &&
                        myList.get(position).getMAC().equals(myBleList.getMac()) &&
                        myList.get(position).getItem().equals(myBleList.getItem())){
                    key = dataSnapshot.getKey();
                    if(deleteflag==1) {
                        databaseReference.child("/blelist/" + key).removeValue();

                        myList.remove(position);
                        remove(position);
                        mRecyclerView.setAdapter(mAdapter);
                        deleteflag = 0;
                    }
                 Log.d(TAG, "onChildAdded: "+key);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //myList.remove(position);
        //remove(position);


       // mRecyclerView.setAdapter(mAdapter);
    }

    public void details(final int position){

        databaseReference.child("blelist").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myBleList = dataSnapshot.getValue(MyBleList.class);

//                Log.d("WWW", key);
                if (myList.get(position).getName().equals(myBleList.getId()) &&
                        myList.get(position).getMAC().equals(myBleList.getMac()) &&
                        myList.get(position).getItem().equals(myBleList.getItem())){
                    key = dataSnapshot.getKey();

                    Log.d(TAG, "onChildAdded: "+key);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Intent intent = new Intent(mContext, Details.class);

        intent.putExtra("set",myList.get(position));
        intent.putExtra("p",position);
        startActivity(intent);
    }

   public static void name_details(int position, String name){
       Map<String,Object> taskMap = new HashMap<>();
       taskMap.put(key+"/id",name);
       databaseReference.child("blelist").updateChildren(taskMap);
       myList.get(position).setName(name);
       mRecyclerView.setAdapter(mAdapter);
    }
    public static void item_details(int position, String item, int icon){
        Map<String,Object> taskMap2 = new HashMap<>();
        taskMap2.put(key+"/item",item);
        taskMap2.put(key+"/icon",icon+"");
        databaseReference.child("blelist").updateChildren(taskMap2);
        myList.get(position).setItem(item);
        myList.get(position).setIcon(icon);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void disconnectAll() {
        for (int i = 0; i < bluetoothGatts.size(); i++) {
            //Log.d(TAG, "btn devicedisconnect: ");
            if (bluetoothGatts.get(i) != null) {
                bluetoothGatts.get(i).disconnect();
                stopprevent(i);
                signal_stop(i);

                myList.get(i).setSignal_state(R.drawable.signal_0);
                myList.get(i).setCon(R.drawable.con_1);
                myList.get(i).setCon_state("연결 끊김");
                myList.get(i).setSiran(R.drawable.siran_1);
                prevent_flag.set(i,0);
                bluetoothGatts.set(i,null);
            }
        }
    }

   /* public void notification(int p,int icon , String name , String item){

        nb.set(p,new NotificationCompat.Builder(mContext));
        Bitmap largeIconForNoti =
                BitmapFactory.decodeResource(getResources(),icon);
        nb.get(p).setSmallIcon(icon)
                .setContentText(name+" ("+item+") "+"이 핸드폰과 멀어졌습니다.")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLargeIcon(largeIconForNoti)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC);
        nm.set(p,(NotificationManager)mContext.getSystemService(mContext.NOTIFICATION_SERVICE));
        nm.get(p).notify(p,nb.get(p).build());

        ((MainActivity)mContext).wakelock();
    }*/

   public int bleflag(){

       for (int i =0; i<bluetoothGatts.size(); i++){
           if(bluetoothGatts.get(i)!=null){
               return 1;
           }
       }
       return 0;
   }

   int pushflag = 0;
   public static void pushlist(AttributeList attribute, final int flage){
       int pushflag = flage;
       if(pushflag == 1) {
           databaseReference.child("blelist").push().setValue(new MyBleList(userID, attribute.getId(), attribute.getItem() + "",
                   attribute.getMAC(), attribute.getIcon() + ""));
            pushflag = 0;
       }
   }

}
