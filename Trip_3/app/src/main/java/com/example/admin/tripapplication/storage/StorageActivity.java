package com.example.admin.tripapplication.storage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

//import com.example.admin.tripapplication.Manifest;
import com.example.admin.tripapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StorageActivity extends AppCompatActivity {
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://storage-bc8f5.appspot.com/");
        StorageReference storageReference = storage.getReference();
        StorageReference spaceRef = storageReference.child("images/space1.jpg");
        Uri file = Uri.fromFile(new File("/sdcard/DCIM/Camera/IMG_20180526_053949.jpg"));
        UploadTask uploadTask = spaceRef.putFile(file);
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        // 파일 업로드의 성공/실패에 대한 콜백 받아 핸들링 하기 위해 아래와 같이 작성한다
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(StorageActivity.this, "no", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(StorageActivity.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageView imageView=findViewById(R.id.imageView);
        FirebaseStorage storage1=FirebaseStorage.getInstance();
        StorageReference storageReference1=storage1.getReferenceFromUrl("gs://storage-bc8f5.appspot.com/");
        StorageReference pathReference=storageReference1.child("images/space.JPEG");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(getApplicationContext(), "다운로드 성공 : "+ uri, Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_SHORT).show();
            }
        });





    }
}
