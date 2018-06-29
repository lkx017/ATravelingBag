package com.example.admin.tripapplication.BLU;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.tripapplication.R;

public class Step04 extends Fragment {
    ImageView arrow1;
    TextView item;
    TextView name_MAC;
    EditText id;

    Button add;

    Step activity;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    ImageView profile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.step_04, container, false);
       activity = (Step)getActivity();


        profile = (ImageView)rootView.findViewById(R.id.icon);

        profile.setImageResource(activity.geticon());

        arrow1 = (ImageView)rootView.findViewById(R.id.arrow1);
        arrow1.setOnClickListener(event);

        id = (EditText)rootView.findViewById(R.id.id);
        id.setText("");

        item = (TextView)rootView.findViewById(R.id.item);
        item.setText(activity.getitem());

        name_MAC = (TextView)rootView.findViewById(R.id.name_mac);
        name_MAC.setText(activity.getname()+" ("+activity.getMAC()+") ");

        add=(Button)rootView.findViewById(R.id.add);
        add.setOnClickListener(event);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("알림").setMessage("설정을 완료하시겠습니까?");
        builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                activity.success();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();

        return rootView;
    }

    View.OnClickListener event =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.arrow1:
                    activity.onFragmentChanged(3);
                    break;
                case R.id.add:
                    activity.setid(id.getText()+"");
                    dialog.show();
                    break;
            }


        }
    };
}
