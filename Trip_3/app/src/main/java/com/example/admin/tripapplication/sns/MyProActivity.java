package com.example.admin.tripapplication.sns;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.TestRecyclerViewAdapter;
import com.example.admin.tripapplication.login.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyProActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference();
    SnsData snsData;
    LikeData likeData;
    ArrayList<SnsData> snsDataArrayList = new ArrayList<>();
    ArrayList<LikeData> likeDataArrayList = new ArrayList<>();
    String userID;
    TestRecyclerViewAdapter adapter;
    ArrayList<SnsData> items;
    ArrayList<SnsData> temp;
    ArrayList<LikeData> templike;
    ArrayList<LikeData> LikeArray=new ArrayList<>();
    ArrayList<LikeData> likeitem=new ArrayList<>();
    ArrayList<User> userItem=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pro);
        Intent intent=getIntent();
        items = new ArrayList<>();
        userID=intent.getStringExtra("id");
        snsDataArrayList=(ArrayList<SnsData>) intent.getSerializableExtra("array");
        LikeArray=(ArrayList<LikeData>) intent.getSerializableExtra("like");
        userItem=(ArrayList<User>)intent.getSerializableExtra("users");
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        for (int i = snsDataArrayList.size() - 1; i >= 0; i--) {
            if(snsDataArrayList.get(i).getId().equals(userID)) {
                items.add(new SnsData(snsDataArrayList.get(i).getId(), snsDataArrayList.get(i).getDate(), snsDataArrayList.get(i).getTitle(), snsDataArrayList.get(i).getText(), snsDataArrayList.get(i).getImageurl(), snsDataArrayList.get(i).getAnonymous()));
            }
        }
        for(int i=LikeArray.size()-1; i>=0; i--){
            if(LikeArray.get(i).getSetID().equals(userID)) {
                likeitem.add(new LikeData(LikeArray.get(i).getGetID(), LikeArray.get(i).getGetTitle(), LikeArray.get(i).getGetDate(), LikeArray.get(i).getSetID(), LikeArray.get(i).getSetText()));
            }
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new TestRecyclerViewAdapter(items);
        adapter.setID(userID);
        adapter.setContext(getApplicationContext());
        adapter.setLike(likeitem);
        adapter.setUsers(userItem);
        boolean delBtn=true;
        adapter.setDel(delBtn);
        recyclerView.setAdapter(adapter);
    }
}
