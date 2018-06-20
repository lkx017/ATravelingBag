package com.example.admin.tripapplication.sns;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.login.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DatAdapter extends ArrayAdapter<DatData> {
    Context context;
    int resource;

    String userID;
    ArrayList<User> userList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public DatAdapter(@NonNull Context context, int resource, @NonNull List<DatData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resource, null);


        ImageView imageView = convertView.findViewById(R.id.datcircleView);
        TextView textView = convertView.findViewById(R.id.datTv);
        TextView DataView = convertView.findViewById(R.id.datTime);
        String name=getItem(position).getId().toString()+"\n"+getItem(position).getText().toString();
        textView.setText(name);
        DataView.setText(getItem(position).getDate());

        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getUserID().equals(getItem(position).getId())){
                Picasso.get().load(userList.get(i).getUserImage()).into(imageView);
            }
        }

        return convertView;
    }

    public void setID(String id) {
        userID = id;
    }
    public void setUser(ArrayList<User> arrayList){this.userList= arrayList;}
}

public class datWriteActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    String formatDate;
    DatData datData;
    SnsData snsData;
    ArrayList<SnsData> snsDataArrayList = new ArrayList<>();
    ArrayList<DatData> datDataArrayList = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    DatAdapter datAdapter;
    String userID;
    String name;
    ListView listView;
    TextView likeText;
    TextView talkText;
    int likecount;
    static int datcount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_write);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        listView = findViewById(R.id.datlv);
        Log.d("WWW", "받아");
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        snsData = (SnsData)intent.getSerializableExtra("snsDB");
        userList=(ArrayList<User>)intent.getSerializableExtra("users");
        likecount=intent.getIntExtra("likecount",0);

        initDatDB();

        likeText=findViewById(R.id.likeText);
        talkText=findViewById(R.id.talkText);
        Button button = findViewById(R.id.datBtn);
        final EditText editText = findViewById(R.id.datEt);
        TextView textView = findViewById(R.id.datTv);
        textView.setText(snsData.getText());
        ImageView imageView=findViewById(R.id.datIv);
        if(!snsData.getImageurl().equals("")){
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(snsData.getImageurl()).into(imageView);
        }
        else{
            imageView.setVisibility(View.GONE);
        }
        likeText.setText(likecount+"");

        button.setOnClickListener(new View.OnClickListener() {
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
                databaseReference.child(name).push().setValue(new DatData(userID, editText.getText().toString(), formatDate));
                editText.setText("");
            }
        });

    }

    public void initDatDB() {
        Log.d("WWW", "받아와");
        name = snsData.getDate().toString() + "" + snsData.getTitle().toString() + "" + snsData.getId().toString();
        databaseReference.child(name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                datData = dataSnapshot.getValue(DatData.class);
                datDataArrayList.add(datData);
                datAdapter = new DatAdapter(datWriteActivity.this, R.layout.dat_item, datDataArrayList);
                datAdapter.setID(userID);
                datAdapter.setUser(userList);
                listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                listView.setAdapter(datAdapter);
                talkText.setText(datAdapter.getCount()+"");
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setSelection(datAdapter.getCount()-1);

                        Log.d("TTT", "내려가");
                    }
                });
                Log.d("WWW", "zz");
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
