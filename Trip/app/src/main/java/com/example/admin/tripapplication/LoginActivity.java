package com.example.admin.tripapplication;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.tripapplication.login.User;
import com.example.admin.tripapplication.sns.LikeData;
import com.example.admin.tripapplication.sns.SnsData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    SharedPreferences auto;
    EditText editID;
    EditText editPW;
    String inputID;
    String inputPW;
    CheckBox checkBox;
    User user;
    ArrayList<User> userList = new ArrayList<>();
    SnsData snsData;
    ArrayList<SnsData> snsDataArrayList = new ArrayList<>();
    Handler handler = new Handler();
    LikeData likeData;
    ArrayList<LikeData> LikeArray=new ArrayList<>();
    @Override
    protected void onStart() {

        super.onStart();
        initDatabase();
//        snsinitDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        snsinitDatabase();
        likeinitDatabase();
        Button LoginBtn = findViewById(R.id.LoginBtn);
        Button JoinBtn = findViewById(R.id.JoinBtn);
        editID = findViewById(R.id.editID);
        editPW = findViewById(R.id.editPW);
        checkBox = findViewById(R.id.autoLogin);
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        inputID = auto.getString("inputID", null);
        inputPW = auto.getString("inputPW", null);


        LoginBtn.setOnClickListener(event);
        JoinBtn.setOnClickListener(event);
        /*LoadingTask loadingTask=new LoadingTask();
        loadingTask.execute();*/
        if (inputID != null && inputPW != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(2000);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                snsinitDatabase();
                                Toast.makeText(LoginActivity.this, "로그인 중입니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("inputID", inputID);
                        intent.putExtra("users", userList);
                        intent.putExtra("array", snsDataArrayList);
                        intent.putExtra("like", LikeArray);
                        Log.d("QQQ", snsDataArrayList.size() + "보낸다.");
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    View.OnClickListener event = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.LoginBtn:
                    /*Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(in);*/
                    userLogin(editID.getText().toString(), editPW.getText().toString());
                    break;
                case R.id.JoinBtn:
                    intent = new Intent(LoginActivity.this, JoinActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void userLogin(String email, String password) {
        /*mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "이메일 혹은 비밀번호를 확인하세요.",
                                    Toast.LENGTH_SHORT).show();


                        } else {
                            if (checkBox.isChecked() == true) {
                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("inputID", email);
                                autoLogin.putString("inputPW", password);
                                //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                                autoLogin.commit();
                            }
                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(in);
                            FirebaseUser user = task.getResult().getUser();
                            String sendEmail = String.valueOf(databaseReference.child("users").child(user.getUid()));
                            Toast.makeText(LoginActivity.this, sendEmail, Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "로그인 되었습니다.",
                                    Toast.LENGTH_SHORT).show();
                            finish();

                        }

                        // ...
                    }
                });*/
        int count = 0;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserID().equals(email) &&
                    userList.get(i).getUserPassword().equals(password)) {
                user = userList.get(i);
                count = 1;
                break;
            }
        }
        if (count == 1) {
            if (checkBox.isChecked() == true) {
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("inputID", email);
                autoLogin.putString("inputPW", password);
                //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                autoLogin.commit();
            }
            Intent in = new Intent(LoginActivity.this, MainActivity.class);
            in.putExtra("user", user);
            in.putExtra("users", userList);
            in.putExtra("inputID", user.getUserID());
            Log.d("QW", snsDataArrayList.size() + "");
            in.putExtra("array", snsDataArrayList);
            in.putExtra("like", LikeArray);
            startActivity(in);
            Toast.makeText(LoginActivity.this, "로그인 되었습니다.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initDatabase() {
        Log.d("WWW", "호출");
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                user = dataSnapshot.getValue(User.class);
                userList.add(user);

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

    private void snsinitDatabase() {
        Log.d("QQQ", "호출");
        databaseReference.child("snsDB").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                snsData = dataSnapshot.getValue(SnsData.class);
                snsDataArrayList.add(snsData);
                Log.d("QQQ", "들어옴");
                Log.d("QQQ", snsDataArrayList.size() + "");
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
    private void likeinitDatabase() {
        databaseReference.child("likeDB").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                likeData = dataSnapshot.getValue(LikeData.class);
                LikeArray.add(likeData);
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

}
