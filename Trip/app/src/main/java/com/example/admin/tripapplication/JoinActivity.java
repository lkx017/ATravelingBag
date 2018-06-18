package com.example.admin.tripapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.tripapplication.logger.Log;
import com.example.admin.tripapplication.login.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JoinActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    EditText editID;
    EditText editPW;
    EditText editPW2;
    Button LoginBtn;
    TextView textID;
    TextView textPW;
    boolean stateID = false, statePW = false;
    User user;
    ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        editID = findViewById(R.id.editID);
        editPW = findViewById(R.id.editPW);
        editPW2 = findViewById(R.id.editPW2);
        LoginBtn = findViewById(R.id.LoginBtn);
        textID = findViewById(R.id.textID);
        textPW = findViewById(R.id.textPW);



        editID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getUserID().equals(s.toString())) {
                        textID.setText("아이디가 중복됩니다.");
                        textID.setTextColor(Color.RED);
                        stateID = false;
                        break;
                    } else {
                        textID.setText("사용가능한 아이디입니다..");
                        textID.setTextColor(Color.GREEN);
                        stateID = true;
                    }
                }


            }
        });
        editPW.addTextChangedListener(tevent);
        editPW2.addTextChangedListener(tevent);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statePW==true&&stateID==true) {
                    regiserUser(editID.getText().toString(), editPW.getText().toString());
                }
                else{
                    Toast.makeText(JoinActivity.this, "아이디 및 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    TextWatcher tevent=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!(editPW.getText().toString().equals(editPW2.getText().toString()))) {
                textPW.setText("패스워드가 다릅니다.");
                textPW.setTextColor(Color.RED);
                statePW = false;
            }
            if(editPW.getText().toString().equals(editPW2.getText().toString())){
                textPW.setText("패스워드가 일치합니다.");
                textPW.setTextColor(Color.GREEN);
                statePW = true;
            }

        }
    };

    public void regiserUser(String email, String password) {
        User a= new User(email, password, "");
        databaseReference.child("users").push().setValue(a);
        Toast.makeText(JoinActivity.this, "회원가입완료.", Toast.LENGTH_SHORT).show();

    }

}
