package com.example.admin.tripapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.tripapplication.BLU.AttributeList;
import com.example.admin.tripapplication.BLU.Step;
import com.example.admin.tripapplication.fragment.BleFragment;
import com.example.admin.tripapplication.fragment.FirstFragment;
import com.example.admin.tripapplication.fragment.RecyclerViewFragment;

import com.example.admin.tripapplication.login.User;
import com.example.admin.tripapplication.sns.LikeData;
import com.example.admin.tripapplication.sns.MyProActivity;
import com.example.admin.tripapplication.sns.SnsData;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.admin.tripapplication.fragment.BleFragment.newInstance;

public class MainActivity extends DrawerActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    User upUser;
    User user;
    User initUser;
    ArrayList<User> userList = new ArrayList<>();
    LikeData likeData;
    ArrayList<LikeData> LikeArray = new ArrayList<>();
    boolean getImage = false;
    private static final int SELECT_PICTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private String selectedImagePath;

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;
    SharedPreferences auto;
    SharedPreferences.Editor editor;
    CircleImageView proView;
    TextView idInpo;
    RecyclerViewFragment fragment;
    ArrayList<SnsData> snsDataArrayList = new ArrayList<>();
    Handler handler = new Handler();
    String key;
    SnsData snsData;
    SnsData upSnsData;
    ArrayList<String> snsKey = new ArrayList<>();
    ArrayList<User> userList2 = new ArrayList<>();
    String name;

    private static PowerManager.WakeLock sCpuWakeLock;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // 갤러리 사용권한에 대한 콜백을 받음
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 동의 버튼 선택
                } else {
                    // 사용자가 권한 동의를 안함
                    // 권한 동의안함 버튼 선택
                    Toast.makeText(MainActivity.this, "권한사용을 동의해주셔야 이용이 가능합니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
            // 예외케이스
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("bbb", "------------------------------------------------------");

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        editor = auto.edit();
        setTitle("");
        ButterKnife.bind(this);
        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        proView = findViewById(R.id.proView);

        idInpo = findViewById(R.id.idInpo);
        Button bluButton = findViewById(R.id.bluButton);
        Button LogoutButton = findViewById(R.id.logOutButton);
        Button proButton = findViewById(R.id.proButton);
        Button myBtn=findViewById(R.id.myWriteBtn);
        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MyProActivity.class);
                intent.putExtra("id", name);
                intent.putExtra("array", snsDataArrayList);
                intent.putExtra("like", LikeArray);
                intent.putExtra("users", userList2);
                startActivity(intent);
            }
        });
        proButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermissions();
                }


                // 사진 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);


            }
        });

       /* bluButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BLEMainActivity.class);
                startActivity(intent);
            }
        });*/
        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mViewPager.getViewPager().setAdapter(fragEvent);
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://cdn.crowdpic.net/list-thumb/thumb_l_1AD6BFEF978C9519229D27FD8E7AF688.jpg");
                       /* return HeaderDesign.fromColorAndDrawable(Integer.parseInt("##00ff00"), getResources().getDrawable(R.drawable.login));*/
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnUTrPmYmL-s1pP6bQUw2jlI79BYXJcdewMw6wgTKz81nS3_Ew");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://cdn.crowdpic.net/list-thumb/thumb_l_E821CC94D4790B4AE572E2416D55DDFB.jpg");
                    /*case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");*/
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "클릭", Toast.LENGTH_SHORT).show();
            }
        });
        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    fragEvent.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 사진의 URI 경로를 받는 메소드
     */
    public String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String image_id = cursor.getString(0);
        image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;

    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                getImage = true;
                Log.d("ccc", "들왓어");
                if (getImage == true) {
                    Log.d("ccc", "들왓어?(");
                    loadHtml(fileUpload(selectedImagePath));
                }
            }
        }

        if (requestCode== 5555) {
            if (resultCode == 501) {
                Log.d("Tiger", "onActivityResult: ");
                AttributeList attribute = (AttributeList) data.getExtras().getSerializable("attribute");
                // MAC.add(attribute.getMAC());

                BleFragment.update(attribute);
            }
        }*/

        switch (requestCode){
            case 5555:
                if (resultCode == 501) {
                    Log.d("Tiger", "onActivityResult: ");
                    AttributeList attribute = (AttributeList) data.getExtras().getSerializable("attribute");
                    // MAC.add(attribute.getMAC());

                    BleFragment.update(attribute);
                }
                break;
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {

                        Uri selectedImageUri = data.getData();
                        selectedImagePath = getPath(selectedImageUri);
                        getImage = true;
                        Log.d("ccc", "들왓어");
                        if (getImage == true) {
                            Log.d("ccc", "들왓어?(");
                            loadHtml(fileUpload(selectedImagePath));
                        }

                }
                break;
        }
    }

    FragmentStatePagerAdapter fragEvent = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            switch (position % 3) {
                case 0:
                    return FirstFragment.newInstance();
                case 1:
                    fragment = new RecyclerViewFragment();
                    Bundle bundle = new Bundle();
                    Intent intent = getIntent();
                    name = intent.getStringExtra("inputID");
                    idInpo.setText(name);
                    snsDataArrayList = (ArrayList<SnsData>) intent.getSerializableExtra("array");
                    LikeArray = (ArrayList<LikeData>) intent.getSerializableExtra("like");
                    Log.d("QW", snsDataArrayList.size() + "");
                    userList2=(ArrayList<User>)intent.getSerializableExtra("users");
                    initUser=(User)intent.getSerializableExtra("user");
                    if (!initUser.getUserImage().equals("")) {
                        Picasso.get().load(initUser.getUserImage().toString()).into(proView);
                        Log.d("WWW", initUser.getUserImage().toString() + "gggg");

                    } else {
                        Log.d("WWW", initUser.getUserImage().toString() + "ㅋㅋㅋ");
                        proView.setImageDrawable(getResources().getDrawable(R.drawable.proimage));
                    }
                    bundle.putString("id", name);
                    bundle.putSerializable("array", snsDataArrayList);
                    bundle.putSerializable("like", LikeArray);
                    bundle.putSerializable("users", userList2);
                    fragment.setArguments(bundle);
                    Toast.makeText(MainActivity.this, "인스턴스", Toast.LENGTH_SHORT).show();
                    return fragment;
                //return RecyclerViewFragment.newInstance();
                case 2:
                    return newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        ;

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position % 3) {
                case 0:
                    return "홈";
                case 1:
                    return "여행노트";
                case 2:
                    return "블루투스";
            }
            return "";
        }
    };

    public void loginState() {
        Log.d("PPP", "자동로그인");
        if (!auto.getString("inputID", "").equals("")) {

            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getUserID().equals(auto.getString("inputID", ""))) {
                    user = userList.get(i);
                    Log.d("PPP", user.getUserID().toString() + "222");
                    Log.d("PPP", user.getUserImage().toString() + "333");
//                    Toast.makeText(this, user.getUserID()+"자동", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            Intent intent = getIntent();
            String inputID = intent.getStringExtra("inputID");
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getUserID().equals(inputID)) {
                    user = userList.get(i);
                    break;
                }
            }

        } else {
            Intent intent = getIntent();
            user = (User) intent.getSerializableExtra("user");
        }

        Log.d("PPP", user.getUserImage().toString() + "");
        Log.d("PPP", user.getUserImage().toString() + "");
        if (!user.getUserImage().equals("")) {
            Picasso.get().load(user.getUserImage().toString()).into(proView);
            Log.d("WWW", user.getUserImage().toString() + "gggg");

        } else {
            Log.d("WWW", user.getUserImage().toString() + "ㅋㅋㅋ");
            proView.setImageDrawable(getResources().getDrawable(R.drawable.proimage));
        }
        idInpo.setText(user.getUserID());
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDatabase();
//        snsinitDatabase();
    }

    int count = 0;
    int count1=0;
    private void initDatabase() {
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                user = dataSnapshot.getValue(User.class);
                userList.add(user);
//                loginState(); //자동로그인 상태
                String initkey=dataSnapshot.getKey();
                if (user.getUserID().equals(name)) {
                    if (count == 0) {
                        Log.d("PPP", user.getUserID().toString() + "@@@");
                        key = initkey;

                        upUser = dataSnapshot.getValue(User.class);
                        Log.d("PPP", upUser.getUserID() + "@@@");
                        Log.d("PPP", key + "@@@");
                        count = 1;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

   /* private void snsinitDatabase() {
        Log.d("QQQ", "호출");
        snsDataArrayList.clear();
        snsKey.clear();
        databaseReference.child("snsDB").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                snsData = dataSnapshot.getValue(SnsData.class);
                if (snsData.getId().equals(user.getUserID())) {
                    if(count1==0) {
                        snsDataArrayList.add(snsData);
                        snsKey.add(dataSnapshot.getKey());
                        count=1;
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    public String fileUpload(String image) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        String fileName = null;
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://storage-bc8f5.appspot.com/");
        StorageReference storageReference = storage.getReference();

        fileName = image.substring(image.lastIndexOf("/"));
        StorageReference spaceRef = storageReference.child(user.getUserID() + "" + fileName);
        Log.d("AAA", "images" + fileName);
        image = image.substring(1);
        Log.d("AAA", image);
        Uri file = Uri.fromFile(new File(image));
        Log.d("AAA", file + "");


        UploadTask uploadTask = spaceRef.putFile(file);
        // 파일 업로드의 성공/실패에 대한 콜백 받아 핸들링 하기 위해 아래와 같이 작성한다
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(MainActivity.this, "안올라갓어", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "올라갓어", Toast.LENGTH_SHORT).show();
            }
        });
        fileName = fileName.replace("/", "");
        return fileName;
    }

    String path;

    void loadHtml(final String name) { // 웹에서 html 읽어오기
        Log.d("DDD", name + "파일");
        String result;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuffer sb = new StringBuffer();
                path = "https://firebasestorage.googleapis.com/v0/b/storage-bc8f5.appspot.com/o/" + user.getUserID() + "%2F" + name;
                Log.d("FFF", user.getUserID());
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn =
                            (HttpURLConnection) url.openConnection();// 접속
                    if (conn != null) {
                        conn.setConnectTimeout(2000);
                        conn.setUseCaches(false);
                        String line = "";
                        String input = "";
                        if (conn.getResponseCode()
                                == HttpURLConnection.HTTP_OK) {
                            //    데이터 읽기
                            BufferedReader br
                                    = new BufferedReader(new InputStreamReader
                                    (conn.getInputStream(), "utf-8"));//"utf-8"
                            while (true) {
                                line = br.readLine();
                                if (line == null) break;
                                // sb.append(line+"\n");
                                input += line;
                            }
                            Log.d("FFF", input);
                            JSONObject jsonObject = new JSONObject(input);

                            sb.append(jsonObject.getString("downloadTokens"));
                            br.close(); // 스트림 해제
                        }
                        conn.disconnect(); // 연결 끊기
                    }
                    // 값을 출력하기
                    Log.d("DDD", sb.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            textView.setText(sb.toString());
                            path = "https://firebasestorage.googleapis.com/v0/b/storage-bc8f5.appspot.com/o/" + user.getUserID() + "%2F" + name + "?alt=media&token=" + sb.toString();
                            Log.d("EEE", path);
                            upUser.setUserImage(path);
                            Map<String, Object> imageUpdates = new HashMap<>();
                            imageUpdates.put(key, upUser);
                            Log.d("FFF", upUser.getUserID()+"@@@####");
                            databaseReference.child("users").updateChildren(imageUpdates);
                            /*Map<String, Object> listUpdates = new HashMap<>();
                            for (int i=0; i<snsDataArrayList.size(); i++){
                                snsDataArrayList.get(i).setProimage(path);

                                Log.d("CCC", i+"");
                                Log.d("CCC", snsKey.get(i).toString());

                                listUpdates.put(snsKey.get(i).toString(), snsDataArrayList.get(i));
                                databaseReference.child("snsDB").updateChildren(listUpdates);
                            }*/

                            //디비 업데이트 부분+
                            Picasso.get().load(upUser.getUserImage().toString()).into(proView);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작4
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mViewPager.notifyHeaderChanged();
    }


    public void vibrator(){
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }


    public void addlist(View view) {
        //Intent intent = new Intent(this,Step.class);
        Intent intent = new Intent(this,Step.class);
        //startActivity(intent);
        startActivityForResult(intent,5555);
    }

    public void notification(int p, int icon , String name , String item){
        NotificationCompat.Builder nb;
        nb = new NotificationCompat.Builder(this);
        Bitmap largeIconForNoti =
                BitmapFactory.decodeResource(getResources(),icon);
        nb.setSmallIcon(icon)
                .setContentText(name+" ("+item+") "+"이 핸드폰과 멀어졌습니다.")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLargeIcon(largeIconForNoti)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(p,nb.build());

        wakelock();
    }


    //잠금화면 깨우기
    public void wakelock(){

        if (sCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "hi");

        sCpuWakeLock.acquire();


        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }


    }
}
