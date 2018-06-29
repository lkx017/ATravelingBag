package com.example.admin.tripapplication.BLU;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.admin.tripapplication.R;

public class Step01 extends Fragment {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Intent intent;
    Step activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.step_01, container,false);
        activity = (Step)getActivity();
        btn1 = (Button)rootview.findViewById(R.id.btn1);
        btn2 = (Button)rootview.findViewById(R.id.btn2);
        btn3 = (Button)rootview.findViewById(R.id.btn3);
        btn4 = (Button)rootview.findViewById(R.id.btn4);
        btn5 = (Button)rootview.findViewById(R.id.btn5);
        btn6 = (Button)rootview.findViewById(R.id.btn6);
        btn7 = (Button)rootview.findViewById(R.id.btn7);
        btn8 = (Button)rootview.findViewById(R.id.btn8);
        btn9 = (Button)rootview.findViewById(R.id.btn9);

        btn1.setOnClickListener(event);
        btn2.setOnClickListener(event);
        btn3.setOnClickListener(event);
        btn4.setOnClickListener(event);
        btn5.setOnClickListener(event);
        btn6.setOnClickListener(event);
        btn7.setOnClickListener(event);
        btn8.setOnClickListener(event);
        btn9.setOnClickListener(event);

        return rootview;
    }

   View.OnClickListener event =new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn1:
                    activity.setitem(btn1.getText()+"");
                    activity.onFragmentChanged(2);
                    break;
                case R.id.btn2:
                    activity.setitem(btn2.getText()+"");
                    activity.onFragmentChanged(2);
                    break;
                case R.id.btn3:
                    activity.setitem(btn3.getText()+"");
                    activity.onFragmentChanged(2);
                    break;
                case R.id.btn4:
                    activity.setitem(btn4.getText()+"");
                    activity.onFragmentChanged(2);
                    break;
                case R.id.btn5:
                    activity.setitem(btn5.getText()+"");
                    activity.onFragmentChanged(2);
                    break;
                case R.id.btn6:
                    activity.setitem(btn6.getText()+"");
                    activity.onFragmentChanged(2);
                    break;
                case R.id.btn7:
                    activity.setitem(btn7.getText()+"");
                    activity.onFragmentChanged(2);
                    break;
                case R.id.btn8:
                    activity.setitem(btn8.getText()+"");
                    activity.onFragmentChanged(2);
                    break;
                case R.id.btn9:
                    activity.setitem(btn9.getText()+"");
                    activity.onFragmentChanged(2);
                    break;

            }


        }
    };



}
