package com.example.admin.tripapplication.sns;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.admin.tripapplication.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {
    String userID;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    SnsData snsData;
    ArrayList<SnsData> snsList = new ArrayList<>();
    EditText titleEdit;
    EditText textEdit;
    Button imageBtn;
    Button writeBtn;
    String formatDate;
    ImageView imageView;
    ToggleButton anonymousBtn;
    boolean getImage=false;
    Handler handler = new Handler();
    private static final int SELECT_PICTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private String selectedImagePath;
    int anonymous=0;

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
                    Toast.makeText(WriteActivity.this , "권한사용을 동의해주셔야 이용이 가능합니다." , Toast.LENGTH_LONG ).show();
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
        setContentView(R.layout.activity_write);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        titleEdit = findViewById(R.id.titleEdit);
        textEdit = findViewById(R.id.textEdit);
        imageBtn = findViewById(R.id.imageBtn);
        writeBtn = findViewById(R.id.writeBtn);
        imageView=findViewById(R.id.loadImage);
        anonymousBtn=findViewById(R.id.anonymousBtn);
        final Date date = new Date();
        anonymousBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    anonymous=1;
                }
                else{
                    anonymous=0;
                }
            }
        });
        imageBtn.setOnClickListener(new View.OnClickListener() {
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

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재시간을 msec 으로 구한다.
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 E요일 HH시mm분");
                // nowDate 변수에 값을 저장한다.
                formatDate = sdfNow.format(date);
                Log.d("DDD", formatDate);


                if (!titleEdit.getText().toString().equals("") && !textEdit.getText().toString().equals("")&&getImage==true) {
                    Log.d("RRR", fileUpload(selectedImagePath));
                    loadHtml(fileUpload(selectedImagePath));

//                    setLikeData(userID, titleEdit.getText().toString(), formatDate, "", "");
                }
                else if(!titleEdit.getText().toString().equals("") && !textEdit.getText().toString().equals("")&&getImage==false){
                    setSnsDB(userID, formatDate, titleEdit.getText().toString(), textEdit.getText().toString(), "", anonymous);
//                    setLikeData(userID, titleEdit.getText().toString(), formatDate, "", "");
                }
                else {
                    Toast.makeText(WriteActivity.this, "제목 및 내용을 적어주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void checkPermissions(){

        if (ContextCompat.checkSelfPermission(WriteActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(WriteActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(WriteActivity.this,
                    new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);
        }

    }

    public void setSnsDB(String id, String date, String title, String text, String image, int anonymous) {
        snsData = new SnsData(id, date, title, text, image, anonymous);
        databaseReference.child("snsDB").push().setValue(snsData);
        Toast.makeText(this, "작성완료.", Toast.LENGTH_SHORT).show();
        finish();
    }
    void setLikeData(String getID, String getTitle, String getDate, String setID, String setText){
        LikeData likeData=new LikeData(getID, getTitle, getDate, setID, setText);
        databaseReference.child("likeDB").push().setValue(likeData);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageURI(selectedImageUri);
                getImage=true;
            }
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
    public String fileUpload(String image){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        String fileName=null;
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://storage-bc8f5.appspot.com/");
        StorageReference storageReference = storage.getReference();

        fileName=image.substring(image.lastIndexOf("/"));
        StorageReference spaceRef = storageReference.child(userID+""+fileName);
        Log.d("AAA", "images"+fileName);
        image=image.substring(1);
        Log.d("AAA", image);
        Uri file = Uri.fromFile(new File(image));
        Log.d("AAA", file+"");


        UploadTask uploadTask = spaceRef.putFile(file);
        // 파일 업로드의 성공/실패에 대한 콜백 받아 핸들링 하기 위해 아래와 같이 작성한다
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(WriteActivity.this, "안올라갓어", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(WriteActivity.this, "올라갓어", Toast.LENGTH_SHORT).show();
            }
        });
        fileName=fileName.replace("/", "");
        return fileName;
    }
    String path;
    void loadHtml(final String name) { // 웹에서 html 읽어오기
        Log.d("DDD", name+"파일");
        String result;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuffer sb = new StringBuffer();
                path ="https://firebasestorage.googleapis.com/v0/b/storage-bc8f5.appspot.com/o/"+userID+"%2F"+name;
                Log.d("FFF", path);
                try {
                    URL url = new URL(path);
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
                            Log.d("FFF", input);
                            JSONObject jsonObject=new JSONObject(input);

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
                            path ="https://firebasestorage.googleapis.com/v0/b/storage-bc8f5.appspot.com/o/"+userID+"%2F"+name+"?alt=media&token="+sb.toString();
                            Log.d("EEE", path);
                            setSnsDB(userID, formatDate, titleEdit.getText().toString(), textEdit.getText().toString(), path, anonymous);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작4
    }
}
