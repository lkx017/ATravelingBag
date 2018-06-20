package com.example.admin.tripapplication.BLU;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.fragment.BleFragment;

import java.util.ArrayList;

public class Details extends AppCompatActivity {

    TextView name;
    TextView item;
    TextView mac;
    ImageView icon;

    ImageView s1;
    ImageView s2;


    int position;
    int ricon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Intent intent = getIntent();
        MyList myList = (MyList)intent.getSerializableExtra("set");
        position = intent.getIntExtra("p",0);

        name = findViewById(R.id.name);
        item = findViewById(R.id.item);
        mac = findViewById(R.id.mac);
        icon = findViewById(R.id.icon);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);

        name.setText(myList.getName());
        item.setText(myList.getItem());
        mac.setText(myList.getMAC());
        icon.setImageResource(myList.getIcon());

        s1.setOnClickListener(event);
        s2.setOnClickListener(event);


    }

    View.OnClickListener event = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder ad = new AlertDialog.Builder(Details.this);
            final EditText et =  new EditText(Details.this);
            final ArrayList<String> items = new ArrayList<>();
            items.add("가방"); items.add("지갑"); items.add("여권"); items.add("열쇠"); items.add("애견");
            items.add("애묘"); items.add("자녀"); items.add("쇼핑백"); items.add("기타");
            final CharSequence[] itemss = items.toArray(new String[items.size()]);

            switch (v.getId()){
                case R.id.s1:
                    ad.setTitle("name");
                    et.setText(name.getText());
                    ad.setView(et);
                    ad.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           String n = et.getText()+"";
                           name.setText(n);
                            BleFragment.name_details(position,n);
                        }
                    });
                    ad.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                    break;
                case R.id.s2:
                    ad.setTitle("item");
                    ad.setItems(itemss, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                         String selectedText = itemss[which].toString();
                         item.setText(selectedText);
                         BleFragment.item_details(position,selectedText,geticon(selectedText));
                         icon.setImageResource(geticon(selectedText));

                        }
                    });
                    ad.show();
                    break;

            }
        }
    };

    public int geticon (String s){
        switch (s){
            case "가방":
                ricon= R.drawable.bag;
                break;
            case "지갑":
                ricon = R.drawable.wallet;
                break;
            case "여권":
                ricon = R.drawable.passport;
                break;
            case "열쇠":
                ricon = R.drawable.key;
                break;
            case "애견":
                ricon = R.drawable.dog;
                break;
            case "애묘":
                ricon = R.drawable.cat;
                break;
            case "자녀":
                ricon = R.drawable.children;
                break;
            case "쇼핑백":
                ricon = R.drawable.shopping_bag;
                break;
            case "기타":
                ricon = R.drawable.ble;
                break;

        }
        return ricon;
    }

}
