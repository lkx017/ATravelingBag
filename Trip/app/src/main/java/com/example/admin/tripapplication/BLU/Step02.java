package com.example.admin.tripapplication.BLU;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.admin.tripapplication.R;

public class Step02 extends Fragment {

    private static final String TAG = "Tiger";
    ImageView arrow1;
    ImageView arrow2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.step_02, container, false);


        arrow1 = (ImageView)rootView.findViewById(R.id.arrow1);
        arrow2 = (ImageView)rootView.findViewById(R.id.arrow2);

        arrow1.setOnClickListener(event);
        arrow2.setOnClickListener(event);

        return rootView;
    }

    View.OnClickListener event =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Step activity = (Step)getActivity();

            switch (v.getId()){
                case R.id.arrow1:
                    activity.onFragmentChanged(1);
                    break;
                case  R.id.arrow2:
                    activity.onFragmentChanged(3);
                    break;
            }


        }
    };
}
