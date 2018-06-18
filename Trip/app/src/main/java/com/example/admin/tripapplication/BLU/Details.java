package com.example.admin.tripapplication.BLU;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.tripapplication.R;

import java.io.InputStream;

public class Details extends AppCompatActivity {

    TextView name;
    TextView item;
    TextView mac;
    ImageView icon;

    ImageView s1;
    ImageView s2;
    ImageView s3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Intent intent = getIntent();
        MyList myList = (MyList)intent.getSerializableExtra("set");

        name = findViewById(R.id.name);
        item = findViewById(R.id.item);
        mac = findViewById(R.id.mac);
        icon = findViewById(R.id.icon);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);

        name.setText(myList.getName());
        item.setText(myList.getItem());
        mac.setText(myList.getMAC());
        icon.setImageResource(myList.getIcon());

        s1.setOnClickListener(event);
        s2.setOnClickListener(event);
        s3.setOnClickListener(event);

    }

    View.OnClickListener event = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.s1:
                    break;
                case R.id.s2:
                    break;
                case R.id.s3:
                    break;
            }
        }
    };
}
