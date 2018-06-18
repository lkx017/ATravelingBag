package com.example.admin.tripapplication.BLU;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.admin.tripapplication.R;

public class Step extends AppCompatActivity {

    String id=null;
    String item=null;
    String name=null;
    String MAC=null;
   // BluetoothDevice devicesDiscovered;
    /////////////////////////////////////////////////

    Step01 step01;
    Step02 step02;
    Step03 step03;
    Step04 step04;

    FragmentManager fm;
    FragmentTransaction tran;

    int icon =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step);

        Log.d("Tiger", "onCreate: ");
        step01 = new Step01();
        step02 = new Step02();
        step03 = new Step03();
        step04 = new Step04();

        onFragmentChanged(1);
    }

    public void onFragmentChanged(int n){
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
            case 1:
                tran.replace(R.id.fragment_main, step01);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다
                tran.commit();
                break;
            case 2:
                tran.replace(R.id.fragment_main, step02);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
            case 3:
                tran.replace(R.id.fragment_main,step03);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
            case 4:
                tran.replace(R.id.fragment_main,step04);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
        }

    }

    public void setitem (String s){
        item = "";
        item = s;
    }

    public String getitem (){
        return item;
    }

    public String getname (){
        return name;
    }
    public String getMAC (){
        return MAC;
    }

    public void setdevice (String name, String MAC){
        this.name =name;
        this.MAC =MAC;
       // this.devicesDiscovered = devicesDiscovered;
    }

    public void setid (String s){
        id = s;
    }

    public void success (){
        Intent intent = new Intent();
        AttributeList attri = new AttributeList(id,item,MAC,icon);
        intent.putExtra("attribute", attri);

        setResult(501,intent);
        finish();
    }


    public int geticon (){
        switch (item){
            case "가방":
                icon= R.drawable.bag;
                break;
            case "지갑":
                icon = R.drawable.wallet;
                break;
            case "여권":
                icon = R.drawable.passport;
                break;
            case "열쇠":
                icon = R.drawable.key;
                break;
            case "애견":
                icon = R.drawable.dog;
                break;
            case "애묘":
                icon = R.drawable.cat;
                break;
            case "자녀":
                icon = R.drawable.children;
                break;
            case "쇼핑백":
                icon = R.drawable.shopping_bag;
                break;
            case "기타":
                icon = R.drawable.ble;
                break;

        }
         return icon;
    }



}
