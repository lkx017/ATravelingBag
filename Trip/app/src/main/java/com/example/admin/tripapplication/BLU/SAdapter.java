package com.example.admin.tripapplication.BLU;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.tripapplication.R;

import java.util.ArrayList;

public class SAdapter extends RecyclerView.Adapter<SAdapter.ViewHolder> {

    private ArrayList<SensorList> mDataset;
    public static Context mContext;

    public Step activity;
    ViewGroup rootView;
    View v;
   // ArrayList<BluetoothDevice> devicesDiscovered;

    // Provide a suitable constructor (depends on the kind of dataset)
    //생성자
    public SAdapter(ArrayList<SensorList> myDataset , Context mContext, Step activity, ViewGroup rootView) {
        mDataset = myDataset;
      this.mContext = mContext;
     this.activity = activity;
     this.rootView = rootView;
     //this.devicesDiscovered = devicesDiscovered;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        // create a new view
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sensor_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name = null;
        private TextView MAC = null;
        private ProgressBar rssi = null;
        private CheckBox check = null;



        public ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            MAC = (TextView)view.findViewById(R.id.mac);
            rssi = (ProgressBar) view.findViewById(R.id.rssi);
            check =(CheckBox)view.findViewById(R.id.checkBox);

        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final SAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.name.setText(mDataset.get(position).getName());
        holder.MAC.setText(mDataset.get(position).getMAC());
        holder.rssi.setProgress(mDataset.get(position).getRssi());
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImageView arrow2 = rootView.findViewById(R.id.arrow2);

                if(isChecked==true){
                    activity.setdevice(holder.name.getText()+"",holder.MAC.getText()+"");
                    arrow2.setVisibility(View.VISIBLE);
                }else {
                    arrow2.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


